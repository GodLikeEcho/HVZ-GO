//
//  FirstViewController.h
//  HvZGo
//
//  Created by Jacob shanklin on 2/18/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FirstViewController : UIViewController
{
    IBOutlet UILabel * myClock;
    NSTimer *TimerEventHandler;

}

-(void)updateClock;
-(IBAction)DisplayRulesAlert:(id)sender;
-(IBAction)DisplayMissionsAlert:(id)sender;
-(IBAction)SwitchToChat:(id)sender;


@end

