
def distance( first_point, second_point ):
  x1 = first_point[0]
  x2 = second_point[0]
  
  y1 = first_point[1]
  y2 = second_point[1]

  xdiff = x2 - x1
  ydiff = y2 - y1
  
  dist = xdiff**2 + ydiff**2

  dist = dist**0.5
  return dist

print distance((1,1),(4,5))
