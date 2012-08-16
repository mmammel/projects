public class ArrayInit
{
    String str_m;
    int integer_m;
    Object obj_m;
  
  public static void main( String [] args )
  {
     String [] array = new String [5];

     for( int i = 0; i < array.length; i++ )
     {
       System.out.println( array[i] );
     }

     String instStr = null;
     int instInt = 0;
     Object instObj = null;

     Inner test = new Inner();
     ArrayInit test2 = new ArrayInit();

     System.out.println("Values: "
        + instStr + "," + instInt + "," + instObj + 
        test.str+ "," + test.integer + "," + test.obj +"," +
        test2.str_m+ "," + test2.integer_m + "," + test2.obj_m );
  }

  public static class Inner
  {
    String str;
    int integer;
    Object obj;
  }
}
