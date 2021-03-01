public enum NullParam {
  NullString(String.class),
  NullInteger(Integer.class),
  NullFloat(Float.class),
  NullDate(java.util.Date.class),
  NullDouble(java.lang.Double.class),
  NullByte(java.lang.Byte.class),
  NullChar(java.lang.Character.class),
  NullSqlDate(java.sql.Date.class),
  NullTimestamp(java.sql.Timestamp.class);
  
  private final Class<?> myClass;
  
  NullParam( Class<?> clazz ) {
    this.myClass = clazz;
  }
  
  public Class<?> getNullClass() {
    return this.myClass;
  }
}
