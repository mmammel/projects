public interface BoardCursor
{
  /**
   * Getters for row and column
   */
  public int row();
  public int col();

  /**
   * Setter for row and column
   */
  public void setPosition( int r, int c );
  
  /**
   * Move to the next position.  Wraps, moves
   * left to right, top to bottom.
   */
  public void next();
  
  /**
   * Returns true if at the last (bottom right) position of the board.
   */
  public boolean last();
  
  /**
   * Move to the next blank position and return true.
   * Wraps, and returns false if there are no empty positions.
   */
  public boolean nextBlank();
  
  /**
   * Move to the next filled position and return true.
   * Wraps, and returns false if there are no filled positions.
   */
  public boolean nextFilled();
  
  public String toString();
  
  /**
   * Get or set the value at the current position
   */
  public int getValue();
  public void setValue( int val );
  
  /**
   * Get the index of the subquare the current position is in
   */
  public int getSubSquareIdx();
  public int [] getSubSquarePosition();
  public int [] getRowPosition();
  public int [] getColumnPosition();
  
  /**
   * Masks that contain bit representations
   * of all of the values in the current row,
   * column, or sub square.  Returns -1 if
   * duplicates exist.
   */
  public int getRowMask();
  public int getColMask();
  public int getSubSquareMask();
  
  /**
   * Gets a mask containing the possible values for a position, or -1 
   * if an invalid state is found
   */
  public int getPossibleValueMask();
}

