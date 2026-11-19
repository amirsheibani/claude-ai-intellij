# Graph Report - claude-fa-intellij  (2026-11-16)

## Corpus Check
- 27 files · ~29,241 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 9 file(s) not represented in the graph (top: .properties 2, .xml 2, (none) 1)

## Summary
- 548 nodes · 1258 edges · 36 communities (6 shown, 30 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 39 edges (avg confidence: 0.85)
- Token cost: 45,394 input · 0 output

## Graph Freshness
- Built from commit: `27045bee`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ChatPanel
- Webview Chat Renderer (JS)
- Plugin Concepts & Docs
- SelectionActions.kt
- DependencyScanner.kt
- Settings UI Configurable
- IdeMcpServer.kt
- DiffPreview.kt
- SelectionTracker.kt
- PermissionBroker
- IdeTools.kt
- CLAUDE.md
- .send
- ClaudeCliRunner.kt
- .overview
- ClaudeFaSettings
- SlashCommands

## God Nodes (most connected - your core abstractions)
1. `ChatPanel` - 41 edges
2. `receive()` - 30 edges
3. `chat()` - 24 edges
4. `esc()` - 24 edges
5. `toIde()` - 20 edges
6. `renderInfo()` - 19 edges
7. `PluginLog` - 17 edges
8. `IdeMcpServer` - 17 edges
9. `textDir()` - 16 edges
10. `PermissionBroker` - 16 edges

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

## Communities (36 total, 30 thin omitted)

### Community 0 - "ChatPanel"
Cohesion: 0.06
Nodes (6): ClaudeConfig, CliInfo, Line, PluginLog, ChatPanel, Listener

### Community 1 - "Webview Chat Renderer (JS)"
Cohesion: 0.05
Nodes (123): acceptMention(), acceptSlash(), activate(), addTool(), addUser(), agentFor(), alertNotice(), attachMenu() (+115 more)

### Community 2 - "Plugin Concepts & Docs"
Cohesion: 0.11
Nodes (22): ChatPanel.kt (JCEF browser + JS-Kotlin bridge), Claude Code CLI (@anthropic-ai/claude-code), Claude Farsi IntelliJ Plugin, Code Block Copy / Insert-into-Editor Buttons, Editor Context Menu Actions (Send Selection / Explain / Review), Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems), Gradle Build (buildPlugin / runIde), Headless Claude CLI (claude -p --output-format stream-json) (+14 more)

### Community 5 - "SelectionActions.kt"
Cohesion: 0.10
Nodes (7): ExplainSelectionAction, OpenChatAction, ReviewSelectionAction, SelectionToClaudeAction, SendSelectionAction, ChatPanelHolder, ChatToolWindowFactory

### Community 13 - "SelectionTracker.kt"
Cohesion: 0.13
Nodes (5): SelectionTracker, CaretListener, FileEditorManagerListener, SelectionListener, Snapshot

### Community 14 - "PermissionBroker"
Cohesion: 0.09
Nodes (12): Action, BLOCK, WARN, Decision, Guard, Rule, Allow, Decision (+4 more)

## Knowledge Gaps
- **8 isolated node(s):** `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)`, `Code Block Copy / Insert-into-Editor Buttons`, `Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems)` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 118 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **30 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ChatPanel` connect `ChatPanel` to `ChatPanel.kt`, `SelectionActions.kt`, `DependencyScanner.kt`, `SelectionTracker.kt`, `PermissionBroker`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **What connects `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `ChatPanel` be split into smaller, more focused modules?**
  _Cohesion score 0.06409130816505706 - nodes in this community are weakly interconnected._
- **Why does `I18n` connect `ClaudeCliRunner.kt` to `ChatPanel.kt`, `SelectionActions.kt`, `DiffPreview.kt`, `Guard.kt`, `.overview`?**
  _High betweenness centrality (0.067) - this node is a cross-community bridge._
- **Should `Webview Chat Renderer (JS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05422222222222222 - nodes in this community are weakly interconnected._
- **Why does `PluginLog` connect `ChatPanel` to `ChatPanel.kt`, `DependencyScanner.kt`, `PermissionBroker`, `ClaudeCliRunner.kt`, `Guard.kt`, `file`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **Should `Plugin Concepts & Docs` be split into smaller, more focused modules?**
  _Cohesion score 0.1067193675889328 - nodes in this community are weakly interconnected._