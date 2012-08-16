import java.util.List;

public class Driver
{
  public static void main( String [] args )
  {
    String propsFile = null;
    String inputFile = null;
    boolean quiet = false;
    boolean errors = false;

    for( int i = 0; i < args.length; i++ )
    {
      if( args[i].equals( "-f" ) )
      {
        i++;
        inputFile = args[i];
      }
      else if( args[i].equals( "-p" ) )
      {
        i++;
        propsFile = args[i];
      }
      else if( args[i].equals( "-q" ) )
      {
        quiet = true;
      }
    }

    Validator validator = null;

    if( propsFile != null && inputFile != null )
    {
      try
      {
         validator = ValidatorFactory.getInstance().getValidator( propsFile );
         List<RowResult> results = validator.validateFile( inputFile );

         for( RowResult res : results )
         {
           if( res.getErrors().size() > 0 )
           {
             errors = true;
             if( quiet )
             {
               System.out.println( "errors" );
               System.exit(0);
             }
             else
             {
               System.out.println( "[ERROR][Row " + res.getRowNum() + "]" );
               for( String error : res.getErrors() )
               {
                 System.out.println( "--> " + error );
               }
             }
           }
         }

         if( !errors )
         {
           for( RowResult res2 : results )
           {
             System.out.println( res2.getCsvRow() );
           }
         }
      }
      catch( Exception e )
      {
        if( !quiet )
        {
          System.out.println( "Caught an exception: " + e.toString() );
          e.printStackTrace();
        }
      }
    }
    else if( !quiet )
    {
      throw new RuntimeException( "Usage: Driver -f <inputfile> -p <propertyfile> [-q]" );
    }
  }
}