package com.fars.navtrack.webservices.util;

import org.apache.log4j.Logger;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Method;

/**
 * User: mammelma
 * Date: Oct 30, 2009
 * Time: 4:53:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnumAdapter extends XmlAdapter<EnumWrapper, Enum> {
    static private Logger logger = Logger.getLogger(EnumAdapter.class);

    public static final String ENUM_PACKAGE = "com.fars.navtrack.domain.general";
    public EnumAdapter(){}

    // Convert a value type to a bound type.
    // read xml content and put into Java class.
    public Enum unmarshal(EnumWrapper wrapper){
      Class clazz = null;

      try
      {
        clazz = Class.forName( ENUM_PACKAGE + "." + wrapper.enumName );
      }
      catch( Exception e )
      {
         logger.error( "Failed while trying to unmarshal an enum: " + e.toString() );
      }

      return Enum.valueOf(clazz,wrapper.enumValue);
    }

    // Convert a bound type to a value type.
    // write Java content into class that generates desired XML
    public EnumWrapper marshal(Enum e){
        Method displayMethod;
        EnumWrapper retVal = new EnumWrapper();

        try
        {
          Class clazz = e.getClass();
          String className = clazz.getName();
          retVal.enumName = className.substring(className.lastIndexOf(".") + 1);
          displayMethod = clazz.getDeclaredMethod("getDisplayValue",(Class[])null);
          retVal.enumDisplay = (String)displayMethod.invoke(e);
          retVal.enumValue = e.name();
        }
        catch( Exception ex )
        {
          logger.error( "Failed while trying to marshal an enum: " + ex.toString() );
        }

        return retVal;
    }
}
