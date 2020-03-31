import java.util.List;
import java.util.ArrayList;

public class FourDigits
{
  //public static final String [] single_terminals = {"a", ".a", ".aaaaaaaaaaaaa" };
  public static final String [] single_terminals = {"a"};

  //public static final String [] double_terminals = { "ab", "ba", ".ab", ".ba", "a.b", "b.a" };
  public static final String [] double_terminals = { "ab", "ba" };


  public static final String [] triple_terminals = { "abc", "acb", "bac", "bca", "cab", "cba" }; //,
                                                     //"a.bc", "a.cb", "b.ac", "b.ca", "c.ab", "c.ba",
                                                     //"ab.c", "ac.b", "ba.c", "bc.a", "ca.b", "cb.a",
                                                     //".abc", ".acb", ".bac", ".bca", ".cab", ".cba" };

  public static final String [] quad_terminals = { "abcd", "abdc", "bacd", "badc", "acbd", "acdb", "cabd", "cadb",
                                                   "adbc", "adcb", "dabc", "dacb", "bcad", "bcda", "cbad", "cbda",
                                                   "bdac", "bdca", "dbac", "dbca", "cdab", "cdba", "dcab", "dcba" }; //,
                                                   //"a.bcd", "a.bdc", "b.acd", "b.adc", "a.cbd", "a.cdb", "c.abd", "c.adb",
                                                   //"a.dbc", "a.dcb", "d.abc", "d.acb", "b.cad", "b.cda", "c.bad", "c.bda",
                                                   //"b.dac", "b.dca", "d.bac", "d.bca", "c.dab", "c.dba", "d.cab", "d.cba",
                                                   //"ab.cd", "ab.dc", "ba.cd", "ba.dc", "ac.bd", "ac.db", "ca.bd", "ca.db",
                                                   //"ad.bc", "ad.cb", "da.bc", "da.cb", "bc.ad", "bc.da", "cb.ad", "cb.da",
                                                   //"bd.ac", "bd.ca", "db.ac", "db.ca", "cd.ab", "cd.ba", "dc.ab", "dc.ba",
                                                   //"abc.d", "abd.c", "bac.d", "bad.c", "acb.d", "acd.b", "cab.d", "cad.b",
                                                   //"adb.c", "adc.b", "dab.c", "dac.b", "bca.d", "bcd.a", "cba.d", "cbd.a",
                                                   //"bda.c", "bdc.a", "dba.c", "dbc.a", "cda.b", "cdb.a", "dca.b", "dcb.a",
                                                   //".abcd", ".abdc", ".bacd", ".badc", ".acbd", ".acdb", ".cabd", ".cadb",
                                                   //".adbc", ".adcb", ".dabc", ".dacb", ".bcad", ".bcda", ".cbad", ".cbda",
                                                   //".bdac", ".bdca", ".dbac", ".dbca", ".cdab", ".cdba", ".dcab", ".dcba" };


  public static final String POW1 = "A^B";
  public static final String POW2 = "B^A";
  public static final String ADD1  = "A+B";
  public static final String ADD2  = "B+A";
  public static final String SUB1 = "A-B";
  public static final String SUB2 = "B-A";
  public static final String MULT1 = "A*B";
  public static final String MULT2 = "B*A";
  public static final String DIV1 = "A/B";
  public static final String DIV2 = "B/A";

  public static final String [] double_non_terminals = { POW1, POW2, ADD1, ADD2, SUB1, SUB2, MULT1, MULT2, DIV1, DIV2 };

  public static final String ID = "A";
  public static final String SQRT = "sqrt(A)";
  public static final String FACT = "A!";

