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
@property (weak, nonatomic) __block NSString *faction;
-(BOOL)CheckValidLogin:(NSString*)pswd :(NSString*)uname;
-(IBAction)SwitchToZombieView;
-(IBAction)SwitchToHumanView;
-(IBAction)SwitchToModeratorView:(id)sender;
-(IBAction)DisplayLoginAlert:(id)sender;
-(void)alertPasswordVerify:(UITextField *)sender;
-(void)CallLogin:(NSString*)username :(NSString*)password completion:(void (^)(NSDictionary *responseObject, NSError *error))completion;
-(void)CallRegister:(NSString*)username :(NSString*)password completion:(void (^)(NSDictionary *responseObject, NSError *error))completion;

@end
