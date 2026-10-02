---
projects:
  - label: 'iOS'
    path: 'iOS'
    prefix: 'IOS'
    value: 'ios'
  - label: 'Android'
    path: 'Android'
    prefix: 'AND'
    value: 'android'
  - label: 'Shared'
    path: ''
    prefix: 'SHR'
    value: 'shared'
task-statuses:
  - value: 'planning'
    label: 'Planning'
    description: "The task is being scoped and conceptualized. We still don't know what we want."
  - value: 'triage'
    label: 'Triage'
    description: "The task is being evaluated and prioritized. We don't know how we want to do it."
  - value: 'pending'
    label: 'Pending'
    description: 'The task is ready to be acted on.'
  - value: 'in_progress'
    label: 'In Progress'
    description: 'The task is currently being worked on.'
  - value: 'done'
    label: 'Fixed'
    description: 'The task has been completed.'
  - value: 'blocked'
    label: 'Blocked'
    description: 'The task cannot proceed due to an obstacle or dependency.'
  - value: 'cancelled'
    label: 'Cancelled'
    description: 'The task was decided against or is no longer relevant.'
  - value: 'research'
    label: 'Researching'
    description: 'This task needs more research before planning.'
task-types:
  - value: 'lint'
    prefix: 'LNT'
    label: 'Lint'
    description: 'The task involves fixing a linting error or other warning.'
  - value: 'bug'
    prefix: 'BUG'
    label: 'Bug'
    description: 'The task is a bug to fix.'
  - value: 'feature'
    prefix: 'ENH'
    label: 'Feature'
    description: 'The task is a new feature to implement.'
  - value: 'chore'
    prefix: 'CHR'
    label: 'Chore'
    description: 'The task is a routine maintenance or administrative task.'
---

# MonsterCards Task Tracker

This document serves as the Single Source of Truth (SSOT) for tracking tasks, modernization milestones, feature parity implementations, and bug fixes across the MonsterCards Android and iOS apps.

## Instructions for Working with Tasks

### Quick Start (Environment Setup)

Before running task commands, source the helper environment script in your shell to load convenient aliases and paths:

```bash
source ./scripts/env.sh
```

> **Note**: For local configurations that should not be committed (such as custom environment variables, agent endpoints, or tokens), create a `scripts/env.sh.local` or `.env` file. It will be automatically sourced by `env.sh` and is ignored by Git.

### Command Reference

You can manage tasks using the provided automation scripts in `./scripts/` (or via their aliases):

- **List tasks**:
  - `get-tasks` (or `./scripts/get-tasks`): Show active pending tasks.
  - `get-planning-tasks` (or `get-tasks --planning`): Show tasks in _Planning_ or _Triage_ status.
  - `get-tasks --all`: List all tasks regardless of status.
  - `get-tasks --accept "Status:In Progress"` / `get-tasks --reject "Project:Android"`: Filter tasks.
- **View a specific task**:
  - `get-task MCR-001` (or `./scripts/get-task 1`): Inspect task details, requirements, and metadata.
  - `get-task MCR-001 --json`: Output task data in JSON format.
- **Add a new task**:
  - `add-task "Task Title" --project=ios --type=feature --status=triage`
  - (Optionally supply `-d "Description and checklist"`)
- **Update an existing task**:
  - `update-task MCR-001 --status in_progress`
  - `update-task MCR-001 --title "New Title"`
  - `update-task MCR-001 --status done --check-all`
  - `update-task MCR-001 --append-description "- [ ] Additional subtask"`
- **Synchronize document**:
  - `update-tasks` (or `./scripts/update-tasks`): Re-indexes task IDs, re-renders the summary progress table, and regenerates metadata enum tables.

### Workflow Guidelines

1. **Do Not Edit the Summary Table Manually**: The summary table below is automatically regenerated from the detailed task entries. Use `./scripts/add-task` and `./scripts/update-task`, or edit the detailed task blocks directly and run `./scripts/update-tasks`.
2. **Anchor Tags as SSOT**: Each task in `## Detailed Tasks` is defined by an anchor tag:
   `<a id="MCR-XXX" class="task" data-project="PROJECT" data-status="STATUS" data-task-type="TYPE"></a>`
   The attributes on this anchor tag are the canonical source of truth for the task's state.
