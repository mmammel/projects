import java.math.BigInteger;

public class SequenceDiffs
{
  public void dumpDiffs( BigInteger [] array )
  {
    BigInteger [] diffArray = new BigInteger [ array.length - 1 ];;
    this.printArray( array );
    if( array.length == 1 ) return;
    for( int i = 1; i < array.length; i++ )
    {
      diffArray[ i - 1 ] = (array[i].subtract(array[i-1])).abs();
    }

    dumpDiffs( diffArray ); 
  }

  private void printArray( BigInteger [] array )
  {
    for( int i = 0; i < array.length; i++ )
    {
      System.out.print( "[" + array[i] + "]" );
    }
    System.out.print( "\n" );
  }

  public static void main( String [] args )
  {
    String [] inputs = args[0].split(",");
    BigInteger [] initial = new BigInteger [ inputs.length ];
    for( int i = 0; i < inputs.length; i++ )
    {
      initial[i] = new BigInteger(inputs[i]);
      System.out.println( initial[i].isProbablePrime(5) );
    }

    SequenceDiffs SD = new SequenceDiffs();
    SD.dumpDiffs( initial );
  }

}
