#!/bin/bash

while read p ; do 
  gzcat *.gz | grep $p >> Final.txt;
done

