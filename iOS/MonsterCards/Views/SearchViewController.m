//
//  SearchViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "SearchViewController.h"
#import "MonsterViewController.h"
#import "Monster.h"
#import "AppDelegate.h"

@interface SearchViewController ()

@property NSArray* allMonsters;
@property NSArray* foundMonsters;

@end

@implementation SearchViewController {
    NSManagedObjectContext *_context;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate *appDelegate = (AppDelegate*)UIApplication.sharedApplication.delegate;
    _context = appDelegate.persistentContainer.viewContext;
}

- (void)viewWillAppear:(BOOL)animated {
    NSString *searchText = nil;
    if (self.searchBar != nil) {
        searchText = self.searchBar.text;
    }
    self.allMonsters = [_context executeFetchRequest:[Monster fetchRequest] error:nil];
    self.foundMonsters = [self filterAllMonstersWithText:searchText];
    [self.tableView reloadData];
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"ShowMonsterDetail" isEqualToString:segue.identifier]) {
        NSIndexPath *indexPath = [self.searchResults indexPathForSelectedRow];
        if ([segue.destinationViewController isKindOfClass:[MonsterViewController class]]) {
            MonsterViewController *vc = (MonsterViewController*)segue.destinationViewController;
            vc.monster = [self.foundMonsters objectAtIndex:indexPath.row];
        }
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.foundMonsters count];
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    static NSString *simpleTableIdentifier = @"MonsterCell";
    
    UITableViewCell *cell = [self.searchResults dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    Monster *monster = (Monster*)[self.foundMonsters objectAtIndex:indexPath.row];
    
    cell.textLabel.text = monster.name;
    
    return cell;
}

#pragma mark - UISearchBarDelegate

- (NSArray*)filterAllMonstersWithText:(NSString *)searchText {
    if (searchText != nil && ![@"" isEqualToString:searchText]) {
        NSPredicate *predicate = [NSPredicate predicateWithBlock:^BOOL(id item, NSDictionary *bindings) {
            if (![item isKindOfClass:[Monster class]]) {
                return false;
            }
            Monster *monster = (Monster*)item;
            
            if ([monster.name localizedCaseInsensitiveContainsString:searchText]) {
                return true;
            }
            
            return false;
        }];
        return [self.allMonsters filteredArrayUsingPredicate:predicate];
    } else {
        return self.allMonsters;
    }
}

- (void)searchBar:(UISearchBar *)searchBar
    textDidChange:(NSString *)searchText {

    self.foundMonsters = [self filterAllMonstersWithText:searchText];
    
    [self.tableView reloadData];
}

@end
