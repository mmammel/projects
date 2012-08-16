import java.util.List;
import java.util.ArrayList;

public class KTree
{
  private String mValue;
  private List<KTree> mSubTrees;

  public KTree()
  {
  }

  public KTree( String val )
  {
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

  public List<KTree> getSubTrees()
  {
    return this.mSubTrees;
  }

  public void setSubTrees( List<KTree> trees )
  {
    this.mSubTrees = trees;
  }

  public void addSubTree( KTree tree )
  {
    if( this.mSubTrees == null )
    {
      this.mSubTrees = new ArrayList<KTree>();
    }

    this.mSubTrees.add( tree );
  }
  
  public boolean hasSubTrees()
  {
    return (this.mSubTrees != null && this.mSubTrees.size() > 0);
  }
  
  public String printPreOrder()
  {
    return this.innerPrintPreOrder( new StringBuffer(), this ).toString();
  }
  
  private StringBuffer innerPrintPreOrder(StringBuffer buff, KTree tree)
  {
    buff.append( tree.getValue() );
    
    if( tree.hasSubTrees() )
    {
      buff.append("(");
      boolean first = true;
      for( KTree subTree : tree.getSubTrees() )
      {
        if( !first ) buff.append(",");
        else first = false;
        
        buff.append( this.innerPrintPreOrder( new StringBuffer(), subTree ) );
      }
      buff.append(")");
    }
    
    return buff;
  }
  
  public String printPostOrder()
  {
    return this.innerPrintPostOrder( new StringBuffer(), this ).toString();
  }
  
  private StringBuffer innerPrintPostOrder(StringBuffer buff, KTree tree)
  {    
    if( tree.hasSubTrees() )
    {
      buff.append("(");
      boolean first = true;
      for( KTree subTree : tree.getSubTrees() )
      {
        if( !first ) buff.append(",");
        else first = false;
        
        buff.append( this.innerPrintPostOrder( new StringBuffer(), subTree ) );
      }
      buff.append(")");
    }
    
    buff.append( tree.getValue() );
    
    return buff;
  }

}
