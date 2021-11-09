class Solution {
    public int reverse(int x) {
        int retVal = 0;
        String xs = ""+x;
        if( x < 0 ) {
            xs = xs.substring(1);
        }
        
        String xsr = (new StringBuilder(xs)).reverse().toString();
        if( x < 0 ) xsr = "-"+xsr;
        try {
            retVal = Integer.parseInt(xsr);
        } catch( Exception e ) {
            retVal = 0;
        }
        
        return retVal;
    }
}

