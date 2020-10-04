//
//  Monster.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Monster.h"
#import "StringHelper.h"
#import "JSONHelper.h"

@implementation Monster

@synthesize blindsightDistance;
@synthesize challengeRating;
@synthesize customChallengeRating;
@synthesize customProficiencyBonus;
@synthesize darkvisionDistance;
@synthesize isBlind;
@synthesize naturalArmorBonus;
@synthesize telepathy;
@synthesize tremorsenseDistance;
@synthesize truesightDistance;
@synthesize understandsBut;

const int kArmorClassUnarmored = 10;
const int kArmorClassMageArmor = kArmorClassUnarmored + 3;
const int kArmorClassPadded = kArmorClassUnarmored + 1;
const int kArmorClassLeather = kArmorClassUnarmored + 1;
const int kArmorClassStudded = kArmorClassUnarmored + 2;
const int kArmorClassHide = kArmorClassUnarmored + 2;
const int kArmorClassChainShirt = kArmorClassUnarmored + 3;
const int kArmorClassScaleMail = kArmorClassUnarmored + 4;
const int kArmorClassBreastplate = kArmorClassUnarmored + 4;
const int kArmorClassHalfPlate = kArmorClassUnarmored + 5;
const int kArmorClassRingMail = kArmorClassUnarmored + 4;
const int kArmorClassChainMail = kArmorClassUnarmored + 6;
const int kArmorClassSplintMail = kArmorClassUnarmored + 7;
const int kArmorClassPlate = kArmorClassUnarmored + 8;

NSString* const kArmorNameNone = @"none";
NSString* const kArmorNameNaturalArmor = @"natural armor";
NSString* const kArmorNameMageArmor = @"mage armor";
NSString* const kArmorNamePadded = @"padded";
NSString* const kArmorNameLeather = @"leather";
NSString* const kArmorNameStuddedLeather = @"studded";
NSString* const kArmorNameHide = @"hide";
NSString* const kArmorNameChainShirt = @"chain shirt";
NSString* const kArmorNameScaleMail = @"scale mail";
NSString* const kArmorNameBreastplate = @"breastplate";
NSString* const kArmorNameHalfPlate = @"half plate";
NSString* const kArmorNameRingMail = @"ring mail";
NSString* const kArmorNameChainMail = @"chain mail";
NSString* const kArmorNameSplintMail = @"splint";
NSString* const kArmorNamePlateMail = @"plate";
NSString* const kArmorNameOther = @"other";

NSString* const kMonsterSizeTiny = @"tiny";
NSString* const kMonsterSizeSmall = @"small";
NSString* const kMonsterSizeMedium = @"medium";
NSString* const kMonsterSizeLarge = @"large";
NSString* const kMonsterSizeHuge = @"huge";
NSString* const kMonsterSizeGargantuan = @"gargantuan";

NSString *const kProficiencyTypeNone = @"none";
NSString *const kProficiencyTypeProficient = @"proficient";
NSString *const kProficiencyTypeExpertise = @"expertise";

NSString *const kAdvantageTypeNone = @"none";
NSString *const kAdvantageTypeAdvantage = @"advantage";
NSString *const kAdvantageTypeDisadvantage = @"disadvantage";

+(int)abilityModifierForScore: (int)score {
    return (int)floor((score - 10) / 2.0);
}

+(int)hitDieForSize: (NSString*)size{
    if ([kMonsterSizeTiny isEqualToString:size]) {
        return 4;
    } else if ([kMonsterSizeSmall isEqualToString:size]) {
        return 6;
    } else if ([kMonsterSizeMedium isEqualToString:size]) {
        return 8;
    } else if ([kMonsterSizeLarge isEqualToString:size]) {
        return 10;
    } else if ([kMonsterSizeHuge isEqualToString:size]) {
        return 12;
    } else if ([kMonsterSizeGargantuan isEqualToString:size]) {
        return 20;
    } else {
        return 8;
    }
}

-(id)init {
    self = [super init];
        
    self.name = @"";
    self.size = @"";

    return self;
}

