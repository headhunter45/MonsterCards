//
//  EditMonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/8/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "EditMonsterViewController.h"
#import "MCBooleanFieldTableViewCell.h"
#import "MCIntegerFieldTableViewCell.h"
#import "MCRadioFieldTableViewCell.h"
#import "MCSelectFieldTableViewCell.h"
#import "MCShortStringFieldTableViewCell.h"
#import "AppDelegate.h"

@interface EditMonsterViewController ()

@property Monster* editingMonster;

@end

const int kSectionIndexBasicInfo = 0;
const int kSectionIndexArmor = 1;
const int kSectionIndexSpeed = 2;
const int kSectionIndexAbilityScores = 3;
const int kSectionIndexSavingThrows = 4;

const int kBasicInfoSectionRowIndexName = 0;
const int kBasicInfoSectionRowIndexSize = 1;
const int kBasicInfoSectionRowIndexType = 2;
const int kBasicInfoSectionRowIndexSubtype = 3;
const int kBasicInfoSectionRowIndexAlignment = 4;
const int kBasicInfoSectionRowIndexHitDice = 5;
const int kBasicInfoSectionRowIndexCustomHP = 6;
const int kBasicInfoSectionRowIndexCustomHPText = 7;

const int kArmorSectionRowIndexArmorType = 0;
const int kArmorSectionRowIndexHasShield = 1;
const int kArmorSectionRowIndexNaturalArmorBonus = 2;
const int kArmorSectionRowIndexCustomArmor = 3;

const int kSpeedSectionRowIndexBaseSpeed = 0;
const int kSpeedSectionRowIndexBurrowSpeed = 1;
const int kSpeedSectionRowIndexClimbSpeed = 2;
const int kSpeedSectionRowIndexFlySpeed = 3;
const int kSpeedSectionRowIndexCanHover = 4;
const int kSpeedSectionRowIndexSwimSpeed = 5;
const int kSpeedSectionRowIndexHasCustomSpeed = 6;
const int kSpeedSectionRowIndexCustomSpeed = 7;

const int kAbilityScoreSectionRowIndexStrength = 0;
const int kAbilityScoreSectionRowIndexDexterity = 1;
const int kAbilityScoreSectionRowIndexConstitution = 2;
const int kAbilityScoreSectionRowIndexIntelligence = 3;
const int kAbilityScoreSectionRowIndexWisdom = 4;
const int kAbilityScoreSectionRowIndexCharisma = 5;

const int kSavingThrowsSectionRowIndexStrengthProficiency = 0;
const int kSavingThrowsSectionRowIndexStrengthAdvantage = 1;
const int kSavingThrowsSectionRowIndexDexterityProficiency = 2;
const int kSavingThrowsSectionRowIndexDexterityAdvantage = 3;
const int kSavingThrowsSectionRowIndexConstitutionProficiency = 4;
const int kSavingThrowsSectionRowIndexConstitutionAdvantage = 5;
const int kSavingThrowsSectionRowIndexIntelligenceProficiency = 6;
const int kSavingThrowsSectionRowIndexIntelligenceAdvantage = 7;
const int kSavingThrowsSectionRowIndexWisdomProficiency = 8;
const int kSavingThrowsSectionRowIndexWisdomAdvantage = 9;
const int kSavingThrowsSectionRowIndexCharismaProficiency = 10;
const int kSavingThrowsSectionRowIndexCharismaAdvantage = 11;

