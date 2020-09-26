//
//  EditMonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/8/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "EditMonsterViewController.h"
#import "MCShortStringFieldTableViewCell.h"
#import "MCIntegerFieldTableViewCell.h"
#import "MCBooleanFieldTableViewCell.h"
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

const int kArmorSectionRowIndexHitDice = 0;
const int kArmorSectionRowIndexCustomHP = 1;
const int kArmorSectionRowIndexCustomHPText = 2;

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
}

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate *appDelegate = (AppDelegate*)UIApplication.sharedApplication.delegate;
    _context = appDelegate.persistentContainer.viewContext;

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
            return 5;
        case kSectionIndexArmor:
            return 3;
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
            return NSLocalizedString(@"Armor and HP", @"Section title");
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
            }
            break;
        case kSectionIndexArmor:
            switch (indexPath.row) {
                case kArmorSectionRowIndexHitDice:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.hitDice"
                                                        label:NSLocalizedString(@"Hit Dice", @"")
                                              andInitialValue:self.editingMonster.hitDice];
                case kArmorSectionRowIndexCustomHP:
                    return [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.customHP"
                                                        label:NSLocalizedString(@"Custom HP", @"")
                                              andInitialValue:self.editingMonster.customHP];
                    return nil;
                case kArmorSectionRowIndexCustomHPText:
                    return [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:@"monster.customHPText"
                                                            label:NSLocalizedString(@"Custom HP Text", @"")
                                                  andInitialValue:self.editingMonster.hpText];
            }
            break;
        case kSectionIndexSpeed:
            switch (indexPath.row) {
                case kSpeedSectionRowIndexBaseSpeed:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.baseSpeed"
                                                        label:NSLocalizedString(@"Base", @"")
                                              andInitialValue:self.editingMonster.baseSpeed];
                case kSpeedSectionRowIndexBurrowSpeed:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.burrowSpeed"
                                                        label:NSLocalizedString(@"Burrow", @"")
                                              andInitialValue:self.editingMonster.burrowSpeed];
                case kSpeedSectionRowIndexClimbSpeed:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.climbSpeed"
                                                        label:NSLocalizedString(@"Climb", @"")
                                              andInitialValue:self.editingMonster.climbSpeed];
                case kSpeedSectionRowIndexFlySpeed:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.flySpeed"
                                                        label:NSLocalizedString(@"Fly", @"")
                                              andInitialValue:self.editingMonster.flySpeed];
                case kSpeedSectionRowIndexCanHover:
                    return [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.canHover"
                                                        label:NSLocalizedString(@"Hover", @"")
                                              andInitialValue:self.editingMonster.canHover];
                case kSpeedSectionRowIndexSwimSpeed:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.swimSpeed"
                                                        label:NSLocalizedString(@"Swim", @"")
                                              andInitialValue:self.editingMonster.swimSpeed];
                case kSpeedSectionRowIndexHasCustomSpeed:
                    return [self makeBooleanCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.hasCustomSpeed"
                                                        label:NSLocalizedString(@"Custom Speed", @"")
                                              andInitialValue:self.editingMonster.hasCustomSpeed];
                case kSpeedSectionRowIndexCustomSpeed:
                    return [self makeShortStringCellFromTableView:self.monsterTableView
                                                   withIdentifier:@"monster.customSpeed"
                                                            label:NSLocalizedString(@"Custom Speed", @"")
                                                  andInitialValue:self.editingMonster.customSpeed];

            }
            break;
        case kSectionIndexAbilityScores:
            switch (indexPath.row) {
                case kAbilityScoreSectionRowIndexStrength:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.strengthScore"
                                                        label:NSLocalizedString(@"STR", @"Placeholder abbreviation for the strength score of a monster or NPC.")
                                                     andInitialValue:self.editingMonster.strengthScore];
                case kAbilityScoreSectionRowIndexDexterity:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.dexterityScore"
                                                        label:NSLocalizedString(@"DEX", @"Placeholder abbreviation for the dexterity score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.dexterityScore];
                case kAbilityScoreSectionRowIndexConstitution:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.constitutionScore"
                                                        label:NSLocalizedString(@"CON", @"Placeholder abbreviation for the constitution score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.constitutionScore];
                case kAbilityScoreSectionRowIndexIntelligence:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.intelligenceScore"
                                                        label:NSLocalizedString(@"INT", @"Placeholder abbreviation for the intelligence score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.intelligenceScore];
                case kAbilityScoreSectionRowIndexWisdom:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.wisdomScore"
                                                        label:NSLocalizedString(@"WIS", @"Placeholder abbreviation for the wisdom score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.wisdomScore];
                case kAbilityScoreSectionRowIndexCharisma:
                    return [self makeIntegerCellFromTableView:self.monsterTableView
                                               withIdentifier:@"monster.charismaScore"
                                                        label:NSLocalizedString(@"CHA", @"Placeholder abbreviation for the charisma score of a monster or NPC.")
                                              andInitialValue:self.editingMonster.charismaScore];
                    
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
        }
    }
    if ([kMCFieldValueTypeBoolean isEqualToString:type]) {
        if ([@"monster.customHP" isEqualToString:identifier]) {
            self.editingMonster.customHP = [(NSNumber*)value boolValue];
        } else if ([@"monster.canHover" isEqualToString:identifier]) {
            self.editingMonster.canHover = [(NSNumber*)value boolValue];
        } else if ([@"monster.hasCustomSpeed" isEqualToString:identifier]) {
            self.editingMonster.hasCustomSpeed = [(NSNumber*)value boolValue];
        }
    }
}

@end