-(id)initWithJSONString: (NSString*)jsonString andContext:(NSManagedObjectContext*)context {
    NSData* jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    return [self initWithJSONData:jsonData andContext:context];
}

-(id)initWithJSONData: (NSData*)jsonData andContext:(NSManagedObjectContext*)context {
    self = [self initWithContext:context];
    
    NSDictionary *jsonRoot = [JSONHelper parseJSONDataAsDictionary:jsonData];
    
    self.name = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"name" withDefaultValue:@""];
    self.size = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"size" withDefaultValue:@""];
    self.type = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"type" withDefaultValue:@""];
    self.subtype = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"tag" withDefaultValue:@""];
    self.hpText = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"hpText" withDefaultValue:@""];

    self.alignment = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"alignment" withDefaultValue:@""];
    self.armorType = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"armorName" withDefaultValue:@""];
    self.otherArmorDescription = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"otherArmorDesc" withDefaultValue:@""];
    self.strengthScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"strPoints" withDefaultValue:10];
    self.dexterityScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"dexPoints" withDefaultValue:10];
    self.constitutionScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"conPoints" withDefaultValue:10];
    self.intelligenceScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"intPoints" withDefaultValue:10];
    self.wisdomScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"wisPoints" withDefaultValue:10];
    self.charismaScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"chaPoints" withDefaultValue:10];
    self.shieldBonus = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"shieldBonus" withDefaultValue:0];
    self.hitDice = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"hitDice" withDefaultValue:0];
    
    self.customHP = [JSONHelper readBoolFromDictionary:jsonRoot forKey:@"customHP" withDefaultValue:NO];

    return self;
}

-(id)initWithMonster:(Monster* _Nonnull)monster {
    self = [self initWithContext:monster.managedObjectContext];
    
    [self copyFromMonster:monster];
    
    return self;
}

-(NSString*)meta {
    // "${size} ${type} (${subtype}) ${alignment}"

    NSMutableArray *parts = [NSMutableArray arrayWithCapacity:4];
    if (![StringHelper isStringNilOrEmpty:self.size]) {
        [parts addObject:self.size];
    }
    
    if (![StringHelper isStringNilOrEmpty:self.type]) {
        [parts addObject:self.type];
    }
    
    if (![StringHelper isStringNilOrEmpty:self.subtype]) {
        [parts addObject:[NSString stringWithFormat:@"(%@)", self.subtype]];
    }
    
    if (![StringHelper isStringNilOrEmpty:self.alignment]) {
        [parts addObject:self.alignment];
    }
    
    return [parts componentsJoinedByString:@" "];
}

-(int)abilityScoreForAbilityScoreName: (NSString*)abilityScoreName {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)abilityModifierForAbilityScoreName: (NSString*)abilityScoreName {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)strengthModifier {
    return [Monster abilityModifierForScore:self.strengthScore];
}

-(int)dexterityModifier {
    return [Monster abilityModifierForScore:self.dexterityScore];
}

-(int)constitutionModifier {
    return [Monster abilityModifierForScore:self.constitutionScore];
}

-(int)intelligenceModifier {
    return [Monster abilityModifierForScore:self.intelligenceScore];
}

-(int)wisdomModifier {
    return [Monster abilityModifierForScore:self.wisdomScore];
}

-(int)charismaModifier {
    return [Monster abilityModifierForScore:self.charismaScore];
}


