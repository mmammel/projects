public class DefaultTupleProcessor
   implements TupleProcessor
{
  public void processTuple( Object [] tuple )
  {
    StringBuffer buff = new StringBuffer();

    buff.append("[");

    for( int i = 0; i < tuple.length; i++ )
    {
      if( i != 0 )
      {
        buff.append(",");
      }

      if( tuple[ i ] != null )
      {
        buff.append( tuple[ i ].toString() );
      }
      else
      {
        buff.append( "null" );
      }
    }

    buff.append("]");

    System.out.println( buff.toString() );
  }
}
