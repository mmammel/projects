public class Factor {
  public static void main( String [] args ) {
    long num = 0L;
    if( args.length == 1 && args[0].matches("[0-9]+")) {
      num = Long.parseLong(args[0]);
    } else {
      System.exit(1);
    }

    long f = 2L;
    long half = num / 2;
    while( f <= num / f  ) {
      while( num % f == 0 ) {
        System.out.println( f );
        num = num / f;
      }
      f = f + (f==2L ? 1L : 2L);
    }

    if( num > 1 ) System.out.println( num );
    
  }
}
