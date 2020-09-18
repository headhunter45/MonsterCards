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
#import "AppDelegate.h"

@interface EditMonsterViewController ()

@property Monster* editingMonster;

@end

const int kSectionIndexBasicInfo = 0;
const int kSectionIndexAbilityScores = 1;

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

- (MCShortStringFieldTableViewCell*) makeShortStringCellFromCell:(UITableViewCell*)cell {
    if (cell == nil || ![cell isKindOfClass:[MCShortStringFieldTableViewCell class]]) {
        // TODO: Figure out how to make this cell generate child views.
        return [[MCShortStringFieldTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MCShortStringField"];
    } else {
        return (MCShortStringFieldTableViewCell*)cell;
    }
}

- (MCIntegerFieldTableViewCell*) makeIntegerCellFromCell:(UITableViewCell*)cell {
    if (cell == nil || ![cell isKindOfClass:[MCIntegerFieldTableViewCell class]]) {
        // TODO: Figure out how to make this cell generate child views.
        return [[MCIntegerFieldTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MCIntegerField"];
    } else {
        return (MCIntegerFieldTableViewCell*)cell;
    }
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
        case kSectionIndexAbilityScores:
            return 0;
        default:
            return 0;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSString *)tableView:(UITableView *)tableView
titleForHeaderInSection:(NSInteger)section {
    switch(section) {
        case kSectionIndexBasicInfo:
            return NSLocalizedString(@"Basic Info", @"Section title");
        case kSectionIndexAbilityScores:
            return NSLocalizedString(@"Ability Scores", @"Section title");
        default:
            return nil;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    
    MCShortStringFieldTableViewCell *shortStringCell = nil;
    MCIntegerFieldTableViewCell *integerCell = nil;

    switch (indexPath.section) {
        case kSectionIndexBasicInfo:
            switch (indexPath.row) {
                case 0:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"MCShortStringField"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.name";
                    // TODO: make these use setters on MCShortStringFieldTableViewCell
                    shortStringCell.textField.text = self.editingMonster.name;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Name", @"Placeholder text for the name of a monster or NPC.");
                    return shortStringCell;
                case 1:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"MCShortStringField"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.size";
                    // TODO: make these use setters on MCShortStringFieldTableViewCell
                    shortStringCell.textField.text = self.editingMonster.size;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Size", @"Placehodler text for the size of a monster or NPC.");
                    return shortStringCell;
                case 2:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"MCShortStringField"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.type";
                    // TODO: make these use setters on MCShortStringFieldTableViewCell
                    shortStringCell.textField.text = self.editingMonster.type;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Type", @"Placehodler text for the type of a monster or NPC.");
                    return shortStringCell;
                case 3:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"MCShortStringField"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.subtype";
                    shortStringCell.textField.text = self.editingMonster.subtype;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Subtype", @"Placeholder text for the subtype of a monster or NPC.");
                    return shortStringCell;
                case 4:
                    shortStringCell = [self makeShortStringCellFromCell: [self.monsterTableView dequeueReusableCellWithIdentifier:@"MCShortStringField"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.alignment";
                    shortStringCell.textField.text = self.editingMonster.alignment;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Alignment", @"Placeholder text for the alignment of a monster or NPC.");
                    return shortStringCell;
            }
            break;
    }
    
#if DEBUG
    NSLog(@"ERROR: Unable to build a cell for %@", indexPath);
    return nil;
#else
    return [[UITableViewCell alloc] init];
#endif
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
        }
    }
    if ([kMCFieldValueTypeInteger isEqualToString:type]) {
        if ([@"monster.strengthScore" isEqualToString:identifier]) {
            self.editingMonster.strengthScore = [(NSNumber*)value intValue];
        }
    }
}

@end
