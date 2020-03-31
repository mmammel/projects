BEGIN { 
  FS="\t"i;
  print "  ITEM PARAMETER FILE";
  print "  ***** TODAY'S DATE: 10/19/2014 TODAY'S TIME: 18: 7:45 *****\n\n";
  count=1;
}
{
  printf( " ITEM#  %d INITIAL=  2.11830139 DELTA= %s DSTD=  0.55193074 ALPHA= %s ASTD=  0.07731848 CORR= -0.07934491\n", count++, $2, $1);
  print( " THRESHOLD# 1 INITIAL=  0.00000000 TAU=  0.00000000 TAUSTD=  0.00000000" );
  for( i = 3; i <= NF; i++ ) {
    printf( " THRESHOLD# %d INITIAL= 0.00000000 TAU= %s TAUSTD=  0.000000000\n", (i - 1), $i );
  }
}
END {
  print("");
}

