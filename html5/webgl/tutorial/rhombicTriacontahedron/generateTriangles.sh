#!/bin/bash

i=0;
RESULT="[";
while [ $i -lt 32 ]; do
 RESULT="$RESULT$i,";
 let i=i+1;
 RESULT="$RESULT$i,";
 let i=i+1;
 RESULT="$RESULT$i,";
done

echo $RESULT
