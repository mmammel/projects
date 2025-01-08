def solve(nums):
  maxCount = -1
  maxOwner = -1
  counts = {}
  for n in nums:
    if n in counts:
      counts[n] = counts[n]+1
    else:
      counts[n] = 1

    if counts[n] > maxCount:
      maxCount = counts[n]
      maxOwner = n
  return maxOwner
