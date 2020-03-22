from string import ascii_lowercase

names = ["Jim", "Max Mammel", "Preston Franklin"]

def get_stringnumber( s ):
  result = 0
  s = s.lower()
  for l in s:
    if l.isalpha():
      result = result + ascii_lowercase.find(l)

  return result


small_names = [ n for n in names if get_stringnumber(n) < 100 ]

print(small_names)
print("There are {} names with number less than 100".format(len(small_names)))
