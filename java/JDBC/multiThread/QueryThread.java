import java.sql.*;

public class QueryThread implements Runnable {
   static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerXADataSource";  
   //static final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";  
   static final String DB_URL = "jdbc:sqlserver://127.0.0.1:1433;database=mydb-dev";

   //  Database credentials
   static final String USER = "xxxxxx";
   static final String PASS = "xxxxxxxxxx";

   private Connection conn = null;
   private Preparer preparer = null;
   private String query = null;
   private boolean isUpdateInsert = false;
   private String name = null;
   private boolean useExternalConnection = false;
   
   public QueryThread( String name, String db, String query, Preparer p ) {
     String dbURL = "jdbc:sqlserver://127.0.0.1:1433;database=" + db;
     //String dbURL = "jdbc:jtds:sqlserver://127.0.0.1:1433/" + db;
     try {
       Class.forName(JDBC_DRIVER);
       this.conn = DriverManager.getConnection(dbURL,USER,PASS);
       this.conn.setAutoCommit( false );
     } catch( Exception e ) {
       System.out.println( "Blew up trying to initialize the connections! " + e.toString());
     }
     this.preparer = p;
     this.query = query;
     this.name = name;
     this.isUpdateInsert = !this.query.toUpperCase().startsWith( "SELECT" );
   }

   public void setExternalConnection( Connection conn ) {
     this.useExternalConnection = true;
     this.conn = conn;
   }
   
   public void run() {
     PreparedStatement stmt = null;

     try{

      //STEP 4: Execute a query
      stmt = this.conn.prepareStatement(this.query);
      this.preparer.prepareStatement( stmt );

      if( this.isUpdateInsert ) {
        int n = stmt.executeUpdate();
        System.out.println( "Update: " + this.name + " affected " + n + " rows" );
        this.conn.commit();
      } else {
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
          System.out.println( "Got a row..." );
        }
        rs.close();
      }
      //STEP 6: Clean-up environment
      stmt.close();
      if( !this.useExternalConnection ) this.conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(this.conn!=null)
            if( !this.useExternalConnection ) conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end QueryThread
