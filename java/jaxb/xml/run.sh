#!/bin/bash

JARS=`ls *.jar | sed -e '/.*/s/^/\.\//' | tr '\n' ':'`

java -cp $JARS:. Driver ${1}
