//
//  AnnouncementViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/2/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "AnnouncementViewController.h"

@interface AnnouncementViewController ()

@end

@implementation AnnouncementViewController

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

- (IBAction)sendButton:(UIButton *)sender {
    
    //UITextField *login = _textBox.text;
    //UITextField *password = alertController.textFields.lastObject;
    
    NSString *message = _textBox.text;
    NSString *faction = @"Z";
    [self postAlert:message :faction completion:^(NSDictionary *response, NSError *error) {
        if (response) {
            NSLog(@"Response: %@", response[@"faction"]);
            //_faction = response[@"faction"];
        }
        else {
            NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
        }
    }];

    

}

- (IBAction)goBack:(UISwipeGestureRecognizer *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
    [self presentViewController:vc animated:YES completion:nil];
}

-(void)postAlert:(NSString*)message :(NSString*)faction completion:(void (^)(NSDictionary *responseObject, NSError *error))completion
{
    
    NSURL *url = [NSURL URLWithString:@"http://www.hvz-go.com/iosRegister.php"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    // 3
    NSDictionary *dictionary = @{@"message":message, @"faction":faction};
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



- (IBAction)factionChoice:(UISegmentedControl *)sender {
    NSString *selected = [sender titleForSegmentAtIndex:sender.selectedSegmentIndex];
}
@end
