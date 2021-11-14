class CombinationIterator {

    private int [] pointer = null;
    private char [] chars = null;
    private boolean done = false;
    
    public CombinationIterator(String characters, int combinationLength) {
        this.pointer = new int [combinationLength];
        this.chars = characters.toCharArray();
        
        for( int i = 0; i < combinationLength; i++ ) {
            this.pointer[i] = i;
        }
        
        if( this.pointer.length == this.chars.length ) this.done = true;
    }
    
    public String next() {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < pointer.length; i++ ) {
            sb.append( this.chars[ this.pointer[i] ] );
        }
        
        this.advance();
        
        return sb.toString();
    }
    
    private void advance() {
        int advanced = -1; // which pointer advanced.
        if( !this.done ) {
            for( int i = this.pointer.length - 1; i >= 0; i-- ) {
                if( ((this.chars.length - this.pointer[i]) > (this.pointer.length - i)) &&
                    (i == this.pointer.length - 1 ||
                    this.pointer[i] < this.pointer[i+1] - 1 )) {
                    this.pointer[i]++;
                    advanced = i;
                    break;
                }
            }
            
            if( advanced > -1 ) {
                for( int i = advanced + 1; i < this.pointer.length; i++ ) {
                    this.pointer[i] = this.pointer[i-1] + 1;
                }
            } else {
                this.done = true;
            }
        }
    }
    
    public boolean hasNext() {
        return !this.done;
    }
}

/**
 * Your CombinationIterator object will be instantiated and called as such:
 * CombinationIterator obj = new CombinationIterator(characters, combinationLength);
 * String param_1 = obj.next();
 * boolean param_2 = obj.hasNext();
 */

