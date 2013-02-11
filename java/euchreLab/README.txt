1. order up/pass
2. discard
3. set trump/pass
4. first card
5. second card
6. third card
7. fourth card
8. fifth card

Game play is represented as a stack of "Plays".

When a play is pushed, the game knows what the next play that has to be pushed is.

Plays can be played, and unplayed.

When a play is played it applies changes to the state of the player that made the play.

When a play is played it provides events to be applied to the game result table.

When a play is unplayed it rolls back the changes to the player state.

When a play is unplayed the game unsets any events that were applied during the play.

p4 is dealer
teams: t1:p4/p2 vs. t2:p1/p3

order up/pass:  p1order p1orderalone p1orderpass p2order p2orderalone p2orderpass p3order p3orderalone p3orderpass p4order p4orderalone p4orderpass

discard: p4discard

set trump/pass: p1set p1setalone p1setpass p2set p2setalone p2setpass p3set p3setalone p3setpass p4set p4setalone

first card: p1first p2first p3first p4first
second card: p1second p2second p3second p4second
third card: p1third p2third p3third p4third
fourth card: p1fourth p2fourth p3fourth p4fourth
fifth card: p1fifth p2fifth p3fifth p4fifth

CardVal enum:
Ace
King
Queen
Jack
Ten
Nine

CardSuit enum:

Diamond
Heart
Spade
Club

Card enum:

AceOfDiamonds
KingOfDiamonds
QueenOfDiamonds
JackOfDiamonds
TenOfDiamonds
NineOfDiamonds
AceOfHearts
KingOfHearts
QueenOfHearts
JackOfHearts
TenOfHearts
NineOfHearts
AceOfSpades
KingOfSpades
QueenOfSpades
JackOfSpades
TenOfSpades
NineOfSpades
AceOfClubs
KingOfClubs
QueenOfClubs
JackOfClubs
TenOfClubs
NineOfClubs

Build giant enum set of all possible events.  This is the "flat table" of a complete game.
 - reference cards by card1,card2, ... , card5 (i.e. p1playscard2, p3playscard4, etc.)
 - build a hand index to look up card values
 - end of game events - t2win3tricks, p3winloner4tricks, etc.
 
For play simulation:
 - Tree roots are as follows:
 
p1orderup
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5  
p1orderupalone
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p1orderuppass
p2orderup
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p2orderupalone
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p2orderuppass
p3orderup
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p3orderupalone
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p3orderuppass
p4orderup
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p4orderupalone
  p4discardcard1
  p4discardcard2
  p4discardcard3
  p4discardcard4
  p4discardcard5
p4orderuppass
p1maketrumphearts
p1maketrumpheartsalone
p1maketrumpdiamonds
p1maketrumpdiamondsalone
p1maketrumpspades
p1maketrumpspadesalone
p1maketrumpclubs
p1maketrumpclubsalone
p1maketrumppass
p2maketrumphearts
p2maketrumpheartsalone
p2maketrumpdiamonds
p2maketrumpdiamondsalone
p2maketrumpspades
p2maketrumpspadesalone
p2maketrumpclubs
p2maketrumpclubsalone
p2maketrumppass
p3maketrumphearts
p3maketrumpheartsalone
p3maketrumpdiamonds
p3maketrumpdiamondsalone
p3maketrumpspades
p3maketrumpspadesalone
p3maketrumpclubs
p3maketrumpclubsalone
p3maketrumppass
p4maketrumphearts
p4maketrumpheartsalone
p4maketrumpdiamonds
p4maketrumpdiamondsalone
p4maketrumpspades
p4maketrumpspadesalone
p4maketrumpclubs
p4maketrumpclubsalone

For each of those events, every possible care must be played recursively.
 
 Events:

OPENING ROUND EVENTS
--------------------
p1orderup
p1orderupalone
p1orderuppass
p2orderup
p2orderupalone
p2orderuppass
p3orderup
p3orderupalone
p3orderuppass
p4orderup
p4orderupalone
p4orderuppass
t1orderup
t1orderupalone
t2orderup
t2orderupalone
p4discardcard1
p4discardcard2
p4discardcard3
p4discardcard4
p4discardcard5
p1maketrump
p1maketrumpalone
p1maketrumphearts
p1maketrumpheartsalone
p1maketrumpdiamonds
p1maketrumpdiamondsalone
p1maketrumpspades
p1maketrumpspadesalone
p1maketrumpclubs
p1maketrumpclubsalone
p1maketrumppass
p2maketrump
p2maketrumpalone
p2maketrumphearts
p2maketrumpheartsalone
p2maketrumpdiamonds
p2maketrumpdiamondsalone
p2maketrumpspades
p2maketrumpspadesalone
p2maketrumpclubs
p2maketrumpclubsalone
p2maketrumppass
p3maketrump
p3maketrumpalone
p3maketrumphearts
p3maketrumpheartsalone
p3maketrumpdiamonds
p3maketrumpdiamondsalone
p3maketrumpspades
p3maketrumpspadesalone
p3maketrumpclubs
p3maketrumpclubsalone
p3maketrumppass
p4maketrump
p4maketrumpalone
p4maketrumphearts
p4maketrumpheartsalone
p4maketrumpdiamonds
p4maketrumpdiamondsalone
p4maketrumpspades
p4maketrumpspadesalone
p4maketrumpclubs
p4maketrumpclubsalone
screwthedealer
t1maketrump
t1maketrumpalone
t1maketrumphearts
t1maketrumpheartsalone
t1maketrumpdiamonds
t1maketrumpdiamondsalone
t1maketrumpspades
t1maketrumpspadesalone
t1maketrumpclubs
t1maketrumpclubsalone
t2maketrump
t2maketrumpalone
t2maketrumphearts
t2maketrumpheartsalone
t2maketrumpdiamonds
t2maketrumpdiamondsalone
t2maketrumpspades
t2maketrumpspadesalone
t2maketrumpclubs
t2maketrumpclubsalone

