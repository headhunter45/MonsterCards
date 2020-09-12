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
    monster.name = @"Unnamed Monster";
    self.allMonsters = [self.allMonsters arrayByAddingObject:monster];
    //DispatchQueue.main.async{"code here"}
    [_context save:nil];
    [self.monstersTable reloadData];
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"ShowMonsterDetail" isEqualToString:segue.identifier]) {
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        MonsterViewController *vc = (MonsterViewController*)segue.destinationViewController;
        vc.monster = [self.allMonsters objectAtIndex:indexPath.row];
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

@end
