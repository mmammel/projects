import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: mammelma
 * Date: Apr 6, 2011
 * Time: 10:23:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuotedSplitterTest extends TestCase {

    private static Pattern QUOTE_STRING_PATTERN = Pattern.compile( "(?:,|^)(\"+)[^,\"]" );

    public void testQuotedSplitter()
    {
        QuotedSplitter splitter = new QuotedSplitter(",","\"");
        String [] tempResult = null;
        for( String [] testArr : TEST_INPUT )
        {
            tempResult = splitter.split(testArr[0]);

            System.out.println( "Input: ");
            System.out.println( testArr[0] );
            System.out.println( "Result: ");
            for( String element : tempResult )
            {
                System.out.println( "[" + element + "]" );
            }

            assertEquals("Wrong result length!",testArr.length - 1,tempResult.length);

            for( int i = 0; i < tempResult.length; i++ )
            {
                assertEquals("Element " + i + " mismatch!",testArr[i+1],tempResult[i]);
            }
        }
    }

    public void testDynamicQuoteString()
    {
        QuotedSplitter splitter;
        String [] tempResult = null;
        String quoteString = "\"";
        for( String [] testArr : DYNAMIC_TEST_INPUT )
        {
            quoteString = this.getQuoteStringFromLine(testArr[0]);
            splitter = new QuotedSplitter(",",quoteString);
            tempResult = splitter.split(testArr[0]);
            System.out.println( "Using quote String: " + quoteString );
            System.out.println( "Input: ");
            System.out.println( testArr[0] );
            System.out.println( "Result: ");
            for( String element : tempResult )
            {
                System.out.println( "[" + element + "]" );
            }

            assertEquals("Wrong result length!",testArr.length - 1,tempResult.length);

            for( int i = 0; i < tempResult.length; i++ )
            {
                assertEquals("Element " + i + " mismatch!",testArr[i+1],tempResult[i]);
            }
        }
    }

    private String getQuoteStringFromLine( String line )
    {
        String retVal = "\"";
        Matcher m = QUOTE_STRING_PATTERN.matcher(line);
        if( m.find() )
        {
            retVal = m.group(1);
        }

        return retVal;
    }

    private static String [][] TEST_INPUT = {
            { "this,is,a,basic,test","this","is","a","basic","test" },
            { "test,\"with,some,quoting\",foo","test","with,some,quoting","foo"},
            { "test,late\",quoting","test","late\"","quoting"},
            { "test,\"early,unquoting\" foo,bar","test","early,unquoting foo","bar"},
            { "","" },
            { "single","single"},
            { "\"single,quoted\"","single,quoted"},
            { "unterminated,\"foo,bar","unterminated","foo,bar" }
    };

    private static String [][] DYNAMIC_TEST_INPUT = {
            { "this,is,a,basic,test","this","is","a","basic","test" },
            { "test,\"\"with,some,quoting\"\",foo","test","with,some,quoting","foo"},
            { "test,late\",quoting","test","late\"","quoting"},
            { "test,\"\"\"early,unquoting\"\"\" foo,bar","test","early,unquoting foo","bar"},
            { "","" },
            { "single","single"},
            { "\"\"\"single,quoted\"\"\"","single,quoted"},
            { "unterminated,\"\"foo,bar","unterminated","foo,bar" }
    };
}
