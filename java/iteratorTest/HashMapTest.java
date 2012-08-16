package test;

import java.util.Hashtable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class HashMapTest {

    static
    {
      // TODO: This should be done in a base calss common with all types of caches, not just the sequence cache.
     // CacheManager.create("C:\\Documents and Settings\\max_mammel\\My Documents\\projects\\java\\sequenceCache\\ehcache.xml");
        CacheManager.create("/home/lwoessne/dev/ehcache-1.3.0-beta3/ehcache.xml");
    }
    
    public static void main(String[] args) {

        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.addCache("LeoCache" );        
        Cache cache = cacheManager.getCache("LeoCache");

        //long roundingNumber = (long)(Math.pow(10, SequenceKey.roundingDigits ));
        long seed = 1074723300;
        for (int i = 0; i < 10; i++ ) {

            String stuff = "stuff" + i;

            SequenceKey key = new SequenceKey(seed,(long)(seed + SequenceKey.bucketSize *i ),  "simulator",
                    "10.128.24.84");
            
            System.out.println("key.hashCode()=" + key.hashCode());
            
            cache.put(new Element(key, stuff));
        }
        
        for (int x = 0; x < 10; x++) {
            SequenceKey key = new SequenceKey( x, seed + SequenceKey.bucketSize + x*1000000 ,
                    "simulator", "10.128.24.84");
            Element found = cache.get(key);

            System.out.println( "seraching for " + key);
            if (found != null) {
                String foundStuff = (String) found.getObjectValue();

                System.out.println("foundStuff=" + foundStuff);
            }else{
                                
                String stuff = "new stuff" + x;
                cache.put(new Element(key, stuff));
            }
        }
    }
}

class SequenceKey {

    long startTime;
    long endTime;
    
    /** 
     * roundedEndTime is used to decide what sequence the 
     * endTime belongs to.  For example
     * endTime 1000 to 1999 will round to 1000
     * this allows all Slices with the same
     * roundedEndTime to exist in the same sequence
     */    
    //long roundedEndTime;    
    
    /**
     * number of digits to set to zeros 
     * to round the endTime to 
     * get the roundedEndTime
     */
    //static final int roundingDigits = 7;
    String jvmID;
    String hostName;
    final long bucketNumber;
    static final int bucketSize = 100;
    

    public SequenceKey(long startTime, long endTime, String jvmID,
            String hostName) {

//        this.startTime = startTime;
//        this.endTime = endTime;
        this.jvmID = jvmID;
        this.hostName = hostName;
//        
//        int roundingNumber = (int) Math.pow(10, roundingDigits);
//        this.roundedEndTime = ((endTime /  roundingNumber) * roundingNumber);
        
        //this.endTime = endTime;
        //this.name = name;
        
        // add one to never allow bucketNumber 0
        this.bucketNumber = endTime/bucketSize + 1;
        this.endTime = (bucketNumber * (bucketSize + 1) ) -1;
        this.startTime = bucketNumber * bucketSize;

    }

    public boolean equals(Object aThat) {
        // check for self-comparison
        if (this == aThat)
            return true;

        // use instanceof instead of getClass here for two reasons
        // 1. if need be, it can match any supertype, and not just one
        // class;
        // 2. it renders an explict check for "that == null" redundant,
        // since
        // it does the check for null already - "null instanceof [type]"
        // always
        // returns false. (See Effective Java by Joshua Bloch.)
        if (!(aThat instanceof SequenceKey)) {
            return false;
        }
        // Alternative to the above line :
        // if ( aThat == null || aThat.getClass() != this.getClass() )
        // return false;

        // cast to native object is now safe
        SequenceKey that = (SequenceKey) aThat;

        // now a proper field-by-field evaluation can be made
        // TODO this needs to be if the cache has this.has.. or more
//        return (this.roundedEndTime == that.roundedEndTime                
//                && this.jvmID.equals(that.jvmID) 
//                && this.hostName.equals(that.hostName));
        return (this.bucketNumber == that.bucketNumber                
                && this.jvmID.equals(that.jvmID) 
                && this.hostName.equals(that.hostName));
    }

    public int hashCode() {
        StringBuffer hashBuf = new StringBuffer();
        hashBuf.append(jvmID);
        hashBuf.append(hostName);        

        System.out.println("endTime=" + endTime);
        //System.out.println("roundedEndTime:" + roundedEndTime);
        
        //hashBuf.append(roundedEndTime);
        System.out.println("bucketNumber=" + bucketNumber);
        hashBuf.append(bucketNumber);

        return hashBuf.toString().hashCode();

    }

    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append(this.startTime);
        buf.append(", ");
        buf.append(this.endTime);
        buf.append(", ");
        buf.append(this.jvmID);
        buf.append(", ");
        buf.append(this.hostName);

        return buf.toString();
    }

}
