#!/bin/bash

JAVA_FLAGS="-classpath .;C:\javaLibs\xerces-2_7_1\xercesImpl.jar;C:\javaLibs\xerces-2_7_1\xmlParserAPIs.jar;C:\javaLibs\xalan-j_2_7_0\xalan.jar"

java ${JAVA_FLAGS} GenericParser "$1"
