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

| ID      | Title                                                                                                           | Project | Status  | Type                |
|:------|:--------------------------------------------------------------------------------------------------------------|:------|:------|:------------------|
| MCR-001 | Clean up legacy Bukkit plugin and Maven leftovers from repository root                                          | Shared  | Fixed   | [Chore](#mcr-001)   |
| MCR-002 | Modernize iOS Xcode project configuration, build pipeline, and dependency management                            | iOS     | Fixed   | [Chore](#mcr-002)   |
| MCR-003 | Modernize CoreData / CloudKit persistence layer and implement repository architecture                           | iOS     | Fixed   | [Feature](#mcr-003) |
| MCR-004 | Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)              | iOS     | Fixed   | [Feature](#mcr-004) |
| MCR-005 | Modernize SwiftUI architecture, NavigationStack, and Observation framework                                      | iOS     | Fixed   | [Feature](#mcr-005) |
| MCR-006 | Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions                       | iOS     | Fixed   | [Feature](#mcr-006) |
| MCR-007 | Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards                     | iOS     | Fixed   | [Feature](#mcr-007) |
| MCR-008 | Implement Collections and Encounters management with CR/XP summary metrics                                      | iOS     | Fixed   | [Feature](#mcr-008) |
| MCR-009 | Implement unified local Full-Text Search and remote Open5e API live search                                      | iOS     | Fixed   | [Feature](#mcr-009) |
| MCR-010 | Build comprehensive multi-section 5e Monster Editor suite with validation and live preview                      | iOS     | Fixed   | [Feature](#mcr-010) |
| MCR-011 | Implement QuickLook Preview Extension, custom document types (.monster), and system sharing                     | iOS     | Fixed   | [Feature](#mcr-011) |
| MCR-012 | Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows                       | iOS     | Fixed   | [Feature](#mcr-012) |
| MCR-013 | Add gameSystem and sourceLabel fields to Monster entity, editor, and UI tag bubbles                             | Android | Fixed   | [Feature](#mcr-013) |
| MCR-014 | Create isolated ReferenceMonster database table, repository, and JSON ingestion pipeline                        | Android | Fixed   | [Feature](#mcr-014) |
| MCR-015 | Implement user-initiated compendium downloader with 3rd-party disclaimer and legal confirmation modal           | Android | Fixed   | [Feature](#mcr-015) |
| MCR-016 | Implement Git commit SHA and HTTP ETag update checker with atomic source replacement                            | Android | Fixed   | [Feature](#mcr-016) |
| MCR-017 | Integrate reference compendiums into Search with source tag filters and badges                                  | Android | Fixed   | [Feature](#mcr-017) |
| MCR-018 | Support tap-to-preview and one-tap cloning/importing from ReferenceMonsters into user library                   | Android | Fixed   | [Feature](#mcr-018) |
| MCR-019 | Add gameSystem and sourceLabel attributes to CoreData Monster entity, editor, and UI tag bubbles                | iOS     | Fixed   | [Feature](#mcr-019) |
| MCR-020 | Create isolated ReferenceMonster CoreData entity, repository, and JSON ingestion pipeline                       | iOS     | Fixed   | [Feature](#mcr-020) |
| MCR-021 | Implement user-initiated compendium downloader with 3rd-party disclaimer and legal confirmation modal           | iOS     | Fixed   | [Feature](#mcr-021) |
| MCR-022 | Implement Git commit SHA and HTTP ETag update checker with atomic source replacement                            | iOS     | Fixed   | [Feature](#mcr-022) |
| MCR-023 | Integrate reference compendiums into Search with source tag filters and badges                                  | iOS     | Fixed   | [Feature](#mcr-023) |
| MCR-024 | Support tap-to-preview and one-tap cloning/importing from ReferenceMonsters into user library                   | iOS     | Fixed   | [Feature](#mcr-024) |
| MCR-025 | Remove the open5e online tab from search.                                                                       | iOS     | Fixed   | [Feature](#mcr-025) |
| MCR-026 | Imported compendium for pf2e and sf2e only lists 9 total monsters. That is nowhere near the correct numbers.    | iOS     | Fixed   | [Feature](#mcr-026) |
| MCR-027 | Do not call the repositories official.                                                                          | iOS     | Fixed   | [Feature](#mcr-027) |
| MCR-028 | When adding a monster from open5e.com set the source as open5e.com.                                             | iOS     | Fixed   | [Feature](#mcr-028) |
| MCR-029 | After downloading all compendiums there are no search results in the search compendiums tab.                    | iOS     | Fixed   | [Feature](#mcr-029) |
| MCR-030 | Text on compendium sources screen is very tiny.                                                                 | Android | Fixed   | [Feature](#mcr-030) |
| MCR-031 | The download buttons on the compendium page are magenta bubbles.                                                | Android | Fixed   | [Feature](#mcr-031) |
| MCR-032 | The 3rd party content notice frame is also tiny text.                                                           | Android | Fixed   | [Feature](#mcr-032) |
| MCR-033 | Stop calling the repositories community.                                                                        | Android | Fixed   | [Feature](#mcr-033) |
| MCR-034 | Stop calling the repositories community.                                                                        | iOS     | Fixed   | [Feature](#mcr-034) |
| MCR-035 | Search does not seem to search the downloaded compendiums at all.                                               | Android | Fixed   | [Feature](#mcr-035) |
| MCR-036 | Fix Room Database Migration MIGRATION_10_11 column nullability mismatch                                         | Android | Fixed   | [Bug](#mcr-036)     |
| MCR-037 | **Open5e import is only importing 500 monsters instead of the 3451 that android does using the /v2 api calls.** | iOS     | Pending | [Feature](#mcr-037) |
| MCR-038 | **It looks like the source book/origin is confused still.**                                                     | iOS     | Pending | [Feature](#mcr-038) |

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

<a id="mcr-013" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Add gameSystem and sourceLabel fields to Monster entity, editor, and UI tag bubbles
**ID:** MCR-013
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Add a structured `gameSystem` enum (e.g. `DND_5E`, `PF_2E`, `SF_2E`, `CUSTOM`) and optional `bookSource` / `sourceLabel` field to the Android `Monster` Room entity. Update the Monster Editor to let users choose the game system and book source, and display styled source tag bubbles (pill badges) across monster listings, search results, and combat dashboards.

**Requirements:**

- [x] Add `gameSystem` and `sourceLabel` columns to Room `Monster` entity and run database migration
- [x] Add Game System selector and Source / Book text field to `EditMonsterFragment` and viewmodel
- [x] Create `SourceTagView` component rendering colored pill tag bubbles (e.g., `[PF2e | Bestiary]`, `[5e | SRD]`, `[SF2e | Alien Archive]`)
- [x] Render source tag bubbles on Monster Library cards, Search results, and Dashboard cards

<a id="mcr-014" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Create isolated ReferenceMonster database table, repository, and JSON ingestion pipeline
**ID:** MCR-014
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Create an isolated `ReferenceMonster` Room entity and DAO matching all statblock fields of `Monster` plus compendium metadata (`sourceId`, `sourceLabel`, `gameSystem`, `bookSource`). Keep reference monsters completely separate from user library counts, regular user exports (`BinderExporter`, `Open5eExporter`), and user backup archives. Provide fast bulk-insertion of extracted JSON files.

**Requirements:**

- [x] Create `ReferenceMonster` Room entity, DAO, and full-text search indexes
- [x] Ensure user library exports and collection operations exclusively query user `Monster` records, ignoring reference monsters
- [x] Implement fast background JSON ingestion pipeline mapping external schemas (PF2e, SF2e, Open5e) into `ReferenceMonster` records
- [x] Cache extracted reference entities on local disk/database so import workflows reuse existing data without re-downloading

<a id="mcr-015" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Implement user-initiated compendium downloader with 3rd-party disclaimer and legal confirmation modal
**ID:** MCR-015
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Add a Compendium Sources screen in Android settings where users can view available community content repositories (PF2e, SF2e, Open5e, etc.) and tap to download. Tapping download presents a mandatory legal disclaimer/confirmation dialog warning the user that content is third-party, fetched from the Git repository URL, and not created or owned by this app.

**Requirements:**

- [x] Build Compendium Sources management UI in Settings listing available repository sources with download status and file sizes
- [x] Implement confirmation modal dialog displaying the target Git repo URL, 3rd-party content disclaimer, and user acknowledgment checkbox/button
- [x] Initiate background download and extraction service upon user confirmation with progress bar and cancellation support
- [x] Design extensible source configuration model allowing new community sources to be added easily

<a id="mcr-016" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Implement Git commit SHA and HTTP ETag update checker with atomic source replacement
**ID:** MCR-016
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Implement an update checking service for downloaded compendiums that checks remote Git commit SHAs (via GitHub API) or HTTP `ETag` / `Last-Modified` headers. When an update is confirmed and downloaded by the user, atomically replace all existing `ReferenceMonster` records for that specific source rather than appending duplicates.

**Requirements:**

- [x] Store source metadata (`sourceId`, `lastCommitSha`, `lastEtag`, `lastUpdatedTimestamp`, `monsterCount`) in `SharedPreferences` / Room
- [x] Implement remote update check querying GitHub commit SHA or HTTP `If-None-Match` header to detect upstream repository changes
- [x] Display "Update Available" badge/button in Sources settings when newer commit/hash is detected
- [x] Perform atomic database replacement per `sourceId` in a single Room transaction (delete old source records and insert new version)

<a id="mcr-017" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Integrate reference compendiums into Search with source tag filters and badges
**ID:** MCR-017
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Update `SearchFragment` and `SearchResultsRecyclerViewAdapter` to perform unified full-text search across both user library monsters and offline `ReferenceMonster` records, displaying source tag bubbles and filter chips.

**Requirements:**

- [x] Update `MonsterRepository.searchAll` to query both `Monster` and `ReferenceMonster` tables concurrently
- [x] Render source tag bubbles (`[PF2e]`, `[SF2e]`, `[Local]`, `[5e]`) on all search result items
- [x] Add horizontal filter chips / filter bottom sheet to filter search results by game system and source compendium
- [x] Ensure debounced, responsive UI performance across large reference catalogs (5,000+ monsters)

<a id="mcr-018" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Support tap-to-preview and one-tap cloning/importing from ReferenceMonsters into user library
**ID:** MCR-018
**Project:** Android
**Status:** Fixed
**Type:** Feature


**Description:**
Allow users to tap on any reference compendium monster in search results to preview its full statblock card, and provide a one-tap "Import to Library" action that clones the reference entity into the user's editable `Monster` table.

**Requirements:**

- [x] Present full statblock detail view/dialog when tapping a `ReferenceMonster` search result
- [x] Provide prominent "Import to Library" button with confirmation feedback
- [x] Clone `ReferenceMonster` attributes into a new editable `Monster` entity and save to Room
- [x] Support adding directly into an existing Collection upon import

<a id="mcr-019" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Add gameSystem and sourceLabel attributes to CoreData Monster entity, editor, and UI tag bubbles
**ID:** MCR-019
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Add `gameSystem` (enum string) and `sourceLabel` attributes to the CoreData `Monster` entity model. Update the SwiftUI monster editor suite with Game System and Book Source selectors, and render styled source tag bubbles across all monster cards and listing views.

**Requirements:**

- [x] Add `gameSystem` and `sourceLabel` attributes to `MonsterCards.xcdatamodeld`
- [x] Add Game System picker (`D&D 5e`, `Pathfinder 2e`, `Starfinder 2e`, `Custom`) and Source/Book field to `EditMonsterView`
- [x] Create `SourceTagView` SwiftUI component rendering styled pill badges
- [x] Render source tag bubbles in `LibraryView`, `Search` results, and `DashboardView`

<a id="mcr-020" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Create isolated ReferenceMonster CoreData entity, repository, and JSON ingestion pipeline
**ID:** MCR-020
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Create an isolated `ReferenceMonster` CoreData entity matching all statblock properties of `Monster` plus compendium metadata (`sourceId`, `sourceLabel`, `gameSystem`, `bookSource`). Keep reference monsters excluded from user library counts, regular exports (`BinderExporter`, `Open5eExporter`), and CloudKit user sync containers.

**Requirements:**

- [x] Add `ReferenceMonster` entity to `MonsterCards.xcdatamodeld` (local non-cloud sync configuration)
- [x] Ensure exporters and user library queries filter exclusively for user `Monster` entities
- [x] Build background batch ingestion pipeline mapping extracted JSON files into `ReferenceMonster` entities using `NSBatchInsertRequest`
- [x] Cache extracted entities locally to reuse during import workflows without re-downloading

<a id="mcr-021" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement user-initiated compendium downloader with 3rd-party disclaimer and legal confirmation modal
**ID:** MCR-021
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Add a Compendium Sources view in iOS Settings where users can view available community repositories (PF2e, SF2e, Open5e) and tap to download. Tapping download presents a legal disclaimer/confirmation sheet warning that content is third-party, fetched from the Git repository URL, and not created or owned by this app.

**Requirements:**

- [x] Create `CompendiumSourcesView` in iOS Settings listing available content repositories
- [x] Implement disclaimer confirmation sheet displaying target Git repo URL, 3rd-party legal notice, and confirmation button
- [x] Implement background `URLSessionDownloadTask` and zip decompression pipeline with progress bar and cancellation
- [x] Design extensible source registry allowing new community repositories to be registered easily

<a id="mcr-022" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Implement Git commit SHA and HTTP ETag update checker with atomic source replacement
**ID:** MCR-022
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Implement an update checking service for iOS compendiums that checks remote Git commit SHAs or HTTP `ETag` headers. When an update is downloaded, atomically replace all existing `ReferenceMonster` records for that specific source rather than appending duplicates.

**Requirements:**

- [x] Store compendium source metadata (`sourceId`, `lastCommitSha`, `lastEtag`, `lastUpdatedDate`, `monsterCount`) in `UserDefaults`
- [x] Check remote GitHub commit SHA or HTTP `If-None-Match` header to detect upstream repository updates
- [x] Display "Update Available" indicator in `CompendiumSourcesView`
- [x] Perform atomic replacement per `sourceId` via `NSBatchDeleteRequest` and `NSBatchInsertRequest` in a single CoreData background context save

<a id="mcr-023" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Integrate reference compendiums into Search with source tag filters and badges
**ID:** MCR-023
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Update the iOS `Search` view to perform unified asynchronous search across both local library monsters and offline `ReferenceMonster` records, displaying source tag bubbles and filter controls.

**Requirements:**

- [x] Update `MonsterRepository` to query `Monster` and `ReferenceMonster` entities concurrently
- [x] Render source tag bubbles (`[PF2e]`, `[SF2e]`, `[5e]`, `[Local]`) on search result rows
- [x] Add source filter menu / chips in `Search` view to filter results by game system and compendium source
- [x] Ensure fast, debounced UI response across large reference catalogs

<a id="mcr-024" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Support tap-to-preview and one-tap cloning/importing from ReferenceMonsters into user library
**ID:** MCR-024
**Project:** iOS
**Status:** Fixed
**Type:** Feature


**Description:**
Allow users to tap on any reference compendium monster in search results to preview its full statblock card, and provide a one-tap "Import to Library" action that clones the reference entity into the user's editable CoreData `Monster` store.

**Requirements:**

- [x] Present `MonsterCardView` sheet when tapping a `ReferenceMonster` search result
- [x] Add prominent "Import to Library" toolbar button with confirmation feedback
- [x] Clone `ReferenceMonster` attributes into a new editable `Monster` entity and persist to CoreData
- [x] Support adding directly into an existing Collection from the preview sheet

<a id="mcr-025" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Remove the open5e online tab from search.
**ID:** MCR-025
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
Describe task objectives and implementation requirements here.

- [x]

<a id="mcr-026" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Imported compendium for pf2e and sf2e only lists 9 total monsters. That is nowhere near the correct numbers.
**ID:** MCR-026
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
Describe task objectives and implementation requirements here.

- [x]

<a id="mcr-027" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Do not call the repositories official.
**ID:** MCR-027
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
They are unofficial. Also do not specify the OGL/ORC licenses. Say under their own 3rd party licenses.

- [x]

<a id="mcr-028" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### When adding a monster from open5e.com set the source as open5e.com.
**ID:** MCR-028
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
I like the tag bubble in the search results with `5e | Open5e` and hope the monster would keep the same after being imported.
Describe task objectives and implementation requirements here.

- [x]

<a id="mcr-029" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### After downloading all compendiums there are no search results in the search compendiums tab.
**ID:** MCR-029
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
Describe task objectives and implementation requirements here.
- [x]

<a id="mcr-030" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Text on compendium sources screen is very tiny.
**ID:** MCR-030
**Project:** Android
**Status:** Fixed
**Type:** Feature

**Description:**
Make it the same size as text in the rest of the app. Are we not using the same theme and components.
- [x]

<a id="mcr-031" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### The download buttons on the compendium page are magenta bubbles.
**ID:** MCR-031
**Project:** Android
**Status:** Fixed
**Type:** Feature

**Description:**
Are we missing themes/styles. Check elsewhere in the layouts. I remember seeing another magenta button somewhere earlier.
- [x]

<a id="mcr-032" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### The 3rd party content notice frame is also tiny text.
**ID:** MCR-032
**Project:** Android
**Status:** Fixed
**Type:** Feature

**Description:**
Plese look for other occurences of this issue in the layouts when fixing it.
- [x]

<a id="mcr-033" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Stop calling the repositories community.
**ID:** MCR-033
**Project:** Android
**Status:** Fixed
**Type:** Feature

**Description:**
They are unrelated 3rd party sources and we are simply helping the user download the data and import it. They have no connection to us.
- [x]

<a id="mcr-034" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Stop calling the repositories community.
**ID:** MCR-034
**Project:** iOS
**Status:** Fixed
**Type:** Feature

**Description:**
They are unrelated 3rd party sources and we are simply helping the user download the data and import it. They have no connection to us.
- [x]

<a id="mcr-035" class="task" data-project="android" data-status="done" data-task-type="feature"></a>
### Search does not seem to search the downloaded compendiums at all.
**ID:** MCR-035
**Project:** Android
**Status:** Fixed
**Type:** Feature

**Description:**
Even with open5e and pf2e downloaded bugbear gives me no results. Give the search ui a similar toggle as iOS where we can search the library or the compendiums specifically.
- [x]

<a id="mcr-036" class="task" data-project="android" data-status="done" data-task-type="bug"></a>
### Fix Room Database Migration MIGRATION_10_11 column nullability mismatch
**ID:** MCR-036
**Project:** Android
**Status:** Fixed
**Type:** Bug

**Description:**
### Root Cause
In MIGRATION_10_11 (MonsterCardsApplication.java), the ALTER TABLE statements created columns custom_game_system, custom_origin, and book_source with NOT NULL DEFAULT ''. However, in Monster.java and ReferenceMonster.java, those fields lacked @NonNull annotations, causing Room to expect notNull = false in TableInfo. On app launch, Room schema validation (onValidateSchema) fails with IllegalStateException: Migration didn't properly handle: monsters.

### Fix Instructions
1. Annotate customGameSystem, customOrigin, and bookSource with @NonNull in Monster.java and ReferenceMonster.java so that all String fields are consistently non-null.
2. In MIGRATION_10_11 (MonsterCardsApplication.java), ensure ALTER TABLE statements use TEXT NOT NULL DEFAULT '':
   - monsters: custom_game_system, origin, custom_origin, book_source
   - reference_monsters: custom_game_system, origin, custom_origin
3. Re-generate Room schema file 11.json and verify build with ./gradlew assembleDebug and ./gradlew test.

<a id="mcr-037" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Open5e import is only importing 500 monsters instead of the 3451 that android does using the /v2 api calls.
**ID:** MCR-037
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Describe task objectives and implementation requirements here.
- [ ]

<a id="mcr-038" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### It looks like the source book/origin is confused still.
**ID:** MCR-038
**Project:** iOS
**Status:** Pending
**Type:** Feature

**Description:**
Describe task objectives and implementation requirements here.
- [ ]

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
