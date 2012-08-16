public class PiShifts {
  public static void main( String [] args )
  {
    StringBuilder sb = new StringBuilder();
    int idx = 0;
    String temp = "";
    int tempInt = 0;
    while( idx < args[0].length() )
    {
      temp = "" + args[0].charAt(idx);
      sb.append( temp );
      tempInt = Integer.parseInt(temp);
      if( tempInt == 0 )
      {
        System.out.println( sb.toString() );
        sb = new StringBuilder();
        idx++;
      }
      else
      {
        idx += Integer.parseInt(temp);
      }
    }
    System.out.println( sb.toString() );
  }
}
