import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class WeightedLCRSTreeFactory
{
  private static WeightedLCRSTreeFactory theInstance = new WeightedLCRSTreeFactory();

  private static final String LPAREN = "(";
  private static final String RPAREN = ")";
  private static final String COMMA = ",";

  public static WeightedLCRSTreeFactory getInstance()
  {
    return theInstance;
  }

  private WeightedLCRSTreeFactory()
  {
  }

  /**
   *  (A(B,C(D,E,F),G(H),I,K),L(M))
   *  (A-1(B-4(E-10(L-12,M-43),F-34(N-23,O-12,P-15),G-33),C-23(H-32,I-6(Q-22,R-24)),D-51(J-12(S-11,T-32,U-23),K-25))
   */
  public WeightedLCRSTree buildTreeFromParenString( String parenString )
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

     return (WeightedLCRSTree)context.get("latestTree");
  }

  private Map<String,Object> startParen( Map<String,Object> context )
  {
    Stack<WeightedLCRSTree> stack = (Stack<WeightedLCRSTree>)context.get("treeStack");
    WeightedLCRSTree tempTree = null;

    if( stack == null )
    {
      stack = new Stack<WeightedLCRSTree>();
      tempTree = new WeightedLCRSTree("*",0);
      stack.push(tempTree);
      context.put("treeStack", stack );
    }
    else
    {
      tempTree = (WeightedLCRSTree)context.get("lastTree");
      stack.push( tempTree );
    }

    return context;
  }

  private Map<String,Object> endParen( Map<String,Object> context )
  {
    Stack<WeightedLCRSTree> stack = (Stack<WeightedLCRSTree>)context.get("treeStack");

    if( stack == null ) throw new RuntimeException("Unexpected ')' at token " + context.get("tokenCount") );
    WeightedLCRSTree latest = stack.pop();
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
    Stack<WeightedLCRSTree> stack = (Stack<WeightedLCRSTree>)context.get("treeStack");
    if( stack == null ) throw new RuntimeException("Unexpected characters at token " + context.get("tokenCount") );
    WeightedLCRSTree currentTree = stack.peek();
    WeightedLCRSTree lastTree = (WeightedLCRSTree)context.get("lastTree");
    String [] valSplit = val.split("-");
    WeightedLCRSTree tree = new WeightedLCRSTree(valSplit[0],Integer.parseInt(valSplit[1]));
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
    WeightedLCRSTree tree = WeightedLCRSTreeFactory.getInstance().buildTreeFromParenString( args[0] );

    System.out.println( tree );
    System.out.println( tree.findOptimal( tree, true ) );
    System.out.println( tree.memoizedFindOptimal( tree, true, new HashMap<String,OptimalSolution>() ) );
  }

}
