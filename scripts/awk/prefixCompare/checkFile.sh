#!/bin/bash

if [ `cat $1 | awk '{ print substr( $0, 0, 9 ) }' | sort -u | wc -l` -gt 1 ]; then
  echo "Bad File!!";
else
  echo "Good File!!";
fi

