# Graph Report - claude-fa-intellij  (2026-11-16)

## Corpus Check
- 25 files · ~22,259 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 9 file(s) not represented in the graph (top: .properties 2, .xml 2, (none) 1)

## Summary
- 513 nodes · 1128 edges · 29 communities (8 shown, 21 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 31 edges (avg confidence: 0.85)
- Token cost: 45,394 input · 0 output

## Graph Freshness
- Built from commit: `bfeed6fe`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ChatPanel
- Webview Chat Renderer (JS)
- Plugin Concepts & Docs
- ProjectFileSearch
- SelectionActions.kt
- DependencyScanner.kt
- Settings UI Configurable
- IdeMcpServer.kt
- DiffPreview.kt
- SelectionTracker.kt
- PermissionBroker
- IdeTools.kt
- CLAUDE.md
- file
- Guard

## God Nodes (most connected - your core abstractions)
1. `ChatPanel` - 40 edges
2. `receive()` - 26 edges
3. `chat()` - 22 edges
4. `esc()` - 21 edges
5. `IdeMcpServer` - 19 edges
6. `toIde()` - 18 edges
7. `PluginLog` - 17 edges
8. `PermissionBroker` - 17 edges
9. `renderInfo()` - 16 edges
10. `SelectionTracker` - 15 edges

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

## Communities (29 total, 21 thin omitted)

### Community 0 - "ChatPanel"
Cohesion: 0.06
Nodes (7): ClaudeCliRunner, Listener, CliInfo, Line, PluginLog, ChatPanel, Listener

### Community 1 - "Webview Chat Renderer (JS)"
Cohesion: 0.06
Nodes (106): acceptMention(), acceptSlash(), activate(), addTool(), addUser(), agentFor(), alertNotice(), attachMenu() (+98 more)

### Community 2 - "Plugin Concepts & Docs"
Cohesion: 0.07
Nodes (24): ChatPanel.kt (JCEF browser + JS-Kotlin bridge), Claude Code CLI (@anthropic-ai/claude-code), Claude Farsi IntelliJ Plugin, Code Block Copy / Insert-into-Editor Buttons, Editor Context Menu Actions (Send Selection / Explain / Review), Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems), Gradle Build (buildPlugin / runIde), Headless Claude CLI (claude -p --output-format stream-json) (+16 more)

### Community 5 - "SelectionActions.kt"
Cohesion: 0.11
Nodes (7): ExplainSelectionAction, OpenChatAction, ReviewSelectionAction, SelectionToClaudeAction, SendSelectionAction, ChatPanelHolder, ChatToolWindowFactory

### Community 13 - "SelectionTracker.kt"
Cohesion: 0.13
Nodes (5): SelectionTracker, CaretListener, FileEditorManagerListener, SelectionListener, Snapshot

### Community 14 - "PermissionBroker"
Cohesion: 0.13
Nodes (6): Allow, Decision, Deny, Pending, PermissionBroker, Ui

### Community 19 - "file"
Cohesion: 0.25
Nodes (3): GitInfo, Command, SlashCommands

### Community 23 - "Guard"
Cohesion: 0.18
Nodes (6): Action, BLOCK, WARN, Decision, Guard, Rule

## Knowledge Gaps
- **8 isolated node(s):** `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)`, `Code Block Copy / Insert-into-Editor Buttons`, `Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems)` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 111 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **21 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ChatPanel` connect `ChatPanel` to `ChatPanel.kt`, `SelectionActions.kt`, `DependencyScanner.kt`, `SelectionTracker.kt`, `PermissionBroker`?**
  _High betweenness centrality (0.087) - this node is a cross-community bridge._
- **What connects `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ChatPanel` be split into smaller, more focused modules?**
  _Cohesion score 0.05536568694463431 - nodes in this community are weakly interconnected._
- **Why does `IdeMcpServer` connect `IdeMcpServer.kt` to `Plugin Concepts & Docs`, `ChatPanel.kt`, `IdeTools.kt`?**
  _High betweenness centrality (0.056) - this node is a cross-community bridge._
- **Should `Webview Chat Renderer (JS)` be split into smaller, more focused modules?**
  _Cohesion score 0.060574593284873655 - nodes in this community are weakly interconnected._
- **Why does `PluginLog` connect `ChatPanel` to `Plugin Concepts & Docs`, `ChatPanel.kt`, `DependencyScanner.kt`, `PermissionBroker`, `Guard`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **Should `Plugin Concepts & Docs` be split into smaller, more focused modules?**
  _Cohesion score 0.06747638326585695 - nodes in this community are weakly interconnected._