import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class StarDataProcessor 
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    String [] temp = null;
    String [] dataRow = null;
    int tempDist = 0, maxDist = 0;
    double tempMag = 0.0, maxMag = 0, minMag = 0;
    List<String[]> data = new ArrayList<String[]>();
    StringBuilder output = new StringBuilder();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        temp = input_str.split("\\|");
        // array is:
        // x,x,x,x,Const.,RA,DEC,elon,elat,dist,mag,x,x
        dataRow = new String [5];
        data.add( dataRow );
        dataRow[0] = temp[4].trim();  // constellation
        // dataRow[1] = ""+convertHMS(temp[5].trim());  // RA -> theta
        // dataRow[2] = ""+convertDMS(temp[6].trim());  // Dec -> phi
        dataRow[1] = ""+convertELON(temp[7].trim());
        dataRow[2] = ""+convertELAT(temp[8].trim());
        dataRow[3] = temp[9].trim();  // dist (ly)
        dataRow[4] = temp[10].trim(); // visual magnitude

        tempDist = Integer.parseInt(dataRow[3]);
        tempMag = Double.parseDouble(dataRow[4]);

        if( tempDist > maxDist ) maxDist = tempDist;
        if( tempMag < maxMag ) maxMag = tempMag;
        if( tempMag > minMag ) minMag = tempMag;
      }

      output.append("[\n");
      boolean first = true;
      for( String [] row : data ) {
        if( first ) {
          first = false;
        } else {
          output.append(",\n");
        }

        output.append("  [").append("'").append(row[0]).append("',")
              .append(row[1]).append(",").append(row[2]).append(",")
              .append(row[3]).append(",").append(row[4]).append("]");
      }

      output.append("];\n\n");

      System.out.println( output.toString() );
      System.out.println( "var maxDistance = " + maxDist + ";\n");
      System.out.println( "var minMagnitude = " + minMag + ";\n");
      System.out.println( "var maxMagnitude = " + maxMag + ";\n");
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public static double convertELON( String elon ) {
    double retVal = 0.0;
    
    try {
      retVal = Double.parseDouble( elon );
      retVal = (retVal / 180.0 ) * Math.PI;
    } catch( Exception e ) {
      System.out.println( e.toString() );
    }

    return retVal;
  }

  public static double convertELAT( String elat ) {
    double retVal = 0.0;
    
    try {
      retVal = Double.parseDouble( elat );

      if( retVal >= 0 ) {
        retVal = 90.0 - retVal;
      } else {
        retVal = 90.0 + (-1.0 * retVal);
      }

      retVal = (retVal / 180.0 ) * Math.PI;
    } catch( Exception e ) {
      System.out.println( e.toString() );
    }

    return retVal;
  }

  // takes "23 59 1.23" and returns value in radians, 0 <= v <= 2*PI
  public static double convertHMS( String hms ) {
    double retVal = 0.0;
    String [] arr = hms.split("[ ]+");
    try {
      double hours = Double.parseDouble( arr[0] );
      double mins = Double.parseDouble( arr[1] );
      double seconds = Double.parseDouble( arr[2] );

      hours *= 3600.0;
      mins *= 60.0;
      seconds += (hours + mins);

      retVal = (seconds / (24.0 * 3600.0)) * Math.PI;

    } catch( Exception e ) {
      System.out.println( e.toString() );
    }

    return retVal;
  }

  // convert -35 54 21 to radians from the Z-Axis
  public static double convertDMS( String dms ) {
    double retVal = 0.0;
    String [] arr = dms.split("[ ]+");
    try {
      double degrees = Double.parseDouble( arr[0] );
      double arcmins = Double.parseDouble( arr[1] );
      double arcseconds = Double.parseDouble( arr[2] );

      degrees *= 3600.0;
      arcmins *= 60.0;
      arcseconds += (degrees + arcmins);

      retVal = ((arcseconds / 3600.0) / 180.0) * Math.PI;
      if( retVal >= 0 ) {
        retVal = (Math.PI / 2.0) - retVal;
      } else {
        retVal += Math.PI;
      }
    } catch( Exception e ) {
      System.out.println( e.toString() );
    }

    return retVal;
  }
}

