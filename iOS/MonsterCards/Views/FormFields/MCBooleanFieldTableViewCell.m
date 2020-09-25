//
//  MCBooleanFieldTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/25/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCBooleanFieldTableViewCell.h"

@implementation MCBooleanFieldTableViewCell

@synthesize value = _value;

- (void)setValue:(BOOL)value {
    if (value != _value) {
        _value = value;
        if (self.switchView) {
            self.switchView.on = _value;
        }
    }
}

- (BOOL)value {
    return _value;
}

@synthesize label = _label;

- (void)setLabel:(NSString*)label {
    if (![_label isEqualToString:label]) {
        _label = label;
        if (self.labelView) {
            self.labelView.text = label;
        }
    }
}

- (NSString*)label {
    return _label;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    self.switchView.on = self.value;
    self.labelView.text = self.label;
}

- (IBAction)valueChanged:(id)sender {
    self.value = self.switchView.on;
    if (self.delegate != nil) {
        [self.delegate editableValueDidChange:[NSNumber numberWithBool:self.value]
                                forIdentifier:self.identifier
                                      andType:kMCFieldValueTypeBoolean];
    }
}

@end