  public static final String [] single_non_terminals = { ID }; //, SQRT, FACT };
  /**
   *
   */
  public void getSingleTerminalSets( int x, int y, int z, int k )
  {
    int [] digits = new int [4];
    String [] terminals = new String [4];
    Expression t0, t1, t2, t3;
    digits[0] = x;
    digits[1] = y;
    digits[2] = z;
    digits[3] = k;

    int terminalCount = single_terminals.length;

    for( int i = 0; i < terminalCount; i++ )
    {
      terminals[0] = getReplaceString( x, single_terminals[i] );
      t0 = new TerminalExpression( terminals[0] );

      for( int j = 0; j < terminalCount; j++ )
      {
        terminals[1] = getReplaceString( y, single_terminals[j] );
        t1 = new TerminalExpression( terminals[1] );

        for( int l = 0; l < terminalCount; l++ )
        {
          terminals[2] = getReplaceString( z, single_terminals[l] );
          t2 = new TerminalExpression( terminals[2] );

          for( int m = 0; m < terminalCount; m++ ) {
 
            terminals[3] = getReplaceString( k, single_terminals[m] );
            t3 = new TerminalExpression( terminals[3] );

            this.doAllExpressions( t0, t1, t2, t3 );
            //this.doAllExpressions( t0, t1, t3, t2 );
            //this.doAllExpressions( t1, t0, t2, t3 );
            //this.doAllExpressions( t1, t0, t3, t2 );

            //this.doAllExpressions( t0, t2, t1, t3 );
            //this.doAllExpressions( t0, t2, t3, t1 );
            //this.doAllExpressions( t2, t0, t1, t3 );
            //this.doAllExpressions( t2, t0, t3, t1 );

            //this.doAllExpressions( t0, t3, t1, t2 );
            //this.doAllExpressions( t0, t3, t2, t1 );
            //this.doAllExpressions( t3, t0, t1, t2 );
            //this.doAllExpressions( t3, t0, t2, t1 );

            //this.doAllExpressions( t1, t2, t0, t3 );
            //this.doAllExpressions( t1, t2, t3, t0 );
            //this.doAllExpressions( t2, t1, t0, t3 );
            //this.doAllExpressions( t2, t1, t3, t0 );

            //this.doAllExpressions( t1, t3, t0, t2 );
            //this.doAllExpressions( t1, t3, t2, t0 );
            //this.doAllExpressions( t3, t1, t0, t2 );
            //this.doAllExpressions( t3, t1, t2, t0 );

            //this.doAllExpressions( t2, t3, t0, t1 );
            //this.doAllExpressions( t2, t3, t1, t0 );
            //this.doAllExpressions( t3, t2, t0, t1 );
            //this.doAllExpressions( t3, t2, t1, t0 );

          }

        }
      }
    }

  }

  /**
   *
   */
  public void getDoubleTerminalSets( int x, int y, int z, int k )
  {
    int [] digits = new int [4];
    String tempTerminal1 = null, tempTerminal2 = null, tempTerminal3 = null, tempTerminal4 = null;
    Expression t1, t2, t3, t4;

    digits[0] = x;
    digits[1] = y;
    digits[2] = z;
    digits[3] = k;

    // first do double-doubles
    for( int i = 0; i < double_terminals.length; i++ )
    {
      for( int j = 0; j < double_terminals.length; j++ )
      {
        tempTerminal1 = this.getReplaceString( digits[0], digits[1], double_terminals[i] );
        tempTerminal2 = this.getReplaceString( digits[2], digits[3], double_terminals[j] );
        t1 = new TerminalExpression(tempTerminal1);
        t2 = new TerminalExpression(tempTerminal2);
        this.doAllExpressions( t1, t2);
        
        tempTerminal1 = this.getReplaceString( digits[0], digits[2], double_terminals[i] );
        tempTerminal2 = this.getReplaceString( digits[1], digits[3], double_terminals[j] );
        t1 = new TerminalExpression(tempTerminal1);
        t2 = new TerminalExpression(tempTerminal2);
        this.doAllExpressions( t1, t2);

        tempTerminal1 = this.getReplaceString( digits[0], digits[3], double_terminals[i] );
        tempTerminal2 = this.getReplaceString( digits[1], digits[2], double_terminals[j] );
        t1 = new TerminalExpression(tempTerminal1);
        t2 = new TerminalExpression(tempTerminal2);
        this.doAllExpressions( t1, t2);
      }
    }

    int [][] pairIdxs = {
      { 0, 1, 2, 3 },
      { 0, 2, 1, 3 },
      { 0, 3, 1, 2 },
      { 1, 2, 0, 3 },
      { 1, 3, 0, 2 },
      { 2, 3, 0, 1 }
    };

    // Now do double-single-singles
    for( int [] idxs : pairIdxs ) {
      for( int i = 0; i < double_terminals.length; i++ ) {
        tempTerminal1 = this.getReplaceString( digits[idxs[0]], digits[idxs[1]], double_terminals[i] );
        t1 = new TerminalExpression( tempTerminal1 );
        for( int j = 0; j < single_terminals.length; j++ ) {
          tempTerminal3 = this.getReplaceString( digits[idxs[2]], single_terminals[j] );
          t3 = new TerminalExpression( tempTerminal3 );
          for( int m = 0; m < single_terminals.length; m++ ) {
            tempTerminal4 = this.getReplaceString( digits[idxs[3]], single_terminals[m] );
            t4 = new TerminalExpression( tempTerminal4 );
            this.doAllExpressions( t1, t3, t4 );
            //this.doAllExpressions( t1, t4, t3 );
            //this.doAllExpressions( t3, t1, t4 );
            //this.doAllExpressions( t3, t4, t1 );
            //this.doAllExpressions( t4, t1, t3 );
            //this.doAllExpressions( t4, t3, t1 );
          }
        }
      }
    }

  }

