#include <iostream.h>
#include "String.h"

void perm(String s, int n);

int
main(int argc,char **argv)
{
    String S(argv[1]);
    S.rotate(0);
    cout << "S.rotate(0) = " << S << endl;
    perm( S, 0 );
}

void
perm(String s, int n)
{
    int i;
    if( n == s.length()-1 )
    {
        cout << s << endl;
    } else {
        int k=s.length();
        for( i=n ; i<k ; i++ )
        {
            int j;
            for( j=k+n-i ; j<k ; j++ )
                if( s[n] == s[j]) break;
            if( j==k ) perm( s,n+1);
            s.rotate(n);
        }
    }
}

