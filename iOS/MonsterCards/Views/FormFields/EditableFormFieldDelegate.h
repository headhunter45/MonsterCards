//
//  EditableFormFieldDelegate.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#ifndef EditableFormFieldDelegate_h
#define EditableFormFieldDelegate_h

@protocol EditableFormFieldDelegate <NSObject>

@optional
-(void)editableValueDidChange:(NSObject*)value forIdentifier:(NSString*)identifier andType:(NSString*)type;

@end


#endif /* EditableFormFieldDelegate_h */
