import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.text.*;

/**
  Take in a set of comma-delimited integer weight strings
  Calculate all possible sums of single weights from each string
  produce an array of possibilities along with the calculated average for each
**/

public class ScaleCalculator
{

  private List<Integer[]> weights = new ArrayList<Integer []>();
  private Set<Integer> sums = new TreeSet<Integer>(Collections.reverseOrder());
  private List<Double> avgs = new ArrayList<Double>();

  public ScaleCalculator( List<String> weightStrings ) {
    Integer [] tempInts;
    String [] tempStrs;

    for( String str : weightStrings ) {
      tempStrs = str.split(",");
      tempInts = new Integer [ tempStrs.length ];
      for( int i = 0; i < tempStrs.length; i++ ) {
        tempInts[i] = Integer.parseInt( tempStrs[i] );
      }
      weights.add( tempInts );
    }
  }

  public void calculateResults() {
    this.innerCalculate(0,0);
    double numWeights = new Integer( this.weights.size() ).doubleValue();

    int count = 0;
    for( Integer sum : this.sums ) {
      if( count++ > 0 ) System.out.print( "~" );
      System.out.print( sum );
      this.avgs.add( sum.doubleValue() / numWeights );
    }

    DecimalFormat DF = new DecimalFormat();
    DF.setMaximumFractionDigits(1);
    DF.setMinimumFractionDigits(0);

    System.out.println("");
    for( int i = 0; i < this.avgs.size(); i++ ) {
      if( i > 0 ) System.out.print( "~" );
      System.out.print( DF.format(this.avgs.get(i)) );
    }
    System.out.println("");
  }

  private void innerCalculate( int sum, int weightIdx ) {
    Integer [] weightVals = this.weights.get(weightIdx);
    for( int i = 0; i < weightVals.length; i++ ) {
      sum += weightVals[i];
      if( weightIdx == this.weights.size() - 1 ) {
        this.sums.add( sum );
      } else {
        this.innerCalculate( sum, weightIdx + 1 );
      }
      sum -= weightVals[i];
    }
  }
   
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    List<String> weightStrs = new ArrayList<String>();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        weightStrs.add( input_str );
      }

      ScaleCalculator SC = new ScaleCalculator( weightStrs );
      SC.calculateResults();

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

