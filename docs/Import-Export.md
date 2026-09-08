# Import & Export Architecture & Plan

This document outlines the modular architecture and step-by-step technical roadmap for handling monster stat block imports, exports, and sharing across multiple formats in the **Monster Cards** application.

---

## 1. Modular Importer Architecture

To keep import/export code clean and maintainable, all format parsers implement a shared interface:

```java
public interface EntityImporter<T> {
    /**
     * Determines whether the given input string or payload can be handled by this importer.
     */
    boolean canImport(@NonNull String input);

    /**
     * Parses the raw input string into internal domain memory objects (e.g., Monster).
     */
    @NonNull
    T parse(@NonNull String input) throws Exception;
}
```

The output of any `EntityImporter` is an in-memory domain model (such as `Monster`), which is then passed to `MonsterImportFragment` for UI preview and Room database persistence.

---

## 2. Step-by-Step Implementation Roadmap

```
  ┌────────────────----------------────────────────────────┐
  │ Step 0: Restrict File Extension Intent Filters         │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 1: Refactor Import Code to Shared Interface       │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 2: Update Tetra-cube Importer to Newest Format    │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 3: Import from D&D Beyond Character URL           │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 4: Export to Custom Internal Format (Open5e)      │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 5: Import from Custom Internal Format (Open5e)      │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 6: Generic Android Share Button Feature           │
  └────────────────────────────────────────────────────────┘
                               │
                               v
  ┌────────────────----------------────────────────────────┐
  │ Step 7: Specific Share Targets (NFC, Bluetooth, URL)   │
  └────────────────────────────────────────────────────────┘
```

---

### Step 0: Restrict File Extension Intent Filters

Restrict the Android app so it reacts **only** to `.monster` and `.monster.txt` files instead of all generic `.txt` files.

1. **`AndroidManifest.xml`**:
   Add `android:pathPattern` and `android:pathSuffix` constraints (`.monster` and `.monster.txt`) to the `<intent-filter>` for `MainActivity`.
2. **`MainActivity.java`**:
   Implement runtime display name validation querying `OpenableColumns.DISPLAY_NAME` via `ContentResolver` to filter out non-monster files passed via `content://` URIs.

---

### Step 1: Refactor Import Code into Importer Architecture

Extract the current parsing logic out of `MonsterImportHelper.java` into a standalone importer class implementing `EntityImporter<Monster>`.

- **New Class**: `TetraCubeMonsterImporter implements EntityImporter<Monster>`
- **Responsibilities**:
  - `canImport(json)`: Validates that the JSON string contains Tetra-cube properties (e.g. `hitDice`, `armorName`, `strPoints`).
  - `parse(json)`: Converts the raw JSON payload into an in-memory `Monster` object.

---

### Step 2: Update Tetra-cube Importer to Newest Format

Update `TetraCubeMonsterImporter` to support newer fields from the Tetra-cube generator source (`js/statblock-script.js`):

- **New Fields to Parse**:
  - `bonusActions`: Array of `{ name, desc }` objects $\rightarrow$ mapped to `monster.actions` or bonus action structure.
  - `mythics` / `isMythic` / `mythicDescription`: Array of mythic action objects and intro text $\rightarrow$ mapped to `monster.legendaryActions`.
  - `blind`: Parse boolean flag to append `"(blind beyond this radius)"` to `blindsight`.
  - Section intro text: Parse `legendariesDescription`, `lairDescription`, `regionalDescription`.

---

### Step 3: Import from D&D Beyond URL & Web Services

Allow users to import characters, monsters, and future entity types directly from D&D Beyond URLs using a multi-channel ingestion architecture.

- **Primary Target Share Links**:
  - Site Share Link: `https://www.dndbeyond.com/characters/49074997/s7sLyX` (primary link copied when tapping "Share" on D&D Beyond; contains numeric character ID `49074997` and share token `s7sLyX`).
  - Standard Web Link: `https://www.dndbeyond.com/characters/49074997` (HTML page that loads background JavaScript to fetch stats).
  - URL Extractor Regex: `https?://(?:www\.)?dndbeyond\.com/characters/(\d+)(?:/([a-zA-Z0-9]+))?`

