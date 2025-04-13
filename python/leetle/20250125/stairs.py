def solve(n):
  return solve_inner(n)

def solve_inner(n):
  if n <= 0:
    return 0
  elif n < 3:
    return n
  else:
    return solve_inner(n - 1) + solve_inner(n - 2)
