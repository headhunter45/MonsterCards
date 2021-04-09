//
//  MonsterViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation
import CoreData

class MonsterViewModel: ObservableObject {
    
    // TODO: Determine whether to prefer Int or Int64 for these fields and switch as many as possible to the winner.
    
    @Published var name: String
    @Published var size: String
    @Published var type: String
    @Published var subType: String
    @Published var alignment: String
    @Published var hitDice: Int64
    @Published var hasCustomHP: Bool
    @Published var customHP: String
    @Published var armorType: ArmorType
    @Published var hasShield: Bool {
        didSet { shieldBonus = hasShield ? 2 : 0 }
    }
    @Published var naturalArmorBonus: Int64
    @Published var customArmor: String
    @Published var walkSpeed: Int64
    @Published var burrowSpeed: Int64
    @Published var climbSpeed: Int64
    @Published var flySpeed: Int64
    @Published var canHover: Bool
    @Published var swimSpeed: Int64
    @Published var hasCustomSpeed: Bool
    @Published var customSpeed: String
    @Published var strengthScore: Int64
    @Published var dexterityScore: Int64
    @Published var constitutionScore: Int64
    @Published var intelligenceScore: Int64
    @Published var wisdomScore: Int64
    @Published var charismaScore: Int64
    @Published var strengthSavingThrowProficiency: ProficiencyType
    @Published var strengthSavingThrowAdvantage: AdvantageType
    @Published var dexteritySavingThrowProficiency: ProficiencyType
    @Published var dexteritySavingThrowAdvantage: AdvantageType
    @Published var constitutionSavingThrowProficiency: ProficiencyType
    @Published var constitutionSavingThrowAdvantage: AdvantageType
    @Published var intelligenceSavingThrowProficiency: ProficiencyType
    @Published var intelligenceSavingThrowAdvantage: AdvantageType
    @Published var wisdomSavingThrowProficiency: ProficiencyType
    @Published var wisdomSavingThrowAdvantage: AdvantageType
    @Published var charismaSavingThrowProficiency: ProficiencyType
    @Published var charismaSavingThrowAdvantage: AdvantageType
    @Published var skills: [SkillViewModel]
    @Published var damageImmunities: [StringViewModel]
    @Published var damageResistances: [StringViewModel]
    @Published var damageVulnerabilities: [StringViewModel]
    @Published var conditionImmunities: [StringViewModel]
    @Published var senses: [StringViewModel]
    @Published var languages: [LanguageViewModel]
    @Published var telepathy: Int64
    @Published var understandsBut: String
    @Published var challengeRating: ChallengeRating
    @Published var customChallengeRating: String
    @Published var customProficiencyBonus: Int64
    @Published var abilities: [AbilityViewModel]
    @Published var actions: [AbilityViewModel]
    @Published var legendaryActions: [AbilityViewModel]
    @Published var lairActions: [AbilityViewModel]
    @Published var regionalActions: [AbilityViewModel]
    @Published var reactions: [AbilityViewModel]
    @Published var isBlind: Bool
    @Published var shieldBonus: Int
    @Published var otherArmorDescription: String
    
    let kBaseArmorClassUnarmored = 10;
    let kBaseArmorClassMageArmor = 13;
    let kBaseArmorClassPadded = 11;
    let kBaseArmorClassLeather = 11;
    let kBaseArmorClassStudded = 12;
    let kBaseArmorClassHide = 12;
    let kBaseArmorClassChainShirt = 13;
    let kBaseArmorClassScaleMail = 14;
    let kBaseArmorClassBreastplate = 14;
    let kBaseArmorClassHalfPlate = 15;
    let kBaseArmorClassRingMail = 14;
    let kBaseArmorClassChainMail = 16;
    let kBaseArmorClassSplintMail = 17;
    let kBaseArmorClassPlate = 18;
    