- **URL Ingestion Channels**:
  1. **Android Share Sheet (`ACTION_SEND`)**:
     - Register `intent-filter` for `ACTION_SEND` with `text/plain` in `AndroidManifest.xml`.
     - Tapping "Share" on D&D Beyond passes `https://www.dndbeyond.com/characters/49074997/s7sLyX` directly into Monster Cards.
  2. **Android Web Link Chooser (`ACTION_VIEW`)**:
     - Register `intent-filter` for `ACTION_VIEW` in `AndroidManifest.xml` with scheme `https`, host `www.dndbeyond.com`, and path prefixes `/characters/`, `/monsters/`, `/spells/`, etc.
     - Allows Monster Cards to appear in the Android "Open with..." link chooser dialog.
  3. **In-App Menu & Clipboard Auto-Detection**:
     - Add an "Import from URL..." menu item in `LibraryFragment` and `MonsterImportFragment`.
     - Displays a URL input dialog with automatic clipboard URL detection (`ClipboardManager`).

- **Known Endpoints & Web Asset Structure**:
  - **Main Character Service Endpoint**:
    - `https://character-service.dndbeyond.com/character/v5/character/49074997?includeCustomItems=true`
    - Returns JSON payload containing full character statistics, attributes, modifiers, classes, race, inventory, and spells.
    - Full reference sample JSON payload extracted to: [`docs/dndbeyond-character-sample.json`](file:///Users/tom/Projects/Apps/MonsterCards/docs/dndbeyond-character-sample.json).
  - **Auxiliary Service Endpoints**:
    - Vehicles: `https://character-service.dndbeyond.com/character/v5/vehicles?characterId=49074997`
    - Vehicle Components: `https://character-service.dndbeyond.com/character/v5/vehicle/components?characterId=49074997`
    - Known Infusions: `https://character-service.dndbeyond.com/character/v5/known-infusions?characterId=49074997`
    - Infusion Items: `https://character-service.dndbeyond.com/character/v5/infusion/items?characterId=49074997`
  - **Image & Avatar Assets**:
    - Default Builder Avatar: `https://www.dndbeyond.com/Content/Skins/Waterdeep/images/characters/default-avatar-builder.png`
    - Race Portrait Avatar: `portraitAvatarUrl` (e.g., `https://www.dndbeyond.com/avatars/2489/881/636680412207671648.jpeg`)
    - Item / Attunement Icons: Returned in `inventory[].definition.avatarUrl` (e.g. `https://www.dndbeyond.com/avatars/19/144/636382339478303209.jpeg`, `https://www.dndbeyond.com/avatars/9249/564/637203446409923453.jpeg`).
  - **Analytics / Telemetry**:
    - `https://global.ketchcdn.com/web/v2/log...` (returns 204 No Content; user consent log endpoint ignored by importer).

- **Architecture (`DnDBeyondImporter implements EntityImporter<Monster>`)**:
  - **URL Extractor**: Extracts `{characterId}` (`49074997`) from shared URLs (`https://www.dndbeyond.com/characters/49074997/s7sLyX`), trimming optional share tokens.
  - **HTTP Service Client**: Performs an asynchronous HTTP GET request (via OkHttp/Retrofit) to `character-service.dndbeyond.com/character/v5/character/{characterId}?includeCustomItems=true`.
  - **Domain Mapper**: Maps D&D Beyond JSON fields (stats, modifiers, AC, HP, speed, proficiencies, actions, bonus actions, reactions, spells, traits, avatar URLs) into the internal `Monster` domain model (`sourceUrl = "https://www.dndbeyond.com/characters/49074997/s7sLyX"`).
  - **UI Preview**: Passes the generated `Monster` object to `MonsterImportFragment` for review and persistence.

---

### Step 4: Export to Custom Internal Format (Open5e Specification)

Implement export capability to output monsters and collections using our two-tier Open5e JSON specification (detailed in [`/Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/import-export.md`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/import-export.md)).

- **New Class**: `Open5eExporter`

#### Open5e Ruleset Architecture & Schema Specification
The Open5e ruleset is part of a modular, versioned ruleset framework located at [`/Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/). In the future, this ruleset will be embedded directly into the application assets and versioned as our canonical model reference.

1. **Ruleset & Entity Identifiers**:
   - Every ruleset defines a short string ID (`ruleset_id: "open5e"`) and a unique UUID (`uuid: "7a35e4d2-f67b-4890-a292-6a7593c72b21"`), declared in the ruleset manifest [`manifest.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/manifest.json).
   - Exported entities contain top-level identifiers (`uuid`, `ruleset_id`, `entity_type`, `display_name`) wrapped in a universal envelope.
   - When importing a JSON payload like [`examples/character/goblin.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/examples/character/goblin.json), importers inspect `ruleset_id == "open5e"` to match the registered ruleset parser.

2. **Base Schemas ([`/Users/tom/Projects/TTRPG/CharacterDataFiles/schema/*.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/schema/))**:
   - **[`schema/entity.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/schema/entity.json)**: The universal envelope schema wrapping all exported entities (`uuid`, `ruleset_id`, `entity_type`, `display_name`, `description`, `version`, `tags`, `template_id`, `properties`).
   - **[`schema/manifest.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/schema/manifest.json)**: Schema defining ruleset manifest metadata, registered entity types, and sheet templates.
   - **[`schema/entity-definition.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/schema/entity-definition.json)**: Meta-schema describing how entity types and property schemas are declared.

3. **Character Entity Schema ([`entities/character.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/entities/character.json))**:
   - Defines the `properties` payload for Player Characters (PCs), NPCs, and Monsters/Creatures.
   - Core fields in `properties`:
     - **Identity & Vitals**: `size`, `type`, `subtype`, `alignment`, `armor_class`, `armor_description`, `hit_points` (`current`, `max`, `formula`, `hit_dice`), `speed`, `speed_desc`, `challenge_rating`, `cr`.
     - **Ability Scores**: `abilities` (`strength`, `dexterity`, `constitution`, `intelligence`, `wisdom`, `charisma`).
     - **Proficiencies & Senses**: `saving_throws`, `skills`, `senses`, `languages`.
     - **Actions & Traits**: `traits`, `actions`, `reactions`, `legendary_actions`, `lair_actions`, `regional_effects`, and `spellcasting`.

4. **Rendering Templates (`template_id`)**:
   - The `template_id` field in the universal envelope specifies how the entity should be rendered in external or host applications.
   - Registered templates in [`manifest.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/manifest.json):
     - `"stat_block"`: Monster Manual style stat block (`templates/stat_block.html`).
     - `"character_card"`: Compact NPC summary card (`templates/character_card.html`).
     - `"character_list_item"`: Fixed-height quick-reference list item (`templates/character_list_item.html`).
     - `"character_sheet"` / `"character_sheet_alt"`: Full 5e interactive character sheets.

#### Exporter Output Requirements (`Open5eExporter`)
- **Envelope Generation**:
  - Emits `$schema: "../../../schema/entity.json"`.
  - Populates `uuid` (random UUID v4), `ruleset_id: "open5e"`, `entity_type: "character"`, `display_name: monster.name`, `template_id: "stat_block"`.
- **Payload Mapping**:
  - Maps `Monster` fields to Open5e character properties in `properties` object conforming to [`entities/character.json`](file:///Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/entities/character.json).

---

### Step 5: Import from Custom Internal Format (Open5e Specification)

Implement import capability for custom Open5e JSON files.

- **New Class**: `Open5eImporter implements EntityImporter<Monster>`
- **Pipeline**:
  - `canImport(json)`: Validates `"ruleset_id": "open5e"` and `"entity_type": "character"`.
  - `parse(json)`: Unpacks `properties` into internal `Monster` domain models.

---

### Step 6: Generic Android Share Button Feature

Implement a generic "Share" feature allowing users to export and share monsters or collections from detail screens.

- **UI Action**: Add a "Share" item to action menus in `MonsterDetailFragment` and `CollectionDetailFragment`.
- **Android Intent**: Uses standard Android `ACTION_SEND` intent with `Intent.EXTRA_STREAM` or `Intent.EXTRA_TEXT` to pass exported files to other apps (e.g. Email, Drive, Messaging, Files).

---

### Step 7: Specific Share Targets & Channels

Extend the generic sharing feature with specialized, direct sharing channels (to be implemented as individual sub-steps):

- **Sub-Step 7.1 (NFC Sharing)**: Share monster data directly between devices via NFC (NDEF records / Android Beam).
- **Sub-Step 7.2 (Bluetooth / Wi-Fi Direct)**: Share monster files directly between nearby Android devices via Bluetooth / Wi-Fi.
- **Sub-Step 7.3 (Web URL with Embedded Payload)**: Generate a shareable Web URL containing compressed/base64-encoded monster JSON data.

---

## 3. Implementation Checklist & Status

- [x] **Step 0**: Restrict app intent filters in `AndroidManifest.xml` (`.monster` & `.monster.txt`) and add runtime filename validation in `MainActivity.java`.
- [x] **Step 1**: Refactor import & conversion code into a shared `EntityImporter<T>` interface and `TetraCubeMonsterImporter` class.
- [x] **Step 2**: Update Tetra-cube importer class to support the newest Tetra-cube format (`bonusActions`, `mythics`, `blind`, intro descriptions).
- [ ] **Step 3**: Import from D&D Beyond URL (`https://www.dndbeyond.com/characters/49074997` fetching from character service endpoint `character/v2/character/49074997`).
- [ ] **Step 4**: Export to internal format described by Open5e document (`Open5eExporter`).
- [ ] **Step 5**: Import from internal format described by Open5e document (`Open5eImporter`).
- [ ] **Step 6**: Generic Android Share button feature (`ACTION_SEND`).
- [ ] **Step 7**: Specific share targets (7.1 NFC, 7.2 Bluetooth, 7.3 Embedded Web URL).

---

## 4. Schema Planning & Text Formatting Notes

### 4.1 Schema Architecture Considerations
- **Unified Action Entity / Type Column**:
  - Instead of maintaining separate `List<Trait>` columns for each category (`actions`, `reactions`, `legendaryActions`, `lairActions`, `regionalActions`, etc.), a unified `monster_actions` table (or model list) with an `action_type` column (`ABILITY`, `ACTION`, `BONUS_ACTION`, `REACTION`, `LEGENDARY_ACTION`, `MYTHIC_ACTION`, `LAIR_ACTION`, `REGIONAL_EFFECT`) simplifies Room DB queries and enables a single reusable editor UI component.
- **Section Intro & End Note Metadata**:
  - Store section-level intro text and end notes (e.g. `legendaryActionsDescription`, `lairActionsDescription`, `lairActionsEndNote`, `regionalActionsDescription`, `regionalActionsEndNote`, `mythicActionsDescription`) as dedicated metadata fields on the `Monster` entity rather than as artificial action traits.

### 4.2 Markdown & Content Formatting Support
- **Supported Markdown Syntax**:
  - **Emphasis**: Italics (`_text_` or `*text*`) and Bold (`__text__` or `**text**`).
  - **Lists**: Bulleted lists (`- `, `* `) and Numbered lists (`1. `, `2. `).
  - **External Links**: Standard markdown links `[Text](https://...)`.
  - **Internal Links (Future Deep Linking)**: Custom URI scheme `[Label](mc://<type>/<id>)` (e.g., `[Fireball](mc://spell/fireball)`) reserved for in-app navigation.
- **Data Model & Import/Export Handling**:
  - All text fields across domain models, Room persistence, and import/export payloads store raw CommonMark markdown strings.
  - Importers preserve raw markdown syntax as exported by source formats (Tetra-cube, Open5e, D&D Beyond).
  - UI rendering layers handle conversion from CommonMark to Android Spanned text at display time.