  public void getTripleTerminalSets( int x, int y, int z, int k ) {
    int [] digits = new int [4];
    String tempTerminal1 = null, tempTerminal2 = null, tempTerminal3 = null, tempTerminal4 = null;
    Expression t0 = null, t1 = null;

    digits[0] = x;
    digits[1] = y;
    digits[2] = z;
    digits[3] = k;

    int [][] tripleIdxs = {
      { 0, 1, 2, 3 },
      { 0, 1, 3, 2 },
      { 0, 2, 3, 1 },
      { 1, 2, 3, 0 }
    };

    for( int [] idxs : tripleIdxs ) {
      for( int i = 0; i < triple_terminals.length; i++ ) {
        tempTerminal1 = this.getReplaceString( digits[idxs[0]], digits[idxs[1]], digits[idxs[2]], triple_terminals[i] );
        for( int j = 0; j < single_terminals.length; j++ ) {
          tempTerminal2 = this.getReplaceString( digits[idxs[3]], single_terminals[j] );

          t0 = new TerminalExpression( tempTerminal1 );
          t1 = new TerminalExpression( tempTerminal2 );

          this.doAllExpressions( t0, t1 );
          //this.doAllExpressions( t1, t0 );
        }
      }
    }
     
  }

  public void getQuadTerminalSets( int x, int y, int z, int k )
  {
    String tempTerminal = null;
    Expression e0 = null;

    for( int i = 0; i < quad_terminals.length; i++ )
    {
      tempTerminal = this.getReplaceString( x, y, z, k, quad_terminals[i]);
      e0 = new TerminalExpression( tempTerminal );

      try
      {
        System.out.println( "Expression: " + e0.toString() + " = " + e0.evaluate() );
      }
      catch( Exception e )
      {
        System.out.println( "Caught Exception: " + e.toString() + " while evaluating: " + e0 );
      }
    }
  }

  // replace "a" with the int arg
  public String getReplaceString( int r0, String template )
  {
    String retVal = template.replaceAll( "a", "" + r0 );

    return retVal;
  }

  // replace "a" with arg 0, "b" with arg 1
  public String getReplaceString( int r0, int r1, String template )
  {
    String retVal = template.replaceAll( "a", "" + r0 );
    retVal = retVal.replaceAll( "b", "" + r1 );

    return retVal;
  }

  // you get the idea
  public String getReplaceString( int r0, int r1, int r2, String template )
  {
    String retVal = template.replaceAll( "a", "" + r0 );
    retVal = retVal.replaceAll( "b", "" + r1 );
    retVal = retVal.replaceAll( "c", "" + r2 );

    return retVal;
  }

  public String getReplaceString( int r0, int r1, int r2, int r3, String template )
  {
    String retVal = template.replaceAll( "a", "" + r0 );
    retVal = retVal.replaceAll( "b", "" + r1 );
    retVal = retVal.replaceAll( "c", "" + r2 );
    retVal = retVal.replaceAll( "d", "" + r3 );

    return retVal;
  }

