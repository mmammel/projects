class Solution {
    public boolean isPowerOfTwo(int n) {
        if( n < 1 ) {
            return false;
        } else {
            Set<Integer> powers = new HashSet<Integer>();
            
            for( int i = 0; i < 32; i++ ) {
              powers.add( 1 << i );    
            }
            
            return powers.contains(n);
        }
    }
}

