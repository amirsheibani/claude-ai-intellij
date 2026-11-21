<div dir="rtl" lang="fa">

# تب‌های زیرعامل (Subagent Tabs)

نمایش هر زیرعامل (`Task` / `Agent`) به‌صورت یک تب در یک ردیف بالای گفتگو، با مرتب‌سازی بر اساس وضعیت، چشمک برای «منتظر شما» و امکان حذف تب‌های تمام‌شده.

مرتبط: [فهرست مستندات](README.md)

---

## نمای کلی

<div dir="ltr">

```
stream-json event (parent_tool_use_id)
        │
        ▼
targetFor() ──► agentFor()  ──► c.agents[id] / c.agentOrder
        │
addTool() / setToolResult() ──► a.status = running | done | error
showPermission() / resolvePermission() ──► c.perms changes
        │
        ▼
renderSubTabs()  ──► sort: waiting → running → done/error
        │
        ▼
#subTabs  (one row, overflow-x: auto)
   × click ──► removeAgent()
```

</div>

---

## وضعیت‌ها

| وضعیت | شرط | ظاهر |
|-------|-----|------|
| `waiting` | زیرعامل در حال اجراست و یک درخواست مجوز باز دارد (`c.perms` → ابزاری با `t.T === a.T`) | چشمک زرد (`tabblink`) و نقطهٔ زرد |
| `running` | نتیجهٔ ابزار Task هنوز نیامده | نقطهٔ چشمک‌زن آبی |
| `done` | نتیجه بدون خطا آمده | طوسی، دکمهٔ `×` |
| `error` | نتیجه با خطا آمده | طوسی با نقطهٔ قرمز، دکمهٔ `×` |

## مرتب‌سازی

تب «گفتگو» همیشه اول است. بقیه به ترتیب: `waiting` ← `running` ← `done`/`error`؛ در هر گروه به ترتیب زمان ساخت. تب فعال خودکار به محدودهٔ دید اسکرول می‌شود.

## حذف تب

- فقط تب‌های `done` و `error` دکمهٔ `×` دارند (`data-del-agent`).
- `removeAgent()` لاگ زیرعامل را از DOM و از `c.agents` / `c.agentOrder` پاک می‌کند؛ اگر همان تب باز بود، به گفتگوی اصلی برمی‌گردد.
- تب حذف‌شده از نمای Workloads هم حذف می‌شود.
- اگر روی لینک «مشاهدهٔ زیرعامل ›» یک تب حذف‌شده کلیک شود، `showAgent()` فقط یک پیام هشدار نشان می‌دهد.

---

## فایل‌ها

| فایل | نقش |
|------|-----|
| `src/main/resources/webview/chat.js` | `renderSubTabs()`، `agentWaiting()`، `removeAgent()`، گارد `showAgent()`، هندلر کلیک `data-del-agent` |
| `src/main/resources/webview/chat.css` | استایل `.subtabs` (یک ردیف، اسکرول افقی)، وضعیت‌های `waiting` / `done` / `error`، `@keyframes tabblink` |

## چک‌لیست دیباگ

- تب چشمک نمی‌زند؟ بررسی کنید `c.tools[permId].T` همان `a.T` زیرعامل باشد (یعنی رویداد ابزار با `parent_tool_use_id` آمده باشد).
- تب بعد از اتمام هنوز «در حال اجرا» است؟ نتیجهٔ ابزار Task (`setToolResult`) نرسیده است.
- تب حذف‌شده دوباره ظاهر شد؟ رویداد دیرهنگامی با همان `parent_tool_use_id` آمده و `agentFor()` آن را دوباره ساخته است.

</div>
