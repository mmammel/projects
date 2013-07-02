import java.util.regex.*;


public class ReplaceAllTest
{
  public static Pattern JOBS_PATTERN = Pattern.compile("<Jobs>(.*?)</Jobs>");
  public static void main( String [] args )
  {
    String foo = "asdfasdfasdf<Jobs><Job>asdfa</Job><Job>hfhf</Job></Jobs>asdfasdfasdfasdffoobar<Jobs><Job>23423</Job></Jobs>blah blah";
    String result = "";

    Matcher m = JOBS_PATTERN.matcher( foo );

    while( m.find() )
    {
      result = result+m.group(1);
    } 

    System.out.println( result ); 
  }

}
