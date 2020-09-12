//
//  MonsterTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
@import OCHamcrest;
@import OCMockito;
#import "Monster.h"

@interface MonsterTests : XCTestCase

@end

@implementation MonsterTests {
    Monster *_monster;
    NSManagedObjectContext *_context;
    NSString *_jsonString;
    NSData *_jsonData;
}

- (void)setUp {
    _context = nil;
    _monster = [[Monster alloc] initWithContext:_context];
    _jsonString = @"{\"name\":\"Acolyte\",\"size\":\"large\"}";
    _jsonData = [_jsonString dataUsingEncoding:NSUTF8StringEncoding];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"", _monster.name);
    XCTAssertEqualObjects(@"", _monster.size);
}

- (void)testInitWithJSONString {
    _monster = [[Monster alloc] initWithJSONString:_jsonString andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"Acolyte", _monster.name);
    XCTAssertEqualObjects(@"large", _monster.size);
}

- (void)testInitWithEmptyJSONString {
    _monster = [[Monster alloc] initWithJSONString:@"{}" andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"", _monster.name);
    XCTAssertEqualObjects(@"", _monster.size);
}
- (void)testInitWithJSONData {
    _monster = [[Monster alloc] initWithJSONData:_jsonData andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"Acolyte", _monster.name);
    XCTAssertEqualObjects(@"large", _monster.size);
}

- (void)testNameGetterAndSetter {
    NSString *name = @"Pixie";
    _monster.name = name;
    XCTAssertEqualObjects(name, _monster.name);
}

- (void)testSizeGetterAndSetter {
    NSString *size = @"huge";
    _monster.size = size;
    XCTAssertEqualObjects(size, _monster.size);
}

@end
