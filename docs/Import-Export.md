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

### Step 3: Import from D&D Beyond Character URL

Allow users to import monsters or characters directly from a D&D Beyond URL.

- **Target URL Structure**:
  `https://www.dndbeyond.com/characters/49074997` (where `49074997` is the internal character ID).
- **Service Endpoints**:
  D&D Beyond character pages load data from background JSON endpoints such as:
  `https://character-service.dndbeyond.com/character/v2/character/49074997`
- **New Importer Class**: `DnDBeyondImporter implements EntityImporter<Monster>`
- **Pipeline**:
  1. Extract character ID (`49074997`) from user-provided D&D Beyond URL.
  2. Perform an asynchronous HTTP request (via OkHttp/Retrofit) to fetch the character JSON payload.
  3. Map D&D Beyond JSON fields (stats, modifiers, AC, HP, speed, actions, traits, spells) to our internal `Monster` memory model.
  4. Pass the parsed `Monster` to `MonsterImportFragment` for review and persistence.

---

### Step 4: Export to Custom Internal Format (Open5e Specification)

Implement export capability to output monsters and collections using our two-tier Open5e JSON specification (`/Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/import-export.md`).

- **New Class**: `Open5eExporter`
- **Output Architecture**:
  - **Universal Envelope (`entity.json`)**: `uuid`, `ruleset_id: "open5e"`, `entity_type: "character"`, `display_name`, `version`, `tags`, `properties`.
  - **Character Payload (`character.json`)**: Export ability scores, combat vitals (AC, HP formula, speed), proficiency bonuses, actions, reactions, legendary actions, features, and equipped items (`weapon.json`, `armor.json`, `shield.json`, `spell.json`).

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
- [ ] **Step 2**: Update Tetra-cube importer class to support the newest Tetra-cube format (`bonusActions`, `mythics`, `blind`, intro descriptions).
- [ ] **Step 3**: Import from D&D Beyond URL (`https://www.dndbeyond.com/characters/49074997` fetching from character service endpoint `character/v2/character/49074997`).
- [ ] **Step 4**: Export to internal format described by Open5e document (`Open5eExporter`).
- [ ] **Step 5**: Import from internal format described by Open5e document (`Open5eImporter`).
- [ ] **Step 6**: Generic Android Share button feature (`ACTION_SEND`).
- [ ] **Step 7**: Specific share targets (7.1 NFC, 7.2 Bluetooth, 7.3 Embedded Web URL).
