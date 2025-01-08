def solve(n):
  result = []
  for i in range(1,n+1):
    if i % 15 == 0:
      result.append("FizzBuzz")
    elif i % 5 == 0:
      result.append("Buzz")
    elif i % 3 == 0:
      result.append("Fizz")
    else:
      result.append(i)

  return result

print( solve(15) )
