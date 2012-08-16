import java.util.*;
import java.lang.reflect.*;

public class EnumValues
{
    public static Map<Enum123,String> enum123Values = new EnumMap<Enum123,String>(Enum123.class);
    public static Map<EnumABC,String> enumABCValues = new EnumMap<EnumABC,String>(EnumABC.class);

    static {
      enum123Values.put(Enum123.ONE,"One");
      enum123Values.put(Enum123.TWO,"Two");
      enum123Values.put(Enum123.THREE,"Three");
      enumABCValues.put(EnumABC.APPLE,"Apple");
      enumABCValues.put(EnumABC.BANANA,"Banana");
      enumABCValues.put(EnumABC.CHERRY,"Cherry");

    }


    public static String getValue( Object obj )
    {
      String retVal = null;
      Class clazz = obj.getClass();
      if( clazz == Enum123.class )
      {
        retVal = enum123Values.get(obj);
      }
      else if( clazz == EnumABC.class )
      {
        retVal = enumABCValues.get(obj);
      }

      return retVal;
    }

    public static List<List<String>> getDisplayArray(String name)
    {

      List<String> tempList = null;
      List<List<String>> retList = new ArrayList<List<String>>();
try
{
      Class clazz = Class.forName(name);
      Method tempMeth;

System.out.println( clazz.getEnumConstants().length );
      for( Object obj : clazz.getEnumConstants() )
      {
        tempList = new ArrayList<String>();
        tempList.add( obj.toString() );
        tempMeth = clazz.getDeclaredMethod( "displayValue", (Class[])null );
        tempList.add( (String)tempMeth.invoke(obj) );
        retList.add( tempList );
      }

} catch( Exception e )
{
  System.out.println( "Exception: " + e.toString() );
  e.printStackTrace();
  }
      return retList;
    }


    public static void main( String [] args )
    {
      System.out.println( EnumValues.getValue( Enum123.ONE ) );
      System.out.println( EnumValues.getValue( EnumABC.APPLE ) );
      System.out.println( EnumValues.getValue( Enum123.THREE ) );
      System.out.println( EnumValues.getValue( EnumABC.BANANA ) );

      List<List<String>> vals = EnumValues.getDisplayArray( "EnumDisplay" );

      for( List<String> lst : vals )
      {
        System.out.println( "Entry:");
        for( String str : lst )
        {
          System.out.println( "  " + str );
        }
      }
    }
}
