import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Security;
import java.security.Provider;
import java.util.Set;
import java.util.Iterator;

public class CryptoInfo
{
  public static final String [] SERVICES = { "Signature", "MessageDigest", "Cipher", "Mac", "KeyStore" };

  public static void main( String [] args )
  {
    System.out.println( "Security Providers" );
    System.out.println( "------------------" );

    Provider []  providers = Security.getProviders();

    for( int j = 0; j < providers.length; j++ )
    {
      System.out.println( "\n" + j + ". " + providers[j].getName() + " :" );
      System.out.println( providers[j].getInfo() );
    }

    System.out.println( "\nAlgorithms" );
    System.out.println( "----------" );

    Set algs = null;

    for( int i = 0; i < SERVICES.length; i++ )
    {
      System.out.println( "\n" + (i+1) + ". " + SERVICES[i] );

      algs = Security.getAlgorithms( SERVICES[i] );
      String tempStr = null;

      for( Iterator iter = algs.iterator(); iter.hasNext(); )
      {
        tempStr = (String)iter.next();
        System.out.println( "  * " + tempStr );
      }
    }
  }
}