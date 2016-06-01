//
//  SecondViewController.m
//  HvZGo
//
//  Created by Jacob shanklin on 2/18/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "SecondViewController.h"

@interface SecondViewController ()

@end

@implementation SecondViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
- (IBAction)swipeBack:(UISwipeGestureRecognizer *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginVC"];
    [self presentViewController:vc animated:YES completion:nil];
}

-(IBAction)DisplayMissionsAlert:(id)sender {
    
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Active Mission:"
                                                                   message:@"Eat more brains."
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {}];
    
    [alert addAction:defaultAction];
    [self presentViewController:alert animated:YES completion:nil];
    
    
}

-(IBAction)DisplaySuccess {
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Tag User"
                                                                   message:@"Success!"
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {}];
    
    [alert addAction:defaultAction];
    [self presentViewController:alert animated:YES completion:nil];

}

-(IBAction)DisplayFailure {
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Tag User"
                                                                   message:@"UserID Not Recognized!"
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {}];
    
    [alert addAction:defaultAction];
    [self presentViewController:alert animated:YES completion:nil];
    
}



-(IBAction)DisplayTagAlert:(id)sender
{

NSString *alertTitle = NSLocalizedString(@"Tagged User", @"Please enter tagged users UserID.");
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



                               NSLog(@"OK action");
                               NSLog(@"Login value: %@",login.text); //this is were we will send a json verification request and compare the values, if they values do not match we will call a new alert view. if they match we are going to segue to the next scene.
                               {if([login.text isEqualToString:@"111111"])
                               { [self DisplayFailure];}
                               else
                               {
                                [self DisplaySuccess];
                               }}
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




@end
