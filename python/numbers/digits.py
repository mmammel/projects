import sys
import math

n = int(sys.argv[1])
print(f"Input: {n}")

digits = math.floor(math.log(n,10))
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

print(f"Num: {n}, RevNum: {revnum}")
