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
        self.originalMonster.name = self.editingMonster.name;
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
    
    return 1;
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    
    EditableShortStringTableViewCell *shortStringCell = nil;

    switch (indexPath.section) {
        case 0:
            switch (indexPath.row) {
                case 0:
                    shortStringCell = [self.monsterTableView dequeueReusableCellWithIdentifier:@"EditableShortString"];
                    if (shortStringCell == nil) {
                        shortStringCell = [[EditableShortStringTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"EditableShortString"];
                    }
                    shortStringCell.delegate = self;
                    shortStringCell.identifier = @"monster.name";
                    // TODO: make these setters on EditableShortStringTableViewCell
                    shortStringCell.textField.text = self.editingMonster.name;
                    shortStringCell.textField.placeholder = @"Name";
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
        }
    }
}

@end
