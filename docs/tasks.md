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
  - `get-task FGJ-001` (or `./scripts/get-task 1`): Inspect task details, requirements, and metadata.
  - `get-task FGJ-001 --json`: Output task data in JSON format.
- **Add a new task**:
  - `add-task "Task Title" --project=ios --type=feature --status=triage`
  - (Optionally supply `-d "Description and checklist"`)
- **Update an existing task**:
  - `update-task FGJ-001 --status in_progress`
  - `update-task FGJ-001 --title "New Title"`
  - `update-task FGJ-001 --status done --check-all`
  - `update-task FGJ-001 --append-description "- [ ] Additional subtask"`
- **Synchronize document**:
  - `update-tasks` (or `./scripts/update-tasks`): Re-indexes task IDs, re-renders the summary progress table, and regenerates metadata enum tables.

### Workflow Guidelines

1. **Do Not Edit the Summary Table Manually**: The summary table below is automatically regenerated from the detailed task entries. Use `./scripts/add-task` and `./scripts/update-task`, or edit the detailed task blocks directly and run `./scripts/update-tasks`.
2. **Anchor Tags as SSOT**: Each task in `## Detailed Tasks` is defined by an anchor tag:
   `<a id="fgj-XXX" class="task" data-project="PROJECT" data-status="STATUS" data-task-type="TYPE"></a>`
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

