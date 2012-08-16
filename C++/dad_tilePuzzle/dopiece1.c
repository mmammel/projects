#include <stdio.h>
#include "place1.c"
#include "bits1.c"
#include "bits2.c"
#include "bits3.c"
#include "bits4.c"
#include "bits5.c"
#include "bits6.c"
#include "bits7.c"
#include "symbits8.c" /* Truncated set */
#include "bits9.c"
#include "bits10.c"
#include "bits11.c"
#include "bits12.c"
#include "bits13.c"
#include "bits14.c"
#include "bits15.c"
#include "bits16.c"
#include "bits17.c"
#include "bits18.c"
int bnum[19] = {
0,
BNUM8,
BNUM9,
BNUM10,
BNUM11,
BNUM12,
BNUM13,
BNUM14,
BNUM15,
BNUM16,
BNUM17,
BNUM18,
BNUM1,
BNUM2,
BNUM3,
BNUM4,
BNUM5,
BNUM6,
BNUM7,
};
int (*pbits[19])[3] = {
0,
bits8,
bits9,
bits10,
bits11,
bits12,
bits13,
bits14,
bits15,
bits16,
bits17,
bits18,
bits1,
bits2,
bits3,
bits4,
bits5,
bits6,
bits7,
};
int board[19][3];
int bdex[19];
int used[19];
unsigned int nsol;
int init=0;
int piece;

main(argc,argv)
int argc;
char **argv;
{
int i;
switch( argc ){
case 2:
	piece = atoi( argv[1] );
	if ( piece < 1 || piece > 18 ) usage( argv[0] );
	break;
case 22:
	piece = atoi( argv[2] );
	if ( strcmp(argv[1],"-i") || piece < 1 || piece > 18 ) usage( argv[0]);
	nsol = atoi(argv[3]);
	for( i=1 ; i<19 ; i++ ) bdex[i] = atoi(argv[i+3]);
	init = 1;
	break;
default: usage( argv[0] );
}
try(1);
printf("%d\n",nsol);
}
try(n)
{
int i,m;
int nplace; /* Advances from 0 to 80 */


if( n == 19 ){
if( init ){ --nsol ; init=0; }
if( ++nsol % 10000 == 0 ){
printf("%8d ",nsol);
for( i=1 ; i<19 ; i++ )printf("%d ",bdex[i]);
printf("\n");
fflush(stdout);
}
return;
}
for(
nplace=0;
(board[n-1][place[nplace][0]]&place[nplace][1]) && nplace < 81 ;
++nplace );

/*
printf("%8d : %d : %d : ",nsol, n, nplace);
printf("%08x %08x %08x\n",board[n-1][0],board[n-1][1],board[n-1][2] );
*/

for( m=1 ; m<19 ; m++ ){
if( m == 19 )return;
if( used[m] == 1 ) continue;

if( n == 1 ) m = piece;

for( i=0 ; i<bnum[m] ; i++ ){
if( ( pbits[m][i][place[nplace][0]] & place[nplace][1] ) &&
    ( board[n-1][0] & pbits[m][i][0] ) == 0 &&
    ( board[n-1][1] & pbits[m][i][1] ) == 0 &&
    ( board[n-1][2] & pbits[m][i][2] ) == 0 ){


if( init && bdex[m] != i ) continue;
board[n][0] = board[n-1][0] | pbits[m][i][0] ;
board[n][1] = board[n-1][1] | pbits[m][i][1] ;
board[n][2] = board[n-1][2] | pbits[m][i][2] ;
bdex[m] = i;
used[m] = 1;
try( n+1 );
}
}
if( n == 1 ) break;
used[m] = 0;
}
}

usage( progname )
char *progname;
{
printf("usage: %s [ -i ] piece_num\n\n",progname);
printf("       -i indicates init option ; %s reads a progress\n",progname);
printf("       output line from stdin and continues the enumeration.\n\n");
printf("       piece_num is an integer from 1 to 18.\n");
exit(0);
}
