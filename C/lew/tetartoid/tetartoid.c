/* Print the 001 layers of a tetartoid, consisting in the lattice points
satisfying all 12 face inequalities defined by normal[i] dot point <= 0 */


int lim=7;
int val=28;

int planes[12][3] =
{ {4,2,1}, {2,1,4}, {1,4,2},
  {4,-2,-1}, {-2,-1,4}, {-1,4,-2},
  {-4,1,-2}, {-2,1,-4}, {1,-4,-2},
  {-4,-1,2}, {2,-1,-4}, {-1,-4,2},
};
main()
{
int i,j,k,n,c;
int tot=0;

{
for( k=-lim ; k<=lim ; k++ )
{
printf("%4d\n",k);
for( j=-lim ; j<=lim ; j++ )
{
for( i=-lim ; i<=lim ; i++ )
{
        if( i == j && j == k ) c = '*';
        else c='+';

        for( n=0 ; n<12 ; n++ )
        {
//#ifdef DEBUG
printf("%d %d %d = %d\n", i,j,k, planes[n][0]*i + planes[n][1]*j + planes[n][2]*k);
//#endif
        if (planes[n][0]*i + planes[n][1]*j + planes[n][2]*k > val ) c=' ';


        }
        if( c == '+' ) ++tot;
        printf("%c ",c);
}
printf("\n");
}
printf("\n\n");
}
}

printf("%d\n",tot);
}
