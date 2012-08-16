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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class CryptoTest
{
  //private static final String DEFAULT_ALGORITHM = "PBEWithMD5AndDES";
  private static final String DEFAULT_ALGORITHM = "PBEWithMD5AndAES";

  public static final byte[] salt = {
              (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
              (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
  };

  public static final int iterationCount = 19;

  protected Cipher encryptCipher;
  protected Cipher decryptCipher;

  public void init( String algorithm ) throws Exception
  {
    String secret = "JoMaMa";

    // create a key spec based on the password.
    KeySpec keySpec = new PBEKeySpec(secret.toCharArray(), salt, iterationCount, 128);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
    SecretKey key = factory.generateSecret(keySpec);

    byte [] raw = key.getEncoded();

    System.out.println( "Length of encoded key: " + raw.length );

    if( raw.length != 16 )
    {
      byte [] new_raw = new byte [16];

      for( int i = 0; i < 16; i++ )
      {
        new_raw[i] = raw[i%raw.length];
      }

      raw = new_raw;
    }

    // now create an AES key from that.
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    encryptCipher = Cipher.getInstance("AES");
    decryptCipher = Cipher.getInstance("AES");
    //AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
    encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);
  }

  public static void main( String [] args )
  {
    CryptoTest CT = new CryptoTest();
    String inputStr = null;
    BufferedReader inputReader = null;
    String prompt = "Enter an algorithm String (e.g. \"PBEWithMD5AndDES\") : ";

    Provider []  providers = Security.getProviders();

    for( int j = 0; j < providers.length; j++ )
    {
      System.out.println( providers[j].getName() + " :" );
      System.out.println( providers[j].getInfo() );
    }

    try
    {
      inputReader = new BufferedReader( new InputStreamReader( System.in ) );

      System.out.print( prompt );

      while( (inputStr = inputReader.readLine()) != null )
      {
        try
        {
          if( inputStr.equalsIgnoreCase( "quit" ) )
          {
            System.out.println( "Goodbye." );
            break;
          }

          CT.init( inputStr.trim() );
          System.out.println( "Success!" );
        }
        catch( Exception ie )
        {
          System.out.println( "No Good: " + ie.toString() );
          ie.printStackTrace();
        }

        System.out.print( prompt );
      }


    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }

  }

}