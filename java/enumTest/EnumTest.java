public class EnumTest
{
  TestEnum mValue = null;

  public EnumTest( TestEnum val )
  {
    mValue = val;
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer();

    if( mValue == TestEnum.JOMAMA )
    {
      buff.append( "D.E. jomama" ).append("\n");
    }
    else if( mValue == TestEnum.YOMAMA )
    {
      buff.append( "D.E. yomama" ).append("\n");
    }
    else if( mValue == TestEnum.MAMMEL )
    {
      buff.append( "D.E. mmammel" ).append("\n");
    }

    if( mValue.equals(TestEnum.JOMAMA) )
    {
      buff.append( "equals jomama" ).append("\n");
    }
    else if( mValue.equals(TestEnum.YOMAMA) )
    {
      buff.append( "equals yomama" ).append("\n");
    }
    else if( mValue.equals(TestEnum.MAMMEL) )
    {
      buff.append( "equals mmammel" ).append("\n");
    }

    return buff.toString();
  }

  public static void main( String [] args )
  {
    EnumTest ET = new EnumTest( TestEnum.MAMMEL );
    System.out.println( ET );
    System.out.println( TestEnum.MAMMEL.ordinal() );
  }
}
