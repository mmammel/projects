import sys
import math

def solve(n):
  n = abs(n)
  
  if n < 10:
    return True
    
  digits = math.floor(math.log(n,10))
  print(f"Digits: {digits}")
  num = n
  revnum = 0
  for i in range(digits, -1, -1):
    if i == 0:
      dig = math.floor(num)
    else:
      dig = math.floor(num / math.pow(10,i))
    print(dig)
    num = num % math.pow(10,i)
    revnum = math.floor(revnum + (dig * math.pow(10, digits - i)))
    print( f"Num: {num}, rev: {revnum}")

  return (revnum == n)

print(solve(121))
