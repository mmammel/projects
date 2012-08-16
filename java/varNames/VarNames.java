import java.util.List;
import java.util.ArrayList;


public class VarNames
{
  public String $foobar = null;
  public int $barfoo = 0;
  public String [] $foonuts = null;

  public VarNames()
  {
    this( "Max", 1729, 13 );
  }

  public VarNames(String str, int a, int b )
  {
    $foobar = str;
    $barfoo = a;
    $foonuts = new String [ b ];
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "Name: " + $foobar ).append("\n");
    sb.append( "Size: " + $barfoo ).append("\n");
    sb.append( "ArrayLen: " + $foonuts );
    return sb.toString();
  }


public static String computeUniqueFieldNamePrefix(List fieldNames) {
    int length = fieldNames.size();
    if (length == 0)
      return "";

// Choose a candidate prefix character.
// Through each iteration:
//  1) The "maximum" character is determined;
//  2) If the candidate character does not match
// the first character of a field name, then it is promoted to the
// next iteration; and
//  3) If the candidate character does match
// the first character of a field name, the candidate is changed
// to one character past the maxiumum character seen so far.

    char candidate = '$';
    char max = (char)0;
    int count = 0;
    for (int i = 0; i < length; i += 1) {
      String string = (String)fieldNames.get(i);
      if (string == null || string.length() == 0)
        continue;
      char c = string.charAt(0);
      if (c == max)
        count += 1;
      else if (c > max) {
        max = (c <= 'z')? c: 'z';
        count = 1;
      }
      if (c == candidate) {
        candidate = nchar0(max);
      }
    }

// At this point:
//  1) The candidate is not the last character possible ('z') and had no conflicts
// and can be chosen as a unique prefix for field names that
// does not conflict with existing field names.  This is true since,
// if the candidate character is not 'z', then it is 1 past the maximum
// character seen as the first character of field names or did not
// conflict with the first character of any field name by virtue of
// the loop above.
//  2) The candidate is the the last character possible ('z') and so subsequent
// characters must be considered.

    if (candidate != 'z') return String.valueOf(candidate);

// Generate a subset of field names whose first character matches
// the failed candidate and repeat the steps above.

    String[] conflicts = new String[count];
    int nlength = 0;
    for (int i = 0; i < length; i += 1) {
      String string = (String)fieldNames.get(i);
      if (string == null || string.length() <= 1)
        continue;
      char c = string.charAt(0);
      if (c == candidate)
        conflicts[(nlength ++)] = string;
    }
    return computeUniquePrefix(conflicts, nlength, String.valueOf(candidate));
  }



