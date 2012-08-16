public class SliceTimeGen
{
  public static void main( String [] args )
  {
    long currTime = 0L;
    //1186872713997

    if( args.length != 2 )
    {
      System.out.println( "Usage: java SliceTimeGet <long> <numslices>" );
    }
    else
    {
      currTime = Long.parseLong( args[0] );
      int slices = Integer.parseInt( args[1] );

      for( int i = 0; i < slices; i++ )
      {
        System.out.print( "" + currTime );
        currTime += 30;
        System.out.println( " " + currTime);
        //currTime += 1;
      }
    }

  }

}