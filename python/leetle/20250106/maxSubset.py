def solve(nums):
  highRes = nums[0]
  sum = 0

  if len(nums) > 0:
    if len(nums) == 1:
      return nums[0]

    for p1 in range(0, len(nums)):
      sum = 0
      for p2 in range(p1, len(nums)):
        if p1 == p2:
          sum = nums[p1]          
        else:
          sum = sum + nums[p2]

        if sum > highRes:
          highRes = sum

  return highRes 

nums = [-2,-11]
print( solve(nums) )
