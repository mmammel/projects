import java.util.*;
import java.io.*;

public class SliceMap
{
  private Map sliceMap = new HashMap();

  public void insertSlice( Slice slice )
  {
    sliceMap.put( new SliceKey( slice ), slice );
  }

  public Slice getSlice( SliceKey key )
  {
    Slice retVal = null;

    retVal = (Slice)sliceMap.get( key );

    return retVal;
  }

  public List getSlicesForRange( long start, long end )
  {
    long currEnd = start;
    List retVal = new ArrayList();
    Slice tempSlice = null;
    SliceKey tempKey = null;

    while( currEnd <= end )
    {
      tempKey = new SliceKey( 0L, currEnd, "myhost", "myjvm" );
      tempSlice = this.getSlice( tempKey );

      if( tempSlice != null )
      {
        retVal.add( tempSlice );
        currEnd = tempSlice.endTime + 1;
      }
      else
      {
        currEnd += 29;
      }
    }

    return retVal;
  }

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    long start = 0L;
    long end = 0L;
    SliceMap SM = new SliceMap();
    List resultList = null;
    Slice tempSlice = null;

    SM.insertSlice( new Slice( 1186607877233L, 1186607877263L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877264L, 1186607877294L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877295L, 1186607877325L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877326L, 1186607877356L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877357L, 1186607877387L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877388L, 1186607877418L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877419L, 1186607877449L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877450L, 1186607877480L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877481L, 1186607877511L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877512L, 1186607877542L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877543L, 1186607877573L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877574L, 1186607877604L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877605L, 1186607877635L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877636L, 1186607877666L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877667L, 1186607877697L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877698L, 1186607877728L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877729L, 1186607877759L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877760L, 1186607877790L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877791L, 1186607877821L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877822L, 1186607877852L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877853L, 1186607877883L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877884L, 1186607877914L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877915L, 1186607877945L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877946L, 1186607877976L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607877977L, 1186607878007L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878008L, 1186607878038L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878039L, 1186607878069L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878070L, 1186607878100L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878101L, 1186607878131L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878132L, 1186607878162L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878163L, 1186607878193L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878194L, 1186607878224L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878225L, 1186607878255L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878256L, 1186607878286L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878287L, 1186607878317L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878318L, 1186607878348L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878349L, 1186607878379L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878380L, 1186607878410L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878411L, 1186607878441L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878442L, 1186607878472L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878473L, 1186607878503L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878504L, 1186607878534L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878535L, 1186607878565L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878566L, 1186607878596L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878597L, 1186607878627L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878628L, 1186607878658L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878659L, 1186607878689L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878690L, 1186607878720L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878721L, 1186607878751L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878752L, 1186607878782L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878783L, 1186607878813L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878814L, 1186607878844L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878845L, 1186607878875L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878876L, 1186607878906L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878907L, 1186607878937L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878938L, 1186607878968L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607878969L, 1186607878999L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879000L, 1186607879030L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879031L, 1186607879061L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879062L, 1186607879092L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879093L, 1186607879123L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879124L, 1186607879154L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879155L, 1186607879185L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879186L, 1186607879216L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879217L, 1186607879247L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879248L, 1186607879278L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879279L, 1186607879309L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879310L, 1186607879340L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879341L, 1186607879371L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879372L, 1186607879402L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879403L, 1186607879433L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879434L, 1186607879464L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879465L, 1186607879495L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879496L, 1186607879526L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879527L, 1186607879557L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879558L, 1186607879588L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879589L, 1186607879619L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879620L, 1186607879650L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879651L, 1186607879681L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879682L, 1186607879712L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879713L, 1186607879743L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879744L, 1186607879774L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879775L, 1186607879805L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879806L, 1186607879836L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879837L, 1186607879867L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879868L, 1186607879898L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879899L, 1186607879929L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879930L, 1186607879960L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879961L, 1186607879991L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607879992L, 1186607880022L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880023L, 1186607880053L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880054L, 1186607880084L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880085L, 1186607880115L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880116L, 1186607880146L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880147L, 1186607880177L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880178L, 1186607880208L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880209L, 1186607880239L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880240L, 1186607880270L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880271L, 1186607880301L, "myhost", "myjvm" ) );
    SM.insertSlice( new Slice( 1186607880302L, 1186607880332L, "myhost", "myjvm" ) );

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Enter two space separated values, the first being less than the second.  This is the range." );

      System.out.print("$ ");


      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          start = Long.parseLong( input_str.substring( 0, input_str.indexOf( " " ) ) );
          end = Long.parseLong( input_str.substring( input_str.lastIndexOf( " " ) + 1 ) );

          resultList = SM.getSlicesForRange( start, end );

          for( Iterator iter = resultList.iterator(); iter.hasNext(); )
          {
            tempSlice = (Slice)iter.next();
            System.out.println( tempSlice );
          }
        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Nice work." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
