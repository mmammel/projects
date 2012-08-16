#!/bin/bash

{
  array=(`cat input.txt`)
  echo > output.txt;

  for line in ${array[@]}; do
    sleep 5;
    echo ${line} >> output.txt;
  done
} &
