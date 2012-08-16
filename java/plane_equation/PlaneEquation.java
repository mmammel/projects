import java.io.*;
import java.util.*;

// input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

public class PlaneEquation
{
  public String getEquation(BufferedReader input) throws IOException
  {
    System.out.println("Enter point 1\n_____________");
    Point p1 = this.readPoint(input);
    System.out.println("Enter point 2\n_____________");
    Point p2 = this.readPoint(input);
    System.out.println("Enter point 3\n_____________");
    Point p3 = this.readPoint(input);

    return this.getEquation(p1,p2,p3);
  }

  public String getEquation( Point p1, Point p2, Point p3 )
  {
    long a = 0, b = 0, c = 0, d = 0;

    a = p1.y * (p2.z - p3.z) + p2.y * (p3.z - p1.z) + p3.y * (p1.z - p2.z);
    b = p1.z * (p2.x - p3.x) + p2.z * (p3.x - p1.x) + p3.z * (p1.x - p2.x);
    c = p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
    d = p1.x * ((p2.y * p3.z) - (p3.y * p2.z)) + p2.x * ((p3.y * p1.z) - (p1.y * p3.z)) + p3.x * ((p1.y * p2.z) - (p2.y * p1.z));
    d = d * -1;

    return "" + a + "x" + (b < 0 ? " - " + b*-1 : " + " + b) + "y" +
                          (c < 0 ? " - " + c*-1 : " + " + c) + "z" +
                          (d < 0 ? " - " + d*-1 : " + " + d);


  }

  public Point readPoint( BufferedReader input ) throws IOException
  {
    Point p = new Point();
    System.out.print( "Input X coord: " );
    p.x = Long.parseLong( input.readLine() );
    System.out.print( "Input Y coord: " );
    p.y = Long.parseLong( input.readLine() );
    System.out.print( "Input Z coord: " );
    p.z = Long.parseLong( input.readLine() );

    return p;
  }

  public class Point
  {
    public long x;
    public long y;
    public long z;
  }

  public static void main( String [] args )
  {
    PlaneEquation pe = new PlaneEquation();

    try
    {
      BufferedReader input = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( pe.getEquation(input) );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
