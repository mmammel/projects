import java.io.StringWriter;
import java.util.*;
import java.util.regex.*;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.MathTool;
import bsh.Interpreter;
import bsh.EvalError;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BeanShellCalc
{
  public static void main( String [] args )
  {
    /* first, we init the runtime engine.  Defaults are fine. */

    Velocity.init();

    BeanShellCalc BSC = new BeanShellCalc();

    List<Expression> expressions = new ArrayList<Expression>();
    Map<String,Float> scores = new HashMap<String,Float>();

    scores.put( "scaleA",30.0f );
    scores.put( "scaleB",45.0f );
    scores.put( "scaleC",75.0f );


    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Start entering expressions, or \"done\" when finished:" );

      System.out.print("Expression name: ");
      boolean readingName = true;
      String tempName = "";

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "done" ) )
        {
          break;
        }
        else
        {
          if( readingName )
          {
            tempName = input_str;
          }
          else
          {
            expressions.add( new Expression( tempName, input_str ) );
          }

        }

        readingName = !readingName;

        System.out.print( readingName ? "\nExpression name: " : "\nExpression: " );
      }

      System.out.println( "Processing template..." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }


    expressions.add( new Expression("scaleA_avg","40" ));
    expressions.add( new Expression("scaleB_avg","50" ));
    expressions.add( new Expression("scaleC_avg","30" ));

    expressions.add( new Expression("scaleA_SD", "3" ));
    expressions.add( new Expression("scaleB_SD", "3" ));
    expressions.add( new Expression("scaleC_SD", "3" ));

    expressions.add( new Expression("scaleA_weight",".6" ));
    expressions.add( new Expression("scaleB_weight",".2" ));
    expressions.add( new Expression("scaleC_weight",".2" ));

    expressions.add( new Expression("scaleA_zscore", "(rawScore(\"scaleA\") - scaleA_avg)/scaleA_SD" ));
    expressions.add( new Expression("scaleB_zscore", "(rawScore(\"scaleB\") - scaleB_avg)/scaleB_SD" ));
    expressions.add( new Expression("scaleC_zscore", "(rawScore(\"scaleC\") - scaleC_avg)/scaleC_SD" ));

    expressions.add( new Expression("scaleA_tscore", "(10*scaleA_zscore)+50" ));
    expressions.add( new Expression("scaleB_tscore", "(10*scaleB_zscore)+50" ));
    expressions.add( new Expression("scaleC_tscore", "(10*scaleC_zscore)+50" ));

    expressions.add( new Expression("overallSum","(scaleA_weight*scaleA_tscore)+(scaleB_weight*scaleB_tscore)+(scaleC_weight*scaleC_tscore)" ));
    expressions.add( new Expression("overallAvg", "overallSum/3" ));


/**
  cutoffs:
    20,19,18,...,1
  percentiles:
    99,97,94,...,10
*/

    StringBuilder template = new StringBuilder();

    template.append( "This is my template!!\n\n" );
    template.append( "Overall score: $math.roundTo(2,\"$overallAvg\")\n" );
    template.append( "\nOverall sum: $math.roundTo(2,\"$overallSum\")" );
    template.append( "\nMax's custom metric: $mjm" );



    CalcContext calc = new CalcContext();
    calc.put( "scores", scores );

    System.out.println( BSC.evaluate( calc, expressions, template.toString() ) );
  }

  public String evaluate( CalcContext calc, List<Expression> expressions, String template )
  {
    VelocityContext context = new VelocityContext();
    context.put("math",new MathTool());
    Interpreter i = new Interpreter();
    Object tempResult = null;

    try
    {
      i.set("calc",calc);

      if( expressions != null )
      {
        for( Expression exp : expressions )
        {
          tempResult = i.eval( exp.expression.replaceAll( "rawScore", "calc.rawScore" ) ); // (calc.rawScore("scaleA") - scaleA_avg)/scaleA_SD
          i.set(exp.name, tempResult);
          context.put(exp.name,tempResult);
        }
      }
    }
    catch( EvalError ee )
    {
      System.out.println( "Expression error: " + ee.toString() );
    }

    StringWriter w = new StringWriter();

    Velocity.evaluate( context, w, "calc", template );

    return w.toString();
  }

  public static class Expression
  {
    public Expression( String n, String e ) { this.name = n; this.expression = e; }
    String name;
    String expression;
  }

  public static class CalcContext {
    // Hold basic info that should be available.
    private Map<String,Object> data = new HashMap<String,Object>();

    public CalcContext()
    {
    }

    public void put( String key, Object value )
    {
      this.data.put( key, value );
    }

    public Float rawScore( String scaleName )
    {
      return (Float)((Map)this.data.get( "scores" )).get(scaleName);
    }

    public Float AVG( List items )
    {
      Float retVal = Float.NaN;

      try
      {
        for( Object item : items )
        {
          retVal += (Float)item;
        }

        retVal = retVal / items.size();
      }
      catch( Exception e )
      {
        retVal = Float.NaN;
        System.out.println( "Exception: " + e.toString() );
      }

      return retVal;
    }
  }

}