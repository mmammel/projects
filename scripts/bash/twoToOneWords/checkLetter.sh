#!/bin/bash

LETTER=$1

cat /Users/mmammel/github/projects/C++/texttwist/scrabble_words.txt | egrep "^[^"$LETTER"]*"$LETTER$LETTER"[^"$LETTER"]*"$LETTER"[^"$LETTER"]*$"
