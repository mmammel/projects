//STEP 1. Import required packages
import java.sql.*;

public class Standalone {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerXADataSource";  
   static final String DB_URL = "jdbc:sqlserver://10.51.2.162\\skillcheck:1433";

   //  Database credentials
   static final String USER = "skmgmt";
   static final String PASS = "skillcheck";
   
   public static void main(String[] args) {
   Connection conn = null;
   PreparedStatement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //STEP 4: Execute a query
      String sql;
      //sql = "SELECT customerName as foobar FROM Customer WHERE tableKey IN (?, ?, ?)  AND enabled = 84";
      //sql = "SELECT customerName as foobar FROM Customer WHERE tableKey like ? AND enabled = 84";
      sql = "SELECT CONVERT( DATETIME, ? ) as foobar";
      System.out.println("Creating statement...");
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, "2017-05-16 00:00:00" );
      ResultSet rs = stmt.executeQuery();

      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         String name = rs.getString("foobar");

         //Display values
         System.out.println("Name: " + name);
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
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
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end Standalone
