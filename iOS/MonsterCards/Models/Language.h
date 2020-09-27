//
//  Language.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface Language : NSObject

@property NSString* name;
@property BOOL speaks;

-(id)initWithName:(NSString*)name
        andSpeaks:(BOOL)canSpeak;

@end

NS_ASSUME_NONNULL_END
