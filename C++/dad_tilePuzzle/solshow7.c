char *map[3] =
{
"000000000000010000000011100000011111000001111100000111110000001110000000010"
,
"000100000000111000000111110000011111000001111100000111110000001110000000010"
,
"000000000000110000000111100000111111000011111100001111110000011110000000110"
};
#include "bits1.c"
#include "bits2.c"
#include "bits3.c"
#include "bits4.c"
#include "bits5.c"
#include "bits6.c"
#include "bits7.c"
#include "bits8.c"
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

char disp[20][40];
main( argc, argv)
int argc;
char **argv;
{
int i,j,k, b0,b1,b2;

for( i=0 ; i<20 ; i++)for( j=0 ; j<39 ; j++ ) disp[i][j] = ' ';
for( k=0 ; k<75 ; k++ ){
if( map[0][k] == '1' ){
disp[(k%10) * 2][(k/10) * 4] = '*';
}
if( map[1][k] == '1' ){
disp[k%10 * 2][k/10 * 4 + 2 ] = '*';
}
if( map[2][k] == '1' ){
disp[k%10 * 2 + 1][k/10 * 4] = '*';
}
}
scanf("%d",&i); /* eat nsol value */
for( i=1 ; i<19 ; i++ ){
if( scanf("%d",&j) != 1 ) break;

b0=b1=b2=0;
for( k=0 ; k<75 ; k++ ){
if( map[0][k] == '1' ){
 if((pbits[i][j][0] >> b0++ ) & 1 )
disp[(k%10) * 2][(k/10) * 4] = 'a'+i-1;
}
if( map[1][k] == '1' ){
 if((pbits[i][j][1] >> b1++ ) & 1 )
disp[k%10 * 2][k/10 * 4 + 2 ] = 'a'+i-1;
}
if( map[2][k] == '1' ){
 if((pbits[i][j][2] >> b2++ ) & 1 )
disp[k%10 * 2 + 1][k/10 * 4] = 'a'+i-1;
}
}
}
for( k=0 ; k<12 ; k++ )
printf("%s\n",disp[k]);
}
