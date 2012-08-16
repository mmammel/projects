#include <string.h>
#include <stdio.h>
#include <stdlib.h>

/* input array of x,y grid values */
struct node {
int x;
int y;
}arr[100000];

/* array of indexes to be sorted by x-value */
int x[100000];

/* array of "delta" values in y-component of cost */
double dy[100000];

extern int xcmp( const void *, const void * ),ycmp( const void *, const void *);
main(){
int i,n,j ;
double s,t,low, limx;
double sum1,sum2,sumx;
int i1,i2,ix,m,mx,my,mx_v,my_v;

scanf("%d",&n);

if( n == 1 ){
printf("0\n"); exit(0);
}


for( i=0 ; i<n ; i++ ){
if( scanf("%d %d",&arr[i].x,&arr[i].y) != 2 ) break;
x[i]=i;
}

/* sort array in y-order */
qsort( arr, n, sizeof(struct node), ycmp );

/* sort array of indexes in x-order */
qsort( x, n, sizeof(int), xcmp );


m = n/2 ;
mx = x[m] ; /* median x index */
my = m ; /* median y index  */

mx_v = arr[mx].x ; /* median x value  */
my_v = arr[my].y ; /* median y value  */


/* cost is sum of xcost and ycost, and these take their
  minimum values at the median
*/

for( s=t=j= 0 ; j<n ; j++ ){
 s += abs(mx_v - arr[j].x) ;
 t += abs(my_v - arr[j].y) ;
}

low = s + t ; /* lower bound of the minimum */

sum1 = sum2 = 0;
i1=i2=0;

/* check for "direct hit" on absolute minimum */
if ( arr[mx].y == my_v ) {
printf("%.0f\n",s+t);
exit(0);
}

/* Evaluate needed portion of array of "deltas" in ycost value */

/* limx is the amount above the absolute minimum of
the cost of the node with median x-value. It equals the
delta of the ycost of that node, and no node with an
xcost delta greater than this can be a minimum node
*/

if ( mx < m ){
for ( i1 = m-1 ; i1>= mx ; --i1 ){
sum1 -= (2*i1+2 -n)*(double)(arr[i1+1].y - arr[i1].y);
dy[i1] = limx = sum1;
}

for( i2 = m+1 ; i2 < n && sum2 <= sum1 ; ++i2 ){
sum2 += (2*i2-n)*(double)(arr[i2].y-arr[i2-1].y);
dy[i2] = sum2;
}

} else{
for( i2 = m+1 ; i2 <= mx ; ++i2 ){
sum2 += (2*i2-n)*(double)(arr[i2].y-arr[i2-1].y);
dy[i2] = limx =  sum2;
}

for ( i1 = m-1 ; i1>=0 ; --i1 ){
sum1 -= (2*i1+2 -n)*(double)(arr[i1+1].y - arr[i1].y);
dy[i1] = sum1;
}
}

/* search nodes with xcost <= limx for the minimum cost */

sumx=0;
for( ix = m+1 ;  ix < n && sumx <= limx ; ++ix ){
sumx += ( 2*ix-n)* (double)( arr[x[ix]].x - arr[x[ix-1]].x );
if( x[ix] < i2 && x[ix] > i1  && sumx + dy[x[ix]] < limx )
       limx = sumx +dy[x[ix]] ;
}

sumx=0;
for( ix = m-1 ;  ix >= 0  && sumx <= limx ; --ix ){
sumx -= ( 2*ix+2-n)* (double)( arr[x[ix+1]].x - arr[x[ix]].x );
if( x[ix] < i2 && x[ix] > i1  && sumx + dy[x[ix]] < limx )
       limx = sumx + dy[x[ix]] ;
}


/* This is it! */
printf("%.0f\n",low+limx);

}
int xcmp( const void *a, const void *b )
{
return arr[*(int *)a].x - arr[*(int *)b].x ;
}

int ycmp( const void *a, const void *b )
{
return ((struct node *)a)->y - ((struct node *)b)->y ;
}

