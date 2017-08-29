BEGIN {
  FS=":"
}
/.*/{
  if( $1 == "BC" ) {
    bccommand = "echo \""$2"+"$3"\" | bc";
    bccommand | getline sum;
    print "bc: "sum;
  } else {
    sum = ($2+=0) + ($3+=0);
    print "nobc: "sum;
  }
}

