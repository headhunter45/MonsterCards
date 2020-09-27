//
//  MCChoice.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/26/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MCChoice.h"

@implementation MCChoice

+(id)choiceWithLabel:(NSString*)label
            andValue:(NSObject*)value {
    return [[MCChoice alloc] initWithLabel:label
                                  andValue:value];
}

-(id)initWithLabel:(NSString*)label
          andValue:(NSObject*)value {
    self = [super init];
    
    self.label = label;
    self.value = value;
    
    return self;
}

@end
