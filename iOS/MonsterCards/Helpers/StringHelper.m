//
//  StringHelper.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "StringHelper.h"

@implementation StringHelper

+(BOOL)isStringNilOrEmpty:(NSString*)theString {
    if (nil == theString) {
        return YES;
    }
    
    if (theString.length < 1) {
        return YES;
    }
    
    return NO;
}

@end
