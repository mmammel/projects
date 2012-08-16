import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URL;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by IntelliJ IDEA.
 * User: mmammel
 * Date: Jun 20, 2008
 * Time: 12:32:15 PM
 */
public class ConvertEncoding {

    private static final String CHARMAP_PROPS = "characterMappings.properties";
    private static final String CHARMAP_PROP_PREFIX = "charactermap.character";
    private static final String ENCODING_PROP_PREFIX = "charactermap.encoding";
    private static final String STRING_PREFIX = "string(";
    private static final String NOT_AVAILABLE = "N/A";

    private static final String STRING_PATTERN_STR = "(?i)string\\(.*?\\)";
    private static final Pattern STRING_PATTERN = Pattern.compile( STRING_PATTERN_STR );

    private List<String> supportedEncodings = null;
    private Map<String,Map<Character,CharMapNode>> charMap = null;
    private Map<String,List<CharMapNode>> charArrayMap = null;
    private Properties charMapProps = null;

    public ConvertEncoding()
    {
        URL config = this.getClass().getResource( CHARMAP_PROPS );
        charMapProps = new Properties();

        try
        {
            charMapProps.load( config.openStream() );
        }
        catch( IOException ioe )
        {
            System.out.println( "Unable to load character mapping properties: " + ioe.toString());
        }

        // Get the supported encodings.
        supportedEncodings = new ArrayList<String>();
        String tempProp, keyChars;
        int propCount = 0;

        while( (tempProp = charMapProps.getProperty(ENCODING_PROP_PREFIX + propCount++ )) != null )
        {
            supportedEncodings.add(tempProp);
        }

        // Get the characters
        charMap = new HashMap<String,Map<Character,CharMapNode>>();
        charArrayMap = new HashMap<String, List<CharMapNode>>();
        CharMapNode tempNode = null;
        List<CharMapNode> tempList = null;

        Map<Character,CharMapNode> tempMap = null;

        for( String encoding : supportedEncodings )
        {
            propCount = 0;
            tempMap = new HashMap<Character,CharMapNode>();
            tempList = new ArrayList<CharMapNode>();
            charMap.put( encoding, tempMap );
            charArrayMap.put( encoding, tempList );

            while( (tempProp = charMapProps.getProperty(CHARMAP_PROP_PREFIX + propCount + "." + encoding)) != null )
            {
                tempNode = new CharMapNode( propCount++, tempProp );
                tempList.add( tempNode );

                if( (keyChars = tempNode.getKeyChars()) != null )
                {
                    for( int i = 0; i < keyChars.length(); i++ )
                    {
                        tempMap.put( keyChars.charAt(i), tempNode );
                    }
                }
            }
        }
    }

    public boolean execute(String inputFile, String fromEnc, String toEnc) throws Exception
    {
        InputStreamReader input = new InputStreamReader( new FileInputStream( inputFile ), fromEnc );
        OutputStreamWriter output = new OutputStreamWriter( new FileOutputStream( inputFile + ".conv" ), toEnc ); 
        this.convert(input,output,fromEnc, toEnc);
        input.close();
        output.close();
        return false;
    }

    protected void convert( Reader in, Writer out, String from, String to ) throws IOException
    {
        String retVal = null;

        CharMapNode toNode, fromNode;
        int readInt;
        char inChar;

        while( (readInt = in.read()) != -1 ) 
        {
            inChar = (char)readInt;
            fromNode = charMap.get(from).get(inChar);
            if( fromNode != null )
            {
                toNode = charArrayMap.get(to).get(fromNode.getCorrelationId());

                if( toNode == null )
                {
                    System.out.println( "Unable to locate character mapping for " + fromNode );
                }
                else
                {
                    out.append(toNode.getReplaceValue());
                }
            }
            else
            {
                out.append(inChar);
            }
        }

        out.flush();
    }

    private class CharMapNode
    {
        int correlationId = -1;
        String value;
        String keyChars;


        CharMapNode( int id, String val )
        {
            Matcher matcher;
            String tempReplace;
            this.correlationId = id;

            if( val.startsWith(STRING_PREFIX) )
            {
                this.value = val.substring(STRING_PREFIX.length(), val.length() - 1 );
                matcher = STRING_PATTERN.matcher( val );
                tempReplace = matcher.replaceAll("");

                this.keyChars = tempReplace != null && tempReplace.length() > 0 ? tempReplace : null;
            }
            else if( val.equalsIgnoreCase( NOT_AVAILABLE ))
            {
                this.value = "";
            }
            else
            {
                this.value = "" + val.charAt(0);
                this.keyChars = val;
            }
        }

        public String getReplaceValue() {
            return this.value;
        }

        public String toString() {
            return "[val:" + this.value + ",key(s):" + this.keyChars + ",idx:" + this.correlationId + "]";
        }

        public int getCorrelationId() {
            return correlationId;
        }

        public void setCorrelationId(int correlationId) {
            this.correlationId = correlationId;
        }

        public String getKeyChars() {
            return keyChars;
        }

        public void setKeyChars(String keyChars) {
            this.keyChars = keyChars;
        }
    }

    public static void main( String [] args )
    {
      ConvertEncoding CE = new ConvertEncoding();

      try
      {
        CE.execute( args[0], args[1], args[2] );
      }
      catch( Exception e )
      {
        System.out.println( "Caught an Exception: " + e.toString() );
        e.printStackTrace();
      }
    }
}


