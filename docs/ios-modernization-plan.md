# Analysis & Gap Summary

1. **Root Directory Artifacts**:
   - Confirmed that `.classpath`, `.project`, `.settings/`, `Manifest.MF`, `MobScores.jardesc`, `pom.xml`, `src/`, and `ProjectDescription.*` are leftover files from a legacy Minecraft/Bukkit plugin (`MobScores`) and should be removed.
2. **Architecture & Modern Standards**:
   - **Android**: Uses a feature-rich Room persistence architecture with `MonsterRepository`, full-text search (`MonsterFTS`), multiple importers (`Open5e`, `TetraCube`, `DnDBeyond`, `Pf2e`, `Binder`), exporters, and UI modules for Library, Dashboard, Collections, and Global Search.
   - **iOS**: Currently builds but requires modernization to iOS 17+/18+ paradigms (Swift Observation `@Observable`, `NavigationStack`/`NavigationSplitView`, strict concurrency), parity with the repository pattern, full implementation of the Dashboard and Collections workflows, unified online/offline Search, and complete importer/exporter coverage.

---

## Tasks Added to [docs/tasks.md](file:///Users/tom/Projects/MonsterCards/docs/tasks.md)

| ID          | Title                                                                                                  | Project | Type    |
| :---------- | :----------------------------------------------------------------------------------------------------- | :------ | :------ |
| **FGJ-001** | **Clean up legacy Bukkit plugin and Maven leftovers from repository root**                             | Shared  | Chore   |
| **FGJ-002** | **Modernize iOS Xcode project configuration, build pipeline, and dependency management**               | iOS     | Chore   |
| **FGJ-003** | **Modernize CoreData / CloudKit persistence layer and implement repository architecture**              | iOS     | Feature |
| **FGJ-004** | **Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)** | iOS     | Feature |
| **FGJ-005** | **Modernize SwiftUI architecture, NavigationStack, and Observation framework**                         | iOS     | Feature |
| **FGJ-006** | **Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions**          | iOS     | Feature |
| **FGJ-007** | **Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards**        | iOS     | Feature |
| **FGJ-008** | **Implement Collections and Encounters management with CR/XP summary metrics**                         | iOS     | Feature |
| **FGJ-009** | **Implement unified local Full-Text Search and remote Open5e API live search**                         | iOS     | Feature |
| **FGJ-010** | **Build comprehensive multi-section 5e Monster Editor suite with validation and live preview**         | iOS     | Feature |
| **FGJ-011** | **Implement QuickLook Preview Extension, custom document types (.monster), and system sharing**        | iOS     | Feature |
| **FGJ-012** | **Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows**          | iOS     | Feature |

Viewed Persistence.swift:1-48
Viewed MonsterCards.entitlements:1-17

### 1. Is there something like Core Data + CloudKit sync on Android?

**No built-in equivalent exists in Android OS/SDK.** Apple's `NSPersistentCloudKitContainer` is unique because Apple provides the free cloud database infrastructure, push notification pipeline (APNs), and automatic SQLite-to-Cloud replication tied directly to the user's Apple ID with zero custom backend required.

On Android, Room / SQLite does not have a native "Google Drive sync" container. Here are the common approaches for Android:

| Approach                                             | How it Works                                                                                                           | Pros / Cons                                                                                                                                           |
| :--------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------- |
| **File Exchange (`.card` / `.binder`)** _(Our Plan)_ | Export/import native JSON/zip archives via file picker, Google Drive, AirDrop, Nearby Share.                           | **Pros:** Zero cost, no backend, complete user privacy.<br>**Cons:** Manual export/import rather than automatic background sync.                      |
| **Google Drive AppData Folder**                      | Uses Google Drive REST API to read/write backups in a hidden, app-specific folder in the user's personal Google Drive. | **Pros:** No server cost, uses user's Google account.<br>**Cons:** Requires OAuth Google Sign-In setup and manual conflict/sync logic in Kotlin/Java. |
| **Backend-as-a-Service (Supabase / Firebase)**       | Replaces or bridges local DB with a cloud database (PostgreSQL / Firestore) with offline persistence.                  | **Pros:** True cross-platform automatic sync between Android and iOS.<br>**Cons:** Requires user accounts/auth and hosting infrastructure.            |
| **SQLite Sync Engines (PowerSync / ElectricSQL)**    | Keeps Room/SQLite on the device and syncs changes bi-directionally to a cloud backend.                                 | **Pros:** Full offline-first support.<br>**Cons:** Requires running a cloud sync service/backend.                                                     |

---

### 2. What else is needed to get iOS CloudKit Sync working again?

Beyond the paid Apple Developer Account and provisioning profile, here is the checklist required by `NSPersistentCloudKitContainer`:

#### A. Xcode Capabilities & Entitlements

1. **iCloud Capability**: Ensure the App ID in the Apple Developer Portal has the **iCloud (CloudKit)** capability enabled.
2. **Container Identifier**: The container in [MonsterCards.entitlements](file:///Users/tom/Projects/MonsterCards/iOS/MonsterCards/MonsterCards.entitlements) (`iCloud.com.majinnaibu.MonsterCards.MonsterCards`) must match the container ID registered to your developer team.
3. **Background Modes (Silent Push Notifications)**:
   - In Xcode under **Signing & Capabilities**, add **Background Modes** and check **Remote notifications**.
   - CloudKit uses silent push notifications to wake the app in the background and sync changes immediately when modified on another device.

#### B. CloudKit Rules for Core Data Models ([MonsterCards.xcdatamodeld](file:///Users/tom/Projects/MonsterCards/iOS/MonsterCards/MonsterCards.xcdatamodeld))

`NSPersistentCloudKitContainer` enforces strict schema constraints that regular Core Data does not:

- **All attributes must be Optional or have Default Values**: CloudKit cannot guarantee record availability during partial sync.
- **All relationships must be Optional and have an Inverse**: Required so CloudKit can resolve graph links in any order.
- **No Unique Constraints**: CloudKit does not support Core Data's unique constraints (uniqueness must be handled in application logic).

#### C. CloudKit Dashboard Schema Deployment

1. When developing locally in Xcode, Core Data automatically pushes record types and fields to the **CloudKit Development Environment**.
2. **Before releasing to TestFlight / App Store**, you must log in to the [Apple CloudKit Console](https://icloud.developer.apple.com/), select the container, and click **Deploy Schema to Production**. (Production apps cannot query development schema).

#### D. Graceful Fallback in Code ([Persistence.swift](file:///Users/tom/Projects/MonsterCards/iOS/MonsterCards/Persistence.swift))

- Update `Persistence.swift` so that if CloudKit is unavailable (e.g., user is signed out of iCloud or in airplane mode), it does not crash on `fatalError()`, but rather functions as a local-only database and syncs automatically once iCloud becomes reachable.
