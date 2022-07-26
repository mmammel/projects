class Solution {
    public String intToRoman(int num) {
        int m = 0, d = 0, cm = 0, cd = 0, c = 0, l = 0, xc = 0, xl = 0, x = 0, v = 0, ix = 0, iv = 0, i = 0;
        m = num / 1000;
        num = num % 1000;
        
        if( num >= 900 ) {
            cm = 1;
            num = num - 900;
        } else if( num >= 500 ) {
            d = 1;
            num = num - 500;
        } else if( num >= 400 ) {
            cd = 1;
            num = num - 400;
        }
        
        if( num >= 100 ) {
            c = num / 100;
            num = num % 100;
        }
        
        if( num >= 90 ) {
            xc = 1;
            num = num - 90;
        } else if( num >= 50 ) {
            l = 1;
            num = num - 50;
        } else if( num >= 40 ) {
            xl = 1;
            num = num - 40;
        } 
        
        if( num >= 10 ) {
            x = num / 10;
            num = num % 10;
        }
        
        if( num == 9 ) {
            ix = 1;
            num = num - 9;
        } else if( num >= 5 ) {
            v = 1;
            num = num - 5;
        } else if( num >= 4 ) {
            iv = 1;
            num = num - 4;
        }
        
        i = num;
        
        StringBuilder retVal = new StringBuilder();
        
        for( int j = 0; j < m; j++ ) retVal.append("M");
        retVal.append( cm == 1 ? "CM" : "" );
        retVal.append( d == 1 ? "D" : "" );
        retVal.append( cd == 1 ? "CD" : "" );
        for( int j = 0; j < c; j++ ) retVal.append("C");
        retVal.append( xc == 1 ? "XC" : "" );
        retVal.append( l == 1 ? "L" : "" );
        retVal.append( xl == 1 ? "XL" : "" );
        for( int j = 0; j < x; j++ ) retVal.append("X");
        retVal.append( ix == 1 ? "IX" : "" );
        retVal.append( v == 1 ? "V" : "" );
        retVal.append( iv == 1 ? "IV" : "" );   
        for( int j = 0; j < i; j++ ) retVal.append("I");        
        return retVal.toString();   
    }
}

