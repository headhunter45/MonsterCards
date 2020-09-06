//
//  Monster.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Ability.h"
#import "Action.h"
#import "DamageType.h"
#import "Language.h"
#import "SavingThrow.h"
#import "Skill.h"

NS_ASSUME_NONNULL_BEGIN

@class Skill;

@interface Monster : NSObject

@property NSString *name;
@property NSString *size;
@property NSString *type;
@property NSString *tag;
@property NSString *alignment;
@property NSString *armorName;
@property NSString *otherArmorDescription;
@property NSString *hpText;
@property NSString *speed;
@property NSString *burrowSpeed;
@property NSString *climbSpeed;
@property NSString *flySpeed;
@property NSString *swimSpeed;
// speedDescription
@property NSString *customSpeedDescription;
@property NSString *challengeRating;
@property NSString *customChallengeRating;
@property NSString *blindsightDistance;
@property NSString *darkvisionDistance;
@property NSString *tremorsenseDistance;
@property NSString *truesightDistance;
@property NSString *understandsBut;

@property int strengthScore;
@property int dexterityScore;
@property int constitutionScore;
@property int intelligenceScore;
@property int wisdomScore;
@property int charismaScore;
@property int shieldBonus;
@property int naturalArmorBonus;
@property int hitDice;
@property int customProficiencyBonus;
// Shouldn't this be a BOOL?
@property int telepathy;

@property BOOL customHP;
@property BOOL hover;
@property BOOL customSpeed;
@property BOOL isBlind;

+(int)abilityModifierForScore: (int)score;
+(int)hitDieForSize: (NSString*)size;

-(id)initWithJSON:(NSString*)jsonData;
-(NSString*)meta;
-(int)abilityScoreForAbilityScoreName: (NSString*)abilityScoreName;
-(int)abilityModifierForAbilityScoreName: (NSString*)abilityScoreName;
-(int)strengthModifier;
-(int)dexterityModifier;
-(int)constitutionModifier;
-(int)intelligenceModifier;
-(int)wisdomModifier;
-(int)charismaModifier;
//getArmorClass
-(NSString*)armorClassDescription;
//getHitPoints
-(NSString*)hitPointsDescription;
//getSpeedText
-(NSString*)speedDescription;
-(NSString*)strengthDescription;
-(NSString*)dexterityDescription;
-(NSString*)constitutionDescription;
-(NSString*)intelligenceDescription;
-(NSString*)wisdomDescription;
-(NSString*)charismaDescription;
-(NSSet*)savingThrows;
-(void)addSavingThrow: (SavingThrow*)savingThrow;
-(void)remvoeSavingThrow: (SavingThrow*)savingThrow;
-(void)clearSavingThrows;
-(NSString*)savingThrowsDescription;
-(int)proficiencyBonus;
-(void)addSkill: (Skill*)skill;
-(void)removeSkill: (Skill*)skill;
-(void)clearSkills;
-(NSString*)skillsDescription;
-(void)addDamageType: (DamageType*)damageType;
-(void)removeDamageType: (DamageType*)damageType;
-(void)clearDamageTypes;
-(NSString*)damageImmunitiesDescription;
-(NSString*)damageResistancesDescription;
-(NSString*)damageVulnerabilitiesDescription;
-(void)addConditionImmunity: (NSString*)condition;
-(void)removeConditionImmunity: (NSString*)condition;
-(void)clearConditionImmunities;
-(NSString*)conditionImmunitiesDescription;
-(NSString*)sensesDescription;
-(void)addLanguage: (Language*)language;
-(void)removeLanguage: (Language*)language;
-(void)clearLanguages;
-(NSString*)languagesDescription;
-(NSString*)challengeRatingDescription;
-(void)addAbility: (Ability*)ability;
-(void)removeAbility: (Ability*)ability;
-(void)clearAbilities;
-(NSArray*)abilityDescriptions;
-(void)addAction: (Action*)action;
-(void)removeAction: (Action*)action;
-(void)clearActions;
-(NSArray*)actionDescriptions;
-(NSString*)placeholderReplacedText: (NSString*)text;
-(int)savingThrowForAbilityScoreName: (NSString*)abilityScoreName;
-(int)spellSaveDCForAbilityScoreName: (NSString*)abilityScoreName;
-(int)attackBonusForAbilityScoreName: (NSString*)abilityScoreName;

@end

NS_ASSUME_NONNULL_END
