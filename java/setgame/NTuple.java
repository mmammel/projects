public class NTuple
{
  int [] mArrayIndices      = null;
  int mIndices              = 0;
  int mLastIdx              = 0;
  int mFirstIdx             = 0;

  int mTupleCount           = 0;

  Object [] mTupleHolder    = null;

  TupleProcessor mProcessor = new DefaultTupleProcessor();

  public NTuple()
  {
    mArrayIndices = new int [ 0 ];
    mTupleHolder  = new Object [ 0 ];
  }

  private void init( int n )
  {

    if( mArrayIndices.length != n )
    {
      mArrayIndices = new int [ n ];
    }

    for( int i = 0; i < mArrayIndices.length; i++ )
    {
      mArrayIndices[ i ] = i;
    }

    if( mTupleHolder.length != n )
    {
      mTupleHolder = new Object [ n ];
    }

    mIndices = mArrayIndices.length;
    mLastIdx = mIndices - 1;
    mTupleCount = 0;
  }

  public int getTupleCount()
  {
    return mTupleCount;
  }

  public void setProcessor( TupleProcessor tp )
  {
    mProcessor = tp;
  }

  public void processTuples( Object [] array, int n )
       throws NTupleException
  {
    this.init( n );

    if( array.length < n )
    {
      throw new NTupleException( "Object array length: " + array.length + " has to be larger than the tuple size: " + n );
    }

    this.processTuplesInner( array );
  }

  private void processTuplesInner( Object [] array )
  {
    boolean moreTuples = true;

    while( moreTuples )
    {
  
      if( mArrayIndices[ mFirstIdx ] == (array.length - mIndices) )
      {
        moreTuples = false;
      }
      else
      {
        for( int i = 1; i < mIndices; i++ )
        {
          if( mArrayIndices[ i ] == (array.length - ( mIndices - i ) ) )
          {
            mArrayIndices[ i - 1 ] += 1;
  
            for( int j = i; j < mIndices; j++ )
            {
              mArrayIndices[ j ] = mArrayIndices[ j - 1 ] + 1;
            }

            break;
          }
        }

        while( mArrayIndices[ mLastIdx ] < array.length )
        {
          mProcessor.processTuple( this.fillTuple( array ) );
          mArrayIndices[ mLastIdx ] += 1;
          mTupleCount++;
        }

        mArrayIndices[ mLastIdx ] = array.length - 1;
      }
    }

  }

  public Object [] fillTuple( Object [] array )
  {
    for( int i = 0; i < mIndices; i++ )
    {
      mTupleHolder[ i ] = array[ mArrayIndices[ i ] ];
    }

    return mTupleHolder;
  }

  private class NTupleException extends Exception
  {
    public NTupleException( String msg )
    {
      super( msg );
    }

    public NTupleException( String msg, Exception e )
    {
      super(  msg + "\nUnderlying Cause:\n\n" + e.toString() );
    }
  }

  public static void main( String [] args )
  {
    int objs = 0;
    int N    = 0;
    NTuple NT = null;

    try
    {
      if( args.length != 2 )
      {
        throw new Exception( "Need two arguments: java NTuple <numObjs> <N>" );
      }
      else
      {
        objs = Integer.parseInt( args[ 0 ] );
        N    = Integer.parseInt( args[ 1 ] );
      }

      Object [] array = new Object [ objs ];

      for( int i = 0; i < objs; i++ )
      {
        array[ i ] = new String("" + i);
      }

      NT = new NTuple();
      NT.processTuples( array, N );
      System.out.println( "Tuple Count: " + NT.getTupleCount() );
    }
    catch( Exception e )
    {
      System.out.println( "Caught and Exception!!! " + e.toString() );
      e.printStackTrace();
    }
  }
}