| ID      | Title                                                                                                  | Project | Status  | Type                |
|:------|:-----------------------------------------------------------------------------------------------------|:------|:------|:------------------|
| FGJ-001 | Clean up legacy Bukkit plugin and Maven leftovers from repository root                                 | Shared  | Fixed   | [Chore](#fgj-001)   |
| FGJ-002 | Modernize iOS Xcode project configuration, build pipeline, and dependency management                   | iOS     | Fixed   | [Chore](#fgj-002)   |
| FGJ-003 | Modernize CoreData / CloudKit persistence layer and implement repository architecture                  | iOS     | Fixed   | [Feature](#fgj-003) |
| FGJ-004 | **Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)** | iOS     | Pending | [Feature](#fgj-004) |
| FGJ-005 | **Modernize SwiftUI architecture, NavigationStack, and Observation framework**                         | iOS     | Pending | [Feature](#fgj-005) |
| FGJ-006 | **Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions**          | iOS     | Pending | [Feature](#fgj-006) |
| FGJ-007 | **Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards**        | iOS     | Pending | [Feature](#fgj-007) |
| FGJ-008 | **Implement Collections and Encounters management with CR/XP summary metrics**                         | iOS     | Pending | [Feature](#fgj-008) |
| FGJ-009 | **Implement unified local Full-Text Search and remote Open5e API live search**                         | iOS     | Pending | [Feature](#fgj-009) |
| FGJ-010 | **Build comprehensive multi-section 5e Monster Editor suite with validation and live preview**         | iOS     | Pending | [Feature](#fgj-010) |
| FGJ-011 | **Implement QuickLook Preview Extension, custom document types (.monster), and system sharing**        | iOS     | Pending | [Feature](#fgj-011) |
| FGJ-012 | **Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows**          | iOS     | Pending | [Feature](#fgj-012) |

---

<a id="task-details"></a>

## Detailed Tasks

<a id="fgj-001" class="task" data-project="shared" data-status="done" data-task-type="chore"></a>
### Clean up legacy Bukkit plugin and Maven leftovers from repository root
**ID:** FGJ-001
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

<a id="fgj-002" class="task" data-project="ios" data-status="done" data-task-type="chore"></a>
### Modernize iOS Xcode project configuration, build pipeline, and dependency management
**ID:** FGJ-002
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

<a id="fgj-003" class="task" data-project="ios" data-status="done" data-task-type="feature"></a>
### Modernize CoreData / CloudKit persistence layer and implement repository architecture
**ID:** FGJ-003
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

<a id="fgj-004" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement full Importers & Exporters suite matching Android (Open5e, Tetra-Cube, D&D Beyond, PF2e)
**ID:** FGJ-004
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Bring the iOS import and export engine to full parity with Android. Implement robust parsers and serializers for all supported tabletop formats, external APIs, and native archives.

**Requirements:**

- [ ] `Open5eImporter` & `Open5eApiWrapper`: Fetch and parse monsters from the Open5e REST API (https://api.open5e.com/monsters/)
- [ ] `TetraCubeMonsterImporter`: Complete full JSON parsing from Tetra-Cube 5e statblock generator including special abilities, traits, and layout options
- [ ] `DnDBeyondImporter`: Parse character and monster JSON export payloads from D&D Beyond
- [ ] `Pf2eImporter`: Parse Pathfinder 2e creature statblock JSON formats
- [ ] `BinderImporter` / Native `.monster`: Native MonsterCards multi-monster archive and single-card format
- [ ] `Open5eExporter`: Export local monster stat blocks into Open5e-compatible JSON schemas
- [ ] `BinderExporter`: Export selected collections or entire libraries into shareable `.monster` / zip archives
- [ ] `MonsterCardExporter`: Export formatted monster stat blocks as printable PDF, image, or markdown

<a id="fgj-005" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Modernize SwiftUI architecture, NavigationStack, and Observation framework
**ID:** FGJ-005
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Migrate the iOS UI architecture from legacy SwiftUI patterns (`NavigationView`, `ObservableObject`, `@ObservedObject`, `@EnvironmentObject`) to modern iOS idioms: `NavigationStack`, `NavigationSplitView` (adaptive for iPhone and iPad), and Swift's `@Observable` macro (Observation framework) with structured concurrency (`async/await`, `@MainActor`).

**Requirements:**

- [ ] Replace deprecated `NavigationView` in `ContentView`, `Library`, `Search`, and `Collections` with `NavigationStack` and type-safe navigation destinations
- [ ] Migrate ViewModels (`MonsterViewModel`, `SkillViewModel`, `AbilityViewModel`, etc.) to Swift's `@Observable` macro
- [ ] Support adaptive multi-column `NavigationSplitView` for iPadOS and landscape orientation
- [ ] Standardize design tokens, color palettes, dark mode support, and 5e statblock parchment card styling
- [ ] Implement modern SwiftUI controls: `.searchable`, `ContentUnavailableView`, `.refreshable`, swipe actions, and contextual menus

<a id="fgj-006" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Build feature-complete Monster Library with advanced filtering, sorting, and bulk actions
**ID:** FGJ-006
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Upgrade the Monster Library tab (`Library.swift`) to match Android functionality, providing a rich, high-performance monster management hub with filtering, sorting, multi-selection, and quick actions.

**Requirements:**

- [ ] Implement filter bar and sheet supporting filters by Challenge Rating (range), Monster Type, Size, Alignment, Source, and Custom Tags
- [ ] Implement sorting options: Name (A-Z, Z-A), Challenge Rating (Ascending/Descending), Date Added, Date Modified
- [ ] Implement multi-selection mode for bulk actions: batch delete, add to collection, bulk export
- [ ] Add swipe actions on monster rows: Quick Favorite, Pin to Dashboard, Duplicate, Delete
- [ ] Add pull-to-refresh and empty-state placeholders with one-tap import triggers

<a id="fgj-007" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement interactive Combat Dashboard with HP tracking and quick-reference encounter cards
**ID:** FGJ-007
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Replace the placeholder `Dashboard.swift` with a combat companion dashboard matching Android's encounter running capabilities. Allows Dungeon Masters to pin monster stat blocks, track current/max/temp hit points, manage turn orders, and view compact vital stats during tabletop sessions.

**Requirements:**

- [ ] Create dashboard monster grid/list with vital stats glance (AC, HP bar, Speed, Passive Perception, Key Attacks)
- [ ] Implement interactive Hit Point tracker: current HP, max HP, temporary HP, quick damage/healing steppers with dice roll helpers
- [ ] Implement Pin/Unpin monster actions from Library, Search, and StatBlock views
- [ ] Implement quick expand/collapse sheet to inspect full monster card details without losing dashboard context
- [ ] Persist dashboard combat state across app restarts

<a id="fgj-008" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement Collections and Encounters management with CR/XP summary metrics
**ID:** FGJ-008
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Build out the Collections tab (`Collections.swift`) and detail views to organize monsters into thematic collections, campaigns, locations, or combat encounters with automated challenge rating / encounter XP calculations.

**Requirements:**

- [ ] Create, edit, and delete Collections with custom names, descriptions, and icon/color tags
- [ ] Implement Collection Detail view displaying assigned monsters with sorting and filtering
- [ ] Implement Add/Remove monsters picker with search and multi-select
- [ ] Display encounter metrics: Total Monster Count, Average CR, Total XP, and 5e Encounter Difficulty estimate (Easy, Medium, Hard, Deadly)
- [ ] Export collection as a standalone Binder archive or share sheet payload

<a id="fgj-009" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement unified local Full-Text Search and remote Open5e API live search
**ID:** FGJ-009
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Modernize `Search.swift` to provide a unified search experience that queries both local library monsters and the remote Open5e REST API simultaneously with instant preview and import.

**Requirements:**

- [ ] Local search across multiple fields: name, type, subtype, size, alignment, traits, actions, and tags
- [ ] Remote search querying Open5e API with debounced user typing, loading indicators, and error resilience
- [ ] Segmented results view showing Local Library matches vs. Online Open5e results
- [ ] Tap-to-preview remote stat block with a single-tap "Import to Library" or "Add to Collection" action
- [ ] Filter chips for quick category filtering (e.g. Beasts, Undead, Fiends, Dragons, Humanoids)

<a id="fgj-010" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Build comprehensive multi-section 5e Monster Editor suite with validation and live preview
**ID:** FGJ-010
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Revamp the monster creation and editing suite (`EditMonster.swift` and subviews) to support every 5e statblock attribute with modern form controls, live modifier calculations, Markdown support for traits/actions, and side-by-side / toggleable card preview.

**Requirements:**

- [ ] Basic Info: Name, Size, Type, Subtype, Alignment, Challenge Rating, XP, Source text/URL
- [ ] Defense & Health: Armor Class, Armor Type, Hit Points, Hit Dice count and die size, Hit Dice modifier
- [ ] Movement: Walk, Burrow, Climb, Fly (hover toggle), Swim speeds
- [ ] Ability Scores: STR, DEX, CON, INT, WIS, CHA with automatic modifier calculation and manual override option
- [ ] Proficiencies: Saving Throws and Skills with proficiency/expertise/half-proficiency levels
- [ ] Vulnerabilities, Resistances, Damage Immunities, and Condition Immunities pickers
- [ ] Senses & Languages: Darkvision, Blindsight, Tremorsense, Truesight, Passive Perception, Languages with telepathy
- [ ] Markdown-enabled Action Lists: Special Traits, Actions, Bonus Actions, Reactions, Legendary Actions, Mythic Actions
- [ ] Spellcasting Block: Caster level, spellcasting ability, save DC, spell attack bonus, spell slots by level, cantrips and prepared spells
- [ ] Live preview mode to switch seamlessly between editor forms and rendered statblock card

<a id="fgj-011" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement QuickLook Preview Extension, custom document types (.monster), and system sharing
**ID:** FGJ-011
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Configure iOS document handling for `.monster` and `.json` files, implement AirDrop sharing, system Share Sheet integration, and finish the `MonsterPreview` App Extension for system QuickLook previews in Files.app.

**Requirements:**

- [ ] Register Uniform Type Identifiers (UTIs) for `com.majinnaibu.monstercards.monster` and JSON documents in `Info.plist`
- [ ] Implement custom file import handling via `.onOpenURL` / document opening delegates
- [ ] Implement `MonsterPreview` QuickLook preview extension rendering stat blocks directly in Files.app and AirDrop previews
- [ ] Add Share Sheet integration to export and send monster cards via Messages, Mail, AirDrop, and cloud storage
- [ ] Support Drag and Drop of monster files on iPadOS

<a id="fgj-012" class="task" data-project="ios" data-status="pending" data-task-type="feature"></a>
### Implement comprehensive Unit and UI test suite across Importers, Models, and UI workflows
**ID:** FGJ-012
**Project:** iOS
**Status:** Pending
**Type:** Feature


**Description:**
Establish a complete test suite covering data conversion, all format importers/exporters, CoreData repository operations, and critical SwiftUI user journeys.

**Requirements:**

- [ ] Importer Unit Tests: Test fixtures for Tetra-Cube JSON, D&D Beyond character JSON, Open5e JSON, PF2e JSON, and native Binder formats
- [ ] Exporter Unit Tests: Verify roundtrip fidelity for Open5e export and Binder archive export
- [ ] Repository & Persistence Tests: In-memory CoreData stack testing CRUD, search predicates, and cascade deletion rules
- [ ] UI Tests: Automated user journeys testing Monster creation, Library search & filter, Dashboard HP adjustments, and Collection creation

---

## Notes

- This file uses monotonic 3-digit identifiers (e.g. `FGJ-001`, `FGJ-002`).
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
- **ID Formatting**: All IDs follow the `FGJ-XXX` format where `XXX` is a monotonic 3-digit number.
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
