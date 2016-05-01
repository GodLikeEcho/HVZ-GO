//
//  ObjectStorage.h
//  HvZGo
//
//  Created by Jacob shanklin on 4/10/16.
//  Copyright © 2016 Jacob shanklin. All rights reserved.
//

#ifndef ObjectStorage_h
#define ObjectStorage_h

@class ObjectStorage;

extern ObjectStorage *Store;

@interface ObjectStorage : NSObject
{
    NSString *Faction;
    
}

+(void)loadStore;

//+(void)setFaction:(NSString*)_Faction;

@property(strong, nonatomic, readwrite) NSString *Faction;

@end

#endif /* ObjectStorage_h */
