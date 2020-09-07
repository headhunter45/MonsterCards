//
//  MonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MonsterViewController.h"

@interface MonsterViewController ()

@end

@implementation MonsterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    if (self.monsterName != nil) {
        self.monsterName.text = self.monster.name;
    } else if (self.navigationItem != nil && self.navigationItem.title != nil) {
        self.navigationItem.title = self.monster.name;
    }
}

@end
