import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;

public class Bucket {
  private BigInteger key;
  private Set<Bucket> parentBuckets = null;
  private List<Letter> letters = null;
  private String rootWord = null;
  private Set<String> subWords;
  private Set<Letter> letterSet;
  private boolean hasDuplicates = false;
 
  public Bucket( String word ) {
    this.rootWord = word.toUpperCase();
    this.letters = Letter.listForWord( word );
    this.key = Letter.getWordKey( word );
    this.parentBuckets = new HashSet<Bucket>();
    this.letterSet = EnumSet.noneOf( Letter.class );
    for( Letter l : this.letters ) {
      this.letterSet.add( l );
    }

    this.hasDuplicates =  this.letters.size() > this.letterSet.size(); 

    this.subWords = new HashSet<String>();
    this.subWords.add( this.rootWord );
  } 

  public boolean test( Bucket b ) {
    boolean retVal = false;
    if( this.letterSet.containsAll( b.getLetterSet() ) ) {
      if( b.hasDuplicates() ) {
        retVal = this.key.remainder( b.getKey() ).equals( BigInteger.ZERO );
      } else {
        retVal = true;
      }
    }

    return true;
  }

  public Set<Letter> getLetterSet() {
    return this.letterSet;
  }

  public void addSubWord( String w ) {
    this.subWords.add( w );
  }

  public void addParent( Bucket b ) {
    this.parentBuckets.add( b );
    b.addSubWord( this.rootWord );
  }

  public Set<Bucket> getParents() {
    return this.parentBuckets;
  }

  public boolean equals( Object o ) {
    Bucket other = (Bucket)o;
    return this.key.equals( other.key );
  }

  public boolean hasDuplicates() {
    return this.hasDuplicates;
  }

  public int hashCode() {
    return this.key.hashCode();
  }

  public BigInteger getKey() {
    return this.key;
  }

  public String getRootWord() {
    return this.rootWord;
  }

  public Set<Bucket> getParentBuckets() {
    return this.parentBuckets;
  }

}
