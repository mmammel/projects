class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> retVal = new ArrayList<Integer>();
        Set<Integer> unq = new HashSet<Integer>();
        for( int n : nums ) {
            unq.add(n);
        }
        for( int i = 1; i < nums.length + 1; i++ ) {
            if( !unq.contains(i) ) retVal.add( i );
        }
        
        return retVal;
    }
}