    init() {
        self.name = ""
        self.size = ""
        self.type = ""
        self.subType = ""
        self.alignment = ""
        self.hitDice = 0
        self.hasCustomHP = false
        self.customHP = ""
        self.armorType = .none
        self.hasShield = false
        self.naturalArmorBonus = 0
        self.customArmor = ""
        self.walkSpeed = 0
        self.burrowSpeed = 0
        self.climbSpeed = 0
        self.flySpeed = 0
        self.canHover = false
        self.swimSpeed = 0
        self.hasCustomSpeed = false
        self.customSpeed = ""
        self.strengthScore = 10
        self.strengthSavingThrowAdvantage = .none
        self.strengthSavingThrowProficiency = .none
        self.dexterityScore = 10
        self.dexteritySavingThrowAdvantage = .none
        self.dexteritySavingThrowProficiency = .none
        self.constitutionScore = 10
        self.constitutionSavingThrowAdvantage = .none
        self.constitutionSavingThrowProficiency = .none
        self.intelligenceScore = 10
        self.intelligenceSavingThrowAdvantage = .none
        self.intelligenceSavingThrowProficiency = .none
        self.wisdomScore = 10
        self.wisdomSavingThrowAdvantage = .none
        self.wisdomSavingThrowProficiency = .none
        self.charismaScore = 10
        self.charismaSavingThrowAdvantage = .none
        self.charismaSavingThrowProficiency = .none
        self.skills = []
        self.damageImmunities = []
        self.damageResistances = []
        self.damageVulnerabilities = []
        self.conditionImmunities = []
        self.senses = []
        self.languages = []
        self.telepathy = 0
        self.understandsBut = ""
        self.challengeRating = .one
        self.customChallengeRating = ""
        self.customProficiencyBonus = 0
        self.abilities = []
        self.actions = []
        self.legendaryActions = []
        self.lairActions = []
        self.regionalActions = []
        self.reactions = []
        self.isBlind = false
        
        // Private properties
        self.shieldBonus = 0
        self.otherArmorDescription = ""
    }
    
    // MARK: Basic Info
    
    var meta: String {
        get {
            // size type (subtype) alignment
            var parts: [String] = []
            
            if (!(self.size.isEmpty)) {
                parts.append(self.size)
            }
            
            if (!(self.type.isEmpty)) {
                parts.append(self.type)
            }
            
            if (!(self.subType.isEmpty)) {
                parts.append(String.init(format: "(%@)", arguments: [self.subType]))
            }
            
            if (!(self.alignment.isEmpty)) {
                parts.append(self.alignment)
            }
            
            return parts.joined(separator: " ")
        }
    }
    
    var sizeEnum: SizeType {
        get {
            return SizeType.init(rawValue: size) ?? .medium
        }
        set {
            size = newValue.rawValue
        }
    }
    
    var hitPoints: String {
        get {
            if (hasCustomHP) {
                return customHP;
            } else {
                let dieSize = Double(MonsterViewModel.hitDieForSize(sizeEnum))
                let conMod = Double(constitutionModifier)
//                let level1HP = Double(dieSize + conMod)
                let level1HP = Double(dieSize/2.0 + conMod)
                let extraLevels = Double(hitDice - 1)
                let levelNHP = (dieSize + 1.0) / 2.0 + conMod
                let extraLevelsHP = extraLevels * levelNHP
                let hpTotal = Int(ceil(level1HP + extraLevelsHP))
                let conBonus = Int(conMod) * Int(hitDice)
                return String(format: "%d (%dd%d%+d)", hpTotal, hitDice, Int(dieSize), conBonus)
            }
        }
    }
    
    var speed: String {
        get {
            if (hasCustomSpeed) {
                return customSpeed
            } else {
                var parts: [String] = []
                
                if (walkSpeed > 0) {
                    parts.append("\(walkSpeed) ft.")
                }
                if (burrowSpeed > 0) {
                    parts.append("burrow \(burrowSpeed) ft.")
                }
                if (climbSpeed > 0) {
                    parts.append("climb \(climbSpeed) ft.")
                }
                if (flySpeed > 0) {
                    parts.append("fly \(flySpeed) ft.\(canHover ? " (hover)": "")")
                }
                if (swimSpeed > 0) {
                    parts.append("swim \(swimSpeed) ft.")
                }
                
                return parts.joined(separator: ", ")
            }
        }
    }

