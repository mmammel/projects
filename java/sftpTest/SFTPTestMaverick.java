import com.sshtools.net.SocketTransport;
import com.sshtools.ssh.HostKeyVerification;
import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpFile;
import com.sshtools.sftp.SftpFileAttributes;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.components.SshPublicKey;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh2.Ssh2Client;
import com.sshtools.ssh2.Ssh2Context;
import java.io.*;

public class SFTPTestMaverick {
  public static void main( String [] args ) {
   try {
    SFTPTestMaverick SFTP = new SFTPTestMaverick();
    SFTP.processFiles("fe01.ultipro.com", "SFTPDEB1001_Rec", "niR5Aefm" );
    //SFTP.processFiles("208.86.168.142", "SFTPDEB1001_Rec", "niR5Aefm" );
    } catch( Exception e ) {
      System.out.println("Caught an exception: " + e.toString() );
    }
  }

  public void processFiles( String server, String username, String password ) {

    try {
      /**
       * Create an SshConnector instance
       */
      SshConnector con = SshConnector.createInstance();

      HostKeyVerification hkv = new HostKeyVerification() {
        public boolean verifyHost(String name, SshPublicKey key) {
                // Verify the host somehow???
                return true;
        }
      };

      // Lets do some host key verification

      con.getContext().setHostKeyVerification(hkv);

      /**
       * Connect to the host
       */
      SocketTransport t = new SocketTransport(server, 22);
      t.setTcpNoDelay(true);

      SshClient ssh = con.connect(t, username);

      Ssh2Client ssh2 = (Ssh2Client) ssh;
      /**
       * Authenticate the user using password authentication
       */
      PasswordAuthentication pwd = new PasswordAuthentication();

      do {
        pwd.setPassword(password);
      } while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE
          && ssh.isConnected());

      /**
       * Start a session and do basic IO
       */
      if (ssh.isAuthenticated()) {
        System.out.println( "We authenticated!!!!!" );
      }

    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

