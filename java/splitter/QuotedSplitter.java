import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;

/**
 * User: mammelma
 * Date: Apr 6, 2011
 * Time: 9:17:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuotedSplitter
{
    private String delimiter;
    private String quoter;
    private int idx = 0;
    private List<String> result = null;
    private State state;
    private StringBuilder currentElement = null;

    private static enum State {
        DELIMITER,   // just read a delimiter, also used as the initial state.
        QUOTED,      // We are quoted.
        UNQUOTED    // We are unquoted.
    }

    public QuotedSplitter( String delim, String quoter )
    {
        if( delim == null || quoter == null )
        {
            throw new IllegalArgumentException("Delimiter and quoter must be non-null");
        }
        else if( delim.trim().length() < delim.length() || quoter.trim().length() < quoter.length() )
        {
            throw new IllegalArgumentException("Delimiter and Quoter cannot start or end with whitespace" );
        }
        else if( delim.equals(quoter) )
        {
            throw new IllegalArgumentException( "Delimiter and quoter cannot be the same String" );
        }

        this.delimiter = delim;
        this.quoter = quoter;
    }

    public String [] split(String val)
    {
        this.state = State.DELIMITER;
        this.result = new ArrayList<String>();
        this.currentElement = new StringBuilder();
        this.idx = 0;

        String [] retVal = null;

        if( val != null )
        {
          while( this.idx < val.length() )
          {
            switch( this.state )
            {
                case DELIMITER:
                    this.process_delimiter(val);
                    break;
                case QUOTED:
                    this.process_quoted(val);
                    break;
                case UNQUOTED:
                    this.process_unquoted(val);
                    break;
                default:
                    break;
            }
          }

          this.addElement();

          retVal = new String [0];
          retVal = this.result.toArray(retVal);
        }

        return retVal;
    }

    private void process_delimiter( String val )
    {
        // Get rid of whitespace
        //while( Character.isWhitespace(val.charAt(this.idx)) ) this.idx++;

        if( val.startsWith(this.quoter,this.idx) )
        {
            this.idx += this.quoter.length();
            this.state = State.QUOTED;
        }
        else if( val.startsWith(this.delimiter,this.idx) )
        {
            this.addElement();
            this.idx += this.delimiter.length();
            this.state = State.DELIMITER;
        }
        else
        {
            this.currentElement.append(val.charAt(this.idx++) );
            this.state = State.UNQUOTED;
        }
    }

    private void process_quoted( String val )
    {
        if( val.startsWith(this.quoter,this.idx) )
        {
            // Not sure what to do here, but I guess
            // we can allow for premature un-quoting.
            this.idx += this.quoter.length();
            this.state = State.UNQUOTED;
        }
        else
        {
            this.currentElement.append(val.charAt(this.idx++) );
            this.state = State.QUOTED;
        }
    }

    private void process_unquoted( String val )
    {
       // Don't allow late-quoting.  I.e. the first character
       // after the delimiter is the only place to start a quote,
       // otherwise it is just consumed.
       if( val.startsWith(this.delimiter,this.idx) )
        {
            this.addElement();
            this.idx += this.delimiter.length();
            this.state = State.DELIMITER;
        }
        else
        {
            this.currentElement.append(val.charAt(this.idx++) );
            this.state = State.UNQUOTED;
        }
    }

    private void addElement() {
        this.result.add(this.currentElement.toString());
        this.currentElement = new StringBuilder();
    }
}
