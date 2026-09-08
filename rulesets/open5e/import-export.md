# Open5e Ruleset — Import & Export Specification Guide

This document is the technical integration and schema guide for third-party applications, services, and developers who want to **import** or **export** Open5e (D&D 5th Edition SRD) entities—including **Player Characters / Creatures**, **Weapons**, **Armor**, **Shields**, **Spells**, and **Generic Items**.

---

## Table of Contents
1. [Overview & Architecture](#1-overview--architecture)
2. [What Files to Distribute to External Apps](#2-what-files-to-distribute-to-external-apps)
3. [The Universal Entity Envelope (`entity.json`)](#3-the-universal-entity-envelope-entityjson)
4. [Entity Specifications & Payloads](#4-entity-specifications--payloads)
   - [4.1 Character (`character.json`)](#41-character-entity-characterjson)
   - [4.2 Base Item (`item.json`)](#42-base-item-entity-itemjson)
   - [4.3 Weapon (`weapon.json`)](#43-weapon-entity-weaponjson)
   - [4.4 Armor (`armor.json`)](#44-armor-entity-armorjson)
   - [4.5 Shield (`shield.json`)](#45-shield-entity-shieldjson)
   - [4.6 Spell (`spell.json`)](#46-spell-entity-spelljson)
5. [Validation & Conformance Rules](#5-validation--conformance-rules)
6. [Import & Export Implementation Checklist](#6-import--export-implementation-checklist)

---

## 1. Overview & Architecture

In our system, all data entities (characters, items, spells, etc.) follow a two-tier JSON architecture:

1. **The Universal Envelope (`entity.json`)**: Provides standard metadata required across all rulesets and systems (`uuid`, `ruleset_id`, `entity_type`, `display_name`, `version`, `tags`, etc.).
2. **The Entity Properties Payload (`properties`)**: Houses ruleset-specific and entity-specific fields defined by the entity schema (e.g. ability scores, spell slots, armor classes, damage dice).

```
┌─────────────────────────────────────────────────────────────┐
│ Universal Envelope (schema/entity.json)                     │
│  ├─ uuid: "4c8f3824-3453-48bf-9a0e-a4c336b3bcf2"            │
│  ├─ ruleset_id: "open5e"                                    │
│  ├─ entity_type: "character" | "weapon" | "spell" | ...      │
│  ├─ display_name: "Arannis"                                 │
│  └─ properties: { ... }  <── Validated by Entity Schema     │
└─────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
      ┌──────────────────────────────────────────────────┐
      │ Entity-Specific Schema (entities/{type}.json)     │
      │  ├─ character.json                               │
      │  ├─ weapon.json (inherits from item.json)         │
      │  ├─ armor.json  (inherits from item.json)         │
      │  ├─ shield.json (inherits from item.json)         │
      │  ├─ spell.json                                   │
      │  └─ item.json                                    │
      └──────────────────────────────────────────────────┘
```

---

## 2. What Files to Distribute to External Apps

To give an external application complete import and export capability, package and provide the following files:

### A. Root Schemas (`schema/`)
1. **[`schema/entity.json`](../../schema/entity.json)**: The universal envelope schema. Every exported file must validate against this schema.
2. **[`schema/entity-definition.json`](../../schema/entity-definition.json)**: The meta-schema describing how entity types and schemas are declared.

### B. Ruleset Manifest (`rulesets/open5e/`)
3. **[`rulesets/open5e/manifest.json`](manifest.json)**: Identifies the ruleset (`open5e`), version, author, and all registered entities and templates.

### C. Entity Property Schemas (`rulesets/open5e/entities/`)
4. **[`character.json`](entities/character.json)**: 5e Character and creature schema (attributes, vitals, proficiencies, attacks, spells, inventory).
5. **[`item.json`](entities/item.json)**: Base schema for equipment, gear, and magic items.
6. **[`weapon.json`](entities/weapon.json)**: Weapon schema (damage dice, damage types, ranges, weapon properties; inherits from `item.json`).
7. **[`armor.json`](entities/armor.json)**: Armor schema (AC base, Dex caps, stealth penalty, strength requirement; inherits from `item.json`).
8. **[`shield.json`](entities/shield.json)**: Shield schema (AC bonus, stealth penalty; inherits from `item.json`).
9. **[`spell.json`](entities/spell.json)**: Spell schema (spell level 0–9, school, casting time, range, components, duration).

> **Integration Note on `$ref` Resolution**:
> `weapon.json`, `armor.json`, and `shield.json` use JSON Schema `$ref: "item.json#/schema"`. When integrating with schema validators (such as Ajv, jsonschema, or Newtonsoft.Json), ensure all schema files are preloaded in the schema registry or resolved relative to their folder.

---

## 3. The Universal Entity Envelope (`entity.json`)

Every exported file **must** include these top-level fields:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `$schema` | `string` | Recommended | URI or relative path to schema (`"../../../schema/entity.json"`). |
| `uuid` | `string` | **Yes** | Unique identifier (standard UUID v4 format recommended). |
| `ruleset_id` | `string` | **Yes** | Must be `"open5e"` for this ruleset. |
| `entity_type` | `string` | **Yes** | One of: `"character"`, `"item"`, `"weapon"`, `"armor"`, `"shield"`, `"spell"`. |
| `display_name` | `string` | **Yes** | Human-readable primary label (e.g. `"Arannis"`, `"Longsword +1"`). |
| `description` | `string` | No | Short synopsis or narrative summary. |
| `version` | `string` | No | Data version format (default `"1.0.0"`). |
| `created_at` | `string` | No | ISO 8601 timestamp (e.g. `"2026-09-07T12:00:00Z"`). |
| `updated_at` | `string` | No | ISO 8601 timestamp of last modification. |
| `tags` | `array<string>`| No | Filter and categorization tags (e.g. `["pc", "ranger", "elf"]`). |
| `template_id` | `string` | No | Optional UI template override (e.g. `"character_sheet"`). |
| `properties` | `object` | **Yes** | The entity's payload data (detailed below). |

---

## 4. Entity Specifications & Payloads

### 4.1 Character Entity (`character.json`)

Used for **Player Characters (PCs)**, **Non-Player Characters (NPCs)**, and **Monsters / Creatures**.

#### Required Fields in `properties`:
- `level` (`integer`, 1–20)
- `abilities` (`object` containing `strength`, `dexterity`, `constitution`, `intelligence`, `wisdom`, `charisma`)
- `hit_points` (`object` containing `current` and `max`)

#### Full Property Breakdown:
| Group | Field | Type | Description / Example |
| :--- | :--- | :--- | :--- |
| **Identity** | `player_name` | `string` | Player name (optional for NPCs/monsters). |
| | `class` | `string` | Primary class (e.g. `"Ranger"`). |
| | `subclass` | `string` | Archetype / subclass (e.g. `"Hunter"`). |
| | `race` | `string` | Character race (e.g. `"Elf"`). |
| | `background` | `string` | Background (e.g. `"Outlander"`). |
| | `alignment` | `string` | Moral alignment (e.g. `"Chaotic Good"`). |
| | `experience_points` | `integer` | Current total XP. |
| | `inspiration` | `boolean` | Inspiration point toggle. |
| **Abilities** | `abilities` | `object` | 6 scores. Can be raw integers or score objects: `{"strength": 12, "dexterity": 16, "constitution": 14, "intelligence": 10, "wisdom": 15, "charisma": 8}`. |
| | `proficiency_bonus` | `integer` | Bonus added to proficient rolls (auto-calculated from level or explicit). |
| | `saving_throws` | `array<string>` | Formatted saves (e.g. `["Strength +3", "Dexterity +5"]`). |
| | `skills` | `array<string>` | Formatted skill proficiencies (e.g. `["Athletics +3", "Stealth +5"]`). |
| | `passive_perception` | `integer` | Passive Wisdom (Perception) score (e.g. `15`). |
| **Combat** | `hit_points` | `object` | `{"current": 24, "max": 24, "temp": 0, "formula": "3d10 + 6"}`. |
| | `hit_dice` | `object` | `{"total": 3, "current": 3, "dice": "1d10"}`. |
| | `death_saves` | `object` | `{"successes": 0, "failures": 0}` (0–3 each). |
| | `speed` | `integer` | Walking speed in feet (e.g. `35`). |
| | `speed_desc` | `string` | Display string including flight/swim (e.g. `"30 ft., fly 60 ft."`). |
| | `initiative` | `integer` | Initiative modifier (usually Dex modifier). |
| | `armor_class` | `integer\|object` | Total AC (e.g. `15`). |
| | `equipped_armor` | `object\|null` | Snapshot: `{"name": "Studded Leather", "ac_base": 12, "dex_cap": null}`. |
| | `equipped_shield` | `object\|null` | Snapshot: `{"name": "Shield", "ac_bonus": 2}`. |
| | `attacks` | `array<object>` | Weapon/spell attack rows: `[{"name": "Longbow", "atk_bonus": "+7", "damage": "1d8 + 3", "damage_type": "Piercing", "range": "150/600 ft."}]`. |
| **Inventory & Wealth** | `currency` | `object` | `{"cp": 15, "sp": 40, "ep": 0, "gp": 25, "pp": 0}`. |
| | `inventory` | `array<object>` | Carried items matching `item.json` schema. |
| **Roleplay** | `personality_traits` | `string` | Personality trait text. |
| | `ideals` | `string` | Character ideals. |
| | `bonds` | `string` | Character bonds. |
| | `flaws` | `string` | Character flaws. |
| | `features_and_traits` | `array<object>` | Class/racial features: `[{"name": "Colossus Slayer", "source": "Hunter Archetype", "desc": "..."}]`. |
| **Spellcasting** | `spellcasting` | `object` | `{"class": "Ranger", "ability": "Wisdom", "save_dc": 13, "attack_bonus": 5, "cantrips": [], "slots": {"1": {"total": 3, "expended": 1}}, "spells": [...]}`. |
| **NPC / Monster** | `challenge_rating` | `number\|string` | CR (e.g. `0.25`, `"1/4"`, `14`). |
| | `actions` | `array<object>` | Monster stat block actions: `[{"name": "Scimitar", "desc": "Melee Weapon Attack..."}]`. |
| | `reactions` | `array<object>` | Creature reactions. |
| | `legendary_actions` | `array<object>` | Creature legendary actions. |

#### Complete Export/Import Example (`character`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "4c8f3824-3453-48bf-9a0e-a4c336b3bcf2",
  "ruleset_id": "open5e",
  "entity_type": "character",
  "display_name": "Arannis",
  "description": "Elven Ranger exploring ancient ruins.",
  "version": "1.0.0",
  "tags": ["pc", "ranger", "elf"],
  "properties": {
    "player_name": "Tom",
    "level": 3,
    "class": "Ranger",
    "subclass": "Hunter",
    "race": "Elf",
    "background": "Outlander",
    "alignment": "Chaotic Good",
    "experience_points": 900,
    "inspiration": true,
    "proficiency_bonus": 2,
    "initiative": 3,
    "speed": 35,
    "hit_points": {
      "current": 24,
      "max": 24,
      "temp": 0,
      "formula": "3d10 + 6"
    },
    "hit_dice": {
      "total": 3,
      "current": 3,
      "dice": "1d10"
    },
    "death_saves": {
      "successes": 0,
      "failures": 0
    },
    "armor_class": 15,
    "abilities": {
      "strength": 12,
      "dexterity": 16,
      "constitution": 14,
      "intelligence": 10,
      "wisdom": 15,
      "charisma": 8
    },
    "saving_throws": ["Strength +3", "Dexterity +5"],
    "skills": ["Athletics +3", "Perception +5", "Stealth +5", "Survival +5"],
    "passive_perception": 15,
    "languages": ["Common", "Elvish", "Sylvan"],
    "attacks": [
      {
        "name": "Longbow",
        "atk_bonus": "+7",
        "damage": "1d8 + 3",
        "damage_type": "Piercing",
        "range": "150/600 ft."
      }
    ],
    "currency": {
      "cp": 15,
      "sp": 40,
      "ep": 0,
      "gp": 25,
      "pp": 0
    }
  }
}
```

---

### 4.2 Base Item Entity (`item.json`)

Used for general adventuring gear, tools, containers, consumables, and magic items.

#### Required Fields in `properties`:
- `name` (`string`)

#### Property Fields:
| Field | Type | Default | Description / Enum |
| :--- | :--- | :--- | :--- |
| `name` | `string` | — | Item name (e.g. `"Rope, Hempen (50 feet)"`). |
| `description` | `string` | `""` | Detailed description or rules text. |
| `cost` | `object` | `{"quantity": 1, "unit": "gp"}` | Value (`quantity`: number, `unit`: `"cp"`, `"sp"`, `"ep"`, `"gp"`, `"pp"`). |
| `weight` | `number` | `0` | Weight in pounds (lbs). |
| `rarity` | `string` | `"common"` | `"common"`, `"uncommon"`, `"rare"`, `"very rare"`, `"legendary"`, `"artifact"`. |
| `attunement` | `boolean` | `false` | Whether the item requires attunement. |
| `equipped` | `boolean` | `false` | Equipped/worn status. |
| `quantity` | `integer` | `1` | Count carried. |
| `grants_modifiers`| `array<object>` | `[]` | Stat bonuses granted when equipped (e.g. `[{"target": "armor_class", "value": 1, "type": "bonus", "source": "Cloak of Protection"}]`). |

#### Example Payload (`item`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "8b5123d1-419b-4351-92b0-9b34e56711aa",
  "ruleset_id": "open5e",
  "entity_type": "item",
  "display_name": "Potion of Healing",
  "description": "A magical red fluid that restores hit points when consumed.",
  "properties": {
    "name": "Potion of Healing",
    "description": "You regain 2d4 + 2 hit points when you drink this potion.",
    "cost": { "quantity": 50, "unit": "gp" },
    "weight": 0.5,
    "rarity": "common",
    "attunement": false,
    "equipped": false,
    "quantity": 2
  }
}
```

---

### 4.3 Weapon Entity (`weapon.json`)

Inherits all fields from `item.json` plus combat damage specifications.

#### Required Fields in `properties`:
- `name` (`string`)
- `damage_dice` (`string`, e.g. `"1d8"`, `"2d6"`)
- `damage_type` (`string`: `"bludgeoning"`, `"piercing"`, or `"slashing"`)

#### Additional Property Fields:
| Field | Type | Default | Description / Enum |
| :--- | :--- | :--- | :--- |
| `weapon_category` | `string` | `"simple"` | `"simple"` or `"martial"`. |
| `weapon_range` | `string` | `"melee"` | `"melee"` or `"ranged"`. |
| `damage_dice` | `string` | `"1d6"` | Damage roll formula (e.g. `"1d8"`, `"2d6"`). |
| `damage_type` | `string` | `"slashing"` | `"bludgeoning"`, `"piercing"`, `"slashing"`. |
| `properties` | `array<string>` | `[]` | 5e weapon properties: `"ammunition"`, `"finesse"`, `"heavy"`, `"light"`, `"loading"`, `"range"`, `"reach"`, `"special"`, `"thrown"`, `"two-handed"`, `"versatile"`. |
| `range_normal` | `integer\|null` | `null` | Normal range in feet (e.g. `20` for dagger, `150` for longbow). |
| `range_long` | `integer\|null` | `null` | Disadvantage range in feet (e.g. `60` for dagger, `600` for longbow). |
| `versatile_dice`| `string\|null` | `null` | Alternate two-handed damage die (e.g. `"1d10"` for longsword/battleaxe). |

#### Example Payload (`weapon`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "7f139d42-26cb-4029-a1b7-6ec7e34ef399",
  "ruleset_id": "open5e",
  "entity_type": "weapon",
  "display_name": "Longsword",
  "description": "A versatile martial melee blade.",
  "properties": {
    "name": "Longsword",
    "description": "A classic double-edged straight blade with a cruciform hilt.",
    "cost": { "quantity": 15, "unit": "gp" },
    "weight": 3.0,
    "rarity": "common",
    "weapon_category": "martial",
    "weapon_range": "melee",
    "damage_dice": "1d8",
    "damage_type": "slashing",
    "properties": ["versatile"],
    "versatile_dice": "1d10",
    "equipped": true,
    "quantity": 1
  }
}
```

---

### 4.4 Armor Entity (`armor.json`)

Inherits all fields from `item.json` plus protection and armor class modifiers.

#### Required Fields in `properties`:
- `name` (`string`)
- `armor_category` (`string`: `"light"`, `"medium"`, or `"heavy"`)
- `ac_base` (`integer`, minimum `0`)

#### Additional Property Fields:
| Field | Type | Description |
| :--- | :--- | :--- |
| `armor_category` | `string` | `"light"`, `"medium"`, or `"heavy"`. |
| `ac_base` | `integer` | Base AC provided (e.g. `11` for Leather, `14` for Scale Mail, `18` for Plate). |
| `ac_modifier` | `integer` | Bonus over base 10 (e.g. `+1` for Leather, `+4` for Scale Mail, `+8` for Plate). |
| `dex_cap` | `integer\|null` | Max Dex modifier allowed: `null` (uncapped, light armor), `2` (medium armor cap), `0` (heavy armor, no Dex). |
| `strength_requirement` | `integer\|null` | Minimum Strength score required without -10 speed penalty (e.g. `13` or `15`). `null` if none. |
| `stealth_disadvantage` | `boolean` | `true` if wearing this armor imposes disadvantage on Stealth checks. |

#### Example Payload (`armor`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "21950d81-817b-40fa-9861-bbd51381aa01",
  "ruleset_id": "open5e",
  "entity_type": "armor",
  "display_name": "Breastplate",
  "description": "Fitted metal chest armor with leather backing.",
  "properties": {
    "name": "Breastplate",
    "description": "Protects the wearer's torso while leaving limbs free.",
    "cost": { "quantity": 400, "unit": "gp" },
    "weight": 20.0,
    "rarity": "common",
    "armor_category": "medium",
    "ac_base": 14,
    "dex_cap": 2,
    "strength_requirement": null,
    "stealth_disadvantage": false,
    "equipped": true,
    "quantity": 1
  }
}
```

---

### 4.5 Shield Entity (`shield.json`)

Inherits all fields from `item.json` plus shield-specific AC bonus and penalties.

#### Required Fields in `properties`:
- `name` (`string`)
- `ac_bonus` (`integer`, standard 5e value is `2`)

#### Additional Property Fields:
| Field | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `ac_bonus` | `integer` | `2` | Armor Class bonus added when wielded. |
| `strength_requirement` | `integer\|null` | `null` | Minimum Strength required to wield without penalty. |
| `stealth_disadvantage` | `boolean` | `false` | Imposes disadvantage on Stealth rolls if `true`. |

#### Example Payload (`shield`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "43e2f901-b841-47fa-89fa-948123da6711",
  "ruleset_id": "open5e",
  "entity_type": "shield",
  "display_name": "Shield",
  "description": "A wooden or metal shield carried in one hand.",
  "properties": {
    "name": "Shield",
    "description": "Wielding a shield increases your Armor Class by 2.",
    "cost": { "quantity": 10, "unit": "gp" },
    "weight": 6.0,
    "rarity": "common",
    "ac_bonus": 2,
    "strength_requirement": null,
    "stealth_disadvantage": false,
    "equipped": true,
    "quantity": 1
  }
}
```

---

### 4.6 Spell Entity (`spell.json`)

Defines complete 5e spells, cantrips, and rituals.

#### Required Fields in `properties`:
- `level` (`integer`, 0–9; 0 represents cantrips)
- `school` (`string`: abjuration, conjuration, divination, enchantment, evocation, illusion, necromancy, transmutation)
- `casting_time` (`string`, e.g. `"1 action"`, `"1 bonus action"`, `"1 reaction"`)
- `range` (`string`, e.g. `"Self"`, `"Touch"`, `"60 feet"`, `"120 feet"`)
- `duration` (`string`, e.g. `"Instantaneous"`, `"Concentration, up to 1 minute"`)
- `description` (`string`, the full rules text)

#### Additional Property Fields:
| Field | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `components` | `object` | `{"verbal": true, "somatic": true, "material": false}` | Component breakdown: `verbal` (`bool`), `somatic` (`bool`), `material` (`bool`), `material_specified` (`string`). |
| `concentration` | `boolean` | `false` | Requires spellcaster concentration. |
| `ritual` | `boolean` | `false` | Can be cast as a 10-minute ritual without expending a spell slot. |
| `higher_levels` | `string` | `""` | Additional damage or target effects when upcast with higher level slots. |

#### Example Payload (`spell`):
```json
{
  "$schema": "../../../schema/entity.json",
  "uuid": "e58123f0-49a1-4322-9fa1-01f4218aef82",
  "ruleset_id": "open5e",
  "entity_type": "spell",
  "display_name": "Fireball",
  "description": "A bright streak that blossoms with a low roar into an explosion of flame.",
  "properties": {
    "level": 3,
    "school": "evocation",
    "casting_time": "1 action",
    "range": "150 feet",
    "duration": "Instantaneous",
    "components": {
      "verbal": true,
      "somatic": true,
      "material": true,
      "material_specified": "a tiny ball of bat guano and sulfur"
    },
    "concentration": false,
    "ritual": false,
    "description": "Each creature in a 20-foot-radius sphere centered on that point must make a Dexterity saving throw. A target takes 8d6 fire damage on a failed save, or half as much damage on a successful one.",
    "higher_levels": "When you cast this spell using a spell slot of 4th level or higher, the damage increases by 1d6 for each slot level above 3rd."
  }
}
```

---

## 5. Validation & Conformance Rules

When parsing or validating imported entities, external apps should follow these rules:

1. **Top-Level Envelope Validation**:
   - Verify `ruleset_id === "open5e"`.
   - Ensure `entity_type` matches one of the 6 registered entity types (`character`, `item`, `weapon`, `armor`, `shield`, `spell`).
   - Ensure `uuid` is a valid unique string or UUID v4.
2. **Sub-Schema Dispatch**:
   - Extract `properties` and validate against the corresponding `entities/{entity_type}.json` schema.
3. **Graceful Defaults**:
   - If an optional field is missing (such as `temp` HP or `currency.ep`), assume default 0 or empty structures rather than throwing a validation exception.
4. **Extensibility**:
   - Extra custom fields in `properties` are permitted (`additionalProperties: true` in base schemas) to allow apps to preserve their own proprietary metadata (e.g. inventory IDs, campaign notes, asset URLs) without breaking import/export compatibility.

---

## 6. Import & Export Implementation Checklist

When implementing an integration adapter in another application:

- [ ] **Export Checklist**:
  - [ ] Generate or preserve a stable `uuid` (UUID v4).
  - [ ] Set `"ruleset_id": "open5e"`.
  - [ ] Set `"entity_type"` to the exact entity type slug.
  - [ ] Populate `"display_name"` with the character/item name.
  - [ ] Place all game attributes inside the `"properties"` root object.
  - [ ] Export numbers as JSON numbers (not stringified quotes, except for dice formulas like `"1d8 + 3"`).
  - [ ] Format ISO 8601 dates for `created_at` and `updated_at`.

- [ ] **Import Checklist**:
  - [ ] Check `"ruleset_id" === "open5e"`.
  - [ ] Inspect `"entity_type"` to determine character vs. item vs. spell importer.
  - [ ] Read `properties.abilities` (handle both raw numeric maps and modifier objects).
  - [ ] Safely read `properties.hit_points` (`current`, `max`, `temp`).
  - [ ] Safely import or map `properties.attacks` and `properties.currency`.
  - [ ] If importing items or weapons into a character inventory, unpack nested items into the character's equipment list.
