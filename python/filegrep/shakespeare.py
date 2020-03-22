f = open("input.txt", "r")
out_file = open("output.txt", "w")
for x in f:
  l = x.lower()
  if l.find("love") >= 0:
    out_file.write(x)

out_file.close()
