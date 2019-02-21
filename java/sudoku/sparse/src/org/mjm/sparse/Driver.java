package org.mjm.sparse;

public class Driver {
  public static final Value [] VALS = { Value.ONE, Value.TWO, Value.FIVE };
  public static void main( String [] args ) {
    Board B = new Board();
    tryVals( B, VALS );
  }
  
  public static void tryVals( Board b, Value [] vals ) {
    tryValsInner( 0, b, vals);
  }
  
  public static void tryValsInner( int idx, Board b, Value [] vals ) {
    boolean result = false;
    if( b.getNumCellsSet() == 9*vals.length ) {
      System.out.println("Filled!\n" + b);
    } else if( idx >= 0 && idx < 81 ) {
      for( Value v : vals ) {
        //System.out.println( "Trying to set value " + v + " in cell " + idx);
        result = b.setCellValue(idx, v);
        if( result ) {
          //System.out.println("New Board: \n" + b );
          tryValsInner( idx + 1, b, vals );
        }
      }
      
      b.setCellValue(idx, Value.EMPTY);
      tryValsInner( idx + 1, b, vals );
    }
  }
}
