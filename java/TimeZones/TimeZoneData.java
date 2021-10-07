import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TimeZone;
import java.util.regex.*;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

public class TimeZoneData
{

  /*
    Array built on 2021-10-07 11:30 US Eastern time.  To rebuild, run query:

    SELECT '{ "' + name + '", "' + current_utc_offset + '", "' + CAST(is_currently_dst AS VARCHAR) + '" },' FROM sys.time_zone_info;

    And change 0 to 1 on:
      Mid-Atlantic Standard Time
      Iran Standard Time
      Fiji Standard Time

    as they *can* be in DST, but are not currently.  This determined with the query:

    SELECT 
      tz.name,
      tz.is_currently_dst,
      REPLACE(SUBSTRING( CAST( CAST('2021-01-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Jan',
      REPLACE(SUBSTRING( CAST( CAST('2021-02-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Feb',
      REPLACE(SUBSTRING( CAST( CAST('2021-03-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Mar',
      REPLACE(SUBSTRING( CAST( CAST('2021-04-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Apr',
      REPLACE(SUBSTRING( CAST( CAST('2021-05-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'May',
      REPLACE(SUBSTRING( CAST( CAST('2021-06-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Jun',
      REPLACE(SUBSTRING( CAST( CAST('2021-07-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Jul',
      REPLACE(SUBSTRING( CAST( CAST('2021-08-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Aug',
      REPLACE(SUBSTRING( CAST( CAST('2021-09-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Sep',
      REPLACE(SUBSTRING( CAST( CAST('2021-10-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Oct',
      REPLACE(SUBSTRING( CAST( CAST('2021-11-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Nov',
      REPLACE(SUBSTRING( CAST( CAST('2021-12-01 00:00:00' AS DATETIME) AT TIME ZONE tz.name AS VARCHAR), 25, 99),'+','') as 'Dec'
    FROM 
      sys.time_zone_info tz;
   */

