#!/bin/bash

javac *.java

echo "Using .equals():"
time java DontUseIntern input.txt
echo ""
echo "Using ==:"
time java UseIntern input.txt
