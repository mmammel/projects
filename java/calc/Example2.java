import java.io.StringWriter;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.config.EasyFactoryConfiguration;
import org.apache.velocity.tools.generic.MathTool;

public class Example2
{
    public static void main( String args[] )
    {
        /* first, we init the runtime engine.  Defaults are fine. */

        Velocity.init();

        /* lets make a Context and put data into it */

        VelocityContext context = new VelocityContext();

        context.put("name", "Velocity");
        context.put("project", "Jakarta");
        context.put("bracketer", new Bracketer() );
        context.put("float",Float.class);
        context.put("math",new MathTool());

        /* lets render a template */

        StringWriter w = new StringWriter();

        StringBuilder sb = new StringBuilder();
        sb.append("We are using $project $name to render this.\n");
        sb.append( "#set( $val = 10 )\n" );
        sb.append( "#set( $output = (($val * 10) + ($val *.8))/12)" );
        sb.append( "#set( $pi = (22.0/9.0*100)/100 )\n" );
        sb.append( "Output: $output\n" );
        sb.append( "$float.toHexString(\"$float.parseFloat($output)\")\n" );
        sb.append( "$bracketer.getBracketed(\"$output\")\n" );
        sb.append( "$float.parseFloat(\"$output\")\n" );
        sb.append( "$math.roundTo(2,\"$pi\")" );
        Velocity.evaluate( context, w, "mystring", sb.toString() );
        System.out.println(" string : " + w );
    }

    public static class Bracketer
    {
      public String getBracketed( String s ) {  return "["+s+"]"; }
    }
}