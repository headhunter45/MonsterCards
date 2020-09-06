//
//  DamageType.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface DamageType : NSObject

@property NSString* name;
@property NSString* note;
@property NSString* type;

-(id)initWithName: (NSString*)name note: (NSString*)note andType: (NSString*)type;

@end

NS_ASSUME_NONNULL_END
