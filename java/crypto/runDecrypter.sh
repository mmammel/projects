#!/bin/bash

if [ ! -f ./Encryption.class ]; then
  javac -classpath ./jasypt-1.5.jar Encryption.java
fi

java -classpath './commons-codec-1.5.jar:./commons-lang-2.6.jar:./jasypt-1.5.jar:.' Encryption $1 $2
