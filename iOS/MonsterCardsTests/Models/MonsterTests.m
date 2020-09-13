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
    _jsonString = @"{\"name\":\"Acolyte\",\"size\":\"medium\",\"type\":\"humanoid\",\"tag\":\"any race\",\"alignment\":\"any alignment\",\"strPoints\":8}";
    _jsonData = [_jsonString dataUsingEncoding:NSUTF8StringEncoding];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"", _monster.name);
    XCTAssertEqualObjects(@"", _monster.size);
    XCTAssertEqualObjects(@"", _monster.type);
    XCTAssertEqualObjects(@"", _monster.subtype);
    XCTAssertEqualObjects(@"", _monster.alignment);
    XCTAssertEqual(0, _monster.strengthScore);
}

- (void)testInitWithJSONString {
    _monster = [[Monster alloc] initWithJSONString:_jsonString andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"Acolyte", _monster.name);
    XCTAssertEqualObjects(@"medium", _monster.size);
    XCTAssertEqualObjects(@"humanoid", _monster.type);
    XCTAssertEqualObjects(@"any race", _monster.subtype);
    XCTAssertEqualObjects(@"any alignment", _monster.alignment);
    XCTAssertEqual(8, _monster.strengthScore);
}

- (void)testInitWithEmptyJSONString {
    _monster = [[Monster alloc] initWithJSONString:@"{}" andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"", _monster.name);
    XCTAssertEqualObjects(@"", _monster.size);
    XCTAssertEqualObjects(@"", _monster.type);
    XCTAssertEqualObjects(@"", _monster.subtype);
    XCTAssertEqualObjects(@"", _monster.alignment);
    XCTAssertEqual(0, _monster.strengthScore);
}
- (void)testInitWithJSONData {
    _monster = [[Monster alloc] initWithJSONData:_jsonData andContext:_context];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"Acolyte", _monster.name);
    XCTAssertEqualObjects(@"medium", _monster.size);
    XCTAssertEqualObjects(@"humanoid", _monster.type);
    XCTAssertEqualObjects(@"any race", _monster.subtype);
    XCTAssertEqualObjects(@"any alignment", _monster.alignment);
    XCTAssertEqual(8, _monster.strengthScore);
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

- (void)testTypeGetterAndSetter {
    NSString *type = @"fey";
    _monster.type = type;
    XCTAssertEqualObjects(type, _monster.type);
}

- (void)testSubtypeGetterAndSetter {
    NSString *subtype = @"elf";
    _monster.subtype = subtype;
    XCTAssertEqualObjects(subtype, _monster.subtype);
}

- (void)testAlignmentGetterAndSetter {
    NSString *alignment = @"chaotic good";
    _monster.alignment = alignment;
    XCTAssertEqualObjects(alignment, _monster.alignment);
}
- (void)testCopyFromMonster {
    Monster *otherMonster = [[Monster alloc] initWithJSONString:_jsonString andContext:_context];
    [_monster copyFromMonster:otherMonster];
    
    XCTAssertNotNil(_monster);
    XCTAssertEqualObjects(@"Acolyte", _monster.name);
    XCTAssertEqualObjects(@"medium", _monster.size);
    XCTAssertEqualObjects(@"humanoid", _monster.type);
    XCTAssertEqualObjects(@"any race", _monster.subtype);
    XCTAssertEqualObjects(@"any alignment", _monster.alignment);
    XCTAssertEqual(8, _monster.strengthScore);
}

- (void)testMetaWithNoFieldsSet {
    
    XCTAssertEqualObjects(@"", _monster.meta);
}

- (void)testMetaWithSize {
    _monster.size = @"large";
    XCTAssertEqualObjects(@"large", _monster.meta);
}

- (void)testMetaWithType {
    _monster.type = @"humanoid";
    XCTAssertEqualObjects(@"humanoid", _monster.meta);
}

- (void)testMetaWithSizeAndType {
    _monster.size = @"large";
    _monster.type = @"humanoid";
    XCTAssertEqualObjects(@"large humanoid", _monster.meta);
}

- (void)testMetaWithSubtype {
    _monster.subtype = @"elf";
    XCTAssertEqualObjects(@"(elf)", _monster.meta);
}

