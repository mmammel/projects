from pprint import pprint
import sys

def solve(s):
  n = len(s)
  if n == 0:
    return ""

  dp = [""] * n

  for i in range(n):
    dp[i] = s[i]

  for i in range(n):
    for j in range(i):
      if s[j] < s[i] and len(dp[j]) + 1 >= len(dp[i]):
        dp[i] = dp[j] + s[i]

  pprint(dp)

solve(sys.argv[1])
