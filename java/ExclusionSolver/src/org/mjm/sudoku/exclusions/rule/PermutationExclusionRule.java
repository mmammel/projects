package org.mjm.sudoku.exclusions.rule;

import org.mjm.sudoku.*;

import java.util.EnumSet;

/**
  Permutation Exclusion rule: (from sudoku dragon):
  
  The [ExcludeNRule is an example of] of a general logical property of Sudoku puzzles. 
  To follow this you may need to look at the theory of permutations. 
  Each group is just a permutation  of the numbers 1 to 9 (for a 9x9 grid). 
  If you can identify a group within this permutation that is restricted to 
  the same number of squares then you have a Sudoku permutation rule. 
  [Note: In fact the 'only square'; 'single possibility' and 'only choice' 
  are just special cases of this general rule - the subset size is one in this case.] 
  There are more exotic situations for application of this general rule.

  The twin, triplet, quadruplet rules are stated in terms of the size of the 
  sub-group (2,3,4...) [This is the excludeN rule] but a chain is also possible. 
  A chain can take in any number of squares, for example if the first three 
  squares in a group allow possibilities {1,7}; {4,7} and {1,4} we have a 
  closed chain group of three symbols {1,4,7} this is not a twin or a triplet 
  but the general permutation rule means that if you can spot it then 1, 4 
  and 7 elsewhere in the same group can be safely excluded as possibilities.
 */
public class PermutationExclusionRule implements Rule
{   
    public boolean runRule( Board board )
    {
      boolean changesMade = false, changeTracker = false;

      for( Row row : board.getRows() )
      {
        changeTracker = this.runRuleInner(row);

        if( changeTracker ) {
          System.out.println( "Set permutation rule success on row " + row.getId() + ":\n" + board );
        }

        changesMade = changesMade | changeTracker;
      }

      //if( changesMade ) continue;

      for( Column column : board.getColumns() )
      {
        changeTracker = this.runRuleInner(column);

        if( changeTracker ) {
           System.out.println( "Set permutation rule success on column " + column.getId() + ":\n" + board );
        }
        changesMade = changesMade | changeTracker;
      }

      //if( changesMade ) continue;


      for( Box box : board.getBoxes() )
      {
        changeTracker = this.runRuleInner(box);

        if( changeTracker ) {
          System.out.println( "Set permutation rule success on box " + box.getId() + ":\n" + board );
        }
        changesMade = changesMade | changeTracker;
      }

      return changesMade;
  }    
  
  private boolean runRuleInner( CellCollection cells )
  {
    /*
      check 3 cells, then 4, thru 8.  Checking 1 is obviously unnecessary,
      as is checking 2: either each cell would contain the same two 
      possible values and therefore be covered by the Exclude N rule for N=2,
      or each cell would only have 1 possiblity and be covered by the 
      LonePossibility rule.
      Checking 9 cells is obviously nonsensical.
    */
    Cell [] possibleValOrderedCells = cells.getPossibleValCountOrderedCells();
    Cell [] allButArray = null;
    EnumSet<Value> uniqueVals = EnumSet.noneOf(Value.class);
    int totalPossibleValCount = 0;
    Cell tempCell = null;
    boolean retVal = false;
    
    uniqueVals.addAll( possibleValOrderedCells[0].getPossibleVals() );
    uniqueVals.addAll( possibleValOrderedCells[1].getPossibleVals() );
    
    for( int i = 2; i < 8; i++ )
    {
      tempCell = possibleValOrderedCells[i];
      uniqueVals.addAll( tempCell.getPossibleVals() );
      if( tempCell.getPossibleValCount() <= i && tempCell.getPossibleValCount() > 1 && uniqueVals.size() == (i+1) )
      {
        // We can exclude the values in the cells up to this point from the rest of the cells!
        allButArray = new Cell [i+1];
        for( int j = 0; j < allButArray.length; j++ )
        {
          allButArray[j] = possibleValOrderedCells[j];
          retVal = retVal | cells.exclude( uniqueVals.toArray( new Value[0]), allButArray );
        }
      }
    }
    
    return retVal;
  }
}
