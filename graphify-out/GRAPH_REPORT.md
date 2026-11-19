# Graph Report - claude-fa-intellij  (2026-11-19)

## Corpus Check
- 36 files · ~31,744 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 9 file(s) not represented in the graph (top: .properties 2, .xml 2, (none) 1)

## Summary
- 628 nodes · 1294 edges · 86 communities (11 shown, 75 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 53 edges (avg confidence: 0.87)
- Token cost: 45,394 input · 0 output

## Graph Freshness
- Built from commit: `366282b9`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Webview Chat Renderer (JS)
- Plugin Concepts & Docs
- ChatPanel.kt
- .tr
- DependencyScanner.kt
- Settings UI Configurable
- DiffPreview.kt
- SelectionTracker.kt
- IdeTools.kt
- CLAUDE.md
- IdeMcpServer.kt
- ClaudeCliRunner.kt
- .decide
- .send
- Guard
- .add
- Task workflow: plan → approve → delegate → review → document
- Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)
- Confirm before edit
- plugin-git.md
- debug-hypotheses/SKILL.md
- explore-feature/SKILL.md
- senior-review/SKILL.md

## God Nodes (most connected - your core abstractions)
1. `ChatPanel` - 41 edges
2. `receive()` - 32 edges
3. `esc()` - 26 edges
4. `chat()` - 24 edges
5. `PluginLog` - 20 edges
6. `toIde()` - 20 edges
7. `IdeMcpServer` - 19 edges
8. `renderInfo()` - 19 edges
9. `PermissionBroker` - 17 edges
10. `textDir()` - 16 edges

## Surprising Connections (you probably didn't know these)
- `3. Delegate to cheaper coding models` --references--> `PluginLog`  [INFERRED]
  .claude/rules/plugin-agent-workflow.md → src/main/kotlin/ir/claudeai/PluginLog.kt
- `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` --references--> `PluginLog`  [INFERRED]
  .claude/rules/plugin-donts.md → src/main/kotlin/ir/claudeai/PluginLog.kt
- `Implement after confirm` --references--> `PluginLog`  [INFERRED]
  .claude/skills/implement/SKILL.md → src/main/kotlin/ir/claudeai/PluginLog.kt
- `1. Backend / state` --references--> `State`  [INFERRED]
  .claude/skills/scaffold-feature/SKILL.md → src/main/kotlin/ir/claudeai/settings/ClaudeAiSettings.kt
- `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` --references--> `esc()`  [INFERRED]
  .claude/rules/plugin-donts.md → src/main/resources/webview/chat.js

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Chat webview UI regions** — src_main_resources_webview_chat_topbar, src_main_resources_webview_chat_messages, src_main_resources_webview_chat_status, src_main_resources_webview_chat_composer [EXTRACTED 1.00]
- **Persian text rendering support** — readme_rtl_bidi_rendering, readme_vazirmatn_font, readme_jcef_html_panel, src_main_resources_webview_vazirmatn_ofl_sil_open_font_license [INFERRED 0.85]

## Communities (86 total, 75 thin omitted)

### Community 1 - "Webview Chat Renderer (JS)"
Cohesion: 0.05
Nodes (124): 4. Webview, acceptMention(), acceptSlash(), activate(), addTool(), addUser(), agentFor(), alertNotice() (+116 more)

### Community 2 - "Plugin Concepts & Docs"
Cohesion: 0.11
Nodes (22): ChatPanel.kt (JCEF browser + JS-Kotlin bridge), Claude Code CLI (@anthropic-ai/claude-code), Claude Farsi IntelliJ Plugin, Code Block Copy / Insert-into-Editor Buttons, Editor Context Menu Actions (Send Selection / Explain / Review), Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems), Gradle Build (buildPlugin / runIde), Headless Claude CLI (claude -p --output-format stream-json) (+14 more)

### Community 4 - "ChatPanel.kt"
Cohesion: 0.05
Nodes (3): CliInfo, ChatPanel, Listener

### Community 5 - ".tr"
Cohesion: 0.09
Nodes (7): ExplainSelectionAction, OpenChatAction, ReviewSelectionAction, SelectionToClaudeAction, SendSelectionAction, ChatPanelHolder, ChatToolWindowFactory

### Community 13 - "SelectionTracker.kt"
Cohesion: 0.09
Nodes (6): ProjectFileSearch, SelectionTracker, CaretListener, FileEditorManagerListener, SelectionListener, Snapshot

### Community 22 - "ClaudeCliRunner.kt"
Cohesion: 0.06
Nodes (14): 1. Backend / state, 2. IDE integration, 3. Bridge (`ui/ChatPanel.kt`), 5. Registration, Build order, New feature checklist, New feature scaffold, ClaudeConfig (+6 more)

### Community 25 - ".decide"
Cohesion: 0.20
Nodes (6): Allow, Decision, Deny, Pending, PermissionBroker, Ui

### Community 29 - "Guard"
Cohesion: 0.29
Nodes (6): Action, BLOCK, WARN, Decision, Guard, Rule

### Community 36 - ".add"
Cohesion: 0.25
Nodes (3): Implement after confirm, Line, PluginLog

### Community 37 - "Task workflow: plan → approve → delegate → review → document"
Cohesion: 0.25
Nodes (7): 1. Analyze & plan (strong model), 2. Explain the plan and wait for approval, 3. Delegate to cheaper coding models, 4. Review the subagents' code, 5. Document the work, 6. Report, Task workflow: plan → approve → delegate → review → document

### Community 38 - "Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)"
Cohesion: 0.50
Nodes (3): Build, Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview), Preferred instead

## Knowledge Gaps
- **9 isolated node(s):** `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)`, `Claude Code CLI (@anthropic-ai/claude-code)`, `Code Block Copy / Insert-into-Editor Buttons` (+4 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 189 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **75 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` connect `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` to `Webview Chat Renderer (JS)`, `.add`, `ChatPanel.kt`?**
  _High betweenness centrality (0.261) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `receive()` (e.g. with `4. Webview` and `New feature scaffold`) actually correct?**
  _`receive()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)` to the rest of the system?**
  _9 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Webview Chat Renderer (JS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05374328208973878 - nodes in this community are weakly interconnected._
- **Why does `PluginLog` connect `.add` to `ChatPanel.kt`, `Task workflow: plan → approve → delegate → review → document`, `DependencyScanner.kt`, `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)`, `.tr`, `PermissionBroker.kt`, `ClaudeCliRunner.kt`, `Guard.kt`?**
  _High betweenness centrality (0.170) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `esc()` (e.g. with `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` and `4. Webview`) actually correct?**
  _`esc()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **Should `Plugin Concepts & Docs` be split into smaller, more focused modules?**
  _Cohesion score 0.1067193675889328 - nodes in this community are weakly interconnected._