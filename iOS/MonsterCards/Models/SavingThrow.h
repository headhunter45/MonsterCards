//
//  SavingThrow.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface SavingThrow : NSObject

@property NSString* name;
@property int order;

-(id)initWithName: (NSString*)name andOrder: (int)order;

@end

NS_ASSUME_NONNULL_END
