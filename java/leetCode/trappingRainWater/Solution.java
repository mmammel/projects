class Solution {
    public int trap(int[] height) {
        int maxHeight = 0, totalHeight = 0;
        SortedSet<Integer> maxHeightIndices = new TreeSet<Integer>();
        for( int i = 0; i < height.length; i++ ) {
            totalHeight += height[i];
            if( height[i] > maxHeight ) {
                maxHeight = height[i];
                maxHeightIndices.clear();
                maxHeightIndices.add( i );
            } else if( height[i] == maxHeight ) {
                maxHeightIndices.add(i);
            }
        }
        
        int rain = maxHeight * height.length - totalHeight;
        
        int sub = maxHeight;
        int highestSeen = 0;
        for( int i = 0; i < maxHeightIndices.first(); i++ ) {
            if( height[i] > highestSeen ) {
                highestSeen = height[i];
                sub = maxHeight - height[i];
            }
            
            rain -= sub;
        }
        
        sub = maxHeight;
        highestSeen = 0;
        for( int i = height.length - 1; i > maxHeightIndices.last(); i-- ) {
            if( height[i] > highestSeen ) {
                highestSeen = height[i];
                sub = maxHeight - height[i];
            }
            
            rain -= sub;
        }
        
        return rain;
    }
}

