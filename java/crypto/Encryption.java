import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.*;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
public class Encryption {

  static String encryptionString1 = "I did not fail the test, I just found 100 ways to do it wrong.";

  static public String encryptPassword(String passwordIn) {
    try {
        ConfigurablePasswordEncryptor conPassEncrypt = new ConfigurablePasswordEncryptor();
        conPassEncrypt.setAlgorithm("SHA-512");
        conPassEncrypt.setPlainDigest(false);
        conPassEncrypt.setStringOutputType("base64");  // default
        String passwordOut = conPassEncrypt.encryptPassword(passwordIn);

      return passwordOut;

    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static public boolean checkPassword(String passwordIn, String passwordCompare) {

    try {
      ConfigurablePasswordEncryptor conPassEncrypt = new ConfigurablePasswordEncryptor();
      conPassEncrypt.setAlgorithm("SHA-512");

      if(conPassEncrypt.checkPassword(passwordIn, passwordCompare))
        return true;
      else
        return false;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

  }


static StandardPBEStringEncryptor getTextStandardPBEEncryptor (boolean strong, String encryptPassword) {

  StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();

  if (strong == true) {
      stringEncryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
  }
  else
      stringEncryptor.setAlgorithm("PBEWithMD5AndDES");
    stringEncryptor.setStringOutputType("base64");
    stringEncryptor.setSaltGenerator(new ZeroSaltGenerator());
    stringEncryptor.setPassword(encryptPassword + Encryption.encryptionString1 + encryptPassword);

  return stringEncryptor;
}

static BasicTextEncryptor getTextStandardEncryptor (boolean strong, String encryptPassword) {

  BasicTextEncryptor stringEncryptor = new BasicTextEncryptor();
  stringEncryptor.setPassword ( encryptPassword );

  return stringEncryptor;
}


static StrongTextEncryptor getTextEncryptor () {

  StrongTextEncryptor stringEncryptor = new StrongTextEncryptor();
    stringEncryptor.setPassword(Encryption.encryptionString1);
    return stringEncryptor;

}
static public String encryptTextString(String encryptPassword, String textStringIn) {
  String encryptedText =  null;
  try {

    String encryptKey = encryptionString1;
    StandardPBEStringEncryptor stringEncryptor = getTextStandardPBEEncryptor(false, encryptKey);
    encryptedText = stringEncryptor.encrypt(textStringIn);

  return encryptedText;
  }
  catch (Exception e) {
    throw new RuntimeException(e);
  }

}

static public String decryptTextString(String encryptPassword, String textStringIn) {
  try {

    String encryptKey = encryptionString1;
    StandardPBEStringEncryptor stringEncryptor = getTextStandardPBEEncryptor(false, encryptKey);
    String decryptedText = stringEncryptor.decrypt(textStringIn);

  return decryptedText;
  }
  catch (Exception e) {
    throw new RuntimeException(e);
  }

}

static public boolean checkPassword(String encryptPassword, String passwordIn, String passwordCompare) {

  try {
    String passwordDecrypted = decryptTextString(encryptPassword, passwordCompare);

    if(passwordDecrypted.equals(passwordIn))
      return true;
    else
      return false;
  }
  catch (Exception e) {
    throw new RuntimeException(e);
  }
}

  public static void main( String [] args )
  {
    if( args.length == 2 )
    {
       if( args[0].equalsIgnoreCase("encrypt") ) {
          System.out.println(  "Encrypted: " +  Encryption.encryptPassword(args[1]) );
       }
       else if( args[0].equalsIgnoreCase("decrypt") ) {
          System.out.println(  "Decrypted: " +  Encryption.decryptTextString(args[1], args[1]) );
       }
       else
       {
          System.out.println( "Usage: runDecrypter.sh [encrypt|decrypt] <string>" );
       }
     } else {
          System.out.println( "Usage: runDecrypter.sh [encrypt|decrypt] <string>" );
      }
  }
}
