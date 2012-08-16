public class FinalFour
{

  public static void main( String [] args )
  {
    String start = args[0];
    FinalFour FF = new FinalFour();

    try
    {
      while( start.length() > 0 )
      {
        System.out.println( start );
        start = FF.processString( start );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public String processString( String input ) throws NumberFormatException
  {
    boolean firstOfPair = true;
    StringBuffer results = new StringBuffer();
    String [] games = input.split("\\|");

    for( int i = 0; i < games.length; i++ )
    {
      results.append( this.processGame( games[i] ) );

      if( firstOfPair )
      {
        firstOfPair = false;
        results.append(",");
      }
      else
      {
        results.append("|");
        firstOfPair = true;
      }
    }

    String retVal = results.toString();
    return retVal.substring(0,retVal.length() - 1);
  }

  private String processGame( String game ) throws NumberFormatException
  {
    String winner = "";
    int sum = 0,low = 0, high = 0;
    String [] teams = game.split(",");

    if( teams.length == 2 )
    {

      int a = Integer.parseInt( teams[0] );
      int b = Integer.parseInt( teams[1] );

      low = a;
      high = b;
      sum = a + b;

      if( b < a )
      {
        low = b;
        high = a;
      }

      if( (Math.random() * sum) < low )
      {
        winner = "" + high;
      }
      else
      {
        winner = "" + low;
      }
    }

    return winner;
  }
}