  /**
   * One-stop shopping
   */
  public void doAllExpressions( Expression ... expressions ) {
    ExpressionFactory factory = new ExpressionFactory();
    Expression t0 = null, t1 = null;
    Expression snt0 = null, snt1 = null, dnt0 = null;
    List<Expression> nextExpressions = new ArrayList<Expression>();

    if( expressions == null ) return;

    if( expressions.length == 1 ) {
      // we've boiled down to a single expression, run through them and evaluate.
      for( int i = 0; i < single_non_terminals.length; i++ ) {
        snt0 = factory.getExpression( expressions[0], single_non_terminals[i] );
        System.out.println( "Expression: " + snt0.toString() + " = " + snt0.evaluate() );
      }
    } else if( expressions.length > 1 ) {
      // loop through and grab pairs of expressions, then build all of the possible 
      // double non-terminals.
      for( int i = 0; i < expressions.length - 1; i++ ) {
        t0 = expressions[i];
        t1 = expressions[i+1];
        for( int j = 0; j < double_non_terminals.length; j++ ) {
          for( int k = 0; k < single_non_terminals.length; k++ ) {
            snt0 = factory.getExpression( t0, single_non_terminals[k] );
            for( int l = 0; l < single_non_terminals.length; l++ ) {
              nextExpressions.clear();
              snt1 = factory.getExpression( t1, single_non_terminals[l] );
              dnt0 = factory.getExpression( snt0, snt1, double_non_terminals[j] );

              // add the head expressions
              for( int h = 0; h < i; h++ ) {
                nextExpressions.add( expressions[h] );
              }

              // add the new double
              nextExpressions.add( dnt0 );

              // add the tail expressions
              for( int t = (i+2); t < expressions.length; t++ ) {
                nextExpressions.add( expressions[t] );
              }

              this.doAllExpressions( nextExpressions.toArray( new Expression [0] ) );
            }
          }
        }
      }
    } 
  }

  /**
   *
   */
  public interface Expression
  {
    public float evaluate() throws NumberFormatException;
    public boolean isTerminal();
  }

  /**
   *
   */
  public class TerminalExpression implements Expression
  {
    String terminal = null;

    public TerminalExpression( String term )
    {
      terminal = term;
    }

    public float evaluate() throws NumberFormatException
    {
      float retVal = 0;

      retVal = new Float( terminal ).floatValue();

      return retVal;
    }

    public boolean isTerminal() {
      return true;
    }

    public String toString()
    {
      return terminal;
    }
  }

  /**
   *
   */
  public abstract class SingleExpression implements Expression
  {
    Expression expr0 = null;

    public SingleExpression( Expression e0 )
    {
      expr0 = e0;
    }

    public boolean isTerminal() {
      return expr0.isTerminal();
    }

  }

  /**
   * E -> (E)
   */
  public class ParenExpression extends SingleExpression
  {
    public ParenExpression( Expression e0 )
    {
      super(e0);
    }

    public float evaluate() throws NumberFormatException
    {
      return expr0.evaluate();
    }

    public String toString()
    {
      return "(" + expr0.toString() + ")";
    }
  }

  /**
   * E -> sqrt(E)
   */
  public class SqrtExpression extends SingleExpression
  {
    public SqrtExpression( Expression e0 )
    {
      super(e0);
    }

    public float evaluate() throws NumberFormatException
    {
      float f = expr0.evaluate();
      double d = new Float(f).doubleValue();
      d = Math.sqrt(d);
      return new Double(d).floatValue();
    }

    public String toString()
    {
      return "sqrt(" + expr0.toString() + ")";
    }
  }

  /**
   * E -> E!
   */
  public class FactorialExpression extends SingleExpression
  {
    public FactorialExpression( Expression e0 )
    {
      super(e0);
    }

    public float evaluate() throws NumberFormatException
    {
      float f = expr0.evaluate();
      int i = new Float(f).intValue();
      float f2 = new Integer(i).floatValue();

      if( f != f2 )
        return Float.NaN;
      else
        return factorial(i);
    }

