import java.util.Stack;

public class Permuter
{
   private static char [] vars = { 'n', 'i', 'j', 'k' };
   private static int [] vals = new int [4];
   private static Stack<Object> recursionTracker = new Stack<Object>();
   
   private String rotate_orig( String s, int idx )
   {
     String newString = new String();
     char holder = '\0';
     for( int i = 0; i < idx; i++ ) newString += s.charAt(i);
     holder = s.charAt(idx);
     for( int j = idx+1; j < s.length(); j++ ) newString += s.charAt(j);
     newString += holder;
     return newString;
   }

   public String rotate( String s, int idx )
   {
     StringBuilder retVal = new StringBuilder( s );
     for( int i = idx; i < s.length()-1; i++ ) retVal.setCharAt( i, s.charAt(i+1) );
     retVal.setCharAt( s.length() - 1, s.charAt(idx) );
     return retVal.toString();
   }

   public void permute( String s, int n )
   {
     recursionTracker.push( new Object() );
     
     int i,j = 0;
     int k = s.length();
     vals[0] = n;
     vals[3] = k;
     
     if( n == k - 1 )
     {
       System.out.println( s );
     }
     else
     {
       for( i = n; i < k; i++ )
       {
         vals[1] = i;
         for( j=k+n-i; j < k; j++ ){  
           vals[2] = j;
           //printStatus( "Lookahead", s, vars, vals );
           if( s.charAt(n) == s.charAt(j) ){
               //System.out.println( "Breaking! j=" + j + ", n=" + n);
               break;
           }
         }
         //printStatus( "Normal", s, vars, vals );
         
         if( j == k ){
            //System.out.println( "Recursing!" );
            System.out.println( "About to recurse on " + s + " at n=" + n );
            permute( s, n+1 );
         } else {
           System.out.println( "Decided not to recurse on " + s + " at n=" + n );
         }
         vals[0] = n;

         s = this.rotate( s, n );
       }
     }
     
     recursionTracker.pop();
   }

   private static void printStatus( String label, String s, char [] vars, int [] vals )
   {
      StringBuilder sb = new StringBuilder();
      sb.append("[").append(recursionTracker.size()).append("]").append(label).append(":\n");
      sb.append( s ).append("\n");
      for( int i = 0; i < vars.length; i++ )
      {
        for( int j = 0; j < vals[i]; j++ ) sb.append(" ");
        sb.append( vars[i] ).append("\n");
      }

      System.out.println( sb.toString() );
   }
   
   public static void main( String [] args )
   {
     Permuter P = new Permuter();
     P.permute( args[0], 0 );
   }

}
