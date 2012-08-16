#include <iostream.h>
#include "String.h"

void permuter(String a, int s);

int
main(){

   String *S = new String;

cout << "Enter a String\n";
cin >> *S;

cout << "*S = " << *S << endl;

permuter(*S, 0);

return 0;

}


void permuter(String a, int s){
    if( s== a.length() - 1){
        cout << a << endl;
    } else {
        char temp;
        int k = a.length() - s;
        for(int i = s; i < k; i++){
            int l;
            for(l = k+s-1; l < k; l++)
                if (a[l] == a[s]) break;
            if(l==s) permuter(a, s+1); 
            temp = a[s];
            for(int j = s; j < k-1; j++){
                a.replace(j, a[j+1]);
            }
            a.replace((a.length() - 1), temp);
        }
    }
}
