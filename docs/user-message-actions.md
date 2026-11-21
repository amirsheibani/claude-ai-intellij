<div dir="rtl" lang="fa">

# دکمه‌های پیام کاربر (کپی و اجرای مجدد)

روی هر پیامی که کاربر در چت فرستاده دو دکمه ظاهر می‌شود: کپی متن پیام در کلیپ‌بورد و ارسال دوبارهٔ همان پیام.

مرتبط: [فهرست مستندات](README.md) · [تب‌های زیرعامل](subagent-tabs.md)

---

## نمای کلی

<div dir="ltr">

```
send(text) ──► addUser(T, text)
                 ├─ d._text = text          (raw text, kept off the DOM)
                 └─ .uacts  [⧉ copy] [↻ rerun]

click [data-uact]
   ├─ copy  ──► toIde({ type: "copy", text }) ──► ChatPanel.handleFromJs "copy"
   │                                              └─ CopyPasteManager.setContents
   └─ rerun ──► send(d._text)   (ignored while c.busy)
```

</div>

---

## رفتار

- دکمه‌ها فقط با بردن موس روی پیام دیده می‌شوند (`opacity`) و با `flex` در انتهای ردیف می‌نشینند؛ در RTL خودکار به سمت چپ می‌روند.
- **کپی**: متن خام پیام (همان چیزی که فرستاده شده) کپی می‌شود و دکمه یک ثانیه `✓` نشان می‌دهد.
- **اجرای مجدد**: همان متن مستقیم در گفتگوی فعال ارسال می‌شود و یک پیام کاربر جدید پایین چت اضافه می‌شود.
- وقتی گفتگو مشغول است، `syncBusyUi()` کلاس `busy` را روی `#chatLogs` می‌گذارد و دکمهٔ اجرای مجدد کم‌رنگ و غیرفعال می‌شود؛ `send()` هم در حالت مشغول کاری نمی‌کند.

## امنیت

متن پیام فقط با `textContent` رندر می‌شود و در یک property جاوااسکریپت (`_text`) نگه داشته می‌شود، نه در attribute یا `innerHTML`. پیام `copy` در `ChatPanel.kt` از قبل وجود داشت و تغییری در Kotlin لازم نبود.

---

## فایل‌ها

| فایل | نقش |
|------|-----|
| `src/main/resources/webview/chat.js` | ساخت `.uacts` در `addUser()`، هندلر کلیک `[data-uact]`، کلاس `busy` در `syncBusyUi()` |
| `src/main/resources/webview/chat.css` | استایل `.msg.user .uacts` و حالت غیرفعال در `#chatLogs.busy` |
| `src/main/kotlin/ir/claudeai/ui/ChatPanel.kt` | (بدون تغییر) پیام `copy` → `CopyPasteManager` |

## چک‌لیست دیباگ

- دکمه دیده نمی‌شود؟ موس را روی پیام ببرید؛ در حالت عادی `opacity: 0` است.
- اجرای مجدد کار نمی‌کند؟ بررسی کنید گفتگو مشغول نباشد (`c.busy`).
- کپی کار نمی‌کند؟ لاگ پلاگین را برای پیام `copy` در `handleFromJs` ببینید.

</div>
