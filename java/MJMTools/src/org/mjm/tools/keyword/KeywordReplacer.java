
package org.mjm.tools.keyword;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import org.mjm.tools.lookuptree.*;

/**
 * This service does text replacement based on keywords and values provided by the user.
 */
public class KeywordReplacer
{
  protected static final String DEFAULT_KEY_START   = "${";
  protected static final String DEFAULT_KEY_END     = "}";
  protected static final String FUNCTION_PREFIX     = "func:";
  protected static final String FUNC_UNIQUE_ID      = "getUniqueID";

  protected static final int OUT_BUFFER_SIZE        = 102400;
  protected static final int INITIAL_BUFF_SIZE      = 102400;
  protected static final int DOC_BUFF_SIZE          = 32768;

  protected ByteArrayOutputStream mOutStreamBuffer  = null;
  protected InputStream mInStream                   = null;
  protected OutputStream mOutStream                 = null;

  protected Map mDocKeywordMap                      = new HashMap();
  protected Map mDocKeywordsOnlyMap                 = new HashMap();
  protected Map mStrKeywordMap                      = new HashMap();
  protected Map mStrKeywordsOnlyMap                 = new HashMap();
  protected String mKeyStart                        = DEFAULT_KEY_START;
  protected String mKeyEnd                          = DEFAULT_KEY_END;

  protected LookupTree mKeywordLookupTree           = new LookupTree();
  protected LookupTreeIterator mTreeIter            = null;

  protected byte [] mInStreamBuffer                 = new byte [ INITIAL_BUFF_SIZE ];

  protected int mInStreamIdx                        = 0;
  protected int mInStreamCount                      = 0;
  protected boolean mHitEOF                         = false;
  protected HashMap mFunctionTable                  = new HashMap();

  protected void init()
  {
    mInStreamIdx = 0;
    mInStreamCount = 0;
    mHitEOF = false;
    mInStreamBuffer = new byte [ INITIAL_BUFF_SIZE ];
  }

  public KeywordReplacer()
  {
    this.registerKeywordFunction( new DateKeywordFunction() );
  }

  public KeywordReplacer( String keyStart, String keyEnd ) 
  {
    this();
    mKeyStart = keyStart;
    mKeyEnd = keyEnd;
  }

  public void reset()
  {
    this.clearKeywords();    
    this.init();
  }

  protected void clearKeywords()
  {
    mDocKeywordsOnlyMap.clear();
    mStrKeywordsOnlyMap.clear();
  }

  /**
   * Called when a keyword/value pair needs to be set
   * @param key The Keyword
   * @param replaceVal The value to replace keyword
   */
  public void setStringKeyword( String key, String replaceVal )
  {
    if( key != null && key.length() >= 1 )
    {
      mStrKeywordsOnlyMap.put(key, replaceVal);
    }
  }

  /**
   * Called when a keyword/Document pair needs to be set
   * @param key The Keyword
   * @param doc The document to insert in place of keyword
   */
  public void setDocumentKeyword( String key, InputStream doc ) 
  {
    if( key != null && key.length() >= 1 )
    {
      mDocKeywordsOnlyMap.put( key, doc );
    }
  }

  public void registerKeywordFunction( IKeywordFunction func )
  {
    mFunctionTable.put( func.getName(), func );
  }

  /**
   * Called to reconcile the keywords and key tags if the tags change
   */
  protected void loadKeywordInfo() 
  {
    mKeywordLookupTree    = new LookupTree();
    mStrKeywordMap        = new HashMap();
    mDocKeywordMap        = new HashMap();
    String totalKey = null, tempKey = null, tempReplace = null;

    //First load the doc keywords
    for( Iterator iter = mDocKeywordsOnlyMap.keySet().iterator(); iter.hasNext(); ) 
    {
      tempKey = (String)iter.next();

      totalKey = mKeyStart + tempKey + mKeyEnd;

      mKeywordLookupTree.addWord( totalKey );
      mDocKeywordMap.put( totalKey, mDocKeywordsOnlyMap.get( tempKey ) );
    }

    //then load the string keywords
    for( Iterator iter = mStrKeywordsOnlyMap.keySet().iterator(); iter.hasNext(); ) 
    {
      tempKey = (String)iter.next();

      totalKey = mKeyStart + tempKey + mKeyEnd;

      tempReplace = (String)mStrKeywordsOnlyMap.get( tempKey );

      if( tempReplace != null && tempReplace.indexOf( totalKey ) == -1 )
      {
        mKeywordLookupTree.addWord( totalKey );
        mStrKeywordMap.put( totalKey, tempReplace );
      }
    }

    mStrKeywordsOnlyMap = null;
    mDocKeywordsOnlyMap = null;
    mTreeIter = mKeywordLookupTree.getIterator();
  }