    class func hitDieForSize(_ size: SizeType) -> Int {
        switch size {
            case .tiny: return 4
            case .small: return 6
            case .medium: return 8
            case .large: return 10
            case .huge: return 12
            case .gargantuan: return 20
        }
    }

    // MARK: Ability Scores
    class func abilityModifierForScore(_ score: Int) -> Int {
        return Int(floor(Double((score - 10)) / 2.0))
    }

    func abilityModifierForAbilityScore(_ abilityScore: AbilityScore) -> Int {
        switch abilityScore {
        case .strength:
            return strengthModifier;
        case .dexterity:
            return dexterityModifier
        case .constitution:
            return constitutionModifier
        case .intelligence:
            return intelligenceModifier
        case .wisdom:
            return wisdomModifier
        case .charisma:
            return charismaModifier
        }
    }

    var strengthModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(strengthScore))
        }
    }

    var dexterityModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(dexterityScore))
        }
    }

    var constitutionModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(constitutionScore))
        }
    }

    var intelligenceModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(intelligenceScore))
        }
    }

    var wisdomModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(wisdomScore))
        }
    }

    var charismaModifier: Int {
        get {
            return MonsterViewModel.abilityModifierForScore(Int(charismaScore))
        }
    }

    
    // MARK: Armor
    
    var armorClassDescription: String {
        get {
            var armorClassTotal = 0
            if (armorType == ArmorType.none) {
                // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(shieldBonus)
                return  "\(armorClassTotal)\(hasShield ? " (shield)" : "")"
            } else if (armorType == .naturalArmor) {
                // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(naturalArmorBonus) + Int(shieldBonus)
                return  "\(armorClassTotal) (natural armor\(hasShield ? " (shield)" : ""))"
            } else if (armorType == .mageArmor) {
                // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(shieldBonus)
                let acWithMageArmor = kBaseArmorClassMageArmor + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (%@%d with mage armor)", armorClassTotal, (hasShield ? "shield, " : ""), acWithMageArmor)
            } else if (armorType == .padded) {
                // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
                armorClassTotal = kBaseArmorClassPadded + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (padded%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorType == .leather) {
                // 11 + dexMod + 2 for shield "18 (leather, shield)"
                armorClassTotal = kBaseArmorClassLeather + dexterityModifier + Int(shieldBonus)
                return String(format:"%d (leather%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorType == .studdedLeather) {
                // 12 + dexMod +2 for shield "17 (studded leather)"
                armorClassTotal = kBaseArmorClassStudded + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (studded leather%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorType == .hide) {
                // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
                armorClassTotal = kBaseArmorClassHide + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (hide%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .chainShirt) {
                // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
                armorClassTotal = kBaseArmorClassChainShirt + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (chain shirt%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .scaleMail) {
                // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
                armorClassTotal = kBaseArmorClassScaleMail + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (scale mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .breastplate) {
                // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
                armorClassTotal = kBaseArmorClassBreastplate + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (breastplate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .halfPlate) {
                // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
                armorClassTotal = kBaseArmorClassHalfPlate + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (half plate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .ringMail) {
                // 14 + 2 for shield "14 (ring mail)
                armorClassTotal = kBaseArmorClassRingMail + Int(shieldBonus)
                return String(format: "%d (ring mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .chainMail) {
                // 16 + 2 for shield "16 (chain mail)"
                armorClassTotal = kBaseArmorClassChainMail + Int(shieldBonus)
                return String(format: "%d (chain mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .splintMail) {
                // 17 + 2 for shield "17 (splint)"
                armorClassTotal = kBaseArmorClassSplintMail + Int(shieldBonus)
                return String(format: "%d (splint%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .plateMail) {
                // 18 + 2 for shield "18 (plate)"
                armorClassTotal = kBaseArmorClassPlate + Int(shieldBonus)
                return String(format: "%d (plate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorType == .other) {
                // pure string value shield check does nothing just copies the string from otherArmorDesc
                return otherArmorDescription;
            } else {
                return ""
            }
        }
    }
    
    // MARK: Challenge Rating / Proficiency Bonus
    

    var proficiencyBonus: Int {
        switch challengeRating {
        case .custom:
            return Int(customProficiencyBonus)
        case .zero:
            fallthrough
        case .oneEighth:
            fallthrough
        case .oneQuarter:
            fallthrough
        case .oneHalf:
            fallthrough
        case .one:
            fallthrough
        case .two:
            fallthrough
        case .three:
            fallthrough
        case .four:
            return 2
        case .five:
            fallthrough
        case .six:
            fallthrough
        case .seven:
            fallthrough
        case .eight:
            return 3
        case .nine:
            fallthrough
        case .ten:
            fallthrough
        case .eleven:
            fallthrough
        case .twelve:
            return 4
        case .thirteen:
            fallthrough
        case .fourteen:
            fallthrough
        case .fifteen:
            fallthrough
        case .sixteen:
            return 5
        case .seventeen:
            fallthrough
        case .eighteen:
            fallthrough
        case .nineteen:
            fallthrough
        case .twenty:
            return 6
        case .twentyOne:
            fallthrough
        case .twentyTwo:
            fallthrough
        case .twentyThree:
            fallthrough
        case .twentyFour:
            return 7
        case .twentyFive:
            fallthrough
        case .twentySix:
            fallthrough
        case .twentySeven:
            fallthrough
        case .twentyEight:
            return 8
        case .twentyNine:
            fallthrough
        case .thirty:
            return 9
        }
    }

    func proficiencyBonusForType(_ profType: ProficiencyType) -> Int {
        switch profType {
        case .none:
            return 0
        case .proficient:
            return proficiencyBonus
        case .expertise:
            return proficiencyBonus * 2
        }
    }

    // MARK: Saving Throws
    
    var savingThrowsDescription: String {
        get {
            // TODO: port from objective-c
            var parts: [String] = []
            var name: String
            var advantage: String
            var bonus: Int

            if (strengthSavingThrowAdvantage != .none || strengthSavingThrowProficiency != .none) {
                name = "Strength"
                bonus = strengthModifier + proficiencyBonusForType(strengthSavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(strengthSavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            if (dexteritySavingThrowAdvantage != .none || dexteritySavingThrowProficiency != .none) {
                name = "Dexterity"
                bonus = dexterityModifier + proficiencyBonusForType(dexteritySavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(dexteritySavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            if (constitutionSavingThrowAdvantage != .none || constitutionSavingThrowProficiency != .none) {
                name = "Constitution"
                bonus = constitutionModifier + proficiencyBonusForType(constitutionSavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(constitutionSavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            if (intelligenceSavingThrowAdvantage != .none || intelligenceSavingThrowProficiency != .none) {
                name = "Intelligence"
                bonus = intelligenceModifier + proficiencyBonusForType(intelligenceSavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(intelligenceSavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            if (wisdomSavingThrowAdvantage != .none || wisdomSavingThrowProficiency != .none) {
                name = "Wisdom"
                bonus = wisdomModifier + proficiencyBonusForType(wisdomSavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(wisdomSavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            if (charismaSavingThrowAdvantage != .none || charismaSavingThrowProficiency != .none) {
                name = "Charisma"
                bonus = charismaModifier + proficiencyBonusForType(charismaSavingThrowProficiency)
                advantage = MonsterViewModel.advantageLabelStringForType(charismaSavingThrowAdvantage)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }

            return parts.joined(separator: ", ")
        }
    }


    // MARK: Misc Helpers

    class func advantageLabelStringForType(_ advType: AdvantageType) -> String {
        switch advType {
        case .none:
            return ""
        case .advantage:
            return "(A)"
        case .disadvantage:
            return "(D)"
        }
    }

    // MARK: Skills

    var skillsDescription: String {
        get {
            let sortedSkills = self.skills.sorted {$0.name < $1.name}
            return sortedSkills.reduce("") {
                if $0 == "" {
                    return $1.skillDescription(forMonster: self)
                } else {
                    return $0 + ", " + $1.skillDescription(forMonster: self)
                }
            }
        }
    }


    // MARK: Immunities, Resistances, and Vulnerabilities
    
    var damageVulnerabilitiesDescription: String {
        get {
            // TODO: sort "bludgeoning, piercing, and slashing from nonmagical attacks" to the end and use ; as a separator before it.
            let sortedVulnerabilities = self.damageVulnerabilities.sorted().map({$0.name})
            return StringHelper.oxfordJoin(sortedVulnerabilities)
        }
    }

    var damageResistancesDescription: String {
        get {
            // TODO: sort "bludgeoning, piercing, and slashing from nonmagical attacks" to the end and use ; as a separator before it.
            let sortedResistances = self.damageResistances.sorted().map({$0.name})
            return StringHelper.oxfordJoin(sortedResistances)
        }
    }

    var damageImmunitiesDescription: String {
        get {
            // TODO: sort "bludgeoning, piercing, and slashing from nonmagical attacks" to the end and use ; as a separator before it.
            let sortedImmunities = self.damageImmunities.sorted().map({$0.name})
            return StringHelper.oxfordJoin(sortedImmunities)
        }
    }

    var conditionImmunitiesDescription: String {
        get {
            let sortedImmunities = self.conditionImmunities.sorted().map({$0.name})
            return StringHelper.oxfordJoin(sortedImmunities)
        }
    }
    
    // MARK: OTHER
    
    var passivePerception: Int {
        get {
            let perceptionSkill = skills.first(where: {
                StringHelper.safeEqualsIgnoreCase($0.name, "Perception")
            })
            if (perceptionSkill == nil) {
                return 10 + wisdomModifier
            } else if (perceptionSkill!.proficiency == ProficiencyType.expertise) {
                return 10 + wisdomModifier + proficiencyBonus + proficiencyBonus
            } else if (perceptionSkill!.proficiency == ProficiencyType.proficient) {
                return 10 + wisdomModifier + proficiencyBonus
            } else {
                return 10 + wisdomModifier
            }
        }
    }

    var sensesDescription: String {
        get {
            var modifiedSenses = self.senses.sorted().map({$0.name})
            let hasPassivePerceptionSense = modifiedSenses.contains(where: {
                $0.starts(with: "passive Perception")
            })
            if (!hasPassivePerceptionSense) {
                let calculatedPassivePerception = String(format: "passive Perception %d", passivePerception)
                modifiedSenses.append(calculatedPassivePerception)
            }

            return modifiedSenses.sorted().joined(separator: ", ")
        }
    }

    
    var languagesDescription: String {
        get {
            let spokenLanguages =
                languages
                    .filter({ $0.speaks })
                    .map({$0.name})
                    .sorted()
            let understoodLanguages =
                languages
                    .filter({ !$0.speaks })
                    .map({$0.name})
                    .sorted()

            let understandsButText = understandsBut.isEmpty
                ? ""
                : String(format: " but %@", understandsBut)
            
            let telepathyText = telepathy > 0
                ? String(format: ", telepathy %d ft.", telepathy)
                : ""
            
            if (spokenLanguages.count > 0) {
                if (understoodLanguages.count > 0) {
                    return String(
                        format:"%@ and understands %@%@%@",
                        StringHelper.oxfordJoin(spokenLanguages),
                        StringHelper.oxfordJoin(understoodLanguages),
                        understandsButText,
                        telepathyText)
                } else {
                  return String(
                    format: "%@%@%@",
                    StringHelper.oxfordJoin(spokenLanguages),
                    understandsButText,
                    telepathyText)
                }
            } else {
                if (understoodLanguages.count > 0) {
                    return String(
                      format: "understands %@%@%@",
                      StringHelper.oxfordJoin(understoodLanguages),
                      understandsButText,
                      telepathyText)
                } else if (telepathy > 0){
                    return String(format: "telepathy %d ft.", telepathy)
                } else {
                    return ""
                }
            }
        }
    }
    
    var challengeRatingDescription: String {
        get {
            if (challengeRating != .custom) {
                return challengeRating.displayName
            } else {
                return customChallengeRating
            }
        }
    }
    
    // MARK: End
}
