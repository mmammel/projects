import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class LCRSTree
{
  private String mId;
  private String mValue = "";
  private LCRSTree mLeftChild = null;
  private LCRSTree mRightSibling = null;
  private LCRSTree mParent = null;

  public LCRSTree()
  {
    this.mId = UUID.randomUUID().toString();
  }

  public LCRSTree( String val )
  {
    this();
    this.mValue = val;
  }

  public String getValue()
  {
    return this.mValue;
  }

  public void setValue(String val)
  {
    this.mValue = val;
  }

  public LCRSTree getLeftChild()
  {
    return this.mLeftChild;
  }

  public LCRSTree getRightSibling()
  {
    return this.mRightSibling;
  }

  public LCRSTree getParent()
  {
    return this.mParent;
  }

  public void setLeftChild( LCRSTree tree )
  {
    this.mLeftChild = tree;
  }

  public void setRightSibling( LCRSTree tree )
  {
    this.mRightSibling = tree;
  }

  public void setParent( LCRSTree tree )
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

     sb.append( this.mValue ).append("\t").append("| ");
     sb.append( "LC(" ).append( this.mLeftChild == null ? "x" : this.mLeftChild.getValue() ).append(")\t");
     sb.append( "RS(" ).append( this.mRightSibling == null ? "x" : this.mRightSibling.getValue() ).append(")\t");
     sb.append( "P(" ).append( this.mParent == null ? "x" : this.mParent.getValue() ).append(")\n");
     if( this.mLeftChild != null ) sb.append( this.mLeftChild.toString() );
     if( this.mRightSibling != null ) sb.append( this.mRightSibling.toString() );
     return sb.toString();
  }

  public boolean equals( Object o )
  {
    boolean retVal = (o == this);

    if( !retVal && o instanceof LCRSTree )
    {
      LCRSTree other = (LCRSTree)o;
      retVal = other.mId.equals( this.mId );
    }

    return retVal;
  }

  public int hashCode()
  {
    return this.mId.hashCode();
  }

}
