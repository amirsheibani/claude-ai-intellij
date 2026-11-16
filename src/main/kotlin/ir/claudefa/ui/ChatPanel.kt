package ir.claudefa.ui

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.ide.BrowserUtil
import com.intellij.ide.ui.LafManagerListener
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.JBColor
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefJSQuery
import com.intellij.util.ui.UIUtil
import ir.claudefa.cli.ClaudeCliRunner
import ir.claudefa.settings.ClaudeFaSettings
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter
import java.awt.Color
import java.awt.datatransfer.StringSelection
import java.io.File
import java.util.Base64
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.JComponent

class ChatPanel(private val project: Project) : Disposable {

    private val gson = Gson()
    private val browser: JBCefBrowser = JBCefBrowser.createBuilder().setOffScreenRendering(false).build()
    private val jsQuery: JBCefJSQuery = JBCefJSQuery.create(browser as JBCefBrowserBase)

    @Volatile
    private var pageReady = false
    private val pending = ConcurrentLinkedQueue<String>()
    private val lock = Any()

    private val runner = ClaudeCliRunner(project.basePath, object : ClaudeCliRunner.Listener {
        override fun onEvent(event: JsonObject) = post("claude", event)
        override fun onStderr(line: String) = post("stderr", JsonObject().apply { addProperty("text", line) })
        override fun onExit(exitCode: Int, gotResult: Boolean) = post("exit", JsonObject().apply {
            addProperty("code", exitCode)
            addProperty("gotResult", gotResult)
        })
        override fun onStartFailed(message: String) = post("error", JsonObject().apply { addProperty("text", message) })
    })

    val component: JComponent get() = browser.component

    init {
        Disposer.register(this, browser)
        Disposer.register(this, jsQuery)

        jsQuery.addHandler { raw ->
            try {
                handleFromJs(JsonParser.parseString(raw).asJsonObject)
            } catch (e: Exception) {
                post("error", JsonObject().apply { addProperty("text", "خطای داخلی: ${e.message}") })
            }
            null
        }

        browser.jbCefClient.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(cefBrowser: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
                if (!frame.isMain) return
                cefBrowser.executeJavaScript(
                    "window.__toIde = function(m){ ${jsQuery.inject("m")} };",
                    cefBrowser.url, 0,
                )
                synchronized(lock) {
                    pageReady = true
                    while (true) {
                        val js = pending.poll() ?: break
                        cefBrowser.executeJavaScript(js, cefBrowser.url, 0)
                    }
                }
                sendInit()
            }
        }, browser.cefBrowser)

        ApplicationManager.getApplication().messageBus.connect(this)
            .subscribe(LafManagerListener.TOPIC, LafManagerListener { sendInit() })

        browser.loadHTML(buildHtml())
    }

    // ---------------------------------------------------------------- public API (used by actions)

    /** Inserts text into the chat input box (does not send). */
    fun insertIntoInput(text: String) = post("insertInput", JsonObject().apply { addProperty("text", text) })

    /** Sends a prompt immediately, as if the user typed it. */
    fun sendPrompt(text: String) = post("externalSend", JsonObject().apply { addProperty("text", text) })

    // ---------------------------------------------------------------- JS -> IDE

    private fun handleFromJs(msg: JsonObject) {
        when (msg.get("type")?.asString) {
            "send" -> {
                val text = msg.get("text")?.asString.orEmpty()
                if (text.isBlank()) return
                if (runner.isRunning) {
                    post("error", JsonObject().apply { addProperty("text", "پاسخ قبلی هنوز در حال اجراست.") })
                    return
                }
                sendInit()
                runner.send(text)
            }
            "stop" -> runner.stop()
            "new" -> {
                runner.resetSession()
                post("cleared", JsonObject())
            }
            "copy" -> {
                CopyPasteManager.getInstance().setContents(StringSelection(msg.get("text")?.asString.orEmpty()))
            }
            "openUrl" -> msg.get("url")?.asString?.takeIf { it.startsWith("http") }?.let { BrowserUtil.browse(it) }
            "insertCode" -> insertAtCaret(msg.get("text")?.asString.orEmpty())
            "openFile" -> openFile(msg.get("path")?.asString.orEmpty(), msg.get("line")?.asInt ?: 0)
            "settings" -> ApplicationManager.getApplication().invokeLater {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, "Claude Farsi")
                sendInit()
            }
        }
    }

    private fun insertAtCaret(text: String) {
        ApplicationManager.getApplication().invokeLater {
            val editor = FileEditorManager.getInstance(project).selectedTextEditor
            if (editor == null) {
                notify("هیچ ویرایشگری باز نیست.", NotificationType.WARNING)
                return@invokeLater
            }
            WriteCommandAction.runWriteCommandAction(project, "Insert Code from Claude", null, {
                val caret = editor.caretModel.primaryCaret
                if (caret.hasSelection()) {
                    editor.document.replaceString(caret.selectionStart, caret.selectionEnd, text)
                } else {
                    editor.document.insertString(caret.offset, text)
                }
            })
        }
    }

    private fun openFile(path: String, line: Int) {
        if (path.isBlank()) return
        ApplicationManager.getApplication().invokeLater {
            val f = File(path).let { if (it.isAbsolute) it else File(project.basePath ?: "", path) }
            val vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(f) ?: return@invokeLater
            OpenFileDescriptor(project, vf, maxOf(0, line - 1), 0).navigate(true)
        }
    }

    // ---------------------------------------------------------------- IDE -> JS

    private fun sendInit() {
        val s = ClaudeFaSettings.get()
        post("init", JsonObject().apply {
            addProperty("dark", !JBColor.isBright())
            addProperty("bg", hex(UIUtil.getPanelBackground()))
            addProperty("fg", hex(UIUtil.getLabelForeground()))
            addProperty("fontSize", s.fontSize)
            addProperty("sendOnEnter", s.sendOnEnter)
            addProperty("projectPath", project.basePath ?: "")
            addProperty("permissionMode", s.permissionMode)
        })
    }

    private fun post(kind: String, payload: JsonObject) {
        val js = "window.bridge && window.bridge.receive(${gson.toJson(kind)}, ${gson.toJson(payload)});"
        synchronized(lock) {
            if (!pageReady) {
                pending.add(js)
                return
            }
        }
        browser.cefBrowser.executeJavaScript(js, browser.cefBrowser.url, 0)
    }

    private fun notify(text: String, type: NotificationType) {
        NotificationGroupManager.getInstance().getNotificationGroup("Claude Farsi")
            .createNotification(text, type).notify(project)
    }

    private fun buildHtml(): String {
        fun res(name: String): ByteArray =
            ChatPanel::class.java.getResourceAsStream("/webview/$name")?.use { it.readBytes() } ?: ByteArray(0)

        val html = String(res("chat.html"), Charsets.UTF_8)
        val css = String(res("chat.css"), Charsets.UTF_8)
        val js = String(res("chat.js"), Charsets.UTF_8)
        val font = Base64.getEncoder().encodeToString(res("Vazirmatn.woff2"))
        return html
            .replace("/*__CSS__*/", css.replace("__FONT_BASE64__", font))
            .replace("/*__JS__*/", js)
    }

    private fun hex(c: Color) = String.format("#%02x%02x%02x", c.red, c.green, c.blue)

    override fun dispose() {
        runner.stop()
        ChatPanelHolder.getInstance(project).let { if (it.panel === this) it.panel = null }
    }
}