3. **Task Lifecycle**:
   - **Planning**: Conceptualization and scoping phase.
   - **Triage**: Requirements known, pending prioritization or design.
   - **Pending**: Ready to be picked up for implementation.
   - **In Progress**: Actively being worked on.
   - **Fixed / Done**: Completed and verified.
   - **Blocked**: Waiting on external dependencies.
   - **Cancelled**: Abandoned or obsolete.
   - **Researching**: Requires investigation before planning.

<a id="tasks-summary"></a>

## Overall Progress

<a id="tasks-list"></a>

| ID      | Title                                                                                                                               | Project | Status  | Type                |
|:------|:----------------------------------------------------------------------------------------------------------------------------------|:------|:------|:------------------|
| MCR-001 | Clean up legacy Bukkit plugin and Maven leftovers from repository root                                                              | Shared  | Fixed   | [Chore](#mcr-001)   |
| MCR-002 | Modernize iOS Xcode project configuration, build pipeline, and dependency management                                                | iOS     | Fixed   | [Chore](#mcr-002)   |
| MCR-003 | Modernize CoreData / CloudKit persistence layer and implement repository architecture                                               | iOS     | Fixed   | [Feature](#mcr-003) |
| MCR-004 | Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)                                  | iOS     | Fixed   | [Feature](#mcr-004) |
| MCR-005 | Modernize SwiftUI architecture, NavigationStack, and Observation framework                                                          | iOS     | Fixed   | [Feature](#mcr-005) |
| MCR-006 | Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions                                           | iOS     | Fixed   | [Feature](#mcr-006) |
| MCR-007 | Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards                                         | iOS     | Fixed   | [Feature](#mcr-007) |
| MCR-008 | Implement Collections and Encounters management with CR/XP summary metrics                                                          | iOS     | Fixed   | [Feature](#mcr-008) |
| MCR-009 | Implement unified local Full-Text Search and remote Open5e API live search                                                          | iOS     | Fixed   | [Feature](#mcr-009) |
| MCR-010 | Build comprehensive multi-section 5e Monster Editor suite with validation and live preview                                          | iOS     | Fixed   | [Feature](#mcr-010) |
| MCR-011 | Implement QuickLook Preview Extension, custom document types (.monster), and system sharing                                         | iOS     | Fixed   | [Feature](#mcr-011) |
| MCR-012 | Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows                                           | iOS     | Fixed   | [Feature](#mcr-012) |
| MCR-013 | **Implement remote API search client supporting Open5e REST query with debouncing, error handling, and cancellation**               | Android | Pending | [Feature](#mcr-013) |
| MCR-014 | **Add a registry in settings for each of the search/import APIs**                                                                   | Android | Pending | [Feature](#mcr-014) |
| MCR-015 | **Add distinct source badges/icons on search result items to clearly differentiate local library monsters vs. remote API monsters** | Android | Pending | [Feature](#mcr-015) |
| MCR-016 | **Provide modular architecture for registering additional OGL/ORC API providers**                                                   | Android | Pending | [Feature](#mcr-016) |
| MCR-017 | **Add source toggle controls/filter sheet in Search UI allowing users to enable or disable specific remote sources**                | Android | Pending | [Feature](#mcr-017) |
| MCR-018 | **Support tap-to-preview for remote search results with one-tap import into local Room database**                                   | Android | Pending | [Feature](#mcr-018) |
| MCR-019 | **Add a registry in settings for each of the search/import APIs**                                                                   | iOS     | Pending | [Feature](#mcr-019) |
| MCR-020 | **Add distinct source badges/icons on search result items to clearly differentiate local library monsters vs. remote API monsters** | iOS     | Pending | [Feature](#mcr-020) |
| MCR-021 | **Provide modular architecture for registering additional OGL/ORC API providers**                                                   | iOS     | Pending | [Feature](#mcr-021) |
| MCR-022 | **Add source toggle controls/filter sheet in Search UI allowing users to enable or disable specific remote sources**                | iOS     | Pending | [Feature](#mcr-022) |
| MCR-023 | **Support tap-to-preview for remote search results with one-tap import into local CoreData database**                               | iOS     | Pending | [Feature](#mcr-023) |

---

<a id="task-details"></a>

## Detailed Tasks

<a id="mcr-001" class="task" data-project="shared" data-status="done" data-task-type="chore"></a>
### Clean up legacy Bukkit plugin and Maven leftovers from repository root
**ID:** MCR-001
**Project:** Shared
**Status:** Fixed
**Type:** Chore


**Description:**
Remove obsolete files that leaked into the root directory from an unrelated legacy Bukkit/Minecraft plugin (`MobScores`). Ensure the repository root cleanly represents only the MonsterCards cross-platform project.

**Requirements:**

- [x] Remove legacy Maven and Eclipse build metadata: `.classpath`, `.project`, `.settings/`, `pom.xml`, `Manifest.MF`, `MobScores.jardesc`
- [x] Remove legacy Bukkit Java source files in `src/main/java/com/majinnaibu/bukkitplugins/` and `src/main/resources/`
- [x] Remove outdated Bukkit documentation and project descriptor files: `ProjectDescription.json`, `ProjectDescription.md`, `Readme.txt`
- [x] Ensure `Project.json`, `Project.md`, and root `README.md` accurately describe the MonsterCards Android & iOS application
- [x] Update `.gitignore` to avoid re-introducing obsolete build artifacts

<a id="mcr-002" class="task" data-project="ios" data-status="done" data-task-type="chore"></a>
### Modernize iOS Xcode project configuration, build pipeline, and dependency management
**ID:** MCR-002
**Project:** iOS
**Status:** Fixed
**Type:** Chore


**Description:**
Update the iOS Xcode project to modern standards, targeting iOS 17.0+ / iOS 18.0+, enabling modern Swift concurrency settings, updating Swift Package dependencies, and resolving all build warnings.

**Requirements:**

- [x] Update Deployment Target to iOS 17.0 minimum (recommend iOS 17.6+)
- [x] Audit Swift Package Manager dependencies (`NetworkImage`, `MarkdownUI`, `swift-cmark`) to latest stable compatible releases
- [x] Enable Swift 5.10 / Swift 6 concurrency checking (`SWIFT_STRICT_CONCURRENCY=complete`)
- [x] Resolve all Xcode project build warnings, deprecations, and code signing configurations for local/simulator testing
- [x] Verify clean command-line builds via `xcodebuild` for both device and simulator destinations

<a id="mcr-003" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Modernize CoreData / CloudKit persistence layer and implement repository architecture
**ID:** MCR-003
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Modernize the data persistence stack in `Persistence.swift` and `MonsterCards.xcdatamodeld`. Ensure schema parity with Android's Room database (`Monster`, `Collection`, `CollectionMonster`, `DashboardMonster`), robust iCloud synchronization via `NSPersistentCloudKitContainer`, and clean separation of concerns using an async/actor-isolated repository pattern (`MonsterRepository`).

**Requirements:**

- [x] Update CoreData schema model to match Android data entities:
  - `Monster`: all 5e attributes, spellcasting, legendary/mythic actions, reactions, senses, damage types, condition immunities, source URL, image URL, custom tags
  - `Collection` & `CollectionMonster`: sort order, description, monster relationship, membership count
  - `DashboardMonster`: pinned status, display order, current HP, max HP, temporary notes/counter
- [x] Implement `NSPersistentCloudKitContainer` background sync with automatic merge policies and error recovery
- [x] Create an actor-isolated `MonsterRepository` protocol and implementation providing async CRUD operations, pagination, search, and batch mutations
- [x] Implement database seeding for development/previews (`DevContent`)

<a id="mcr-004" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)
**ID:** MCR-004
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Bring the iOS import and export engine to full parity with Android. Implement robust parsers and serializers for all supported tabletop formats, external APIs, and native archives.

**Requirements:**

- [x] `Open5eImporter` & `Open5eApiWrapper`: Fetch and parse monsters from the Open5e REST API (https://api.open5e.com/monsters/)
- [x] `TetraCubeMonsterImporter`: Complete full JSON parsing from Tetra-Cube 5e statblock generator including special abilities, traits, and layout options
- [x] `DnDBeyondImporter`: Parse character and monster JSON export payloads from D&D Beyond
- [x] `Pf2eImporter`: Parse Pathfinder 2e creature statblock JSON formats
- [x] `BinderImporter` / Native `.monster`: Native MonsterCards multi-monster archive and single-card format
- [x] `Open5eExporter`: Export local monster stat blocks into Open5e-compatible JSON schemas
- [x] `BinderExporter`: Export selected collections or entire libraries into shareable `.monster` / zip archives
- [x] `MonsterCardExporter`: Export formatted monster stat blocks as printable PDF, image, or markdown

<a id="mcr-005" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Modernize SwiftUI architecture, NavigationStack, and Observation framework
**ID:** MCR-005
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Migrate the iOS UI architecture from legacy SwiftUI patterns (`NavigationView`, `ObservableObject`, `@ObservedObject`, `@EnvironmentObject`) to modern iOS idioms: `NavigationStack`, `NavigationSplitView` (adaptive for iPhone and iPad), and Swift's `@Observable` macro (Observation framework) with structured concurrency (`async/await`, `@MainActor`).

**Requirements:**

- [x] Replace deprecated `NavigationView` in `ContentView`, `Library`, `Search`, and `Collections` with `NavigationStack` and type-safe navigation destinations
- [x] Migrate ViewModels (`MonsterViewModel`, `SkillViewModel`, `AbilityViewModel`, etc.) to Swift's `@Observable` macro
- [x] Support adaptive multi-column `NavigationSplitView` for iPadOS and landscape orientation
- [x] Standardize design tokens, color palettes, dark mode support, and 5e statblock parchment card styling
- [x] Implement modern SwiftUI controls: `.searchable`, `ContentUnavailableView`, `.refreshable`, swipe actions, and contextual menus

<a id="mcr-006" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions
**ID:** MCR-006
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Upgrade the Monster Library tab (`Library.swift`) to match Android functionality, providing a rich, high-performance monster management hub with filtering, sorting, multi-selection, and quick actions.

**Requirements:**

- [x] Implement filter bar and sheet supporting filters by Challenge Rating (range), Monster Type, Size, Alignment, Source, and Custom Tags
- [x] Implement sorting options: Name (A-Z, Z-A), Challenge Rating (Ascending/Descending), Date Added, Date Modified
- [x] Implement multi-selection mode for bulk actions: batch delete, add to collection, bulk export
- [x] Add swipe actions on monster rows: Quick Favorite, Pin to Dashboard, Duplicate, Delete
- [x] Add pull-to-refresh and empty-state placeholders with one-tap import triggers

<a id="mcr-007" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards
**ID:** MCR-007
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Replace the placeholder `Dashboard.swift` with a combat companion dashboard matching Android's encounter running capabilities. Allows Dungeon Masters to pin monster stat blocks, track current/max/temp hit points, manage turn orders, and view compact vital stats during tabletop sessions.

**Requirements:**

- [x] Create dashboard monster grid/list with vital stats glance (AC, HP bar, Speed, Passive Perception, Key Attacks)
- [x] Implement interactive Hit Point tracker: current HP, max HP, temporary HP, quick damage/healing steppers with dice roll helpers
- [x] Implement Pin/Unpin monster actions from Library, Search, and StatBlock views
- [x] Implement quick expand/collapse sheet to inspect full monster card details without losing dashboard context
- [x] Persist dashboard combat state across app restarts

<a id="mcr-008" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement Collections and Encounters management with CR/XP summary metrics
**ID:** MCR-008
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Build out the Collections tab (`Collections.swift`) and detail views to organize monsters into thematic collections, campaigns, locations, or combat encounters with automated challenge rating / encounter XP calculations.

**Requirements:**

- [x] Create, edit, and delete Collections with custom names, descriptions, and icon/color tags
- [x] Implement Collection Detail view displaying assigned monsters with sorting and filtering
- [x] Implement Add/Remove monsters picker with search and multi-select
- [x] Display encounter metrics: Total Monster Count, Average CR, Total XP, and 5e Encounter Difficulty estimate (Easy, Medium, Hard, Deadly)
- [x] Export collection as a standalone Binder archive or share sheet payload

<a id="mcr-009" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement unified local Full-Text Search and remote Open5e API live search
**ID:** MCR-009
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Modernize `Search.swift` to provide a unified search experience that queries both local library monsters and the remote Open5e REST API simultaneously with instant preview and import.

**Requirements:**

- [x] Local search across multiple fields: name, type, subtype, size, alignment, traits, actions, and tags
- [x] Remote search querying Open5e API with debounced user typing, loading indicators, and error resilience
- [x] Segmented results view showing Local Library matches vs. Online Open5e results
- [x] Tap-to-preview remote stat block with a single-tap "Import to Library" or "Add to Collection" action
- [x] Filter chips for quick category filtering (e.g. Beasts, Undead, Fiends, Dragons, Humanoids)

<a id="mcr-010" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Build comprehensive multi-section 5e Monster Editor suite with validation and live preview
**ID:** MCR-010
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Revamp the monster creation and editing suite (`EditMonster.swift` and subviews) to support every 5e statblock attribute with modern form controls, live modifier calculations, Markdown support for traits/actions, and side-by-side / toggleable card preview.

**Requirements:**

- [x] Basic Info: Name, Size, Type, Subtype, Alignment, Challenge Rating, XP, Source text/URL
- [x] Defense & Health: Armor Class, Armor Type, Hit Points, Hit Dice count and die size, Hit Dice modifier
- [x] Movement: Walk, Burrow, Climb, Fly (hover toggle), Swim speeds
- [x] Ability Scores: STR, DEX, CON, INT, WIS, CHA with automatic modifier calculation and manual override option
- [x] Proficiencies: Saving Throws and Skills with proficiency/expertise/half-proficiency levels
- [x] Vulnerabilities, Resistances, Damage Immunities, and Condition Immunities pickers
- [x] Senses & Languages: Darkvision, Blindsight, Tremorsense, Truesight, Passive Perception, Languages with telepathy
- [x] Markdown-enabled Action Lists: Special Traits, Actions, Bonus Actions, Reactions, Legendary Actions, Mythic Actions
- [x] Spellcasting Block: Caster level, spellcasting ability, save DC, spell attack bonus, spell slots by level, cantrips and prepared spells
- [x] Live preview mode to switch seamlessly between editor forms and rendered statblock card

<a id="mcr-011" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement QuickLook Preview Extension, custom document types (.monster), and system sharing
**ID:** MCR-011
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Configure iOS document handling for `.monster` and `.json` files, implement AirDrop sharing, system Share Sheet integration, and finish the `MonsterPreview` App Extension for system QuickLook previews in Files.app.

**Requirements:**

- [x] Register Uniform Type Identifiers (UTIs) for `com.majinnaibu.monstercards.monster` and JSON documents in `Info.plist`
- [x] Implement custom file import handling via `.onOpenURL` / document opening delegates
- [x] Implement `MonsterPreview` QuickLook preview extension rendering stat blocks directly in Files.app and AirDrop previews
- [x] Add Share Sheet integration to export and send monster cards via Messages, Mail, AirDrop, and cloud storage
- [x] Support Drag and Drop of monster files on iPadOS

<a id="mcr-012" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows
**ID:** MCR-012
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Establish a complete test suite covering data conversion, all format importers/exporters, CoreData repository operations, and critical SwiftUI user journeys.

**Requirements:**

- [x] Importer Unit Tests: Test fixtures for Tetra-Cube JSON, D&D Beyond character JSON, Open5e JSON, PF2e JSON, and native Binder formats
- [x] Exporter Unit Tests: Verify roundtrip fidelity for Open5e export and Binder archive export
- [x] Repository & Persistence Tests: In-memory CoreData stack testing CRUD, search predicates, and cascade deletion rules
- [x] UI Tests: Automated user journeys testing Monster creation, Library search & filter, Dashboard HP adjustments, and Collection creation

<a id="mcr-013" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Implement remote API search client supporting Open5e REST query with debouncing, error handling, and cancellation
**ID:** MCR-013
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Build an asynchronous remote API search client in the Android codebase that queries Open5e monsters (`api.open5e.com/monsters/?search=...`) with user input debouncing, network error resilience, and RxJava/Coroutine cancellation.

**Requirements:**

- [ ] Implement debounced API search query runner in `SearchFragment` / ViewModel
- [ ] Connect `Open5eApiWrapper` query methods for paginated search results
- [ ] Implement cancellation of in-flight search requests when user updates search input
- [ ] Add loading indicators and network error handling states

<a id="mcr-014" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Add a registry in settings for each of the search/import APIs
**ID:** MCR-014
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Implement an API Registry in the Android settings/preferences to configure, manage, enable/disable, and supply custom endpoints or credentials for external monster APIs (e.g. Open5e, community OGL/ORC providers).

**Requirements:**

- [ ] Create API Source configuration model with name, base URL, enabled toggle, and rate limit settings
- [ ] Build Settings UI screen/section for managing registered search and import API providers
- [ ] Persist API provider settings in SharedPreferences / Room configuration store
- [ ] Allow adding custom REST endpoints conforming to Open5e / ORC schemas

<a id="mcr-015" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Add distinct source badges/icons on search result items to clearly differentiate local library monsters vs. remote API monsters
**ID:** MCR-015
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Update `SearchResultsRecyclerViewAdapter` and `search_result_list_item.xml` to display distinct badges/icons representing the source origin (Local Room Database, Open5e, PF2e, SF2e, etc.).

**Requirements:**

- [ ] Add source badge UI element to `search_result_list_item.xml`
- [ ] Display icon and text pill badge for Local Library vs. Open5e / Remote sources
- [ ] Style badges with distinctive color coding for rapid identification

<a id="mcr-016" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Provide modular architecture for registering additional OGL/ORC API providers
**ID:** MCR-016
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Create an extensible API provider interface/abstraction (`RemoteMonsterApiProvider`) allowing pluggable search and import providers for Pathfinder 2e (PF2e), Starfinder 2e (SF2e), and other ORC/OGL content sources.

**Requirements:**

- [ ] Define `RemoteMonsterApiProvider` interface with search, fetch-by-id, and pagination contracts
- [ ] Implement provider registry to discover and execute queries across all registered active providers
- [ ] Create data mapping adaptors from external API response schemas to internal `Monster` domain models

<a id="mcr-017" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Add source toggle controls/filter sheet in Search UI allowing users to enable or disable specific remote sources
**ID:** MCR-017
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Add filter chips and/or a source selection bottom sheet in `SearchFragment` so users can dynamically filter search results by specific remote API sources or restrict search to local content only.

**Requirements:**

- [ ] Add horizontal source filter chips or filter button in `SearchFragment`
- [ ] Support toggling Local, Open5e, and future API provider sources individually
- [ ] Filter live search results according to selected active source filters

<a id="mcr-018" class="task" data-project="android" data-status="pending" data-task-type="feature"></a>
### Support tap-to-preview for remote search results with one-tap import into local Room database
**ID:** MCR-018
**Project:** Android
**Status:** Pending
**Type:** Feature

**Description:**
Allow users to tap on remote search results to preview the full statblock sheet and import the monster into the local Room database with one tap.

**Requirements:**

- [ ] Open monster preview dialog or detail screen when tapping a remote search item
- [ ] Add persistent "Import to Library" action button with confirmation feedback
- [ ] Save imported monster to Room database and update local library state immediately

<a id="mcr-019" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Add a registry in settings for each of the search/import APIs
**ID:** MCR-019
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Implement a configurable API Source Registry in iOS App Settings to manage endpoint URLs, toggles, rate limits, and custom feeds for external monster providers.

**Requirements:**

- [ ] Create `ApiSourceConfig` model and `ApiRegistry` observable service in Swift
- [ ] Add API Sources management view to iOS Settings/Preferences
- [ ] Support enabling, disabling, and adding custom OGL/ORC API endpoint URLs
- [ ] Persist settings using `AppStorage` / UserDefaults

<a id="mcr-020" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Add distinct source badges/icons on search result items to clearly differentiate local library monsters vs. remote API monsters
**ID:** MCR-020
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Update the iOS Search list rows with visual badges/icons indicating whether each monster is stored locally in CoreData or surfaced from Open5e/remote API providers.

**Requirements:**

- [ ] Create `SourceBadgeView` component with icon and badge styling
- [ ] Render source badges in `Search` result rows (Local Library vs. Open5e / Remote)
- [ ] Provide distinct color-coded chips for game systems and API origins

<a id="mcr-021" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Provide modular architecture for registering additional OGL/ORC API providers
**ID:** MCR-021
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Design a modular Swift protocol (`RemoteMonsterProvider`) and registry to dynamically support multiple external game system endpoints (Open5e, PF2e, SF2e, and community ORC/OGL APIs).

**Requirements:**

- [ ] Define async `RemoteMonsterProvider` protocol with search, detail retrieval, and pagination
- [ ] Implement `RemoteProviderRegistry` managing active search providers
- [ ] Provide schema normalizers converting heterogeneous API JSON payloads into `Monster` entities

<a id="mcr-022" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Add source toggle controls/filter sheet in Search UI allowing users to enable or disable specific remote sources
**ID:** MCR-022
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Implement a source filtering popover/sheet in iOS `Search` view allowing users to selectively include or exclude remote sources (Open5e, future PF2e/SF2e) from live search queries.

**Requirements:**

- [ ] Add source filter menu / sheet to iOS `Search` view toolbar
- [ ] Provide toggles for Local Library, Open5e, and registered API providers
- [ ] Dynamically update unified search execution based on selected source toggles

<a id="mcr-023" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Support tap-to-preview for remote search results with one-tap import into local CoreData database
**ID:** MCR-023
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Enhance remote search result interaction with a dedicated statblock preview sheet and an "Import to Library" / "Add to Collection" one-tap action that saves the entity into CoreData.

**Requirements:**

- [ ] Support tapping remote search results to present full `MonsterCardView` preview
- [ ] Add one-tap "Import to Library" toolbar/floating button on preview sheet
- [ ] Save monster entity into CoreData persistence container and post notification
- [ ] Support adding directly into an existing Collection from the preview sheet

---

## Notes

- This file uses monotonic 3-digit identifiers (e.g. `MCR-001`, `MCR-002`).
- Run `./scripts/update-tasks` whenever manually editing tasks to keep all tables synchronized.

#### Task System Documentation (Requirements for Agents)

- **Single Source of Truth (SSOT)**:
  - The `id` attribute on the anchor tag is the SSOT for the Task ID.
  - The `data-status` attribute on the anchor tag is the SSOT for the Status value.
  - The `data-task-type` attribute on the anchor tag is the SSOT for the Type value.
- **Detailed Task Identification**: Any anchor tag with the `task` class identifies the start of a task details section.
- **Rendered Summary Views**:
  - The **Overall Progress Table** is a rendered summary of task data. It must be updated whenever the SSOT attributes are modified.
  - The **Status and Type Descriptions** sections at the end of the doc are rendered versions of the YAML frontmatter (task-statuses and task-types respectively).
- **ID Formatting**: All IDs follow the `MCR-XXX` format where `XXX` is a monotonic 3-digit number.
- **Task Lifecycle**:
  1. **Planning**: The task is being scoped and conceptualized. We still don't know what we want.
  2. **Triage**: Requirements are identified, but we don't know how we want to do it.
  3. **Pending**: Information complete, ready for implementation.
  4. **In Progress**: Work active.
  5. **Fixed / Done**: Work completed.
  6. **Blocked**: Work stopped by external dependency.
  7. **Cancelled**: Task discarded.

#### Projects (Rendered from frontmatter projects)
| Value   | Label   | Prefix | Path    |
|:------|:------|:-----|:------|
| ios     | iOS     | IOS    | iOS     |
| android | Android | AND    | Android |
| shared  | Shared  | SHR    |         |

#### Task Statuses (Rendered from frontmatter task-statuses)
| Value       | Label       | Description                                                                      |
|:----------|:----------|:-------------------------------------------------------------------------------|
| planning    | Planning    | The task is being scoped and conceptualized. We still don't know what we want.   |
| triage      | Triage      | The task is being evaluated and prioritized. We don't know how we want to do it. |
| pending     | Pending     | The task is ready to be acted on.                                                |
| in_progress | In Progress | The task is currently being worked on.                                           |
| done        | Fixed       | The task has been completed.                                                     |
| blocked     | Blocked     | The task cannot proceed due to an obstacle or dependency.                        |
| cancelled   | Cancelled   | The task was decided against or is no longer relevant.                           |
| research    | Researching | This task needs more research before planning.                                   |

#### Task Types (Rendered from frontmatter task-types)
| Value   | Label   | Prefix | Description                                                |
|:------|:------|:-----|:---------------------------------------------------------|
| lint    | Lint    | LNT    | The task involves fixing a linting error or other warning. |
| bug     | Bug     | BUG    | The task is a bug to fix.                                  |
| feature | Feature | ENH    | The task is a new feature to implement.                    |
| chore   | Chore   | CHR    | The task is a routine maintenance or administrative task.  |

<!-- Table Sort Injection -->
<!-- @import "assets/table-sort.css" -->
<!-- @import "assets/table-sort.js" -->
<link rel="stylesheet" href="assets/table-sort.css">
<script src="assets/table-sort.js"></script>
