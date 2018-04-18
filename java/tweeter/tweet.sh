#!/bin/bash

if [ ! -f ./Tweeter.class ]; then
  echo "Build it first!  Run make"
else
  java -cp .:./scribejava-apis-5.3.0.jar:./scribejava-core-5.3.0.jar Tweeter ~/MJMToolsMon.txt
fi
