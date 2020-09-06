//
//  AbilityTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "Ability.h"

@interface AbilityTests : XCTestCase

@end

@implementation AbilityTests {
    Ability *_ability;
    NSString *_name;
    NSString *_description;
}

- (void)setUp {
    _ability = [[Ability alloc] init];
    _name = @"My Ability Name";
    _description = @"This is my ability description.";
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertEqualObjects(@"", _ability.name);
    XCTAssertEqualObjects(@"", _ability.abilityDescription);
}

- (void)testNameGetterAndSetter {
    _ability.name = _name;
    XCTAssertEqualObjects(_name, _ability.name);
}

- (void)testDescriptionGetterAndSetter {
    _ability.abilityDescription = _description;
    XCTAssertEqualObjects(_description, _ability.abilityDescription);
}

- (void)testInitWithNameAndDescription {
    _ability = [[Ability alloc] initWithName:_name andDescription:_description];
    
    XCTAssertEqualObjects(_name, _ability.name);
    XCTAssertEqualObjects(_description, _ability.abilityDescription);
}

@end
