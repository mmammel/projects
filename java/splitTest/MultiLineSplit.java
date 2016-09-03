public class MultiLineSplit {
  public static void main( String [] args ) {
    StringBuilder testString = new StringBuilder();

    testString.append( "Max@mammel.com;        homama@mail.com\n\nmax@gmail.com,,hello@you.com\n\r\n\n\n  ;mag@gmial.com,hello@anothermail.com anotherone@mail.com;email.com");

    String toSplit = args.length > 0 ? args[0] : testString.toString();

    System.out.println("multi-line-splitting: \"" + toSplit + "\" on whitespace (including newline), comma and semi-colon");
    String [] result = toSplit.split("[\\s,;]+");
    //String [] result = toSplit.replaceAll("[\\s]+", "," ).split("[,;]+");

    for( String res : result ) {
      System.out.println( "[" + res + "]" );
    }

  }
}
