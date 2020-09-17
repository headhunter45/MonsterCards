//
//  JSONHelperTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/15/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "JSONHelper.h"

@interface JSONHelperTests : XCTestCase

@end

@implementation JSONHelperTests {
    NSString *_jsonStringKey;
    NSString *_jsonStringValue;
    NSString *_jsonStringFragment;
    NSString *_jsonIntegerKey;
    NSNumber *_jsonIntegerValue;
    NSString *_jsonIntegerFragment;
}

NSString* escapeStringForJSON(NSString *unescaped) {
    return [[unescaped stringByReplacingOccurrencesOfString:@"\\" withString:@"\\\\"] stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""];
}

NSDictionary* readJSONDictionaryFromString(NSString *jsonString) {
    NSData* jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *jsonRoot = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    if (![jsonRoot isKindOfClass:[NSDictionary class]]) {
        return nil;
    } else {
        return jsonRoot;
    }
}

NSArray* readJSONArrayFromString(NSString *jsonString) {
    NSData* jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSArray *jsonRoot = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    if (![jsonRoot isKindOfClass:[NSArray class]]) {
        return nil;
    } else {
        return jsonRoot;
    }
}

- (void)setUp {
    _jsonStringKey = @"my_string";
    _jsonStringValue = @"Hello, World!";
    _jsonStringFragment = [NSString stringWithFormat:@"\"%@\":\"%@\"", escapeStringForJSON(_jsonStringKey), escapeStringForJSON(_jsonStringValue)];
    _jsonIntegerKey = @"my_int";
    _jsonIntegerValue = @12345;
    _jsonIntegerFragment = [NSString stringWithFormat:@"\"%@\":%@", escapeStringForJSON(_jsonIntegerKey), [_jsonIntegerValue stringValue]];
}

- (void)tearDown {
}

#pragma mark - Strings in Dictionaries

- (void)testReadStringFromDictionaryReturnsNilIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromDictionary:jsonRoot  forKey:_jsonStringKey];
    XCTAssertNil(readString);
}

- (void)testReadStringFromDictionaryWithDefaultReturnsDefaultIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromDictionary:jsonRoot  forKey:_jsonStringKey withDefaultValue:_jsonStringValue];
    XCTAssertEqualObjects(_jsonStringValue, readString);
}

- (void) testReadStringFromDictionaryReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromDictionary:jsonRoot  forKey:_jsonStringKey];
    XCTAssertEqualObjects(_jsonStringValue, readString);
}

- (void)testReadStringFromDictionaryWithDefaultReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromDictionary:jsonRoot  forKey:_jsonStringKey withDefaultValue:@"Some other string"];
    XCTAssertEqualObjects(_jsonStringValue, readString);
}

- (void) testReadStringFromDictionaryReturnsNilIfWrongType {
    NSString *jsonString = [NSString stringWithFormat:@"{\"%@\":%@}", _jsonStringKey, _jsonIntegerValue];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromDictionary:jsonRoot  forKey:_jsonStringKey];
    XCTAssertNil(readString);
}

#pragma mark - Strings in Arrays

- (void)testReadStringFromArrayReturnsNilIfNotAString {
    NSString *jsonString = [NSString stringWithFormat:@"[%@]", _jsonIntegerValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromArray:jsonRoot forIndex:0];
    XCTAssertNil(readString);
}

- (void)testReadStringFromArrayWithDefaultReturnsDefaultValueIfNotAString {
    NSString *jsonString = [NSString stringWithFormat:@"[%@]", _jsonIntegerValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromArray:jsonRoot forIndex:0 withDefaultValue:_jsonStringValue];
    XCTAssertEqualObjects(_jsonStringValue, readString);
}

- (void)testReadStringFromArrayThrowsIfIndexOutOfRange {
    // TODO: Decide if this should throw or return nil
    NSString *jsonString = @"[]";
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    XCTAssertThrows([JSONHelper readStringFromArray:jsonRoot forIndex:0]);
    XCTAssertThrows([JSONHelper readStringFromArray:jsonRoot forIndex:-1]);
}

- (void)testReadStringFromArrayReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"[\"%@\"]", _jsonStringValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSString *readString = [JSONHelper readStringFromArray:jsonRoot forIndex:0];
    XCTAssertEqualObjects(_jsonStringValue, readString);
}

#pragma mark - Integers in Dictionaries

- (void)testReadIntegerFromDictionaryReturnsNilIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromDictionary:jsonRoot forKey:_jsonIntegerKey];
    XCTAssertNil(readNumber);
}

- (void)testReadIntegerFromDictionaryWithDefaultReturnsDefaultIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromDictionary:jsonRoot  forKey:_jsonIntegerKey withDefaultValue:_jsonIntegerValue];
    XCTAssertEqualObjects(_jsonIntegerValue, readNumber);
}

- (void) testReadIntegerFromDictionaryReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromDictionary:jsonRoot  forKey:_jsonIntegerKey];
    XCTAssertEqualObjects(_jsonIntegerValue, readNumber);
}

