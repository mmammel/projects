#!/bin/bash

callfunc()
{
  echo "$1: $FOO,$BAR"
}

for line in `cat input.txt`; do
  FOO=`echo $line | cut -d"," -f1`
  BAR=`echo $line | cut -d"," -f2`

  callfunc JOMAMA
  callfunc YOMAMA

done
