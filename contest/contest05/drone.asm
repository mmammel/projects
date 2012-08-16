info: DRONE, Max Mammel, 630 706-4742, Chicago, IL

//[1337] = badfood_lo
//[1137] = badfood_hi
premain:
  rand r8, 20 //distance
main:
  rand r9, 2  //direction
  mov r10, 0  //counter
mainloop:
  cmp r10, r8
  je hmove
  travel r9
  jns hmove
  call eatfood
  add r10, 1
  jmp mainloop
hmove:
  rand r9, 2
  add r9, 2
  rand r8, 10 //distance
  mov r10, 0  //counter
hmoveloop:
  cmp r10, r8
  je main
  travel r9
  jns main
  call eatfood
  add r10, 1
  jmp hmoveloop

eatfood:
  sense r0
  cmp r0, 0xFFFF
  je on_collect
  cmp r0, 16
  jg food_hi
  cmp r0, 1
  jl return  //not food
food_lo:
  sub r0, 1
  mov r1, 1
  shl r1, r0     //r1 contains the bit id of the food
  test r1, [1337]
  je return      //this is probably good food
  jmp do_eat
food_hi:
  sub r0, 17
  mov r1, 1
  shl r1, r0     //r1 contains the bit id of the food
  test r1, [1737]
  je return      //this is probably good food
  jmp do_eat
do_eat:
  eat
  jns find_collection
  jmp return
on_collect:
  mov [1237], 0
  getxy r10, r11
  shl r10, 8
  or [1237], r10
  or [1237], r11
  release 2000
return:
  ret

find_collection:
  cmp [1237], 0x4528
  je return
  getxy r0, r1
  mov r10, 0xFF00
  and r10, [1237]
  shr r10, 8
  mov r11, 0x00FF
  and r11, [1237]
hfind:
  mov r2, 1
  cmp r10, r0
  je vfind
  jl wfind
  jg efind
wfind:    
  travel 3
  jns fstuck
  sub r0, 1
  jmp hfind
efind:
  travel 2
  jns fstuck
  add r0, 1
  jmp hfind
vfind:
  mov r2, 0
  cmp r11, r1
  je check_found
  jl nfind
  jg sfind
sfind:
  travel 1
  jns fstuck
  add r1, 1
  jmp vfind
nfind:
  travel 0
  jns fstuck
  sub r1, 1
  jmp vfind
check_found:
  getxy r0, r1
  cmp r10, r0
  jne hfind
  cmp r11, r1
  jne vfind
  release 40000
  jmp return
fstuck:
  rand r2, 4
  travel r2
  rand r2, 4
  travel r2
  rand r2, 4
  travel r2
  rand r2, 4
  travel r2
  jmp find_collection

drone:
  data{ 0xFFFF 0xFFFF 0xFFFF }