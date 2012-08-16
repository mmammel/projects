main(argc,argv)
int argc;
char **argv;
{
    perm( argv[1], 0 );
}

perm(s,n)
char *s;
int n;
{
    int i;
    if( n == strlen(s)-1 )
    {
        printf("%s\n",s);
    } else {
        int k=strlen(s);
        for( i=n ; i<k ; i++ )
        {
            int j;
            for( j=k+n-i ; j<k ; j++ )
                if( s[n] == s[j]) break;
            if( j==k ) perm( s,n+1);
            rotate( &s[n] );
        }
    }
}

/*
  abcde -> bcdea
*/
rotate(t)
char *t;
{
    char c;
    for( c = t[0] ; t[1] ; t++ ) t[0] = t[1];
    t[0] = c;
}


