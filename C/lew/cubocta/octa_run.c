#include "octa_inc.c"

main(){
int i,j,k,n;

for( i=n=0 ; i<NUMV ; i++ )for( j=0 ; j<4 ; j++ ){
if( i < edges[i].index[j] ) {
edgelist[n].v1 = i;
edgelist[n++].v2 = edges[i].index[j] ;
}
}

printf("#include \"struct_defs.c\"\n\n");
printf("#define NUMV %d\n\n",2*NUMV);
printf("struct vertex octa[] =\n{\n");

for( i=0 ; i<2*NUMV ; i++ )
printf("{ %3d, %3d, %3d },\n",
octa[edgelist[i].v1].coord[0] + octa[edgelist[i].v2].coord[0],
octa[edgelist[i].v1].coord[1] + octa[edgelist[i].v2].coord[1],
octa[edgelist[i].v1].coord[2] + octa[edgelist[i].v2].coord[2]
);
printf("};\n");

for( i=n=0 ; i<NUMV ; i++ )for( j=0 ; j<4 ; j++ ){
edges[i].index[j] = edge_id(i,edges[i].index[j])  ;

}

for( i=0 ; i<NUMV ; i++ ){
for( j=0 ; j<4 ; j++ ){
/* printf("%3d ",edges[i].index[j]); */
}
/* printf("\n"); */
}

for( n=0 ; n<2*NUMV ; n++ ){
for( i=k=0 ; i<NUMV ; i++ ){
for( j=0 ; j<4 ; j++ )if( edges[i].index[j] == n ){

/*
Each edge index appears in edges[] twice, and each
appearance generates two entries in newedges[]
*/

newedges[n].index[k++] = edges[i].index[(j+3)%4];
newedges[n].index[k++] = edges[i].index[(j+1)%4];
}
}

}

printf("struct edgeorder edges[] =\n{\n");
for( n=0 ; n<2*NUMV ; n++ ){
printf("{ ");
for ( j=0 ; j<4 ; j++ )printf(" %3d,", newedges[n].index[j] );
printf(" },\n");
}
printf("};\n");
printf("struct edge edgelist[2*NUMV] ;\n");

printf("struct edgeorder newedges [ 2*NUMV ];\n");

}
edge_id(i,j)
int i,j;
{
int n;
for( n=0 ; n<2*NUMV ; n++ )
if( edgelist[n].v1 == (i<j?i:j) && edgelist[n].v2 == (i<j?j:i) ) return n;

printf("ERROR: no edge for %d %d\n",i,j);
exit(0);
}


