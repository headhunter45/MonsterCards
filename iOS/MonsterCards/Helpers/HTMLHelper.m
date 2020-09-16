//
//  HtmlHelper.m
//  MonsterCards
//
//  Created by Tom Hicks on 9/12/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import "HTMLHelper.h"
@import UIKit;

@implementation HTMLHelper

+ (NSAttributedString*)attributedStringFromHTML:(NSString *)htmlString {
    
    return [[NSAttributedString alloc] initWithData:[htmlString dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES]
               options:@{NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType,
                         NSCharacterEncodingDocumentAttribute: @(NSUTF8StringEncoding)}
    documentAttributes:nil error:nil];
}

@end
