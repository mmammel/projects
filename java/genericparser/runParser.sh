#!/bin/bash

JAVA_FLAGS="-classpath .:./xercesImpl.jar:./xalan.jar"

java ${JAVA_FLAGS} GenericParser "$1"
