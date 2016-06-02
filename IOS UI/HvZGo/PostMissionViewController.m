//
//  PostMissionViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/2/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "PostMissionViewController.h"

@interface PostMissionViewController ()

@end

@implementation PostMissionViewController

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

NSString *faction = @"H";
- (IBAction)goBack:(UISwipeGestureRecognizer *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)toggleFaction:(UISegmentedControl *)sender {
    if(_toggleFactionOutlet.selectedSegmentIndex == 0)
    {
        faction = @"H";
    }
    else if(_toggleFactionOutlet.selectedSegmentIndex == 1)
    {
        faction = @"Z";
    }
}

- (IBAction)clickMission:(UIButton *)sender {
    //UITextField *login = _textBox.text;
    //UITextField *password = alertController.textFields.lastObject;
    
    NSString *message = _textBox.text;
    //NSString *faction = @"Z";
    //Send endtime here
    //NSString *endtime = _timeBox.text;
    int inttime = [_timeBox.text intValue];
    NSDateFormatter *gmtDateFormatter = [[NSDateFormatter alloc] init];
    gmtDateFormatter.timeZone = [NSTimeZone timeZoneForSecondsFromGMT:0];
    gmtDateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    NSDate *mydate = [NSDate date];
    NSTimeInterval secondsInHours = inttime * 60 * 60;
    NSDate *newDate = [mydate dateByAddingTimeInterval:secondsInHours];
    NSString *endtime = [gmtDateFormatter stringFromDate:newDate];
    NSLog(@"Response: %@", endtime );

    [self postMission:message :faction :endtime completion:^(NSDictionary *response, NSError *error) {
        if (response) {
            NSLog(@"Response: %@", response[@"status"]);
            NSLog(@"Log: %@ %@ %@", message, faction, endtime);
            //_faction = response[@"faction"];
           if([response[@"status"]  isEqual: @"success"])
           {
            UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Status"
                                                                           message:[NSString stringWithFormat: @"%C Success: Mission has been posted.\n", (unichar) 0x2022]
                                                                    preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"Confirm" style:UIAlertActionStyleDefault
                                                                  handler:^(UIAlertAction * action) {}];
            
            
            [alert addAction:defaultAction];
            [self presentViewController:alert animated:YES completion:nil];
           }
           else {
               NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
               UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Status"
                                                                              message:[NSString stringWithFormat: @"%C Failure: Recieved incorrect parameters.\n", (unichar) 0x2022]
                                                                       preferredStyle:UIAlertControllerStyleAlert];
               
               UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"Confirm" style:UIAlertActionStyleDefault
                                                                     handler:^(UIAlertAction * action) {}];
               
               
               [alert addAction:defaultAction];
               [self presentViewController:alert animated:YES completion:nil];

           }
        }
        else {
            NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
            UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Status"
                                                                           message:[NSString stringWithFormat: @"%C Failure: No response recieved.\n", (unichar) 0x2022]
                                                                    preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"Confirm" style:UIAlertActionStyleDefault
                                                                  handler:^(UIAlertAction * action) {}];
            
            
            [alert addAction:defaultAction];
            [self presentViewController:alert animated:YES completion:nil];

        }
    }];
}

-(void)postMission:(NSString*)message :(NSString*)faction : (NSString*)endtime completion:(void (^)(NSDictionary *responseObject, NSError *error))completion
{
    
    NSURL *url = [NSURL URLWithString:@"http://www.hvz-go.com/iosPostMission.php"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    // 3
    NSDictionary *dictionary = @{@"message":message, @"faction":faction, @"endtime":endtime};
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

@end
