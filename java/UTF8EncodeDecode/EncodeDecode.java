import java.io.*;

class EncodeDecode
{

  public static void main( String [] args )
  {
    byte [] byteArray = { 0x5B, Byte.parseByte("-30", 10 ), Byte.parseByte("-128", 10 ), Byte.parseByte("-100", 10 ), Byte.parseByte("-30", 10 ), Byte.parseByte("-128", 10 ), Byte.parseByte("-99", 10 ), 0x5D };
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    try
    {
      String testStr = "[\u201C\u201D]";
      String testString = null;
      byteArray = testStr.getBytes( "windows-1252" );

      EncodeDecode.printByteArray( byteArray );
      testString = new String( byteArray, "windows-1252" );
      byteArray = testString.getBytes( "UTF-8" );

      for( int j = 0; j < 10; j++ )
      {
        stream = new ByteArrayOutputStream();
        EncodeDecode.writeToStream( stream, byteArray );

        EncodeDecode.printByteArray( stream.toByteArray() );
        System.out.println( stream.toString("UTF-8") );

        testString = new String( stream.toString().getBytes(), "UTF-8" );

        System.out.println( testString );

        byteArray = testString.getBytes( "UTF-8" );

        EncodeDecode.printByteArray( byteArray );
      }

    }
    catch( Exception e )
    {
      System.out.println( "Caught an Exception: " + e.toString() );
    }
  }

  public static void printByteArray( byte [] array )
  {
    System.out.print( "[ ");
    for( int i = 0; i < array.length; i++ )
    {
      System.out.print( Byte.toString( array[i] ) + " " );
    }
    System.out.println( "]");
  }

  public static void writeToStream( ByteArrayOutputStream out, byte [] array )
  {
    try
    {
      out.write( array, 0, array.length );
      out.close();
    }
    catch( Exception e )
    {
      System.out.println( "Caught another exception: " + e.toString());
    }

  }

}