  private static final String [][] SQL_TIMEZONE_INFO = {
    { "Dateline Standard Time", "-12:00", "0" },
    { "UTC-11", "-11:00", "0" },
    { "Aleutian Standard Time", "-09:00", "1" },
    { "Hawaiian Standard Time", "-10:00", "0" },
    { "Marquesas Standard Time", "-09:30", "0" },
    { "Alaskan Standard Time", "-08:00", "1" },
    { "UTC-09", "-09:00", "0" },
    { "Pacific Standard Time (Mexico)", "-07:00", "1" },
    { "UTC-08", "-08:00", "0" },
    { "Pacific Standard Time", "-07:00", "1" },
    { "US Mountain Standard Time", "-07:00", "0" },
    { "Mountain Standard Time (Mexico)", "-06:00", "1" },
    { "Mountain Standard Time", "-06:00", "1" },
    { "Central America Standard Time", "-06:00", "0" },
    { "Easter Island Standard Time", "-05:00", "1" },
    { "Central Standard Time (Mexico)", "-05:00", "1" },
    { "Central Standard Time", "-05:00", "1" },
    { "Canada Central Standard Time", "-06:00", "0" },
    { "SA Pacific Standard Time", "-05:00", "0" },
    { "Eastern Standard Time (Mexico)", "-05:00", "0" },
    { "Eastern Standard Time", "-04:00", "1" },
    { "Haiti Standard Time", "-04:00", "1" },
    { "Cuba Standard Time", "-04:00", "1" },
    { "Turks And Caicos Standard Time", "-04:00", "1" },
    { "US Eastern Standard Time", "-04:00", "1" },
    { "Paraguay Standard Time", "-03:00", "1" },
    { "Atlantic Standard Time", "-03:00", "1" },
    { "Venezuela Standard Time", "-04:00", "0" },
    { "Central Brazilian Standard Time", "-04:00", "0" },
    { "SA Western Standard Time", "-04:00", "0" },
    { "Pacific SA Standard Time", "-03:00", "1" },
    { "Newfoundland Standard Time", "-02:30", "1" },
    { "Tocantins Standard Time", "-03:00", "0" },
    { "E. South America Standard Time", "-03:00", "0" },
    { "SA Eastern Standard Time", "-03:00", "0" },
    { "Argentina Standard Time", "-03:00", "0" },
    { "Greenland Standard Time", "-02:00", "1" },
    { "Montevideo Standard Time", "-03:00", "0" },
    { "Magallanes Standard Time", "-03:00", "0" },
    { "Saint Pierre Standard Time", "-02:00", "1" },
    { "Bahia Standard Time", "-03:00", "0" },
    { "UTC-02", "-02:00", "0" },
    { "Mid-Atlantic Standard Time", "-02:00", "1" },
    { "Azores Standard Time", "+00:00", "1" },
    { "Cape Verde Standard Time", "-01:00", "0" },
    { "UTC", "+00:00", "0" },
    { "GMT Standard Time", "+01:00", "1" },
    { "Sao Tome Standard Time", "+00:00", "0" },
    { "Greenwich Standard Time", "+00:00", "0" },
    { "Central Europe Standard Time", "+02:00", "1" },
    { "Romance Standard Time", "+02:00", "1" },
    { "Morocco Standard Time", "+01:00", "0" },
    { "Central European Standard Time", "+02:00", "1" },
    { "W. Europe Standard Time", "+02:00", "1" },
    { "W. Central Africa Standard Time", "+01:00", "0" },
    { "Jordan Standard Time", "+03:00", "1" },
    { "GTB Standard Time", "+03:00", "1" },
    { "Middle East Standard Time", "+03:00", "1" },
    { "Egypt Standard Time", "+02:00", "0" },
    { "E. Europe Standard Time", "+03:00", "1" },
    { "Syria Standard Time", "+03:00", "1" },
    { "West Bank Standard Time", "+03:00", "1" },
    { "South Africa Standard Time", "+02:00", "0" },
    { "FLE Standard Time", "+03:00", "1" },
    { "Israel Standard Time", "+03:00", "1" },
    { "Kaliningrad Standard Time", "+02:00", "0" },
    { "Sudan Standard Time", "+02:00", "0" },
    { "Libya Standard Time", "+02:00", "0" },
    { "Namibia Standard Time", "+02:00", "0" },
    { "Arabic Standard Time", "+03:00", "0" },
    { "Turkey Standard Time", "+03:00", "0" },
    { "Arab Standard Time", "+03:00", "0" },
    { "Belarus Standard Time", "+03:00", "0" },
    { "Russian Standard Time", "+03:00", "0" },
    { "E. Africa Standard Time", "+03:00", "0" },
    { "Iran Standard Time", "+03:30", "1" },
    { "Arabian Standard Time", "+04:00", "0" },
    { "Astrakhan Standard Time", "+04:00", "0" },
    { "Azerbaijan Standard Time", "+04:00", "0" },
    { "Russia Time Zone 3", "+04:00", "0" },
    { "Mauritius Standard Time", "+04:00", "0" },
    { "Saratov Standard Time", "+04:00", "0" },
    { "Georgian Standard Time", "+04:00", "0" },
    { "Volgograd Standard Time", "+04:00", "0" },
    { "Caucasus Standard Time", "+04:00", "0" },
    { "Afghanistan Standard Time", "+04:30", "0" },
    { "West Asia Standard Time", "+05:00", "0" },
    { "Ekaterinburg Standard Time", "+05:00", "0" },
    { "Pakistan Standard Time", "+05:00", "0" },
    { "Qyzylorda Standard Time", "+05:00", "0" },
    { "India Standard Time", "+05:30", "0" },
    { "Sri Lanka Standard Time", "+05:30", "0" },
    { "Nepal Standard Time", "+05:45", "0" },
    { "Central Asia Standard Time", "+06:00", "0" },
    { "Bangladesh Standard Time", "+06:00", "0" },
    { "Omsk Standard Time", "+06:00", "0" },
    { "Myanmar Standard Time", "+06:30", "0" },
    { "SE Asia Standard Time", "+07:00", "0" },
    { "Altai Standard Time", "+07:00", "0" },
    { "W. Mongolia Standard Time", "+07:00", "0" },
    { "North Asia Standard Time", "+07:00", "0" },
    { "N. Central Asia Standard Time", "+07:00", "0" },
    { "Tomsk Standard Time", "+07:00", "0" },
    { "China Standard Time", "+08:00", "0" },
    { "North Asia East Standard Time", "+08:00", "0" },
    { "Singapore Standard Time", "+08:00", "0" },
    { "Taipei Standard Time", "+08:00", "0" },
    { "Ulaanbaatar Standard Time", "+08:00", "0" },
    { "Aus Central W. Standard Time", "+08:45", "0" },
    { "Transbaikal Standard Time", "+09:00", "0" },
    { "Tokyo Standard Time", "+09:00", "0" },
    { "North Korea Standard Time", "+09:00", "0" },
    { "Korea Standard Time", "+09:00", "0" },
    { "Yakutsk Standard Time", "+09:00", "0" },
    { "West Pacific Standard Time", "+10:00", "0" },
    { "Tasmania Standard Time", "+11:00", "1" },
    { "Vladivostok Standard Time", "+10:00", "0" },
    { "Lord Howe Standard Time", "+11:00", "1" },
    { "Bougainville Standard Time", "+11:00", "0" },
    { "Russia Time Zone 10", "+11:00", "0" },
    { "Magadan Standard Time", "+11:00", "0" },
    { "Norfolk Standard Time", "+11:00", "0" },
    { "Sakhalin Standard Time", "+11:00", "0" },
    { "Central Pacific Standard Time", "+11:00", "0" },
    { "Russia Time Zone 11", "+12:00", "0" },
    { "UTC+12", "+12:00", "0" },
    { "Fiji Standard Time", "+12:00", "1" },
    { "Kamchatka Standard Time", "+13:00", "1" },
    { "W. Australia Standard Time", "+08:00", "0" },
    { "New Zealand Standard Time", "+13:00", "1" },
    { "AUS Eastern Standard Time", "+11:00", "1" },
    { "Chatham Islands Standard Time", "+13:45", "1" },
    { "E. Australia Standard Time", "+10:00", "0" },
    { "AUS Central Standard Time", "+09:30", "0" },
    { "Cen. Australia Standard Time", "+10:30", "1" },
    { "UTC+13", "+13:00", "0" },
    { "Tonga Standard Time", "+13:00", "0" },
    { "Samoa Standard Time", "+14:00", "1" },
    { "Line Islands Standard Time", "+14:00", "0" }
  };

