def solve(nums, k):
  retVal = []
  if len(nums) == 0:
    retVal.append(0)
  elif len(nums) <= k:
    retVal.append(max(nums))
  else:
    for n in range(0,len(nums) - k + 1):
      retVal.append(max(nums[n:(n+k)]))

  return retVal

print(solve([1,3,-1,-3,5,3,6,7],3))
