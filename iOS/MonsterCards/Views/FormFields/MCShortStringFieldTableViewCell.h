//
//  EditableShortStringTableViewCell.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MCFormFieldDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface MCShortStringFieldTableViewCell : UITableViewCell

@property NSString* identifier;
@property NSString* label;
@property NSString* value;

@property (weak, nonatomic) id<MCFormFieldDelegate> delegate;
@property (weak, nonatomic) IBOutlet UITextField *textField;

@end

NS_ASSUME_NONNULL_END
