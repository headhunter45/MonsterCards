//
//  MCSelectFieldTableViewCell.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/26/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCSelectFieldTableViewCell.h"

@implementation MCSelectFieldTableViewCell {
    MCChoice* _selectedChoice;
}

@synthesize choices = _choices;
-(void)setChoices:(NSArray<MCChoice*>*)choices {
    // TODO: only do this if choices is different
    // TODO: update selectedValue and selectedIndex
    _choices = choices;
    
    if (![_choices isEqualToArray:choices]) {
        self.selectedValue = _selectedValue;
    } else if (choices) {
        self.selectedValue = [choices firstObject].value;
    }
}
-(NSArray<MCChoice*>*)choices {
    return _choices;
}

@synthesize identifier = _identifier;
-(void)setIdentifier:(NSString*)identifier {
    if (![_identifier isEqualToString:identifier]) {
        _identifier = identifier;
    }
}
-(NSString*)identifier {
    return _identifier;
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

-(MCChoice*)findChoiceWithValue:(NSObject*)value
                        inArray:(NSArray*)array {
    NSPredicate *predicate = [NSPredicate predicateWithBlock:^BOOL(id element, NSDictionary *bindings) {
        if (![element isKindOfClass:[MCChoice class]]) {
            return NO;
        }
        MCChoice *choice = (MCChoice*)element;
        return [choice.value isEqual:value];
    }];
    NSArray<MCChoice*> *matchingChoices = [_choices filteredArrayUsingPredicate:predicate];
    MCChoice *foundChoice = matchingChoices.count > 0 ? matchingChoices.firstObject : nil;
    return foundChoice;
}

@synthesize selectedValue = _selectedValue;
-(void)setSelectedValue:(NSObject*)value {
    if (!_choices) {
        // choices hasn't been initialized yet so just set our selected value until choices is set
        _selectedValue = value;
    }
    MCChoice *foundChoice = [self findChoiceWithValue:value
                                              inArray:_choices];
    if (![_selectedChoice isEqual:foundChoice] || ![_selectedValue isEqual:foundChoice.value]) {
        _selectedChoice = foundChoice;
        _selectedValue = foundChoice.value;
        if (_delegate) {
            [_delegate editableValueDidChange:_selectedValue
                                forIdentifier:_identifier
                                      andType:kMCFieldValueTypeChoice];
        }
    }
    self.textField.text = _selectedChoice != nil
        ? _selectedChoice.label != nil
            ? _selectedChoice.label
            : @""
        : @"";
}
-(NSObject*)selectedValue {
    return _selectedValue;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    UIPickerView *childPicker = [[UIPickerView alloc] init];
    childPicker.delegate = self;
    childPicker.dataSource = self;
    self.textField.inputView = childPicker;
    
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    [toolbar sizeToFit];
    
    UIBarButtonItem *button =
    [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"Done", @"Button label")
                                     style:UIBarButtonItemStylePlain
                                    target:self
                                    action:@selector(choiceSelected)];
    [toolbar setItems:@[button]
             animated:true];
    [toolbar setUserInteractionEnabled:YES];
    self.textField.inputAccessoryView = toolbar;
    self.textField.hidden = NO;
    self.textField.text = _selectedChoice.label;
    self.textField.delegate = self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)choiceSelected {
    [self endEditing:true];
}

#pragma mark - UIPickerViewDataSource

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component {
    return [_choices count];
}

#pragma mark - UIPickerViewDelegate

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component {
    return [_choices objectAtIndex:row].label;
}

- (void)pickerView:(UIPickerView *)pickerView
      didSelectRow:(NSInteger)row
       inComponent:(NSInteger)component {
   
    _selectedChoice = [_choices objectAtIndex:row];
    self.textField.text = _selectedChoice.label;
    self.selectedValue = _selectedChoice.value;
}

@end
