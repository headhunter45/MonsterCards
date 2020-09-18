//
//  MCFormFieldDelegate.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/9/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#ifndef MCFormFieldDelegate_h
#define MCFormFieldDelegate_h

#import "MCFormFieldConstants.h"

@protocol MCFormFieldDelegate <NSObject>

@optional
-(void)editableValueDidChange:(NSObject*)value forIdentifier:(NSString*)identifier andType:(NSString*)type;

@end


#endif /* MCFormFieldDelegate_h */
