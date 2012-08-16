#!/usr/bin/bash

CLASSPATH=`cygpath -p -w "/c/Program Files/VERITAS/Application Saver/products/appsaver/collector/jar/tfj-runtime.jar"`

java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask $@ > leaker.out


