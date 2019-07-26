package com.fars.navtrack.webservices.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * User: mammelma
 * Date: Oct 30, 2009
 * Time: 4:53:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnumWrapper
{
  @XmlAttribute
  public String enumName;

  @XmlValue
  public String enumValue;

  @XmlAttribute
  public String enumDisplay;
}
