import java.util.*;

public class DoubleTest
{
  public static void main( String [] args )
  {
    DoubleTest dt = new DoubleTest();

    List listx = new ArrayList();
    List listy = new ArrayList();

    listx.add( new Long( 110L ) );
    listx.add( new Long( 120L ) );
    listx.add( new Long( 130L ) );
    listx.add( new Long( 140L ) );
    listx.add( new Long( 150L ) );

    listy.add( new Long( 200L ) );
    listy.add( new Long( 200L ) );
    listy.add( new Long( 200L ) );
    listy.add( new Long( 200L ) );
    listy.add( new Long( 0L ) );

    List result = dt.getTrendLineData( listx, listy );

    for( int i = 0; i < 5; i++ )
    {
      System.out.println( "[ " + listx.get(i) + ", " +
                                 listy.get(i) + ", " +
                                 result.get(i) + " ]" );
    }

  }

  /**
   * Assume two lists of Long
   */
  public List getTrendLineData( List x, List y )
  {
    List resultList = null;
    double sx  = 0.0;
    double sxx = 0.0;
    double sy  = 0.0;
    double syy = 0.0;
    double sxy = 0.0;
    double a   = 0.0;
    double b   = 0.0;
    int size_n   = 0;
    
    if( (x.size() != y.size()) || x.size() <= 0 )
    {
      System.out.println( "Mismatched list sizes, or no data for regression line" );
    }
    else
    {
      size_n = x.size();
      sx = this.getSum( x );
      sxx = this.getSumSquares( x );
      sy = this.getSum( y );
      syy = this.getSumSquares( y );
      sxy = this.getProduct( x, y );
      
      b = ((size_n * sxy) - (sx*sy))/((size_n * sxx) - (sx*sx));
      
      if( Double.isInfinite(b) || Double.isNaN(b) )
      {
        resultList = this.getZeroList( size_n );
      }
      else
      {
        a = (sy - (b * sx)) / (new Integer(size_n).doubleValue());
        
        if( Double.isInfinite(a) || Double.isNaN(a) )
        {
          resultList = this.getZeroList( size_n );
        }
        else
        {
          resultList = new ArrayList();
          
          for( int n = 0; n < size_n; n++ )
          {
            resultList.add( new Double( (b * ((Long)x.get(n)).doubleValue()) + a ) );
          }
        }
      }
    }
    
    return resultList;
  }
  
  /**
   * Assume a list of Long's
   */
  protected double getSum( List data )
  {
    double retVal = 0.0;
    
    for( Iterator iter = data.iterator(); iter.hasNext(); )
    {
      retVal += ((Long)iter.next()).doubleValue();
    }
    
    return retVal;
  }
  
  /**
   * Assume list of Long
   */
  protected double getSumSquares( List data )
  {
    double retVal = 0.0;
    double tempVal = 0.0;
    
    for( Iterator iter = data.iterator(); iter.hasNext(); )
    {
      tempVal = ((Long)iter.next()).doubleValue();
      retVal += (tempVal * tempVal);
    }
    
    return retVal;
  }
  
  /**
   * Assume equal sizes, and lists of Long's
   */
  protected double getProduct( List data1, List data2 )
  {
    double retVal = 0.0;
    
    for( int i = 0; i < data1.size(); i++ )
    {
      retVal += ( ((Long)data1.get(i)).doubleValue() * ((Long)data2.get(i)).doubleValue() );
    }
    
    return retVal;
  }
  
  /**
   * get a list of all zero Doubles
   */
  protected List getZeroList( int size )
  {
    List retList = new ArrayList();
    
    for( int i = 0; i < size; i++ )
    {
      retList.add( new Double( 0.0 ) );
    }
    
    return retList;
  }
     
}
