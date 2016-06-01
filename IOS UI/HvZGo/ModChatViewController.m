//
//  ModeratorChatViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/3/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "ModChatViewController.h"

@interface ModChatViewController ()

@end

@implementation ModChatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *fullURL = @"http://hvz-go.com/mchat/";
    NSURL *url = [NSURL URLWithString:fullURL];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [_ModWebView loadRequest:requestObj];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)swipeBack:(UISwipeGestureRecognizer *)sender {
        UIStoryboard *storyboard = self.storyboard;
        UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
        [self presentViewController:vc animated:YES completion:nil];
}


/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
