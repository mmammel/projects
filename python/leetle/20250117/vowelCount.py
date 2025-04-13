import re

def solve(text):
  return len(re.sub(r"[^aeiou]", "", text.lower()))
