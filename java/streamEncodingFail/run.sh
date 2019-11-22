#!/bin/bash

if [ ! -f StreamEncoding.class ]; then
  javac StreamEncoding.java
fi

if [ "x$1" = "x" ]; then
  echo "usage: run.sh <inputfilename>"
  exit 1
fi

java -Dfile.encoding=UTF-8 StreamEncoding $1

