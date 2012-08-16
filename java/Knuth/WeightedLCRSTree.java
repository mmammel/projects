import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;

public class WeightedLCRSTree
{
  private String mId;
  private String mName = "";
  private int mValue = -1;
  private WeightedLCRSTree mLeftChild = null;
  private WeightedLCRSTree mRightSibling = null;
  private WeightedLCRSTree mParent = null;

  public WeightedLCRSTree()
  {
    this.mId = UUID.randomUUID().toString();
  }

  public WeightedLCRSTree( String name, int val )
  {
    this();
    this.mName = name;
    this.mValue = val;
  }

  public int getValue()
  {
    return this.mValue;
  }

  public void setValue(int val)
  {
    this.mValue = val;
  }

  public String getName()
  {
    return this.mName;
  }

  public void setName( String name )
  {
    this.mName = name;
  }

  public WeightedLCRSTree getLeftChild()
  {
    return this.mLeftChild;
  }

  public WeightedLCRSTree getRightSibling()
  {
    return this.mRightSibling;
  }

  public WeightedLCRSTree getParent()
  {
    return this.mParent;
  }

  public void setLeftChild( WeightedLCRSTree tree )
  {
    this.mLeftChild = tree;
  }

  public void setRightSibling( WeightedLCRSTree tree )
  {
    this.mRightSibling = tree;
  }

  public void setParent( WeightedLCRSTree tree )
  {
    this.mParent = tree;
  }

  public boolean hasNonParents()
  {
    return (this.mLeftChild != null || this.mRightSibling != null );
  }

  public String toString()
  {
     StringBuilder sb = new StringBuilder();

     sb.append( this.mName ).append("-").append( this.mValue ).append("\t").append("| ");
     sb.append( "LC(" ).append( this.mLeftChild == null ? "x" : this.mLeftChild.getName() ).append(")\t");
     sb.append( "RS(" ).append( this.mRightSibling == null ? "x" : this.mRightSibling.getName() ).append(")\t");
     sb.append( "P(" ).append( this.mParent == null ? "x" : this.mParent.getName() ).append(")\n");
     if( this.mLeftChild != null ) sb.append( this.mLeftChild.toString() );
     if( this.mRightSibling != null ) sb.append( this.mRightSibling.toString() );
     return sb.toString();
  }

  public OptimalSolution findOptimal( WeightedLCRSTree tree, boolean include )
  {
    OptimalSolution retVal = null;
    OptimalSolution temp = null;

    if( include ) {
      OptimalSolution inclusive = new OptimalSolution( tree.getName() + " ", tree.getValue() );
      OptimalSolution exclusive = new OptimalSolution("",0);

      if( tree.getRightSibling() != null ) {
        temp = this.findOptimal( tree.getRightSibling(), true );
        inclusive.absorb( temp );
        exclusive.absorb( temp );
      }

      if( tree.getLeftChild() != null ) {
        inclusive.absorb( this.findOptimal( tree.getLeftChild(), false ) );
        exclusive.absorb( this.findOptimal( tree.getLeftChild(), true ) );
      }

      if( inclusive.value > exclusive.value ) {
        retVal = inclusive;
      }
      else {
        retVal = exclusive;
      }
    }
    else {
      retVal = new OptimalSolution("",0);
      if( tree.getRightSibling() != null ) retVal.absorb( this.findOptimal( tree.getRightSibling(), false ) );
      if( tree.getLeftChild() != null ) retVal.absorb( this.findOptimal( tree.getLeftChild(), true ) );
    }

    return retVal;
  }

  public OptimalSolution memoizedFindOptimal( WeightedLCRSTree tree, boolean include, Map<String,OptimalSolution> memo )
  {
    OptimalSolution retVal = null;
    OptimalSolution temp = null;

    if( include ) {
      if( memo.containsKey( tree.getName()+"_inclusive")) {
        retVal = memo.get(tree.getName()+"_inclusive");
      }
      else {
        OptimalSolution inclusive = new OptimalSolution( tree.getName() + " ", tree.getValue() );
        OptimalSolution exclusive = new OptimalSolution("",0);

        if( tree.getRightSibling() != null ) {
          temp = this.memoizedFindOptimal( tree.getRightSibling(), true, memo );
          inclusive.absorb( temp );
          exclusive.absorb( temp );
        }

        if( tree.getLeftChild() != null ) {
          inclusive.absorb( this.memoizedFindOptimal( tree.getLeftChild(), false, memo ) );
          exclusive.absorb( this.memoizedFindOptimal( tree.getLeftChild(), true, memo ) );
        }

        if( inclusive.value > exclusive.value ) {
          retVal = inclusive;
        }
        else {
          retVal = exclusive;
        }

        memo.put(tree.getName()+"_inclusive",retVal);
      }
    }
    else {
      if( memo.containsKey( tree.getName()+"_exclusive") )
      {
        retVal = memo.get(tree.getName()+"_exclusive");
      }
      else {
        retVal = new OptimalSolution("",0);
        if( tree.getRightSibling() != null ) retVal.absorb( this.memoizedFindOptimal( tree.getRightSibling(), false, memo ) );
        if( tree.getLeftChild() != null ) retVal.absorb( this.memoizedFindOptimal( tree.getLeftChild(), true, memo ) );
        memo.put(tree.getName()+"_exclusive",retVal);
      }
    }

    return retVal;
  }

  public boolean equals( Object o )
  {
    boolean retVal = (o == this);

    if( !retVal && o instanceof WeightedLCRSTree )
    {
      WeightedLCRSTree other = (WeightedLCRSTree)o;
      retVal = other.mId.equals( this.mId );
    }

    return retVal;
  }

  public int hashCode()
  {
    return this.mId.hashCode();
  }

}