  /**
   * Called to customize the keyword start pattern.  Default: ${
   * @param start Set the pattern that represents a keyword start
   */
  public void setKeyStart( String start ) 
  {
    if( start != null ) 
    {
      mKeyStart = start;
    }
  }

  /**
   * Called to customize the keyword end pattern.  Default: }
   * @param end Set the pattern that represents a keyword's end
   */
  public void setKeyEnd( String end ) 
  {
    if( end != null ) 
    {
      mKeyEnd = end;
    }
  }

  public void processStreams( Map strKeys, Map docKeys, InputStream is, OutputStream os ) throws IOException, KeywordException
  {
    this.clearKeywords();
    
    if( strKeys != null )
    {
      mStrKeywordsOnlyMap.putAll( strKeys );
    }
    
    if( docKeys != null )
    {
      mDocKeywordsOnlyMap.putAll( docKeys );
    }
    
    this.processStreams( is, os );
  }

  /**
   * Allows a caller to process any input stream
   * @param is The InputStream to look for keywords in
   * @param os The OutputStream to place the processed bytes in
   * @exception Exception
   */
  public void processStreams( InputStream is, OutputStream os ) throws IOException, KeywordException 
  {
    mOutStreamBuffer = new ByteArrayOutputStream();
    mInStream  = is;
    mOutStream = os;

    this.init();
    this.loadKeywordInfo();
    this.lookForKeywords();

    mOutStreamBuffer.writeTo( mOutStream );
    mOutStreamBuffer.close();
    mInStream.close();
    mOutStream.close();
  }


  /**
   * Actually do the work of processing the bytes from the input stream
   * @exception IOException
   * @exception KeywordException
   */
  protected void lookForKeywords() throws IOException, KeywordException 
  {
    int getByte = 0;
    char currentChar = (char)0;

    while( (getByte = this.getNextChar()) != -1 ) 
    {
      currentChar = (char)getByte;
      if( mKeywordLookupTree.isRootChar (currentChar) ) 
      {
        this.doKeyword( currentChar );
      } 
      else 
      {
        this.writeToOutBuffer( currentChar );
      }
    } //end while

  }

  /**
   * Wrapper for getting a byte from the stream - basically a custom form of an
   * input buffer.  Cannot use the standard input buffer because bytes may need to
   * be written back to the buffer in front of the current pointer.
   * @exception IOException
   * @return int An int whose low 8 bytes are the next character
   */
  protected int getNextChar() throws IOException 
  {
    int returnByte = 0;

    if( mInStreamCount == mInStreamIdx )
    {
      if( mHitEOF )
      {
        returnByte = -1;
      }
      else
      {
        mInStreamCount = mInStream.read( mInStreamBuffer );
        mInStreamIdx = 0;

        returnByte = (int)mInStreamBuffer[ mInStreamIdx++ ];

        if( mInStreamCount == -1 )
        {
          //the rare case that the stream length is a multiple of
          //the buffer size
          returnByte = -1;
        }
        else if( mInStreamCount < mInStreamBuffer.length )
        {
          mHitEOF = true;
        }
      }
    }
    else
    {
      returnByte = (int)mInStreamBuffer[ mInStreamIdx++ ];
    }

    return returnByte;
  }

  /**
   * Found the first character of the keyword start, start examining the bytes following.
   * Uses the lookupTree to incrementally validate the potential keyword.  If the keyword
   * eventually validates and is a word in the lookup tree - replace it with the appropriate
   * value in the output stream.  If it fails validation at any point write all but the
   * first character of what we read back to the *input* stream to be processed again.
   * @param c The first character of a potential keyword.
   * @exception IOException
   * @exception KeywordException
   */
  protected void doKeyword( char c ) throws IOException, KeywordException 
  {
    StringBuffer tempBuffer = new StringBuffer();
    mTreeIter.reset();

    if( !mTreeIter.tryCharacter( c ) )
    {
      //if this is no good something is terribly wrong
      throw new KeywordException( "Corrupt key data" );
    }
    
    tempBuffer.append( c );

    if (mTreeIter.isWord()) 
    {
      this.replaceKeyword( mTreeIter.getCurrentBuffer() );
    } 
    else 
    {
      while( mTreeIter.tryCharacter( (char)this.getNextChar() ) )
      {
        if( mTreeIter.isWord() )
        {
          this.replaceKeyword( mTreeIter.getCurrentBuffer() );
          break;
        }
      }
    }
    
    if( !mTreeIter.isValid() )
    {
      this.writeToOutBuffer( mTreeIter.getCurrentBuffer().charAt(0) );
      this.appendInStreamBuffer( mTreeIter.getCurrentBuffer().substring(1) );
    }

  }

