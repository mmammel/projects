import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.*;

public class CoordinateHelper
{
  private static Pattern LOADVERT_PATTERN = Pattern.compile( "vertices.*?[=:].*?\\[(.*?)]" );
  private static Pattern COORDPATTERN = Pattern.compile( "^([^,]+?),([^,]+?),([^,]+?)$");
  private List<double []> coords = null;
  private List<List<double []>> faces = null;
  private Stack<HistoryObj> history = null;

  public CoordinateHelper() {
    this.coords = new ArrayList<double []>();
    this.faces = new ArrayList<List<double[]>>();
    this.history = new Stack<HistoryObj>();
  }

  public void addCoord( String coords ) {
    double [] c = this.fromCommaString( coords );
    this.addCoord( c[0], c[1], c[2] );
  }

  private double[] fromCommaString( String commaString ) {
    String [] strC = commaString.split(",");
    double [] retVal = new double [ strC.length ];
    try {
      for( int i = 0; i < strC.length; i++ ) {
        retVal[i] = Double.parseDouble(strC[i].trim());
      }
    } catch( Exception e ) {
      System.out.println( "Screwed the pooch with : " + coords );
    }

    return retVal;
  }

  public void addCoord( double x, double y, double z ) {
    double [] arr = new double [3];
    arr[0] = x;
    arr[1] = y;
    arr[2] = z;

    this.coords.add( arr );
  }

  public void addFace() {
    this.faces.add( this.coords );
    this.clear();

    this.history.push( new HistoryObj( true, false, false, null ) );
  }

  public void clear() {
    this.coords = new ArrayList<double[]>();
  }

  public void clearAll() {
    this.clear();
    this.faces = new ArrayList<List<double[]>>();
  }

  public void map( String command ) {
    // map x,y,z
    String coords = command.replaceAll( "^map[ ]+?([^,]+?),([^,]+?),([^,]+?)$", "$1,$2,$3" );
    double [] c = this.fromCommaString( coords );
    this.map( c[0], c[1], c[2] );
  }

  public void map( double deltax, double deltay, double deltaz ) {
    double [] deltas = new double[3];

    deltas[0] = deltax;
    deltas[1] = deltay;
    deltas[2] = deltaz;

    this.history.push( new HistoryObj( false, true, false, deltas) );
    this.innerMap( deltas, this.coords );
  }

  public void mapAll( String command ) {
    // map x,y,z
    String coords = command.replaceAll( "^map[ ]+?all[ ]+?([^,]+?),([^,]+?),([^,]+?)$", "$1,$2,$3" );
    double [] c = this.fromCommaString( coords );
    this.mapAll( c[0], c[1], c[2] );
  }

  public void mapAll( double deltax, double deltay, double deltaz ) {
    double [] deltas = new double[3];

    deltas[0] = deltax;
    deltas[1] = deltay;
    deltas[2] = deltaz;

    this.history.push( new HistoryObj( false, false, true, deltas ) );
    for( List<double []> cs : this.faces ) {
      this.innerMap( deltas, cs );
    }
  }

  private void innerMap( double [] deltas, List<double []> cs ) {
    for( double [] c : cs ) {
      c[0] += deltas[0];
      c[1] += deltas[1];
      c[2] += deltas[2];
    }

    System.out.println( "Mapping applied" );
  }

  private void undo() {
    HistoryObj hist = null;
    if( this.history.empty() ) {
      System.out.println( "Nothing to undo" );
    } else {
      hist = this.history.pop();
      if( hist.mapping ) {
        double [] h = hist.deltas;
        for( int i = 0; i < h.length; i++ ) {
          h[i] *= -1.0d;
        }  

        this.innerMap( h, this.coords );
      } else if( hist.allMapping ) {
        double [] h = hist.deltas;
        for( int i = 0; i < h.length; i++ ) {
          h[i] *= -1.0d;
        }
        for( List<double []> cs : this.faces ) {
          this.innerMap( h, cs );
        }
      } else if( hist.addFace ) {
        // move the last added face to coords
        List<double []> face = this.faces.remove( this.faces.size() - 1 );
        this.coords = face;
      }

      System.out.println( "Undo successful" );
    }
  }

  private String faceToString( List<double []> face ) {
    StringBuilder sb = new StringBuilder();

    boolean first = true;
    sb.append("  {").append("\n");
    sb.append("    vertices : [\n" );
    for( double[] c : face ) {
      if( first ) {
        first = false;
      } else {
        sb.append(",");
      }

      sb.append( c[0] ).append(",").append( c[1] ).append( "," ).append( c[2] );
    }

    sb.append("\n    ]\n  }");

    return sb.toString();
  }

