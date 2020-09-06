//
//  Ability.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface Ability : NSObject

@property NSString* name;
@property NSString* abilityDescription;

-(id)initWithName: (NSString*)name andDescription: (NSString*)description;

@end

NS_ASSUME_NONNULL_END