  /**
   * The end of a keyword end tag has been found, so do the replacement, if the key exists.
   * @param key The key that was found
   * @exception IOException
   * @exception KeywordException
   */
  private void replaceKeyword( String key ) throws IOException, KeywordException
  {
    if( mDocKeywordMap.containsKey( key ) ) 
    {
      this.replaceDocKeyword( key );
    } 
    else if( mStrKeywordMap.containsKey( key ) )
    {
      this.replaceStringKeyword( key );
    }
    else
    {
      throw new KeywordException( "Corrupt key data: " + key + " should be a key.");
    }
  }

  /**
   * Do the replacement.
   * @param key The key that was found
   * @exception IOException
   * @exception KeywordException
   */
  public void replaceDocKeyword(String key) throws IOException, KeywordException 
  {
    int readCount = 0;
    byte [] buffer = null;
    InputStream is = null;

    is = (InputStream)(mDocKeywordMap.get( key ) );

    if( is != null )
    {
      readCount = 0;
      buffer = new byte [ DOC_BUFF_SIZE ];

      while( (readCount = is.read( buffer, 0, DOC_BUFF_SIZE )) != -1 )
      {
        writeToOutBuffer( buffer, readCount );
      }

      is.close();
    }
  }
  
  /**
   * Do the replacement.
   * @param key The key that was found
   * @exception IOException
   * @exception KeywordException
   */
  public void replaceStringKeyword(String key) throws IOException, KeywordException 
  {
    String replacer = null;
    String tempVal  = null;

    tempVal = (String)(mStrKeywordMap.get( key ) );
    
    if( tempVal != null && tempVal.length() > 0 )
    {
      replacer = this.processKeywordValue( tempVal );
      appendInStreamBuffer( replacer );
    }
  }

  /**
   * If a keyword replace value starts with "func:" then it means that the actual
   * replace value should be the result of the function described by the right side
   * of func:<funcName>
   * @param val The replace value
   * @return String The actual replace value (may be the same as the original)
   */
  private String processKeywordValue( String val ) 
  {
    String replacer = null;
    String tempFunc = null;
    String arg      = null;
    int argIdx = 0;
    
    IKeywordFunction func = null;

    if( val.startsWith( FUNCTION_PREFIX ) ) 
    {
      tempFunc = val.substring( FUNCTION_PREFIX.length() );
     
      if( (argIdx = tempFunc.indexOf( "(" )) != -1 )
      {
        arg = tempFunc.substring( argIdx + 1, tempFunc.length() - 1 ).trim();
        tempFunc = tempFunc.substring(0, argIdx);    
      }
      
      func = (IKeywordFunction)mFunctionTable.get( tempFunc );
      
      if( func != null )
      {
        if( arg != null && arg.length() > 0 )
        {
          replacer = func.getVal(arg);
        }
        else
        {
          replacer = func.getVal();
        }
      }
      else
      {
        replacer = val;
      }
    }
    else 
    {
      replacer = val;
    }

    return replacer;
  }

