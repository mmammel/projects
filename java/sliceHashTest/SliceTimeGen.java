public class SliceTimeGen
{
  public static void main( String [] args )
  {
    long currTime = 0L;

    if( args.length != 1 )
    {
      System.out.println( "Usage: java SliceTimeGet <long>" );
    }
    else
    {
      currTime = Long.parseLong( args[0] );
      for( int i = 0; i < 100; i++ )
      {
        System.out.print( "SM.insertSlice( new Slice( " + currTime + "L, " );
        currTime += 30;
        System.out.println( "" + currTime + "L, \"myhost\", \"myjvm\" ) );" );
        currTime += 1;
      }
    }

  }



}

