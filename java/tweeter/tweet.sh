#!/bin/bash

BASEDIR=~/github/projects/java/tweeter

if [ ! -f $BASEDIR/Tweeter.class ]; then
  echo "Build it first!  Run make"
else
  java -cp $BASEDIR:$BASEDIR/scribejava-apis-5.3.0.jar:$BASEDIR/scribejava-core-5.3.0.jar Tweeter ~/MJMToolsMon.txt
fi
