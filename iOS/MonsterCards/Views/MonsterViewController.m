//
//  MonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MonsterViewController.h"
#import "EditMonsterViewController.h"

@interface MonsterViewController ()

@end

@implementation MonsterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    if (self.monsterName != nil) {
        self.monsterName.text = self.monster.name;
    } else if (self.navigationItem != nil) {
        self.navigationItem.title = self.monster.name;
    }
}

- (void)viewWillAppear:(BOOL)animated {
    // TODO: get the latest version of this monster from CoreData
    if (self.monsterName != nil) {
        self.monsterName.text = self.monster.name;
    } else if (self.navigationItem != nil) {
        if (self.monster.name == nil) {
            self.navigationItem.title = @"Unnamed Monster";
        } else {
            self.navigationItem.title = self.monster.name;
        }
    }
}

- (IBAction)unwindWithSegue:(UIStoryboardSegue *)unwindSegue {
//    UIViewController *sourceViewController = unwindSegue.sourceViewController;
    // Use data from the view controller which initiated the unwind segue
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"EditMonster" isEqualToString:segue.identifier]) {
        if ([segue.destinationViewController isKindOfClass:[EditMonsterViewController class]]) {
            EditMonsterViewController *vc = (EditMonsterViewController*)segue.destinationViewController;
            vc.originalMonster = self.monster;
        }
    }
}

@end