    public String toString()
    {
      if( expr0.isTerminal() ) {
        return expr0.toString() + "!";
      } else {
        return "(" + expr0.toString() + ")!";
      }
    }
  }

  /**
   * E -> E
   */
  public class IdentityExpression extends SingleExpression
  {
    public IdentityExpression( Expression e0 )
    {
      super(e0);
    }

    public float evaluate() throws NumberFormatException
    {
      return expr0.evaluate();
    }

    public String toString()
    {
      return expr0.toString();
    }
  }

  private float factorial( float n )
  {
    if( n <= 1 ) return 1.0F;
    else if( n >= 10 ) return Float.NaN;
    else return n * factorial( n - 1 );
  }

  /**
   *
   */
  public abstract class MultiExpression implements Expression
  {
    Expression expr0 = null;
    Expression expr1 = null;

    public MultiExpression( Expression e0, Expression e1 )
    {
      expr0 = e0;
      expr1 = e1;
    }

    public boolean isTerminal() {
      return false;
    }

  }

  /**
   * E -> E^E
   */
  public class PowerExpression extends MultiExpression
  {
    public PowerExpression( Expression e0, Expression e1 )
    {
      super(e0, e1);
    }

    public float evaluate() throws NumberFormatException
    {
      return new Double( Math.pow( new Float( expr0.evaluate() ).doubleValue(), new Float( expr1.evaluate() ).doubleValue() ) ).floatValue();
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      if( expr0.isTerminal() ) {
        sb.append( expr0.toString() );
      } else {
        sb.append("(").append( expr0.toString() ).append(")");
      }

      sb.append("^");

      if( expr1.isTerminal() ) {
        sb.append( expr1.toString() );
      } else {
        sb.append("(").append( expr1.toString() ).append(")");
      }

      return sb.toString();
    }
  }

  /**
   * E -> E + E
   */
  public class AdditionExpression extends MultiExpression
  {
    public AdditionExpression( Expression e0, Expression e1 )
    {
      super(e0, e1);
    }

    public float evaluate() throws NumberFormatException
    {
      float retVal = expr0.evaluate() + expr1.evaluate();
      return retVal;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      if( expr0.isTerminal() ) {
        sb.append( expr0.toString() );
      } else {
        sb.append("(").append( expr0.toString() ).append(")");
      }

      sb.append("+");

      if( expr1.isTerminal() ) {
        sb.append( expr1.toString() );
      } else {
        sb.append("(").append( expr1.toString() ).append(")");
      }

      return sb.toString();
    }
  }

  /**
   * E -> E - E
   */
  public class SubtractionExpression extends MultiExpression
  {
    public SubtractionExpression( Expression e0, Expression e1 )
    {
      super(e0, e1);
    }

    public float evaluate() throws NumberFormatException
    {
      float retVal = expr0.evaluate() - expr1.evaluate();
      return retVal;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      if( expr0.isTerminal() ) {
        sb.append( expr0.toString() );
      } else {
        sb.append("(").append( expr0.toString() ).append(")");
      }

      sb.append("-");

      if( expr1.isTerminal() ) {
        sb.append( expr1.toString() );
      } else {
        sb.append("(").append( expr1.toString() ).append(")");
      }

      return sb.toString();
    }
  }

  /**
   * E -> E * E
   */
  public class MultiplicationExpression extends MultiExpression
  {
    public MultiplicationExpression( Expression e0, Expression e1 )
    {
      super(e0, e1);
    }

    public float evaluate() throws NumberFormatException
    {
      float retVal = expr0.evaluate() * expr1.evaluate();
      return retVal;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      if( expr0.isTerminal() ) {
        sb.append( expr0.toString() );
      } else {
        sb.append("(").append( expr0.toString() ).append(")");
      }

      sb.append("*");

      if( expr1.isTerminal() ) {
        sb.append( expr1.toString() );
      } else {
        sb.append("(").append( expr1.toString() ).append(")");
      }

      return sb.toString();
    }
  }

