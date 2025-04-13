def solve(nums):
  snums = sorted(nums)
  seqcount = 0
  max = 0
  last = 0
  for n in range(len(snums)):
    if n == 0:
      last = snums[0]
      max = 1
      seqcount = 1
    else:
      if snums[n] == (last+1):
        seqcount = seqcount + 1
        if seqcount > max:
          max = seqcount
      elif snums[n] != last:
        seqcount = 1

      last = snums[n]
        
  return max
