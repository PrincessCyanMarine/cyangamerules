This is a really simple mod I made as practice

It adds a few gamerules inspired by some youtube videos that make blocks rain from the sky, tnt spawn on players, random items to be given at a timer, etc.


You can find my other mods [here](https://www.cyanmarine.net/mods)

###### *Server sided*


# Gamerules

## Block rain
### cyangamerules:doBlockRain
Default: False

Turns block rain on/off
### cyangamerules:doBlockRainDropOnBreak
Default: False

If the falling blocks should drop themselves when breaking (as sand, gravel, concrete powder, etc do when falling on decorations like slabs or flowers)
### cyangamerules:blockRainFrequency
Default: 10

Min: 1

Max: 10000

How frequently should blocks fall from the sky
### cyangamerules:blockRainRadius
Default: 5

Min: 1

Max: 50

How big is the area in which blocks fall (centered on players)

## Magical feet
### cyangamerules:doMagicalFeet
Default: False

If the block bellow the player (or the one they are inside, in the case of slabs/grass) should be transformed into a random block
### cyangamerules:magicalFeetAfter
Default: False

If magical feet should transform the block after the player stepped off of it


## Give random item
### cyangamerules:doGiveRandomItem
Default: False

Gives a random item to players on a set timer
### cyangamerules:randomItemInterval
Default: 100

Min: 1

How long in ticks it waits in between items (20 ticks/second)

## Spawn TNT
### cyangamerules:doSpawnTnt
Default: False

Spawns tnt on the player's location on a set timer

### cyangamerules:spawnTntRandomFuse
Default: True

If the tnt should have a random fuse (how long it takes to explode) between 1 or 160 or should use the default fuse of 80  

### cyangamerules:spawnTntInterval
Default: 200

Min: 1

How long in ticks it waits in between spawning a TNT (20 ticks/second)

## Spawn mob

### cyangamerules:doSpawnMob
Default: False

Spawns a random mob on the player's location on a set timer

### cyangamerules:spawnMobInterval
Default 200

Min: 1

How long in ticks it waits in between spawning mobs (20 ticks/second)