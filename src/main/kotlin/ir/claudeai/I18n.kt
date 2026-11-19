
package ir.claudeai

import com.intellij.openapi.application.ApplicationManager
import ir.claudeai.settings.ClaudeAiSettings

/** UI language of the plugin: "en" (default) or "fa". The chat page has its own dictionary in chat.js. */
object I18n {
    val lang: String
        get() = runCatching {
            if (ApplicationManager.getApplication() == null) "en" else ClaudeAiSettings.get().language
        }.getOrDefault("en").let { if (it == "fa") "fa" else "en" }

    val isFa: Boolean get() = lang == "fa"

    /** Picks the string for the current UI language. */
    fun tr(en: String, fa: String): String = if (isFa) fa else en
}
