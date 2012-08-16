import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;

public class StateListTest
{
    public Set<String> processTargetStates( String targetStates )
    {
        Set<String> retVal = new HashSet<String>();

        StringTokenizer strtok = new StringTokenizer( targetStates, ",");

        while( strtok.hasMoreTokens() )
        {
            retVal.add( strtok.nextToken().trim() );
        }

        return retVal;
    }

    public static void main( String [] args )
    {
      StateListTest slt = new StateListTest();

      Set<String> results = slt.processTargetStates( args[0] );

      for( String result : results )
      {
        System.out.println( result );
      }
    }
}