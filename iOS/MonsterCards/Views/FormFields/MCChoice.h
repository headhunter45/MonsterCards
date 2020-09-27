//
//  MCChoice.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/26/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MCChoice : NSObject

@property NSString* label;
@property NSObject* value;

+(id)choiceWithLabel:(NSString*)label
            andValue:(NSObject*)value;

-(id)initWithLabel:(NSString*)label
          andValue:(NSObject*)value;



@end

NS_ASSUME_NONNULL_END
