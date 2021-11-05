class Solution {
    public int[] twoSum(int[] nums, int target) {
        int [] ans = new int [2];
        ans[0] = -1; ans[1] = -1;
        if( target < 0 ) {
            for( int i = 0; i < nums.length; i++ ) nums[i] *= -1;
            target *= -1;
        }
        int [] sorted = Arrays.copyOf(nums, nums.length);

        Arrays.sort(sorted);
        int insertionPoint = Arrays.binarySearch( sorted, target );
        if( insertionPoint < 0 ) {
            insertionPoint = (-1 * (insertionPoint + 1) );
        } 
        
        if( insertionPoint == sorted.length ) insertionPoint--;
        
        int leftIdx = 0, rightIdx = insertionPoint, sum = 0;
        
        while( (sum = sorted[leftIdx] + sorted[rightIdx]) != target ) {
            if( sum > target ) {
                rightIdx--;
            } else if( sum < target ) {
                leftIdx++;
            }
        }
        
        for( int i = 0; i < nums.length; i++ ) {
            if( ans[0] == -1 && nums[i] == sorted[leftIdx] ) {
                ans[0] = i;
            } else if( ans[1] == -1 && nums[i] == sorted[rightIdx] ) {
                ans[1] = i;
            }
            
            if( ans[0] != -1 && ans[1] != -1 ) break;
        }
        
        return ans;
        
    }
}

