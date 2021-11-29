class Solution {
    public int coinChange(int[] coins, int amount) {
        
        if( amount == 0 ) return 0;
        
        Arrays.sort(coins);
        
        int [] S = new int [ amount + 1 ];
        int [] C = new int [ amount + 1 ];
        
        Arrays.fill(S,-1);
        Arrays.fill(C,0);
        int least = -1;
        int coin = 0;
        
        for( int i = 1; i <= amount; i++ ) {
            least = Integer.MAX_VALUE;
            
            for( int k = 0; k < coins.length; k++ ) {
                if( i >= coins[k] ) {
                    if( C[i - coins[k]] != Integer.MAX_VALUE && (1 + C[i - coins[k]]) < least ) {
                        least = 1 + C[i - coins[k]];
                        coin = coins[k];
                    }
                }
            }
            
            C[i] = least;
            S[i] = least == Integer.MAX_VALUE ? -1 : coin;
        }
        
        int retVal = 0;
        
        if( S[amount] != -1 ) {
            while( amount > 0 ) {
                amount = amount - S[amount];
                retVal++;
            }
        } else {
            retVal = -1;
        }
        
        return retVal;
    }
}

