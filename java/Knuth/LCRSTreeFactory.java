import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class LCRSTreeFactory
{
  private static LCRSTreeFactory theInstance = new LCRSTreeFactory();

  private static final String LPAREN = "(";
  private static final String RPAREN = ")";
  private static final String COMMA = ",";

  public static LCRSTreeFactory getInstance()
  {
    return theInstance;
  }

  private LCRSTreeFactory()
  {
  }

  /**
   *  (A(B,C(D,E,F),G(H),I,K),L(M))
   *  (A(B(E(L,M),F(N,O,P),G),C(H,I(Q,R)),D(J(S,T,U),K))
   */
  public LCRSTree buildTreeFromParenString( String parenString )
  {
     ParenStringTokenizer PST = new ParenStringTokenizer();
     List<String> tokens = PST.tokenize( parenString );
     Map<String,Object> context = new HashMap<String,Object>();
     int count = 1;

     for( String tok : tokens )
     {
       context.put( "tokenCount", new Integer(count++) );

       if( tok.equals("(") )
       {
         this.startParen(context);
       }
       else if( tok.equals(")") )
       {
         this.endParen(context);
       }
       else if( tok.equals(",") )
       {
         this.comma(context);
       }
       else
       {
         this.characters(context, tok);
       }
     }

     return (LCRSTree)context.get("latestTree");
  }

  private Map<String,Object> startParen( Map<String,Object> context )
  {
    Stack<LCRSTree> stack = (Stack<LCRSTree>)context.get("treeStack");
    LCRSTree tempTree = null;

    if( stack == null )
    {
      stack = new Stack<LCRSTree>();
      tempTree = new LCRSTree("*");
      stack.push(tempTree);
      context.put("treeStack", stack );
    }
    else
    {
      tempTree = (LCRSTree)context.get("lastTree");
      stack.push( tempTree );
    }

    return context;
  }

  private Map<String,Object> endParen( Map<String,Object> context )
  {
    Stack<LCRSTree> stack = (Stack<LCRSTree>)context.get("treeStack");

    if( stack == null ) throw new RuntimeException("Unexpected ')' at token " + context.get("tokenCount") );
    LCRSTree latest = stack.pop();
    context.put( "latestTree", latest );
    context.put( "lastTree", latest );
    return context;
  }

  private Map<String,Object> comma( Map<String,Object> context )
  {
    return context;
  }

  private Map<String,Object> characters( Map<String,Object> context, String val )
  {
    Stack<LCRSTree> stack = (Stack<LCRSTree>)context.get("treeStack");
    if( stack == null ) throw new RuntimeException("Unexpected characters at token " + context.get("tokenCount") );
    LCRSTree currentTree = stack.peek();
    LCRSTree lastTree = (LCRSTree)context.get("lastTree");
    LCRSTree tree = new LCRSTree(val);
    if( currentTree.getLeftChild() == null )
    {
      currentTree.setLeftChild( tree );
      tree.setParent( currentTree );
    }
    else
    {
      lastTree.setRightSibling( tree );
      tree.setParent( currentTree );
    }

    context.put( "lastTree", tree );
    return context;
  }

  public static void main( String [] args )
  {
    LCRSTree tree = LCRSTreeFactory.getInstance().buildTreeFromParenString( args[0] );

    System.out.println( tree );
  }

}