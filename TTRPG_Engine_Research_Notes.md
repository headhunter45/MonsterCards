# TTRPG Engine - Cross-Platform & NFC Research Notes

## 1. Cross-Platform Portability (iOS & Web)
The "Ruleset Manifest + Entity JSON + Template = UI" architecture translates exceptionally well across platforms because it relies on standard web technologies rather than native UI code.

### iOS Translation
*   **Data Storage:** Use **Core Data** instead of Room. Entities are stored with indexed metadata columns (`uuid`, `ruleset_id`, `entity_type`) and a `json_payload` string attribute.
*   **Deep Linking:** Handled via Universal Links (preferred) or Custom URL Schemes (`ttrpg://`) declared in `Info.plist`, intercepted by `application(_:open:options:)`.
*   **Rendering & Security:** Use **`WKWebView`**.
    *   Use `WKURLSchemeHandler` to intercept asset requests and serve cached CSS/JS locally.
    *   Compile Handlebars templates natively (e.g., using `GRMustache` or a Swift Handlebars port), then load via `webView.loadHTMLString(html, baseURL: ...)`.
    *   Secure minigame callbacks are handled via **`WKScriptMessageHandler`** (Apple's secure equivalent to Android's `@JavascriptInterface`).

### Desktop Web App Translation
If hosted directly by the API provider, the trust boundary allows for a significantly simplified SPA architecture.
*   **Data Storage:** Lightweight caching via **IndexedDB**; heavy local database (Room/Core Data) is unnecessary.
*   **Deep Linking:** Standard web routing (`https://api.provider.com/v1/open5e/character/123`).
*   **Rendering & Security:** No `WebView` sandboxing needed. Handlebars templating (`handlebars.js`) and minigame JS can execute entirely client-side within the browser's native security model.

---

## 2. NFC Tag Scanning & Deep Linking Strategies

To achieve a seamless "tap-to-open" experience across both Android and iOS, NFC tag encoding requires careful consideration due to differing OS-level restrictions.

### iOS NFC Limitations
1.  **Background Tag Reading (iPhone XS and newer):** 
    *   Allows scanning without the app being open.
    *   **CRITICAL CONSTRAINT:** Only supports standard `http://` and `https://` URLs. It will **not** trigger on custom URI schemes like `ttrpg://`.
    *   If triggered, iOS drops a notification banner which the user must tap to launch the app via Universal Links.
2.  **Foreground Tag Reading (All NFC-capable iPhones):** 
    *   Requires the user to open the app, tap a "Scan" button to invoke the `CoreNFC` system modal, and then scan the tag. Can read any NDEF payload (including `ttrpg://`).

### Recommended Hybrid Strategy (The "Tap-and-Go" Flow)
To support seamless physical NFC integration (e.g., tapping a miniature base) across the widest range of devices:

1.  **Encode Tags with HTTPS:** Use standard web URLs on the physical NFC tags (e.g., `https://ttrpgwith.me/scan?payload=...`). Do not encode `ttrpg://` onto physical tags.
2.  **Universal / App Links:** Host `apple-app-site-association` and `assetlinks.json` on the routing domain (`ttrpgwith.me`) to prove app ownership.
3.  **Cross-Platform Resolution:**
    *   **Android:** Detects the HTTPS NDEF tag, resolves the App Link, and opens the app immediately.
    *   **iOS (XS+):** Detects the tag in the background, displays a notification, and launches the app via Universal Links upon tap.
    *   **Fallback:** If the app is not installed, the OS opens the device browser to a landing page offering the app download.
4.  **Custom Scheme Usage:** Retain the `ttrpg://` custom scheme explicitly for QR codes, chat app links (Discord/WhatsApp), and internal app-to-app routing where Universal Link interception is unreliable.
