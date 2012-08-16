public class Driver
{
  public static void main( String [] args )
  {
    Driver drv = new Driver();

    ICloneObject root = new ACloneObject("da_rooti", 100);

    ICloneObject kid1 = new BCloneObject("kid1", 1000);

    root.addChild(kid1);

    ICloneObject kid2 = new BCloneObject("kid2", 2000);

    root.addChild(kid2);

    ICloneObject gkid1 = new CCloneObject("gkid1", 10000);

    kid1.addChild(gkid1);
    
    ICloneObject gkid2 = new CCloneObject("gkid2", 20000);

    kid2.addChild(gkid2);

    System.out.println("\n---------");
    drv.printTree(root, "");

    ICloneObject copy = gkid2.getCopy();

    kid1.addChild( copy );

    System.out.println("\n---------");
    drv.printTree(root, "");
    
  }

  public void printTree( ICloneObject obj, String indent )
  {
    System.out.println( indent + obj.getName() + "[ " + ((obj.getParent() == null) ? "NO_PARENT" : obj.getParent().getName()) + " ]" );
    
    for( int i = 0; i < obj.getChildCount(); i++ )
    {
      this.printTree( obj.getChild(i), indent + " " );
    }
  }
}
