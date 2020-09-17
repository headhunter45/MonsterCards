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
+(int)readIntFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key;
+(int)readIntFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(int)defaultValue;
+(BOOL)readBoolFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key;
+(BOOL)readBoolFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(BOOL)defaultValue;
+(NSDictionary*)readDictionaryFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key;
+(NSDictionary*)readDictionaryFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(NSDictionary* _Nullable)defaultValue;

+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSString* _Nullable)defaultValue;
+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSNumber* _Nullable)defaultValue;
+(int)readIntFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(int)readIntFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(int)defaultValue;
+(BOOL)readBoolFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(BOOL)readBoolFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(BOOL)defaultValue;
+(NSDictionary*)readDictionaryFromArray:(NSArray*)array forIndex:(NSUInteger)index;
+(NSDictionary*)readDictionaryFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSDictionary* _Nullable)defaultValue;

@end

NS_ASSUME_NONNULL_END
