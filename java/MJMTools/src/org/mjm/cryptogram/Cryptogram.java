package org.mjm.cryptogram;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Date;
import org.mjm.tools.wordpattern.WordPatternTree;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cryptogram
{
  private CryptoWord [] mNaturalOrderedArray = null;
  private CryptoWord [] mLengthOrderedArray = null;
  private CipherTable mCipher = new CipherTable();
  private WordPatternTree mPatternTree = new WordPatternTree("dictionary.txt");

  public Cryptogram( String puzzle )
  {
    String [] tokens = null;

    if( puzzle != null )
    {
      puzzle = puzzle.toLowerCase();
      tokens = puzzle.split( "\\s" );
      CryptoWord tempWord = null;
      
      this.mLengthOrderedArray = new CryptoWord[ tokens.length ];
      this.mNaturalOrderedArray = new CryptoWord[ tokens.length ];
      SortedSet<CryptoWord> lengthOrderedSet = new TreeSet<CryptoWord>( new CryptoWordLengthComparator() );

      for( int i = 0; i < tokens.length; i++ )
      {
        //System.out.println( "Token: " + tokens[i] );
        tempWord = new CryptoWord( tokens[i], i );
        lengthOrderedSet.add( tempWord );
        mNaturalOrderedArray[i] = tempWord;
      }
      
      this.mLengthOrderedArray = lengthOrderedSet.toArray(this.mLengthOrderedArray);
    }
  }
  
  public Cryptogram( String puzzle, String hints )
  {
    this(puzzle);
    if( hints != null )
    {
      String [] pairs = hints.split(",");
      String [] tempHint;
      for( String pair : pairs )
      {
        tempHint = pair.split("=");
        this.mCipher.set(tempHint[0].charAt(0), tempHint[1].charAt(0));
      }
    }
  }

  public void solve()
  {
    this.solveInner( 0, this.mCipher );
  }
  
  private void solveInner( int index, CipherTable cipher )
  {
    CryptoWord word = null;
    String hintPattern = null;
    List<String> candidateList = null;
    CipherTable tableCopy = null;
    if( index >= this.mLengthOrderedArray.length )
    {
      this.printSolution();
    }
    else
    {
      word = this.mLengthOrderedArray[index];
      hintPattern = word.getHintPattern(cipher);
      //System.out.println( word.getOriginal() + ":" + hintPattern);
      candidateList = this.mPatternTree.collectWordsWithHint( word.getOriginal(),hintPattern);
      if( candidateList != null )
      {
        for( String candidate : candidateList )
        {
          //System.out.println( "Trying: " + candidate );
          tableCopy = new CipherTable(cipher);
          word.setDecrypted(candidate);
          tableCopy.set(word.getOriginal(),candidate);
          this.solveInner( index+1, tableCopy );
        }
      }
    }
  }

  public void printSolution()
  {
    for( int i = 0; i < this.mNaturalOrderedArray.length; i++ )
    {
      System.out.print(this.mNaturalOrderedArray[i].getDecrypted() + " ");
    }
    System.out.println("");
  }

  public static void main( String [] args )
  {
    /**
     Use: The inquisitive brown fox jumped over the lazy dog
     with cipher:
       abcdefghijklmnopqrstuvwxyz
       zyxwvutsrqponmlkjihgfedcba
       
    producing:
       gsv rmjfrhrgrev yildm ulc qfnkvw levi gsv ozab wlt
    */
    //Cryptogram C = new Cryptogram("ekje jbeycpp jtgycp ekc gnt nhqc pejec, pkc xhzke jbefjnni bkjqzc kcy qjxc eg xjyinjqt xgqygc.");
    //Cryptogram C = new Cryptogram("DHTF XQ H OXQXGS GA JYB NGIUC XS JBITQ GA QJLUB EPJ H FHIJXDPUHI VXSC GA QJLUB XJ XQ UGOB GA JYB BMHZZBIHJBC");
    //Cryptogram CR = new Cryptogram("kg zee bdh ckaeiep sksqhemhlhar m dzlh hlha qhhs ms uksbzub cmbd, dh czr ihgmsmbhep bdh hzabdmhrb zbdhmr");
    //Cryptogram CR = new Cryptogram("vuvzwb jexgwzw qvbxcj cbbxsxvg secs lxmes tmjvmgxqw pxzsz vd swcl upcrwtz rwct vd sew tvzswt", "u=p");
    //CR.solve();
      BufferedReader input_reader = null;
      String input_str = "";
      Cryptogram C = null;
      double stime = 0.0d;
      double etime = 0.0d;
      try
      {
        input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

        System.out.println( "Welcome to the MJM cyrptogram solver, v0.1" );

        System.out.print("Type in your cryptogram, or \"quit\" to exit: ");

        while( (input_str = input_reader.readLine()) != null )
        {

          if( input_str.equalsIgnoreCase( "quit" ) )
          {
            break;
          }
          else
          {
            stime = new Date().getTime();
            C = new Cryptogram( input_str );
            etime = new Date().getTime();
            System.out.println("\nInitialization took: " + (etime - stime)/1000d + " seconds");
            stime = new Date().getTime();
            C.solve();
            etime = new Date().getTime();
            System.out.println("\nSolving took: " + (etime - stime)/1000d + " seconds");
            
          }

          System.out.print( "\nType in your cryptogram, or \"quit\" to exit: " );
        }

        System.out.println( "Latah!" );

      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception: " + e.toString() );
        e.printStackTrace();
      }
  }
}