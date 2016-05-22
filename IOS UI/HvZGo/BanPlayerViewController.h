//
//  BanPlayerViewController.h
//  HvZGo
//
//  Created by Clint Jellesed on 5/3/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BanPlayerViewController : UIViewController
- (IBAction)goBack:(UISwipeGestureRecognizer *)sender;
- (IBAction)clickBan:(UIButton *)sender;
@property (weak, nonatomic) IBOutlet UITextView *banBox;
@property (weak, nonatomic) IBOutlet UITextView *dayBox;
@property (weak, nonatomic) IBOutlet UITextView *reasonBox;
-(void)banPlayer:(NSString*)player :(NSString*)days :(NSString*)reason completion:(void (^)(NSDictionary *responseObject, NSError *error))completion;

@end
