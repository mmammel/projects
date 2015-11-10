import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SFTPTest {
  public static void main( String [] args ) {
   try {
      Handler fh = new FileHandler("example.log");
      fh.setFormatter(new SimpleFormatter());
      Logger.getLogger("com.sshtools").setUseParentHandlers(false);
      Logger.getLogger("com.sshtools").addHandler(fh);
      Logger.getLogger("com.sshtools").setLevel(Level.ALL);
    SFTPTest SFTP = new SFTPTest();
    //SFTP.processFiles("fe01.ultipro.com", "SFTPDEB1001_Rec", "niR5Aefm" );
    SFTP.processFiles("208.86.168.142", "SFTPDEB1001_Rec", "niR5Aefm" );
    } catch( Exception e ) {
      System.out.println("Caught an exception: " + e.toString() );
    }
  }

  public void processFiles( String server, String username, String password ) {
    SshClient ssh = new SshClient();
    SftpClient ftp = null;
    PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();

    try {
      ssh.connect(server, 22, new IgnoreHostKeyVerification());
      pwd.setUsername(username);
      pwd.setPassword(password);

      int result = ssh.authenticate(pwd);

      System.out.println( "Result of auth: " + result );
    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

