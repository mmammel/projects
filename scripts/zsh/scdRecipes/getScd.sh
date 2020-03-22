#!/bin/bash
OUTFILE="./output.txt"
rm $OUTFILE;
touch $OUTFILE;

for l in A B C D E F G H I J K L M N O P Q R S T U V W X Y Z; do
  curl -s -o - "http://www.scdrecipe.com/recipes/view-all-alpha/$l" | grep listing_panel | grep col-md-4 | awk -F '\&nbsp' '{for( i = 1; i <= NF; i++) print $i;}' | cut -d '>' -f2 | cut -d '<' -f1 | egrep "[A-z]" | sed "s/\&#39;/'/" >> $OUTFILE;

done
