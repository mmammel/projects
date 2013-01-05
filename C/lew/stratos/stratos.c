#include <math.h>

double density[] =
{ 1.0,
0.738479,
0.532812,
0.374133,
0.246170,
0.152229,
0.094137,
0.057894,
0.035529,
0.021950,
0.013650,
0.008452,
0.005256,
0.003323,
};
main(argc,argv)
int argc;
char **argv;
{

double h,v,v1,t,g,V0;
int l;

sscanf( argv[1], "%lf", & h );
g = 32.147 ;
V0 = 200 ;

l=12;
while ( l*10000.0 > h ) --l;

for ( v=t=0 ; h > 0 ; t += 0.01 ){
if( l*10000.0 > h ) --l;
v1 = v + 0.01*g*( 1 - density[l] * pow(density[l+1]/density[l],h/10000 - l) * (v/V0)*(v/V0) ) ;
h -= 0.01*( v+v1 )/2.0 ;
v = v1;
printf("%8.2f %16.4f %16.4f\n",t,v,h);
}
}