//getArmorClass
-(NSString*)armorClassDescription {
    BOOL hasShield = [self shieldBonus] != 0;
    NSString *armorType = [self armorType];
    if ([StringHelper isStringNilOrEmpty:armorType] || [kArmorNameNone isEqualToString:armorType]) {
        // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d%@", armorClass, (hasShield ? @" (shield)" : @"")];
    } else if ([kArmorNameNaturalArmor isEqualToString:armorType]) {
        // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.naturalArmorBonus + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (natural armor%@)", armorClass, (hasShield ? @" (shield)" : @"")];
    } else if ([kArmorNameMageArmor isEqualToString:armorType]) {
        // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.shieldBonus;
        int armorClassWithMageArmor = kArmorClassMageArmor + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (%@%d with mage armor)", armorClass, (hasShield ? @"shield, " : @""), armorClassWithMageArmor];
    } else if ([kArmorNamePadded isEqualToString:armorType]) {
        // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
        int armorClass = kArmorClassPadded + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (padded%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameLeather isEqualToString:armorType]) {
        // 11 + dexMod + 2 for shield "18 (leather, shield)"
        int armorClass = kArmorClassLeather + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (leather%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameStuddedLeather isEqualToString:armorType]) {
        // 12 + dexMod +2 for shield "17 (studded leather)"
        int armorClass = kArmorClassStudded + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (studded leather%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameHide isEqualToString:armorType]) {
        // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
        int armorClass = kArmorClassHide + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (hide%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameChainShirt isEqualToString:armorType]) {
        // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
        int armorClass = kArmorClassChainShirt + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (chain shirt%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameScaleMail isEqualToString:armorType]) {
        // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
        int armorClass = kArmorClassScaleMail + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (scale mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameBreastplate isEqualToString:armorType]) {
        // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
        int armorClass = kArmorClassBreastplate + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (breastplate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameHalfPlate isEqualToString:armorType]) {
        // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
        int armorClass = kArmorClassHalfPlate + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (half plate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameRingMail isEqualToString:armorType]) {
        // 14 + 2 for shield "14 (ring mail)
        int armorClass = kArmorClassRingMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (ring mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameChainMail isEqualToString:armorType]) {
        // 16 + 2 for shield "16 (chain mail)"
        int armorClass = kArmorClassChainMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (chain mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameSplintMail isEqualToString:armorType]) {
        // 17 + 2 for shield "17 (splint)"
        int armorClass = kArmorClassSplintMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (splint%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNamePlateMail isEqualToString:armorType]) {
        // 18 + 2 for shield "18 (plate)"
        int armorClass = kArmorClassPlate + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (plate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameOther isEqualToString:armorType]) {
        // pure string value shield check does nothing just copies the string from otherArmorDesc
        return self.otherArmorDescription;
    } else {
        return @"";
    }
}

//getHitPoints
-(NSString*)hitPointsDescription {
    if (self.customHP) {
        return self.hpText;
    } else {
        int dieSize = [Monster hitDieForSize:self.size];
        int conMod = self.constitutionModifier;
        int hpTotal = (int)MAX(1, ceil(dieSize + conMod + (self.hitDice - 1) * ((dieSize + 1) / 2.0 + conMod)));
        int conBonus = conMod * self.hitDice;
        return [NSString stringWithFormat:@"%d (%dd%d%+d)", hpTotal, self.hitDice, dieSize, conBonus];
    }
}

-(NSString*)speedDescription {
    if (self.hasCustomSpeed) {
        return self.customSpeed;
    } else {
        NSMutableArray* parts = [[NSMutableArray alloc] init];
        if (self.baseSpeed > 0) {
            [parts addObject:[NSString stringWithFormat:@"%d ft.", self.baseSpeed]];
        }
        if (self.burrowSpeed > 0) {
            [parts addObject:[NSString stringWithFormat:@"burrow %d ft.", self.burrowSpeed]];
        }
        if (self.climbSpeed > 0) {
            [parts addObject:[NSString stringWithFormat:@"climb %d ft.", self.climbSpeed]];
        }
        if (self.flySpeed > 0) {
            [parts addObject:[NSString stringWithFormat:@"fly %d ft.%@", self.flySpeed, self.canHover ? @" (hover)" : @""]];
        }
        if (self.swimSpeed > 0) {
            [parts addObject:[NSString stringWithFormat:@"swim %d ft.", self.swimSpeed]];
        }
        return [parts componentsJoinedByString:@" "];
    }
}

-(NSString*)strengthDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.strengthScore, self.strengthModifier];
}

-(NSString*)dexterityDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.dexterityScore, self.dexterityModifier];
}

-(NSString*)constitutionDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.constitutionScore, self.constitutionModifier];
}

-(NSString*)intelligenceDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.intelligenceScore, self.intelligenceModifier];

}

