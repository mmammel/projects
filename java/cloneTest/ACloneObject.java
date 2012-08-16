
public class ACloneObject extends BaseCloneObject
{
  private String A_Attribute = null;
  private int mSerialNum = -1;

  public ACloneObject(){ }

  public ACloneObject(String n, int serial)
  {
    mName = n;
    mSerialNum = serial;
    A_Attribute = "A_"+n;
  }

  public String getName()
  {
    return mName + "_" + A_Attribute+"["+mSerialNum+"]";
  }

  public ICloneObject getCopy()
  {
    ICloneObject copy = new ACloneObject( "ACOPY_" + mName, mSerialNum );
    ICloneObject tempParent = null;

    if( mParent != null )
    {
      tempParent = this.getParent().getCopy();
      tempParent.addChild(copy);
      copy.setParent( tempParent );
    }

    return copy;
  }
}
