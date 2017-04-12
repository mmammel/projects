#!/bin/bash
OUTPUT_FILE=beanies.txt
BEANIES=(`curl -s http://www.tycollector.com/boos/boos-roster.htm | grep roster | cut -d"\"" -f4 | cut -d"\"" -f1 | egrep "."`)

rm -rf $OUTPUT_FILE

for beanie in ${BEANIES[@]}; do
  NAME=`curl -s http://www.tycollector.com/boos/$beanie | grep itemname | cut -d">" -f2 | cut -d"<" -f1`;
  TYPE=`curl -s http://www.tycollector.com/boos/$beanie | grep -A1 Animal | grep 70 | cut -d">" -f2 | cut -d"<" -f1`;
  BIRTHDAY=`curl -s http://www.tycollector.com/boos/$beanie | grep -A1 Birthday | grep 70 | cut -d">" -f2 | cut -d"<" -f1`;

  echo "$NAME ($TYPE) : $BIRTHDAY" >> $OUTPUT_FILE
done
