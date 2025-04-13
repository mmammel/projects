def solve(nums, target):
  if len(nums) < 3:
    return 0
  elif len(nums) == 3:
    return nums[0] + nums[1] + nums[2]
  else:
    lowdiff = -1
    lowsum = 0
    sum = 0
    for i in range(len(nums) - 2):
      for j in range(i+1, len(nums) - 1 ):
        for k in range( j+1, len(nums) ):
          print(f"{i},{j},{k}")
          sum = nums[i]+nums[j]+nums[k]
          if lowdiff < 0 or abs(sum - target) < lowdiff:
            lowdiff = abs(sum - target)
            lowsum = sum
    return lowsum

print(solve([-1,2,1,-4,6,2,3,4,5,-12,7,8,9,7,4,3,2,6,5],1))
