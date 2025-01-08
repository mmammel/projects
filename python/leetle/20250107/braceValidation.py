import re

def solve(s):
  news = re.sub(r"[^\[\]\(\)\{\}]", "", s)

  while True:
    loops = re.sub(r"\(\)|\[\]|\{\}", "", news)

    if (slen := len(loops)) == 0 or loops == news:
      break

    news = loops

  return (slen == 0)