NSString *const kIdentifierName = @"monster.name";
NSString *const kIdentifierSize = @"monster.size";
NSString *const kIdentifierType = @"monster.type";
NSString *const kIdentifierSubtype = @"monster.subtype";
NSString *const kIdentifierAlignment = @"monster.alignment";
NSString *const kIdentifierCustomHP = @"monster.customHPText";
NSString *const kIdentifierCustomSpeed = @"monster.customSpeed";
NSString *const kIdentifierCustomArmor = @"monster.customArmor";
NSString *const kIdentifierStrengthScore = @"monster.strengthScore";
NSString *const kIdentiferDexterityScore = @"monster.dexterityScore";
NSString *const kIdentifierConstitutionScore = @"monster.constitutionScore";
NSString *const kIdentifierIntelligenceScore = @"monster.intelligenceScore";
NSString *const kIdentifierWisdomScore = @"monster.wisdomScore";
NSString *const kIdentifierCharismaScore = @"monster.charismaScore";
NSString *const kIdentifierHitDice = @"monster.hitDice";
NSString *const kIdentifierBaseSpeed = @"monster.baseSpeed";
NSString *const kIdentifierBurrowSpeed = @"monster.burrowSpeed";
NSString *const kIdentifierClimbSpeed = @"monster.climbSpeed";
NSString *const kIdentifierFlySpeed = @"monster.flySpeed";
NSString *const kIdentifierSwimSpeed = @"monster.swimSpeed";
NSString *const kIdentifierNaturalArmorBonus = @"monster.naturalArmorBonus";
NSString *const kIdentifierHasCustomHP = @"monster.customHP";
NSString *const kIdentifierCanHover = @"monster.canHover";
NSString *const kIdentifierHasCustomSpeed = @"monster.hasCustomSpeed";
NSString *const kIdentifierHasShield = @"monster.hasShield";
NSString *const kIdentifierArmorType = @"monster.armorType";
NSString *const kIdentifierStrengthSavingThrowAdvantage = @"monster.savingThrows.strength.advantage";
NSString *const kIdentifierStrengthSavingThrowProficiency = @"monster.savingThrows.strength.proficiency";
NSString *const kIdentifierDexteritySavingThrowAdvantage = @"monster.savingThrows.dexterity.advantage";
NSString *const kIdentifierDexteritySavingThrowProficiency = @"monster.savingThrows.dexterity.proficiency";
NSString *const kIdentifierConstitutionSavingThrowAdvantage = @"monster.savingThrows.constitution.advantage";
NSString *const kIdentifierConstitutionSavingThrowProficiency = @"monster.savingThrows.constitution.proficiency";
NSString *const kIdentifierIntelligenceSavingThrowAdvantage = @"monster.savingThrows.intelligence.advantage";
NSString *const kIdentifierIntelligenceSavingThrowProficiency = @"monster.savingThrows.intelligence.proficiency";
NSString *const kIdentifierWisdomSavingThrowAdvantage = @"monster.savingThrows.wisdom.advantage";
NSString *const kIdentifierWisdomSavingThrowProficiency = @"monster.savingThrows.wisdom.proficiency";
NSString *const kIdentifierCharismaSavingThrowAdvantage = @"monster.savingThrows.charisma.advantage";
NSString *const kIdentifierCharismaSavingThrowProficiency = @"monster.savingThrows.charisma.proficiency";

@implementation EditMonsterViewController {
    NSManagedObjectContext *_context;
    NSArray<MCChoice*>* _armorTypes;
    NSArray<MCChoice*>* _proficiencyTypes;
    NSArray<MCChoice*>* _advantageTypes;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate *appDelegate = (AppDelegate*)UIApplication.sharedApplication.delegate;
    _context = appDelegate.persistentContainer.viewContext;
    
    _armorTypes = [NSArray arrayWithObjects:
                   [MCChoice choiceWithLabel:NSLocalizedString(@"None", @"")
                                    andValue:kArmorNameNone],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Natural Armor", @"")
                                    andValue:kArmorNameNaturalArmor],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Mage Armor", @"")
                                    andValue:kArmorNameMageArmor],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Padded", @"")
                                    andValue:kArmorNamePadded],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Leather", @"")
                                    andValue:kArmorNameLeather],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Studded", @"")
                                    andValue:kArmorNameStuddedLeather],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Hide", @"")
                                    andValue:kArmorNameHide],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Chain Shirt", @"")
                                    andValue:kArmorNameChainShirt],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Scale Mail", @"")
                                    andValue:kArmorNameScaleMail],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Breastplate", @"")
                                    andValue:kArmorNameBreastplate],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Half Plate", @"")
                                    andValue:kArmorNameHalfPlate],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Ring Mail", @"")
                                    andValue:kArmorNameRingMail],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Chain Mail", @"")
                                    andValue:kArmorNameChainMail],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Splint", @"")
                                    andValue:kArmorNameSplintMail],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Plate", @"")
                                    andValue:kArmorNamePlateMail],
                   [MCChoice choiceWithLabel:NSLocalizedString(@"Other", @"")
                                    andValue:kArmorNameOther],
                   nil];
    _proficiencyTypes = [NSArray arrayWithObjects:
                                 [MCChoice choiceWithLabel:NSLocalizedString(@"None", @"")
                                                  andValue:kProficiencyTypeNone],
                                 [MCChoice choiceWithLabel:NSLocalizedString(@"Proficient", @"")
                                                  andValue:kProficiencyTypeProficient],
                                 [MCChoice choiceWithLabel:NSLocalizedString(@"Expertise", @"")
                                                  andValue:kProficiencyTypeExpertise],
                                 nil];
    _advantageTypes = [NSArray arrayWithObjects:
                       [MCChoice choiceWithLabel:NSLocalizedString(@"None", @"")
                                        andValue:kAdvantageTypeNone],
                       [MCChoice choiceWithLabel:NSLocalizedString(@"Advantage", @"")
                                        andValue:kAdvantageTypeAdvantage],
                       [MCChoice choiceWithLabel:NSLocalizedString(@"Disadvantage", @"")
                                        andValue:kAdvantageTypeDisadvantage],
                       nil];

    self.monsterTableView.allowsSelection = NO;
    self.monsterTableView.allowsSelectionDuringEditing = NO;
    self.monsterTableView.dataSource = self;
    self.monsterTableView.delegate = self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.editingMonster = [[Monster alloc] initWithMonster:self.originalMonster];
}

