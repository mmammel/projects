#!/bin/bash

FILENAME=$1
shift
WORDS=$@
TMPFILE=_temp.txt

echo "Lines before: `wc -l $FILENAME`"
for word in ${WORDS[@]}; do
  cat $FILENAME | grep -v $word > $TMPFILE
  mv $TMPFILE $FILENAME
done
echo "Lines after: `wc -l $FILENAME`"


