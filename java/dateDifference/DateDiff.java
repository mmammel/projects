import java.text.SimpleDateFormat;
import java.util.*;

public class DateDiff {

  public static final int YEAR = 0;
  public static final int MONTH = 1;
  public static final int DAY = 2;
  public static final int HOUR = 3;
  public static final int MINUTE = 4;

  public static final int [] UNITS = {Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,Calendar.HOUR,Calendar.MINUTE};

  public static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  public static void main(String [] args)
  {
    Calendar now = Calendar.getInstance();
    Calendar input = Calendar.getInstance();
    Calendar clone = (Calendar)now.clone();


    int [] counter = new int [5];
    try
    {
       input.setTime( sdf.parse(args[0]));

       int tempCount = 0;
       int unitIdx = 0;

       for( int unit : UNITS )
       {
         while( clone.before( input ) )
         {
           clone.add( unit, 1 );
           tempCount++;
         }

         counter[unitIdx] = tempCount > 0 ? tempCount - 1 : 0;
         now.add(unit,counter[unitIdx++]);
         clone = (Calendar)now.clone();
         tempCount = 0;
       }

       String result = "";
       String tempUnit = "";
       StringBuffer buff = new StringBuffer();
       for( int i = 0; i < counter.length; i++ )
       {
         switch(i)
         {
           case YEAR:
              tempUnit = counter[i] > 1 ? "years" : "year";
              break;
           case MONTH:
              tempUnit = counter[i] > 1 ? "months" : "month";
              break;
           case DAY:
              tempUnit = counter[i] > 1 ? "days" : "day";
              break;
           case HOUR:
              tempUnit = counter[i] > 1 ? "hours" : "hour";
              break;
           case MINUTE:
              tempUnit = counter[i] > 1 ? "minutes" : "minute";
              break;
         }

         if( counter[i] > 0 )
         {
           buff.append( "" + counter[i] + " " + tempUnit + ", " );
         }
       }

       result = buff.toString();
       System.out.println( result.substring(0,result.length() - 2 ) );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception!! " + e.toString() );
    }

  }
}