/*

Compares invsqrt() with inv2sqrt(), which goes to second 
order. The use of a single step of the Newton method
is equivalent to a single term of a Taylor series about
a point 'a', whose value, f(a), is 1/sqrt(a) and is
given by the "magic" operation.

Note:

f(x) = f(a) + (x-a)*f'(a) + (x-a)^2/2 * f''(a) + ...

f(x) = x^-1/2
f'(x) = -1/2 * x^-3/2
f''(x) = 3/4 * x^-5/2

Taking the first order term gives the last step of
invsqrt(). Taking the second order term gives

x^-1/2 ~= a^-1/2 * ( 15/8 - ( 5/4 - 3/8 * x/a ) * x/a )

inv2sqrt() uses only one more multiplication than
invsqrt() and has two or three places of extra precision.
( Note the "0.5*x" multiplication is omitted. )

*/
 
#include <math.h>
main(){
float w,x,y,z, invsqrt(), inv2sqrt();
int *px, *pz,*py,*pw;
px = (int *)&x;
py = (int *)&y;
pz = (int *)&z;
pw = (int *)&w;

while( scanf("%f",&x) == 1 ){
y = 1.0/sqrt(x);
z = invsqrt(x);
w = inv2sqrt(x);
printf( "%08x %08x %08x %08x %15.10f %15.10f\n", *px, *py, *pz, *pw, z-y, w-y );
printf( "invsqrt: %15.10f  inv2sqrt: %15.10f\n", z, w );
}
}
float invsqrt(x)
float x;
{
float xhalf = 0.5*x;
int i = *(int *) &x;

i = 0x5f3759df - (i>>1);
x = *(float*)&i;
x = x*(1.5-xhalf*x*x);
return x;
}
float inv2sqrt(x)
float x;
{
float y0 = x;
float y, xy0y0;
int i = *(int *) &y0;

i = 0x5f3759df - (i>>1);
y0 = *(float*)&i;
xy0y0 = x*y0*y0;
y = y0*(1.875 - ( 1.25 - 0.375*xy0y0)*xy0y0 );
return y;
}
