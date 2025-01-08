def solve(nums):
  if len(nums) == 1:
    return nums[0]
  snums = sorted(nums)
  idx = 0
  while True:
    if idx == len(snums) - 1 or snums[idx] != snums[idx+1]:
      return snums[idx]
    idx = idx+2
