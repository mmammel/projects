import java.util.List;
import java.util.ArrayList;

public class Driver
{
  public static void main( String [] args)
  {
    SequenceCache SC = new SequenceCache("testCache");
    List testList = null;

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(5) );
    testList.add( new IntegerCacheItem(6) );
    testList.add( new IntegerCacheItem(7) );
    testList.add( new IntegerCacheItem(8) );
    testList.add( new IntegerCacheItem(9) );
    testList.add( new IntegerCacheItem(10) );
    testList.add( new IntegerCacheItem(11) );
    testList.add( new IntegerCacheItem(12) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(20) );
    testList.add( new IntegerCacheItem(21) );
    testList.add( new IntegerCacheItem(22) );
    testList.add( new IntegerCacheItem(23) );
    testList.add( new IntegerCacheItem(24) );
    testList.add( new IntegerCacheItem(25) );
    testList.add( new IntegerCacheItem(26) );
    testList.add( new IntegerCacheItem(27) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(212) );
    testList.add( new IntegerCacheItem(213) );
    testList.add( new IntegerCacheItem(214) );
    testList.add( new IntegerCacheItem(215) );
    testList.add( new IntegerCacheItem(216) );
    testList.add( new IntegerCacheItem(217) );
    testList.add( new IntegerCacheItem(218) );
    testList.add( new IntegerCacheItem(219) );
    testList.add( new IntegerCacheItem(220) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(17) );
    testList.add( new IntegerCacheItem(18) );
    testList.add( new IntegerCacheItem(19) );
    testList.add( new IntegerCacheItem(20) );
    testList.add( new IntegerCacheItem(21) );
    testList.add( new IntegerCacheItem(22) );
    testList.add( new IntegerCacheItem(23) );
    testList.add( new IntegerCacheItem(24) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(7) );
    testList.add( new IntegerCacheItem(8) );
    testList.add( new IntegerCacheItem(9) );
    testList.add( new IntegerCacheItem(10) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    testList = new ArrayList();

    testList.add( new IntegerCacheItem(12) );
    testList.add( new IntegerCacheItem(13) );
    testList.add( new IntegerCacheItem(14) );
    testList.add( new IntegerCacheItem(15) );
    testList.add( new IntegerCacheItem(16) );
    testList.add( new IntegerCacheItem(17) );
    testList.add( new IntegerCacheItem(18) );
    testList.add( new IntegerCacheItem(19) );

    SC.insert( testList );

    System.out.println( "-------------------\n" + SC + "\n-------------------" );

    SequenceCacheQueryResult result = null;

    result = SC.query( new SequenceDescriptor( new IntegerCacheItem(0), new IntegerCacheItem( 10 ) ) );
    System.out.println( "\n\n--------------\nResult for 0 to 10 query: \n" + result.toString() );
    result = SC.query( new SequenceDescriptor( new IntegerCacheItem(30), new IntegerCacheItem( 300 ) ) );
    System.out.println( "\n\n--------------\nResult for 30 to 300 query: \n" + result.toString() );
    result = SC.query( new SequenceDescriptor( new IntegerCacheItem(10), new IntegerCacheItem( 15 ) ) );
    System.out.println( "\n\n--------------\nResult for 10 to 15 query: \n" + result.toString() );
    result = SC.query( new SequenceDescriptor( new IntegerCacheItem(18), new IntegerCacheItem( 33 ) ) );
    System.out.println( "\n\n--------------\nResult for 18 to 33 query: \n" + result.toString() );


  }
}