  public void printCoords() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    sb.append("[ ");
    for( double[] c : this.coords ) {
      if( first ) {
        first = false;
      } else {
        sb.append(",");
      }
      sb.append( c[0] ).append(",").append( c[1] ).append( "," ).append( c[2] );
    }
    sb.append( " ]" );

    System.out.println( sb.toString() );
  } 

  public void printAll() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    sb.append( "[\n" );
    for( List<double []> f : this.faces ) {
      if( first ) {
        first = false;
      } else {
        sb.append(",\n");
      }
      sb.append( this.faceToString( f ) );
    }

    sb.append("\n]");

    System.out.println( sb.toString() );
  }

  private static class HistoryObj {
    boolean addFace = false;
    boolean mapping = false;
    boolean allMapping = false;
    double [] deltas = null;

    public HistoryObj( boolean f, boolean m, boolean a, double [] ds ) {
      this.addFace = f;
      this.mapping = m;
      this.allMapping = a;
      this.deltas = ds;
    }
  }

  private void load( String def ) {
    this.clearAll();
    int count = 0;
    boolean faces = false;
    String tempArray = null;
    double [] tmpD = null;
    String [] arr = null;
    Matcher m = LOADVERT_PATTERN.matcher( def );
    while( m.find() ) {
      if( count > 0 ) {
        this.addFace();
      }
      tempArray = m.group(1);
System.out.println( "Splitting: " + tempArray );
      arr = tempArray.split(",");
      try {
        for( int i = 0; i < arr.length; i+=3 ) {
          tmpD = new double[3];
          tmpD[0] = Double.parseDouble(arr[i]);
          tmpD[1] = Double.parseDouble(arr[i+1]);
          tmpD[2] = Double.parseDouble(arr[i+2]);

          this.coords.add( tmpD );
        }
      } catch( Exception e ) {
      }
      count++;
    } 

    if( count == 1 ) {
      System.out.println( "Loaded " + this.coords.size() + " coords" );
    } else if( count > 1 ) {
      this.addFace();
      System.out.println( "Loaded " + count + " faces" );
    } else {
      System.out.println( "Didn't load anything!" );
    }
  }

  public void loadFromFile( String fname ) {
    BufferedReader input_reader = null;
    String input_str = "";
    StringBuffer sb = new StringBuffer();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        sb.append( input_str );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    } finally {
      try {
        input_reader.close();
      } catch( IOException ioe2 ) {
      }
    }

    this.load( sb.toString() );
  }
    
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    CoordinateHelper CH = new CoordinateHelper();

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Welcome to the coordinate helper" );

      System.out.print("command> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          input_str = input_str.trim();
          if( input_str.toUpperCase().equals("HELP") ) {
            System.out.println(" Commands: " );
            System.out.println("  x,y,z: add coordinate x,y,z" );
            System.out.println("  face: push the current set onto the face stack" );
            System.out.println("  undo: undo the last command" );
            System.out.println("  map x,y,z: apply mapping to current face (not pushed faces)" );
            System.out.println("  map all x,y,z: apply mapping to all faces" );
            System.out.println("  print all: dump JSON for all faces" );
            System.out.println("  print: just print the current set of coords in an array" );
            System.out.println("  clear: reset just the current coords" );
            System.out.println("  clear all: reset everything" );
          } else if( input_str.toUpperCase().equals( "PRINT ALL" ) ) {
            CH.printAll();
          } else if( input_str.toUpperCase().equals( "PRINT" ) ) {
            CH.printCoords();
          } else if( input_str.toUpperCase().startsWith( "MAP ALL" ) ) {
            CH.mapAll( input_str.trim().toLowerCase() );
          } else if( input_str.toUpperCase().startsWith( "MAP" ) ) {
            CH.map( input_str.trim().toLowerCase() );
          } else if( input_str.toUpperCase().equals( "UNDO" ) ) {
            CH.undo();
          } else if( input_str.toUpperCase().equals("FACE" ) ) {
            CH.addFace();
          } else if( input_str.matches( COORDPATTERN.pattern() ) ) {
            CH.addCoord( input_str );
          } else if( input_str.toUpperCase().equals("CLEAR ALL") ) {
            CH.clearAll();
          } else if( input_str.toUpperCase().equals("CLEAR") ) {
            CH.clear();
          } else if( input_str.toUpperCase().startsWith( "LOAD" ) ) {
            String [] arr = input_str.split("\\s");
            CH.loadFromFile( arr[1] );
          } else {
            System.out.println( "Unknown command: " + input_str );
          }
        }

        System.out.print( "\ncommand> " );
      }

      System.out.println( "Adios" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

