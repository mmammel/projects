import java.util.*;
import java.io.*;

public class DestRuleSort
{
  public Set destFieldSet = new TreeSet( new DestFieldComparator() );
  
  public void addDestField( String field )
  {
    this.destFieldSet.add( field );
  }
  
  public List getSortedList()
  {
    Iterator iter = destFieldSet.iterator();
    List retList = new ArrayList();
    
    while( iter.hasNext() )
    {
      retList.add( iter.next() );
    }
    
    return retList;
  }
  
  public class DestFieldComparator implements Comparator
  {
    public int compare( Object obj1, Object obj2 )
    {
      String d1 = (String)obj1;
      String d2 = (String)obj2;
      
      return new Integer( d1.substring(1) ).compareTo( new Integer( d2.substring(1) ) );
    }  
  }
  
  public static void main( String [] args )
  {
    DestRuleSort DRS = new DestRuleSort();
    String inputStr = null;
    List tempList = null;
    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    StringTokenizer tok = null;

    try
    { 
      while( (inputStr = reader.readLine()) != null )
      {
        if( inputStr.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        
        tok = new StringTokenizer( inputStr );
        
        while( tok.hasMoreTokens() )
        {
          DRS.addDestField( tok.nextToken() );
        }
        
        tempList = DRS.getSortedList();
        
        System.out.print( "Result: \n[ " );
        
        for( Iterator iter = tempList.iterator(); iter.hasNext(); )
        {
          System.out.print( (String)iter.next() + " " );
        }
        
        System.out.print( "]\n" );

      }
    }
    catch( Exception ioe )
    {
      System.out.println( "Exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
  }

}