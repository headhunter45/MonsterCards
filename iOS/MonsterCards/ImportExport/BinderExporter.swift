//
//  BinderExporter.swift
//  MonsterCards
//
//  Port of com.majinnaibu.monstercards.exporters.BinderExporter.java
//  Serialises MonsterViewModel → Binder platform JSON (pretty-printed, Gson-compatible).
//

import Foundation

// MARK: - Binder export DTOs

struct BinderExport {
    var schema = "https://majinnaibu.com/schemas/binder.schema.json"
    var schemaVersion = 1
    var collections: [CollectionExport] = []

    struct CollectionExport {
        let id: String
        let name: String
        let description: String
        let cards: [MonsterExport]

        init(id: String, name: String, description: String, cards: [MonsterExport]) {
            self.id = id
            self.name = name
            self.description = description
            // Preserve empty list explicitly (Android uses new ArrayList<>())
            self.cards = cards.count > 0 ? cards : []
        }

        static func named(_ name: String, cards: [MonsterExport]) -> CollectionExport {
            CollectionExport(id: UUID().uuidString, name: name, description: "", cards: cards)
        }
    }
}

struct MonsterExport {
    /// Build a JSON-serialisable dictionary from a `MonsterViewModel`.
    static func makeDictionary(monster: MonsterViewModel, idOverride: UUID? = nil) -> [String: Any] {
        var d: [String: Any] = [
            "$schema": "https://majinnaibu.com/schemas/monster-card.schema.json",
            "schemaVersion": 1,

            // Core identity
            "id": idOverride?.uuidString ?? UUID().uuidString,
            "name": monster.name ?? "",
            "size": monster.size ?? "",
            "type": monster.type ?? "",
            "subtype": monster.subType ?? "",
            "alignment": monster.alignment ?? "",

            // Ability scores — keys match Android Monster.java Gson output literally.
            "strengthScore": monster.strengthScore,
            "dexterityScore": monster.dexterityScore,
            "constitutionScore": monster.constitutionScore,
            "intelligenceScore": monster.intelligenceScore,
            "wisdomScore": monster.wisdomScore,
            "charismaScore": monster.charismaScore,

            // Saving throw proficiencies & advantages (raw string from Android enum.toString()).
            "strengthSavingThrowProficiency": monster.strengthSavingThrowProficiency.rawValue,
            "strengthSavingThrowAdvantage": monster.strengthSavingThrowAdvantage.rawValue,
            "dexteritySavingThrowProficiency": monster.dexteritySavingThrowProficiency.rawValue,
            "dexteritySavingThrowAdvantage": monster.dexteritySavingThrowAdvantage.rawValue,
            "constitutionSavingThrowProficiency": monster.constitutionSavingThrowProficiency.rawValue,
            "constitutionSavingThrowAdvantage": monster.constitutionSavingThrowAdvantage.rawValue,
            "intelligenceSavingThrowProficiency": monster.intelligenceSavingThrowProficiency.rawValue,
            "intelligenceSavingThrowAdvantage": monster.intelligenceSavingThrowAdvantage.rawValue,
            "wisdomSavingThrowProficiency": monster.wisdomSavingThrowProficiency.rawValue,
            "wisdomSavingThrowAdvantage": monster.wisdomSavingThrowAdvantage.rawValue,
            "charismaSavingThrowProficiency": monster.charismaSavingThrowProficiency.rawValue,
            "charismaSavingThrowAdvantage": monster.charismaSavingThrowAdvantage.rawValue,

            // Armor
            "armorType": monster.armorType.rawValue,
            "shieldBonus": Int32(monster.shieldBonus),
            "naturalArmorBonus": monster.naturalArmorBonus,
            "otherArmorDescription": monster.otherArmorDescription ?? "",

            // Hit points
            "hitDice": monster.hitDice,
            "hasCustomHP": monster.hasCustomHP,
            "customHitPointsDescription": monster.customHP ?? "",

            // Speeds
            "walkSpeed": monster.walkSpeed,
            "burrowSpeed": monster.burrowSpeed,
            "climbSpeed": monster.climbSpeed,
            "flySpeed": monster.flySpeed,
            "canHover": monster.canHover,
            "swimSpeed": monster.swimSpeed,
            "hasCustomSpeed": monster.hasCustomSpeed,
            "customSpeedDescription": monster.customSpeed ?? "",

            // Challenge rating
            "challengeRating": monster.challengeRating.rawValue,
            "customChallengeRatingDescription": monster.customChallengeRating ?? "",
            "customProficiencyBonus": monster.customProficiencyBonus,

            // Telepathy & understanding
            "telepathyRange": monster.telepathy,
            "understandsButDescription": monster.understandsBut ?? "",

            // Lists (strings) — keys match Android Monster.java @ColumnInfo / Gson names.
            "senses": monster.senses.map { ($0.name ?? "") },
            "damageImmunities": monster.damageImmunities.map { ($0.name ?? "") },
            "damageResistances": monster.damageResistances.map { ($0.name ?? "") },
            "damageVulnerabilities": monster.damageVulnerabilities.map { ($0.name ?? "") },
            "conditionImmunities": monster.conditionImmunities.map { ($0.name ?? "") },

            // Skills — each is {"name": ..., "stat": "...", "proficiency": "..."}
            "skills": monster.skills.map { s -> [String: Any] in
                return ["name": s.name ?? "", "stat": s.stat.rawValue, "proficiency": s.proficiency.rawValue]
            },

            // Languages — each is {"name": ..., "speaks": Bool}
            "languages": monster.languages.map { l -> [String: Any] in
                return ["name": l.name ?? "", "speaks": l.speaks]
            },

            // Combat lists — each is {"name": ..., "desc": ...}
            "abilities": monster.abilities.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "actions": monster.actions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "reactions": monster.reactions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "legendaryActions": monster.legendaryActions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "lairActions": monster.lairActions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "regionalActions": monster.regionalActions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },
            "bonusActions": monster.bonusActions.map { ["name": $0.name ?? "", "desc": $0.description ?? ""] },

            // Mythic actions — Android Monster model has this; iOS lacks it.
            "mythicActions": [] as [[String: Any]],

            // Personality / physical description — Android-only fields on Monster.java.
            "playerName": "",
            "background": "",
            "personalityTraits": "",
            "ideals": "",
            "bonds": "",
            "flaws": "",
            "age": "",
            "height": "",
            "weight": "",
            "eyes": "",
            "skin": "",
            "hair": "",
            "appearance": "",
            "backstory": "",
            "alliesAndOrganizations": "",
            "sourceUrl": ""
        ]
        return d
    }
}

