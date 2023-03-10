import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.*;

public class NewLineBreak {

  private static final Pattern QTEXT_CODE_PATTERN = Pattern.compile("[A-Za-z][-A-Za-z0-9_]\\(\\s*?\\w*?\\s*?\\)|\\w+?\\s*?=\\s*?[-A-Za-z_0-9]+");


  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    StringBuilder inputString = new StringBuilder();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        inputString.append(input_str).append("\n");
      }

      String [] sections = inputString.toString().split( "\\n{2,}" );

      for( String section : sections ) {
        System.out.println("------");
        System.out.println(applyFormatting(section));
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public static String applyFormatting( String qtext ) {
    qtext = qtext.replaceAll("[ \t]+\\n", "\n");
    
    String [] sections = qtext.split("\\n{2,}");
    for( int i = 0; i < sections.length; i++ ) {
      sections[i] = sections[i].replaceAll("([.?!:])  ", "$1 ");
      sections[i] = sections[i].replaceAll("\\t", "  ");
      sections[i] = sections[i].replaceAll("(?<=[A-Za-z0-9])[ ]{2,}(?=[A-Za-z0-9])", " ");
      
      if( sections[i].indexOf("  ") >= 0 || looksLikeCode( sections[i] ) ) {
        // this should be escaped, wrap in <pre>
        sections[i] = "<pre style=\"word-break: break-word;\">" + sections[i] + "</pre>";        
      } 
      
      sections[i] = sections[i].replaceAll("\\n", "<br>");
    } 

    return joinArray(sections, "<br><br>");
  }

  public static boolean looksLikeCode( String text ) {
    boolean retVal = false;

    Matcher m = QTEXT_CODE_PATTERN.matcher(text);

    retVal = m.find();

    return retVal;
  }

  public static String joinArray( String [] array, String s ) {
    StringBuilder retVal = new StringBuilder();
    if( array != null ) {
      boolean first = true;
      for( String str : array ) {
       if( !first ) {
          retVal.append( s );
        } else {
          first = false;
        }
        retVal.append( str );
      }
    }
	  
    return retVal.toString();
  }
}

