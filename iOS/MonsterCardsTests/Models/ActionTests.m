//
//  ActionTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "Action.h"

@interface ActionTests : XCTestCase

@end

@implementation ActionTests {
    Action *_action;
    NSString *_name;
    NSString *_description;
}

- (void)setUp {
    _action = [[Action alloc] init];
    _name = @"My Action Name";
    _description = @"This is my action description.";
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertEqualObjects(@"", _action.name);
    XCTAssertEqualObjects(@"", _action.actionDescription);
}

- (void)testNameGetterAndSetter {
    _action.name = _name;
    XCTAssertEqualObjects(_name, _action.name);
}

- (void)testDescriptionGetterAndSetter {
    _action.actionDescription = _description;
    XCTAssertEqualObjects(_description, _action.actionDescription);
}

- (void)testInitWithNameAndDescription {
    _action = [[Action alloc] initWithName:_name andDescription:_description];
    
    XCTAssertEqualObjects(_name, _action.name);
    XCTAssertEqualObjects(_description, _action.actionDescription);
}

@end
