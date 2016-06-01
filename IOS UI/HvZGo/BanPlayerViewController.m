//
//  BanPlayerViewController.m
//  HvZGo
//
//  Created by Clint Jellesed on 5/3/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import "BanPlayerViewController.h"

@interface BanPlayerViewController ()

@end

@implementation BanPlayerViewController

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

- (IBAction)goBack:(UISwipeGestureRecognizer *)sender {
    UIStoryboard *storyboard = self.storyboard;
    UIViewController * vc = [storyboard instantiateViewControllerWithIdentifier:@"ModeratorVC"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)clickBan:(UIButton *)sender {
    //UITextField *login = _textBox.text;
    //UITextField *password = alertController.textFields.lastObject;
    
    NSString *player = _banBox.text;
    int days = [_dayBox.text intValue];
    NSString *reason = _reasonBox.text;
    NSString *reqid = @"5";
    
    NSDateFormatter *gmtDateFormatter = [[NSDateFormatter alloc] init];
    gmtDateFormatter.timeZone = [NSTimeZone timeZoneForSecondsFromGMT:0];
    gmtDateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    NSDate *mydate = [NSDate date];
    NSTimeInterval secondsInDays = days * 60 * 60 * 24;
    NSDate *newDate = [mydate dateByAddingTimeInterval:secondsInDays];
    NSString *endtime = [gmtDateFormatter stringFromDate:newDate];
    
    NSLog(@"Response: %@", endtime );

    [self banPlayer:player :endtime :reason :reqid completion:^(NSDictionary *response, NSError *error) {
        if (response) {
            NSLog(@"Response: %@", response[@"status"]);
        }
        else {
            NSLog(@"%s: Server Request Error: %@", __FUNCTION__, error);
            NSLog(@"Response: %@", response[@"retVal"]);
        }
    }];

}

-(void)banPlayer:(NSString*)player :(NSString*)endtime : (NSString*)reason : (NSString*)reqid completion:(void (^)(NSDictionary *responseObject, NSError *error))completion
{
    
    NSURL *url = [NSURL URLWithString:@"http://www.hvz-go.com/iosBanPlayer.php"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    // 3
    NSDictionary *dictionary = @{@"player":player, @"days":endtime, @"reason":reason, @"reqid":reqid};
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
