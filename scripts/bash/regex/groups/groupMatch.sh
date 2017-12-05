#!/bin/sh
examplefile="
foobarasset('1a/1b/1c.ext')
asset('2a/2b/2c.ext')bar
foobar
fooasset('3a/3b/3c.ext')bar
"
examplearr=(`echo "$examplefile" | grep asset | sed -e '/.*/s/^.*asset(\(.*\)).*$/\1/'`)
for el in ${examplearr[*]}; do
  echo "$el"
done
