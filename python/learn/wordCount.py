word_dict = {}
text_sample = raw_input("Enter some text: " )

wordList = text_sample.split()

for w in wordList:
  if w in word_dict:
    count = word_dict[w]
    count = count + 1
    word_dict[w] = count
  else:
    word_dict[w] = 1

print( word_dict )
