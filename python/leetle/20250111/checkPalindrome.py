import re

def solve(s):
  normal = re.sub(r"[^A-Za-z0-9]","",s).lower()
  revnormal = normal[::-1]
  
  return normal == revnormal
