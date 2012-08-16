package org.mjm.cryptogram;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class CipherTable
{
  public static final char NONE = '0';
  private char [] cipher = null;
  
  public CipherTable()
  {
    this.cipher = new char [26];
    this.reset();
  }
  
  public CipherTable( CipherTable t )
  {
    this.cipher = Arrays.copyOf(t.cipher, 26);
  }
  
  public void set( char a, char b )
  {
    this.cipher[a-97] = b;
  }
  
  /*
   Return a string of characters that have mappings.
   */
  public String getMapped()
  {
    StringBuilder retVal = new StringBuilder();
    for( int i = 0; i < 26; i++ )
    {
      if( this.cipher[i] != NONE )
      {
        retVal.append( (char)(i+97) );
      }
    }
    
    return retVal.toString();
  }
  
  public String getUnMapped()
  {
    String retVal = "abcdefghijklmnopqrstuvwxyz";
    for( int i = 0; i < 26; i++ )
    {
      if( this.cipher[i] != NONE )
      {
        retVal = retVal.replace(""+this.cipher[i],"");
      }
    }
    
    return retVal.toString();    
  }
  
  public String applyCipher( String s )
  {
    StringBuilder retVal = new StringBuilder();
    
    for( int i = 0; i < s.length(); i++ )
    {
      retVal.append( this.cipher[s.charAt(i) - 97] );
    }
    
    return retVal.toString();
  }
  
  public void set( String s1, String s2 )
  {
    if( s1.length() != s2.length() ) throw new IllegalArgumentException("String sizes must match!");
    
    for( int i = 0; i < s1.length(); i++ )
    {
      this.cipher[s1.charAt(i) - 97] = s2.charAt(i);
    }
  }
  
  public void reset()
  {
    Arrays.fill( this.cipher, NONE );
  }
  
  public void reset( char a )
  {
    this.cipher[a-97] = NONE;
  }
  
  public void reset( String s )
  {
    for( int i = 0; i < s.length(); i++ )
    {
      this.cipher[s.charAt(i)] = NONE;
    }
  }
  
  public boolean isMapped( char c )
  {
    return (this.cipher[c-97] != NONE);
  }
  
  public char get(char a)
  {
    return this.cipher[a-97];
  }
}