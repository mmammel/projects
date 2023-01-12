#!/bin/bash

first=1;
last=0;
let page=1;
currLast=-1;
data="[";
temp="";

while [ 1 ]; do

  currLast=$last;

  temp=`curl -s 'https://race-find.com/us/races?query=&state=&date=&type=&race-page='$page'&per-page=15' | grep data-key | sed -e '/.*/s/^<tr data-geo="\([^"]*\)".*data-key="\([^"]*\)"><td>\(.*\)<\/td><td><\/td><td>\(.*\)<\/td><td><a.*href="\(.*\)".*target="_blank">\(.*\)<\/a><\/td><td>\(.*\)<\/td><td>\(.*\)<\/td><\/tr>$/[ "\1", "\2", "\3", "\4", "\5", "\6", "\7", "\8" ],/'`;

  last=`echo "$temp" | tail -1 | cut -d'"' -f6`;

  if [ $currLast = $last ]; then
    break;
  else
    data="$data\
$temp";
    let page=page+1;
  fi

done
 
echo "$data\
 [] ];"
