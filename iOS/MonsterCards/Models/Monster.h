//
//  Monster.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import "Ability.h"
#import "Action.h"
#import "DamageType.h"
#import "Language.h"
#import "SavingThrow.h"
#import "Skill.h"

NS_ASSUME_NONNULL_BEGIN

extern NSString* const kMonsterSizeTiny;
extern NSString* const kMonsterSizeSmall;
extern NSString* const kMonsterSizeMedium;
extern NSString* const kMonsterSizeLarge;
extern NSString* const kMonsterSizeHuge;
extern NSString* const kMonsterSizeGargantuan;

extern const int kArmorClassUnarmored;
extern const int kArmorClassMageArmor;
extern const int kArmorClassPadded;
extern const int kArmorClassLeather;
extern const int kArmorClassStudded;
extern const int kArmorClassHide;
extern const int kArmorClassChainShirt;
extern const int kArmorClassScaleMail;
extern const int kArmorClassBreastplate;
extern const int kArmorClassHalfPlate;
extern const int kArmorClassRingMail;
extern const int kArmorClassChainMail;
extern const int kArmorClassSplintMail;
extern const int kArmorClassPlate;

extern NSString* const kArmorNameNone;
extern NSString* const kArmorNameNaturalArmor;
extern NSString* const kArmorNameMageArmor;
extern NSString* const kArmorNamePadded;
extern NSString* const kArmorNameLeather;
extern NSString* const kArmorNameStuddedLeather;
extern NSString* const kArmorNameHide;
extern NSString* const kArmorNameChainShirt;
extern NSString* const kArmorNameScaleMail;
extern NSString* const kArmorNameBreastplate;
extern NSString* const kArmorNameHalfPlate;
extern NSString* const kArmorNameRingMail;
extern NSString* const kArmorNameChainMail;
extern NSString* const kArmorNameSplintMail;
extern NSString* const kArmorNamePlateMail;
extern NSString* const kArmorNameOther;

@class Skill;

@interface Monster : NSManagedObject

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

@property int naturalArmorBonus;
@property int customProficiencyBonus;
// Shouldn't this be a BOOL?
@property int telepathy;

@property BOOL hover;
@property BOOL customSpeed;
@property BOOL isBlind;

+(int)abilityModifierForScore: (int)score;
+(int)hitDieForSize: (NSString*)size;

-(id)initWithJSONString:(NSString*)jsonString andContext:(NSManagedObjectContext*)context;
-(id)initWithJSONData:(NSData*)jsonData andContext:(NSManagedObjectContext*)context;
-(id)initWithMonster:(Monster*)monster;
-(void)copyFromMonster:(Monster*)monster;

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

#import "Monster+CoreDataProperties.h"
