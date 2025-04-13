def solve(matrix):
  n = len(matrix)
  array = [[0] * n for _ in range(n)]
  for i, row in enumerate(matrix):
    for j, element in enumerate(row):
      array[j][n-i-1] = element

  return array
