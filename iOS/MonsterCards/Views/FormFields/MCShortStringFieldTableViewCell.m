//
//  EditableShortStringTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCShortStringFieldTableViewCell.h"

@implementation MCShortStringFieldTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [self.textField addTarget:self action:@selector(valueChanged:) forControlEvents:UIControlEventEditingChanged];
}

- (void)valueChanged:(UITextField*)textField {
    NSString *newValue = textField.text;
    if (self.delegate != nil) {
        [self.delegate editableValueDidChange:newValue
                                forIdentifier:self.identifier
                                      andType:kMCFieldValueTypeString];
    }
}

@end
