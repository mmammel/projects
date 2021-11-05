class Solution0 {
    public int trap(int[] height) {
        int [] sortedCopy = Arrays.copyOf( height, height.length );
        Arrays.sort( sortedCopy );
        int maxHeight = sortedCopy[ height.length - 1 ];
        sortedCopy = null;
        
        int rain = maxHeight * height.length;
        
        // make a two dimensional array
        boolean [][] world = new boolean [height.length][];
        for( int i = 0; i < world.length; i++ ) {
            world[i] = new boolean [ maxHeight ];
            for( int j = 0; j < maxHeight; j++ ) {
                world[i][j] = true; // water.
            }
        }
        
        for( int i = 0; i < world.length; i++ ) {
            for( int j = 0; j < height[i]; j++ ) {
                world[i][j] = false;  // earth
                rain--;
            }
        }
        
        for( int i = maxHeight - 1; i >= 0; i-- ) {
          for( int j = 0; j < world.length; j++ ) {
              if( world[j][i] ) {
                  rain--;
              } else {
                  break;
              }
          }
        }
        
        for( int i = maxHeight - 1; i >= 0; i-- ) {
          for( int j = world.length - 1; j >= 0; j-- ) {
              if( world[j][i]) {
                  rain--;
              } else {
                  break;
              }
          }
        }
        
        return rain;
    }
}
