from pprint import pprint

directions = [
  [1,0], [0,1], [-1,0], [0,-1]
]

rows,cols,spaces = 0,0,0
firstX,firstY = -1,-1
numPaths = 0

def solve(obstacleGrid):
  global rows, cols, firstX, firstY, spaces, numPaths
  rows = len(obstacleGrid)
  cols = len(obstacleGrid[0])
  spaces = 0
  # find first 0
  for i in range(rows):
    for j in range(cols):
      if obstacleGrid[i][j] == 0:
        spaces += 1
        if firstX == -1:
          firstX = i
          firstY = j

  if firstX != -1:
    numPaths = 0
    solveInner(firstX,firstY,obstacleGrid,0)

  return numPaths


def solveInner(x,y,thegrid, seen):
  global numPaths
  thegrid[x][y] = 2 # visited
  seen += 1
  pprint(thegrid)
  if seen == spaces:
    numPaths += 1
  
  for d in directions:
    nextX = x + d[0]
    nextY = y + d[1]
    if nextX < rows and nextY < cols and nextX >= 0 and nextY >= 0:
      if thegrid[nextX][nextY] == 0:
        solveInner(nextX,nextY,thegrid,seen)

  thegrid[x][y] = 0 # unvisited
  seen -= 1
    
def createGrid( rows, cols):
  grid = [[0] * rows for _ in range(cols)]
  return grid

#for i in range(1,10):  
#  print(f"(2,{i}): {solve(createGrid(2,i))}")

print(solve([
  [0,0,0,0],
  [0,1,0,0],
  [0,0,0,0],
  [0,0,0,0],
]))
