//
//  LoginViewController.h
//  HvZGo
//
//  Created by Jacob shanklin on 3/18/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KeychainItemWrapper.h"

@interface LoginViewController : UIViewController
{
    

}
@property (weak, nonatomic) IBOutlet UITextField *UserNameText;
@property (weak, nonatomic) IBOutlet UITextField *PasswordText;
-(BOOL)CheckValidLogin:(NSString*)pswd :(NSString*)uname;
-(IBAction)SwitchToZombieView;
-(IBAction)SwitchToHumanView;
-(IBAction)DisplayLoginAlert:(id)sender;
-(NSString*)CallLogin:(NSString*)username :(NSString*)password;

@end
