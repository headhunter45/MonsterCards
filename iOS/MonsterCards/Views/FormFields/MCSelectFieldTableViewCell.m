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
    [self updateView];
    
    if (_delegate) {
        [_delegate editableValueDidChange:_selectedValue
                            forIdentifier:_identifier
                                  andType:kMCFieldValueTypeChoice];
    }
}

-(void)updateView {
    self.textField.text = _selectedChoice.label;
    if (_choices && _choices.count > 0) {
        NSInteger selectedRow = [_choices indexOfObject:_selectedChoice];
        if (selectedRow != NSNotFound) {
            [self.pickerView selectRow:selectedRow inComponent:0 animated:YES];
        } else {
            [self.pickerView selectRow:0 inComponent:0 animated:YES];
        }
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
    
    [self updateView];
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
        foundChoice = [_choices firstObject];
        newValue = foundChoice.value;
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
    self.pickerView = [[UIPickerView alloc] init];
    self.pickerView.delegate = self;
    self.pickerView.dataSource = self;
    self.textField.inputView = self.pickerView;
    self.pickerView.translatesAutoresizingMaskIntoConstraints = NO;
    
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

#pragma mark - UITextFieldDelegate

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    [self updateView];
}

@end
