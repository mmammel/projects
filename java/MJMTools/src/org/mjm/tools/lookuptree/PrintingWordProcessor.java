package org.mjm.tools.lookuptree;

public class PrintingWordProcessor implements WordProcessor
{
  public void processWord( String word )
  {
    System.out.println( word );
  }
}
