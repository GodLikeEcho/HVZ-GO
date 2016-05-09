//
//  ObjectStorage.m
//  HvZGo
//
//  Created by Jacob shanklin on 4/10/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <Foundation/Foundation.h>
<<<<<<< HEAD
@interface ObjectStorage
+(NSString*)Faction;
=======
#import "ObjectStorage.h"
ObjectStorage *Store;

@implementation ObjectStorage

@synthesize Faction;

+(void)loadStore {
    Store = [[ObjectStorage alloc] init];
}


-(id)init {
    self = [super init];
    if(self) {
              Faction = @"My Value";
    }
        return self;
}

//-(void)setFaction:(NSString *)_Faction
//{
//    Faction = _Faction;
//}


>>>>>>> c5cd3c4ef73d2673f411a6399c3df46ca1e86c1e
@end
