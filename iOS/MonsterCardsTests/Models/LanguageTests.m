//
//  Language.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "Language.h"

@interface LanguageTests : XCTestCase

@end

@implementation LanguageTests {
    Language *_language;
    NSString *_name;
    BOOL _canSpeak;
}

- (void)setUp {
    _language = [[Language alloc] init];
    _name = @"English";
    _canSpeak = YES;
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertNotNil(_language);
    XCTAssertEqualObjects(@"", _language.name);
    XCTAssertEqual(YES, _language.speaks);
}

- (void)testInitWithNameAndSpeaks {
    _language = [[Language alloc] initWithName:_name andSpeaks:_canSpeak];

    XCTAssertNotNil(_language);
    XCTAssertEqualObjects(_name, _language.name);
    XCTAssertEqual(_canSpeak, _language.speaks);
}

- (void)testNameGetterAndSetter {
    _language.name = _name;
    XCTAssertEqualObjects(_name, _language.name);
}

- (void)testSpeaksGetterAndSetter {
    _language.speaks = NO;
    XCTAssertEqual(NO, _language.speaks);
}

@end
