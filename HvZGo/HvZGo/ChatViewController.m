//
//  ChatViewController.m
//  HvZGo
//
//  Created by Jacob shanklin on 4/4/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "ChatViewController.h"
#import "ObjectStorage.h"

@interface ChatViewController ()

@end

@implementation ChatViewController

-(IBAction)SwitchToAllChatView
{
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"AllChatViewController"];
    [self presentViewController:vc animated:YES completion:nil];
}

-(IBAction)SwitchToFactionChatView
{
    NSString *sFaction = [ObjectStorage Faction];
    UIStoryboard *storyboard = self.storyboard;
    
    //check to see which faction user is in.
    NSString *faction = [[NSString alloc] init];
    if([sFaction isEqual: @"H"])
        faction = @"HumanChatView";
    else
        faction = @"ZombieChatView";
    
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:faction];
    [self presentViewController:vc animated:YES completion:nil];
    
}

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

@end
