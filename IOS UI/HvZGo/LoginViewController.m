//
//  LoginViewController.m
//  HvZGo
//
//  Created by Jacob shanklin on 3/18/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "LoginViewController.h"
#import "SecondViewController.h"
#import "ObjectStorage.h"


@interface LoginViewController ()

@end

@implementation LoginViewController

-(BOOL)CheckValidLogin:(NSString*)pswd :(NSString*)uname;
{
    BOOL returnValue = false;
    KeychainItemWrapper *keychainItem = [[KeychainItemWrapper alloc] initWithIdentifier:@"HvZGoLogin" accessGroup:nil];
    NSString *password = [keychainItem objectForKey:(__bridge id)kSecValueData];
    NSString *username = [keychainItem objectForKey:(__bridge id)kSecAttrAccount];
    
    
    if(password != nil && username != nil)
    {
        [self CallLogin:username :password completion:^(NSDictionary *response, NSError *error) {
            if (response) {
                _faction = response[@"faction"];
            } else {
                NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
            }
            
        }];
    }
    else //verify correctness and store in keychain
    {
        [self CallLogin:uname :pswd completion:^(NSDictionary *response, NSError *error) {
            if (response) {
                _faction = response[@"faction"];
            } else {
                NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
            }
            
        }];
        if([_faction isEqualToString:@"fail"])
        {
            KeychainItemWrapper *keychainItem = [[KeychainItemWrapper alloc] initWithIdentifier:@"HvZGoLogin" accessGroup:nil];
            [keychainItem setObject:pswd forKey:(__bridge id)kSecValueData]; //set keychain password
            [keychainItem setObject:uname forKey:(__bridge id)kSecAttrAccount]; //set keychain username
        }
    }
    
    [self CallLogin:uname :pswd completion:^(NSDictionary *response, NSError *error) {
        if (response) {
            _faction = response[@"faction"];
        } else {
            NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
        }
        
    }];
    
    if(![_faction
         isEqualToString:@"fail"])
    {
        returnValue = true;
    }
    return returnValue;
}
-(IBAction)DisplayRegisterAlert:(UIButton *)sender
{
    NSString *alertTitle = NSLocalizedString(@"Registration", @"Please enter registration information.");
    NSString *alertMessage = NSLocalizedString(@"HVZGo", @"PasswordImplimenation");
    
    
    //Create an alert controller
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:alertTitle
                                                                             message:alertMessage
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    //setup the alert to have user ID input field.
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField)
     {
         textField.placeholder = NSLocalizedString(@"User ID", @"Login");
         /*[textField addTarget:self
          action:@selector(alertTextFieldDidChange:)
          forControlEvents:UIControlEventEditingChanged];*/
     }];
    //add a password field as well, and enable secure text entry
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField)
     {
         textField.placeholder = NSLocalizedString(@"Password", @"Password");
         textField.secureTextEntry = YES;
         [textField addTarget:self
                       action:@selector(alertPasswordVerify:)
             forControlEvents:UIControlEventEditingChanged];
     }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField)
     {
         textField.placeholder = NSLocalizedString(@"Verify Password", @"Password");
         textField.secureTextEntry = YES;
         [textField addTarget:self
                       action:@selector(alertPasswordVerify:)
             forControlEvents:UIControlEventEditingChanged];
     }];
    
    //setup the OK and Cancel buttons.
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", @"Cancel action")
                                                           style:UIAlertActionStyleCancel
                                                         handler:^(UIAlertAction *action)
                                   {
                                       NSLog(@"Cancel action");
                                   }];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"OK", @"OK action")
                                                       style:UIAlertActionStyleDefault
                                                     handler:^(UIAlertAction *action)
                               {
                                   UITextField *login = alertController.textFields.firstObject;
                                   UITextField *password = alertController.textFields[2];
                                   
                                   NSString *sLogin = login.text;
                                   NSString *sPassword = password.text;
                                   
                                   [self CallLogin:sLogin :sPassword completion:^(NSDictionary *response, NSError *error) {
                                       if (response) {
                                           NSLog(@"Response: %@", response[@"faction"]);
                                           _faction = response[@"faction"];
                                           if([_faction isEqualToString:@"Human"])
                                           {
                                               [self SwitchToHumanView];
                                           } else {
                                               [self SwitchToZombieView];
                                           }
                                       } else {
                                           NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
                                       }
                                       
                                       
                                   }];
                                   
                                   NSLog(@"Value: %@", _faction);
                                   
                               }];
    
    okAction.enabled = NO; //disable the OK button which will update if there is data input into the text field.
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}
- (IBAction)DisplayLoginAlert:(UIButton *)sender //Login Alert Window
{
    NSString *alertTitle = NSLocalizedString(@"Login", @"Please enter your login information.");
    NSString *alertMessage = NSLocalizedString(@"HVZGo", @"PasswordImplimenation");
    
    
    //Create an alert controller
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:alertTitle
                                                                             message:alertMessage
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    //setup the alert to have user ID input field.
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField)
     {
         textField.placeholder = NSLocalizedString(@"User ID", @"Login");
         [textField addTarget:self
                       action:@selector(alertTextFieldDidChange:)
             forControlEvents:UIControlEventEditingChanged];
     }];
    //add a password field as well, and enable secure text entry
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField)
     {
         textField.placeholder = NSLocalizedString(@"Password", @"Password");
         textField.secureTextEntry = YES;
     }];
    //setup the OK and Cancel buttons.
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", @"Cancel action")
                                                           style:UIAlertActionStyleCancel
                                                         handler:^(UIAlertAction *action)
                                   {
                                       NSLog(@"Cancel action");
                                   }];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"OK", @"OK action")
                                                       style:UIAlertActionStyleDefault
                                                     handler:^(UIAlertAction *action)
                               {
                                   UITextField *login = alertController.textFields.firstObject;
                                   UITextField *password = alertController.textFields.lastObject;
                                   
                                   NSString *sLogin = login.text;
                                   NSString *sPassword = password.text;
                                   
                                   [self CallLogin:sLogin :sPassword completion:^(NSDictionary *response, NSError *error) {
                                       if (response) {
                                           NSLog(@"Response: %@", response[@"faction"]);
                                           _faction = response[@"faction"];
                                           if([_faction isEqualToString:@"Human"])
                                           {
                                               [self SwitchToHumanView];
                                           } else {
                                               [self SwitchToZombieView];
                                           }
                                       } else {
                                           NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
                                       }
                                       
                                   }];
                                   NSLog(@"Value: %@", _faction);
                                   
                               }];
    
    okAction.enabled = NO; //disable the OK button which will update if there is data input into the text field.
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}
-(void)alertPasswordVerify:(UITextField *)sender
{
    UIAlertController *alertController = (UIAlertController *)self.presentedViewController;
    NSString *alertMessage = NSLocalizedString(@"Passwords must match!", @"PasswordImplimenation");
    if (alertController) //if we got an active view controller
    {
        UITextField *password1 = alertController.textFields[1]; //Check for data inslot one
        NSLog(@"value 1 entered: %@", password1.text);
        UITextField *password2 = alertController.textFields[2];
        NSLog(@"value 2 entered: %@", password2.text);
        UIAlertAction *okAction = alertController.actions.lastObject; //get a pointer to the OK button
        if(password1.text.length > 5)
        {
            if([password1.text isEqualToString:password2.text])
            {
                okAction.enabled = true;
            } else {
                alertController.message = alertMessage;
            }
        } else {
            alertController.message = @"Password must be 6 characters";
        }
        
    }
}
- (void)alertTextFieldDidChange:(UITextField *)sender //Verify if the user has input data into the userID window
{
    UIAlertController *alertController = (UIAlertController *)self.presentedViewController; //point to the current view controller
    if (alertController) //if we got an active view controller
    {
        UITextField *login = alertController.textFields.firstObject; //check the login field for data
        UIAlertAction *okAction = alertController.actions.lastObject; //get a pointer to the OK button
        okAction.enabled = login.text.length > 5; //enable it if the login text field has a length greater than 5
    }
}


