import java.math.BigInteger;

public class ModuloTest {

  public static BigInteger NEGATIVEONE = new BigInteger("-1");

  public static void main( String [] args ) {
    int n = 0;
    BigInteger a = new BigInteger("2");
    long maxpower = 100L;
    BigInteger num = null;

    if( args.length == 0 ) {
      printUsage();
      System.exit(1);
    } else {
      int i = 0;
      while( i < args.length ) {
        if( args[i].equals( "-m" ) ) {
          num = new BigInteger(args[++i]);
        } else if( args[i].equals("-a") ) {
          a = new BigInteger(args[++i]);
        } else if( args[i].equals("-p") ) {
          try {
            maxpower = Long.parseLong( args[++i] );
          } catch( NumberFormatException nfe ) {
            System.out.println( "Bad value passed to -a");
            printUsage();
            System.exit(1);
          }
        } else if( args[i].equals("-h") ) {
          printUsage();
          System.exit(0);
        } else {
          System.out.println( "Unrecognized option: " + args[i] );
          printUsage();
          System.exit(1);
        }
        i++;
      }
    }

    if( num == null ) {
      System.out.println( "Parameter -m <modnumber> is required" );
      printUsage();
      System.exit(1);
    }

    BigInteger ans = null;

    System.out.println( "N = " + num + " [" + num.toString(2) + "]" );

    for( long i = 0L; i < maxpower; i++ ) {
      ans = a.modPow( BigInteger.valueOf(i), num );
      //System.out.println( a + "^" + i + " (mod " + num + ") === " + (ans.add(BigInteger.ONE).equals( num ) ? NEGATIVEONE : ans) + " [" + ans.toString(2) + "][diff: " + num.subtract(ans) + "]" );
      System.out.println( a + "^" + i + " === " + (ans.add(BigInteger.ONE).equals( num ) ? NEGATIVEONE : ans) + " (mod " + num + ")" );
    }
  }

  public static void printUsage() {
    StringBuilder sb = new StringBuilder();
    sb.append( "Usage: java ModuloTest [-a number] [-p maxpower] -m modnumber [-h]\n");
    sb.append( "   -a number: The number to raise to powers and mod against mod (default: 2)\n");
    sb.append( "   -p maxpower: The maximum power to raise number to (default: 100)\n");
    sb.append( "   -m modnumber: The number to mod against\n" );
    sb.append( "   -h: ignore other parameters and display this message");

    System.out.println( sb.toString() );
  }

  public static BigInteger generateN( int power ) {
    double pow2 = Math.pow( 2.0d, Integer.valueOf(power).doubleValue() );
    StringBuilder sb = new StringBuilder("1");
    for( int i = 1; i < Double.valueOf(pow2).intValue(); i++ ) {
      sb.append("0");
    }
    sb.append("1");
    return new BigInteger(sb.toString());
  }

}
