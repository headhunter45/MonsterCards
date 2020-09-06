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

@end

@implementation SearchViewController {
    NSMutableArray *_monsters;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    _searchBar.text = @"Hello, World!";
    _monsters = [[NSMutableArray alloc] init];
    Monster *monster;
    monster = [[Monster alloc] init];
    monster.name = @"Pixie";
    [_monsters addObject:monster];
    monster = [[Monster alloc] init];
    monster.name = @"Acolyte";
    [_monsters addObject:monster];

    // Do any additional setup after loading the view.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
    return [_monsters count];
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [_searchResults dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = ((Monster*)[_monsters objectAtIndex:indexPath.row]).name;
    
    return cell;
}

#pragma mark - UITableViewDelegate

// Tap on table Row
- (void) tableView: (UITableView *) tableView didSelectRowAtIndexPath: (NSIndexPath *) indexPath {
    
    [self performSegueWithIdentifier:@"ShowMonsterDetail" sender:self];
}


@end
