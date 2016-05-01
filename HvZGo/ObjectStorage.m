//
//  ObjectStorage.m
//  HvZGo
//
//  Created by Jacob shanklin on 4/10/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#import <Foundation/Foundation.h>
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


@end
