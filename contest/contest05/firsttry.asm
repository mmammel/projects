info: MJMBot, Max Mammel, 630 706-4742, Chicago, IL

main:
  // go to the top of the tank
        mov r0, 0xdead
        mov [dir], 0
        getxy [coordx], [coordy]

gonorthloop:
  // check if I’m top of food and eat if so
        call checkfood
        cmp [coordy], 0
        je gowest
        travel [dir]
        jns blockednorth
        sub [coordy], 1
        jmp gonorthloop

checkfood:
   //see if I'm on food.  if so, eat
        sense r13
        js eatfunc
        jmp noFood
        
eatfunc:
   //eat, then return
        eat
        ret

noFood:

        // see if we're over a collection point and
        // release some energy
        energy  r10
        cmp     r10, 2000
        jl      notEnufEnergy
        sense   r5
        cmp     r5, 0xFFFF      // are we on a colleciton point?
        jne     notEnufEnergy
        release 100             // drain my energy by 100, but get 100 points, assuming
                                // that we're releasing on a collection point
        ret

notEnufEnergy:
  // move me
        ret
        
blockednorth:
   //blocked on the migration north to meet my friends.  try to go around
        travel 2
        js gonorthloop
        travel 3
        js gonorthloop
        travel 1
        js wentsouth
        jmp blockednorth
        
wentsouth:
        add [coordy], 1
        jmp blockednorth
        
gowest:
       getxy [coordx], [coordy]
       
gowestloop:
       call checkfood
       cmp [coordx], 0
       je south
       travel 3
       jns bumpwest
       sub [coordx], 1
       jmp gowestloop

bumpwest:
       mov r2, 3
       peek r2, friend
       jns gowestloop
       cmp r2, 0xff10
       jne attackwest
       mov r2, 3
       peek r2, settled
       jns gowestloop
       cmp r2, 0
       je gowestloop
       mov [settled], 1
       jmp south

attackwest:
       push 3
       call attack
       jmp gowestloop

south:
  // go to the top of the tank
        mov r0, 0xdead
        mov [dir], 1
        getxy [coordx], [coordy]

gosouthloop:
  // check if I’m top of food and eat if so
        call checkfood
        cmp [coordy], 39
        je goeast
        travel [dir]
        jns blockedsouth
        add [coordy], 1
        jmp gosouthloop

blockedsouth:
   //blocked on the migration south to meet my friends.  try to go around
        travel 3
        js gosouthloop
        travel 2
        js gosouthloop
        travel 0
        js wentsouth
        jmp blockedsouth
        
wentnorth:
        sub [coordy], 1
        jmp blockedsouth
        
goeast:
       getxy [coordx], [coordy]
       
goeastloop:
       call checkfood
       cmp [coordx], 69
       je main
       travel 2
       jns bumpeast
       add [coordx], 1
       jmp goeastloop

bumpeast:
       mov r2, 2
       peek r2, friend
       jns goeastloop
       cmp r2, 0xff10
       jne attackeast
       mov r2, 2
       peek r2, settled
       jns goeastloop
       cmp r2, 0
       je goeastloop
       mov [settled], 1
       jmp main

attackeast:
       push 2
       call attack
       jmp goeastloop

attack:
       pop r4
       poke r4, 0xffff
       ret

wait:
       mov [settled], 1
       getxy [coordx], [coordy]
       nop
       jmp wait
  
dir:        
        data { 0 }       // our initial direction

count:                   // our initial count of how far to move in the cur dir
        data { 0 }

coordx:
        data { 0 }

coordy:
        data { 0 }

settled:
        data { 0 }
friend:
        data { 0xff10 }
