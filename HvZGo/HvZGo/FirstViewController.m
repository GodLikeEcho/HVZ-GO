//
//  FirstViewController.m
//  HvZGo
//
//  Created by Jacob shanklin on 2/18/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "FirstViewController.h"

@interface FirstViewController ()

@end

@implementation FirstViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    TimerEventHandler = [NSTimer
                         scheduledTimerWithTimeInterval:0.5
                         target:self
                         selector:@selector(updateClock)
                         userInfo:nil
                         repeats:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)updateClock {
    NSDateFormatter *clockFormater = [[NSDateFormatter alloc] init];
    [clockFormater setDateFormat:@"hh:mm"];
    myClock.text = [clockFormater stringFromDate:[NSDate date]];
    
}

-(IBAction)SwitchToChat:(id)sender
{
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ChatView"];
    [self presentViewController:vc animated:YES completion:nil];
}


-(IBAction)DisplayRulesAlert:(id)sender {

    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Rules:"
                                                                   message:[NSString stringWithFormat: @"%C Follow All Moderator Directions.\n %C Some More Rules.\n %C Another Rule.", (unichar) 0x2022, (unichar) 0x2022, (unichar) 0x2022]
                    preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"I will abide by these rules." style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {}];
    
    
    [alert addAction:defaultAction];
    [self presentViewController:alert animated:YES completion:nil];

    
}

-(IBAction)DisplayMissionsAlert:(id)sender {
    
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Active Mission:"
                                                                   message:@"Kill All Zombies."
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {}];
    
    [alert addAction:defaultAction];
    [self presentViewController:alert animated:YES completion:nil];
    
    
}

@end