- (void)testMetaWithSizeAndSubtype {
    _monster.size = @"large";
    _monster.subtype = @"elf";
    XCTAssertEqualObjects(@"large (elf)", _monster.meta);
}

- (void)testMetaWithTypeAndSubtype {
    _monster.type = @"humanoid";
    _monster.subtype = @"elf";
    XCTAssertEqualObjects(@"humanoid (elf)", _monster.meta);
}

- (void)testMetaWithSizeTypeAndSubtype {
    _monster.size = @"large";
    _monster.type = @"humanoid";
    _monster.subtype = @"elf";
    XCTAssertEqualObjects(@"large humanoid (elf)", _monster.meta);
}

- (void)testMetaWithAlignment {
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"chaotic good", _monster.meta);
}

- (void)testMetaWithSizeAndAlignment {
    _monster.size = @"large";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"large chaotic good", _monster.meta);
}

- (void)testMetaWithTypeAndAlignment {
    _monster.type = @"humanoid";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"humanoid chaotic good", _monster.meta);
}

- (void)testMetaWithSizeTypeAndAlignment {
    _monster.size = @"large";
    _monster.type = @"humanoid";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"large humanoid chaotic good", _monster.meta);
}

- (void)testMetaWithSizeSubtypeAndAlignment {
    _monster.size = @"large";
    _monster.subtype = @"elf";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"large (elf) chaotic good", _monster.meta);
}

- (void)testMetaWithTypeSubtypeAndAlignment {
    _monster.type = @"humanoid";
    _monster.subtype = @"elf";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"humanoid (elf) chaotic good", _monster.meta);
}

- (void)testMetaWithSizeTypeSubtypeAndAlignment {
    _monster.size = @"large";
    _monster.type = @"humanoid";
    _monster.subtype = @"elf";
    _monster.alignment = @"chaotic good";
    XCTAssertEqualObjects(@"large humanoid (elf) chaotic good", _monster.meta);
}

- (void)testAbilityModifierForScore {
    XCTAssertEqual(-6, [Monster abilityModifierForScore:-1]);
    XCTAssertEqual(-5, [Monster abilityModifierForScore:0]);
    XCTAssertEqual(-5, [Monster abilityModifierForScore:1]);
    XCTAssertEqual(-4, [Monster abilityModifierForScore:2]);
    XCTAssertEqual(-4, [Monster abilityModifierForScore:3]);
    XCTAssertEqual(-3, [Monster abilityModifierForScore:4]);
    XCTAssertEqual(-3, [Monster abilityModifierForScore:5]);
    XCTAssertEqual(-2, [Monster abilityModifierForScore:6]);
    XCTAssertEqual(-2, [Monster abilityModifierForScore:7]);
    XCTAssertEqual(-1, [Monster abilityModifierForScore:8]);
    XCTAssertEqual(-1, [Monster abilityModifierForScore:9]);
    XCTAssertEqual(0, [Monster abilityModifierForScore:10]);
    XCTAssertEqual(0, [Monster abilityModifierForScore:11]);
    XCTAssertEqual(1, [Monster abilityModifierForScore:12]);
    XCTAssertEqual(1, [Monster abilityModifierForScore:13]);
    XCTAssertEqual(2, [Monster abilityModifierForScore:14]);
    XCTAssertEqual(2, [Monster abilityModifierForScore:15]);
    XCTAssertEqual(3, [Monster abilityModifierForScore:16]);
    XCTAssertEqual(3, [Monster abilityModifierForScore:17]);
    XCTAssertEqual(4, [Monster abilityModifierForScore:18]);
    XCTAssertEqual(4, [Monster abilityModifierForScore:19]);
    XCTAssertEqual(5, [Monster abilityModifierForScore:20]);
}

- (void)testStrengthScoreGetterAndSetter {
    _monster.strengthScore = 11;
    XCTAssertEqual(11, _monster.strengthScore);
}

- (void)testStrengthModifier {
    _monster.strengthScore = 9;
    XCTAssertEqual(-1, _monster.strengthModifier);
    
    _monster.strengthScore = 10;
    XCTAssertEqual(0, _monster.strengthModifier);
    
    _monster.strengthScore = 12;
    XCTAssertEqual(1, _monster.strengthModifier);
}

@end
