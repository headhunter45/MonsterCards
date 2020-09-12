//
//  EditableShortStringTableViewCell.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol EditableShortStringDelegate <NSObject>

@optional
-(void)editableValueDidChange:(NSString*)value forIdentifier:(NSString*)identifier andType:(NSString*)type;

@end

@interface EditableShortStringTableViewCell : UITableViewCell <UITextFieldDelegate>

@property NSString* identifier;
@property NSString* label;
@property NSString* value;
@property (nonatomic, weak) id<EditableShortStringDelegate> delegate;
@property (weak, nonatomic) IBOutlet UITextField *textField;

@end

NS_ASSUME_NONNULL_END
