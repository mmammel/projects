public class NotWordSplit {
  public static void main( String [] args ) {
    String toSplit = args[0];

    System.out.println("notword-splitting: \"" + toSplit + "\" and returning the first.");
    String result = toSplit.split("[ \t\n\r]")[0].trim();
    System.out.println( "Result: " + result );
  }
}
