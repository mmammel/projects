import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class SetCover
{
  private Set<String> X = new TreeSet<String>();

  private Map<String,Set<String>> memo = new HashMap<String,Set<String>>();

  private List<SubSet> subsets = new ArrayList<SubSet>();

  private void addXMember( String ele )
  {
    this.X.add( ele );
  }

  public boolean inX( String ele )
  {
    return this.X.contains(ele);
  }

  public Set<String> getComplement( Set<String> from, SubSet subset )
  {
    Set<String> retVal = new TreeSet<String>();
    retVal.addAll( from );
    retVal.removeAll( subset.subset );
    return retVal;
  }

  public void addSubSet( SubSet s )
  {
    this.subsets.add( s );
  }

  public static class SubSet {
    public SubSet( String name )
    {
      this.descriptor = name;
      subset = new TreeSet<String>();
    }


    public void addMember( String ele )
    {
      this.subset.add( ele );
    }

    public String toString()
    {
      StringBuilder retVal = new StringBuilder();
      retVal.append( "[" + this.descriptor + "](" );
      for( String ele : this.subset )
      {
        retVal.append( ele + " ");
      }
      retVal.append( ")" );

      return retVal.toString();
    }

    public String descriptor;
    public Set<String> subset = new TreeSet<String>();
  }

  public String toString()
  {
    StringBuilder retVal = new StringBuilder();
    retVal.append( "X:[" );
    for( String ele : this.X )
    {
      retVal.append( ele + " " );
    }

    retVal.append( "]\n" );

    for( SubSet s : this.subsets )
    {
      retVal.append( s.toString() + "\n" );
    }

    return retVal.toString();
  }

  public String doGreedy()
  {
    return this.doGreedyInner( this.X, this.subsets );
  }

  private String doGreedyInner( Set<String> s, List<SubSet> l )
  {
    int bestIdx = 0;
    int best = s.size();
    List<SubSet> newList = null;
    Set<String> newSet = null;
    Set<String> bestSet = null;

    for( int i = 0; i < l.size(); i++ )
    {
      newSet = this.getComplement(s,l.get(i));
      if( newSet.size() < best )
      {
        best = newSet.size();
        bestIdx = i;
        bestSet = newSet;
      }
    }
System.out.println( "New best: " + best );
    newList = new ArrayList<SubSet>();
    newList.addAll( l );
    newList.remove( bestIdx );

    if( bestSet.size() > 0 )
    {
      System.out.print("X: " );
      for( String foo : bestSet ) System.out.print( foo + " " );
      System.out.print("\nSS: ");
      for( SubSet bar : newList ) System.out.println( bar );


      return l.get(bestIdx).descriptor + " " + this.doGreedyInner( bestSet, newList );
    }
    else
    {
      return l.get( bestIdx).descriptor;
    }
  }

  /**
   Input file format:

   Line 1: space delimited list of elements of set X

   Lines >1:
     a. if line starts with "S:" The remainder of the line is a subset descriptor.
     b. Any non-blank line is an element of X that belows to the most recent subset.
     c. Any non-blank lines that do not start with S: that do not belong to X or
        do not follow a S: are ignored.
   **/
   public static void main( String [] args )
   {
     String fname = args[0];
     String input_str = null;
     BufferedReader input_reader = null;
     SetCover SC = new SetCover();

     try
     {
       input_reader = new BufferedReader( new FileReader(fname) );

       boolean first = true;
       SubSet current = null;
       while( (input_str = input_reader.readLine()) != null )
       {
         if( first )
         {
           // The list of elements.
           first = false;
           String [] eles = input_str.split(" ");
           for( String ele : eles )
           {
             SC.addXMember( ele );
           }
         }
         else if( input_str.startsWith( "S:" ) )
         {
           current = new SubSet( input_str.substring(2).trim() );
           SC.addSubSet( current );
         }
         else
         {
           if( current != null && input_str.trim().length() > 0 )
           {
             current.addMember( input_str.trim() );
           }
         }
       }

       System.out.println( SC.doGreedy() );
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}