-(NSString*)wisdomDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.wisdomScore, self.wisdomModifier];
}

-(NSString*)charismaDescription {
    return [NSString stringWithFormat:@"%d (%+d)", self.charismaScore, self.charismaModifier];
}

-(NSSet*)savingThrows {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addSavingThrow: (SavingThrow*)savingThrow {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)remvoeSavingThrow: (SavingThrow*)savingThrow {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearSavingThrows {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)savingThrowsDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)proficiencyBonus {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addSkill: (Skill*)skill {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeSkill: (Skill*)skill {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearSkills {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)skillsDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addDamageType: (DamageType*)damageType {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeDamageType: (DamageType*)damageType {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearDamageTypes {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)damageImmunitiesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)damageResistancesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)damageVulnerabilitiesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addConditionImmunity: (NSString*)condition {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeConditionImmunity: (NSString*)condition {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearConditionImmunities {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)conditionImmunitiesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)sensesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addLanguage: (Language*)language {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeLanguage: (Language*)language {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearLanguages {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)languagesDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)challengeRatingDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addAbility: (Ability*)ability {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeAbility: (Ability*)ability {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearAbilities {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSArray*)abilityDescriptions {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)addAction: (Action*)action {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)removeAction: (Action*)action {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)clearActions {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSArray*)actionDescriptions {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)placeholderReplacedText: (NSString*)text {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)savingThrowForAbilityScoreName: (NSString*)abilityScoreName {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)spellSaveDCForAbilityScoreName: (NSString*)abilityScoreName {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)attackBonusForAbilityScoreName: (NSString*)abilityScoreName {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(void)copyFromMonster:(Monster*)monster {
    self.name = monster.name;
    self.size = monster.size;
    self.type = monster.type;
    self.subtype = monster.subtype;
    self.alignment = monster.alignment;
    self.strengthScore = monster.strengthScore;
    self.dexterityScore = monster.dexterityScore;
    self.constitutionScore = monster.constitutionScore;
    self.intelligenceScore = monster.intelligenceScore;
    self.wisdomScore = monster.wisdomScore;
    self.charismaScore = monster.charismaScore;
    self.armorType = monster.armorType;
    self.otherArmorDescription = monster.otherArmorDescription;
    self.shieldBonus = monster.shieldBonus;
    self.customHP = monster.customHP;
    self.hitDice = monster.hitDice;
    self.hpText = monster.hpText;
    self.baseSpeed = monster.baseSpeed;
    self.burrowSpeed = monster.burrowSpeed;
    self.climbSpeed = monster.climbSpeed;
    self.flySpeed = monster.flySpeed;
    self.canHover = monster.canHover;
    self.swimSpeed = monster.swimSpeed;
    self.hasCustomSpeed = monster.hasCustomSpeed;
    self.customSpeed = monster.customSpeed;
    self.armorType = monster.armorType;
    self.naturalArmorBonus = monster.naturalArmorBonus;
    self.hasShield = monster.hasShield;
    self.customArmor = monster.customArmor;
    self.strengthSavingThrowAdvantage = monster.strengthSavingThrowAdvantage;
    self.strengthSavingThrowProficiency = monster.strengthSavingThrowProficiency;
    self.dexteritySavingThrowAdvantage = monster.dexteritySavingThrowAdvantage;
    self.dexteritySavingThrowProficiency = monster.dexteritySavingThrowProficiency;
    self.constitutionSavingThrowAdvantage = monster.constitutionSavingThrowAdvantage;
    self.constitutionSavingThrowProficiency = monster.constitutionSavingThrowProficiency;
    self.intelligenceSavingThrowAdvantage = monster.intelligenceSavingThrowAdvantage;
    self.intelligenceSavingThrowProficiency = monster.intelligenceSavingThrowProficiency;
    self.wisdomSavingThrowAdvantage = monster.wisdomSavingThrowAdvantage;
    self.wisdomSavingThrowProficiency = monster.wisdomSavingThrowProficiency;
    self.charismaSavingThrowAdvantage = monster.charismaSavingThrowAdvantage;
    self.charismaSavingThrowProficiency = monster.charismaSavingThrowProficiency;
}

@end
