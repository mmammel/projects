#include <stdio.h>
char blocks [10][10] ;

main(){
int i;
for( i=0 ; i<10 ; i=i+1 ) scanf( "%s", &blocks[i][0] );
/*
for( i=0 ; i<10 ; i=i+1 ) printf( "%10s\n", &blocks[i][0] );
*/
for( i=0 ; i<10 ; i=i+1 ) printf( "%c\n", blocks[i][0] );
for( i=0 ; i<10 ; i=i+1 ) printf( "%.10s\n", &(blocks[i][0]) );
}
