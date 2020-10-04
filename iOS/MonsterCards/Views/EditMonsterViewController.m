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

@implementation EditMonsterViewController {
    NSManagedObjectContext *_context;
    NSArray<MCChoice*>* _armorTypes;
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
        default:
            return 0;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
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
                                                      withIdentifier:@"monster.name"
                                                               label:NSLocalizedString(@"Name", @"Placeholder text for the name of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.name];
                    break;
                case kBasicInfoSectionRowIndexSize:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:@"monster.size"
                                                               label:NSLocalizedString(@"Size", @"Placehodler text for the size of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.size];
                    break;
                case kBasicInfoSectionRowIndexType:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:@"monster.type"
                                                               label:NSLocalizedString(@"Type", @"Placehodler text for the type of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.type];
                    break;
                case kBasicInfoSectionRowIndexSubtype:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:@"monster.subtype"
                                                               label:NSLocalizedString(@"Subtype", @"Placeholder text for the subtype of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.subtype];
                    break;
                case kBasicInfoSectionRowIndexAlignment:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:@"monster.alignment"
                                                               label: NSLocalizedString(@"Alignment", @"Placeholder text for the alignment of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.alignment];
                    break;
                case kBasicInfoSectionRowIndexHitDice:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.hitDice"
                                                        label:NSLocalizedString(@"Hit Dice", @"")
                                              andInitialValue:self.editingMonster.hitDice];
                    break;
                case kBasicInfoSectionRowIndexCustomHP:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.customHP"
                                                        label:NSLocalizedString(@"Custom HP", @"")
                                              andInitialValue:self.editingMonster.customHP];
                    break;
                case kBasicInfoSectionRowIndexCustomHPText:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:@"monster.customHPText"
                                                            label:NSLocalizedString(@"Custom HP Text", @"")
                                                  andInitialValue:self.editingMonster.hpText];
                    break;
            }
            break;
        case kSectionIndexArmor:
            switch (indexPath.row) {
                case kArmorSectionRowIndexArmorType:
                    newCell = [self makeSelectCellFromTableView:self.monsterTableView
                                                 withIdentifier:@"monster.armorType"
                                                          label:NSLocalizedString(@"Type", @"")
                                                   initialValue:self.editingMonster.armorType
                                                     andChoices:_armorTypes];
                    break;
                case kArmorSectionRowIndexHasShield:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                                  withIdentifier:@"monster.hasShield"
                                                           label:NSLocalizedString(@"Shield", @"")
                                                 andInitialValue:self.editingMonster.hasShield];
                    break;
                case kArmorSectionRowIndexCustomArmor:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                      withIdentifier:@"monster.customArmor"
                                                               label:NSLocalizedString(@"Custom Armor", @"")
                                                     andInitialValue:self.editingMonster.customArmor];
                    break;
                case kArmorSectionRowIndexNaturalArmorBonus:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                                  withIdentifier:@"monster.naturalArmorBonus"
                                                           label:NSLocalizedString(@"Natural Armor Bonus", @"")
                                                 andInitialValue:self.editingMonster.naturalArmorBonus];
                    break;
            }
            break;
        case kSectionIndexSpeed:
            switch (indexPath.row) {
                case kSpeedSectionRowIndexBaseSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.baseSpeed"
                                                        label:NSLocalizedString(@"Base", @"")
                                              andInitialValue:self.editingMonster.baseSpeed];
                    break;
                case kSpeedSectionRowIndexBurrowSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.burrowSpeed"
                                                        label:NSLocalizedString(@"Burrow", @"")
                                              andInitialValue:self.editingMonster.burrowSpeed];
                    break;
                case kSpeedSectionRowIndexClimbSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.climbSpeed"
                                                        label:NSLocalizedString(@"Climb", @"")
                                              andInitialValue:self.editingMonster.climbSpeed];
                    break;
                case kSpeedSectionRowIndexFlySpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.flySpeed"
                                                        label:NSLocalizedString(@"Fly", @"")
                                              andInitialValue:self.editingMonster.flySpeed];
                    break;
                case kSpeedSectionRowIndexCanHover:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.canHover"
                                                        label:NSLocalizedString(@"Hover", @"")
                                              andInitialValue:self.editingMonster.canHover];
                    break;
                case kSpeedSectionRowIndexSwimSpeed:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.swimSpeed"
                                                        label:NSLocalizedString(@"Swim", @"")
                                              andInitialValue:self.editingMonster.swimSpeed];
                    break;
                case kSpeedSectionRowIndexHasCustomSpeed:
                    newCell = [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.hasCustomSpeed"
                                                        label:NSLocalizedString(@"Custom Speed", @"")
                                              andInitialValue:self.editingMonster.hasCustomSpeed];
                    break;
                case kSpeedSectionRowIndexCustomSpeed:
                    newCell = [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:@"monster.customSpeed"
                                                            label:NSLocalizedString(@"Custom Speed", @"")
                                                  andInitialValue:self.editingMonster.customSpeed];
                    break;
            }
            break;
        case kSectionIndexAbilityScores:
            switch (indexPath.row) {
                case kAbilityScoreSectionRowIndexStrength:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.strengthScore"
                                                        label:NSLocalizedString(@"STR", @"Placeholder abbreviation for the strength score of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.strengthScore];
                    break;
                case kAbilityScoreSectionRowIndexDexterity:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.dexterityScore"
                                                        label:NSLocalizedString(@"DEX", @"Placeholder abbreviation for the dexterity score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.dexterityScore];
                    break;
                case kAbilityScoreSectionRowIndexConstitution:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.constitutionScore"
                                                        label:NSLocalizedString(@"CON", @"Placeholder abbreviation for the constitution score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.constitutionScore];
                    break;
                case kAbilityScoreSectionRowIndexIntelligence:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.intelligenceScore"
                                                        label:NSLocalizedString(@"INT", @"Placeholder abbreviation for the intelligence score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.intelligenceScore];
                    break;
                case kAbilityScoreSectionRowIndexWisdom:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.wisdomScore"
                                                        label:NSLocalizedString(@"WIS", @"Placeholder abbreviation for the wisdom score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.wisdomScore];
                    break;
                case kAbilityScoreSectionRowIndexCharisma:
                    newCell = [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.charismaScore"
                                                        label:NSLocalizedString(@"CHA", @"Placeholder abbreviation for the charisma score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.charismaScore];
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
        if ([@"monster.name" isEqualToString:identifier]) {
            self.editingMonster.name = (NSString*)value;
        } else if ([@"monster.size" isEqualToString:identifier]) {
            self.editingMonster.size = (NSString*)value;
        } else if ([@"monster.type" isEqualToString:identifier]) {
            self.editingMonster.type = (NSString*)value;
        } else if ([@"monster.subtype" isEqualToString:identifier]) {
            self.editingMonster.subtype = (NSString*)value;
        } else if ([@"monster.alignment" isEqualToString:identifier]) {
            self.editingMonster.alignment = (NSString*)value;
        } else if ([@"monster.customHPText" isEqualToString:identifier]) {
            self.editingMonster.hpText = (NSString*)value;
        } else if ([@"monster.customSpeed" isEqualToString:identifier]) {
            self.editingMonster.customSpeed = (NSString*)value;
        } else if ([@"monster.customArmor" isEqualToString:identifier]) {
            self.editingMonster.customArmor = (NSString*)value;
        }
    }
    if ([kMCFieldValueTypeInteger isEqualToString:type]) {
        if ([@"monster.strengthScore" isEqualToString:identifier]) {
            self.editingMonster.strengthScore = [(NSNumber*)value intValue];
        } else if ([@"monster.dexterityScore" isEqualToString:identifier]) {
            self.editingMonster.dexterityScore = [(NSNumber*)value intValue];
        } else if ([@"monster.constitutionScore" isEqualToString:identifier]) {
            self.editingMonster.constitutionScore = [(NSNumber*)value intValue];
        } else if ([@"monster.intelligenceScore" isEqualToString:identifier]) {
            self.editingMonster.intelligenceScore = [(NSNumber*)value intValue];
        } else if ([@"monster.wisdomScore" isEqualToString:identifier]) {
            self.editingMonster.wisdomScore = [(NSNumber*)value intValue];
        } else if ([@"monster.charismaScore" isEqualToString:identifier]) {
            self.editingMonster.charismaScore = [(NSNumber*)value intValue];
        } else if ([@"monster.hitDice" isEqualToString:identifier]) {
            self.editingMonster.hitDice = [(NSNumber*)value intValue];
        } else if ([@"monster.baseSpeed" isEqualToString:identifier]) {
            self.editingMonster.baseSpeed = [(NSNumber*)value intValue];
        } else if ([@"monster.burrowSpeed" isEqualToString:identifier]) {
            self.editingMonster.burrowSpeed = [(NSNumber*)value intValue];
        } else if ([@"monster.climbSpeed" isEqualToString:identifier]) {
            self.editingMonster.climbSpeed = [(NSNumber*)value intValue];
        } else if ([@"monster.flySpeed" isEqualToString:identifier]) {
            self.editingMonster.flySpeed = [(NSNumber*)value intValue];
        } else if ([@"monster.swimSpeed" isEqualToString:identifier]) {
            self.editingMonster.swimSpeed = [(NSNumber*)value intValue];
        } else if ([@"monster.naturalArmorBonus" isEqualToString:identifier]) {
            self.editingMonster.naturalArmorBonus = [(NSNumber*)value intValue];
        }
    }
    if ([kMCFieldValueTypeBoolean isEqualToString:type]) {
        if ([@"monster.customHP" isEqualToString:identifier]) {
            self.editingMonster.customHP = [(NSNumber*)value boolValue];
        } else if ([@"monster.canHover" isEqualToString:identifier]) {
            self.editingMonster.canHover = [(NSNumber*)value boolValue];
        } else if ([@"monster.hasCustomSpeed" isEqualToString:identifier]) {
            self.editingMonster.hasCustomSpeed = [(NSNumber*)value boolValue];
        } else if ([@"monster.hasShield" isEqualToString:identifier]) {
            self.editingMonster.hasShield = [(NSNumber*)value boolValue];
        }
    }
    if ([kMCFieldValueTypeChoice isEqualToString:type]) {
        if ([@"monster.armorType" isEqualToString:identifier]) {
            self.editingMonster.armorType = (NSString*)value;
        }
    }
}

@end
