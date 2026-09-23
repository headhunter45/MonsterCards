# TTRPG Engine App - Project Plan

## Overview
The "TTRPG Engine" will be a new, independent Android application residing within the existing monorepo. Unlike MonsterCards, which is strictly coupled to D&D 5e data models, this app is a schema-agnostic rendering engine. It dynamically downloads ruleset manifests, templates (HTML/CSS/JS), and generic entity JSON payloads from remote servers (via URL, QR code, or NFC) and safely renders them on the device.

## 1. App Architecture & Module Structure

### 1.1 New Module
- Create a new Android application module: `:ttrpg-engine`.
- Sharing core utilities (like `Logger`) with `:app` is possible by moving them to a shared Android Library module (e.g. `:core-utils`), but for Phase 1, `:ttrpg-engine` will remain isolated to prevent polluting the `MonsterCards` logic.

### 1.2 Data Storage Model (SQLite/Room)
Because entity schemas vary by ruleset and entity type, relational mapping is impossible. We will use a Document Store pattern via Room.

**Table: `rulesets`**
- `ruleset_id` (String, Primary Key) - e.g., "open5e"
- `base_url` (String) - e.g., "http://localhost:5000/v1"
- `name`, `version`, `author_url` (Strings)
- `last_sync_timestamp` (Long)

**Table: `entities`**
- `uuid` (String, Primary Key)
- `ruleset_id` (String, Foreign Key)
- `entity_type` (String) - e.g., "character", "spell"
- `display_name` (String)
- `json_payload` (String) - The raw JSON string of the entity.

## 2. Ruleset Ingestion & Session Management

### 2.1 Deep Linking & NFC URL Schemes
To ensure the app intercepts NFC tags and QR codes reliably without conflicting with standard web browsers, we will use a hybrid approach:

**1. Custom URI Scheme (App-Specific)**
- Use the custom scheme `ttrpg://`. 
- **Example:** `ttrpg://api.example.com/v1/open5e/character/123`
- **Protocol Resolution:** The app will strip the custom scheme and default to `https://` for the underlying network request. If the `https` request fails (e.g. for local development networks), the app will automatically fall back to `http://`.
- **Pros:** Guarantees the OS will immediately open our app (or prompt the user to install it) rather than opening a browser. Perfect for printed QR codes or programmed NFC tags where the primary goal is app ingestion.

**2. Android App Links (Verified Web URLs)**
- If you prefer standard `https://` URLs so that users without the app see a fallback webpage, we can implement Android App Links (Digital Asset Links).
- **Example:** `https://ttrpgwith.me/import/v1/open5e/character/123`
- **Pros:** If the user has the app installed, Android seamlessly intercepts it. If not, they land on a website that can offer a download link. (Requires hosting a `assetlinks.json` file on your web domain).

**Workflow:**
  1. Intercept URL: `ttrpg://localhost:5000/v1/open5e/character/123` (Host: `localhost:5000/v1`, Ruleset: `open5e`, Type: `character`, ID: `123`).
  2. Extract `base_url` (`http://localhost:5000/v1`), `ruleset_id` (`open5e`), `entity_type` (`character`), and `entity_id` (`123`).
  3. Query local `rulesets` table for `open5e`.
  4. If missing: Trigger the **Ruleset Onboarding Flow**.
  5. Fetch and store the entity JSON.

### 2.2 Ruleset Onboarding Flow
1. Fetch `GET {base_url}/{ruleset_id}.zip`.
2. Extract the archive into the app's internal storage (`context.getFilesDir() + "/rulesets/" + ruleset_id`).
3. Parse `manifest.json` and insert a record into the `rulesets` table.

## 3. Rendering Engine & Security

Entities are rendered by combining their `json_payload` with the HTML/CSS/JS templates provided by the ruleset.

### 3.1 Server-Side vs. Client-Side Templating
- **Native Handlebars:** To minimize WebView vulnerabilities, the Handlebars template compilation will happen natively in Kotlin/Java using [Handlebars.java](https://github.com/jknack/handlebars.java).
- The JSON payload and the HTML template string are merged natively, and the resulting static HTML string is passed to the WebView.

### 3.2 Secure WebView Configuration
To support interactive minigames and scripts within the templates without risking data exfiltration or malicious execution:
- **Sandbox Configuration:**
  - `setJavaScriptEnabled(true)` (Required for minigames/sheet logic).
  - `setAllowFileAccess(false)` and `setAllowContentAccess(false)`.
- **Network Isolation (Critical):**
  - Implement a `WebViewClient.shouldInterceptRequest()`.
  - Intercept local asset requests (e.g., `<link href="style.css">`) and serve them manually from the internal `rulesets/` directory.
  - Block ALL external HTTP/HTTPS network requests originating from the WebView.
- **Content Security Policy (CSP):**
  - Inject a strict CSP meta tag into the `<head>` of all rendered templates:
    `<meta http-equiv="Content-Security-Policy" content="default-src 'none'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;">`

## 4. Phased Implementation Plan

### Phase 1: Foundation
- Create the `:ttrpg-engine` module.
- Set up Room database for `rulesets` and `entities`.
- Implement basic ZIP downloading, extraction to internal storage, and manifest parsing.

### Phase 2: Templating & UI
- Integrate `Handlebars.java`.
- Build the Secure `WebView` fragment with the custom `WebViewClient` interceptor to serve local CSS/JS/Images from the extracted ruleset directory.
- Implement a basic Library list view querying the `entities` table.

### Phase 3: Integration & Deep Links
- Implement NFC and URL Deep Link intent filters.
- Build the resolution workflow (check if ruleset exists -> download ruleset -> download entity -> render).
- Implement 404 retry logic (appending `.json` to paths).

### Phase 4: Telemetry & API Callbacks (Minigames)
- Build a secure bridge (`@JavascriptInterface`) strictly for authorized API callbacks.
- Allow the WebView JS to post specific telemetry events (e.g., "minigame_won") back to the Android layer.
- The Android layer queues these events and safely performs the network request back to `{base_url}/v1/telemetry` using native OkHttp. (The WebView itself remains completely cut off from the network).