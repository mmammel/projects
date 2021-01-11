import java.sql.*;

public class Driver {

  static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerXADataSource";
  //static final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  static final String DB_URL = "jdbc:sqlserver://127.0.0.1:1433;database=mydb-dev";

  //  Database credentials
  static final String USER = "xxxxxx";
  static final String PASS = "xxxxxx";

  public static final String DEV = "xxxxxxxxxxxx";

  public static void main( String [] args ) {
    boolean useSingleConnection = false;
    Connection conn = null;
    if( args.length > 0 && args[0].equalsIgnoreCase( "oneConnection" ) ) {
      System.out.println( "Using single connection mode" );
      useSingleConnection = true;
      try {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection( DB_URL, USER, PASS );
        conn.setAutoCommit( false );
      } catch( SQLException sqle ) {
        System.out.println( "Busted trying to get a DB connection: " + sqle.toString() );
      } catch( Exception e ) {
        System.out.println( "Blowed up real good: " + e.toString() );
      }
    }
    String testListUpdate = "UPDATE TestList SET reportCommentsDescription = ? WHERE parentKey = ? AND reportCommentsText != ?";
    QueryThread thr1 = new QueryThread( "QATEST", DEV, testListUpdate, new Preparer() {
                         public PreparedStatement prepareStatement( final PreparedStatement ps ) throws SQLException {
                           ps.setString( 1, "MAXTEST3");
                           ps.setString( 2, "QATEST");
                           ps.setString( 3, "COMMENTS");
                           return ps;
                         }
                       });
    QueryThread thr2 = new QueryThread( "MARC3A", DEV, testListUpdate, new Preparer() {
                         public PreparedStatement prepareStatement( final PreparedStatement ps ) throws SQLException {
                           ps.setString( 1, "MAXTEST3");
                           ps.setString( 2, "MARC3A");
                           ps.setString( 3, "COMMENTS");
                           return ps;
                         }
                       });
    QueryThread thr3 = new QueryThread( "MARC3C", DEV, testListUpdate, new Preparer() {
                         public PreparedStatement prepareStatement( final PreparedStatement ps ) throws SQLException {
                           ps.setString( 1, "MAXTEST3");
                           ps.setString( 2, "MARC3C");
                           ps.setString( 3, "COMMENTS");
                           return ps;
                         }
                       });

    if( useSingleConnection ) {
      thr1.setExternalConnection( conn );
      thr2.setExternalConnection( conn );
      thr2.setExternalConnection( conn );
    }

    QueryThreadRunner runner = new QueryThreadRunner();

    Runnable [] queries = new Runnable[3];
    queries[0] = thr1;
    queries[1] = thr2;
    queries[2] = thr3;

    try {
      runner.runThreads( queries );
    } catch( Exception e ) {
      System.out.println( "Exception! : " + e.toString() );
    } finally {
      if( conn != null ) {
        try {
          conn.close();
        } catch( Exception e ) {
          System.out.println( "Error closing connection: " + e.toString() );
        }
      }
    }
  }
}
