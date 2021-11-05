import java.util.regex.*;

class Solution {
    private static final Pattern GOOD_REGION = Pattern.compile("\\((o+)\\)");
    private static final Pattern THE_OS = Pattern.compile("o+");
    public int longestValidParentheses(String s) {
        // Replace all () with 'o'
        s = s.replaceAll("\\(\\)", "o");
        Matcher m = GOOD_REGION.matcher(s);
        while( m.find() ) {
            s = m.replaceAll("o$1");
            m = GOOD_REGION.matcher(s);
        }
        
        int maxLen = 0, currCount = 0;
        
        for( int i = 0; i < s.length(); i++ ) {
            if( s.charAt(i) == 'o' ) {
                currCount += 2;
            } else {
              if(currCount > maxLen ) maxLen = currCount;
              currCount = 0;
            }
        }
        
        if( currCount > maxLen ) maxLen = currCount;
        
        return maxLen;
    }
}
