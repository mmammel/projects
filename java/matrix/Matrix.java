import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Matrix {
  private Fraction [][] matrix;
  private boolean valid = true;

  public boolean isValid() { return this.valid; }

  private void initMatrix( int rows ) {
    this.matrix = new Fraction[rows][];
    for( int i = 0; i < rows; i++ ) {
      this.matrix[i] = new Fraction[rows+1];
    }
  }
  
  public Matrix( File f ) {
    BufferedReader reader = null;
    String tempStr;
    String [] tempArray;
    List<String[]> holder = new ArrayList<String[]>();
    List<String> tempRowList;
    try {
      reader = new BufferedReader( new FileReader( f ) );
      while( (tempStr = reader.readLine()) != null ) {
        holder.add(tempStr.split(" "));
      }
    } catch( IOException ioe ) {
      System.out.println( "Exception: " + ioe.toString() );
      this.valid = false;
    } finally {
      try {
        if( reader != null ) reader.close();
      } catch( IOException ioe2 ) {
        System.out.println( "Failed to close file: " + ioe2.toString() );
        this.valid = false;
      }
    }
    
    if( this.valid ) {
      this.initMatrix( holder.size() );
    
      String [] arr;
    
      try {
        for( int i = 0; i < holder.size(); i++ ) {
          arr = holder.get(i);
          if( arr.length != (holder.size() + 1) ) {
            System.out.println( "Bad row (" + i + ") in file, needs " + (holder.size() + 1) + " columns.");
            this.valid = false;
            break;
          } else {
            for( int j = 0; j < arr.length; j++ ) {
              this.matrix[i][j] = new Fraction( arr[j] );
            }
          }
        }
      } catch( IllegalArgumentException iae ) {
        System.out.println( "Bad Fraction: " + iae.toString() );
        this.valid = false;
      }
    }
  }

  public static void main( String [] args ) {
    try {
      Matrix m = new Matrix( new File(args[0]) );
      
      if( m.isValid() ) {
        System.out.println( m );
        m.rowReduce();
        System.out.println( m );
      } else {
        System.out.println( "Invalid Matrix.");
      }
    } catch( Exception e ) {
      System.out.println( "Error opening file: " + e.toString() );
    }
  }
  
  public void rowReduce() {
    Fraction rowHead;
    Fraction rowHeadInv;
    Fraction tempMult;
    
    //for each row
    for( int i = 0; i < this.matrix.length; i++ ) {
      rowHead = this.matrix[i][i];
      rowHeadInv = Fraction.inverse( rowHead );
      for( int j = i; j < this.matrix[i].length; j++ ) {
        this.matrix[i][j] = Fraction.multiply( rowHeadInv, this.matrix[i][j] );
      }
      
      for( int k = 0; k< this.matrix.length; k++ ) {
        if( k == i ) continue;
        
        rowHead = this.matrix[k][i];
        for( int m = i; m < this.matrix[i].length; m++ ) {
          tempMult = Fraction.multiply(this.matrix[i][m],rowHead);
          this.matrix[k][m] = Fraction.subtract(this.matrix[k][m],tempMult);
        }
      }
      
      System.out.println( this );
    }
  }
  
  public String toString() {
    StringBuffer buff = new StringBuffer();
    for( int i = 0; i < this.matrix.length; i++ ) {
      for( int j = 0; j < this.matrix[i].length; j++ ) {
        buff.append( this.matrix[i][j] ).append( "\t" );
      }
      
      buff.append("\n");
    }
    
    return buff.toString();
  }
}

