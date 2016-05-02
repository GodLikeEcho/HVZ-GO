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
       [self CallLogin:username :password];
   }
   else //verify correctness and store in keychain
   {
       if(![[self CallLogin:uname :pswd] isEqualToString:@"fail"])
        {
            KeychainItemWrapper *keychainItem = [[KeychainItemWrapper alloc] initWithIdentifier:@"HvZGoLogin" accessGroup:nil];
            [keychainItem setObject:pswd forKey:(__bridge id)kSecValueData]; //set keychain password
            [keychainItem setObject:uname forKey:(__bridge id)kSecAttrAccount]; //set keychain username
        }
   }
    
    if(![[self CallLogin:uname :pswd] isEqualToString:@"fail"])
    {
        returnValue = true;
    }
    return returnValue;
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
                                   //ObjectStorage.Faction = [self CallLogin:sLogin :sPassword];
                                   
                                   // if([self CheckValidLogin:sLogin :sPassword])
                                   //{
                                       
                                   //}
                                   //else
                                   //{
                                        //Bad Login
                                   //}
                                   NSLog(@"Value: %@", [self CallLogin:sLogin :sPassword]);
                                   //[self SwitchToHumanView];
                                   //NSLog(@"OK action"); //Temporary data output to the logs for testing purposes
                                   //NSLog(@"Login value: %@",login.text); //this is were we will send a json verification request and compare the values, if they values do not match we will call a new alert view. if they match we are going to segue to the next scene.
                                   //NSLog(@"Password value: %@",password.text);
                               }];
    
    okAction.enabled = NO; //disable the OK button which will update if there is data input into the text field.
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
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

-(IBAction)SwitchToModeratorView:(id)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
    [self presentViewController:vc animated:YES completion:nil];

}
-(IBAction)RegisterUser:(id)sender
{
    //register user
    NSString *password = [[NSString alloc] init];
    NSString *login = [[NSString alloc] init];
    
    //register user php stuff
    
    //then send a check valid for keychain storage
    [self CheckValidLogin:(NSString*)password :(NSString*)login];
    
}

-(NSString*)CallLogin:(NSString*)username :(NSString*)password
{

    __block NSString *temp = [[NSString alloc] init];
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
                                                                   fromData:data completionHandler:^(NSData *data,NSURLResponse *response,NSError *error) {
                                                                       NSDictionary *json= [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
                                                                       NSLog(@"GotThis: %@", json[@"faction"]);
                                                                       temp = json[@"faction"];
                                                                       NSLog(@"Temp is: %@", temp);
                                                                       
                                                                      
                                                                   }];
        
        
        // 5
        [uploadTask resume];
    }
    
    NSLog(@"Returned: %@", temp);
    //login php stuff
    return temp;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do view setup here.
}

@end
