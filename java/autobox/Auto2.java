public class Auto2 {
    public static void main(String[] args) {
        Integer i1 = 1000;
        Integer i2 = 1000;
        if(i1 != i2) {
            System.out.println("i1 and i2 are different objects");
        } else {
            System.out.println("i1 and i2 are the same object");
        }
        if(i1.equals(i2)) {
            System.out.println("i1 and i2 are meaningfully equal");
        }

        Integer i3 = 10;
        i3 = i3.intern();
        Integer i4 = 10;
        if(i3 != i4) {
            System.out.println("i3 and i4 are different objects");
        } else {
            System.out.println("i3 and i4 are the same object");
        }
        if(i3.equals(i4)) {
            System.out.println("i3 and i4 are meaningfully equal");
        }
    }
}

