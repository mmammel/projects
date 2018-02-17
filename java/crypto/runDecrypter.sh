#!/bin/bash

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/bin

if [ ! -f ./Encryption.class ]; then
  $JAVA_HOME/javac -classpath ./jasypt-1.5.jar Encryption.java
fi

$JAVA_HOME/java -classpath './commons-codec-1.5.jar:./commons-lang-2.6.jar:./jasypt-1.5.jar:.' Encryption $1 $2
