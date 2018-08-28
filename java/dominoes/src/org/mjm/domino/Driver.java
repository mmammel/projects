package org.mjm.domino;

import java.io.IOException;

public class Driver {
  public static void main( String [] args ) {
    if( args.length == 1) {
      String filename = args[0];
      try {
        Board b = new Board(filename);
        System.out.println( b );
        b.play();
      } catch( IOException ioe ) {
        System.out.println( "Exception initializing board: " + ioe.toString());
      }
    } else {
      System.out.println( "Usage: java Driver <filename>");
    }
  }
}
