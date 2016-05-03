//
//  ThirdViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/2/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "ThirdViewController.h"

@interface ThirdViewController ()
- (IBAction)SwitchToReportsView:(UIButton *)sender;
- (IBAction)SwitchToBanPlayerView:(UIButton *)sender;
- (IBAction)SwitchToGameSettingsView:(UIButton *)sender;
- (IBAction)SwitchToPostMissionView:(UIButton *)sender;
- (IBAction)SwitchToAnnouncementView:(UIButton *)sender;

@end

@implementation ThirdViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)SwitchToReportsView:(UIButton *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ViewReports"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)SwitchToBanPlayerView:(UIButton *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"BanPlayer"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)SwitchToGameSettingsView:(UIButton *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"GameSettings"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)SwitchToPostMissionView:(UIButton *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"PostMissionView"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)SwitchToAnnouncementView:(UIButton *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"Announcement UI"];
    [self presentViewController:vc animated:YES completion:nil];
}

@end
