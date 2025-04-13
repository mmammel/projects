def solve(obstacleGrid):
  m, n = len(obstacleGrid), len(obstacleGrid[0])
  dp = [0] * n
  dp[0] = 1
  for i in range(m):
    for j in range(n):
      if obstacleGrid[i][j] == 1:
        dp[j] = 0
      elif j > 0:
        dp[j] += dp[j-1]

      print(f"({i},{j}): {dp}")
  return dp[-1]

def solve2(obstacleGrid):
    m, n = len(obstacleGrid), len(obstacleGrid[0])
    dp = [[0] * n for _ in range(m)]
    for i in range(m):
        for j in range(n):
            if obstacleGrid[i][j] == 1:
                dp[i][j] = 0
            elif i == 0 and j == 0:
                dp[i][j] = 1
            elif i == 0:
                dp[i][j] = dp[i][j-1]
            elif j == 0:
                dp[i][j] = dp[i-1][j]
            else:
                dp[i][j] = dp[i-1][j] + dp[i][j-1]
    return dp[-1][-1]

print(solve([
  [0,0,0,0],
  [0,1,0,0],
  [0,0,0,0],
  [0,0,0,0],
]))
