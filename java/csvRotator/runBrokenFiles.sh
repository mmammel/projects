#!/bin/bash

FILES=(ls *.csv)

for file in ${FILES[@]}; do
echo "Processing $file ..."
java -classpath .:./jsonOrg.jar CSVRotator Ultradex.json $file ./rotated/$file
echo -n "...done. "
done

