char Mtab[81][81];
int hit[81];
int pick[81];
int mtab[3][3] = {
{0,2,1},
{2,1,0},
{1,0,2}
};

int count;
int tab[21];
double prob[21];

main(argc,argv)
int argc;
char **argv;
{
char *p;
int i,j,k,l,m,q;
int max;
double drand48();

max = atoi(argv[1]);
srand48( getpid() );

for(i=0;i<81;i++)for(j=0;j<81;j++)Mtab[i][j]=mult(i,j);
for(i=0 ; i<81 ; i++ ) pick[i]=i;

for( j=0 ; j<max ; j++ ){
for( i=0 ; i<81 ; i++ )hit[i]=0;
hit[2]=1;
for( k=2 ; k<21 ; k++ ){
	l = (81-k)*drand48() + k;

	if( hit[pick[l]] == 1 ) break;

	q= pick[k];
	pick[k]=pick[l];
	pick[l]=q;

	for( i=0 ; i<k ; i++ )hit[ Mtab[pick[i]][pick[k]] ]=1;

}

++tab[k];
}
for(i=0 ; i<21 ; i++ ) printf(i%5 ? " %10d" : "\n%10d",tab[i]);
printf("\n");
prob[0]=max;
for(i=1 ; i<21 ; i++ ) prob[i] = prob[i-1]-tab[i];
for(i=0 ; i<21 ; i++ ) printf(i%5 ? " %10.7f" : "\n%10.7f",prob[i]/max);
printf("\n");

}

mult( x,y )
int x,y;
{
int s=0;
s = mtab[x/27][y/27] ;
s = 3*s + mtab[ x%27/9 ] [ y%27/9 ] ;
s = 3*s + mtab[ x%9/3 ] [ y%9/3 ] ;
s = 3*s + mtab[ x%3 ] [ y%3 ] ;
return s;
}
