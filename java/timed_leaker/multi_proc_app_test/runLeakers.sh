#!/usr/bin/bash

CLASSPATH=`cygpath -p -w "/c/Program Files/VERITAS/Application Saver/products/appsaver/collector/jar/tfj-runtime.jar"`

java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask0 $@ > leaker0.out &
java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask1 $@ > leaker1.out &
java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask2 $@ > leaker2.out &
java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask3 $@ > leaker3.out &
java -Dtracebackrt.Log.file=`cygpath -p -w "/c/appsaver_j-%d.log"` -Dtracebackrt.Log.level=DEBUG-LOW -classpath "${CLASSPATH};."  LeakerTask4 $@ > leaker4.out &


