def solve(s):
  longest = ""
  candidate = ""
  for i in range(len(s)):
    candidate = s[i]
    for j in range(i+1,len(s)):
      if s[j] in candidate:
        break
      else:
        candidate = candidate + s[j]
    if len(candidate) > len(longest):
      longest = candidate
  print(longest)

solve("abcabcabcd")
