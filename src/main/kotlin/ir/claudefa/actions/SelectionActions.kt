package ir.claudefa.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.project.guessProjectDir
import ir.claudefa.ui.ChatToolWindowFactory

/** Base for editor actions that turn the current selection into a prompt. */
abstract class SelectionToClaudeAction : AnAction(), DumbAware {

    /** Persian instruction placed before the code. Null = only insert into the input box. */
    protected abstract val instruction: String?

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null && e.getData(CommonDataKeys.EDITOR) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        val sel = editor.selectionModel
        val doc = editor.document
        val hasSel = sel.hasSelection()
        val code = if (hasSel) sel.selectedText.orEmpty() else doc.text
        val startLine = if (hasSel) doc.getLineNumber(sel.selectionStart) + 1 else 1
        val endLine = if (hasSel) doc.getLineNumber(maxOf(sel.selectionStart, sel.selectionEnd - 1)) + 1 else doc.lineCount

        val baseDir = project.guessProjectDir()
        val relPath = when {
            file == null -> "untitled"
            baseDir != null -> VfsUtilCore.getRelativePath(file, baseDir) ?: file.path
            else -> file.path
        }
        val lang = file?.extension.orEmpty()

        val ref = "@$relPath (خطوط $startLine تا $endLine)"
        val block = "```$lang\n$code\n```"
        val prompt = buildString {
            instruction?.let { append(it).append("\n\n") }
            append(ref).append("\n").append(block).append("\n")
        }

        ChatToolWindowFactory.withPanel(project) { panel ->
            if (instruction == null) panel.insertIntoInput(prompt) else panel.sendPrompt(prompt)
        }
    }
}

class SendSelectionAction : SelectionToClaudeAction() {
    override val instruction: String? = null
}

class ExplainSelectionAction : SelectionToClaudeAction() {
    override val instruction = "این کد را به فارسی و قدم‌به‌قدم توضیح بده:"
}

class ReviewSelectionAction : SelectionToClaudeAction() {
    override val instruction =
        "این کد را بررسی کن: باگ‌ها، مشکلات امنیتی و کارایی را پیدا کن و پیشنهاد بهبود بده (فعلاً فایل را تغییر نده):"
}

class OpenChatAction : AnAction(), DumbAware {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ChatToolWindowFactory.withPanel(project) { }
    }
}
