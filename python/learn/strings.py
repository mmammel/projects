mystr = 'The quick brown fox jumps over the lazy dog'

print( mystr.count('z') )
print( mystr.index('fox') )
try:
  print( mystr.index('foo') )
except:
  print( 'Not found!' )

x = lambda a : a + 10

print( x(4) )

print( mystr.islower() )
print( 'asdf123'.islower() )
for y in mystr:
  print(y)

guesses = 'abc'

for l in set(mystr).intersection(guesses):
  print(l)
