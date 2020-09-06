//
//  Skill.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "Skill.h"

@implementation Skill

-(id)initWithName: (NSString*)name abilityScoreName:(NSString*)abilityScoreName andNotes:(NSString*)notes{
    self = [super init];
    
    self.name = name;
    self.abilityScoreName = abilityScoreName;
    self.notes = notes;
    
    return self;
}

-(int)skillBonusForMonster: (Monster*)monster {
    int bonus = [monster abilityModifierForAbilityScoreName: self.abilityScoreName];
    if ([@" (ex)" isEqualToString:self.notes]) {
        bonus += 2 * monster.proficiencyBonus;
    } else {
        bonus += monster.proficiencyBonus;
    }
    return bonus;
}

-(NSString*)textForMonster: (Monster*)monster {
    int bonus = [self skillBonusForMonster:monster];
    
    return [NSString stringWithFormat:@"%@%@ %d", [self.name substringToIndex:1], [self.name substringFromIndex:1], bonus];
}

@end
