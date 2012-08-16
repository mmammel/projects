import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ValidatorFactory
{
  private static ValidatorFactory theInstance = new ValidatorFactory();

  private static final String TYPE_PROP = "file.type";
  private static final String TYPE_POSITIONAL = "positional";
  private static final String TYPE_CSV = "csv";

  private ValidatorFactory(){ }

  public static ValidatorFactory getInstance()
  {
    return theInstance;
  }

  public Validator getValidator( String propertyFile )
  {
    Validator retVal = null;
    Properties props = new Properties();

    try
    {
      props.load( new FileInputStream( propertyFile ) );
    }
    catch( IOException ioe )
    {
      throw new RuntimeException( "Unable to load properties from: " + propertyFile, ioe );
    }

    String type = props.getProperty( TYPE_PROP );

    if( type != null )
    {
      if( type.toLowerCase().equals( TYPE_POSITIONAL ) )
      {
        retVal = new PositionalValidate( props );
      }
      else if( type.toLowerCase().equals( TYPE_CSV ) )
      {
        retVal = new CSVValidate( props );
      }
      else
      {
        throw new RuntimeException( "Invalid type found in properties file: " + type );
      }
    }
    else
    {
      throw new RuntimeException( "No type found in properties file" );
    }

    return retVal;
  }
}