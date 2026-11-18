package ir.claudefa.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import ir.claudefa.I18n.tr

class ClaudeFaConfigurable : BoundConfigurable("ClaudeFA") {

    private val s get() = ClaudeFaSettings.get()

    override fun createPanel(): DialogPanel = panel {
        group(tr("Connection to Claude Code", "اتصال به Claude Code")) {
            row(tr("claude CLI path:", "مسیر claude CLI:")) {
                textField()
                    .align(AlignX.FILL)
                    .bindText(s::cliPath)
                    .comment(
                        tr(
                            "Empty = auto-detect (PATH, ~/.local/bin, ~/.claude/local, ...)",
                            "خالی = تشخیص خودکار (PATH، ‎~/.local/bin، ‎~/.claude/local و ...)",
                        )
                    )
            }
            row(tr("Model:", "مدل:")) {
                textField()
                    .bindText(s::model)
                    .comment(tr("Empty = default model. Example: sonnet or opus", "خالی = مدل پیش‌فرض. مثال: sonnet یا opus"))
            }
            row(tr("Permission mode:", "حالت دسترسی:")) {
                comboBox(PERMISSION_MODES)
                    .bindItem({ s.permissionMode }, { s.permissionMode = it ?: "default" })
                    .comment(
                        tr(
                            "default: asks before every edit and command · acceptEdits: edits files without asking · " +
                                "plan: plan only, no changes · bypassPermissions: everything allowed (use with care)",
                            "default: برای هر ویرایش و دستور از شما اجازه گرفته می‌شود · acceptEdits: ویرایش فایل‌ها بدون پرسش · " +
                                "plan: فقط برنامه‌ریزی، بدون تغییر · bypassPermissions: همه چیز مجاز (با احتیاط)",
                        )
                    )
            }
            row {
                checkBox(tr("Open proposed changes in the Diff Viewer automatically", "نمایش خودکار تغییرات پیشنهادی در Diff Viewer"))
                    .bindSelected(s::autoOpenDiff)
            }
            row {
                checkBox(tr("Share the open file and selected text with Claude", "اشتراک خودکار فایل باز و متن انتخاب‌شده با Claude"))
                    .bindSelected(s::shareSelection)
            }
            row(tr("HTTPS proxy:", "پراکسی HTTPS:")) {
                textField()
                    .align(AlignX.FILL)
                    .bindText(s::httpsProxy)
                    .comment(tr("Optional, e.g. http://127.0.0.1:10809", "اختیاری، مثال: http://127.0.0.1:10809"))
            }
            row(tr("Extra arguments:", "آرگومان‌های اضافه:")) {
                textField()
                    .align(AlignX.FILL)
                    .bindText(s::extraArgs)
                    .comment(tr("Example: --add-dir ../shared", "مثال: --add-dir ../shared"))
            }
        }
        group(tr("Language and Appearance", "زبان و ظاهر")) {
            row(tr("Interface language:", "زبان رابط کاربری:")) {
                comboBox(LANGUAGES, SimpleListCellRenderer.create("") { if (it == "fa") "فارسی (Persian)" else "English" })
                    .bindItem({ s.language }, { s.language = it ?: "en" })
                    .comment(tr("Also switchable from the globe button next to Send.", "از دکمهٔ کره کنار «ارسال» هم قابل تغییر است."))
            }
            row {
                checkBox(
                    tr(
                        "Reply in the language I write in (Persian or English)",
                        "پاسخ به همان زبانی باشد که می‌نویسم (فارسی یا انگلیسی)",
                    )
                ).bindSelected(s::matchReplyLanguage)
            }
            row {
                checkBox(tr("Convert Arabic \"ي\" and \"ك\" to Persian \"ی\" and \"ک\"", "تبدیل «ي» و «ك» عربی به «ی» و «ک» فارسی"))
                    .bindSelected(s::normalizeArabicChars)
            }
            row {
                checkBox(tr("Send with Enter (new line with Shift+Enter)", "ارسال با Enter (خط جدید با Shift+Enter)"))
                    .bindSelected(s::sendOnEnter)
            }
            row(tr("Font size:", "اندازه فونت:")) {
                intTextField(10..28).bindIntText(s::fontSize)
            }
        }
    }

    companion object {
        val PERMISSION_MODES = listOf("default", "acceptEdits", "plan", "bypassPermissions")
        val LANGUAGES = listOf("en", "fa")
    }
}
