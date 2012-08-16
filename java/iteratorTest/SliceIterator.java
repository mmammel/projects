package test;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class SliceIterator implements Iterator {

    Hashtable cache = new Hashtable();
    Hashtable database = new Hashtable();

    private long currentStartTime = -1;
    private long currentEndTime = -1;
    private long startTime = 10;
    private long endTime = 1999;
    private int aggInterval = 10;

    boolean inCache = false;
    boolean inDatabase = false;

    Iterator cacheSequenceIter;
    Iterator databaseSequenceIter;

    SliceIterator() {
        for (int x = 1; x < 50; x += aggInterval) {
            CacheItem cacheItem = new CacheItem();
            cacheItem.startTime = x-aggInterval;
            cacheItem.endTime = x;
            cacheItem.name = "in cache";            
            
            SequenceKey key = new SequenceKey(cacheItem.startTime, cacheItem.endTime, "simulator", "host1");
            List cacheItems = (List) cache.get(key);

            if (cacheItems == null) {
                cacheItems = new Vector();
                cacheItems.add(cacheItem);
                cache.put(key, cacheItems);
            } else {
                cacheItems.add(cacheItem);
            }

        }
        
        CacheItem cacheItem = new CacheItem();
        cacheItem.startTime = 51;
        cacheItem.endTime = 99;
        cacheItem.isMissingTime = true;
        cacheItem.name = "in cache";            
        
        SequenceKey key = new SequenceKey(cacheItem.startTime, cacheItem.endTime, "simulator", "host1");
        List cacheItems = (List) cache.get(key);

        if (cacheItems == null) {
            cacheItems = new Vector();
            cacheItems.add(cacheItem);
            cache.put(key, cacheItems);
        } else {
            cacheItems.add(cacheItem);
        }

        for (int x = 6; x < 200; x += aggInterval) {
            cacheItem = new CacheItem();
            cacheItem.startTime = x-aggInterval;
            cacheItem.endTime = x;
            cacheItem.name = "in database";

            key = new SequenceKey(cacheItem.startTime, cacheItem.endTime, "simulator", "host1");

            cacheItems = (List) database.get(key);
            if (cacheItems == null) {
                cacheItems = new Vector();
                cacheItems.add(cacheItem);
                database.put(key, cacheItems);
            } else {
                cacheItems.add(cacheItem);
            }
        }

        for (int x = 200; x < 300; x += aggInterval) {
            cacheItem = new CacheItem();
            cacheItem.startTime = x-aggInterval;
            cacheItem.endTime = x;
            cacheItem.name = "in cache";

            key = new SequenceKey(cacheItem.startTime, cacheItem.endTime, "simulator", "host1");
            cacheItems = (List) cache.get(key);

            if (cacheItems == null) {
                cacheItems = new Vector();
                cacheItems.add(cacheItem);
                cache.put(key, cacheItems);
            } else {
                cacheItems.add(cacheItem);
            }
        }

        // This is the mock query
        startTime = 10;
        endTime = 250;
        
        currentStartTime = startTime;
        
    }

    public boolean hasNext() {
        System.out.println("currentStartTime= "+ currentStartTime);
        
        if (currentStartTime >= endTime) {
            return false;
        }

        if (inCache) {
            if (cacheSequenceIter != null && cacheSequenceIter.hasNext()) {
                return true;
            } else {
                inCache = false;
            }
        }

        if (inDatabase) {
            if (databaseSequenceIter != null && databaseSequenceIter.hasNext()) {
                return true;
            } else {
                inDatabase = false;
            }
        }

        SequenceKey key = new SequenceKey(currentStartTime, endTime,
                "simulator", "host1");
        List sequence = (List) cache.get(key);
        if (sequence != null) {
            return true;
        }

        // TODO add logic to find hole in cache and query database

        sequence = (List) database.get(key);
        if (sequence != null) {
            return true;
        }

        return false;
    }

    public Object next() {
        
        Query query = new Query();
        query.startTime = currentStartTime;
        query.endTime = endTime;
        
        query.hostName = "host1";
        query.jvmID = "simulator";

        if (inCache) {
            CacheItem item = processCache(query);
           
            if( item != null){
                return item;
            }
            
        }

        if (inDatabase) {
            if (databaseSequenceIter != null && databaseSequenceIter.hasNext()) {
                CacheItem item = (CacheItem) databaseSequenceIter.next();
                currentStartTime = item.endTime + 1;
                return item;
            } else {
                inDatabase = false;
                databaseSequenceIter = null;
            }
        }

        

        CacheItem item = queryCache(query);
        if (item != null) {
            return item;
        }

        item = queryDatabase(query);
        if (item != null) {
            return item;
        }

        return null;
    }

    public void remove() {
        // TODO Auto-generated method stub

    }

    private CacheItem processCache( Query query) {
        if (cacheSequenceIter != null && cacheSequenceIter.hasNext()) {
            CacheItem item = (CacheItem) cacheSequenceIter.next();
            while (item.isMissingTime) {
               
                //currentTime = item.startTime - 1;
                SequenceKey key = new SequenceKey(item.startTime, item.endTime,
                        "simulator", "host1");
                item = queryDatabase(query);
                if (item != null) {
                   
                    return item;
                   
                }
                // query next item from the cache;
                if( cacheSequenceIter.hasNext()){
                    item = (CacheItem) cacheSequenceIter.next();
                } else {
                    inCache = false;
                    cacheSequenceIter = null;
                    return null;
                }
            }
            currentStartTime = item.endTime + 1;
            return item;
        } else {
            inCache = false;
            cacheSequenceIter = null;
        }

        return null;
    }

    private CacheItem queryCache(Query query) {
        
        SequenceKey key = new SequenceKey(query.startTime , query.startTime,
                query.jvmID, query.hostName);
        
        List sequence = (List) cache.get(key);
        if (sequence != null) {
            cacheSequenceIter = sequence.iterator();
            while (cacheSequenceIter.hasNext()) {
                CacheItem item = (CacheItem) cacheSequenceIter.next();
                if (item.endTime <= query.endTime && item.endTime >= query.startTime) {
                    currentStartTime = item.endTime + 1;
                    inCache = true;
                    return item;
                }
                
                if( item.endTime > key.endTime){
                    break;
                }
            }

            cacheSequenceIter = null;
        }

        return null;
    }

    private CacheItem queryDatabase(Query query) {

        // TODO add logic to find hole in cache and query database

        SequenceKey key = new SequenceKey(query.startTime , query.startTime,
                query.jvmID, query.hostName);
        
        List sequence = (List) database.get(key);
        if (sequence != null) {
            databaseSequenceIter = sequence.iterator();
            while (databaseSequenceIter.hasNext()) {
                CacheItem item = (CacheItem) databaseSequenceIter.next();
                if (item.endTime <= query.endTime && item.endTime >= query.startTime) {
                    currentStartTime = item.endTime + 1;
                    inDatabase = true;
                    return item;
                }
                
                if( item.endTime > key.endTime){
                    break;
                }
            }

            databaseSequenceIter = null;
            
        }

        return null;
    }

    public static void main(String[] args) {
        SliceIterator iter = new SliceIterator();

        while (iter.hasNext()) {
            CacheItem item = (CacheItem) iter.next();
            if( item == null){
                System.out.println("end of items");
                break;
            }
            System.out.println("item=" + item.name + ":" + item.endTime);
        }
    }
}

class CacheItem {

    String name;
    long startTime;
    long endTime;
    boolean isMissingTime;

}

class Query{
    
    long startTime;
    long endTime;
    
    String hostName;
    String jvmID;
}
