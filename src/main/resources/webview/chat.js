(function () {
  "use strict";

  // ------------------------------------------------------------------ state
  const $ = (id) => document.getElementById(id);
  const messagesEl = $("messages");
  const inputEl = $("input");
  const state = {
    busy: false,
    sendOnEnter: true,
    projectPath: "",
    permissionMode: "acceptEdits",
    streamedMsgIds: new Set(),
    curStreamMsgId: null,
    blocks: {},          // index -> {type, el, text, toolId}
    tools: {},           // tool_use_id -> element
    sawResult: false,
    stderr: [],
  };

  function toIde(obj) {
    if (window.__toIde) window.__toIde(JSON.stringify(obj));
  }

  // ------------------------------------------------------------------ markdown (small, safe)
  function esc(s) {
    return s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
  }

  const FILE_RE = /^[\w.\-\/\\]+\.[A-Za-z0-9]{1,8}(:\d+)?$/;

  function inline(text) {
    // protect inline code first
    const codes = [];
    let s = text.replace(/`([^`\n]+)`/g, (_, c) => {
      codes.push(c);
      return "\u0000" + (codes.length - 1) + "\u0000";
    });
    s = esc(s);
    s = s.replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>");
    s = s.replace(/(^|[^*])\*([^*\n]+)\*/g, "$1<em>$2</em>");
    s = s.replace(/\[([^\]]+)\]\((https?:\/\/[^)\s]+)\)/g, '<a href="$2" target="_blank">$1</a>');
    s = s.replace(/\u0000(\d+)\u0000/g, (_, i) => {
      const c = codes[+i];
      const isFile = FILE_RE.test(c);
      return '<code class="inline' + (isFile ? ' file" data-file="' + esc(c) : "") + '">' + esc(c) + "</code>";
    });
    return s;
  }

  function codeBlock(lang, code) {
    const id = "cb" + Math.random().toString(36).slice(2);
    return (
      '<div class="codeblock"><div class="bar"><span>' + esc(lang || "code") + '</span><span class="btns">' +
      '<button data-act="insert" data-target="' + id + '" title="درج در ویرایشگر">درج</button>' +
      '<button data-act="copy" data-target="' + id + '" title="کپی">کپی</button></span></div>' +
      '<pre><code id="' + id + '">' + esc(code) + "</code></pre></div>"
    );
  }

  function markdown(src) {
    const lines = src.replace(/\r\n/g, "\n").split("\n");
    let out = "";
    let i = 0;
    let para = [];
    const flush = () => {
      if (para.length) {
        out += "<p>" + para.map(inline).join("<br>") + "</p>";
        para = [];
      }
    };

    while (i < lines.length) {
      const line = lines[i];

      // fenced code
      const fence = line.match(/^\s*(```+|~~~+)\s*([\w+#.\-]*)/);
      if (fence) {
        flush();
        const marker = fence[1];
        const lang = fence[2];
        const buf = [];
        i++;
        while (i < lines.length && !lines[i].trim().startsWith(marker)) {
          buf.push(lines[i]);
          i++;
        }
        i++; // closing fence (or EOF while streaming)
        out += codeBlock(lang, buf.join("\n"));
        continue;
      }

      // heading
      const h = line.match(/^(#{1,4})\s+(.*)$/);
      if (h) {
        flush();
        out += "<h" + h[1].length + ">" + inline(h[2]) + "</h" + h[1].length + ">";
        i++;
        continue;
      }

      // hr
      if (/^\s*(-{3,}|\*{3,}|_{3,})\s*$/.test(line)) {
        flush();
        out += "<hr>";
        i++;
        continue;
      }

      // table
      if (/^\s*\|.*\|\s*$/.test(line) && i + 1 < lines.length && /^\s*\|[\s:\-|]+\|\s*$/.test(lines[i + 1])) {
        flush();
        const row = (l) => l.trim().replace(/^\||\|$/g, "").split("|").map((c) => c.trim());
        out += "<table><tr>" + row(line).map((c) => "<th>" + inline(c) + "</th>").join("") + "</tr>";
        i += 2;
        while (i < lines.length && /^\s*\|.*\|\s*$/.test(lines[i])) {
          out += "<tr>" + row(lines[i]).map((c) => "<td>" + inline(c) + "</td>").join("") + "</tr>";
          i++;
        }
        out += "</table>";
        continue;
      }

      // lists
      const li = line.match(/^\s*([-*+]|\d+[.)])\s+(.*)$/);
      if (li) {
        flush();
        const ordered = /\d/.test(li[1]);
        const tag = ordered ? "ol" : "ul";
        out += "<" + tag + ">";
        while (i < lines.length) {
          const m = lines[i].match(/^\s*([-*+]|\d+[.)])\s+(.*)$/);
          if (!m) break;
          out += "<li>" + inline(m[2]) + "</li>";
          i++;
        }
        out += "</" + tag + ">";
        continue;
      }

      // blockquote
      if (/^\s*>/.test(line)) {
        flush();
        const buf = [];
        while (i < lines.length && /^\s*>/.test(lines[i])) {
          buf.push(lines[i].replace(/^\s*>\s?/, ""));
          i++;
        }
        out += "<blockquote>" + buf.map(inline).join("<br>") + "</blockquote>";
        continue;
      }

      if (line.trim() === "") {
        flush();
      } else {
        para.push(line);
      }
      i++;
    }
    flush();
    return out;
  }

  // ------------------------------------------------------------------ DOM helpers
  function hideEmpty() {
    const e = $("empty");
    if (e) e.remove();
  }

  function scrollDown() {
    const nearBottom = messagesEl.scrollHeight - messagesEl.scrollTop - messagesEl.clientHeight < 160;
    if (nearBottom) messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function addUser(text) {
    hideEmpty();
    const d = document.createElement("div");
    d.className = "msg user";
    d.innerHTML = '<div class="who">شما</div><div class="bubble" dir="auto"></div>';
    d.querySelector(".bubble").textContent = text;
    messagesEl.appendChild(d);
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  let currentTurn = null;
  function turnEl() {
    if (!currentTurn) {
      hideEmpty();
      currentTurn = document.createElement("div");
      currentTurn.className = "msg assistant";
      currentTurn.innerHTML = '<div class="who">Claude</div>';
      messagesEl.appendChild(currentTurn);
    }
    return currentTurn;
  }

  function newTextBlock() {
    const el = document.createElement("div");
    el.className = "bubble md";
    turnEl().appendChild(el);
    return el;
  }

  const renderQueue = new Set();
  let renderScheduled = false;
  function scheduleRender(block) {
    renderQueue.add(block);
    if (renderScheduled) return;
    renderScheduled = true;
    requestAnimationFrame(() => {
      renderScheduled = false;
      renderQueue.forEach((b) => (b.el.innerHTML = markdown(b.text)));
      renderQueue.clear();
      scrollDown();
    });
  }

  function toolSummary(name, input) {
    if (!input) return "";
    const v =
      input.file_path || input.path || input.command || input.pattern || input.url ||
      input.query || input.description || input.prompt || "";
    let s = String(v);
    if (state.projectPath && s.startsWith(state.projectPath)) s = s.slice(state.projectPath.length).replace(/^[\/\\]/, "");
    return s.length > 160 ? s.slice(0, 160) + "…" : s;
  }

  const TOOL_FA = {
    Read: "خواندن", Write: "نوشتن", Edit: "ویرایش", MultiEdit: "ویرایش", Bash: "اجرای دستور",
    Glob: "جستجوی فایل", Grep: "جستجو در کد", LS: "فهرست", WebFetch: "دریافت صفحه", WebSearch: "جستجوی وب",
    Task: "زیرعامل", TodoWrite: "فهرست کارها", NotebookEdit: "ویرایش نوت‌بوک",
  };

  function addTool(id, name, input) {
    let el = id && state.tools[id];
    if (!el) {
      el = document.createElement("div");
      el.className = "tool";
      el.innerHTML = '<span class="state"></span><span class="label"></span><span class="name"></span><span class="arg"></span>';
      el.querySelector(".label").textContent = TOOL_FA[name] || "ابزار";
      el.querySelector(".name").textContent = name;
      turnEl().appendChild(el);
      if (id) state.tools[id] = el;
    }
    if (input) {
      const arg = toolSummary(name, input);
      const argEl = el.querySelector(".arg");
      argEl.textContent = arg;
      argEl.title = arg;
    }
    scrollDown();
    return el;
  }

  function notice(text, kind) {
    const d = document.createElement("div");
    d.className = "notice " + (kind || "");
    d.dir = "auto";
    d.textContent = text;
    (currentTurn || messagesEl).appendChild(d);
    scrollDown();
  }

  function setBusy(b, text) {
    state.busy = b;
    $("btnSend").classList.toggle("hidden", b);
    $("btnStop").classList.toggle("hidden", !b);
    $("status").classList.toggle("hidden", !b);
    if (text) $("statusText").textContent = text;
    const dot = $("statusDot");
    dot.classList.toggle("busy", b);
  }

  // ------------------------------------------------------------------ Claude stream-json events
  function handleStream(ev) {
    switch (ev.type) {
      case "message_start":
        state.curStreamMsgId = ev.message && ev.message.id;
        if (state.curStreamMsgId) state.streamedMsgIds.add(state.curStreamMsgId);
        state.blocks = {};
        break;
      case "content_block_start": {
        const cb = ev.content_block || {};
        if (cb.type === "text") {
          state.blocks[ev.index] = { type: "text", el: newTextBlock(), text: cb.text || "" };
        } else if (cb.type === "tool_use") {
          addTool(cb.id, cb.name, null);
          state.blocks[ev.index] = { type: "tool" };
        } else if (cb.type === "thinking") {
          $("statusText").textContent = "در حال فکر کردن…";
        }
        break;
      }
      case "content_block_delta": {
        const b = state.blocks[ev.index];
        const d = ev.delta || {};
        if (b && b.type === "text" && d.type === "text_delta") {
          b.text += d.text;
          scheduleRender(b);
          $("statusText").textContent = "در حال نوشتن…";
        }
        break;
      }
    }
  }

  function handleAssistant(msg) {
    if (!msg || !Array.isArray(msg.content)) return;
    const streamed = msg.id && state.streamedMsgIds.has(msg.id);
    msg.content.forEach((c) => {
      if (c.type === "text" && !streamed) {
        const el = newTextBlock();
        el.innerHTML = markdown(c.text || "");
      } else if (c.type === "tool_use") {
        addTool(c.id, c.name, c.input);
        $("statusText").textContent = "در حال اجرای " + (TOOL_FA[c.name] || c.name) + "…";
      }
    });
    scrollDown();
  }

  function handleToolResults(msg) {
    if (!msg || !Array.isArray(msg.content)) return;
    msg.content.forEach((c) => {
      if (c.type === "tool_result" && state.tools[c.tool_use_id]) {
        state.tools[c.tool_use_id].classList.add(c.is_error ? "error" : "done");
      }
    });
    $("statusText").textContent = "در حال فکر کردن…";
  }

  function handleResult(ev) {
    state.sawResult = true;
    if (ev.is_error || (ev.subtype && ev.subtype !== "success")) {
      notice("خطا: " + (ev.result || ev.subtype || "نامشخص"), "error");
    }
    if (Array.isArray(ev.permission_denials) && ev.permission_denials.length) {
      const names = [...new Set(ev.permission_denials.map((p) => p.tool_name))].join("، ");
      notice(
        "برخی ابزارها اجازهٔ اجرا نداشتند (" + names + ").\nحالت دسترسی فعلی: " + state.permissionMode +
          " — برای اجازهٔ ویرایش یا اجرای دستورات، آن را از تنظیمات تغییر دهید.",
        "warn"
      );
    }
    const parts = [];
    if (ev.duration_ms) parts.push("⏱ " + (ev.duration_ms / 1000).toFixed(1) + " ثانیه");
    if (ev.num_turns) parts.push(ev.num_turns + " مرحله");
    if (typeof ev.total_cost_usd === "number") parts.push("$" + ev.total_cost_usd.toFixed(4));
    if (parts.length) {
      const m = document.createElement("div");
      m.className = "meta";
      m.textContent = parts.join(" · ");
      turnEl().appendChild(m);
    }
    $("statusDot").classList.add("ok");
    scrollDown();
  }

  function onClaude(ev) {
    switch (ev.type) {
      case "system":
        if (ev.subtype === "init") $("statusText").textContent = "متصل شد" + (ev.model ? " — " + ev.model : "");
        break;
      case "stream_event":
        handleStream(ev.event || {});
        break;
      case "assistant":
        handleAssistant(ev.message);
        break;
      case "user":
        handleToolResults(ev.message);
        break;
      case "result":
        handleResult(ev);
        break;
    }
  }

  // ------------------------------------------------------------------ bridge (IDE -> page)
  window.bridge = {
    receive(kind, p) {
      switch (kind) {
        case "init": {
          const r = document.documentElement.style;
          if (p.bg) r.setProperty("--bg", p.bg);
          if (p.fg) r.setProperty("--fg", p.fg);
          if (p.fontSize) r.setProperty("--font-size", p.fontSize + "px");
          state.sendOnEnter = p.sendOnEnter !== false;
          state.projectPath = p.projectPath || "";
          state.permissionMode = p.permissionMode || state.permissionMode;
          document.body.classList.toggle("light", !p.dark);
          break;
        }
        case "claude":
          onClaude(p);
          break;
        case "stderr":
          state.stderr.push(p.text);
          if (state.stderr.length > 30) state.stderr.shift();
          break;
        case "error":
          notice(p.text, "error");
          setBusy(false);
          break;
        case "exit":
          if (!p.gotResult && p.code !== 0) {
            const tail = state.stderr.slice(-8).join("\n");
            notice(
              (p.code === 143 || p.code === 137 || p.code === 1 && !tail ? "متوقف شد." : "claude با کد " + p.code + " خارج شد.") +
                (tail ? "\n\n" + tail : "") +
                (/login|auth|api key|401/i.test(tail) ? "\n\nیک بار در ترمینال دستور «claude» را اجرا و وارد حساب خود شوید." : ""),
              tail ? "error" : "warn"
            );
          }
          setBusy(false);
          currentTurn = null;
          inputEl.focus();
          break;
        case "cleared":
          messagesEl.innerHTML = "";
          currentTurn = null;
          state.tools = {};
          state.streamedMsgIds.clear();
          setBusy(false);
          $("statusDot").classList.remove("ok");
          notice("گفتگوی جدید شروع شد.", "");
          break;
        case "insertInput":
          inputEl.value = (inputEl.value ? inputEl.value.replace(/\s*$/, "") + "\n\n" : "") + p.text;
          autoGrow();
          inputEl.focus();
          inputEl.setSelectionRange(inputEl.value.length, inputEl.value.length);
          break;
        case "externalSend":
          send(p.text);
          break;
      }
    },
  };

  // ------------------------------------------------------------------ user input
  function send(text) {
    text = (text || "").trim();
    if (!text || state.busy) return;
    addUser(text);
    currentTurn = null;
    state.sawResult = false;
    state.stderr = [];
    setBusy(true, "در حال اتصال…");
    toIde({ type: "send", text });
  }

  function autoGrow() {
    inputEl.style.height = "auto";
    inputEl.style.height = Math.min(inputEl.scrollHeight + 2, window.innerHeight * 0.4) + "px";
  }

  inputEl.addEventListener("input", autoGrow);
  inputEl.addEventListener("keydown", (e) => {
    if (e.key !== "Enter" || e.isComposing) return;
    const wantsSend = state.sendOnEnter ? !e.shiftKey : e.ctrlKey || e.metaKey;
    if (wantsSend) {
      e.preventDefault();
      const t = inputEl.value;
      inputEl.value = "";
      autoGrow();
      send(t);
    }
  });

  $("btnSend").addEventListener("click", () => {
    const t = inputEl.value;
    inputEl.value = "";
    autoGrow();
    send(t);
  });
  $("btnStop").addEventListener("click", () => toIde({ type: "stop" }));
  $("btnNew").addEventListener("click", () => toIde({ type: "new" }));
  $("btnSettings").addEventListener("click", () => toIde({ type: "settings" }));

  document.addEventListener("click", (e) => {
    const t = e.target;
    if (t.classList && t.classList.contains("sugg")) {
      send(t.textContent);
      return;
    }
    const act = t.getAttribute && t.getAttribute("data-act");
    if (act) {
      const code = document.getElementById(t.getAttribute("data-target"));
      if (!code) return;
      if (act === "copy") {
        toIde({ type: "copy", text: code.textContent });
        const old = t.textContent;
        t.textContent = "✓";
        setTimeout(() => (t.textContent = old), 1000);
      } else if (act === "insert") {
        toIde({ type: "insertCode", text: code.textContent });
      }
      return;
    }
    if (t.classList && t.classList.contains("file")) {
      const [path, line] = t.getAttribute("data-file").split(":");
      toIde({ type: "openFile", path, line: line ? parseInt(line, 10) : 0 });
      return;
    }
    const a = t.closest && t.closest("a[href]");
    if (a) {
      e.preventDefault();
      toIde({ type: "openUrl", url: a.getAttribute("href") });
    }
  });

  inputEl.focus();
})();
