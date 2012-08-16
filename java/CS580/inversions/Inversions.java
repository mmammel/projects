public class Inversions
{
  public static void main( String [] args )
  {
    String [] strs = args[0].split(",");
    int [] ints = new int [strs.length];
    try
    {
      for( int i = 0; i < strs.length; i++ )
      {
        ints[i] = Integer.parseInt(strs[i]);
      }

      Inversions inv = new Inversions();

      System.out.println( "Inversions: " + inv.countInversions(ints,0,ints.length - 1));
      for( int j = 0; j < ints.length; j++ ) System.out.print( ""+ints[j]+" " );
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public int countInversions( int [] A, int p, int r )
  {
    int q = 0,result = 0;
    if( r > p )
    {
      q = (r+p)/2;
      result += this.countInversions( A, p, q );
      result += this.countInversions( A, Math.min(q+1,r), r );
      result += this.merge( A, p, q, r );
    }
    return result;
  }

  public int merge( int [] A, int p, int q, int r )
  {
    System.out.println( "Merge [p,q,r] = [" + p + "," + q + "," + r + "]" );
    int result = 0;
    int n1 = q - p + 1;
    int n2 = r - q;
    int [] left = new int [n1+1];
    int [] right = new int [n2+1];
    for( int l = 0; l < n1; l++ ) left[l] = A[p+l];
    for( int t = 0; t < n2; t++ ) right[t] = A[q+t+1];
    left[n1] = right[n2] = Integer.MAX_VALUE;
    System.out.print( "LEFT: [ " );
    for( int pl = 0; pl < left.length - 1; pl++ ) System.out.print(""+left[pl]+" ");
    System.out.println("]");
    System.out.print( "RIGHT: [ " );
    for( int pr = 0; pr < right.length - 1; pr++ ) System.out.print(""+right[pr]+" ");
    System.out.println("]");
    int i = 0;
    int j = 0;
    for(int k = p; k <= r; k++ )
    {
      if( left[i] <= right[j] )
      {
        A[k] = left[i];
        i++;
      }
      else{
        System.out.println( "Found " +(left.length - 1 - i)+ " Inversions, (i,j) = ("+i+","+j+")");
        result=result + left.length - 1 - i;
        A[k] = right[j];
        j++;
      } 
    }
    return result;
  }
}
