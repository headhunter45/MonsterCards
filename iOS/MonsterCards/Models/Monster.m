//
//  Monster.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Monster.h"
#import "StringHelper.h"

@implementation Monster

@synthesize armorName;
@synthesize blindsightDistance;
@synthesize burrowSpeed;
@synthesize challengeRating;
@synthesize charismaScore;
@synthesize climbSpeed;
@synthesize constitutionScore;
@synthesize customChallengeRating;
@synthesize customHP;
@synthesize customProficiencyBonus;
@synthesize customSpeed;
@synthesize customSpeedDescription;
@synthesize darkvisionDistance;
@synthesize dexterityScore;
@synthesize flySpeed;
@synthesize hitDice;
@synthesize hover;
@synthesize hpText;
@synthesize intelligenceScore;
@synthesize isBlind;
@synthesize naturalArmorBonus;
@synthesize otherArmorDescription;
@synthesize shieldBonus;
@synthesize speed;
@synthesize strengthScore;
@synthesize swimSpeed;
@synthesize telepathy;
@synthesize tremorsenseDistance;
@synthesize truesightDistance;
@synthesize understandsBut;
@synthesize wisdomScore;

+(int)abilityModifierForScore: (int)score {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
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
    
    NSDictionary *jsonRoot = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    self.name = [jsonRoot objectForKey:@"name"] ?: @"";
    self.size = [jsonRoot objectForKey:@"size"] ?: @"";
    self.type = [jsonRoot objectForKey:@"type"] ?: @"";
    
    return self;
}

-(id)initWithMonster:(Monster* _Nonnull)monster {
    self = [self initWithContext:monster.managedObjectContext];
    
    [self copyFromMonster:monster];
    
    return self;
}

-(NSString*)meta {
    //"${size} ${type} (${subtype}) ${alignment}"

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
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)dexterityModifier {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)constitutionModifier {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)intelligenceModifier {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)wisdomModifier {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

-(int)charismaModifier {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
}

//getArmorClass
-(NSString*)armorClassDescription {
    @throw [[NSException alloc] initWithName:@"unimplemented" reason:@"Method not implemented." userInfo:nil];
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
}

@end
