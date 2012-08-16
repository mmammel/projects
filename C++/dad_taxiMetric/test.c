#include <string.h>
struct node {
int x;
int y;
}arr[100000];
int x[100000];
double dy[100000];

main(){
int xcmp(),ycmp();
int i,n,j ;
double s,t,low, limx;
double sum1,sum2,sumx;
int i1,i2,ix,m,mx,my,mx_v,my_v;
for( n=0 ; n<100000 ; n++ ){
if( scanf("%d %d",&arr[n].x,&arr[n].y) != 2 ) break;
x[n]=n;
}


/* sort array in y-order */
qsort( arr, n, sizeof(struct node), ycmp );

/* sort array of indexes in x-order */
qsort( x, n, sizeof(int), xcmp );

for( i=0 ; i<n ; i++ ){
 printf("%3d %3d ",arr[i].x, arr[i].y );
for( s=j= 0 ; j<n ; j++ ) s += abs(arr[i].x - arr[j].x) ;
for( t=j= 0 ; j<n ; j++ ) t += abs(arr[i].y - arr[j].y) ;
 printf("%.0f %.0f %.0f\n",s,t,s+t );
}
exit(0);

m = (n+1)/2 ;
mx = x[m] ; /* median x index */
my = m ; /* median y index  */

mx_v = arr[mx].x ; /* median x value  */
my_v = arr[my].y ; /* median y value  */

for( s=t=j= 0 ; j<n ; j++ ){
 s += abs(mx_v - arr[j].x) ;
 t += abs(my_v - arr[j].y) ;
}

low = s + t ; /* lower bound of the minimum */

sum1 = sum2 = 0;
i1=i2=0;

if ( arr[mx].y == my_v ) {
printf("%.0f\n",s+t);
exit(0);
}
if ( mx < m ){
for ( i1 = m-1 ; i1>= mx ; --i1 ){
sum1 -= (2*i1+2 -n)*(arr[i1+1].y - arr[i1].y);
dy[i1] = limx = sum1;
}

for( i2 = m+1 ; i2 < n && sum2 <= sum1 ; ++i2 ){
sum2 += (2*i2-n)*(arr[i2].y-arr[i2-1].y);
dy[i2] = sum2;
}

} else{
for( i2 = m+1 ; i2 <= mx ; ++i2 ){
sum2 += (2*i2-n)*(arr[i2].y-arr[i2-1].y);
dy[i2] = limx =  sum2;
}

for ( i1 = m-1 ; i1>=0 ; --i1 ){
sum1 -= (2*i1+2 -n)*(arr[i1+1].y - arr[i1].y);
dy[i1] = sum1;
}
}

sumx=0;
for( ix = m+1 ;  ix < n && sumx <= limx ; ++ix ){
sumx += ( 2*ix-n)* ( arr[x[ix]].x - arr[x[ix-1]].x );
if( x[ix] < i2 && x[ix] > i1  && sumx + dy[x[ix]] < limx )
	limx = sumx +dy[x[ix]] ;
}

sumx=0;
for( ix = m-1 ;  ix >= 0  && sumx <= limx ; --ix ){
sumx -= ( 2*ix+2-n)* ( arr[x[ix+1]].x - arr[x[ix]].x );
if( x[ix] < i2 && x[ix] > i1  && sumx + dy[x[ix]] < limx )
	limx = sumx + dy[x[ix]] ;
}
	

printf("%.0f\n",low+limx);
/*
for ( i1 = m-1 ; i1>= 0 ; --i1 ){
sum1 -= (2*i1+2 -n)*(arr[i1+1].y - arr[i1].y);
dy[i1] = sum1;
}

for( i2 = m+1 ; i2 < n ; ++i2 ){
sum2 += (2*i2-n)*(arr[i2].y-arr[i2-1].y);
dy[i2] = sum2;
}

for( i=0 ; i<n ; i++ ){
 printf("%3d %3d ",arr[i].x, arr[i].y );
for( s=j= 0 ; j<n ; j++ ) s += abs(arr[i].x - arr[j].x) ;
for( t=j= 0 ; j<n ; j++ ) t += abs(arr[i].y - arr[j].y) ;
 printf("%.0f %.0f %.0f\n",s,t,s+t );
}
sum1=0;
for( j= 0 ; j<n ; j++ ) sum1 += abs(arr[m].y - arr[j].y) ;
printf("\n%.0f\n\n",sum1);
for( i=0 ; i<n ; i++ ){
 printf("%3d %3d ",arr[i].x, arr[i].y );
for( t=j= 0 ; j<n ; j++ ) t += abs(arr[i].y - arr[j].y) ;
 printf("%.0f %.0f\n",t-sum1, dy[i] );
}

*/

}
int xcmp( a, b )
int *a,*b;
{
return arr[*a].x - arr[*b].x ;
}

int ycmp( a, b )
struct node *a,*b;
{
return a->y - b->y ;
}
