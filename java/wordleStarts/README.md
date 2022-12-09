# Wordle Start Word Finder

In the game "Quordle", which is just 4 simultaneous Wordle puzzles with 9 guesses instead of 6, I had been opening with guesses of ROUTE and SLAIN, as they cover all of the vowels and the common consonants R, S, T, L and N.  It was a pretty good strategy but still left the board pretty bare quite a bit, and I was always stuck trying to find a third word that covered a few more common consonants.  Because all of the vowels were already used this meant I couldn't guess 5 new letters.

I decided to write this program that uses the scrabble word list, and takes the following inputs:

* desired letters (e.g. aeiourstln)
* word length (always 5 for wordle)

and the program would find all sets of two words that uniquely covered the desired letters, one of which would be ROUTE and SLAIN.  With the program, I could also give it a list of 15 letters, instead of ten, and get all three word sets that uniquely covered the letters, for example:

aeiourstlnypghd

produces:

```
[adopt,gruel,shiny]
[agile,proud,synth]
[gains,hotly,prude]
[sharp,tiled,young]
...
```

and ~38,000 other combinations.

the algorithm is as follows:

1. Extract from the word file only words that have 5 unique letters, all in the desired letter list
2. Build a "Trie" of these words
3. Find all words in the Trie, when a word is found start the search anew, EXCEPT:
	* ignore letters in words already found
	* only look at words that begin after the most recent word found, alphabetically
4. When 3 words are found (or # desired letters/wordLen words) print the solution

## Build and Run

```
$> javac *.java

$> java WordleStarts -h
Find a set of words of length <wordlen> in the wordlist <wordfile> that uniquely contain the given letters <letters>
Usage: java WordleStarts -f <wordfile> [ -l <letters> -s <wordlen> ] | -h[elp]
   -f <wordfile> (required)
      the name of the file containing a lower-case word on each line
   -l <letters> (optional)
      the letter pool to use.  Default: aeiourstlnypghd
   -s <wordlen> (optional)
      the size of words desired.  Default: 5
   -h[elp]
      display this help and exit

$> java WordleStarts -f ./scrabble_words.txt | more
Filename: ./scrabble_words.txt
letters: aeiourstlnypghd
wordLen: 5
Desired Letter Pattern: ^[aeiourstlnypghd]{5}$
Uniqueness Pattern: ^.*(?:a.*a|e.*e|i.*i|o.*o|u.*u|r.*r|s.*s|t.*t|l.*l|n.*n|y.*y|p.*p|g.*g|h.*h|d.*d).*$
Added 2597 words with 12985 letters. Trie has 5445 nodes - 2597 words in Trie
[adept,giron,lushy]
[adept,groin,lushy]
[adept,gursh,noily]
[adept,gusli,horny]
[adept,gyron,hilus]
[adept,hings,loury]
[adept,hoing,surly]
[adept,hongi,surly]
[adept,horny,iglus]
[adept,horsy,lungi]
[adept,hours,lingy]
[adept,hours,lying]
[adept,hurls,yogin]
...

$> java WordleStarts -f ./scrabble_words.txt -l aeorst -s 3
Filename: ./scrabble_words.txt
letters: aeorst
wordLen: 3
Desired Letter Pattern: ^[aeorst]{3}$
Uniqueness Pattern: ^.*(?:a.*a|e.*e|o.*o|r.*r|s.*s|t.*t).*$
Added 42 words with 126 letters. Trie has 68 nodes - 42 words in Trie
[are,sot]
[ars,toe]
[art,oes]
[art,ose]
[ate,ors]
[ats,ore]
[ats,reo]
[ats,roe]
[ear,sot]
[eas,ort]
...
```
