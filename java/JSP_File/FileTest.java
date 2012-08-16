import java.io.*;

public class FileTest
{

  public static void main( String [] args )
  {
    File errorFileDir = null;
    
    errorFileDir = new File("C:\\Documents and Settings\\max.mammel\\My Documents\\projects\\zimmer\\testdata\\errors");
    File [] errorFiles = errorFileDir.listFiles();

    for( int i = 0; i < errorFiles.length; i++ )
    {
      if( errorFiles[i].isFile() )
      {
        System.out.println( "File #" + i + ": " + errorFiles[i].getName() );
      }
    } 
  }
}