  /**
   * E -> E / E
   */
  public class DivisionExpression extends MultiExpression
  {
    public DivisionExpression( Expression e0, Expression e1 )
    {
      super(e0, e1);
    }

    public float evaluate() throws NumberFormatException
    {
      float retVal = expr0.evaluate() / expr1.evaluate();
      return retVal;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      if( expr0.isTerminal() ) {
        sb.append( expr0.toString() );
      } else {
        sb.append("(").append( expr0.toString() ).append(")");
      }

      sb.append("/");

      if( expr1.isTerminal() ) {
        sb.append( expr1.toString() );
      } else {
        sb.append("(").append( expr1.toString() ).append(")");
      }

      return sb.toString();
    }
  }

  /**
   *
   */
  public class ExpressionFactory
  {
    public Expression getParenExpression( Expression e0 )
    {
      Expression retVal = null;

      retVal = new ParenExpression( e0 );

      return retVal;
    }

    public Expression getExpression( String t0, String t1, String descriptor )
    {
      return this.getExpression( new TerminalExpression( t0 ), new TerminalExpression( t1 ), descriptor );
    }

    public Expression getExpression( String t0, Expression e1, String descriptor )
    {
      return this.getExpression( new TerminalExpression( t0 ), e1, descriptor );
    }

    public Expression getExpression( Expression e0, Expression e1, String descriptor )
    {
      Expression retVal = null;

      if( POW1.equals( descriptor ) )
      {
        retVal = new PowerExpression( e0, e1 );
      }
      else if( POW2.equals( descriptor ) )
      {
        retVal = new PowerExpression( e1, e0 );
      }
      else if( MULT1.equals( descriptor ) )
      {
        retVal = new MultiplicationExpression( e0, e1 );
      }
      else if( MULT2.equals( descriptor ) )
      {
        retVal = new MultiplicationExpression( e1, e0 );
      }
      else if( ADD1.equals( descriptor ) )
      {
        retVal = new AdditionExpression( e0, e1 );
      }
      else if( ADD2.equals( descriptor ) )
      {
        retVal = new AdditionExpression( e1, e0 );
      }
      else if( SUB1.equals( descriptor ) )
      {
        retVal = new SubtractionExpression( e0, e1 );
      }
      else if( SUB2.equals( descriptor ) )
      {
        retVal = new SubtractionExpression( e1, e0 );
      }
      else if( DIV1.equals( descriptor ) )
      {
        retVal = new DivisionExpression( e0, e1 );
      }
      else if( DIV2.equals( descriptor ) )
      {
        retVal = new DivisionExpression( e1, e0 );
      }
      else
      {
        retVal = null;
      }

      return retVal;
    }

    public Expression getExpression( Expression e0, String descriptor )
    {
      Expression retVal = null;

      if( ID.equals( descriptor ) )
      {
        retVal = new IdentityExpression( e0 );
      }
      else if( FACT.equals( descriptor ) )
      {
        retVal = new FactorialExpression( e0 );
      }
      else if( SQRT.equals( descriptor ) )
      {
        retVal = new SqrtExpression( e0 );
      }

      return retVal;
    }
  }

  public static void main( String [] args )
  {
    if( args.length != 4 )
    {
      System.out.println( "Usage: java Digits <x> <y> <z> <k>" );
    }
    else
    {
      FourDigits D = new FourDigits();

      try
      {
        int x = Integer.parseInt( args[0] );
        int y = Integer.parseInt( args[1] );
        int z = Integer.parseInt( args[2] );
        int k = Integer.parseInt( args[3] );

        System.out.println( "\n---------------\nGetting Singles\n---------------" );
        D.getSingleTerminalSets( x, y, z, k );
        System.out.println( "\n---------------\nGetting Doubles\n---------------" );
        D.getDoubleTerminalSets( x, y, z, k );
        System.out.println( "\n---------------\nGetting Triples\n---------------" );
        D.getTripleTerminalSets( x, y, z, k );
        System.out.println( "\n---------------\nGetting Quads\n---------------" );
        D.getQuadTerminalSets( x, y, z, k );
      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception: " + e.toString() );
        e.printStackTrace();
      }
    }
  }

}
