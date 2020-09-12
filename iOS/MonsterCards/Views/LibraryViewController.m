//
//  LibraryViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "LibraryViewController.h"
#import "Monster.h"
#import "MonsterViewController.h"
#import "AppDelegate.h"

@interface LibraryViewController ()

@property NSArray* allMonsters;

@end

@implementation LibraryViewController {
    NSManagedObjectContext *_context;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate *appDelegate = (AppDelegate*)UIApplication.sharedApplication.delegate;
    _context = appDelegate.persistentContainer.viewContext;
}

- (void)viewWillAppear:(BOOL)animated {
    self.allMonsters = [_context executeFetchRequest:[Monster fetchRequest] error:nil];
    [self.monstersTable reloadData];
}

- (IBAction)addNewMonster:(id)sender {
    Monster *monster = [[Monster alloc] initWithContext:_context];
    monster.name = NSLocalizedString(@"Unnamed Monster", @"The default name of a new monster.");
    self.allMonsters = [self.allMonsters arrayByAddingObject:monster];
    //DispatchQueue.main.async{"code here"}
    [_context save:nil];
    [self.monstersTable reloadData];
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"ShowMonsterDetail" isEqualToString:segue.identifier]) {
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        if ([segue.destinationViewController isKindOfClass:[MonsterViewController class]]) {
            MonsterViewController *vc = (MonsterViewController*)segue.destinationViewController;
            vc.monster = [self.allMonsters objectAtIndex:indexPath.row];
        }
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.allMonsters count];
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    static NSString *simpleTableIdentifier = @"MonsterCell";
    
    UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    Monster *monster = (Monster*)[self.allMonsters objectAtIndex:indexPath.row];
    
    cell.textLabel.text = monster.name;
    
    return cell;
}

#pragma  mark - UITableViewDelegate

- (UISwipeActionsConfiguration *)tableView:(UITableView *)tableView
trailingSwipeActionsConfigurationForRowAtIndexPath:(NSIndexPath *)indexPath {
    UIContextualAction *action = [UIContextualAction contextualActionWithStyle:UIContextualActionStyleDestructive title:NSLocalizedString(@"Delete", @"Command to delete an object.") handler:^(UIContextualAction *action, __kindof UIView *sourceView, void (^completionHandler)(BOOL actionPerformed)) {
        Monster *monster = [self.allMonsters objectAtIndex:indexPath.row];
        [self->_context deleteObject:monster];
        [self->_context save:nil];
        self.allMonsters = [self->_context executeFetchRequest:[Monster fetchRequest] error:nil];
        [self.tableView reloadData];
    }];
    
    UISwipeActionsConfiguration *config = [UISwipeActionsConfiguration configurationWithActions:[NSArray arrayWithObject:action]];
    return config;
}

@end
