/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int sumNumbers(TreeNode root) {
        int [] total = new int [1];
        total[0] = 0;
        this.sumInner( root, total, 0);
        return total[0];
    }
    
    public void sumInner( TreeNode node, int [] total, int currNum ) {
        TreeNode l, r;
        l = node.left;
        r = node.right;
        if( l == null && r == null ) {
            // it's a leaf, contribute to total.
            total[0] += (currNum + node.val);
        } else {
            // it's not a leaf, keep going.
            currNum = currNum + node.val;
            if( l != null ) {
                sumInner(l, total, currNum * 10);
            }
            
            if( r != null ) {
                sumInner(r, total, currNum * 10);
            }
        }
    }
}

