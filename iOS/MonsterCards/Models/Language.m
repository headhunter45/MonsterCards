//
//  Language.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Language.h"

@implementation Language

-(id)initWithName: (NSString*)name andSpeaks: (NSString*)canSpeak {
    self = [super init];
    
    self.name = name;
    self.speaks = canSpeak;
    
    return self;
}

@end
