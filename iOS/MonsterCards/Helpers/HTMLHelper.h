//
//  HtmlHelper.h
//  MonsterCards
//
//  Created by Tom Hicks on 9/12/20.
//  Copyright © 2020 Tom Hicks. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HTMLHelper : NSObject

+(NSAttributedString*)attributedStringFromHTML:(NSString*)html;

@end

NS_ASSUME_NONNULL_END
