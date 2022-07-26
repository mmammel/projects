class Solution {
    public int minStartValue(int[] nums) {
        int retVal = 1;
        int minVal = 0, sum = 0;
        for( int i = 0; i < nums.length; i++ ) {
            if( (sum = sum + nums[i]) < minVal ) {
                minVal = sum;
            }
        }
        
        if( minVal < 0 ) retVal = -1 * minVal + 1;
        
        return retVal;
    }
}

