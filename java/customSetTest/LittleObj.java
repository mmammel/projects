import java.util.Set;
import java.util.HashSet;

public class LittleObj {
  private String name;
  private char code;

    public LittleObj( String name, char code ) {
      this.name = name;
      this.code = code;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + code;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      LittleObj other = (LittleObj) obj;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      if (code != other.code)
        return false;
      return true;
    }

  public static void main( String [] args ) {
    Set<LittleObj> myset = new HashSet<LittleObj>();
    byte bt = 80;
    LittleObj a = new LittleObj( "foo", 'b' );
    LittleObj b = new LittleObj( "max", (char)bt );
    myset.add( a );
    myset.add( b );
    System.out.println( "Set size: " + myset.size() );
    LittleObj c = new LittleObj( "foo", 'b' );
    myset.add( c );
    System.out.println( "Set size: " + myset.size() );

  }

}
