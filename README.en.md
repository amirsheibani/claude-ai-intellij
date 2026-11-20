# ClaudeAi — Claude Code chat for JetBrains IDEs with Persian/RTL support

**A chat panel for [Claude Code](https://docs.anthropic.com/en/docs/claude-code) inside IntelliJ IDEA, Android Studio, PyCharm, WebStorm and other JetBrains IDEs, with correct Persian, right-to-left (RTL) and mixed Persian/English text.**

> Unofficial community project, not affiliated with Anthropic.

[فارسی](README.md) · [Download the latest release](https://github.com/amirsheibani/claude-ai-intellij/releases/latest)

## Why

The official Claude Code plugin runs the CLI inside the IDE terminal, and the terminal shows Persian letters separated and in reverse order. This plugin runs Claude Code in headless mode (`claude -p --output-format stream-json`) and renders the output in an HTML panel (JCEF) with proper RTL and bidi support.

## Features

- RTL chat panel with the bundled Vazirmatn font
- Each paragraph detects its own direction (`unicode-bidi: plaintext`); code and file paths always stay LTR
- Live streaming of replies, including the tools Claude runs (reading/editing files, running commands)
- Copy and Insert-into-editor buttons on every code block
- Click a file name in a reply to open it in the IDE
- Right-click code → **ClaudeAi** → send selection / explain code / review code
- Continue a conversation with `--resume`, or start a new chat
- Settings: CLI path, model, permission mode, HTTPS proxy, reply in Persian, Arabic "ي/ك" → Persian "ی/ک"

## Requirements

1. Claude Code CLI installed and logged in:
   ```bash
   npm install -g @anthropic-ai/claude-code
   claude        # run once and log in
   ```
2. A JetBrains IDE 2024.2 or newer (JDK 21 only to build from source)

## Install

Download the zip from [Releases](https://github.com/amirsheibani/claude-ai-intellij/releases/latest), then in the IDE: **Settings › Plugins › ⚙ › Install Plugin from Disk…** and choose the zip.

Build from source:

```bash
./gradlew buildPlugin   # output: build/distributions/
./gradlew runIde        # run a sandbox IDE with the plugin
```

## Usage

- Open the **ClaudeAi** panel on the right side of the IDE (or `Ctrl+Alt+Shift+L`)
- Select code and press `Ctrl+Alt+Shift+K` to add it, with path and line numbers, to the input box
- Settings: **Settings › Tools › ClaudeAi**

### Permission modes

| Mode | Description |
|---|---|
| `default` | Tools that need approval (edits, commands) are denied |
| `acceptEdits` | (default) File edits are allowed |
| `plan` | Analysis and planning only, no changes |
| `bypassPermissions` | Everything without asking — trusted projects only |

Headless mode cannot ask for approval in real time; if a tool is denied, the panel shows a message.

## Project structure

```
src/main/kotlin/ir/claudeai/
  cli/ClaudeCliRunner.kt        runs claude, parses stream-json, --resume
  ui/ChatPanel.kt               JCEF browser + JS ↔ Kotlin bridge
  ui/ChatToolWindowFactory.kt   tool window registration
  actions/SelectionActions.kt   editor menu actions
  settings/                     settings
src/main/resources/webview/
  chat.html / chat.css / chat.js  chat UI (Markdown, RTL, streaming)
  Vazirmatn.woff2                 font (OFL license)
```

## Tech stack

Kotlin · IntelliJ Platform SDK · JCEF (embedded Chromium) · Claude Code CLI (stream-json) · Gradle
