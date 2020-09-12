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

@interface LibraryViewController ()

@property NSArray* allMonsters;

@end

@implementation LibraryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Temporary setup of allMonsters until we bind to CoreData.
    Monster *pixie = [[Monster alloc] initWithJSONString:@"{\"name\":\"Pixie\"}"];
    Monster *acolyte = [[Monster alloc] initWithJSONString:@"{\"name\":\"Acolyte\"}"];
    self.allMonsters = [NSArray arrayWithObjects:acolyte, pixie, nil];
}

- (void)viewWillAppear:(BOOL)animated {
    // TODO: fetch monsters from CoreData
    [self.monstersTable reloadData];
}

- (IBAction)addNewMonster:(id)sender {
    Monster *monster = [[Monster alloc] init];
    monster.name = @"Unnamed Monster";
    self.allMonsters = [self.allMonsters arrayByAddingObject:monster];
    [self.monstersTable reloadData];
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
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
