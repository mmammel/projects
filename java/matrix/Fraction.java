import java.util.regex.*;

public class Fraction
{
  private Long numerator;
  private Long denominator;
  private boolean positive = true;
  private static final Pattern FRACTION_PATTERN = Pattern.compile("-?([0-9]+)(?:/([0-9]+))?");

  public Fraction( Long n, Long d, boolean pos ) {
    this.numerator = Math.abs(n);
    this.denominator = Math.abs(d);
    this.positive = pos;
    this.reduce();
  }

  public Fraction( Long n, Long d ) {
    this(n,d,!((n < 0) ^ (d < 0)));
  }

  public Fraction( String s ) throws IllegalArgumentException {
    Matcher m = FRACTION_PATTERN.matcher(s);
    if( m.matches() ) {
      this.numerator = Long.parseLong(m.group(1));
      if( m.group(2) != null ) {
        this.denominator = Long.parseLong(m.group(2));
      } else {
        this.denominator = 1L;
      }

      positive = s.indexOf('-') != 0;
      this.reduce();
    } else {
      throw new IllegalArgumentException( "Invalid fraction string: " + s );
    }
  }

  public Long getNumerator() {
    return this.numerator;
  }

  public Long getDenominator() {
    return this.denominator;
  }

  private void reduce() {
    long greatest = gcd(numerator,denominator);
    if( greatest > 1 ) {
      this.numerator = this.numerator/greatest;
      this.denominator = this.denominator/greatest;
    }
  }

  public boolean isPositive() {
    return this.positive;
  }

  public String toString() {
    StringBuffer buff = new StringBuffer();
    if( !this.positive && this.numerator != 0L ) buff.append("-");
    buff.append(this.numerator);
    if( this.denominator > 1 ) buff.append("/").append(this.denominator);
    return buff.toString();
  }

  
  private static long gcd( long a, long b ) {
    if (b==0) 
      return a;
    else
      return gcd(b, a % b);
  }

  public static Fraction multiply( Fraction a, Fraction b ) {
    return new Fraction( (a.getNumerator() * b.getNumerator()),
                           (a.getDenominator() * b.getDenominator()),
                           !(a.isPositive() ^ b.isPositive()) );
  }

  public static Fraction divide( Fraction a, Fraction b ) {
    return new Fraction( (a.getNumerator() * b.getDenominator()),
                           (a.getDenominator() * b.getNumerator()),
                           !(a.isPositive() ^ b.isPositive()) );
  }

  public static Fraction inverse( Fraction f ) {
    return new Fraction( f.getDenominator(), f.getNumerator(), f.isPositive() );
  }
  
  public static Fraction add( Fraction a, Fraction b ) {
    long topLeft = a.getNumerator() * b.getDenominator();
    if( !a.isPositive() ) topLeft *= -1L;
    long topRight = a.getDenominator() * b.getNumerator();
    if( !b.isPositive() ) topRight *= -1L;
    long bottom = a.getDenominator() * b.getDenominator();
    
    return new Fraction( (topLeft + topRight), bottom );
  }

  public static Fraction subtract( Fraction a, Fraction b ) {
    long topLeft = a.getNumerator() * b.getDenominator();
    if( !a.isPositive() ) topLeft *= -1L;
    long topRight = a.getDenominator() * b.getNumerator();
    if( !b.isPositive() ) topRight *= -1L;
    long bottom = a.getDenominator() * b.getDenominator();
    
    return new Fraction( (topLeft - topRight), bottom );
  }

  public static void main(String [] args) {
    try {
      Fraction fract1 = new Fraction(args[0]);
      Fraction fract2 = new Fraction(args[1]);
      
      System.out.println( "The fractions [a,b]: [" + fract1 + "," + fract2 + "]" );
      System.out.println( "a*b: " + Fraction.multiply(fract1,fract2) );
      System.out.println( "a/b: " + Fraction.divide(fract1,fract2) );
      System.out.println( "a+b: " + Fraction.add(fract1,fract2) );
      System.out.println( "a-b: " + Fraction.subtract(fract1,fract2) );
    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
    }
  }
}

