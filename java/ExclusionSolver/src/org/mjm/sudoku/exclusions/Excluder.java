package org.mjm.sudoku.exclusions;

import org.mjm.sudoku.Board;
import org.mjm.sudoku.Cell;
import org.mjm.sudoku.exclusions.rule.Rule;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.EnumMap;
import java.util.Properties;

public class Excluder
{
  private static final String RULE_PROPS = "rules.properties";
  private static final String RULE_PROP_PREFIX = "rule";
  private Board mBoard;
  private List<Rule> mRules;
  
  public Excluder(String filename)
  {
    FileInputStream fis = null;
    try
    {
      this.mBoard = new Board(filename);
      this.mRules = new ArrayList<Rule>();
      Properties props = new Properties();
      props.load( fis = new FileInputStream( RULE_PROPS ) );
      String tempProp = null;
      Class clazz = null;
      int ruleIdx = 1;
      try
      {
        while( (tempProp = props.getProperty( RULE_PROP_PREFIX + ruleIdx++ )) != null )
        {
          clazz = Class.forName( tempProp );
          if( clazz == null ) throw new IllegalArgumentException("Illegal rule class for prop #" + (ruleIdx - 1) );
          this.mRules.add( (Rule)clazz.newInstance() );
        }
      }
      catch( IllegalArgumentException iae )
      {
        throw iae;
      }
      catch( Exception e )
      {
        throw new IllegalArgumentException( "Error in rule properties file: " + e.toString() );
      }
    }
    catch( IOException ioe )
    {
      throw new IllegalArgumentException( "Bad filename \"" + filename + "\" given.", ioe );
    }
    finally
    {
      try
      {
        fis.close();
      }
      catch( IOException ioe2 )
      {
        // do nothing.
      }
    }

    System.out.println( "Size of rules: " + this.mRules.size() );
  }

  public Board getBoard()
  {
    return this.mBoard;
  }

  public void runRules()
  {
    Rule tempRule = null;
    boolean changesMade = true;
    boolean changeTracker = false;

    while( changesMade )
    {
      for( int i = 0; i < this.mRules.size(); i++ )
      {
        tempRule = this.mRules.get(i);
        
        if( changesMade = tempRule.runRule( this.mBoard ) ) break;
      }
    }
  }

  private String getStringFromCellList( List<Cell> cells )
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for( Cell cell : cells )
    {
      sb.append( cell.getId() ).append(" " );
    }
    sb.append("]");
    return sb.toString();
  }

  public static void main(String [] args)
  {
    Excluder e = new Excluder(args[0]);
    System.out.println( e.getBoard().toSimpleString() );
    System.out.println( e.getBoard() );
    e.runRules();
    System.out.println( e.getBoard() );
    System.out.println( e.getBoard().toSimpleString() );
  }
}