-(IBAction)SwitchToZombieView
{
    //[self CheckValidLogin];
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ZombieVC"];
    [self presentViewController:vc animated:YES completion:nil];
}

-(IBAction)SwitchToHumanView
{
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"HumanVC"];
    [self presentViewController:vc animated:YES completion:nil];
    
}

-(IBAction)SwitchToModeratorView:(id)sender
{
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
    [self presentViewController:vc animated:YES completion:nil];
    
}

-(void)CallLogin:(NSString*)username :(NSString*)password completion:(void (^)(NSDictionary *responseObject, NSError *error))completion
{
    
    NSURL *url = [NSURL URLWithString:@"http://www.hvz-go.com/iosLogin.php"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    // 3
    NSDictionary *dictionary = @{@"username":username, @"password":password};
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request
                                                                   fromData:data completionHandler:^(NSData *data,NSURLResponse *response,NSError *error)
                                              {
                                                  if (!data) {
                                                      if (completion) {
                                                          dispatch_async(dispatch_get_main_queue(), ^{
                                                              completion(nil, error);
                                                          });
                                                      }
                                                      return;
                                                  }
                                                  
                                                  NSError *parseError = nil;
                                                  NSDictionary *returnedData = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                                                  
                                                  if (!returnedData) {
                                                      if (completion) {
                                                          dispatch_async(dispatch_get_main_queue(), ^{
                                                              completion(nil, parseError);
                                                          });
                                                      }
                                                      return;
                                                  }
                                                  
                                                  // if everything is ok, then just return the JSON object
                                                  
                                                  if (completion) {
                                                      dispatch_async(dispatch_get_main_queue(), ^{
                                                          completion(returnedData, nil);
                                                      });
                                                  }
                                                  
                                                  
                                              }];
        
        
        // 5
        [uploadTask resume];
    }
    
    
}

-(void)CallRegister:(NSString*)username :(NSString*)password completion:(void (^)(NSDictionary *responseObject, NSError *error))completion
{
    
    NSURL *url = [NSURL URLWithString:@"http://www.hvz-go.com/iosRegister.php"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    // 3
    NSDictionary *dictionary = @{@"username":username, @"password":password};
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request
                                                                   fromData:data completionHandler:^(NSData *data,NSURLResponse *response,NSError *error)
                                              {
                                                  if (!data) {
                                                      if (completion) {
                                                          dispatch_async(dispatch_get_main_queue(), ^{
                                                              completion(nil, error);
                                                          });
                                                      }
                                                      return;
                                                  }
                                                  
                                                  NSError *parseError = nil;
                                                  NSDictionary *returnedData = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                                                  
                                                  if (!returnedData) {
                                                      if (completion) {
                                                          dispatch_async(dispatch_get_main_queue(), ^{
                                                              completion(nil, parseError);
                                                          });
                                                      }
                                                      return;
                                                  }
                                                  
                                                  // if everything is ok, then just return the JSON object
                                                  
                                                  if (completion) {
                                                      dispatch_async(dispatch_get_main_queue(), ^{
                                                          completion(returnedData, nil);
                                                      });
                                                  }
                                                  
                                                  
                                              }];
        
        
        // 5
        [uploadTask resume];
    }
    
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do view setup here.
}

@end
