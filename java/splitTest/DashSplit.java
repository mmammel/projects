public class DashSplit {
  public static void main( String [] args ) {
    String toSplit = args[0];

    System.out.println("dash-splitting: \"" + toSplit + "\" and returning the first.");
    String result = toSplit.split("\\-")[0].trim();
    System.out.println( "Result: " + result );
  }
}
