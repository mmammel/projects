typedef char GRID[9][9];
unsigned long steps = 0;

main(){
int i,j,k,c;
GRID grid;

for( i=j=0 ; i<9 ; c=getchar() ){
switch ( c ){
case '_':
case '1':
case '2':
case '3':
case '4':
case '5':
case '6':
case '7':
case '8':
case '9':
	grid[i][j] = c ;
	if( ++j == 9 ){ j=0 ; ++i ; }
}
}

if( fill(grid) )
try(grid);
printf( "Steps: %ld\n", steps );
}
fill( grid)
GRID grid;
{
int i,j,k,c,cnt,hit,good,goodi,goodj,i1,j1,must;
do{
do{
hit=0;
for( i=0 ; i<9 ; i++ )for( j=0 ; j<9 ; j++ ){
if( grid [i][j] == '_' ){
	printf("(%d,%d) ",i+1,j+1);
	for( cnt=k=0 ; k<9 ; k++ ){
		grid[i][j] = '1'+k;
		if( check( grid, i, j ) ){
			++cnt;
			good='1'+k;
			printf("%c ",'1'+k);
		}
	}
	printf("\n");
	if( cnt == 1 ){  hit = 1; grid[i][j] = good;show1(grid,i,j); }
	else grid[i][j] = '_' ;
	if( cnt == 0 ) { printf("BAD GRID\n"); show( grid); return 0;}
}
}

show( grid);
}while( hit );
must=0;
printf("rows\n");
for( i = 0 ; i < 9 ; i++ ){
	for( k = 0 ; k < 9 ; k++ ){
		for( j = 0 ; j<9 ; j++ )if( grid[i][j] == '1'+k ) break;
		if( j < 9 ) continue;
		for( cnt=j=0 ; j<9 ; j++ ){
			if( grid[i][j] == '_' ){
				grid[i][j] = '1'+k;
				if( check( grid, i, j ) ){ ++cnt ; good=j;}
				grid[i][j] = '_';
			}
		}
		if( cnt == 0 ) { printf("BAD GRID\n"); show( grid); return 0;}
		if( cnt == 1 ) { grid[i][good] = '1'+k; must=1;show1(grid,i,good); }

	}
}
printf("cols\n");
for( j = 0 ; j < 9 ; j++ ){
	for( k = 0 ; k < 9 ; k++ ){
		for( i = 0 ; i<9 ; i++ )if( grid[i][j] == '1'+k ) break;
		if( i < 9 ) continue;
		for( cnt=i=0 ; i<9 ; i++ ){
			if( grid[i][j] == '_' ){
				grid[i][j] = '1'+k;
				if( check( grid, i, j ) ){ ++cnt ; good=i;}
				grid[i][j] = '_';
			}
		}
		if( cnt == 0 ) { printf("BAD GRID\n"); show( grid); return 0;}
		if( cnt == 1 ) { grid[good][j] = '1'+k; must=1;show1(grid,good,j); }
	}
}
printf("sqs\n");
for( i=0 ; i<9 ; i += 3)for( j=0 ; j<9 ; j += 3 ){
	for( k = 0 ; k < 9 ; k++ ){
		for( i1 = j1 = 0 ; i1<3 ;  ){
			if( grid[i+i1][j+j1] == '1'+k ) break;
			if( ++j1 == 3 ){ j1=0; ++i1; }
		}
		if( i1 < 3  ) continue;
		for( cnt=i1=j1=0 ; i1<3 ; ){
			if( grid[i+i1][j+j1] == '_' ){
				grid[i+i1][j+j1] = '1'+k;
				if( check( grid, i+i1, j+j1 ) ){
					++cnt ;
					goodi =i1;
					goodj = j1;
				}
				grid[i+i1][j+j1] = '_';
			}
			if( ++j1 == 3 ){ j1=0; ++i1; }
		}
		if( cnt == 0 ) { printf("BAD GRID\n"); show( grid); return 0;}
		if( cnt == 1 ) { grid[i+goodi][j+goodj] = '1'+k; must=1;
		show1(grid,i+goodi,j+goodj); }
	}
}
}while( must );
return 1 ;
}

try( grid )
GRID grid;
{
GRID new;
int i,j,m,n,k;
steps++;
for( m=0 ; m<9 ; m++ ){
 for( n=0 ; n<9 ; n++ ) if( grid[m][n] == '_') break;

if ( n<9 ) break;
}
if( m == 9 ){ printf("SOLUTION:\n");show(grid); return; }
for( k = '1' ; k <= '9' ; ++ k ){
for( i=0 ; i<9 ; i++ ) for( j=0 ; j<9 ; j++ ) new[i][j]=grid[i][j];
new[m][n] = k;
if( check( new, m, n ) ){
printf("TRY (%d,%d) = %c\n",m+1,n+1,k);
		if( fill( new ) )
		try( new );
}

}
}
check( grid, m, n )
GRID grid;
int m,n;
{
int i,j,k,l;
for( i=0 ; i<9 ; i++ )if( i != m  && grid [i][n] == grid[m][n] ) return 0;
for( i=0 ; i<9 ; i++ )if( i != n  && grid [m][i] == grid[m][n] ) return 0;
k = m/3 * 3 ; l = n/3 * 3;
for( i=0 ; i<3 ; i++ ) for( j=0 ; j<3 ; j++ )
if( ( k+i != m || l+j != n ) && grid[k+i][l+j] == grid[m][n] ) return 0;

return 1;
}

show( grid )
GRID grid;
{
int i,j;
for( i=0 ; i<9 ; i++ ){
for( j=0 ; j<9 ; j++ ){
printf(" %c",grid[i][j]);
if( j == 2 || j == 5 ) printf(" ");
}
printf("\n");
if( i%3 == 2 ) printf("\n");
}
printf("\n");
}

show1( grid,m,n )
GRID grid;
{
int i,j;
for( i=0 ; i<9 ; i++ ){
for( j=0 ; j<9 ; j++ ){
printf( i==m &&  n==j  ? "+%c" : " %c",grid[i][j] );
if( j == 2 || j == 5 ) printf(" ");
}
printf("\n");
if( i%3 == 2 ) printf("\n");
}
printf("\n");
}
