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

@interface SearchViewController ()

@property NSArray* allMonsters;
@property NSArray* foundMonsters;

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    Monster *pixie = [[Monster alloc] initWithJSONString:@"{\"name\":\"Pixie\"}"];
    Monster *acolyte = [[Monster alloc] initWithJSONString:@"{\"name\":\"Acolyte\"}"];
    self.allMonsters = [NSArray arrayWithObjects:acolyte, pixie, nil];
    self.foundMonsters=  self.allMonsters;
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([@"ShowMonsterDetail" isEqualToString:segue.identifier]) {
        NSIndexPath *indexPath = [self.searchResults indexPathForSelectedRow];
        MonsterViewController *vc = (MonsterViewController*)segue.destinationViewController;
        vc.monster = [self.foundMonsters objectAtIndex:indexPath.row];
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

- (void)searchBar:(UISearchBar *)searchBar
    textDidChange:(NSString *)searchText {
    
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
        self.foundMonsters = [self.allMonsters filteredArrayUsingPredicate:predicate];
    } else {
        self.foundMonsters = self.allMonsters;
    }
    
    [self.tableView reloadData];
}

@end
