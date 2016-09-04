#include "octa_inc.c"

main(){
int i,j,k;
for( i=0 ; i<NUMV ; i++ ){
if( octa[i].coord[0] <0 || octa[i].coord[1] <0 || octa[i].coord[2] <0 )
                continue;
if ( octa[i].coord[1] > octa[i].coord[0] ||
         octa[i].coord[2] > octa[i].coord[1] ) continue;

printf("%5d %5d %5d\n", octa[i].coord[0], octa[i].coord[1], octa[i].coord[2]);
}
}

