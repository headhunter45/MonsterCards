//
//  JSONHelper.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/15/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "JSONHelper.h"

@implementation JSONHelper

NSString* coerceObjectToString(NSObject *object, NSString *defaultValue) {
    if ([object isKindOfClass:[NSString class]]) {
        return (NSString*)object;
    }

    return defaultValue;
}

NSNumber* coerceObjectToNumber(NSObject *object, NSNumber *defaultValue) {
    if ([object isKindOfClass:[NSNumber class]]) {
        return (NSNumber*)object;
    }
    
    return defaultValue;
}

+(NSString*)readStringFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key {
    return [JSONHelper readStringFromDictionary:dictionary forKey:key withDefaultValue:nil];
}

+(NSString*)readStringFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(NSString* _Nullable)defaultValue {
    NSObject *object = [dictionary objectForKey:key];
    return coerceObjectToString(object, defaultValue);
}

+(NSNumber*)readNumberFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key {
    return [JSONHelper readNumberFromDictionary:dictionary forKey:key withDefaultValue:nil];
}

+(NSNumber*)readNumberFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(NSNumber* _Nullable)defaultValue {
    NSObject *object = [dictionary objectForKey:key];
    return coerceObjectToNumber(object, defaultValue);
}

+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index{
    return [JSONHelper readStringFromArray:array forIndex:index withDefaultValue:nil];
}

+(NSString*)readStringFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSString* _Nullable)defaultValue {
    NSObject *object = [array objectAtIndex:index];
    return coerceObjectToString(object, defaultValue);
}

+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index {
    return [JSONHelper readNumberFromArray:array forIndex:index withDefaultValue:nil];
}

+(NSNumber*)readNumberFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(NSNumber* _Nullable)defaultValue {
    NSObject *object = [array objectAtIndex:index];
    return coerceObjectToNumber(object, defaultValue);
}

@end
