#!/bin/bash
examplefile="
asset('1a/1b/1c.ext')
asset('2a/2b/2c.ext')
asset('3a/3b/3c.ext')
"
examplearr=$(echo "$examplefile" | sed -e '/.*/s/asset(\(.*\))/\1/')
for el in ${examplearr[*]}; do
  echo "$el"
done
