import java.util.*;
import java.util.regex.Pattern;
import java.io.Serializable;

/**
 * User: mammelma
 * Date: Jun 15, 2010
 * Time: 12:40:11 PM
 *
 * This is used in the client config objects to control whether we will allow a login from
 * a particular IP to a particular client.
 *
 * The initializer string contains a comma separated list of patterns, where each pattern
 * must contain a regular IP pattern, but where the octets can take the following forms:
 * [0-9]{1,3}  // normal numeric octet
 * [0-9]{1,3}-[0-9]{1,3} // range
 *     *                // wild card
 *
 * some examples:
 *
 * 127.0.0.2-10 -> matches 127.0.0.2, 127.0.0.5, 127.0.0.10.  Does not match 127.0.0.11
 * 192.168.0-1.* -> matches 192.168.1.1, 192.168.0.1, 192.168.1.999, Does not match 192.168.2.1
 *
 * Each whitelist object contains a list of the individual patterns, and checks input
 * against each pattern, bailing out on the first successful match.
 *
 *
 */
public class IPWhiteList implements Serializable {
    private static final String IP_PATTERN = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
    List<OctetPattern []> validIPs = new ArrayList<OctetPattern []>();

    public IPWhiteList( String descriptor )
    {
      String [] patterns = descriptor.split(",");
      OctetPattern [] temp = null;
      for( String p : patterns )
      {
        temp = this.buildOctetArray( p );
        if( temp != null) this.validIPs.add(temp);
      }
    }

    private OctetPattern [] buildOctetArray( String ip )
    {
        OctetPattern [] retVal = null;
        OctetPattern tempOctet = null;
        String [] patterns = ip.split("\\.");
        if( patterns.length == 4 )
        {
          retVal = new OctetPattern[4];
          for( int i = 0; i < 4; i++ )
          {
            tempOctet = this.buildOctet(patterns[i]);
            if( tempOctet == null )
            {
                retVal = null;
                break;
            }

            retVal[i] = this.buildOctet(patterns[i]);
          }
        }

        return retVal;
    }

    private OctetPattern buildOctet( String oct )
    {
        OctetPattern retVal = null;

        if( oct.matches("[0-9]+") )
        {
            retVal = new OctetPattern();
            retVal.low = Integer.parseInt(oct);
        }
        else if( oct.matches("[0-9]+-[0-9]+") )
        {
            String [] array = oct.split("-");
            retVal = new OctetPattern();
            retVal.low = Integer.parseInt(array[0]);
            retVal.high = Integer.parseInt(array[1]);
        }
        else if( oct.equals("*") )
        {
            retVal = new OctetPattern();
        }

        return retVal;
    }

    public boolean inList( String ip )
    {
        boolean retVal = false;

        if( ip.matches(IP_PATTERN) )
        {
          String [] array = ip.split("\\.");
          for( OctetPattern [] octets : this.validIPs )
          {
            for( int i = 0; i < 4; i++ )
            {
                if( !(retVal = octets[i].check(array[i])) )
                {
                    break;
                }
            }

            if( retVal ) break;
          }
        }

        return retVal;
    }

    private class OctetPattern
    {
        private int low = -1;
        private int high = -1;

        boolean check( String str )
        {
            boolean retVal = false;
            if( str.matches("[0-9]+") )
            {
                retVal = this.check(Integer.parseInt(str) );
            }

            return retVal;
        }

        boolean check( int n )
        {
            boolean retVal = false;

            if( this.low < 0 )
            {
                retVal = true;
            }
            else if( this.high < 0 )
            {
                retVal = (n == this.low );
            }
            else
            {
                retVal = ((n >= this.low) && (n <= this.high));
            }

            return retVal;
        }
    }
}
