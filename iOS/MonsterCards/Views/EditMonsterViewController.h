//
//  EditMonsterViewController.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/8/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Monster.h"
#import "MCShortStringFieldTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface EditMonsterViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, MCFormFieldDelegate>

@property Monster* originalMonster;
@property (weak, nonatomic) IBOutlet UITableView *monsterTableView;

@end

NS_ASSUME_NONNULL_END
