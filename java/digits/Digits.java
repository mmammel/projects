public class Digits
{
  public static final String [] single_terminals = {"a", ".a", ".aaaaaaaaaaaaa" };

  public static final String [] double_terminals = { "ab", "ba", ".ab", ".ba", "a.b", "b.a" };


  public static final String [] triple_terminals = { "abc", "acb", "bac", "bca", "cab", "cba",
                                                     "a.bc", "a.cb", "b.ac", "b.ca", "c.ab", "c.ba",
                                                     "ab.c", "ac.b", "ba.c", "bc.a", "ca.b", "cb.a",
                                                     ".abc", ".acb", ".bac", ".bca", ".cab", ".cba" };


  public static final String POW1 = "A^B";
  public static final String POW2 = "B^A";
  public static final String ADD  = "A+B";
  public static final String SUB1 = "A-B";
  public static final String SUB2 = "B-A";
  public static final String MULT = "A*B";
  public static final String DIV1 = "A/B";
  public static final String DIV2 = "B/A";

  public static final String [] double_non_terminals = { POW1, POW2, ADD, SUB1, SUB2, MULT, DIV1, DIV2 };

  public static final String ID = "A";
  public static final String SQRT = "sqrt(A)";
  public static final String FACT = "A!";

  public static final String [] single_non_terminals = { ID, SQRT, FACT };
  /**
   *
   */
  public void getSingleTerminalSets( int x, int y, int z )
  {
    int [] digits = new int [3];
    String [] terminals = new String [3];
    digits[0] = x;
    digits[1] = y;
    digits[2] = z;

    for( int i = 0; i < 3; i++ )
    {
      terminals[0] = getReplaceString( x, single_terminals[i] );

      for( int j = 0; j < 3; j++ )
      {
        terminals[1] = getReplaceString( y, single_terminals[j] );

        for( int k = 0; k < 3; k++ )
        {
          terminals[2] = getReplaceString( z, single_terminals[k] );

          this.doAllExpressions( terminals[0], terminals[1], terminals[2] );
          this.doAllExpressions( terminals[1], terminals[0], terminals[2] );
          this.doAllExpressions( terminals[2], terminals[0], terminals[1] );

        }
      }
    }

  }

  /**
   *
   */
  public void getDoubleTerminalSets( int x, int y, int z )
  {
    int [] digits = new int [3];
    String tempTerminal = null;
    digits[0] = x;
    digits[1] = y;
    digits[2] = z;
    int leftOver = 2;

    for( int i = 0; i < 2; i++ )
    {
      for( int j = i+1; j < 3; j++ )
      {
        for( int k = 0; k < double_terminals.length; k++ )
        {
          tempTerminal = this.getReplaceString( digits[i], digits[j], double_terminals[k] );

          this.doAllExpressions( "" + digits[leftOver], tempTerminal );
          this.doAllExpressions( "." + digits[leftOver], tempTerminal ); 
        }

        leftOver--;
      }
    }

  }

  public void getTripleTerminalSets( int x, int y, int z )
  {
    String tempTerminal = null;
    Expression e0 = null;

    for( int i = 0; i < triple_terminals.length; i++ )
    {
      tempTerminal = this.getReplaceString( x, y, z, triple_terminals[i]);
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


  public void doAllExpressions( String t0, String t1, String t2 )
  {
    String temp0 = null, temp1 = null;
    Expression e0 = null, e1 = null;
    Expression te0 = null, te1 = null, te2 = null;
    Expression iae = null, ibe = null, jae = null, jbe = null, jce = null;
    ExpressionFactory factory = new ExpressionFactory();
    te0 = new TerminalExpression(t0);
    te1 = new TerminalExpression(t1);
    te2 = new TerminalExpression(t2);

    for( int i = 0; i < double_non_terminals.length; i++ )
    {
      for( int ia = 0; ia < single_non_terminals.length; ia++ )
      {
        iae = factory.getExpression( te1, single_non_terminals[ia] );

        for( int ib = 0; ib < single_non_terminals.length; ib++ )
        {
          ibe = factory.getExpression( te2, single_non_terminals[ib] );
          e1 = factory.getExpression( iae, ibe, double_non_terminals[i] );

          for( int j = 0; j < double_non_terminals.length; j++ )
          {
            for( int ja = 0; ja < single_non_terminals.length; ja++ )
            {
              jae = factory.getExpression( te0, single_non_terminals[ja] );
              for( int jb = 0; jb < single_non_terminals.length; jb++ )
              {
                jbe = factory.getExpression( factory.getParenExpression( e1 ), single_non_terminals[jb] );
                e0 = factory.getExpression( jae, jbe, double_non_terminals[j] );

                for( int jc = 0; jc < single_non_terminals.length; jc++ )
                {
                  jce = factory.getExpression( e0, single_non_terminals[jc] );
                  try
                  {
                    System.out.println( "Expression: " + jce.toString() + " = " + jce.evaluate() );
                  }
                  catch( Exception e )
                  {
                    System.out.println( "Caught Exception: " + e.toString() + " while evaluating: " + jce );
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public void doAllExpressions( String t0, String t1 )
  {
    Expression e0 = null, te0 = null, te1 = null;;
    Expression snt0 = null, snt1 = null, ent0 = null;
    ExpressionFactory factory = new ExpressionFactory();

    te0 = new TerminalExpression(t0);
    te1 = new TerminalExpression(t1);

    for( int i = 0; i < double_non_terminals.length; i++ )
    {
      for( int ia = 0; ia < single_non_terminals.length; ia++ )
      {
        snt0 = factory.getExpression( te0, single_non_terminals[ia] );

        for( int ib = 0; ib < single_non_terminals.length; ib++ )
        {
          snt1 = factory.getExpression( te1, single_non_terminals[ib] );
          e0 = factory.getExpression( snt0, snt1, double_non_terminals[i] );

          for( int ic = 0; ic < single_non_terminals.length; ic++ )
          {
            ent0 = factory.getExpression( e0, single_non_terminals[ic] ); 

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
      }
    }
  }

  /**
   *
   */
  public interface Expression
  {
    public float evaluate() throws NumberFormatException;
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
      return "(" + expr0.toString() + ")!";
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
    else if( n >= 10 ) return Float.POSITIVE_INFINITY;
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
      return expr0.toString() + "^" + expr1.toString();
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
      return expr0.toString() + "+" + expr1.toString();
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
      return expr0.toString() + "-" + expr1.toString();
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
      return expr0.toString() + "*" + expr1.toString();
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
      return expr0.toString() + "/" + expr1.toString();
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
      else if( MULT.equals( descriptor ) )
      {
        retVal = new MultiplicationExpression( e0, e1 );
      }
      else if( ADD.equals( descriptor ) )
      {
        retVal = new AdditionExpression( e0, e1 );
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
    if( args.length != 3 )
    {
      System.out.println( "Usage: java Digits <x> <y> <z>" );
    }
    else
    {
      Digits D = new Digits();

      try
      {
        int x = Integer.parseInt( args[0] );
        int y = Integer.parseInt( args[1] );
        int z = Integer.parseInt( args[2] );

        System.out.println( "\n---------------\nGetting Singles\n---------------" );
        D.getSingleTerminalSets( x, y, z );
        System.out.println( "\n---------------\nGetting Doubles\n---------------" );
        D.getDoubleTerminalSets( x, y, z );
        System.out.println( "\n---------------\nGetting Triples\n---------------" );
        D.getTripleTerminalSets( x, y, z );
      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception: " + e.toString() );
        e.printStackTrace();
      }
    }
  }

}
