//
//  DamageType.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "DamageType.h"

@implementation DamageType

-(id)init {
    self = [super init];
    
    self.name = @"";
    self.note = @"";
    self.type = @"";
    
    return self;
}

-(id)initWithName: (NSString*)name note: (NSString*)note andType: (NSString*)type{
    self = [super init];
    
    self.name = name;
    self.note = note;
    self.type = type;
    
    return self;
}

@end
