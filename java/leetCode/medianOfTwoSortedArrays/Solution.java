class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int totalLen = nums1.length + nums2.length;
        double retVal = 0.0;
        int [] merged = new int [totalLen];
        int idx1 = nums1.length > 0 ? 0 : -1, idx2 = nums2.length > 0 ? 0 : -1, newIdx = 0;
        
        while( newIdx < totalLen / 2 + 1 ) {
            if( idx1 == -1 ) {
                merged[newIdx++] = nums2[idx2++];
            } else if( idx2 == -1 ) {
                merged[newIdx++] = nums1[idx1++];
            } else {
                if( nums1[idx1] < nums2[idx2] ) {
                    merged[newIdx++] = nums1[idx1++];
                } else if( nums1[idx1] > nums2[idx2] ) {
                    merged[newIdx++] = nums2[idx2++];
                } else {
                    merged[newIdx++] = nums1[idx1++];
                    merged[newIdx++] = nums2[idx2++];
                }
                
                if( idx1 >= nums1.length ) idx1 = -1;
                if( idx2 >= nums2.length ) idx2 = -1;
            }
        }
        
        if( totalLen % 2 == 0 ) {
            //even, split the middle two.
            retVal = (new Integer(merged[totalLen / 2 - 1]).doubleValue() + 
                new Integer(merged[totalLen / 2 ]).doubleValue()) / 2.0;
        } else {
            retVal = new Integer( merged[ totalLen / 2 ]).doubleValue();
        }
        
        return retVal;
    }
}

