//
//  MCIntegerFieldTableViewCell.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/17/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MCFormFieldDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface MCIntegerFieldTableViewCell : UITableViewCell <UITextFieldDelegate>

@property NSString* identifier;
@property NSString* label;
@property int value;

@property (weak, nonatomic) id<MCFormFieldDelegate> delegate;
@property (weak, nonatomic) IBOutlet UITextField *textField;
@property (weak, nonatomic) IBOutlet UIStepper *stepper;
- (IBAction)stepperValueChanged:(id)sender;

@end

NS_ASSUME_NONNULL_END
