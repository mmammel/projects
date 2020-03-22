import getpass

guesses = ''
secret_word = 'elephant'
misses = 0

raster = """________
|//     
|/      
|       
|       
|       
|       
|_______"""

the_hangman = [ [13, '|'], [ 22, 'O' ], [ 31, '|'], [30, '/'], [ 32, '\\'], [40, '|'], [48, '/'], [50, '\\'], [56, '-'], [60,'-'] ]

def get_display_word():
  display = ''
  for x in secret_word:
    if guesses.find(x) >= 0:
      display = display + x
    elif x == ' ':
      display = display + ' '
    else:
      display = display + '-'
  return display

def do_miss(n):
  drawchar = the_hangman[ n ][1]
  index = the_hangman[ n ][0]
  newraster = ''
  for c in range(len(raster)):
    if c == index:
      newraster = newraster + drawchar
    else:
      newraster = newraster + raster[c]

  return newraster

def get_postfix(n):
  if n == 1:
    return ''
  else:
    return 'es'

secret_word = getpass.getpass(prompt='Enter the secret word: ')

print(get_display_word())

while get_display_word() != secret_word and misses < 10:
  guess = raw_input("Enter a guess [{} miss{} left]: ".format(10-misses, get_postfix(10 - misses)))
  if not guess.isalpha():
    print("It has to be a letter, duh...")
    continue
  elif not guess.islower():
    print("I said lowercase!")
    continue
  elif len(guess) != 1:
    print("One letter at a time there, chief")
    continue
  elif guesses.find(guess) >= 0:
    print("You already guessed that!")
    continue

  guesses = guesses + guess
  if secret_word.find( guess ) >= 0:
    print(raster)
    print("Got one!")
  else:
    raster = do_miss(misses)
    misses = misses + 1
    print(raster)
    print("Oooh, so sorry...")

  print(get_display_word())

if get_display_word() == secret_word:
  print("You win!")
else:
  print("Womp womp...you lose. The word was \"{}\"".format(secret_word)) 
  
