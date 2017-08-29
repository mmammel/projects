import java.util.*;

/**
 
 The user has to compare the traits to each other, on one end of the scale is -3, the other is 3.
 
 For example:

 Teamwork -3 -2 -1 0 1 2 3 Results
              ^

 The user selected two notches towards Teamwork, so Teamwork gets a 2 and Results gets a -2.

 The test is contructed so that each trait gets two numbers, so a minimum of -3 + -3 = -6, and maximum of
 3 + 3 = 6.

 On top of this, the user is asked to rank the 19 traits, top 5 and bottom 5.  So in addition to
 the comparison number (-6 to 6) you add a ranking, one of: [-5,-4,-3,-2,-1,0,1,2,3,4,5].

 This makes the min/max of a particular trait be -11/11.

 Once the user does this once for *their* preferences, they do it all again on how they perceive the company
 they are applying to.  So in the end, each trait has two #'s - a preference and a perception number, such that:

 -11 <= pref <= 11  and
 -11 <= perc <= 11

 Then you take (pref - perc) and that is the score for that trait.

 The smaller this preference, the higher the score (0-100).

 In an effort to get the distribution of differences given random prefs/percs/rankings, 
 a sort of hacked version of the Standard Distribution is used.

 This code runs a monte carlo simulation against the prefs/percs and rankings.
*/

public class CultureFitScoreGenerator {

  public static final String [][] PAIRINGS = {
    {"Teamwork","Results"},
    {"Diversity","Teamwork"},
    {"Results","Quality"},
    {"Sustainability","Diversity"},
    {"Innovation","Compliance"},
    {"Compliance","Integrity"},
    {"Autonomy","People"},
    {"Passion","Sustainability"},
    {"Excellence","Simplicity"},
    {"Service","Communication"},
    {"Learning","Quality"},
    {"Integrity","Passion"},
    {"Simplicity","Accountability"},
    {"Innovation","Adaptability"},
    {"Achievement","Excellence"},
    {"Accountability","Learning"},
    {"Communication","Autonomy"},
    {"People","Service"},
    {"Adaptability","Achievement"}
  };

  public static final int [] RANKS = {
    5,4,3,2,1,-1,-2,-3,-4,-5,0,0,0,0,0,0,0,0,0
  };

  public static final int [] PREFS = {
    -3,-2,-1,0,1,2,3
  };

  public static final String [] VALUES = {
    "Accountability",
    "Achievement",
    "Adaptability",
    "Autonomy",
    "Communication",
    "Compliance",
    "Diversity",
    "Excellence",
    "Innovation",
    "Integrity",
    "Learning",
    "Passion",
    "People",
    "Quality",
    "Results",
    "Service",
    "Simplicity",
    "Sustainability",
    "Teamwork"
  };

  public static void main( String [] args ) {
    CultureFitScoreGenerator C = new CultureFitScoreGenerator();
    C.run();
  }

  private List<Integer> ranks;
  private List<Integer> prefs;

  public CultureFitScoreGenerator() {
    this.ranks = new ArrayList<Integer>();
    this.prefs = new ArrayList<Integer>();

    for( int n : RANKS ) {
      this.ranks.add(n);
    }

    for( int n : PREFS ) {
      this.prefs.add( n );
    }
  }

  public double run() {
    return this.run( false );
  }

  public double run(boolean quiet) {
    Map<String,Integer> preferenceSums = new HashMap<String,Integer>();
    Map<String,Integer> perceptionSums = new HashMap<String,Integer>();

    for( String val : VALUES ) {
      preferenceSums.put( val, 0 );
      perceptionSums.put( val, 0 );
    }

    // Assign random weights for slider items.
    String [] temp = null;
    int tempScore = 0;
    int tempSum = 0;
    for( int i = 0; i < PAIRINGS.length; i++ ) {
      Collections.shuffle( this.prefs, new Random(System.nanoTime()) );
      temp = PAIRINGS[i];
      tempScore = this.prefs.get(0);
      tempSum = preferenceSums.get(temp[0]);
      preferenceSums.put(temp[0], (tempSum + tempScore) );
      tempSum = preferenceSums.get(temp[1]);
      preferenceSums.put( temp[1], (tempScore - tempSum) );
      Collections.shuffle( this.prefs, new Random(System.nanoTime()) );
      tempScore = this.prefs.get(0);
      tempSum = perceptionSums.get(temp[0]);
      perceptionSums.put(temp[0], (tempSum + tempScore) );
      tempSum = perceptionSums.get(temp[1]);
      perceptionSums.put( temp[1], (tempScore - tempSum) );
    }

    // Assign random rankings
    Collections.shuffle( this.ranks, new Random(System.nanoTime()) );
    for( int j = 0; j < VALUES.length; j++ ) {
      tempSum = preferenceSums.get( VALUES[j] );
      preferenceSums.put( VALUES[j], tempSum + this.ranks.get(j) );
    }

    Collections.shuffle( this.ranks, new Random(System.nanoTime()) );
    for( int k = 0; k < VALUES.length; k++ ) {
      tempSum = perceptionSums.get( VALUES[k] );
      perceptionSums.put( VALUES[k], tempSum + this.ranks.get(k) );
    }

    // Dump Results
    int prefScore = 0, perceptionScore = 0;
    int percentage = 0;
    double totalPercentage = 0.0;
    double diff = 0.0;
    double sn = 0.0;
    for( int i = 0; i < VALUES.length; i++ ) {
      prefScore = preferenceSums.get( VALUES[i] );
      perceptionScore = perceptionSums.get( VALUES[i] );
      // Old way (flat planes)
      //percentage = new Double(100.0 - (new Float(( Math.abs( prefScore - perceptionScore)) / 22.0) * 100.0)).intValue();
      diff = new Double( prefScore - perceptionScore );
      // Single variable normal on difference
      sn = getStandardNormal( diff / 22.0 * 3.0 );
      percentage = new Double(sn * Math.sqrt( 2.0d * Math.PI ) * 100.0d).intValue();
      // bivariate standard normal
      //sn = getBivariateNormal( prefScore, perceptionScore );
      //percentage = new Double( sn ).intValue();
      totalPercentage += percentage;
      if( !quiet ) System.out.println( "[" + VALUES[i] + "] : " + prefScore + "," + perceptionScore + " -> " + percentage );
    }

    if( !quiet ) System.out.println( "Average rating: " + (totalPercentage / new Double( VALUES.length) ) );
    return (totalPercentage / new Double( VALUES.length) );
     
  }

  public static double getStandardNormal( double val ) {
    //double exp = -0.5 * val * val;
    double exp = -2.0 * val * val;
    exp = Math.pow( Math.E, exp );
    double retVal = exp / Math.sqrt(2.0*Math.PI);
    return retVal; 
  }

  public static double getBivariateNormal( double x, double y ) {
    double sn1 = getStandardNormal( (3.0 * x) / 22.0 );
    double sn2 = getStandardNormal( (3.0 * y) / 22.0 );
    double retVal = 100.0d * (sn1 * Math.sqrt( 2.0d * Math.PI ) ) * (sn2 * Math.sqrt( 2.0d * Math.PI ) );
    return retVal;
  }

}
