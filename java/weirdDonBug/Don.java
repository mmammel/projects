public class Don {
  public static void main( String [] args ) {
    int foo = new Float((Float.parseFloat(args[0]))).intValue();

                if(foo <= 3239){

                System.out.println("NQ");

                } else if (foo >= 3240 && foo <= 4799){

                System.out.println("Q1");

                } else if (foo >= 4800){

                System.out.println("Q2");

                }

  }
}