// MARK: - BinderExporter (mirrors public API of com.majinnaibu.monstercards.exporters.BinderExporter)

struct BinderExporter {

    // MARK: -- Utility — pretty-print JSON [String:Any] → string (Gson-compatible format)

    private static func toJsonString(_ obj: Any) -> String? {
        guard let data = try? JSONSerialization.data(withJSONObject: obj, options: [.prettyPrinted]) else { return nil }
        return String(data: data, encoding: .utf8)
    }

    // MARK: -- Public API matching Android exactly

    /// `exportBinder(String collectionName, List<Monster> monsters)`
    static func exportBinder(collectionName: String, monsters: [MonsterViewModel], idOverride: UUID? = nil) -> String {
        let col = BinderExport.CollectionExport(
            id: UUID().uuidString,
            name: collectionName.isEmpty ? "" : collectionName,
            description: "",
            cards: monsters.map { MonsterExport.makeDictionary(monster: $0, idOverride: idOverride) }
        )

        let root: [String: Any] = [
            "$schema": "https://majinnaibu.com/schemas/binder.schema.json",
            "schemaVersion": 1,
            "collections": [[
                "id": col.id,
                "name": col.name,
                "description": col.description,
                "cards": col.cards.map { $0 }
            ]]
        ]

        return toJsonString(root) ?? "{}"
    }

    /// `exportBinder(Collection collection, List<Monster> monsters)`
    static func exportBinder(collection: NSManagedObject?, monsters: [MonsterViewModel]) -> String {
        guard let colObj = collection else {
            // Empty-collection fallback (same path as Android with null).
            return exportBinder(collectionName: "", monsters: monsters)
        }

        let id = (colObj.value(forKey: "id") as? UUID)?.uuidString ?? UUID().uuidString
        let name = (colObj.value(forKey: "name") as? String) ?? ""
        let desc = (colObj.value(forKey: "description") as? String) ?? ""

        let col = BinderExport.CollectionExport(
            id: id,
            name: name,
            description: desc,
            cards: monsters.map { MonsterExport.makeDictionary(monster: $0) }
        )

        let root: [String: Any] = [
            "$schema": "https://majinnaibu.com/schemas/binder.schema.json",
            "schemaVersion": 1,
            "collections": [[
                "id": col.id,
                "name": col.name,
                "description": col.description,
                "cards": col.cards.map { $0 }
            ]]
        ]

        return toJsonString(root) ?? "{}"
    }

    /// `exportFullBackup(List<CollectionExport> collections, List<Monster> dashboardMonsters)`
    static func exportFullBackup(collections: [BinderExport.CollectionExport], dashboardMonsters: [MonsterViewModel]?) -> String {
        let colDicts = collections.map { col -> [String: Any] in
            return [
                "id": col.id,
                "name": col.name,
                "description": col.description,
                "cards": col.cards.map { $0 }  // already a [String: Any] from makeDictionary
            ]
        }

        let base: [String: Any] = [
            "$schema": "https://majinnaibu.com/schemas/binder.schema.json",
            "schemaVersion": 1,
            "collections": colDicts
        ]

        guard let dashboard = dashboardMonsters, !dashboard.isEmpty else {
            // Android Gson only includes the field when it's populated.
            return toJsonString(base) ?? "{}"
        }

        var root = base
        root["dashboard"] = dashboard.map { MonsterExport.makeDictionary(monster: $0) }
        return toJsonString(root) ?? "{}"
    }
}
