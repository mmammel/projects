
  import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;
    import java.util.Random;
    import java.util.HashMap;
    import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: trahinro
 * Date: Oct 26, 2009
 * Time: 3:23:41 PM
 * Discription this was a sample how PMLX will function with the syncronize method.
 */
public class SyncInternStringTest {

        static class ClientUser {
            //name = clientUsername
            private final String name;
            // map = jobs owned by client



            public ClientUser(String name) {
                this.name = name;

            }

            public String getName() {
                return this.name;
            }

            public void postJob( String x, Map map) {
              //System.out.println( "Thread " + this.name + " trying to obtain lock for " + x );
                synchronized(x.intern())
                {
              //System.out.println( "Thread " + this.name + " obtained lock for " + x );

                    String mapKey = x+"";
                    // do clientLookup for job by external and/or publicId
                   // System.out.println("in job "+x+" as "+this.getName());
                   boolean contained = false;

                   for( int i = 0; i < 10; i++ )
                   {
                     contained = map.containsKey(mapKey);
                   }

                    if(contained)
                    {
                        //if it is a duplicated modify information
                        String string = (String)map.get(mapKey);
                        map.put(mapKey,string+", modified by "+this.getName());
                    }else
                    {
                        //otherwise create job
                        map.put(mapKey," Job "+x+"  added by "+this.getName());
                    }

                }
                              //System.out.println( "Thread " + this.name + " released lock for " + x );

//        System.out.println("leave job "+x+" with User "+this.getName());
            }


        }

        static class PostLoop implements Runnable {
            private ClientUser cu;
            private int jobCount;
      private Map map;
            public PostLoop(ClientUser cu, int jobs,Map map) {
                this.cu = cu;
                this.jobCount = jobs;
        this.map=map;
            }
            public void run() {
                Random random = new Random();
                String myString ="";
                for (int i = 0; i<this.jobCount;i++) {
                    myString = i+"";
                    // post job announcement
                   // System.out.println("posting job "+myString+" by "+cu.getName());
                    cu.postJob(myString, map);
                }
                //signal completed posting jobs.
                System.out.println("**************************"+cu.getName() +" finished.");
            }
        }


        public static void main(String[] args) {
            int threadCount = 7;
            int jobs = 1000;
            //final Map map = Collections.synchronizedMap(new HashMap<String,String>());
            //final Map map = new HashMap<String,String>();
            final Map map = new MyMap<String,String>();
            final ClientUser[] users= new ClientUser[threadCount];
            for (int i=0;i<threadCount;i++)
            {
                users[i] = new ClientUser("a"+i);
//                System.out.println("clientUser created "+users[i].getName());
            }
            Thread[] myThreads = new Thread[threadCount];
            for (int i=0;i<threadCount;i++)
            {
                    myThreads[i] = new Thread(new PostLoop(users[i],jobs,map));

            }
            for( int j = 0; j < threadCount ; j++ )
            {
                 myThreads[j].start();

            }

            for( int j = 0; j < threadCount ; j++ )
            {
                 try
                 {
                   myThreads[j].join();
                 }
                 catch( InterruptedException ie )
                 {
                   System.out.println( "InterruptedThread!! " + ie.toString() );
                 }
            }
      int nullCount = 0;
      int perfectCount = 0;
                for (int x = 0;x<map.values().size() ;x++ )
                {
          if (map.get(x+"")==null)
          {
            nullCount++;
          }else
          {
            String myTest = (String)map.get(x+"");
            if (myTest.contains("a1")&&
              myTest.contains("a2")&&
              myTest.contains("a3")&&
              myTest.contains("a4")&&
              myTest.contains("a5")&&
              myTest.contains("a6"))
            {
              perfectCount++;
              System.out.println(x+") Value = "+map.get(x+""));
            }
          }
                }
      System.out.println("null count= "+nullCount);
      System.out.println("perfect count= "+perfectCount);
        }

        static class MyMap<K,V> extends HashMap<K,V>
        {
          public boolean containsKey(Object key) {
            boolean retVal = false;
            System.out.println( "Calling contains " + key );
            retVal = super.containsKey(key);
            //System.out.println( "Done calling contains " + key + ", " + retVal );
            return retVal;
          }
        }

    }





