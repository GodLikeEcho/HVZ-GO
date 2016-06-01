//
//  AllChatViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/3/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "AllChatViewController.h"

@interface AllChatViewController ()

@end

@implementation AllChatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *fullURL = @"http://hvz-go.com/achat/";
    NSURL *url = [NSURL URLWithString:fullURL];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [_viewWeb loadRequest:requestObj];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)swipeBack:(UISwipeGestureRecognizer *)sender {
        UIStoryboard *storyboard = self.storyboard;
        UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginVC"];
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
