//
//  SavingThrowTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "SavingThrow.h"

@interface SavingThrowTests : XCTestCase

@end

@implementation SavingThrowTests {
    SavingThrow *_savingThrow;
    NSString *_name;
    int _order;
}

- (void)setUp {
    _savingThrow = [[SavingThrow alloc] init];
    _name = @"str";
    _order = 9;
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertNotNil(_savingThrow);
    XCTAssertEqualObjects(@"", _savingThrow.name);
    XCTAssertEqual(-1, _savingThrow.order);
}

- (void)testInitWithNameAndOrder {
    _savingThrow = [[SavingThrow alloc] initWithName:_name andOrder:_order];
    XCTAssertNotNil(_savingThrow);
    XCTAssertEqualObjects(_name, _savingThrow.name);
    XCTAssertEqual(_order, _savingThrow.order);
}

- (void)testNameGetterAndSetter {
    _savingThrow.name = _name;
    XCTAssertEqualObjects(_name, _savingThrow.name);
}

- (void)testOrderGetterAndSetter {
    _savingThrow.order = _order;
    XCTAssertEqual(_order, _savingThrow.order);
}

@end
