//
//  JSONHelper.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/15/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface JSONHelper : NSObject

+(NSString*)readStringFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key;
+(NSString*)readStringFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(NSString* _Nullable)defaultValue;
+(NSNumber*)readNumberFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key;
+(NSNumber*)readNumberFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(NSNumber* _Nullable)defaultValue;
+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSString* _Nullable)defaultValue;
+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSNumber* _Nullable)defaultValue;

@end

NS_ASSUME_NONNULL_END
