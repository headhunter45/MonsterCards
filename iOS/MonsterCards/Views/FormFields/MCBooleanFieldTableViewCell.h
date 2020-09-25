//
//  MCBooleanFieldTableViewCell.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/25/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MCFormFieldDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface MCBooleanFieldTableViewCell : UITableViewCell

@property NSString* identifier;
@property NSString* label;
@property BOOL value;

@property (weak, nonatomic) id<MCFormFieldDelegate> delegate;
@property (weak, nonatomic) IBOutlet UILabel *labelView;
@property (weak, nonatomic) IBOutlet UISwitch *switchView;

@end

NS_ASSUME_NONNULL_END
