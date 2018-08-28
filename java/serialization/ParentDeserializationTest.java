import java.io.*;
public class ParentDeserializationTest {

public static void main(String[] args){
    try {
        System.out.println("Creating...");
        Child c = new Child(1,2);
        System.out.println("c.i="+c.getI());
        System.out.println("c.j="+c.getJ());
        System.out.println("c.field="+c.getField());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        c.field = 10;
        System.out.println("Serializing...");
        oos.writeObject(c);
        oos.flush();
        baos.flush();
        oos.close();
        baos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        System.out.println("Deserializing...");
        Child c1 = (Child)ois.readObject();
        System.out.println("c1.i="+c1.getI());
        System.out.println("c1.j="+c1.getJ());
        System.out.println("c1.field="+c1.getField());
    } catch (IOException ex){
        ex.printStackTrace();
    } catch (ClassNotFoundException ex){
        ex.printStackTrace();
    }
}

public static class Parent implements Serializable{
    protected int field;
    protected int i;

    protected Parent(){
        field = 5;
        System.out.println("Parent::Constructor");
    }
    public int getField() {
        return field;
    }
}

public static class Child extends Parent implements Serializable{
    protected int j;
    public Child(int i, int j){
        this.i = i;
        this.j = j;
        System.out.println("Child::Constructor");
    }
    public int getI() {
        return i;
    }
    public int getJ() {
      return j;
    }
}

}
