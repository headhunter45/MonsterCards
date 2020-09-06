//
//  SkillTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/5/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
@import OCMockito;
@import OCHamcrest;
#import "Skill.h"

@interface SkillTests : XCTestCase

@end

@implementation SkillTests {
    Skill *_skill;
    NSString *_name;
    NSString *_abilityScoreName;
    NSString *_notes;
}

- (void)setUp {
    _skill = [[Skill alloc] init];
    _name = @"pranking";
    _abilityScoreName = @"str";
    _notes = @"some notes";
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
}

- (void)testDefaultInitializer {
    XCTAssertNotNil(_skill);
    XCTAssertEqualObjects(@"", _skill.name);
    XCTAssertEqualObjects(@"", _skill.abilityScoreName);
    XCTAssertEqualObjects(@"", _skill.notes);
}

- (void)testInitWithNameAbilityScoreNameAndNotes {
    _skill = [[Skill alloc] initWithName:_name abilityScoreName:_abilityScoreName andNotes:_notes];
    XCTAssertNotNil(_skill);
    XCTAssertEqualObjects(_name, _skill.name);
    XCTAssertEqualObjects(_abilityScoreName, _skill.abilityScoreName);
    XCTAssertEqualObjects(_notes, _skill.notes);
}

- (void)testNameGetterAndSetter {
    _skill.name = _name;
    XCTAssertEqualObjects(_name, _skill.name);
}

- (void)testAbilityScoreNameGetterAndSetter {
    _skill.abilityScoreName = _abilityScoreName;
    XCTAssertEqualObjects(_abilityScoreName, _skill.abilityScoreName);
}

- (void)testNotesGetterAndSetter {
    _skill.notes = _notes;
    XCTAssertEqualObjects(_notes, _skill.notes);
}

- (void)testSkillBonusForMonster {
    Monster *monster = mock([Monster class]);
    [given([monster abilityModifierForAbilityScoreName:_abilityScoreName]) willReturnInt:1];
    stubProperty(monster, proficiencyBonus, @2);
    
    _skill = [[Skill alloc] initWithName:_name abilityScoreName:_abilityScoreName andNotes:_notes];

    XCTAssertEqual(3, [_skill skillBonusForMonster:monster]);
}

- (void)testSkillBonusForMonsterWithExpertise {
    Monster *monster = mock([Monster class]);
    [given([monster abilityModifierForAbilityScoreName:_abilityScoreName]) willReturnInt:1];
    stubProperty(monster, proficiencyBonus, @2);
    
    _skill = [[Skill alloc] initWithName:_name abilityScoreName:_abilityScoreName andNotes:@" (ex)"];

    XCTAssertEqual(5, [_skill skillBonusForMonster:monster]);
}

- (void)testTextForMonster {
    Monster *monster = mock([Monster class]);
    [given([monster abilityModifierForAbilityScoreName:_abilityScoreName]) willReturnInt:1];
    stubProperty(monster, proficiencyBonus, @2);
    
    _skill = [[Skill alloc] initWithName:_name abilityScoreName:_abilityScoreName andNotes:_notes];
    
    XCTAssertEqualObjects(@"Pranking 3", [_skill textForMonster:monster]);
}

- (void)testTextForMonsterWithExpertise {
    Monster *monster = mock([Monster class]);
    [given([monster abilityModifierForAbilityScoreName:_abilityScoreName]) willReturnInt:1];
    stubProperty(monster, proficiencyBonus, @2);
    
    _skill = [[Skill alloc] initWithName:_name abilityScoreName:_abilityScoreName andNotes:@" (ex)"];
    
    XCTAssertEqualObjects(@"Pranking 5", [_skill textForMonster:monster]);
}

@end
