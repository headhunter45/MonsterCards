//
//  DamageTypeTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "DamageType.h"

@interface DamageTypeTests : XCTestCase {
    DamageType *_damageType;
    NSString *_name;
    NSString *_note;
    NSString *_type;
}

@end

@implementation DamageTypeTests

- (void)setUp {
    _damageType = [[DamageType alloc] init];
    _name = @"My Damage Type";
    _note = @"A note";
    _type = @"A type";
}

- (void)tearDown {}

- (void)testDefaultInitializer {
    XCTAssertEqualObjects(@"", _damageType.name);
    XCTAssertEqualObjects(@"", _damageType.note);
    XCTAssertEqualObjects(@"", _damageType.type);
}

- (void)testInitWithNameNoteAndType {
    _damageType = [[DamageType alloc] initWithName:_name note:_note andType:_type];
    
    XCTAssertEqualObjects(_name, _damageType.name);
    XCTAssertEqualObjects(_note, _damageType.note);
    XCTAssertEqualObjects(_type, _damageType.type);
}

- (void)testNameGetterAndSetter {
    _damageType.name = _name;
    XCTAssertEqualObjects(_name, _damageType.name);
}

- (void)testNoteGetterAndSetter {
    _damageType.note = _note;
    XCTAssertEqualObjects(_note, _damageType.note);
}

- (void)testTypeGetterAndSetter {
    _damageType.type = _type;
    XCTAssertEqualObjects(_type, _damageType.type);
}

@end
