import java.util.Arrays;
class Solution {
    public List<Integer> largestDivisibleSubset(int[] nums) {
        int [][] counts = new int [ nums.length ][];
        for( int i = 0; i < counts.length; i++ ) {
            counts[i] = new int [2];
            counts[i][0] = -1;
            counts[i][1] = 0;
        }
        
        int max = 0, maxCountIdx = 0;
        
        Arrays.parallelSort( nums );
        
        for( int i = nums.length - 2; i >= 0; i-- ) {
            for( int j = i+1; j < nums.length; j++ ) {
              if( nums[j] % nums[i] == 0 ) {
                if( counts[i][1] < counts[j][1] + 1 ) {
                    counts[i][0] = j;
                    counts[i][1] = counts[j][1] + 1;
                    if( max < counts[i][1] ) {
                        max = counts[i][1];
                        maxCountIdx = i;
                    }
                };
              }
            }
        }
        
        List<Integer> retVal = new ArrayList<Integer>();
        int idx = maxCountIdx;
        while( idx != -1 ) {
            retVal.add( nums[idx] );
            idx = counts[idx][0];
        }
        
        return retVal;
    }
}

