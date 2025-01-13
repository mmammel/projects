import pprint

def solve(grid):
  color = 1
  for i, row in enumerate(grid):
    for j, element in enumerate(row):
      if element == 1:
        color = color + 1
        doColoring(grid, i,j,color)
  pprint.pprint( grid, width=40 )
  return color - 1

def doColoring( grid, r, c, color ):
  grid[r][c] = color
  if r > 0:
    if grid[r-1][c] == 1:
      doColoring( grid, r - 1, c, color)
  if c < len(grid[r]) - 1:
    if grid[r][c+1] == 1:
      doColoring( grid, r, c + 1, color)
  if r < len(grid) - 1:
    if grid[r+1][c] == 1:
      doColoring( grid, r + 1, c, color)
  if c > 0:
    if grid[r][c - 1] == 1:
      doColoring( grid, r, c - 1, color)

print(solve([
[1,1,0],
[0,1,0],
[1,0,1]]))

print(solve([
 [1,1,0,0,1,1,0,1,0,0],
 [1,0,0,1,0,0,1,0,0,0],
 [0,0,0,1,1,0,1,0,0,0],
 [0,0,1,0,1,0,0,0,1,1],
 [1,0,1,0,0,0,0,0,1,1],
 [1,0,0,0,0,0,0,0,0,0],
 [0,0,0,0,0,0,1,0,0,0],
 [0,1,0,1,0,0,0,0,1,0],
 [0,1,1,1,1,0,1,0,1,1],
 [1,0,0,1,1,0,1,0,0,0]
]))
