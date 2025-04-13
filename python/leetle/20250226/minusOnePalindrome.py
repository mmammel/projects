def solve(s):
  result = True
  for i in range(0,len(s) - 1):
    test = s[:i] + s[i+1:]
    if not isPalindrome(test):
      print(f"Nope -> {test}")
    else:
      print(f"Yep -> {test}")

def isPalindrome(s):
  if len(s) == 1:
    return True
  elif len(s) == 0:
    return False
  else:
    start = 0
    end = len(s) - 1
    result = True
    while start <= end:
      if s[start] != s[end]:
        result = False
        break
      start = start + 1
      end = end - 1
  return result

solve("foobar")
solve("abca")
solve("lookmaiwonatoyotanowiamkool")
solve("lookmaxiwonatoyotanowiamkool")
