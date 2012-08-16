info: SampleBot, Carey Nachenberg, 310 449-4827, Santa Monica, CA

main:
  // select a random direction and distance to move
        rand    [dir], 4
        rand    [count], 10
        add     [count], 1
        mov r13, 2
        mov r12, 3
        call testfunc

loop:
  // check if I’m top of food and eat if so
        sense   r2
        jns     noFood
        eat

noFood:

        // see if we're over a collection point and
        // release some energy
        energy  r0
        cmp     r0, 2000
        jl      notEnufEnergy
        sense   r5
        cmp     r5, 0xFFFF      // are we on a colleciton point?
        jne     notEnufEnergy
        release 100             // drain my energy by 100, but get 100 points, assuming
                                // that we're releasing on a collection point

notEnufEnergy:
  // move me
        cmp     [count], 0      // moved enough in this direction; try a new one
        je      newDir
        travel  [dir]
        jns     newDir    // bumped into another org or the wall
        sub     [count], 1

        jmp     loop

newDir:
        rand    [dir], 4        // select a new direction
        rand    [count], 10 // select a new count between 0 and 9
        jmp     loop
  
testfunc:
        push r13 //param1 [SP+1]
        push r12 //param2 [SP]

        travel [SP+1]
        mov r13, [SP]
        call testfunc2
        pop r0;
        pop r0;
        ret
        
testfunc2:
        push r13 //param1 [SP]
        travel [SP]
        pop r0;
        ret      

dir:        
        data { 0 }       // our initial direction

count:                   // our initial count of how far to move in the cur dir
        data { 0 }

