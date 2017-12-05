#include <stdio.h>
void try();
typedef struct {
        int piece;
        int edge[3];
        int nabe[3];
} PLACE;
PLACE places[20] =
{
{ 0,{0,0,0},{16,10,19} },   /*  0 */
{ 0,{0,0,0},{17,11,10} },   /*  1 */
{ 0,{0,0,0},{18,12,11} },   /*  2 */
{ 0,{0,0,0},{19,16,12} },   /*  3 */
{ 0,{0,0,0},{10,13,18} },   /*  4 */
{ 0,{0,0,0},{11,14,13} },   /*  5 */
{ 0,{0,0,0},{12,17,14} },   /*  6 */
{ 0,{0,0,0},{13,15,17} },   /*  7 */
{ 0,{0,0,0},{14,18,15} },   /*  8 */
{ 0,{0,0,0},{15,19,16} },   /*  9 */
{ 0,{0,0,0},{ 4, 0, 1} },   /* 10 */
{ 0,{0,0,0},{ 5, 1, 2} },   /* 11 */
{ 0,{0,0,0},{ 6, 2, 3} },   /* 12 */
{ 0,{0,0,0},{ 7, 4, 5} },   /* 13 */
{ 0,{0,0,0},{ 8, 5, 6} },   /* 14 */
{ 0,{0,0,0},{ 9, 7, 8} },   /* 15 */

/* 16-19 are the fixed "edge places" */

{ 0,{ 6, 3,-2},{ 0, 3, 9} },   /* 16 */
{ 0,{ 4, 5, 4},{ 1, 6, 7} },   /* 17 */
{ 0,{-5, 2,-2},{ 2, 8, 4} },   /* 18 */
{ 0,{ 4, 1, 2},{ 3, 9, 0} },   /* 19 */
};
int pieces[16][3]=
{
{-6,4,-2},
{6,-2,3},
{-4,-3,-1},
{5,-2,-6},
{1,-6,2},
{5,-1,2},
{4,-5,2},
{2,-5,-1},
{1,-4,-3},
{2,-1,-1},
{-4,-3,-4},
{1,-5,-2},
{1,3,6},
{3,-4,-3},
{-6,-4,-2},
{6,5,4}
};
int e0[] = { 6,4,-5,4 };
int e1[] = { 3,5,2,1 };
int e2[] = { -2, 4, -2, 2 };

int start = 0;
 int next[16] = {
        10, /*  0 */
         4, /*  1 */
         6, /*  2 */
        12, /*  3 */
         3, /*  4 */
        -1, /*  5 */
         9, /*  6 */
         8, /*  7 */
        11, /*  8 */
        15, /*  9 */
         1, /* 10 */
        13, /* 11 */
         2, /* 12 */
        14, /* 13 */
         5, /* 14 */
         7, /* 15 */
};

/*
used[] is indexed by piece_num, and stores the place index
plus 1 of the place using the piece using the piece. 0 means
the piece is unused.
*/
int used[16];

main(){
int i,j;
try(start);
}

void try( place_index )
int place_index;
{
int i,j,k ;

printf("%3d",place_index);
if( place_index == -1 ){

/* success */

printf("\nFOUND:\n");
for( i=0 ; i<16 ; i++ ){
printf("%2d : %2d %2d %2d\n", used[i]-1,
places[used[i]-1].edge[0],
places[used[i]-1].edge[1],
places[used[i]-1].edge[2] );
}
} else {
for( i=0 ; i<16 ; i++ )  if ( used[i] == 0 ) {
/* try this piece in this place */
used[i] = place_index+1;

for( j = 0 ; j< 3 ; j++ ){

/* try each of 3 piece positions */

places[ place_index ].edge[j] = pieces[i][0];
places[ place_index ].edge[(j+1)%3] = pieces[i][1];
places[ place_index ].edge[(j+2)%3] = pieces[i][2];

/* check each edge match */

for( k = 0 ; k < 3 ; k++ ){
int nabe;

nabe =  places[place_index].nabe[k];
if( places[nabe].edge[k] != 0 &&
        places[nabe].edge[k] + places[place_index].edge[k] != 0 )

        /* piece and position don't match neighbor edges */

                break;

}
/* loop completion here means we have a piece fitting in this place with
   no mismatch, so try the next place index. Note that at most one position
   will match, but we try all 3
 */

        if( k == 3 )
                try( next[place_index] );
}

/* done with this piece */
        used[i]=0;

} /* close loop on pieces */

/* tried everything for this place iteration */
/* reset  place edge values */

places[ place_index ].edge[0] = 0;
places[ place_index ].edge[1] = 0;
places[ place_index ].edge[2] = 0;
}


}

