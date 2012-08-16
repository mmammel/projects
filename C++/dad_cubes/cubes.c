/* Recursively construct 4-d figure illustrating

       4*Sum i=1 to N of i^3 = N*N*(N+1)*(N+1)
*/

char *q = "AAAABBAAAABBDDXXBBDDXXBBDDCCCCDDCCCC";
main(argc,argv)
int argc;
char **argv;
{
char *p, *form();
p = form( atoi(argv[1]) );
oput( p, atoi(argv[1]) );
}

char *token[] = { "abcd", "ABCD" };
char * form(N)
{
int i,j,k,l;
char *f, *g;

f = (char *) calloc( 1, N*N*(N+1)*(N+1) );
if( N == 1 ){
       f[0]='A';
       f[1]='B';
       f[2]='D';
       f[3]='C';
       return f ;
}

for( i=0 ; i<N*N*(N+1)*(N+1) ; i++ ) f[i]='X';

for( i=0 ; i<N ; i++ )for( j=0 ; j<N*N ; j++ )

f[ i*N*(N+1) + j ] = token[N%2][0];


for( i=0 ; i<N*N ; i++ )for( j=N*N ; j<N*(N+1) ; j++ )

f[ i*N*(N+1) + j ] = token[N%2][1];


for( i=N*N ; i<N*(N+1) ; i++ )for( j=N ; j<N*(N+1) ; j++ )

f[ i*N*(N+1) + j ] = token[N%2][2];


for( i=N ; i<N*(N+1) ; i++ )for( j=0 ; j<N ; j++ )

f[ i*N*(N+1) + j ] = token[N%2][3];


g = form(N-1);

for( i=0 ; i<N ; i++) for( j=0 ; j<N-1 ; j++ )
for( k=0 ; k<N ; k++) for( l=0 ; l<N-1 ; l++ )

f[ (j+1)*N*N*(N+1) + i*N*(N+1) + (l+1)*N + k] =
g[ i*(N-1)*(N-1)*N + j*(N-1)*N + k*(N-1) + l];

return f;
}

oput( p,N )
char *p;
int N;
{
int i,j,k,l;
for( i=0 ; i<N+1 ; i++ ){
for( j=0 ; j<N ; j++ ){
for( k=0 ; k<N+1   ; k++ ){
for( l=0 ; l<N   ; l++ ){

       printf("%c",p[ i*N*N*(N+1) + j*N*(N+1) + k*N + l ]);
}
       printf(" ");
}
       printf("\n");
}
       printf("\n");
}


}

