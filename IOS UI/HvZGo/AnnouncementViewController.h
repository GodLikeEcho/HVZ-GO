//
//  AnnouncementViewController.h
//  HvZGo
//
//  Created by Clint Jellesed on 5/2/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AnnouncementViewController : UIViewController
@property (weak, nonatomic) IBOutlet UISegmentedControl *ToggleFactionOutput;
- (IBAction)factionChoice:(UISegmentedControl *)sender;
@property (weak, nonatomic) IBOutlet UITextView *textBox;
- (IBAction)sendButton:(UIButton *)sender;
- (IBAction)goBack:(UISwipeGestureRecognizer *)sender;
-(void)postAlert:(NSString*)message :(NSString*)faction completion:(void (^)(NSDictionary *responseObject, NSError *error))completion;

@end