- (UITableViewCell*) makeSafeCell {
#if DEBUG
    return nil;
#else
    return [[UITableViewCell alloc] init];
#endif
}

- (MCShortStringFieldTableViewCell*) makeShortStringCellFromTableView:(UITableView*)tableView
                                                       withIdentifier:(NSString*)identifier
                                                                label:(NSString*)label
                                                      andInitialValue:(NSString*)initialValue {
    MCShortStringFieldTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MCShortStringField"];
    if (!cell || ![cell isKindOfClass:[MCShortStringFieldTableViewCell class]]) {
        return nil;
    }
    cell.delegate = self;
    cell.identifier = identifier;
    cell.label = label;
    cell.value = initialValue;

    // TODO: move these to better properties on MCShortStringFieldTableViewCell they should be stored via label and initialValue/value.
    cell.textField.text = initialValue;
    cell.textField.placeholder = label;

    return cell;
}

- (MCIntegerFieldTableViewCell*) makeIntegerCellFromTableView:(UITableView*)tableView
                                               withIdentifier:(NSString*)identifier
                                                        label:(NSString*)label
                                                     andInitialValue:(int)initialValue {
    MCIntegerFieldTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MCIntegerField"];
    if (!cell || ![cell isKindOfClass:[MCIntegerFieldTableViewCell class]]) {
        return nil;
    }
    cell.delegate = self;
    cell.identifier = identifier;
    cell.label = label;
    cell.value = initialValue;

    return cell;
}

- (MCBooleanFieldTableViewCell*) makeBooleanCellFromTableView:(UITableView*)tableView
                                               withIdentifier:(NSString*)identifier
                                                        label:(NSString*)label
                                              andInitialValue:(BOOL)initialValue {
    MCBooleanFieldTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MCBooleanField"];
    if (!cell || ![cell isKindOfClass:[MCBooleanFieldTableViewCell class]]) {
        return nil;
    }
    cell.delegate = self;
    cell.identifier = identifier;
    cell.label = label;
    cell.value = initialValue;
    
    return cell;
}

- (MCSelectFieldTableViewCell*) makeSelectCellFromTableView:(UITableView*)tableView
                                             withIdentifier:(NSString*)identifier
                                                      label:(NSString*)label
                                               initialValue:(NSObject*)initialValue
                                                 andChoices:(NSArray*)choices {
    MCSelectFieldTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MCSelectField"];
    if (!cell || ![cell isKindOfClass:[MCSelectFieldTableViewCell class]]) {
        return nil;
    }
    cell.delegate = self;
    cell.identifier = identifier;
    cell.label = label;
    cell.selectedValue = initialValue;
    cell.choices = choices;

    return cell;
}

