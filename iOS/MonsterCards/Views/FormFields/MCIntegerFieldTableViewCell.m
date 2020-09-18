//
//  MCIntegerFieldTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/17/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCIntegerFieldTableViewCell.h"

@implementation MCIntegerFieldTableViewCell

@synthesize value = _value;

-(void) setValue:(int)number {
    if (_value != number) {
        NSNumber *newValue = [NSNumber numberWithInt:number];
        _value = number;
        if (self.textField) {
            self.textField.text = [newValue stringValue];
        }
        if (self.stepper) {
            self.stepper.value = number;
        }
        if (self.delegate) {
            [self.delegate  editableValueDidChange:newValue
                                     forIdentifier:self.identifier
                                           andType:kMCFieldValueTypeInteger];
        }
    }
}

- (int) value {
    return _value;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    [self.textField addTarget:self
                       action:@selector(textFieldValueChanged:)
             forControlEvents:UIControlEventEditingChanged];
}

- (void)textFieldValueChanged:(UITextField*)textField {
    self.value = [textField.text intValue];
}

- (IBAction)stepperValueChanged:(id)sender {
    self.value = self.stepper.value;
}
@end
