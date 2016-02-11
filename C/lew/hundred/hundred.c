#include <math.h>
#include <stdio.h>

main(argc,argv)
int argc;
char **argv;
{
int N;
int i,j,k;
double r,s,t,pi,d;

pi=2*acos(0.0);

N = atoi(argv[1]);

for( i=19 ; i>N ; i-=2 )printf("\n");
if( N%4 == 3 ) d=0 ; else  d = pi/N ;
for( i=0 ; i<=N/2 ; ++i ){

/*
printf("%2d %8.4f %8.4f\n", i, N*sin(2*i*pi/N) , N*cos(2*i*pi/N) );
printf("%8.4f\n",  N*sin(2*i*pi/N)  );
*/
printf("%8.4f %8.4f\n", N*sin(2*i*pi/N + d) , N*cos(2*i*pi/N + d ) );
}
}
