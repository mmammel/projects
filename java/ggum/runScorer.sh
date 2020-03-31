#!/bin/bash

make build;
java -classpath .:./commons-math3-3.6.1.jar Driver $1 $2
