#!/bin/bash

INFILE=ips.txt
AWKFILE=resultParse.ak

for ip in `cat $INFILE`; do
  LOCSTR=`curl -s "http://ipinfo.io/$ip" | tr -d '",{}' | sed -e '/.*/s/^[ ]*//' | awk -f resultParse.ak`;
  echo "$ip	$LOCSTR";
done

