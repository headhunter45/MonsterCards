//
//  SavingThrow.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "SavingThrow.h"

@implementation SavingThrow

-(id)init {
    self = [super init];
    
    self.name = @"";
    self.order = -1;
    
    return self;
}

-(id)initWithName: (NSString*)name andOrder: (int)order {
    self = [super init];
    
    self.name = name;
    self.order = order;
    
    return self;
}

@end
