class Solution {
    public int arrangeCoins(int n) {
        // s complete stairs is s(s+1)/2
        // solve this for s:
        //   s(s+1)/2 = n
        // s^2 + s = 2n
        // s^2 + s - 2n = 0
        // (-1 +- sqrt( 1 + 8n ) ) / 2
        // floor( (-1 + sqrt(1+8n))/2)
        double nd = Integer.valueOf(n).doubleValue();
        double ans = Math.floor( (Math.sqrt( 1.0 + 8.0*nd) - 1.0) / 2.0 );
        return Double.valueOf(ans).intValue();
    }
}

