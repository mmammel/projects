int row [24][4] ={
2,4,3,1,
2,4,1,3,
2,1,4,3,
2,1,3,4,
2,3,1,4,
2,3,4,1,
3,1,4,2,
3,1,2,4,
3,2,1,4,
3,2,4,1,
3,4,2,1,
3,4,1,2,
4,2,1,3,
4,2,3,1,
4,3,2,1,
4,3,1,2,
4,1,3,2,
4,1,2,3,
1,3,2,4,
1,3,4,2,
1,4,3,2,
1,4,2,3,
1,2,4,3,
1,2,3,4,
};

int colSees [4] = { 0,0,0,0 };
int rowSees [4] = { 0,0,0,0 };

main(){
int i,j,k,l;
for( i=0 ; i<24 ; i++ ){
for( j=0 ; j<24 ; j++ ){
if( row[i][0] == row[j][0] ) continue;
if( row[i][1] == row[j][1] ) continue;
if( row[i][2] == row[j][2] ) continue;
if( row[i][3] == row[j][3] ) continue;
for( k=0 ; k<24 ; k++ ){
if( row[i][0] == row[k][0] ) continue;
if( row[i][1] == row[k][1] ) continue;
if( row[i][2] == row[k][2] ) continue;
if( row[i][3] == row[k][3] ) continue;
if( row[j][0] == row[k][0] ) continue;
if( row[j][1] == row[k][1] ) continue;
if( row[j][2] == row[k][2] ) continue;
if( row[j][3] == row[k][3] ) continue;
for( l=0 ; l<24 ; l++){
if( row[i][0] == row[l][0] ) continue;
if( row[i][1] == row[l][1] ) continue;
if( row[i][2] == row[l][2] ) continue;
if( row[i][3] == row[l][3] ) continue;
if( row[j][0] == row[l][0] ) continue;
if( row[j][1] == row[l][1] ) continue;
if( row[j][2] == row[l][2] ) continue;
if( row[j][3] == row[l][3] ) continue;
if( row[k][0] == row[l][0] ) continue;
if( row[k][1] == row[l][1] ) continue;
if( row[k][2] == row[l][2] ) continue;
if( row[k][3] == row[l][3] ) continue;

rowSees[0] =  see(row[i][0],row[j][0],row[k][0],row[l][0] ); 
rowSees[1] =  see(row[i][1],row[j][1],row[k][1],row[l][1] );
rowSees[2] =  see(row[i][2],row[j][2],row[k][2],row[l][2] );
rowSees[3] =  see(row[i][3],row[j][3],row[k][3],row[l][3] );

colSees[0] = see(row[i][3],row[i][2],row[i][1],row[i][0] );
colSees[1] = see(row[j][3],row[j][2],row[j][1],row[j][0] );
colSees[2] = see(row[k][3],row[k][2],row[k][1],row[k][0] );
colSees[3] = see(row[l][3],row[l][2],row[l][1],row[l][0] );

printf("%d %d %d %d\n--------\n",rowSees[0],rowSees[1],rowSees[2],rowSees[3]);
printf("%d %d %d %d | %d\n",row[i][0],row[i][1],row[i][2],row[i][3],colSees[0]);
printf("%d %d %d %d | %d\n",row[j][0],row[j][1],row[j][2],row[j][3],colSees[1]);
printf("%d %d %d %d | %d\n",row[k][0],row[k][1],row[k][2],row[k][3],colSees[2]);
printf("%d %d %d %d | %d\n",row[l][0],row[l][1],row[l][2],row[l][3],colSees[3]);
printf("Total: %d\n\n", rowSees[0]+rowSees[1]+rowSees[2]+rowSees[3]+colSees[0]+colSees[1]+colSees[2]+colSees[3]);
}}}}
}
see(a,b,c,d)
{
int g=0,n=0;
if( a > g ){++n;g=a;}
if( b > g ){++n;g=b;}
if( c > g ){++n;g=c;}
if( d > g ){++n;g=d;}
return n;
}

