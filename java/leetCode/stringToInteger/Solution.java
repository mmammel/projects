import java.math.BigInteger;

class Solution {
    private static final String NUM_PATTERN = "^[ ]*?([+-]?)([0-9]+(?=[^0-9])?).*$";
    private static final BigInteger MIN = new BigInteger("-2147483648");
    private static final BigInteger MAX = new BigInteger("2147483647");
    public int myAtoi(String s) {
        int retVal = 0;
        if( s.matches( NUM_PATTERN ) ) {
            String refined = s.replaceAll(NUM_PATTERN, "$1$2");
            refined = refined.replaceAll("\\+","");
            BigInteger b = new BigInteger(refined);
            
            if( b.compareTo(MIN) < 0 ) {
                retVal = MIN.intValue();
            } else if( b.compareTo(MAX) > 0 ) {
                retVal = MAX.intValue();
            } else {            
                retVal = b.intValue();
            }            
        }
        
        return retVal;
    }
}

