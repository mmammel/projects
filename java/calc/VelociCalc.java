import java.io.StringWriter;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.MathTool;
import java.util.*;
import java.util.regex.*;

public class VelociCalc
{
  public static void main( String [] args )
  {
    /* first, we init the runtime engine.  Defaults are fine. */

    Velocity.init();

    VelociCalc VC = new VelociCalc();

    List<Expression> expressions = new ArrayList<Expression>();
    Map<String,Float> scores = new HashMap<String,Float>();

    scores.put( "scaleA",30.0f );
    scores.put( "scaleB",45.0f );
    scores.put( "scaleC",75.0f );

    expressions.add( new Expression("scaleA_avg","40" ));
    expressions.add( new Expression("scaleB_avg","50" ));
    expressions.add( new Expression("scaleC_avg","30" ));

    expressions.add( new Expression("scaleA_SD", "3" ));
    expressions.add( new Expression("scaleB_SD", "3" ));
    expressions.add( new Expression("scaleC_SD", "3" ));

    expressions.add( new Expression("scaleA_weight",".6" ));
    expressions.add( new Expression("scaleB_weight",".2" ));
    expressions.add( new Expression("scaleC_weight",".2" ));

    expressions.add( new Expression("scaleA_zscore", "(rawScore('scaleA') - $scaleA_avg)/$scaleA_SD" ));
    expressions.add( new Expression("scaleB_zscore", "(rawScore('scaleB') - $scaleB_avg)/$scaleB_SD" ));
    expressions.add( new Expression("scaleC_zscore", "(rawScore('scaleC') - $scaleC_avg)/$scaleC_SD" ));

    expressions.add( new Expression("scaleA_tscore", "(10*$scaleA_zscore)+50" ));
    expressions.add( new Expression("scaleB_tscore", "(10*$scaleB_zscore)+50" ));
    expressions.add( new Expression("scaleC_tscore", "(10*$scaleC_zscore)+50" ));

    expressions.add( new Expression("overallSum","($scaleA_weight*$scaleA_tscore)+($scaleB_weight*$scaleB_tscore)+($scaleC_weight*$scaleC_tscore)" ));
    expressions.add( new Expression("overallAvg", "$overallSum/3" ));


    StringBuilder template = new StringBuilder();

    template.append( "This is my template!!\n\n" );
    template.append( "Overall score: $math.roundTo(2,\"$overallAvg\")\n" );
    template.append( "Overall sum: $math.roundTo(2,\"$overallSum\")" );



    CalcContext calc = new CalcContext();
    calc.put( "scores", scores );

    System.out.println( VC.evaluate( calc, expressions, template.toString() ) );
  }

  public String evaluate( CalcContext calc, List<Expression> expressions, String template )
  {
    VelocityContext context = new VelocityContext();
    context.put( "calc", calc );
    context.put( "math", new MathTool() );

    if( expressions != null )
    {
      StringBuilder exprSets = new StringBuilder();

      for( Expression exp : expressions )
      {
        exprSets.append( "#set( $" + exp.name + " = " + exp.expression.replaceAll( "rawScore", "\\$calc.rawScore" ) ).append(")\n");
      }

      exprSets.append( template );
      template = exprSets.toString();
    }

    StringWriter w = new StringWriter();

System.out.println( "\n" + template );
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