def solve(n):
  result = []
  last = []
  if n >= 1:
    for idx in range(n):
      if idx == 0:
        result.append([1])
      elif idx == 1:
        result.append([1,1])
        last = [1,1]
      else:
        temp = [1]
        for i in range(len(last) - 1):
          temp.append(last[i] + last[i+1])
        temp.append(1)
        result.append(temp)
        last = temp
  return result
