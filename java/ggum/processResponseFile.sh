#!/bin/bash

count=1
touch $1.proc
sed -i '' "/.*/s/	/,/g" $1
for row in `cat $1`; do
  echo "$count,"`echo $row | tr '0123456' '1234567'` >> $1.proc
  count=$((count+1))
done
