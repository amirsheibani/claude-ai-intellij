# Graph Report - claude-fa-intellij  (2026-11-16)

## Corpus Check
- 20 files · ~14,829 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 9 file(s) not represented in the graph (top: .properties 2, .xml 2, (none) 1)

## Summary
- 379 nodes · 709 edges · 25 communities (5 shown, 20 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 11 edges (avg confidence: 0.86)
- Token cost: 45,394 input · 0 output

## Graph Freshness
- Built from commit: `bfeed6fe`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- JCEF Chat Panel Bridge
- Webview Chat Renderer (JS)
- Plugin Concepts & Docs
- ProjectFileSearch
- ClaudeFaSettings
- SelectionActions.kt
- Claude CLI Runner API
- Settings UI Configurable
- IdeMcpServer.kt
- DiffPreview.kt
- SelectionTracker.kt
- PermissionBroker
- IdeTools.kt
- CLAUDE.md
- SlashCommands

## God Nodes (most connected - your core abstractions)
1. `ChatPanel` - 28 edges
2. `receive()` - 17 edges
3. `IdeMcpServer` - 16 edges
4. `SelectionTracker` - 15 edges
5. `PermissionBroker` - 15 edges
6. `IdeTools` - 15 edges
7. `DiffPreview` - 13 edges
8. `ClaudeFaSettings` - 11 edges
9. `esc()` - 11 edges
10. `ClaudeCliRunner` - 10 edges

## Surprising Connections (you probably didn't know these)
- `chat.html Webview Template` --implements--> `RTL / Bidi Rendering (unicode-bidi: plaintext)`  [INFERRED]
  src/main/resources/webview/chat.html → README.md
- `ChatPanel.kt (JCEF browser + JS-Kotlin bridge)` --references--> `chat.html Webview Template`  [INFERRED]
  README.md → src/main/resources/webview/chat.html
- `Thinking Status Indicator` --conceptually_related_to--> `Live Streaming Response and Tool Use Display`  [INFERRED]
  src/main/resources/webview/chat.html → README.md
- `Topbar (status dot, New Chat, Settings buttons)` --implements--> `Conversation Resume (--resume) and New Chat`  [INFERRED]
  src/main/resources/webview/chat.html → README.md
- `Messages Area with Empty State and Suggestions` --references--> `Editor Context Menu Actions (Send Selection / Explain / Review)`  [EXTRACTED]
  src/main/resources/webview/chat.html → README.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Chat webview UI regions** — src_main_resources_webview_chat_topbar, src_main_resources_webview_chat_messages, src_main_resources_webview_chat_status, src_main_resources_webview_chat_composer [EXTRACTED 1.00]
- **CLI runner -> JCEF ChatPanel -> chat.html webview pipeline** — src_main_kotlin_ir_claudefa_cli_claudeclirunner, readme_chatpanel, src_main_kotlin_ir_claudefa_ui_chattoolwindowfactory, src_main_resources_webview_chat_chat_html [INFERRED 0.85]
- **Persian text rendering support** — readme_rtl_bidi_rendering, readme_vazirmatn_font, readme_jcef_html_panel, src_main_resources_webview_vazirmatn_ofl_sil_open_font_license [INFERRED 0.85]

## Communities (25 total, 20 thin omitted)

### Community 1 - "Webview Chat Renderer (JS)"
Cohesion: 0.09
Nodes (58): acceptMention(), acceptSlash(), addTool(), addUser(), autoGrow(), closeMention(), closeSlash(), codeBlock() (+50 more)

### Community 2 - "Plugin Concepts & Docs"
Cohesion: 0.11
Nodes (22): ChatPanel.kt (JCEF browser + JS-Kotlin bridge), Claude Code CLI (@anthropic-ai/claude-code), Claude Farsi IntelliJ Plugin, Code Block Copy / Insert-into-Editor Buttons, Editor Context Menu Actions (Send Selection / Explain / Review), Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems), Gradle Build (buildPlugin / runIde), Headless Claude CLI (claude -p --output-format stream-json) (+14 more)

### Community 5 - "SelectionActions.kt"
Cohesion: 0.09
Nodes (7): ExplainSelectionAction, OpenChatAction, ReviewSelectionAction, SelectionToClaudeAction, SendSelectionAction, ChatPanelHolder, ChatToolWindowFactory

### Community 13 - "SelectionTracker.kt"
Cohesion: 0.13
Nodes (5): SelectionTracker, CaretListener, FileEditorManagerListener, SelectionListener, Snapshot

### Community 14 - "PermissionBroker"
Cohesion: 0.14
Nodes (6): Allow, Decision, Deny, Pending, PermissionBroker, Ui

## Knowledge Gaps
- **6 isolated node(s):** `Composer (RTL textarea, Send/Stop buttons)`, `Code Block Copy / Insert-into-Editor Buttons`, `Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems)`, `Gradle Build (buildPlugin / runIde)`, `CSS/JS Injection Placeholders (/*__CSS__*/, /*__JS__*/)` (+1 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 100 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **20 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ChatPanel` connect `JCEF Chat Panel Bridge` to `SelectionActions.kt`, `SelectionTracker.kt`, `Claude CLI Runner API`, `PermissionBroker`?**
  _High betweenness centrality (0.100) - this node is a cross-community bridge._
- **What connects `Composer (RTL textarea, Send/Stop buttons)`, `Code Block Copy / Insert-into-Editor Buttons`, `Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems)` to the rest of the system?**
  _6 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `JCEF Chat Panel Bridge` be split into smaller, more focused modules?**
  _Cohesion score 0.07308970099667775 - nodes in this community are weakly interconnected._
- **Why does `ClaudeCliRunner` connect `Claude CLI Runner API` to `JCEF Chat Panel Bridge`, `IdeMcpServer.kt`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Should `Webview Chat Renderer (JS)` be split into smaller, more focused modules?**
  _Cohesion score 0.09209039548022599 - nodes in this community are weakly interconnected._
- **Why does `SelectionTracker` connect `SelectionTracker.kt` to `JCEF Chat Panel Bridge`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Should `Plugin Concepts & Docs` be split into smaller, more focused modules?**
  _Cohesion score 0.1067193675889328 - nodes in this community are weakly interconnected._