class Solution {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String,Set<String>> uniqueAcctToEmail = new HashMap<String,Set<String>>();
        Map<String,String> emailToUniqueAcct = new HashMap<String,String>();
        
        int count = 0;
        String acct = null, unqAcct = null, tempAcct = null;
        Set<String> tempSet = null, toMerge = new HashSet<String>();
        String merge = null;
        for( List<String> account : accounts ) {
            acct = account.get(0);
            unqAcct = acct + ("_" + count++);
            if( (tempSet = uniqueAcctToEmail.get(unqAcct)) == null ) {
                tempSet = new TreeSet<String>();
                uniqueAcctToEmail.put( unqAcct, tempSet );
            }
                        
            toMerge.clear();
            
            for( int i = 1; i < account.size(); i++ ) {
                tempSet.add(account.get(i));

                if( (tempAcct = emailToUniqueAcct.get(account.get(i)) ) != null && !tempAcct.equals(unqAcct) ) {
                    toMerge.add(tempAcct);
                } else {
                  emailToUniqueAcct.put( account.get(i), unqAcct );
                }
            }
            
            if( toMerge.size() > 0 ) {
                // merge all of the found accounts, re-index the emails to the merged 
                for( String m : toMerge ) {
                    tempSet = uniqueAcctToEmail.get(m);
                    uniqueAcctToEmail.get(unqAcct).addAll(tempSet);
                    uniqueAcctToEmail.remove(m);
                    for( String e : tempSet ) {
                        emailToUniqueAcct.put( e, unqAcct );
                    }
                }
            }
        }
        
        // now build the final.
        List<List<String>> retVal = new ArrayList<List<String>>();
        List<String> tempList = null;
        for( String k : uniqueAcctToEmail.keySet() ) {
            tempList = new ArrayList<String>();
            tempList.add(k.replaceAll("_.*?$", ""));
            tempList.addAll( uniqueAcctToEmail.get(k));
            retVal.add(tempList);
        }
        
        return retVal;
    }
}

