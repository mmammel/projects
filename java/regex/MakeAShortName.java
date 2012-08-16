import java.util.regex.*;

public class MakeAShortName {

  static Pattern thePattern = Pattern.compile("[-\"\'\\s]");


  public static void main( String [] args )
  {
    System.out.println( thePattern.matcher( args[0] ).replaceAll("").toLowerCase());
  }

}