- (MCRadioFieldTableViewCell*) makeRadioCellFromTableView:(UITableView*)tableView
                                           withIdentifier:(NSString*)identifier
                                                    label:(NSString*)label
                                             initialValue:(NSObject*)initialValue
                                               andChoices:(NSArray*)choices {
    MCRadioFieldTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MCRadioField"];
    if (!cell || ![cell isKindOfClass:[MCRadioFieldTableViewCell class]]) {
        return nil;
    }
    cell.delegate = self;
    cell.identifier = identifier;
    cell.label = label;
    // TODO: possibly swap these two
    cell.selectedValue = initialValue;
    cell.choices = choices;
    
    return cell;
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"DiscardChanges" isEqualToString:segue.identifier]) {
        [_context rollback];
    } else if ([@"SaveChanges" isEqualToString:segue.identifier]) {
        // TODO: this should use a method on originalMonster to copy values from editingMonster or pass the new monster back some way. Core Data would save and probably trigger a refresh in the receiving view.
        [self.originalMonster copyFromMonster:self.editingMonster];
        [_context refreshObject:self.editingMonster mergeChanges:NO];
        [_context save:nil];
    } else {
        NSLog(@"Unknown Segue %@", segue.identifier);
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
    switch(section) {
        case kSectionIndexBasicInfo:
            // Section 0 is basic info
            //   * Name
            //   * Size
            //   * Type
            //   * Subtype
            //   * Alignment
            return 8;
        case kSectionIndexArmor:
            return 4;
        case kSectionIndexSpeed:
            return 8;
        case kSectionIndexAbilityScores:
            return 6;
        case kSectionIndexSavingThrows:
            return 12; // 12
        default:
            return 0;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 5;
}

- (NSString *)tableView:(UITableView *)tableView
titleForHeaderInSection:(NSInteger)section {
    switch(section) {
        case kSectionIndexBasicInfo:
            return NSLocalizedString(@"Basic Info", @"Section title");
        case kSectionIndexArmor:
            return NSLocalizedString(@"Armor", @"Section title");
        case kSectionIndexSpeed:
            return NSLocalizedString(@"Speed", @"Section title");
        case kSectionIndexAbilityScores:
            return NSLocalizedString(@"Ability Scores", @"Section title");
        case kSectionIndexSavingThrows:
            return NSLocalizedString(@"Saving Throws", @"Section title");
        default:
            return nil;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    
    UITableViewCell *newCell = nil;

    switch (indexPath.section) {
        case kSectionIndexBasicInfo:
            switch (indexPath.row) {
                case kBasicInfoSectionRowIndexName:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierName
                                                               label:NSLocalizedString(@"Name", @"Placeholder text for the name of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.name];
                    break;
                case kBasicInfoSectionRowIndexSize:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierSize
                                                               label:NSLocalizedString(@"Size", @"Placehodler text for the size of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.size];
                    break;
                case kBasicInfoSectionRowIndexType:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierType
                                                               label:NSLocalizedString(@"Type", @"Placehodler text for the type of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.type];
                    break;
                case kBasicInfoSectionRowIndexSubtype:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierSubtype
                                                               label:NSLocalizedString(@"Subtype", @"Placeholder text for the subtype of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.subtype];
                    break;
                case kBasicInfoSectionRowIndexAlignment:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierAlignment
                                                               label: NSLocalizedString(@"Alignment", @"Placeholder text for the alignment of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.alignment];
                    break;
                case kBasicInfoSectionRowIndexHitDice:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierHitDice
                                                        label:NSLocalizedString(@"Hit Dice", @"")
                                              andInitialValue:self.editingMonster.hitDice];
                    break;
                case kBasicInfoSectionRowIndexCustomHP:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierHasCustomHP
                                                        label:NSLocalizedString(@"Custom HP", @"")
                                              andInitialValue:self.editingMonster.customHP];
                    break;
                case kBasicInfoSectionRowIndexCustomHPText:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:kIdentifierCustomHP
                                                            label:NSLocalizedString(@"Custom HP Text", @"")
                                                  andInitialValue:self.editingMonster.hpText];
                    break;
            }
            break;
        case kSectionIndexArmor:
            switch (indexPath.row) {
                case kArmorSectionRowIndexArmorType:
                    newCell = [self makeSelectCellFromTableView:self.monsterTableView
                                                 withIdentifier:kIdentifierArmorType
                                                          label:NSLocalizedString(@"Type", @"")
                                                   initialValue:self.editingMonster.armorType
                                                     andChoices:_armorTypes];
                    break;
                case kArmorSectionRowIndexHasShield:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                                  withIdentifier:kIdentifierHasShield
                                                           label:NSLocalizedString(@"Shield", @"")
                                                 andInitialValue:self.editingMonster.hasShield];
                    break;
                case kArmorSectionRowIndexCustomArmor:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:kIdentifierCustomArmor
                                                               label:NSLocalizedString(@"Custom Armor", @"")
                                                     andInitialValue:self.editingMonster.customArmor];
                    break;
                case kArmorSectionRowIndexNaturalArmorBonus:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                                  withIdentifier:kIdentifierNaturalArmorBonus
                                                           label:NSLocalizedString(@"Natural Armor Bonus", @"")
                                                 andInitialValue:self.editingMonster.naturalArmorBonus];
                    break;
            }
            break;
        case kSectionIndexSpeed:
            switch (indexPath.row) {
                case kSpeedSectionRowIndexBaseSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierBaseSpeed
                                                        label:NSLocalizedString(@"Base", @"")
                                              andInitialValue:self.editingMonster.baseSpeed];
                    break;
                case kSpeedSectionRowIndexBurrowSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierBurrowSpeed
                                                        label:NSLocalizedString(@"Burrow", @"")
                                              andInitialValue:self.editingMonster.burrowSpeed];
                    break;
                case kSpeedSectionRowIndexClimbSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierClimbSpeed
                                                        label:NSLocalizedString(@"Climb", @"")
                                              andInitialValue:self.editingMonster.climbSpeed];
                    break;
                case kSpeedSectionRowIndexFlySpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierFlySpeed
                                                        label:NSLocalizedString(@"Fly", @"")
                                              andInitialValue:self.editingMonster.flySpeed];
                    break;
                case kSpeedSectionRowIndexCanHover:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierCanHover
                                                        label:NSLocalizedString(@"Hover", @"")
                                              andInitialValue:self.editingMonster.canHover];
                    break;
                case kSpeedSectionRowIndexSwimSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierSwimSpeed
                                                        label:NSLocalizedString(@"Swim", @"")
                                              andInitialValue:self.editingMonster.swimSpeed];
                    break;
                case kSpeedSectionRowIndexHasCustomSpeed:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierHasCustomSpeed
                                                        label:NSLocalizedString(@"Custom Speed", @"")
                                              andInitialValue:self.editingMonster.hasCustomSpeed];
                    break;
                case kSpeedSectionRowIndexCustomSpeed:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:kIdentifierCustomSpeed
                                                            label:NSLocalizedString(@"Custom Speed", @"")
                                                  andInitialValue:self.editingMonster.customSpeed];
                    break;
            }
            break;
        case kSectionIndexAbilityScores:
            switch (indexPath.row) {
                case kAbilityScoreSectionRowIndexStrength:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierStrengthScore
                                                        label:NSLocalizedString(@"STR", @"Placeholder abbreviation for the strength score of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.strengthScore];
                    break;
                case kAbilityScoreSectionRowIndexDexterity:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentiferDexterityScore
                                                        label:NSLocalizedString(@"DEX", @"Placeholder abbreviation for the dexterity score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.dexterityScore];
                    break;
                case kAbilityScoreSectionRowIndexConstitution:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierConstitutionScore
                                                        label:NSLocalizedString(@"CON", @"Placeholder abbreviation for the constitution score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.constitutionScore];
                    break;
                case kAbilityScoreSectionRowIndexIntelligence:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierIntelligenceScore
                                                        label:NSLocalizedString(@"INT", @"Placeholder abbreviation for the intelligence score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.intelligenceScore];
                    break;
                case kAbilityScoreSectionRowIndexWisdom:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierWisdomScore
                                                        label:NSLocalizedString(@"WIS", @"Placeholder abbreviation for the wisdom score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.wisdomScore];
                    break;
                case kAbilityScoreSectionRowIndexCharisma:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:kIdentifierCharismaScore
                                                        label:NSLocalizedString(@"CHA", @"Placeholder abbreviation for the charisma score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.charismaScore];
                    break;
                    
            }
            break;
        case kSectionIndexSavingThrows:
            switch (indexPath.row) {
                case kSavingThrowsSectionRowIndexStrengthProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierStrengthSavingThrowProficiency
                                                         label:NSLocalizedString(@"Strength Proficiency", @"")
                                                  initialValue:self.editingMonster.strengthSavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexStrengthAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierStrengthSavingThrowAdvantage
                                                         label:NSLocalizedString(@"Strength Advantage", @"")
                                                  initialValue:self.editingMonster.strengthSavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
                case kSavingThrowsSectionRowIndexDexterityProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierDexteritySavingThrowProficiency
                                                         label:NSLocalizedString(@"Dexterity Proficiency", @"")
                                                  initialValue:self.editingMonster.dexteritySavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexDexterityAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierDexteritySavingThrowAdvantage
                                                         label:NSLocalizedString(@"Dexterity Advantage", @"")
                                                  initialValue:self.editingMonster.dexteritySavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
                case kSavingThrowsSectionRowIndexConstitutionProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierConstitutionSavingThrowProficiency
                                                         label:NSLocalizedString(@"Constitution Proficiency", @"")
                                                  initialValue:self.editingMonster.constitutionSavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexConstitutionAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierConstitutionSavingThrowAdvantage
                                                         label:NSLocalizedString(@"Constitution Advantage", @"")
                                                  initialValue:self.editingMonster.constitutionSavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
                case kSavingThrowsSectionRowIndexIntelligenceProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierIntelligenceSavingThrowProficiency
                                                         label:NSLocalizedString(@"Intelligence Proficiency", @"")
                                                  initialValue:self.editingMonster.intelligenceSavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexIntelligenceAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierIntelligenceSavingThrowAdvantage
                                                         label:NSLocalizedString(@"Intelligence Advantage", @"")
                                                  initialValue:self.editingMonster.intelligenceSavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
                case kSavingThrowsSectionRowIndexWisdomProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierWisdomSavingThrowProficiency
                                                         label:NSLocalizedString(@"Wisdom Proficiency", @"")
                                                  initialValue:self.editingMonster.wisdomSavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexWisdomAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierWisdomSavingThrowAdvantage
                                                         label:NSLocalizedString(@"Wisdom Advantage", @"")
                                                  initialValue:self.editingMonster.wisdomSavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
                case kSavingThrowsSectionRowIndexCharismaProficiency:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierCharismaSavingThrowProficiency
                                                         label:NSLocalizedString(@"Charisma Proficiency", @"")
                                                  initialValue:self.editingMonster.charismaSavingThrowProficiency
                                                    andChoices:_proficiencyTypes];
                    break;
                case kSavingThrowsSectionRowIndexCharismaAdvantage:
                    newCell = [self makeRadioCellFromTableView:self.monsterTableView
                                                withIdentifier:kIdentifierCharismaSavingThrowAdvantage
                                                         label:NSLocalizedString(@"Charisma Advantage", @"")
                                                  initialValue:self.editingMonster.charismaSavingThrowAdvantage
                                                    andChoices:_advantageTypes];
                    break;
            }
            break;
    }
    
    if (!newCell) {
        NSLog(@"ERROR: Unable to build a cell for %@", indexPath);
        newCell = [self makeSafeCell];
    }
    
    return newCell;
}

#pragma mark - MCShortStringFieldDelegate

- (void)editableValueDidChange:(NSObject*)value forIdentifier:(NSString*)identifier andType:(NSString*)type {
    if ([kMCFieldValueTypeString isEqualToString:type]) {
        if ([kIdentifierName isEqualToString:identifier]) {
            self.editingMonster.name = (NSString*)value;
        } else if ([kIdentifierSize isEqualToString:identifier]) {
            self.editingMonster.size = (NSString*)value;
        } else if ([kIdentifierType isEqualToString:identifier]) {
            self.editingMonster.type = (NSString*)value;
        } else if ([kIdentifierSubtype isEqualToString:identifier]) {
            self.editingMonster.subtype = (NSString*)value;
        } else if ([kIdentifierAlignment isEqualToString:identifier]) {
            self.editingMonster.alignment = (NSString*)value;
        } else if ([kIdentifierCustomHP isEqualToString:identifier]) {
            self.editingMonster.hpText = (NSString*)value;
        } else if ([kIdentifierCustomSpeed isEqualToString:identifier]) {
            self.editingMonster.customSpeed = (NSString*)value;
        } else if ([kIdentifierCustomArmor isEqualToString:identifier]) {
            self.editingMonster.customArmor = (NSString*)value;
        }
    }
    if ([kMCFieldValueTypeInteger isEqualToString:type]) {
        if ([kIdentifierStrengthScore isEqualToString:identifier]) {
            self.editingMonster.strengthScore = [(NSNumber*)value intValue];
        } else if ([kIdentiferDexterityScore isEqualToString:identifier]) {
            self.editingMonster.dexterityScore = [(NSNumber*)value intValue];
        } else if ([kIdentifierConstitutionScore isEqualToString:identifier]) {
            self.editingMonster.constitutionScore = [(NSNumber*)value intValue];
        } else if ([kIdentifierIntelligenceScore isEqualToString:identifier]) {
            self.editingMonster.intelligenceScore = [(NSNumber*)value intValue];
        } else if ([kIdentifierWisdomScore isEqualToString:identifier]) {
            self.editingMonster.wisdomScore = [(NSNumber*)value intValue];
        } else if ([kIdentifierCharismaScore isEqualToString:identifier]) {
            self.editingMonster.charismaScore = [(NSNumber*)value intValue];
        } else if ([kIdentifierHitDice isEqualToString:identifier]) {
            self.editingMonster.hitDice = [(NSNumber*)value intValue];
        } else if ([kIdentifierBaseSpeed isEqualToString:identifier]) {
            self.editingMonster.baseSpeed = [(NSNumber*)value intValue];
        } else if ([kIdentifierBurrowSpeed isEqualToString:identifier]) {
            self.editingMonster.burrowSpeed = [(NSNumber*)value intValue];
        } else if ([kIdentifierClimbSpeed isEqualToString:identifier]) {
            self.editingMonster.climbSpeed = [(NSNumber*)value intValue];
        } else if ([kIdentifierFlySpeed isEqualToString:identifier]) {
            self.editingMonster.flySpeed = [(NSNumber*)value intValue];
        } else if ([kIdentifierSwimSpeed isEqualToString:identifier]) {
            self.editingMonster.swimSpeed = [(NSNumber*)value intValue];
        } else if ([kIdentifierNaturalArmorBonus isEqualToString:identifier]) {
            self.editingMonster.naturalArmorBonus = [(NSNumber*)value intValue];
        }
    }
    if ([kMCFieldValueTypeBoolean isEqualToString:type]) {
        if ([kIdentifierHasCustomHP isEqualToString:identifier]) {
            self.editingMonster.customHP = [(NSNumber*)value boolValue];
        } else if ([kIdentifierCanHover isEqualToString:identifier]) {
            self.editingMonster.canHover = [(NSNumber*)value boolValue];
        } else if ([kIdentifierHasCustomSpeed isEqualToString:identifier]) {
            self.editingMonster.hasCustomSpeed = [(NSNumber*)value boolValue];
        } else if ([kIdentifierHasShield isEqualToString:identifier]) {
            self.editingMonster.hasShield = [(NSNumber*)value boolValue];
        }
    }
    if ([kMCFieldValueTypeChoice isEqualToString:type]) {
        if ([kIdentifierArmorType isEqualToString:identifier]) {
            self.editingMonster.armorType = (NSString*)value;
        } else if ([kIdentifierStrengthSavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.strengthSavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierStrengthSavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.strengthSavingThrowProficiency = (NSString*)value;
        } else if ([kIdentifierDexteritySavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.dexteritySavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierDexteritySavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.dexteritySavingThrowProficiency = (NSString*)value;
        } else if ([kIdentifierConstitutionSavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.constitutionSavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierConstitutionSavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.constitutionSavingThrowProficiency = (NSString*)value;
        } else if ([kIdentifierIntelligenceSavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.intelligenceSavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierIntelligenceSavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.intelligenceSavingThrowProficiency = (NSString*)value;
        } else if ([kIdentifierWisdomSavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.wisdomSavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierWisdomSavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.wisdomSavingThrowProficiency = (NSString*)value;
        } else if ([kIdentifierCharismaSavingThrowAdvantage isEqualToString:identifier]) {
            self.editingMonster.charismaSavingThrowAdvantage = (NSString*)value;
        } else if ([kIdentifierCharismaSavingThrowProficiency isEqualToString:identifier]) {
            self.editingMonster.charismaSavingThrowProficiency = (NSString*)value;
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return UITableViewAutomaticDimension;
}

@end
