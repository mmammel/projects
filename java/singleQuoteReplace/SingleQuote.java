public class SingleQuote {
  public static void main( String [] args )
  {
    String name = "M&#39;ax";
    System.out.println( name.replaceAll( "&#39;","_"));
  }
}
