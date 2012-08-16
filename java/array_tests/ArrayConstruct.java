public class ArrayConstruct
{
  public ArrayConstruct( String [][] arrays )
  {
    name  = arrays[0][0];
    mArr1 = arrays[1];
    mArr2 = arrays[2];
  }

  public String [] mArr1 = { "", "", "" };
  public String [] mArr2 = { "", "", "" };
  public String name = "";

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( name + "\nA1:" );
    for( int i = 0; i < mArr1.length; i++ )
    {
      sb.append( " " + mArr1[i] );
    }
    sb.append( "\nA2:" );
    for( int j = 0; j < mArr2.length; j++ )
    {
      sb.append( " " + mArr2[j] );
    }

    return sb.toString();
  }


  public static void main( String [] args )
  {
    ArrayConstruct [] array = new ArrayConstruct [2];

    String [][] O1 = { 
                       { "jomama" },
                       { "A", "B", "C" },
                       { "D", "E", "F" } 
                     };
    String [][] O2 = {
                       { "yomama" },
                       { "U", "V", "W" },
                       { "X", "Y", "Z" }
                     };
    array[0] = new ArrayConstruct( O1 );
    array[1] = new ArrayConstruct( O2 );

    for( int i = 0; i < array.length; i++ )
    {
      
      System.out.println( array[i] );
    }
  }
}
