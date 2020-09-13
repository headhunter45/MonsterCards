//
//  EditMonsterViewControllerTests.m
//  MonsterCardsTests
//
//  Created by Tom Hicks on 9/12/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <XCTest/XCTest.h>
@import OCMockito;
@import OCHamcrest;
#import "EditMonsterViewController.h"
#import "AppDelegate.h"

@interface EditMonsterViewControllerTests : XCTestCase

@end

@implementation EditMonsterViewControllerTests {
    EditMonsterViewController *_viewController;
    NSManagedObjectContext *_context;
    AppDelegate *_appDelegate;
    NSPersistentCloudKitContainer *_persistentContainer;
    Monster *_monster;
}

- (void)setUp {
    _monster = mock([Monster class]);
    _viewController = [[EditMonsterViewController alloc] init];
    _context = mock([NSManagedObjectContext class]);
    _appDelegate = mock([AppDelegate class]);
    _persistentContainer = mock([NSPersistentCloudKitContainer class]);
    
    UIApplication.sharedApplication.delegate = _appDelegate;
}

- (void)tearDown {
}

- (void)testRendersSubtypeCell {
    UITableView *monstersTableView = mock([UITableView class]);
    NSIndexPath *path = [NSIndexPath indexPathForRow:3 inSection:0];
    EditableShortStringTableViewCell *shortStringCell = [[EditableShortStringTableViewCell alloc] init];
    UITextField *textField = [[UITextField alloc] init];
    shortStringCell.textField = textField;
    
    [given([monstersTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]) willReturn:shortStringCell];
    
    _monster.subtype = @"elf";
    _viewController.originalMonster = _monster;
    _viewController.monsterTableView = monstersTableView;
    [_viewController viewDidLoad];
    [_viewController viewWillAppear:NO];
    
    UITableViewCell *cell = [_viewController tableView:monstersTableView cellForRowAtIndexPath:path];
    
    XCTAssertNotNil(cell);
    
    XCTAssertTrue([cell isKindOfClass:[EditableShortStringTableViewCell class]]);
    shortStringCell = (EditableShortStringTableViewCell*)cell;
    XCTAssertEqualObjects(@"monster.subtype", shortStringCell.identifier);
    XCTAssertEqualObjects(@"Subtype", shortStringCell.textField.placeholder);
    XCTAssertEqualObjects(@"", shortStringCell.textField.text);
    XCTAssertEqual(_viewController, shortStringCell.delegate);
}

- (void)testEditingSubtype {
    UIViewController *destinationVC = mock([UIViewController class]);
    UIStoryboardSegue *segue = [UIStoryboardSegue segueWithIdentifier:@"SaveChanges" source:_viewController destination:destinationVC performHandler:^{}];
    
    _monster = [[Monster alloc] initWithContext:_context];
    _monster.subtype = @"elf";
    _viewController.originalMonster = _monster;
    [_viewController viewDidLoad];
    [_viewController viewWillAppear:NO];
    
    [_viewController editableValueDidChange:@"newValue" forIdentifier:@"monster.subtype" andType:@"String"];
    
    [_viewController prepareForSegue:segue sender:nil];
    
    XCTAssertEqualObjects(@"newValue", _viewController.originalMonster.subtype);
}

- (void)testRendersAlignmentCell {
    UITableView *monstersTableView = mock([UITableView class]);
    NSIndexPath *path = [NSIndexPath indexPathForRow:4 inSection:0];
    EditableShortStringTableViewCell *shortStringCell = [[EditableShortStringTableViewCell alloc] init];
    UITextField *textField = [[UITextField alloc] init];
    shortStringCell.textField = textField;
    
    [given([monstersTableView dequeueReusableCellWithIdentifier:@"EditableShortString"]) willReturn:shortStringCell];
    
    _monster.alignment = @"chaotic good";
    _viewController.originalMonster = _monster;
    _viewController.monsterTableView = monstersTableView;
    [_viewController viewDidLoad];
    [_viewController viewWillAppear:NO];
    
    UITableViewCell *cell = [_viewController tableView:monstersTableView cellForRowAtIndexPath:path];
    
    XCTAssertNotNil(cell);
    
    XCTAssertTrue([cell isKindOfClass:[EditableShortStringTableViewCell class]]);
    shortStringCell = (EditableShortStringTableViewCell*)cell;
    XCTAssertEqualObjects(@"monster.alignment", shortStringCell.identifier);
    XCTAssertEqualObjects(@"Alignment", shortStringCell.textField.placeholder);
    XCTAssertEqualObjects(@"", shortStringCell.textField.text);
    XCTAssertEqual(_viewController, shortStringCell.delegate);
}

- (void)testEditingAlignment {
    UIViewController *destinationVC = mock([UIViewController class]);
    UIStoryboardSegue *segue = [UIStoryboardSegue segueWithIdentifier:@"SaveChanges" source:_viewController destination:destinationVC performHandler:^{}];
    
    _monster = [[Monster alloc] initWithContext:_context];
    _monster.alignment = @"chaotic good";
    _viewController.originalMonster = _monster;
    [_viewController viewDidLoad];
    [_viewController viewWillAppear:NO];
    
    [_viewController editableValueDidChange:@"newValue" forIdentifier:@"monster.alignment" andType:@"String"];
    
    [_viewController prepareForSegue:segue sender:nil];
    
    XCTAssertEqualObjects(@"newValue", _viewController.originalMonster.alignment);
}


@end
