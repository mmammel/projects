import java.util.Formatter;

public class FormatterTest
{
  public static void main( String [] args )
  {
    Formatter formatter = new Formatter();

    int x = 876;

    FormatterTest FT = new FormatterTest();

    System.out.println( FT.getString(23,123,234) );
  }

    public String getString(int x, int y, int z)
    {
      StringBuilder buffer = new StringBuilder();
          Formatter formatter = new Formatter(buffer);


      if (x != 0)
      {
        formatter.format("%1$03d",x);
              buffer.append("-");
      }

      if (y != 0)
      {
        formatter.format("%1$03d",y);
              buffer.append("-");
      }
      formatter.format("%1$04d",z);

    return buffer.toString();
  }

}
