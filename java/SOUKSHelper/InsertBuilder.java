import java.io.*;
import java.util.*;
import java.util.regex.*;

public class InsertBuilder
{

  public static void main( String [] args )
  {
    try
    {

      BufferedReader inputReader = new BufferedReader( new InputStreamReader( new FileInputStream( "insert_input.txt" ), "UTF-16LE" ));

      List<String[]> items = new ArrayList<String[]>();
      String temp = null;
      while( ( temp = inputReader.readLine()) != null )
      {
        items.add( temp.split("\t") );
      }

      BufferedWriter outputWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "inserts.txt"), "UTF-16LE" ) );

      String inputStr = null;
      int count = 1;

      for( String [] item : items )
      {
        inputStr = "INSERT INTO [QnAllTable]([INDEX_NUM],[QNUM],[QTIME],[QNID],[QUESTION],[FINAL],[WRONG],[LEVEL1],[TOPIC]) VALUES(@nextKey," +
                    (count+2) +
                    ",2,'" + item[1] + "',N'" + item[2] + "','CORRECT',cast(@answers" + count + " as varchar(10) )+'|'+cast( @weight" + count + " as varchar(10)),@weight" + count + ",N'" + item[3] + "')";
        outputWriter.write( inputStr, 0, inputStr.length() );
        outputWriter.newLine();
        count++;
      }

      outputWriter.flush();
    }
    catch( Exception e )
    {
      System.out.println( "Caught Exception: " + e.toString() );
    }

  }

}