- (void)testReadIntegerFromDictionaryWithDefaultReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromDictionary:jsonRoot  forKey:_jsonIntegerKey withDefaultValue:@67890];
    XCTAssertEqualObjects(_jsonIntegerValue, readNumber);
}

- (void) testReadIntegerFromDictionaryReturnsNilIfWrongType {
    NSString *jsonString = [NSString stringWithFormat:@"{\"%@\":\"%@\"}", _jsonIntegerKey, _jsonStringValue];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromDictionary:jsonRoot  forKey:_jsonIntegerKey];
    XCTAssertNil(readNumber);
}

- (void)testReadIntFromDictionaryReturnsZeroIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromDictionary:jsonRoot forKey:_jsonIntegerKey];
    XCTAssertEqual(0, readNumber);
}

- (void)testReadIntFromDictionaryWithDefaultReturnsDefaultIfKeyNotPresent {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonStringFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromDictionary:jsonRoot  forKey:_jsonIntegerKey withDefaultValue:[_jsonIntegerValue intValue]];
    XCTAssertEqual([_jsonIntegerValue intValue], readNumber);
}

- (void) testReadIntFromDictionaryReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromDictionary:jsonRoot  forKey:_jsonIntegerKey];
    XCTAssertEqual([_jsonIntegerValue intValue], readNumber);
}

- (void)testReadIntFromDictionaryWithDefaultReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"{%@}", _jsonIntegerFragment];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromDictionary:jsonRoot  forKey:_jsonIntegerKey withDefaultValue:67890];
    XCTAssertEqual([_jsonIntegerValue intValue], readNumber);
}

- (void) testReadIntFromDictionaryReturnsZeroIfWrongType {
    NSString *jsonString = [NSString stringWithFormat:@"{\"%@\":\"%@\"}", _jsonIntegerKey, _jsonStringValue];
    NSDictionary *jsonRoot = readJSONDictionaryFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromDictionary:jsonRoot  forKey:_jsonIntegerKey];
    XCTAssertEqual(0, readNumber);
}

#pragma mark - Integers in Arrays

- (void)testReadIntegerFromArrayReturnsNilIfNotAnInteger {
    NSString *jsonString = [NSString stringWithFormat:@"[\"%@\"]", _jsonStringValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromArray:jsonRoot forIndex:0];
    XCTAssertNil(readNumber);
}

- (void)testReadIntegerFromArrayWithDefaultReturnsDefaultValueIfNotAnInteger {
    NSString *jsonString = [NSString stringWithFormat:@"[\"%@\"]", _jsonStringValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromArray:jsonRoot forIndex:0 withDefaultValue:_jsonIntegerValue];
    XCTAssertEqualObjects(_jsonIntegerValue, readNumber);
}

- (void)testReadIntegerFromArrayThrowsIfIndexOutOfRange {
    // TODO: Decide if this should throw or return nil
    NSString *jsonString = @"[]";
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    XCTAssertThrows([JSONHelper readNumberFromArray:jsonRoot forIndex:0]);
    XCTAssertThrows([JSONHelper readNumberFromArray:jsonRoot forIndex:-1]);
}

- (void)testReadIntegerFromArrayReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"[%@]", _jsonIntegerValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    NSNumber *readNumber = [JSONHelper readNumberFromArray:jsonRoot forIndex:0];
    XCTAssertEqualObjects(_jsonIntegerValue, readNumber);
}

- (void)testReadIntFromArrayReturnsNilIfNotAnInteger {
    NSString *jsonString = [NSString stringWithFormat:@"[\"%@\"]", _jsonStringValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromArray:jsonRoot forIndex:0];
    XCTAssertEqual(0, readNumber);
}

- (void)testReadIntFromArrayWithDefaultReturnsDefaultValueIfNotAnInteger {
    NSString *jsonString = [NSString stringWithFormat:@"[\"%@\"]", _jsonStringValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromArray:jsonRoot forIndex:0 withDefaultValue:[_jsonIntegerValue intValue]];
    XCTAssertEqual([_jsonIntegerValue intValue], readNumber);
}

- (void)testReadIntFromArrayThrowsIfIndexOutOfRange {
    // TODO: Decide if this should throw or return 0
    NSString *jsonString = @"[]";
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    XCTAssertThrows([JSONHelper readIntFromArray:jsonRoot forIndex:0]);
    XCTAssertThrows([JSONHelper readIntFromArray:jsonRoot forIndex:-1]);
}

- (void)testReadIntFromArrayReturnsCorrectValue {
    NSString *jsonString = [NSString stringWithFormat:@"[%@]", _jsonIntegerValue];
    NSArray *jsonRoot = readJSONArrayFromString(jsonString);
    XCTAssertNotNil(jsonRoot);
    
    int readNumber = [JSONHelper readIntFromArray:jsonRoot forIndex:0];
    XCTAssertEqual([_jsonIntegerValue intValue], readNumber);
}

@end
