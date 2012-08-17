import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DynamicFieldFactory {

  private static final Pattern SPEC_PATTERN = Pattern.compile("(?s)^\\$\\{([^~]+)~([^~]+)~([^(}]+)(.*)$");

  /**
   * Builds Dynamic fields based on the spec.  The spec always starts and ends with
   * ${ and }, respectively, and may contain nested specs.
   * @param spec
   * @return
   */
  public static DynamicField getDynamicField( String spec )
  {
     DynamicField retVal = null;
     String name = null;
     String label = null;
     String ref = null;
     String type = null;
     String params = null;

     Matcher m = null;
     if( spec.matches(SPEC_PATTERN.pattern() ) )
     {
       m = SPEC_PATTERN.matcher( spec );
       if( m.find() )
       {
         name = m.group(1);
         label = m.group(2);
         type = m.group(3);
         params = m.group(4);

         if( params.indexOf( "(" ) == 0 )
         {
           params = params.substring( 1, params.lastIndexOf( ")" ) );
         }
         else
         {
           params = null;
         }
       }
     }

     if( type != null )
     {
       type = type.toUpperCase();

       if( type.equals( "SELECT" ) )
       {
         retVal = new SelectDynamicField( name, label, params );
       }
       else if( type.equals( "MULTISELECT" ) || type.equals( "MULTISELECT_QUOTED" ) )
       {
         retVal = new MultiSelectDynamicField( name, label, params, true );
       }
       else if( type.equals( "MULTISELECT_UNQUOTED" ) )
       {
         retVal = new MultiSelectDynamicField( name, label, params, false );
       }
       else if( type.equals( "TEXT" ) )
       {
         retVal = new TextDynamicField( name, label, params );
       }
       else if( type.equals( "DATETIME" ) )
       {
         retVal = new DateTimeDynamicField( name, label, params );
       }
     }
     else
     {
       // Log an error.
     }

     return retVal;
  }
}
