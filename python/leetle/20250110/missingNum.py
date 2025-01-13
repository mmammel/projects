def solve(nums):
  check = 1
  for n in sorted(nums):
    if n <= 0:
      continue
    elif n == check:
      check = check + 1

  return check
