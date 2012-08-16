struct info {
	int this;		/* current card being checked */
	char hit[81];		/* shows which cards will form a set */
	struct info *last;	/* for linked list */
};

char Mtab[81][81];
int mtab[3][3] = {
{0,2,1},
{2,1,0},
{1,0,2} 
};

int count, count1;
main( argc, argv )
int argc;
char **argv;
{
int i,j,k,l,m,q;
struct info start,start1;

for( i=0 ; i<81 ; i++)for(j=0;j<81;j++)Mtab[i][j]=mult(i,j);
for( i=0 ; i<81 ; i++ ) start.hit[i]=start1.hit[i]=0;

start.this=0;
start.last=0;
start1.this=1;
start1.last=&start;

/*
Try draws of N cards against the fixed draw of 0,1,3 
since these cards are quivalent to any three
cards not forming a set.

The number of draws of N+3 cards not forming a
set is given by

`setsY N` * c(81,2)/c(N+3,2) * 78/(N+1)

*/
go( &start1, 3, atoi(argv[1]) );

if( count1 > 0 )
printf("%d%09d\n",count1,count);
else
printf("%d\n",count);
}
mult ( x,y )
int x,y;
{
int s;
s =  mtab[ x/27 ][ y/27 ];
s =  3*s + mtab[ x%27/9 ][ y%27/9 ];
s =  3*s + mtab[ x%9/3 ][ y%9/3 ];
s =  3*s + mtab[ x%3 ][ y%3 ];
return s;
}
go( p, next, level )
struct info *p;
int next, level;
{
struct info new, *q;
int i;

if( level == 0 ){
	if( ++count == 1000000000 )
	{
		count = 0;
		++count1;
	}
	return;
}

new.last = p;
new.this = next;
	for( i=next+1 ; i<81 ; i++ )new.hit[i] = p->hit[i];
	
for( q=p ; q!= 0 ; q = q->last ){
	new.hit[Mtab[ next ][ q->this ] ] = 1;
}

for( i=next+1 ; i<81 ; i++ ){
	if( new.hit[i] == 0 ) go ( &new, i, level-1 );

}
}
