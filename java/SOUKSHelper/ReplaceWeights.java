import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ReplaceWeights
{

  private static Pattern thePattern = Pattern.compile("(?U)(@(?:answers|weight)([0-9]+?))(?=\\D)");
  public static void main( String [] args )
  {
    try
    {
      Map<String,String> mappings = new HashMap<String,String>();

      BufferedReader mapReader = new BufferedReader( new FileReader( "map.txt" ) );

      String temp = null;
      String [] tempSplit = null;

      while( (temp = mapReader.readLine()) != null )
      {
        tempSplit = temp.split("\\|");
        mappings.put( tempSplit[0], tempSplit[1] );

      }

      mapReader.close();

      BufferedReader inputReader = new BufferedReader( new InputStreamReader( new FileInputStream( "input.txt" ), "UTF-16LE" ));

      StringBuilder input = new StringBuilder();

      while( ( temp = inputReader.readLine()) != null )
      {
        input.append( temp ).append("\n");
      }

      String inputStr = input.toString();

      StringBuffer replaceBuff = new StringBuffer();

      Matcher m = thePattern.matcher(inputStr);
      String g1, g2;
      while( m.find() )
      {
        g1 = m.group(1);
        g2 = m.group(2);

        if( g1.indexOf("answers") > 0 )
        {
          m.appendReplacement( replaceBuff, "@answers"+mappings.get(g2) );
        }
        else
        {
          m.appendReplacement( replaceBuff, "@weight"+mappings.get(g2) );
        }
      }

      m.appendTail( replaceBuff );

      BufferedWriter outputWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "output.txt"), "UTF-16LE" ) );

      inputStr = replaceBuff.toString();
      outputWriter.write( inputStr, 0, inputStr.length() );
      outputWriter.flush();
    }
    catch( Exception e )
    {
      System.out.println( "Caught Exception: " + e.toString() );
    }

  }

  public static String getUTFNum( String num )
  {
    String retVal = "";
    for( int i = 0; i < num.length(); i++ )
    {
      switch( num.charAt(i) )
      {
        case '0':
          retVal = retVal + "\u0030";
          break;
        case '1':
          retVal = retVal + "\u0031";
          break;
        case '2':
          retVal = retVal + "\u0032";
          break;
        case '3':
          retVal = retVal + "\u0033";
          break;
        case '4':
          retVal = retVal + "\u0034";
          break;
        case '5':
          retVal = retVal + "\u0035";
          break;
        case '6':
          retVal = retVal + "\u0036";
          break;
        case '7':
          retVal = retVal + "\u0037";
          break;
        case '8':
          retVal = retVal + "\u0038";
          break;
        case '9':
          retVal = retVal + "\u0039";
          break;
      }
    }

    return retVal;
  }

}
