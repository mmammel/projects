import java.util.Comparator;

/*
Note: this comparator imposes orderings that are inconsistent with equals.
*/
public class CellPossibleValCountComparator implements Comparator<Cell>
{
    public int compare( Cell c1, Cell c2 )
    {
      Integer count1 = c1.getPossibleValCount();
      Integer count2 = c2.getPossibleValCount();
      int retVal = count1.compareTo(count2);
      return retVal == 0 ? 1 : retVal;
    }
}