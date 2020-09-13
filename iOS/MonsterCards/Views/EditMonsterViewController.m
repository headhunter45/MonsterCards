//
//  EditMonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/8/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "EditMonsterViewController.h"
#import "EditableShortStringTableViewCell.h"
#import "AppDelegate.h"

@interface EditMonsterViewController ()

@property Monster* editingMonster;

@end

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
    // Section 0 is basic info
    //   * Name
    //   * Size
    //   * Type
    //   * Subtype
    //   * Alignment
    
    return 4;
}

- (EditableShortStringTableViewCell*) makeShortStringCellFromCell:(UITableViewCell*)cell {
    if (cell == nil || ![cell isKindOfClass:[EditableShortStringTableViewCell class]]) {
        // TODO: Figure out why this doesn't create a cell with a text field.
        return [[EditableShortStringTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"EditableShortString"];
    } else {
        return (EditableShortStringTableViewCell*)cell;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    
    EditableShortStringTableViewCell *shortStringCell = nil;

    switch (indexPath.section) {
        case 0:
            switch (indexPath.row) {
                case 0:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.name";
                    // TODO: make these use setters on EditableShortStringTableViewCell
                    shortStringCell.textField.text = self.editingMonster.name;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Name", @"Placeholder text for the name of a monster or NPC.");
                    return shortStringCell;
                case 1:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.size";
                    // TODO: make these use setters on EditableShortStringTableViewCell
                    shortStringCell.textField.text = self.editingMonster.size;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Size", @"Placehodler text for the size of a monster or NPC.");
                    return shortStringCell;
                case 2:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.type";
                    // TODO: make these use setters on EditableShortStringTableViewCell
                    shortStringCell.textField.text = self.editingMonster.type;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Type", @"Placehodler text for the type of a monster or NPC.");
                    return shortStringCell;
                case 3:
                    shortStringCell = [self makeShortStringCellFromCell:[self.monsterTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]];
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.subtype";
                    shortStringCell.textField.text = self.editingMonster.subtype;
                    shortStringCell.textField.placeholder = NSLocalizedString(@"Subtype", @"Placeholder text for the subtype of a monster or NPC.");
                    return shortStringCell;
            }
            break;
    }
    
    return nil;
}

#pragma mark - EditableShortStringDelegate

- (void)editableValueDidChange:(NSObject*)value forIdentifier:(NSString*)identifier andType:(NSString*)type {
    if ([@"String" isEqualToString:type]) {
        if ([@"monster.name" isEqualToString:identifier]) {
            self.editingMonster.name = (NSString*)value;
        } else if ([@"monster.size" isEqualToString:identifier]) {
            self.editingMonster.size = (NSString*)value;
        } else if ([@"monster.type" isEqualToString:identifier]) {
            self.editingMonster.type = (NSString*)value;
        } else if ([@"monster.subtype" isEqualToString:identifier]) {
            self.editingMonster.subtype = (NSString*)value;
        }
    }
}

@end