PLAY EVENTS
-----------

p1turn1playcard1
p1turn1playcard2
p1turn1playcard3
p1turn1playcard4
p1turn1playcard5
p1turn1playcard1
p2turn1playcard2
p2turn1playcard3
p2turn1playcard4
p2turn1playcard5
p3turn1playcard1
p3turn1playcard2
p3turn1playcard3
p3turn1playcard4
p3turn1playcard5
p4turn1playcard1
p4turn1playcard2
p4turn1playcard3
p4turn1playcard4
p4turn1playcard5
p1turn1taketrick
p2turn1taketrick
p3turn1taketrick
p4turn1taketrick
t1turn1taketrick
t2turn1taketrick
t1turn1losetrick
t2turn1losetrick

p1turn2playcard1
p1turn2playcard2
p1turn2playcard3
p1turn2playcard4
p1turn2playcard5
p1turn2playcard1
p2turn2playcard2
p2turn2playcard3
p2turn2playcard4
p2turn2playcard5
p3turn2playcard1
p3turn2playcard2
p3turn2playcard3
p3turn2playcard4
p3turn2playcard5
p4turn2playcard1
p4turn2playcard2
p4turn2playcard3
p4turn2playcard4
p4turn2playcard5
p1turn2taketrick
p2turn2taketrick
p3turn2taketrick
p4turn2taketrick
t1turn2taketrick
t2turn2taketrick
t1turn2losetrick
t2turn2losetrick

p1turn3playcard1
p1turn3playcard2
p1turn3playcard3
p1turn3playcard4
p1turn3playcard5
p1turn3playcard1
p2turn3playcard2
p2turn3playcard3
p2turn3playcard4
p2turn3playcard5
p3turn3playcard1
p3turn3playcard2
p3turn3playcard3
p3turn3playcard4
p3turn3playcard5
p4turn3playcard1
p4turn3playcard2
p4turn3playcard3
p4turn3playcard4
p4turn3playcard5
p1turn3taketrick
p2turn3taketrick
p3turn3taketrick
p4turn3taketrick
t1turn3taketrick
t2turn3taketrick
t1turn3losetrick
t2turn3losetrick

p1turn4playcard1
p1turn4playcard2
p1turn4playcard3
p1turn4playcard4
p1turn4playcard5
p1turn4playcard1
p2turn4playcard2
p2turn4playcard3
p2turn4playcard4
p2turn4playcard5
p3turn4playcard1
p3turn4playcard2
p3turn4playcard3
p3turn4playcard4
p3turn4playcard5
p4turn4playcard1
p4turn4playcard2
p4turn4playcard3
p4turn4playcard4
p4turn4playcard5
p1turn4taketrick
p2turn4taketrick
p3turn4taketrick
p4turn4taketrick
t1turn4taketrick
t2turn4taketrick
t1turn4losetrick
t2turn4losetrick

p1turn5playcard1
p1turn5playcard2
p1turn5playcard3
p1turn5playcard4
p1turn5playcard5
p1turn5playcard1
p2turn5playcard2
p2turn5playcard3
p2turn5playcard4
p2turn5playcard5
p3turn5playcard1
p3turn5playcard2
p3turn5playcard3
p3turn5playcard4
p3turn5playcard5
p4turn5playcard1
p4turn5playcard2
p4turn5playcard3
p4turn5playcard4
p4turn5playcard5
p1turn5taketrick
p2turn5taketrick
p3turn5taketrick
p4turn5taketrick
t1turn5taketrick
t2turn5taketrick
t1turn5losetrick
t2turn5losetrick

END GAME EVENTS
---------------
t1win
t2win
t1winloner
t2winloner
t1lose
t2lose
t1loseloner
t2loseloner
t1wineuchre
t2wineuchre
t1loseeuchre
t2loseeuchre
t1win3tricks
t1win4tricks
t1win1point
t1win5tricks
t1win2points
t2win3tricks
t2win4tricks
t2win1point
t2win5tricks
t2win2points

t1lose0tricks
t1lose1trick
t1lose2tricks
t2lose0tricks
t2lose1trick
t2lose2tricks

p1winloner
p1winloner3tricks
p1winloner4tricks
p1winloner5tricks
p1winloner1point
p1winloner4points

p2winloner
p2winloner3tricks
p2winloner4tricks
p2winloner5tricks
p2winloner1point
p2winloner4points

p3winloner
p3winloner3tricks
p3winloner4tricks
p3winloner5tricks
p3winloner1point
p3winloner4points

p4winloner
p4winloner3tricks
p4winloner4tricks
p4winloner5tricks
p4winloner1point
p4winloner4points

p1loseloner
p1loseloner0tricks
p1loseloner1trick
p1loseloner2tricks

p2loseloner
p2loseloner0tricks
p2loseloner1trick
p2loseloner2tricks

p3loseloner
p3loseloner0tricks
p3loseloner1trick
p3loseloner2tricks

p4loseloner
p4loseloner0tricks
p4loseloner1trick
p4loseloner2tricks

