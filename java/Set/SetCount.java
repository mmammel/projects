public class SetCount
{
  public static String [] DES = { "001", "010", "100" };
  public static String [] SES = { "000", "111" };

  public static void main( String [] args )
  {
    String temp = null;
    int tempInt = 0;

    for( int i = 0; i < 3; i++ )
    {
      for( int j = 0; j < 3; j++ )
      {
        for( int k = 0; k < 3; k++ )
        {
          for( int l = 0; l < 3; l++ )
          {
            temp = DES[i] + DES[j] + DES[k] + DES[l];
            tempInt = Integer.parseInt( temp, 2 );
            System.out.print( temp + ": " + tempInt ); 
            if( tempInt % 7 == 0 )
            {
              System.out.println( " Div. by 7" );
            }
            else
            {
              System.out.println( "" );
            }
          }
        }
      }
    }

    System.out.println( "--------------------------" );

    for( int m = 0; m < 2; m++ )
    {
      for( int n = 0; n < 2; n++ )
      {
        for( int o = 0; o < 2; o++ )
        {
          for( int p = 0; p < 2; p++ )
          {
            temp = SES[m] + SES[n] + SES[o] + SES[p];
            System.out.println( temp + ": " + Integer.parseInt( temp, 2 ) );
          }
        }
      }
    }
  }
}
