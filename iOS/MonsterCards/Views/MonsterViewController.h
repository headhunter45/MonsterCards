//
//  MonsterViewController.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Monster.h"

NS_ASSUME_NONNULL_BEGIN

@interface MonsterViewController : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *monsterName;
@property (weak, nonatomic) IBOutlet UILabel *monsterMeta;
@property (weak, nonatomic) IBOutlet UILabel *monsterArmorClass;
@property (weak, nonatomic) IBOutlet UILabel *monsterHitPoints;
@property (weak, nonatomic) IBOutlet UILabel *monsterSpeed;

@property Monster* monster;

@end

NS_ASSUME_NONNULL_END
