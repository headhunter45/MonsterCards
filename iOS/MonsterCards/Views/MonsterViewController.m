//
//  MonsterViewController.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/4/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "MonsterViewController.h"
#import "EditMonsterViewController.h"
#import "HTMLHelper.h"
#import "AppDelegate.h"

@interface MonsterViewController ()

@end

NSString *const defaultFontFamily = @"helvetica";
NSString *const defaultFontSize = @"12pt";
NSString *const defaultTextColor = @"#9B2818";

NSString* makeHTMLFragmentString(NSString* format, ...) {
    va_list args;
    va_start(args, format);
    NSString *childString = [[NSString alloc] initWithFormat:format arguments:args];
    va_end(args);
    
    NSString *formattedString = [NSString stringWithFormat:@"<span style=\"font-family: %@; font-size: %@; color: %@;\">%@</span>", defaultFontFamily, defaultFontSize, defaultTextColor, childString];
    return formattedString;
}

@implementation MonsterViewController {
    NSManagedObjectContext *_context;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate *appDelegate = (AppDelegate*)UIApplication.sharedApplication.delegate;
    _context = appDelegate.persistentContainer.viewContext;
}

- (void)viewWillAppear:(BOOL)animated {
    [_context refreshObject:self.monster mergeChanges:NO];
    if (self.monsterName != nil) {
        self.monsterName.text = self.monster.name;
    } else if (self.navigationItem != nil) {
        self.navigationItem.title = self.monster.name;
    }
    if (self.monsterName != nil) {
        self.monsterName.text = self.monster.name;
    } else if (self.navigationItem != nil) {
        if (self.monster.name == nil) {
            self.navigationItem.title = @"Unnamed Monster";
        } else {
            self.navigationItem.title = self.monster.name;
        }
    }
    if (self.monsterMeta != nil) {
        NSString *metaText = self.monster.meta;
        if (metaText == nil) {
            self.monsterMeta.text = @"";
        } else {
            self.monsterMeta.text = metaText;
        }
    }
    if (self.monsterArmorClass != nil) {
        NSString *armorClassDescription = self.monster.armorClassDescription;
        if (armorClassDescription == nil) {
            self.monsterArmorClass.text = @"";
        } else {
            self.monsterArmorClass.attributedText = [HTMLHelper attributedStringFromHTML:makeHTMLFragmentString(@"<b>Armor Class</b> %@</span>", armorClassDescription)];
        }
    }
    if (self.monsterHitPoints != nil) {
        NSString *hitPointsDescription = self.monster.hitPointsDescription;
        if (hitPointsDescription == nil) {
            self.monsterHitPoints.text = @"";
        } else {
            self.monsterHitPoints.attributedText = [HTMLHelper attributedStringFromHTML:makeHTMLFragmentString(@"<b>Hit Points</b> %@", hitPointsDescription)];
        }
    }
    if (self.monsterSpeed != nil) {
        NSString *speedDescription = self.monster.speedDescription;
        if (speedDescription == nil) {
            self.monsterSpeed.text = @"";
        } else {
            self.monsterSpeed.attributedText = [HTMLHelper attributedStringFromHTML:makeHTMLFragmentString(@"<b>Speed</b> %@", speedDescription)];
        }
    }
    if (self.monsterStrength) {
        self.monsterStrength.text = self.monster.strengthDescription;
    }
    if (self.monsterDexterity) {
        self.monsterDexterity.text = self.monster.dexterityDescription;
    }
    if (self.monsterConstitution) {
        self.monsterConstitution.text = self.monster.constitutionDescription;
    }
    if (self.monsterIntelligence) {
        self.monsterIntelligence.text = self.monster.intelligenceDescription;
    }
    if (self.monsterWisdom) {
        self.monsterWisdom.text = self.monster.wisdomDescription;
    }
    if (self.monsterCharisma) {
        self.monsterCharisma.text = self.monster.charismaDescription;
    }
}

- (IBAction)unwindWithSegue:(UIStoryboardSegue *)unwindSegue {
//    UIViewController *sourceViewController = unwindSegue.sourceViewController;
    // Use data from the view controller which initiated the unwind segue
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([@"EditMonster" isEqualToString:segue.identifier]) {
        if ([segue.destinationViewController isKindOfClass:[EditMonsterViewController class]]) {
            EditMonsterViewController *vc = (EditMonsterViewController*)segue.destinationViewController;
            vc.originalMonster = self.monster;
        }
    }
}

@end
