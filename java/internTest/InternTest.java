import java.io.*;
import java.util.*;

public class InternTest
{
  public BufferedReader mReader = null;
  public List mInternWordList = new ArrayList();
  public List mWordList = new ArrayList();

  public InternTest( String filename )
  {
    String tempWord = null;

    try
    {
      mReader = new BufferedReader( new FileReader( filename ) ) ;

      while( (tempWord = mReader.readLine()) != null )
      {
        mInternWordList.add( tempWord.intern() );
        //mWordList.add( tempWord );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
    
  }

  public void wordExists_intern( String word )
  {
    String find = word.intern();
    String temp = null;
    boolean found = false;
    long start = 0L, end = 0L;

    start = new Date().getTime();

    for( Iterator iter = mInternWordList.iterator(); iter.hasNext(); )
    {
      temp = (String)iter.next();
      if( temp == find )
      {
        found = true;
        end = new Date().getTime();
        break;
      }
    }

    if( !found )
    {
      end = new Date().getTime();
      System.out.println( "(intern) Not found after " + (end - start) + "ms." );
    }
    else
    {
      System.out.println( "(intern) Found after " + (end - start) + "ms." );
    }
    
  } 

  public void wordExists( String word )
  {
    String temp = null;
    boolean found = false;
    long start = 0L, end = 0L;

    start = new Date().getTime();

    for( Iterator iter = mWordList.iterator(); iter.hasNext(); )
    {
      temp = (String)iter.next();
      if( temp.equals(word) )
      {
        found = true;
        end = new Date().getTime();
        break;
      }
    }

    if( !found )
    {
      end = new Date().getTime();
      System.out.println( "(non-intern) Not found after " + (end - start) + "ms." );
    }
    else
    {
      System.out.println( "(non-intern) Found after " + (end - start) + "ms." );
    }

  }

  public static void main( String [] args )
  {
    InternTest IT = new InternTest( "words.txt" );
    String inputStr = null;
    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    
    try
    { 
      while( (inputStr = reader.readLine()) != null )
      {
        inputStr = inputStr.trim();
        if( inputStr.equals( "quit" ) )
        {
          break;
        }
        
        IT.wordExists_intern( inputStr );
        IT.wordExists( inputStr );

      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
