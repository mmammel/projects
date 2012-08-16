import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.ehcache.CacheManager;

public class DAODriver
{
  public static void main( String [] args )
  {
    List queryList = null;

    IntegerDAO dao = new IntegerDAO( "2 3 4 5 10 11 12 13 16 19 20 21 100 101 102 103 104 105 106 1057" );

    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(0,7);
    System.out.println( "(0,7): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );
    
    queryList = dao.query(0,7);
    System.out.println( "(0,7): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );    
    
    queryList = dao.query(2,5);
    System.out.println( "(2,5): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(2,5);
    System.out.println( "(2,5): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );
    
    queryList = dao.query(3,9);
    System.out.println( "(3,9): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(20,100);
    System.out.println( "(20,100): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(2,5);
    System.out.println( "(2,5): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );
    
    queryList = dao.query(0,2);
    System.out.println( "(0,2): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(14,101);
    System.out.println( "(14,101): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(20,500);
    System.out.println( "(20,500): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );

    queryList = dao.query(20,499);
    System.out.println( "(20,499): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );
  
    queryList = dao.query(22,107);
    System.out.println( "(22,107): " + DAODriver.getStringIntegerList( queryList ) );
    System.out.println( "DAO\n---\n" + dao.toString() );
    
    CacheManager.getInstance().shutdown();
  }

  public static String getStringIntegerList( List list )
  {
    StringBuffer buff = new StringBuffer();
    boolean first = true;
    buff.append("[" );

    for( Iterator iter = list.iterator(); iter.hasNext(); )
    {
      if( !first )
      {
        buff.append(",");
      }
      else
      {
        first = false;
      }

      buff.append(((IntegerCacheItem)iter.next()).toString() );
    }

    buff.append("]");

    return buff.toString();
  }

}