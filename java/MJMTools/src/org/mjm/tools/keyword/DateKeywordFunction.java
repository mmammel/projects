package org.mjm.tools.keyword;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateKeywordFunction implements IKeywordFunction
{
  private static final String DEFAULT_FORMAT = "MM-dd-yyyy HH:mm:ss";
  
  private SimpleDateFormat dateFormatter = new SimpleDateFormat();

  public String getName()
  {
    return "date";
  }
  
  public String getVal( String val )
  {
    dateFormatter.applyPattern( val );
    return dateFormatter.format( new Date());
  }
  
  public String getVal()
  {
    dateFormatter.applyPattern( DEFAULT_FORMAT );
    return dateFormatter.format( new Date() );
  }

}
