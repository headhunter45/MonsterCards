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
    NSData *data = [htmlString dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    NSDictionary *options = @{NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType,
                              NSCharacterEncodingDocumentAttribute: @(NSUTF8StringEncoding)};
    return [[NSAttributedString alloc] initWithData:data
                                            options:options
                                 documentAttributes:nil
                                              error:nil];
}

@end