  private static final Map<String,String> SQL_TIMEZONE_MAP = new HashMap<String,String>();

  static {
    for( String [] tz : SQL_TIMEZONE_INFO ) {
      SQL_TIMEZONE_MAP.put( tz[1]+"-"+tz[2], tz[0] );
    }
  }  


  private static final Pattern OFFSET_PATTERN = Pattern.compile("^([-+]?[0-9]{1,2}):([0-9]{2})$");

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    int offset = 0;
    String [] ids = null;
    boolean queryMode = false;
    

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hi - enter an offset (e.g. -3:00, 0:00, +1:30, etc.) and see a list of matching timezones with all of their info." );

      System.out.print("offset:> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) ) {
          break;
        } else if( input_str.equalsIgnoreCase("query") ) {
          queryMode = !queryMode;
          System.out.println( "Query mode " + (queryMode ? "enabled" : "disabled" ) );
        } else if( input_str.equalsIgnoreCase("all") ) {
          ids = TimeZone.getAvailableIDs();
          for( String id : ids ) {
            System.out.println( queryMode ? getTimeZoneQuery(TimeZone.getTimeZone(id)) : getTimeZoneString(TimeZone.getTimeZone( id )) );
          }
        } else if( input_str.matches(OFFSET_PATTERN.pattern() ) ) {
          offset = getMillisecOffset( input_str );
          if( offset != -1 ) {
            ids = TimeZone.getAvailableIDs( offset );
            if( ids != null && ids.length > 0 ) {
              for( String id : ids ) {
                System.out.println( queryMode ? getTimeZoneQuery(TimeZone.getTimeZone(id)) : getTimeZoneString(TimeZone.getTimeZone( id )) );
              }
            } else {
              System.out.println( "No matching timezones!" );
            }
          } else {
            System.out.println( "Bad offset, it has to be a positive or negative integer, genius" );
          }
        } else {
          TimeZone tz = TimeZone.getTimeZone( input_str );
          System.out.println( queryMode ? getTimeZoneQuery(tz) : getTimeZoneString(tz));
        }

        System.out.print( "\noffset:> " );
      }

      System.out.println( "Smell ya later" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  private static int getMillisecOffset( String offsetStr ) {
     int retVal = -1;
     int hours = 0;
     int mins = 0;

     if( offsetStr != null ) {
       Matcher m = OFFSET_PATTERN.matcher( offsetStr ); 
       if( m.matches() ) {
         try { 
           hours = Integer.parseInt( m.group(1) );
           mins = Integer.parseInt( m.group(2) );
           retVal = hours * 3600 * 1000 + mins * 60 * 1000;
         } catch( Exception e ) {
         }
       }
     }

     return retVal;
  }

  private static String getTimeZoneString( TimeZone tz ) {
    StringBuilder sb = new StringBuilder();
    if( tz != null ) {
      sb.append( tz.getID() ).append( "\t" ).append( tz.getDisplayName() ).append("\t").append( "DST: ").append(tz.observesDaylightTime());
      sb.append("\t").append( displayTimeZone( tz ) );
    }
    return sb.toString();
  }

  private static String getTimeZoneQuery( TimeZone tz ) { 
    String retVal = null;
    // Can this time zone ever be in DST?
    String dst = tz.observesDaylightTime() ? "1" : "0";
    // What is the *current* offset of this timezone?
    String offset = getOffsetString( tz );
    String key = offset + "-" + dst;
    String sqlTimeZone = SQL_TIMEZONE_MAP.get(key);
    if( sqlTimeZone != null ) {
      retVal = "UPDATE Customer c SET timeZone = '" + sqlTimeZone + "' WHERE timeZone = '" + tz.getID() + "'";
    } else {
      retVal = "ERROR! No mapping for " + tz.getID();
    } 

    return retVal;
  }

  private static String getOffsetString( TimeZone tz ) {
    long hours = TimeUnit.MILLISECONDS.toHours(tz.getOffset(Calendar.getInstance().getTime().getTime()));
    long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes(tz.getOffset(Calendar.getInstance().getTime().getTime())) - TimeUnit.HOURS.toMinutes(hours));
    String result = "";
    result = String.format("%+03d:%02d", hours, minutes);
    return result;
  }

  private static String displayTimeZone(TimeZone tz) {
    long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
    long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
    String result = "";
    if (hours > 0) {
      result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getDisplayName());
    } else {
      result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getDisplayName());
    }
    return result;
  }
}

