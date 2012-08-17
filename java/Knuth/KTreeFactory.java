import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class KTreeFactory
{
  private static KTreeFactory theInstance = new KTreeFactory();
  
  private static final String LPAREN = "(";
  private static final String RPAREN = ")";
  private static final String COMMA = ",";

  public static KTreeFactory getInstance()
  {
    return theInstance;
  }

  private KTreeFactory()
  {
  }

  /**
   *  (A(B,C(D,E,F),G(H),I,K),L(M))
   */
  public KTree buildTreeFromParenString( String parenString )
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
     
     return (KTree)context.get("latestTree");
  }
  
  private Map<String,Object> startParen( Map<String,Object> context )
  {
    Stack<KTree> stack = (Stack<KTree>)context.get("treeStack");
    KTree tempTree = null;
    String currentTag = null;
    
    if( stack == null )
    {
      stack = new Stack<KTree>();
      tempTree = new KTree("");
      stack.push(tempTree);
      context.put("treeStack", stack );
    }
    else
    {
      tempTree = (KTree)context.get("lastTree");
      stack.push( tempTree );
    }
    
    return context;
  }

  private Map<String,Object> endParen( Map<String,Object> context )
  {
    Stack<KTree> stack = (Stack<KTree>)context.get("treeStack");
    
    if( stack == null ) throw new RuntimeException("Unexpected ')' at token " + context.get("tokenCount") );
    
    context.put( "latestTree", stack.pop() );
    return context;
  }
  
  private Map<String,Object> comma( Map<String,Object> context )
  {
    return context;
  }

  private Map<String,Object> characters( Map<String,Object> context, String val )
  {
    Stack<KTree> stack = (Stack<KTree>)context.get("treeStack");
    if( stack == null ) throw new RuntimeException("Unexpected characters at token " + context.get("tokenCount") );
    KTree currentTree = stack.peek();
    KTree tree = new KTree(val);
    currentTree.addSubTree(tree);
    context.put( "lastTree", tree );
    return context;
  }

  public static void main( String [] args )
  {
    KTree tree = KTreeFactory.getInstance().buildTreeFromParenString( args[0] );
    System.out.println( tree.printPreOrder() );
    System.out.println( tree.printPostOrder() );
  }

}

