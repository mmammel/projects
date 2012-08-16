BEGIN { FS=","; }
{ printf( "%d -> %d\n",NF,length($NF) ); }

