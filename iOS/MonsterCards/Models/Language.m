//
//  Language.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Language.h"

@implementation Language

-(id)init {
    self = [super init];
    
    self.name = @"";
    self.speaks = YES;
    
    return self;
}

-(id)initWithName: (NSString*)name andSpeaks: (BOOL)canSpeak {
    self = [super init];
    
    self.name = name;
    self.speaks = canSpeak;
    
    return self;
}

@end
