
public class StringArrayComparator
{
  private static final String WILD_CARD = "*";

  public int indexOf( String str, String [] array )
  {
    return this.indexOf( str, array, 0 );
  }

  public int indexOf( String str, String [] array, int startIndex )
  {
    int retIdx = -1;

    if( startIndex >= 0 && startIndex < array.length )
    {
      for( int i = startIndex; i < array.length; i++ )
      {
        if( array[ i ].equals( str ) || array[ i ].equals( WILD_CARD ) )
        {
          retIdx = i;
          break;
        }
      }
    }
    return retIdx;
  }

  public int indexOf( String [] find, String [] array )
  {
    return this.indexOf( find, array, 0, array.length );
  }

  public int indexOf( String [] find, String [] array, int startIndex )
  {
    return this.indexOf( find, array, 0, array.length );
  }

  // startIndex, endIndex -> inclusive, exclusive
  public int indexOf( String [] find, String [] array, int startIndex, int endIndex )
  {
    int retIdx = -1;
    boolean match = true;

    if( endIndex > array.length )
    {
      endIndex = array.length;
    }

    if( endIndex - startIndex >= find.length )
    {
      for( int i = startIndex; i <= ( endIndex - find.length ); i++ )
      {
        match = true;

        for( int j = 0; j < find.length; j++ )
        {
          if( !find[ j ].equals( array[ i + j ] ) && !find[ j ].equals( WILD_CARD ) )
          {
            match = false;
            break;
          }
        }

        if( match )
        {
          retIdx = i;
          break;
        }
      }  
    }
    return retIdx;
  }

  public int lastIndexOf( String str, String [] array )
  {
    return this.lastIndexOf( str, array, array.length - 1 );
  }

  public int lastIndexOf( String str, String [] array, int fromIndex )
  {
    int retIdx = -1;

    if( fromIndex < array.length && fromIndex >= 0 )
    {
      for( int i = fromIndex; i >= 0; i-- )
      {
        if( array[ i ].equals( str ) || array[ i ].equals( WILD_CARD ))
        {
          retIdx = i;
          break;
        }
      }
    }
    return retIdx;
  }

  public int lastIndexOf( String [] find, String [] array )
  {
    return this.lastIndexOf( find, array, array.length - 1 );
  }

  public int lastIndexOf( String [] find, String [] array, int fromIndex )
  {
    int retIdx = -1;
    boolean match = true;

    if( fromIndex < array.length && fromIndex >= 0 && (fromIndex + 1) >= find.length )
    {
      for( int i = (fromIndex - find.length + 1); i >= 0; i-- )
      {
        match = true;

        for( int j = 0; j < find.length; j++ )
        {
          if( !find[ j ].equals( array[ i + j ] ) && !find[ j ].equals( WILD_CARD ) )
          {
            match = false;
            break;
          }
        }
     
        if( match )
        {
          retIdx = i;
          break;
        }
      }
    }
    return retIdx; 
  }

  public static void main( String [] args )
  {
    String [] array1 = { "1", "2", "3", "9", "5", "6", "7", "8", "9", "10", "5", "6", "7" };
    String [] array2 = { "5", "6", "7" };
    String [] array3 = { "1", "2", "4" };
    String [] array4 = { "1", "2", "3" };

    StringArrayComparator SAC = new StringArrayComparator();

    System.out.println( "Result 1: " + SAC.indexOf( "9", array1 ) );
    System.out.println( "Result 2: " + SAC.indexOf( "9", array1, 6 ) );
    System.out.println( "Result 3: " + SAC.indexOf( "3", array1, 8 ) );
    System.out.println( "Result 4: " + SAC.lastIndexOf( "9", array1 ) );
    System.out.println( "Result 5: " + SAC.lastIndexOf( "9", array1, 2 ) );
    System.out.println( "Result 6: " + SAC.indexOf( array2, array1 ) );
    System.out.println( "Result 7: " + SAC.indexOf( array3, array1 ) );  
    System.out.println( "Result 8: " + SAC.indexOf( array2, array1, 5 ) );
    System.out.println( "Result 9: " + SAC.lastIndexOf( array2, array1 ) );
    System.out.println( "Result 10: " + SAC.lastIndexOf( array2, array1, 12 ) );
    System.out.println( "Result 11: " + SAC.indexOf( array1, array4 ) );

  }
}

