def solve(nums, target):
  res = []
  for p1 in range(0,len(nums)-1):
    for p2 in range(p1+1, len(nums)):
      if nums[p1] + nums[p2] == target:
        res.append( p1 )
        res.append( p2 )

  return res

print( solve( [2, 7, 11, 15], 9) )
