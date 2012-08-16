package org.mjm.tools.docbuilder;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * @author mmammel-tw
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DocumentBuilder
{

  private byte [] streamBuffer = null;
  private static final int BUFFER_LENGTH = 32768;

  public DocumentBuilder()
  {
    streamBuffer = new byte [ BUFFER_LENGTH ];
  }

  /**
   * Given a List of InputStreams, stream all of them into the given 
   * OutputStream in order.
   * @param inputStreams The List of InputStreams
   * @param result The OutputStream to send all of the results to
   * @throws SyncEngineException
   */
  public void buildDocument( List inputStreams, OutputStream result )
     throws DocumentBuilderException
  {
    Iterator streamIterator     = null;
    InputStream tempInputStream = null;
    Object tempObj              = null;

    if( result != null )
    {
      if( inputStreams != null )
      {
        streamIterator = inputStreams.iterator();
        
        while( streamIterator.hasNext() )
        {
          tempObj = streamIterator.next();

          if( (tempObj != null) && (tempObj instanceof InputStream) )
          {
            tempInputStream = (InputStream)tempObj; 
            this.writeToOutputStream( tempInputStream, result );
          }
        }
      }  
    }
    else
    {
      String errMsg = "OutputStream given was null, unable to build the result";
      throw new DocumentBuilderException( errMsg );
    }
  }

  private void writeToOutputStream( InputStream is, OutputStream os )
      throws DocumentBuilderException
  {
    int readCount = 0; 
    try
    {
      while( (readCount = is.read( streamBuffer, 0, BUFFER_LENGTH )) != -1 )
      {
        os.write( streamBuffer, 0, readCount );
      }
      os.flush();
    }
    catch( IOException e )
    {
      throw new DocumentBuilderException( "Failure while writing to the output stream", e );
    }
  }

}
