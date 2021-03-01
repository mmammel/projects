#!/bin/bash

CURR_IMAGES=$1

while [ 1 ]; do
  IMAGES=`curl "https://mars.nasa.gov/rss/api/?feed=raw_images&category=mars2020&feedtype=json&num=50&page=0&order=sol+desc&&&undefined" -s | sed -E 's/^.*"total_images":([0-9]+).*$/\1/g' | egrep "[0-9]"`

  if [ "x$IMAGES" != "x$CURR_IMAGES" ]; then
    echo "Images!!!";
    say "New Images Available!"
    echo "New Mars Images Available" | ~/github/projects/java/tweeter/tweet.sh
    CURR_IMAGES=$IMAGES;
#  else
#    echo "Nope."
  fi
  sleep 10;
done

