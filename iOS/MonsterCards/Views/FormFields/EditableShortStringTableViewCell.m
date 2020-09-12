//
//  EditableShortStringTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "EditableShortStringTableViewCell.h"

@implementation EditableShortStringTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.textField.delegate = self;
}

#pragma mark - UITextFieldDelegate

- (BOOL)textField:(UITextField *)textField
shouldChangeCharactersInRange:(NSRange)range
replacementString:(NSString *)string {
    // TODO: See this link for a potentially better way to get this text https://stackoverflow.com/questions/19110617/uitextfieldtextdidchangenotification-ios7-not-fired
    NSString *finalString = [textField.text stringByReplacingCharactersInRange:range withString:string];
    if (self.delegate != nil) {
        [self.delegate editableValueDidChange:finalString forIdentifier:self.identifier andType:@"String"];
    }
    return YES;
}

@end
