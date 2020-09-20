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
@synthesize burrowSpeed;
@synthesize challengeRating;
@synthesize climbSpeed;
@synthesize customChallengeRating;
@synthesize customHP;
@synthesize customProficiencyBonus;
@synthesize customSpeed;
@synthesize customSpeedDescription;
@synthesize darkvisionDistance;
@synthesize flySpeed;
@synthesize hitDice;
@synthesize hover;
@synthesize hpText;
@synthesize isBlind;
@synthesize naturalArmorBonus;
@synthesize speed;
@synthesize swimSpeed;
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
+(int)abilityModifierForScore: (int)score {
    return (int)floor((score - 10) / 2.0);
}

+(int)hitDieForSize: (NSString*)size{
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
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
    self.alignment = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"alignment" withDefaultValue:@""];
    self.armorName = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"armorName" withDefaultValue:@""];
    self.otherArmorDescription = [JSONHelper readStringFromDictionary:jsonRoot forKey:@"otherArmorDesc" withDefaultValue:@""];
    self.strengthScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"strPoints" withDefaultValue:0];
    self.dexterityScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"dexPoints" withDefaultValue:0];
    self.constitutionScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"conPoints" withDefaultValue:0];
    self.intelligenceScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"intPoints" withDefaultValue:0];
    self.wisdomScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"wisPoints" withDefaultValue:0];
    self.charismaScore = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"chaPoints" withDefaultValue:0];
    self.shieldBonus = [JSONHelper readIntFromDictionary:jsonRoot forKey:@"shieldBonus" withDefaultValue:0];

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
    NSString *armorName = [self armorName];
    if ([StringHelper isStringNilOrEmpty:armorName] || [kArmorNameNone isEqualToString:armorName]) {
        // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d%@", armorClass, (hasShield ? @" (shield)" : @"")];
    } else if ([kArmorNameNaturalArmor isEqualToString:armorName]) {
        // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.naturalArmorBonus + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (natural armor%@)", armorClass, (hasShield ? @" (shield)" : @"")];
    } else if ([kArmorNameMageArmor isEqualToString:armorName]) {
        // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
        int armorClass = kArmorClassUnarmored + self.dexterityModifier + self.shieldBonus;
        int armorClassWithMageArmor = kArmorClassMageArmor + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (%@%d with mage armor)", armorClass, (hasShield ? @"shield, " : @""), armorClassWithMageArmor];
    } else if ([kArmorNamePadded isEqualToString:armorName]) {
        // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
        int armorClass = kArmorClassPadded + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (padded%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameLeather isEqualToString:armorName]) {
        // 11 + dexMod + 2 for shield "18 (leather, shield)"
        int armorClass = kArmorClassLeather + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (leather%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameStuddedLeather isEqualToString:armorName]) {
        // 12 + dexMod +2 for shield "17 (studded leather)"
        int armorClass = kArmorClassStudded + self.dexterityModifier + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (studded leather%@)", armorClass, (hasShield ? @"shield, " : @"")];
    } else if ([kArmorNameHide isEqualToString:armorName]) {
        // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
        int armorClass = kArmorClassHide + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (hide%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameChainShirt isEqualToString:armorName]) {
        // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
        int armorClass = kArmorClassChainShirt + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (chain shirt%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameScaleMail isEqualToString:armorName]) {
        // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
        int armorClass = kArmorClassScaleMail + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (scale mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameBreastplate isEqualToString:armorName]) {
        // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
        int armorClass = kArmorClassBreastplate + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (breastplate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameHalfPlate isEqualToString:armorName]) {
        // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
        int armorClass = kArmorClassHalfPlate + MIN(2, self.dexterityModifier) + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (half plate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameRingMail isEqualToString:armorName]) {
        // 14 + 2 for shield "14 (ring mail)
        int armorClass = kArmorClassRingMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (ring mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameChainMail isEqualToString:armorName]) {
        // 16 + 2 for shield "16 (chain mail)"
        int armorClass = kArmorClassChainMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (chain mail%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameSplintMail isEqualToString:armorName]) {
        // 17 + 2 for shield "17 (splint)"
        int armorClass = kArmorClassSplintMail + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (splint%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNamePlateMail isEqualToString:armorName]) {
        // 18 + 2 for shield "18 (plate)"
        int armorClass = kArmorClassPlate + self.shieldBonus;
        return [NSString stringWithFormat:@"%d (plate%@)", armorClass, (hasShield ? @", shield" : @"")];
    } else if ([kArmorNameOther isEqualToString:armorName]) {
        // pure string value shield check does nothing just copies the string from otherArmorDesc
        return self.otherArmorDescription;
    } else {
        return @"";
    }
}

//getHitPoints
-(NSString*)hitPointsDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

//getSpeedText
-(NSString*)speedDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)strengthDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)dexterityDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)constitutionDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)intelligenceDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)wisdomDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(NSString*)charismaDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
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
    self.armorName = monster.armorName;
    self.otherArmorDescription = monster.otherArmorDescription;
    self.shieldBonus = monster.shieldBonus;
}

@end
