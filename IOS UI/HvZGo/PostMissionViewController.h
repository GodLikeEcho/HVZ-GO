//
//  PostMissionViewController.h
//  HvZGo
//
//  Created by Clint Jellesed on 5/2/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PostMissionViewController : UIViewController
- (IBAction)goBack:(UISwipeGestureRecognizer *)sender;
- (IBAction)toggleFaction:(UISegmentedControl *)sender;
- (IBAction)clickMission:(UIButton *)sender;
@property (weak, nonatomic) IBOutlet UISegmentedControl *toggleFactionOutlet;
-(void)postMission:(NSString*)message :(NSString*)faction :(NSString*) endtime completion:(void (^)(NSDictionary *responseObject, NSError *error))completion;
@property (weak, nonatomic) IBOutlet UITextField *timeBox;
@property (weak, nonatomic) IBOutlet UITextView *textBox;


@end
