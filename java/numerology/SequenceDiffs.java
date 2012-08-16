public class SequenceDiffs
{
  public void dumpDiffs( int [] array )
  {
    int [] diffArray = new int [ array.length - 1 ];;
    this.printArray( array );
    if( array.length == 1 ) return;
    for( int i = 1; i < array.length; i++ )
    {
      diffArray[ i - 1 ] = Math.abs(array[i] - array[i-1]);
    }

    dumpDiffs( diffArray ); 
  }

  private void printArray( int [] array )
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
    int [] initial = new int [ inputs.length ];
    for( int i = 0; i < inputs.length; i++ )
    {
      initial[i] = Integer.parseInt(inputs[i]);
    }

    SequenceDiffs SD = new SequenceDiffs();
    SD.dumpDiffs( initial );
  }

}
