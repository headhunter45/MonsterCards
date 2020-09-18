//
//  EditableShortStringTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCShortStringFieldTableViewCell.h"

@implementation MCShortStringFieldTableViewCell

@synthesize value = _value;

- (void)setValue:(NSString*)value {
    if (![_value isEqualToString:value]) {
        _value = value;
        if (self.textField) {
            self.textField.text = value;
        }
    }
}

- (NSString*)value {
    return _value;
}

@synthesize label = _label;

- (void)setLabel:(NSString*)label {
    if (![_label isEqualToString:label]) {
        _label = label;
        if (self.textField) {
            self.textField.placeholder = label;
        }
    }
}

- (NSString*)label {
    return _label;
}

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
