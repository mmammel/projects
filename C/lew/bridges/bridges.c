/* snext points to "neighbor string" , showing the 4 neighbors of each node. */

/*              A   B   C   D   E   F   G   H   I   J   K   L */
char *snext  = "BEFLACDEBDFGBCHIABIKACGLCFHJDGIJDEHKGHKLEIJLAFJK";


main(argc,argv)
int argc;
char **argv;
{



/* Always start with "AB" */
donext( snext, "AB" );

}

/*

donext() takes the current path and extends it by one to each available
neighbor node, in turn.

Called recursively to construct all possible paths from "A to A to A to A"

*/

donext(s, p )
char *s, *p;
{
char path[25], newnext[50], node;
int h,i;
int end; /* index of the null terminator of the path string */

strcpy ( path, p );
end = strlen(path) ;

node = path[ end -1 ];

#ifdef VERBOSE
printf( "path : %s\n", path );
printf("node = %c, end = %d\n",node, end);
#endif

strcpy( newnext, s );

remove( path[end-2], path[end-1], newnext );

/* Index to the 4 neighbors of the current end of the path */

h = node - 'A' ;

#ifdef VERBOSE
printf("h = %d\n", h );
#endif

for( i=0 ; i<4 ; i++ ){

/* Try each available neighbor, in order */

#ifdef VERBOSE
printf("s[%d] = %c\n", 4*h+i, newnext[4*h+i] );
#endif

if( newnext[ 4*h+i] != 'X' ){
         path[ end ] = newnext[4*h+i] ;
         path[ end+1 ] = 0;

#ifdef VERBOSE
printf( "new path : %s\n", path );
#endif
         donext( newnext, path );
}

/* Note: Drop-through can only occur at 'A' */


path [ end ] = 0;

}

if( end == 25 ) printf("%s\n", path); /* Maximal length path */

}

remove( node1, node2, neighborlist )
char node1, node2, *neighborlist;
{
int h, i;

if( node1 == 'X' || node2 == 'X' ){
         printf("ERROR in neighborlist\n%s", node1, node2, neighborlist );
         exit(0);
}

h = node1 - 'A' ;
for( i=0 ; i<4 ; i++ ){
         if( neighborlist[4*h+i] == node2 ) {
                 neighborlist[4*h+i] = 'X';
                 break;
         }
if( i == 4 ){
        printf("ERROR in neighborlist\n%s", node1, node2, neighborlist );
        exit(0);
}
}

h = node2 - 'A' ;
for( i=0 ; i<4 ; i++ ){
         if( neighborlist[4*h+i] == node1 ) {
                 neighborlist[4*h+i] = 'X';
                 break;
         }
if( i == 4 ){
        printf("ERROR in neighborlist\n%s", node1, node2, neighborlist );
        exit(0);
}
}

#ifdef VERBOSE
printf("remove( %c, %c ) : %s\n", node1, node2, neighborlist );
#endif


}

