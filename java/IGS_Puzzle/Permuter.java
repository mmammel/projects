public class Permuter
{
   private String rotate( String s, int idx )
   {
     String newString = new String();
     char holder = '\0';
     for( int i = 0; i < idx; i++ ) newString += s.charAt(i);
     holder = s.charAt(idx);
     for( int j = idx+1; j < s.length(); j++ ) newString += s.charAt(j);
     newString += holder;
     return newString;
   }

   public void permute( String s, int n )
   {
     int i,j = 0;
     int k = s.length();
     
     if( n == k - 1 ) System.out.println( s );
     else
     {
       for( i = n; i < k; i++ )
       {
         for( j=k+n-i; j < k; j++ ) if( s.charAt(n) == s.charAt(j) ) break;
         
         if( j == k ) permute( s, n+1 );
         s = this.rotate( s, n );
       }
     }
   }
   
   public static void main( String [] args )
   {
     Permuter P = new Permuter();
     P.permute( args[0], 0 );
   }

}
