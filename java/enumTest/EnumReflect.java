import java.lang.reflect.Method;

public class EnumReflect
{
  public void printVals( String enumName )
  {
     try
     {
       Class clazz = Class.forName(enumName);
       Method tempMeth,tempOrd;
       tempMeth = clazz.getDeclaredMethod( "displayValue", (Class[])null );

       for( Object obj : clazz.getEnumConstants() )
       {
         //tempOrd = obj.getClass().getDeclaredMethod( "ordinal", (Class[])null );
         System.out.println( "Ordinal: " + ((Enum)obj).ordinal() );
         System.out.println( "DisplayValue: " + (String)tempMeth.invoke(obj) );
       }
     }
     catch( Exception e )
     {
       System.out.println( "Caught an exception: " + e.toString() );
     }
  }

  public static void main( String [] args )
  {
    EnumReflect ER = new EnumReflect();

    ER.printVals( "TestEnum" );
  }

}