  /**
   * We failed keyword validation in the middle of a potential keyword
   * and need to prepend the input stream buffer.  This may be as simple
   * as backing up the pointer and overwriting, but if we've recently
   * dumped and reset the buffer we will need to allocate a new one.
   * @param append The String we need to prepend.
   */
  protected void appendInStreamBuffer(String append) 
  {
    int tempIdx = 0;

    if( mInStreamCount == 0 )
    {
      if( append.length() > INITIAL_BUFF_SIZE )
      {
        mInStreamBuffer = new byte [ append.length() ];
      }
      for( int i = 0; i < append.length(); i++ )
      {
        mInStreamBuffer[ i ] = (byte)append.charAt( i );
        mInStreamCount++;
      }
    }
    else if( append.length() < mInStreamIdx )
    {
      mInStreamIdx = mInStreamIdx - append.length();
      for( int j = mInStreamIdx; tempIdx < append.length(); j++ )
      {
        mInStreamBuffer[ j ] = (byte)append.charAt( tempIdx++ );
      }
    }
    else
    {
      byte [] tempBuff = null;
      int capacityNeeded = mInStreamCount - mInStreamIdx + append.length();

      if( capacityNeeded < INITIAL_BUFF_SIZE )
      {
        tempBuff = new byte [ INITIAL_BUFF_SIZE ];
      }
      else
      {
        tempBuff = new byte [ capacityNeeded ];
      }

      for( int k = 0; k < append.length(); k++ )
      {
        tempBuff[ k ] = (byte)append.charAt( k );
      }

      for( int l = append.length(); l < capacityNeeded; l++ )
      {
        tempBuff[ l ] = mInStreamBuffer[ mInStreamIdx++ ];
      }

      mInStreamBuffer = tempBuff;
      mInStreamCount  = capacityNeeded;
      mInStreamIdx    = 0;
    }
  }

  /**
   * Write a char to the outbuffer
   * @param c The char to write
   * @exception IOException
   */
  protected void writeToOutBuffer( char c ) throws IOException 
  {
    mOutStreamBuffer.write( (int)c );
    if( mOutStreamBuffer.size() > OUT_BUFFER_SIZE ) 
    {
      mOutStreamBuffer.writeTo( mOutStream );
      mOutStreamBuffer.close();
      mOutStreamBuffer = new ByteArrayOutputStream(OUT_BUFFER_SIZE);
    }
  }

  /**
   * Write the contents of a StringBuffer to the outbuffer
   * @param sb The StringBuffer to write
   * @exception IOException
   */
  protected void writeToOutBuffer( StringBuffer sb ) throws IOException 
  {
    this.writeToOutBuffer( sb.toString() );
  }

  /**
   * Write a String to the outbuffer
   * @param str The String to write
   * @exception IOException
   */
  protected void writeToOutBuffer( String str ) throws IOException 
  {
    writeToOutBuffer( str.getBytes() );
  }

  /**
   * Dump a byte array to the outbuffer
   * @param b The byte array to dump
   * @exception IOException
   */
  protected void writeToOutBuffer( byte [] b ) throws IOException 
  {
    writeToOutBuffer( b, b.length );
  }

  /**
   * Dump size bytes from a byte array into the outbuffer, starting from index 0
   * @param b The byte array to dump from
   * @param size The number of bytes to dump
   * @exception some Exception
   */
  protected void writeToOutBuffer( byte [] b, int size ) throws IOException 
  {
    mOutStreamBuffer.write( b, 0, size );
    if( mOutStreamBuffer.size() > OUT_BUFFER_SIZE ) 
    {
      mOutStreamBuffer.writeTo( mOutStream );
      mOutStreamBuffer.close();
      mOutStreamBuffer = new ByteArrayOutputStream(OUT_BUFFER_SIZE);
    }
  }

  public static void main( String [] args ) 
  {
    String startKey       = args[0];
    String endKey         = args[1];
    String templateName   = args[2];
    String insertName     = args[3];
    
    try 
    {
      File template = new File( templateName );
      File insert   = new File( insertName );

      InputStream template_is = new FileInputStream( template );
      InputStream insert_is   = new FileInputStream( insert );
      OutputStream result_os  = new FileOutputStream( "test_result.doc" );

      KeywordReplacer kr = new KeywordReplacer();//startKey, endKey);
      kr.setStringKeyword("MAX", "${JEVNE}mammel${JEVNE}");
      kr.setStringKeyword("JEVNE", "bohnke");
      kr.setStringKeyword("PHONE", "7738448824");
      kr.setStringKeyword("DATE", "func:date" );
      kr.setStringKeyword("hellomy$(MAX${nameismaxma}mmel", "WHATISYOURS");
      kr.setDocumentKeyword( "replaceWithFile", insert_is );
      kr.setKeyStart(startKey);
      kr.setKeyEnd(endKey);
      kr.processStreams( template_is, result_os );
      
      template_is.close();
      result_os.close();
    } 
    catch(Exception e) 
    {
      e.printStackTrace();
      System.out.println("Exception: " + e.toString() );
    }
  }
    
}

