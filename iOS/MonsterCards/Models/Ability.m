//
//  Ability.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Ability.h"

@implementation Ability

-(id)init {
    self = [super init];
    
    self.name = @"";
    self.abilityDescription = @"";
    
    return self;
}

-(id)initWithName: (NSString*)name andDescription: (NSString*)description {
    self = [super init];
    
    self.name = name;
    self.abilityDescription = description;
    
    return self;
}

@end
