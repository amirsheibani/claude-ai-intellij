package ir.claudefa.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "ClaudeFaSettings", storages = [Storage("claude-fa.xml")])
class ClaudeFaSettings : PersistentStateComponent<ClaudeFaSettings.State> {

    class State {
        /** Path to the `claude` executable. Empty = auto-detect. */
        var cliPath: String = ""
        /** Empty = Claude Code default model. e.g. "sonnet", "opus". */
        var model: String = ""
        /** default | acceptEdits | plan | bypassPermissions */
        var permissionMode: String = "acceptEdits"
        /** Ask Claude to answer in Persian. */
        var respondInPersian: Boolean = true
        /** Replace Arabic ي/ك with Persian ی/ک before sending. */
        var normalizeArabicChars: Boolean = true
        /** Optional HTTPS proxy passed to the CLI, e.g. http://127.0.0.1:10809 */
        var httpsProxy: String = ""
        /** Extra CLI arguments, e.g. --add-dir ../shared */
        var extraArgs: String = ""
        var fontSize: Int = 14
        var sendOnEnter: Boolean = true
    }

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, myState)
    }

    companion object {
        fun get(): State =
            ApplicationManager.getApplication().getService(ClaudeFaSettings::class.java).state
    }
}
