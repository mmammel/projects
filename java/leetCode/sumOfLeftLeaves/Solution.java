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
    public int sumOfLeftLeaves(TreeNode root) {
        int [] sum = new int [1];
        this.sumInner( root, sum, false );
        return sum[0];   
    }
    
    private void sumInner( TreeNode node, int [] sum, boolean capture ) {
        if( node.left == null && node.right == null & capture ) {
            sum[0] += node.val;
        } else {
        
            if( node.left != null ) {
                this.sumInner(node.left, sum, true );
            }
        
            if( node.right != null ) {
                this.sumInner(node.right, sum, false );
            }
        }
    }
}

