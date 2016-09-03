public class CommaSplit {
  public static void main( String [] args ) {
    String toSplit = args[0];

    System.out.println("comma-splitting: \"" + toSplit + "\" and returning the first.");
    String result = toSplit.split(",")[0].trim();
    System.out.println( "Result: " + result );
  }
}
