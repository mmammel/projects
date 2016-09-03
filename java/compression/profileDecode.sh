#!/bin/bash

java -classpath .:./commons-codec-1.3.jar DecodeAndDeflate $1 | sed -e $'/.*/s/},{/}\\\n{/g'

