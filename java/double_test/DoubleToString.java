public class DoubleToString
{
  public static void main( String [] args )
  {
    double fin = 1230.1d;
    System.out.println( "Value: " + String.format("%#05.2f",fin) );
    System.out.println( "Value: " + String.format("%.2f",fin) );
  }

}
