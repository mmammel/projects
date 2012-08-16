import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Enumeration;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class AbstractValidator implements Validator
{
  protected static final String NUM_FIELDS_PROP="row.numFields";
  protected static final String LINE_ENDINGS_PROP="file.lineEndings";
  protected static final String FIELD_PATTERN_PROP="fieldValidator";
  protected static final String FIELD_DEFAULT_PROP="fieldDefault";
  protected static final String LAST_VALID = "last_valid";
  protected static final String FUNC = "func:";
  protected static final String FUNC_TODAY = "today";
  protected static final Pattern FUNC_PATTERN = Pattern.compile( "^([^(]+)\\(([^)]+)\\)$" );

  protected Properties validationProps;
  protected int numFields = 0;
  protected Pattern [] validators = null;
  protected String [] lastValids = null;
  protected Map<Integer,String> defaultVals;

  public AbstractValidator( Properties properties )
  {
    this.defaultVals = new HashMap<Integer,String>();
    this.validationProps = properties;
    this.processFileProperties();
    this.processFieldProperties();
  }

  protected void processFileProperties()
  {
    try
    {
      this.numFields = Integer.parseInt( validationProps.getProperty( NUM_FIELDS_PROP ) );
      if( numFields <= 0 ) throw new Exception( "Invalid number of fields: " + numFields );
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Error getting the number of fields", e );
    }
  }

  protected void processFieldProperties()
  {
    String tempPropName = null;
    validators = new Pattern [ this.numFields ];
    lastValids = new String [ this.numFields ];

    Enumeration props = validationProps.propertyNames();
    while( props.hasMoreElements() )
    {
      tempPropName = (String)props.nextElement();
      this.processFieldProperty( tempPropName );
    }

    /*
     Confirm that all of our validators have been filled
     */
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < this.validators.length; i++ )
    {
      if( this.validators[i] == null )
      {
        sb.append( ", " + i );
      }
    }

    if( sb.toString().length() > 0 )
    {
      throw new RuntimeException( "Missing some validators" + sb.toString() );
    }
  }

  protected void processFieldProperty( String prop )
  {
    String propertyVal = validationProps.getProperty( prop );
;
    if( prop.startsWith( FIELD_PATTERN_PROP ) )
    {
      this.storeValidator( prop, propertyVal );
    }
    else if( prop.startsWith( FIELD_DEFAULT_PROP ) )
    {
      this.storeDefaultVal( prop, propertyVal );
    }
  }

  protected void storeDefaultVal( String propName, String defaultVal )
  {
    String descriptor = propName.substring( FIELD_DEFAULT_PROP.length() );
    String [] dArray = descriptor.split(",");
    String [] range;
    int low, high;

    try
    {
      for( String desc : dArray )
      {
        if( desc.indexOf( "-" ) > 0 )
        {
          range = desc.split( "\\-" );
          low = Integer.parseInt( range[0] );
          high = Integer.parseInt( range[1] );
          if( low >= high ) throw new Exception( "Bad range: " + low + " >= " + high );
          for( int i = low; i <= high; i++ )
          {
            this.defaultVals.put( i, defaultVal );
          }
        }
        else
        {
          low = Integer.parseInt( desc );
          this.defaultVals.put(low,defaultVal);
        }
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Error processing descriptor: " + descriptor, e );
    }
  }

  protected void storeValidator(String propName, String pattern)
  {
    String descriptor = propName.substring( FIELD_PATTERN_PROP.length() );
    String [] dArray = descriptor.split(",");
    String [] range;
    int low, high;

    try
    {
      Pattern p = Pattern.compile( pattern );
      for( String desc : dArray )
      {
        if( desc.indexOf( "-" ) > 0 )
        {
          range = desc.split( "\\-" );
          low = Integer.parseInt( range[0] );
          high = Integer.parseInt( range[1] );
          if( low >= high ) throw new Exception( "Bad range: " + low + " >= " + high );
          for( int i = low; i <= high; i++ )
          {
            this.validators[i-1] = p;
          }
        }
        else
        {
          low = Integer.parseInt( desc );
          this.validators[low - 1] = p;
        }
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Error processing descriptor: " + descriptor, e );
    }
  }

  /*
   * Return either a string that displays all of the errors, or a corrected row in CSV format.
   *
   */
  protected RowResult validateRow( String row )
  {
    RowResult retVal = new RowResult();
    StringBuilder csvRow = new StringBuilder();
    String defaultVal = null;
    String [] fields = this.getFieldsFromRow( row );
    if( fields.length != this.validators.length )
    {
      retVal.addError( "Wrong number of fields, should be " + this.validators.length + ", but is " + fields.length );
    }
    else
    {
      for( int i = 0; i < fields.length; i++ )
      {
        if( !fields[i].matches( this.validators[i].pattern() ) )
        {
          defaultVal = this.getDefaultVal( fields[i], i+1 );

          if( defaultVal == null )
          {
            retVal.addError( "Field " + (i+1) + " has an invalid value: [" + fields[i] + "]" );
          }
          else
          {
            fields[i] = defaultVal;
          }
        }
        else
        {
          this.lastValids[i] = fields[i];
        }

        if( i > 0 ) csvRow.append(",");
        csvRow.append(fields[i]);
      }

      retVal.setCsvRow( csvRow.toString() );
    }

    return retVal;
  }

  /*
   * note that field num is the 1-based field number!
   */
  protected String getDefaultVal( String val, int fieldNum )
  {
    String retVal = null;
    String defaultString = null;

    if( (defaultString = this.defaultVals.get( fieldNum )) != null )
    {
      if( defaultString.startsWith( LAST_VALID ) )
      {
        retVal = this.lastValids[ fieldNum - 1];
        defaultString = defaultString.split(",")[1];
      }

      if( retVal == null )
      {
        retVal = this.processDefaultVal( defaultString );
      }
    }

    return retVal;
  }

  protected String processDefaultVal( String defaultVal )
  {
    String retVal = null;
    String funcName = null;
    String funcParam = null;

    if( defaultVal.startsWith( FUNC ) )
    {
      defaultVal = defaultVal.substring( FUNC.length() );
      Matcher m = FUNC_PATTERN.matcher( defaultVal );
      if( m.matches() )
      {
        funcName = m.group(1);
        funcParam = m.group(2);

        if( funcName.toLowerCase().equals( FUNC_TODAY ) )
        {
          retVal = new SimpleDateFormat( funcParam ).format( new Date() );
        }
      }
    }
    else
    {
      retVal = defaultVal;
    }

    return retVal;
  }

  abstract protected String [] getFieldsFromRow( String row );

  public List<RowResult> validateFile( String fileName )
  {
    BufferedReader reader = null;
    List<RowResult> retVal = new ArrayList<RowResult>();
    RowResult tempResult = null;

    try
    {
      reader = new BufferedReader( new FileReader( fileName ) );

      String input = null;

      int count = 1;
      while( (input = reader.readLine()) != null )
      {
        //skip blank lines.
        if( input.trim().length() == 0 ) continue;

        tempResult = this.validateRow( input );
        tempResult.setRowNum( count++ );
        retVal.add(tempResult);
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( "Error reading input file", e );
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch( Exception e )
      {
        //do nothing
      }
    }

    return retVal;
  }

}
