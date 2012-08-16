package org.mjm.tools.docbuilder;

public class DocumentBuilderException extends Exception
{
  protected Throwable mException = null;
  protected static final String DEFAULT_MSG = "DocBuilder Exception";

  public DocumentBuilderException(String msg, Throwable exception) 
  {
    super(msg);
    mException = exception;
  }

  /**
   * Creates a new instance of <code>DocumentBuilderException</code> without detail message.
   */
  public DocumentBuilderException() 
  {
    this(DEFAULT_MSG, null);
  }

  /**
   * Constructs an instance of <code>DocumentBuilderException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public DocumentBuilderException(String msg) 
  {
    this(msg, null);
  }

  public DocumentBuilderException(Exception e) 
  {
    this(e.toString(), null);
  }

  public DocumentBuilderException(Throwable th) 
  {
    this(DEFAULT_MSG, th);
  }

  public String getMessage() 
  {
    StringBuffer sb = new StringBuffer(super.getMessage());

    if (mException != null && isValidMsg(mException.getMessage()) ) 
    {
      if (sb.length() > 0) 
      {
        sb.append("\nUnderlying cause: ");
      }
      sb.append(mException.getMessage());
    }
    return sb.toString();
  }

  public String toString() 
  {
    StringBuffer sb = new StringBuffer(super.toString());

    if (mException != null && isValidMsg(mException.toString()) ) 
    {
      if (sb.length() > 0) 
      {
        sb.append("\nUnderlying cause: ");
      }
      sb.append(mException.toString());
    }
    
    return sb.toString();
  }

  public String getLocalizedMessage() 
  {
    StringBuffer sb = new StringBuffer(super.getLocalizedMessage());

    if (mException != null && isValidMsg( mException.getLocalizedMessage() ) ) 
    {
      if (sb.length() > 0) 
      {
        sb.append("\nUnderlying cause: ");
      }
      sb.append(mException.getLocalizedMessage());
    }
    
    return sb.toString();
  }

  public void printStackTrace() 
  {
    if (mException != null) 
    {
      mException.printStackTrace();
    }
    else
    {
      super.printStackTrace();
    }
  }

  public void printStackTrace(java.io.PrintWriter pw) 
  {
    if (mException != null) 
    {
      mException.printStackTrace(pw);
    }
    else
    {
      super.printStackTrace(pw);
    }
  }

  public void printStackTrace(java.io.PrintStream ps) 
  {
    if (mException != null) 
    {
      mException.printStackTrace(ps);
    }
    else
    {
      super.printStackTrace(ps);
    }
  }

  private boolean isValidMsg( String msg )
  {
    return ( (msg != null) && (msg.length() > 0) );
  }
  
}
