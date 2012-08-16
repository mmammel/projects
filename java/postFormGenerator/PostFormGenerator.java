import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;

public class PostFormGenerator
{
  private List<String> postParams = new ArrayList<String>();
  private String formAction;
  private String name;

  public PostFormGenerator( String name ) { this.name = name; }

  public void addPostParam( String param )
  {
    this.postParams.add( param );
  }
  
  public void setFormAction( String action )
  {
    this.formAction = action;
  }

  public void generateForm() throws IOException
  {
    FileWriter writer = new FileWriter( this.name + ".html" );

    writer.write( "<html>\n<body>\n" );
    writer.write( "  <form action=\"" + this.formAction + "\" method=\"post\">\n" );
    writer.write( "    <table>\n" );
    for( String param : this.postParams )
    {
      writer.write( "    <tr><td>" + param + "</td><td><input name=\"" + param + "\"/></td></tr>\n" );
    }
    writer.write( "   </table>\n" );
    writer.write( "   <input type=\"submit\"/>\n" );
    writer.write( " </form>\n" );
    writer.write( " </body>\n" );
    writer.write( " </html>\n" );
    writer.flush();
    writer.close();
  }

  public static void main( String [] args )
  {
    PostFormGenerator PFG;

    if( args.length != 1 )
    {
      System.out.println( "Usage: java PostFormGenerator <pageName>" );
      System.exit(0);
    }
    else
    {
      PFG = new PostFormGenerator( args[0] );

      BufferedReader input_reader = null;
      String input_str = "";

      try
      {
        input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

        System.out.println( "Welcome to the HTML POST form generator v0.1" );

        System.out.print("Enter the form Action: ");
        input_str = input_reader.readLine();
        PFG.setFormAction( input_str );
        System.out.println("\nEnter post params, or \"done\" when finished.\n");
        System.out.println("param> ");
        while( (input_str = input_reader.readLine()) != null )
        {
  
          if( input_str.equalsIgnoreCase( "done" ) )
          {
            break;
          }
          else
          {
            PFG.addPostParam( input_str );
          }
  
          System.out.print( "\nparam>" );
        }
  
        System.out.println( "Generating " + args[0] + ".html ..." );
        PFG.generateForm();
        System.out.println( "...Done." );
      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception: " + e.toString() );
        e.printStackTrace();
      }
    }
  }
}