private static String computeUniquePrefix(String[] strings, int length, String prefix) {
    int offset = prefix.length();

// Choose a candidate character.
// Through each iteration:
//  1) The "maximum" character is determined;
//  2) If the candidate character does not match
// the offset character of a string, then it is promoted to the
// next iteration; and
//  3) If the candidate character does match
// the offset character of a string, the candidate is changed
// to one character past the maxiumum character seen so far.

    char candidate = '$';
    char max = (char)0;
    int count = 0;
    for (int i = 0; i < length; i += 1) {
      String string = strings[i];
      if (string == null || string.length() <= offset)
        continue;
      char c = string.charAt(offset);
      if (c == max)
        count += 1;
      else if (c > max) {
        max = (c <= 'z')? c: 'z';
        count = 1;
      }
      if (c == candidate)
        candidate = nchar(max);
    }

// At this point:
//  1) The candidate is not the last character possible ('z') and had no conflicts
// and can be chosen as a unique prefix for the strings that
// does not conflict with existing method names.  This is true since,
// if the candidate character is not 'z', then it is 1 past the maximum
// character seen as the offset character of strings or did not
// conflict with the first character of any string by virtue of
// the loop above.
//  2) The candidate is the the last character possible ('z') and so subsequent
// characters must be considered.

    if (candidate != 'z') return prefix + candidate;

// Generate a subset of method names whose first character matches
// the failed candidate and repeat the steps above.

    int nlength = 0;
    for (int i = 0; i < length; i += 1) {
      String string = strings[i];
      if (string == null || string.length() <= offset + 1)
        continue;
      char c = string.charAt(offset);
      if (c == candidate)
        strings[(nlength ++)] = string;
    }
    return computeUniquePrefix(strings, nlength, prefix + candidate);
  }

  private static char nchar0(char c) {
      if (c < '$')
        return '$';
      if (c < 'A')
        return 'A';
      if (c > 'z')
        return 'z';
      return nchar[c - '$'];
    }

  /**
   * Determine the next valid identifier character.
   *
   * <p>
   * If the input is <tt>'z'</tt> then this method returns <tt>'z'</tt>.
   *
   * @param c the character
   *
   * @return the next valid identifier character
   **/

    private static char nchar(char c) {
      if (c < '$')
        return '$';
      if (c > 'z')
        return 'z';
      return nchar[c - '$'];
    }

    private static final char[] nchar = new char['z' - '$' + 1];

    static {
      char c = '$';
      while (c < '0') {
        nchar[c - '$'] = '0';
        c += 1;
      }
      while (c < '9') {
        nchar[c - '$'] = (char)(c + 1);
        c += 1;
      }
      while (c < 'A') {
        nchar[c - '$'] = 'A';
        c += 1;
      }
      while (c < 'Z') {
        nchar[c - '$'] = (char)(c + 1);
        c += 1;
      }
      while (c < '_') {
        nchar[c - '$'] = '_';
        c += 1;
      }
      while (c < 'a') {
        nchar[c - '$'] = 'a';
        c += 1;
      }
      while (c < 'z') {
        nchar[c - '$'] = (char)(c + 1);
        c += 1;
      }
      nchar[c - '$'] = 'z';
  }




  public static void main( String [] args )
  {
    VarNames VN = new VarNames( "Jomama", 23, 5 );

    List fields = new ArrayList();
    fields.add( "Afoobar" );
    fields.add("$ammel" );
    fields.add("$ab" );
    fields.add("$jomama" );
    fields.add("$_foo" );
    fields.add("$_foo" );
    fields.add("$fieldname" );
    fields.add("$var" );
    fields.add("$a" );
    fields.add("$a" );
    fields.add("$xx" );
    fields.add("$c" );
    fields.add("$foo");
    fields.add("#foo");
    fields.add("@foo");
    fields.add("%foo");
    fields.add("^foo");
    fields.add("&foo");
    fields.add("*foo");
    fields.add("!foo");
    fields.add("afoo");
    fields.add("bfoo");
    fields.add("cfoo");
    fields.add("dfoo");
    fields.add("efoo");
    fields.add("ffoo");
    fields.add("gfoo");
    fields.add("hfoo");
    fields.add("ifoo");
    fields.add("jfoo");
    fields.add("kfoo");
    fields.add("lfoo");
    fields.add("mfoo");
    fields.add("nfoo");
    fields.add("ofoo");
    fields.add("pfoo");
    fields.add("qfoo");
    fields.add("rfoo");
    fields.add("sfoo");
    fields.add("tfoo");
    fields.add("ufoo");
    fields.add("vfoo");
    fields.add("wfoo");
    fields.add("xfoo");
    fields.add("yfoo");
    fields.add("zfoo");
    fields.add("Afoo");
    fields.add("Bfoo");
    fields.add("Cfoo");
    fields.add("Dfoo");
    fields.add("Efoo");
    fields.add("Ffoo");
    fields.add("Gfoo");
    fields.add("Hfoo");
    fields.add("Ifoo");
    fields.add("Jfoo");
    fields.add("Kfoo");
    fields.add("Lfoo");
    fields.add("Mfoo");
    fields.add("Nfoo");
    fields.add("Ofoo");
    fields.add("Pfoo");
    fields.add("Qfoo");
    fields.add("Rfoo");
    fields.add("Sfoo");
    fields.add("Tfoo");
    fields.add("Ufoo");
    fields.add("Vfoo");
    fields.add("Wfoo");
    fields.add("Xfoo");
    fields.add("Yfoo");
    fields.add("Zfoo");
    fields.add("1foo");
    fields.add("2foo");
    fields.add("3foo");
    fields.add("4foo");
    fields.add("5foo");
    fields.add("7foo");
    fields.add("8foo");
    fields.add("9foo");
    fields.add("0foo");
    fields.add("z$foo");

    System.out.println( VarNames.computeUniqueFieldNamePrefix(fields) );

    System.out.println( VN );
  }



}
