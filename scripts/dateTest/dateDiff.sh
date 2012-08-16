#!/bin/bash

array1=(`date -d$1 +'%Y %m'`)
array2=(`date -d$2 +'%Y %m'`)
output='Not the previous month'
if [ ${array1[0]} -eq ${array2[0]} ]; then
  let diff=${array1[1]}-${array2[1]}
  if [ $diff -le 1 ]; then
    output='The same or previous month'
  fi
fi

echo $output
