#!/bin/bash

CURR_IMAGES=$1

while [ 1 ]; do
  IMAGES=`ls ./testFiles/*.txt | wc -l | sed -E "/.*/s/[\t ]+//g"`

  if [ "x$IMAGES" != "x$CURR_IMAGES" ]; then
    echo "Images!!!";
    say "New Mars Images Available!"
    echo "New Mars Images Available" | ~/github/projects/java/tweeter/tweet.sh
    CURR_IMAGES=$IMAGES;
  fi
  sleep 10;
done

