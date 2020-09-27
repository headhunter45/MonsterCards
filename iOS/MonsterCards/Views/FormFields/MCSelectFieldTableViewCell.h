//
//  MCSelectFieldTableViewCell.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/26/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MCFormFieldDelegate.h"
#import "MCChoice.h"

NS_ASSUME_NONNULL_BEGIN

@interface MCSelectFieldTableViewCell : UITableViewCell<UIPickerViewDataSource, UIPickerViewDelegate, UITextFieldDelegate>

@property NSString* identifier;
@property NSString* label;
@property NSObject* selectedValue;
@property NSArray<MCChoice*>* choices;

@property (weak, nonatomic) id<MCFormFieldDelegate> delegate;
@property (weak, nonatomic) IBOutlet UITextField *textField;
@property (weak, nonatomic) IBOutlet UILabel *labelView;

@end

NS_ASSUME_NONNULL_END
