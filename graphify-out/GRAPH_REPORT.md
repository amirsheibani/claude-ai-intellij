# Graph Report - claude-ai-intellij  (2026-11-21)

## Corpus Check
- 39 files · ~32,942 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 9 file(s) not represented in the graph (top: .properties 2, .xml 2, (none) 1)

## Summary
- 650 nodes · 1331 edges · 89 communities (10 shown, 79 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 61 edges (avg confidence: 0.88)
- Token cost: 45,394 input · 0 output

## Graph Freshness
- Built from commit: `2dc7047e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Webview Chat Renderer (JS)
- Plugin Concepts & Docs
- .tr
- DependencyScanner.kt
- Settings UI Configurable
- DiffPreview.kt
- SelectionTracker.kt
- ChatPanel
- IdeTools.kt
- CLAUDE.md
- IdeMcpServer.kt
- ClaudeAiSettings
- .decide
- .send
- تب‌های زیرعامل (Subagent Tabs)
- I18n
- ClaudeAi — Claude Code chat for JetBrains IDEs with Persian/RTL support
- Confirm before edit
- plugin-git.md
- debug-hypotheses/SKILL.md
- explore-feature/SKILL.md
- senior-review/SKILL.md
- New feature checklist
- jsonparser
- .overview

## God Nodes (most connected - your core abstractions)
1. `ChatPanel` - 41 edges
2. `receive()` - 32 edges
3. `chat()` - 26 edges
4. `esc()` - 26 edges
5. `toIde()` - 20 edges
6. `PluginLog` - 20 edges
7. `renderInfo()` - 19 edges
8. `IdeMcpServer` - 19 edges
9. `PermissionBroker` - 17 edges
10. `textDir()` - 16 edges

## Surprising Connections (you probably didn't know these)
- `1. Backend / state` --references--> `State`  [INFERRED]
  .claude/skills/scaffold-feature/SKILL.md → src/main/kotlin/ir/claudeai/settings/ClaudeAiSettings.kt
- `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` --references--> `esc()`  [INFERRED]
  .claude/rules/plugin-donts.md → src/main/resources/webview/chat.js
- `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` --references--> `markdown()`  [INFERRED]
  .claude/rules/plugin-donts.md → src/main/resources/webview/chat.js
- `چک‌لیست دیباگ` --references--> `agentFor()`  [INFERRED]
  docs/subagent-tabs.md → src/main/resources/webview/chat.js
- `چک‌لیست دیباگ` --references--> `setToolResult()`  [INFERRED]
  docs/subagent-tabs.md → src/main/resources/webview/chat.js

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Chat webview UI regions** — src_main_resources_webview_chat_topbar, src_main_resources_webview_chat_messages, src_main_resources_webview_chat_status, src_main_resources_webview_chat_composer [EXTRACTED 1.00]
- **Persian text rendering support** — readme_rtl_bidi_rendering, readme_vazirmatn_font, readme_jcef_html_panel, src_main_resources_webview_vazirmatn_ofl_sil_open_font_license [INFERRED 0.85]

## Communities (89 total, 79 thin omitted)

### Community 1 - "Webview Chat Renderer (JS)"
Cohesion: 0.05
Nodes (128): 4. Webview, حذف تب, فایل‌ها, acceptMention(), acceptSlash(), activate(), addTool(), addUser() (+120 more)

### Community 2 - "Plugin Concepts & Docs"
Cohesion: 0.11
Nodes (22): ChatPanel.kt (JCEF browser + JS-Kotlin bridge), Claude Code CLI (@anthropic-ai/claude-code), Claude Farsi IntelliJ Plugin, Code Block Copy / Insert-into-Editor Buttons, Editor Context Menu Actions (Send Selection / Explain / Review), Future Ideas (permission-prompt-tool MCP, IDE diff viewer, session history, auto-send IDE problems), Gradle Build (buildPlugin / runIde), Headless Claude CLI (claude -p --output-format stream-json) (+14 more)

### Community 5 - ".tr"
Cohesion: 0.10
Nodes (7): ExplainSelectionAction, OpenChatAction, ReviewSelectionAction, SelectionToClaudeAction, SendSelectionAction, ChatPanelHolder, ChatToolWindowFactory

### Community 13 - "SelectionTracker.kt"
Cohesion: 0.13
Nodes (5): SelectionTracker, CaretListener, FileEditorManagerListener, SelectionListener, Snapshot

### Community 14 - "ChatPanel"
Cohesion: 0.05
Nodes (16): 1. Analyze & plan (strong model), 2. Explain the plan and wait for approval, 3. Delegate to cheaper coding models, 4. Review the subagents' code, 5. Document the work, 6. Report, Task workflow: plan → approve → delegate → review → document, Build (+8 more)

### Community 25 - ".decide"
Cohesion: 0.09
Nodes (12): Action, BLOCK, WARN, Decision, Guard, Rule, Allow, Decision (+4 more)

### Community 29 - "تب‌های زیرعامل (Subagent Tabs)"
Cohesion: 0.25
Nodes (6): مستندات, تب‌های زیرعامل (Subagent Tabs), مرتب‌سازی, نمای کلی, وضعیت‌ها, چک‌لیست دیباگ

### Community 36 - "I18n"
Cohesion: 0.38
Nodes (3): I18n, Command, SlashCommands

### Community 38 - "ClaudeAi — Claude Code chat for JetBrains IDEs with Persian/RTL support"
Cohesion: 0.20
Nodes (9): ClaudeAi — Claude Code chat for JetBrains IDEs with Persian/RTL support, Features, Install, Permission modes, Project structure, Requirements, Tech stack, Usage (+1 more)

### Community 86 - "New feature checklist"
Cohesion: 0.25
Nodes (7): 1. Backend / state, 2. IDE integration, 3. Bridge (`ui/ChatPanel.kt`), 5. Registration, Build order, New feature checklist, New feature scaffold

## Knowledge Gaps
- **9 isolated node(s):** `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)`, `Claude Code CLI (@anthropic-ai/claude-code)`, `Code Block Copy / Insert-into-Editor Buttons` (+4 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 201 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **79 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` connect `ChatPanel` to `Webview Chat Renderer (JS)`?**
  _High betweenness centrality (0.266) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `receive()` (e.g. with `4. Webview` and `New feature scaffold`) actually correct?**
  _`receive()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `BLOCK`, `WARN`, `Composer (RTL textarea, Send/Stop buttons)` to the rest of the system?**
  _9 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Webview Chat Renderer (JS)` be split into smaller, more focused modules?**
  _Cohesion score 0.05249559600704639 - nodes in this community are weakly interconnected._
- **Why does `esc()` connect `Webview Chat Renderer (JS)` to `ChatPanel`?**
  _High betweenness centrality (0.173) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `esc()` (e.g. with `Plugin don'ts (IntelliJ plugin, Kotlin + JCEF webview)` and `4. Webview`) actually correct?**
  _`esc()` has 2 INFERRED edges - model-reasoned connections that need verification._
- **Should `Plugin Concepts & Docs` be split into smaller, more focused modules?**
  _Cohesion score 0.1067193675889328 - nodes in this community are weakly interconnected._