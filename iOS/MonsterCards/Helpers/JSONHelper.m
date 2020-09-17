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

int coerceObjectToInt(NSObject *object, int defaultValue) {
    if ([object isKindOfClass:[NSNumber class]]) {
        return [(NSNumber*)object intValue];
    }

    if ([object isKindOfClass:[NSString class]]) {
        NSScanner *scanner;
        int temp;
        scanner = [NSScanner scannerWithString:(NSString*)object];
        if ([scanner scanInt:&temp]) {
            return temp;
        }
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

+(int)readIntFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key {
    return [JSONHelper readIntFromDictionary:dictionary forKey:key withDefaultValue:0];
}

+(int)readIntFromDictionary:(NSDictionary*)dictionary forKey:(NSString*)key withDefaultValue:(int)defaultValue {
    NSObject *object = [dictionary objectForKey:key];
    return coerceObjectToInt(object, defaultValue);
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

+(int)readIntFromArray:(NSArray*)array forIndex:(NSUInteger)index {
    return [JSONHelper readIntFromArray:array forIndex:index withDefaultValue:0];
}

+(int)readIntFromArray:(NSArray*)array forIndex:(NSUInteger)index withDefaultValue:(int)defaultValue {
    NSObject *object = [array objectAtIndex:index];
    return coerceObjectToInt(object, defaultValue);
}

@end
