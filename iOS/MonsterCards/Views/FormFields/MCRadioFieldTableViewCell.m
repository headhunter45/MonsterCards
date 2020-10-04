//
//  MCRadioFieldTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/26/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCRadioFieldTableViewCell.h"

@implementation MCRadioFieldTableViewCell {
    MCChoice* _selectedChoice;
}

-(MCChoice*)findChoiceWithValue:(NSObject*)value
                        inArray:(NSArray*)array {
    NSPredicate *predicate = [NSPredicate predicateWithBlock:^BOOL(id element, NSDictionary *bindings) {
        if (![element isKindOfClass:[MCChoice class]]) {
            return NO;
        }
        MCChoice *choice = (MCChoice*)element;
        return [choice.value isEqual:value];
    }];
    NSArray<MCChoice*> *matchingChoices = [array filteredArrayUsingPredicate:predicate];
    MCChoice *foundChoice = matchingChoices.count > 0 ? matchingChoices.firstObject : nil;
    return foundChoice;
}

-(void)notifyChangedValue {
    NSUInteger selectedIndex = [_choices indexOfObject:_selectedChoice];
    [self.segmentedControl setSelectedSegmentIndex:selectedIndex];
    
    if (_delegate) {
        [_delegate editableValueDidChange:_selectedValue
                            forIdentifier:_identifier
                                  andType:kMCFieldValueTypeChoice];
    }
}

-(void)updateSegments {
    if (_segmentedControl) {
        [_segmentedControl removeAllSegments];
        int index = 0;
        for (MCChoice *choice in _choices) {
            [_segmentedControl insertSegmentWithTitle:choice.label atIndex:index animated:NO];
            index++;
        }
        _segmentedControl.selectedSegmentIndex = [_choices indexOfObject:_selectedChoice];
    }
}

@synthesize choices = _choices;
-(void)setChoices:(NSArray<MCChoice*>*)choices {
    MCChoice *foundChoice = [self findChoiceWithValue:_selectedValue
                                              inArray:choices];
    if ([_choices isEqualToArray:choices]) {
        // Choices are equivalent so selected value. Pointer may have changed but content hasn't.
        _selectedChoice = foundChoice;
    } else if (foundChoice) {
        // Choices are different but selected value is in the new choices. Pointer may have changed but content hasn't.
        _selectedChoice = foundChoice;
    } else {
        // Choices are different and selected value is not in the new choices. Select the first choice or nil if there are none.
        _selectedChoice = [choices firstObject];
    }
    _choices = choices;
    
    if (_selectedValue != foundChoice.value) {
        self.selectedValue = foundChoice.value;
    }
    
    [self updateSegments];
}
-(NSArray<MCChoice*>*)choices {
    return _choices;
}

@synthesize label = _label;
-(void)setLabel:(NSString*)label {
    if (![_label isEqualToString:label]) {
        _label = label;
    }
    if (_labelView && ![_labelView.text isEqualToString:label]) {
        _labelView.text = label;
    }
}
-(NSString*)label {
    return _label;
}

@synthesize selectedValue = _selectedValue;
-(void)setSelectedValue:(NSObject*)value {
    NSObject *newValue = nil;
    MCChoice *foundChoice = [self findChoiceWithValue:value inArray:_choices];
    if (!_choices) {
        newValue = value;
    } else if (!foundChoice) {
        newValue = nil;
    } else {
        newValue = foundChoice.value;
    }
    _selectedChoice = foundChoice;
    if (_selectedValue != newValue) {
        _selectedValue = newValue;
        [self notifyChangedValue];
    }
}
-(NSObject*)selectedValue {
    return _selectedValue;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)selectedSegmentChanged:(id)sender {
    NSInteger selectedIndex = _segmentedControl.selectedSegmentIndex;
    MCChoice *newChoice = [_choices objectAtIndex:selectedIndex];
    _selectedChoice = newChoice;
    _selectedValue = _selectedChoice.value;
    [self notifyChangedValue];
}

@end
