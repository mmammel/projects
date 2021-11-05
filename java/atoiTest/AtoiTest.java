import java.util.regex.*;

public class AtoiTest {
    private static Pattern NUM_PATTERN = Pattern.compile( "^[ ]*?([+-]?)([0-9]+?)[^0-9]+?.*$");
    public static void  main(String [] s) {
        String refined = s[0].replaceAll(NUM_PATTERN.pattern(),"$1$2");
        System.out.println("["+refined+"]");
    }
}
