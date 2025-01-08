def solve(nums):
  i = 0
  for n in sorted(nums):
    if n != i:
      return i
    else:
      i = i+1

  return i
