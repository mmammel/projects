from pprint import pprint
import sys

def solve(s):
  longest,temp = "",""
  l,r = 0,0

  if len(s) == 1:
    return s

  for j in range(len(s) - 1):
    r = j + 1
    if s[j] == s[r]:
      temp = ""
      l = j 
    else:
      temp = s[j]
      l = j - 1

    while l >= 0 and r < len(s):
      if s[l] == s[r]:
        temp = s[l] + temp + s[r]
        l -= 1
        r += 1
      else:
        break

    if len(temp) > len(longest):
      longest = temp
  return longest

print(solve(sys.argv[1]))
