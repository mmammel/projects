import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

public class PositionalValidate extends AbstractValidator
{
  private static final String FIELD_SPEC_PROP="fieldSpec";
  private static final String FIELD_TRIM_PROP="file.trimFields";

  private int [][] fieldSpecs;
  private boolean trimFields;

  public PositionalValidate( Properties properties )
  {
    super(properties);
  }

  protected void processFileProperties()
  {
    super.processFileProperties();
    String tempProp = this.validationProps.getProperty( FIELD_TRIM_PROP );

    if( tempProp != null )
    {
      this.trimFields = tempProp.toUpperCase().equals("TRUE");
    }
  }

  protected void processFieldProperties()
  {
    this.fieldSpecs = new int [this.numFields][];
    super.processFieldProperties();
  }

  protected void processFieldProperty( String prop )
  {
    super.processFieldProperty( prop );

    String propertyVal = null;

    if( prop.startsWith( FIELD_SPEC_PROP ) )
    {
      propertyVal = validationProps.getProperty( prop );
      this.storeFieldSpec( prop, propertyVal );
    }
  }

  private void storeFieldSpec( String propertyName, String spec )
  {
    String descriptor = propertyName.substring( FIELD_SPEC_PROP.length() );
    String [] range;
    String [] tempSpecStrs;
    int [] tempSpec;
    int low, high;
    int firstIndex, len;

    try
    {
      tempSpecStrs = spec.split(",");
      tempSpec = new int [2];
      tempSpec[0] = Integer.parseInt( tempSpecStrs[0] ) - 1;
      tempSpec[1] = Integer.parseInt( tempSpecStrs[1] );
      firstIndex = tempSpec[0];
      len = tempSpec[1];

      if( descriptor.indexOf( "-" ) > 0 )
      {
        range = descriptor.split( "\\-" );
        low = Integer.parseInt( range[0] );
        high = Integer.parseInt( range[1] );
        if( low >= high ) throw new Exception( "Bad range: " + low + " >= " + high );
        for( int i = low; i <= high; i++ )
        {
          tempSpec = new int [2];
          tempSpec[0] = firstIndex;
          tempSpec[1] = len;
          firstIndex+=len;
          this.fieldSpecs[i-1] = tempSpec;
        }
      }
      else
      {
        low = Integer.parseInt( descriptor );
        this.fieldSpecs[low - 1] = tempSpec;
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Error processing descriptor: " + descriptor, e );
    }
  }

  protected String [] getFieldsFromRow( String row )
  {
    String [] retVal = new String [this.numFields];
    String tempVal = null;
    int fieldNum = 0;

    for( int [] desc : this.fieldSpecs )
    {
      tempVal = row.substring(desc[0], desc[0]+desc[1]);
      if( this.trimFields ) tempVal = tempVal.trim();
      if( tempVal.length() == 0 ) tempVal = " ";
      retVal[fieldNum++] = tempVal;
    }

    return retVal;
  }

}
