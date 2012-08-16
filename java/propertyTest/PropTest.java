import java.net.URL;
import java.util.Properties;

public class PropTest
{
  Properties mProps = null;

  public PropTest()
  {
    try
    {
      URL config = this.getClass().getResource( "properties.properties" );
      mProps = new Properties();
      mProps.load( config.openStream() );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

  public String getProperty( String name )
  {
    return this.mProps.getProperty( name );
  }

  public static void main( String [] args )
  {
    PropTest PT = new PropTest();

    System.out.println( "Fragment: " + PT.getProperty( "test.docfragment" ) );
    System.out.println( "Normal: " + PT.getProperty( "test.normal" ) );
    System.out.println( "Spaces: " + PT.getProperty( "this is a test property with spaces" ) );

  }
}
