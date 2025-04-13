from pprint import pprint
import sys

def solve(s):
  result = [[""] * len(s) for _ in range(len(s))]

  for i in range(len(s) - 1, -1, -1):
    for j in range(len(s)):
      if i == j:
        result[i][j] = s[i]
      elif i < j:
        if s[i] == s[j]:
          result[i][j] = s[i] + result[i+1][j-1] + s[j]
        else:
          if len(result[i+1][j]) > len(result[i][j-1]):
            result[i][j] = result[i+1][j]
          else:
            result[i][j] = result[i][j-1]

  pprint(result[0][len(s) -1])



solve(sys.argv[1])
