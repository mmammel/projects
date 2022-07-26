import java.util.List;
import java.util.ArrayList;

public class Codec {

    private static final int LPAREN = 1001;
    private static final int RPAREN = 1002;
    private static final int COMMA = 1003;
    private static final int NULL = 1004;
    
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if( root == null ) {
            return "";
        } else if( root.left == null && root.right == null ) {
            return ""+root.val;
        } else {
            return ""+root.val+"("+ this.serialize(root.left) + "," + this.serialize(root.right) + ")";
        }
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        TreeNode retVal = null;

        if( data != null && data.length() > 0 ) {
          int [] index = new int [1];
          index[0] = 0;
          List<Integer> tokens = this.tokenize( data );
System.out.println( tokens );
          retVal =  deserializeInner( tokens, index );
        }

        return retVal;
    }

    private TreeNode deserializeInner( List<Integer> tokens, int [] index ) {
      int token = 0;
      TreeNode retVal = null;
      while( index[0] < tokens.size() ) {
        token = tokens.get(index[0]);
        if( token == NULL ) {
          index[0]++;
          return null;
        } else if( token == LPAREN ) {
          index[0]++;
          retVal.left = deserializeInner( tokens, index );
        } else if( token == RPAREN ) {
          index[0]++;
        } else if( token == COMMA ) {
          index[0]++;
          retVal.right = deserializeInner( tokens, index );
          return retVal;
        } else {
          retVal = new TreeNode( token );
          index[0]++;
          if( index[0] >= tokens.size() || tokens.get(index[0]) != LPAREN ) {
            return retVal;
          }
        }
      }

      return retVal;
    }

    private List<Integer> tokenize( String data ) {
      List<Integer> tokens = new ArrayList<Integer>();
      int val = 0;
      boolean readNode = true;
      StringBuilder sb = null;

      for( char c : data.toCharArray() ) {
        switch( c ) {
          case '(' :
            if( readNode ) this.finishNode( sb, tokens );
            tokens.add(LPAREN);
            // we expect a node
            readNode = true;
            sb = null;
            break;
          case ')' :
            if( readNode ) this.finishNode( sb, tokens );
            tokens.add(RPAREN);
            readNode = false;
            break;
          case ',':
            if( readNode ) this.finishNode( sb, tokens );
            tokens.add(COMMA);
            readNode = true;
            sb = null;
            break;
          default:
            if( readNode ) {
              if( sb == null ) sb = new StringBuilder();
              sb.append(c);
            }
            break;
        }
      }

      if( readNode ) this.finishNode( sb, tokens );

      return tokens;
    }

    private void finishNode( StringBuilder sb, List<Integer> tokens ) {
      int val = NULL;
      if( sb != null && sb.length() > 0 ) {
        try {
          val = Integer.parseInt( sb.toString() );
        } catch( NumberFormatException nfe ) {
          val = NULL;
        }
      }

      tokens.add( val );
    }
    
    public static void main( String [] args ) {
      Codec C = new Codec();
      TreeNode t = C.deserialize( args[0] );
      System.out.println( C.serialize(t) );
      
    }
}

