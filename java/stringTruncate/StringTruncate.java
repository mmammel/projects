public class StringTruncate
{

  protected String enforceStringFieldSize( String str, int size, boolean frontTruncate )
  {
    String retVal = str;
    int diff = 0;
    // leave room for one character, not sure how different databases 
    // enforce column length (null terminator??)
    if( size <= 0 || str == null )
    {
      retVal = "";
    }
    else 
    {
      if( (diff = (str.length() - size)) >= 0 ) 
      {
        if( frontTruncate )
        {
          retVal = retVal.substring( diff + 1 );
          if( retVal.length() > 3 ) retVal = "..." + retVal.substring(3);
        }
        else
        {
          retVal = retVal.substring( 0, retVal.length() - (diff + 1) );
          if( retVal.length() > 3 ) retVal = retVal.substring( 0, retVal.length() - 3) + "...";
        }
      }
    }
    
    return retVal;
  }

  public static void main( String [] args )
  {
    try
    {
      if( args.length != 3 ) 
      {
        System.out.println( "usage: java StringTruncate <string> <size> <F|B>" );
        System.exit(1);
      }

      int size = Integer.parseInt( args[1] );
      String str = args[0];
      boolean front = args[2].startsWith( "F" );
      StringTruncate ST = new StringTruncate();
      System.out.println( "trunc(\"" + str + "\"," + size + "," + front + ") -> " + ST.enforceStringFieldSize( str, size, front ) );
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
    }

    
  }


}
