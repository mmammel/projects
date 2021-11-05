import java.math.BigInteger;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode curr = null;
        ListNode next = null;
        BigInteger b1 = this.numberFromNode(l1);
        BigInteger b2 = this.numberFromNode(l2);
        BigInteger sum = b1.add(b2);
        String sumStr = sum.toString();
        
        curr = next = new ListNode( new Integer( ""+sumStr.charAt(0) ) );
        for( int i = 1; i < sumStr.length(); i++ ) {
            curr = new ListNode( new Integer(""+sumStr.charAt(i) ), next );
            next = curr;
        }
        
        return curr;
    }
    
    public BigInteger numberFromNode( ListNode n ) {
        ListNode next = n;
        StringBuilder sb = new StringBuilder();
        while( next != null ) {
            sb.insert(0,""+next.val);
            next = next.next;
        }
        
        return new BigInteger( sb.toString());
    }
}
