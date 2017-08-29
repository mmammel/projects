public class GradientDebug {
/**
$w = $x2 - $x1;
$h = $y2 - $y1;

$rgb=$c[0]; // start with top left color
for($x=$x1;$x<=$x2;$x++) { // loop columns
  $xoff = $x - $x1;
  if( $xoff == 0 ) $xoff = 1;
  for($y=$y1;$y<=$y2;$y++) { // loop rows
   $yoff = $y - $y1;
   if( $yoff == 0 ) $yoff = 1;
   // set pixel color
   $col=imagecolorallocate($im,$rgb[0],$rgb[1],$rgb[2]);
   imagesetpixel($im,$x,$y,$col);
   // calculate new color
   for($i=0;$i<=2;$i++) {
    $rgb[$i]=
      $c[0][$i]*(($w-$xoff)*($h-$yoff)/($w*$h)) +
      $c[1][$i]*($xoff*($h-$yoff)/($w*$h)) +
      $c[2][$i]*(($w-$xoff)*$yoff/($w*$h)) +
      $c[3][$i]*($xoff*$yoff/($w*$h));
   }
  }
}
**/

  public static void main( String [] args ) { 
    float [][] c = { { 31f, 179f, 233f },
                   { 31f, 179f, 233f },
                   { 19f, 124f, 184f },
                   { 19f, 124f, 184f } };
    Float [] rgb = new Float [3];
    rgb[0] = c[0][0];
    rgb[1] = c[0][1];
    rgb[2] = c[0][2];
 
    float x1 = 60f;
    float y1 = 60f;
    float x2 = 120f;
    float y2 = 120f;
    float xoff = 0f;
    float yoff = 0f;
    float w = x2 - x1;
    float h = y2 - y1;
    int wi = (new Float(w)).intValue();
    int hi = (new Float(h)).intValue();
    int xoffi = 0;
    int yoffi = 0;

    String [][] pixels = new String [wi+1][];
    for( int i = 0; i <= w; i++ ) {
      pixels[i] = new String [hi+1];
    }
    
    for( float x = x1; x <= x2; x++ ) {
      xoff = x - x1;
      xoffi = (new Float(xoff)).intValue();
      for( float y = y1; y <= y2; y++ ) {
        yoff = y - y1;
        yoffi = (new Float(yoff)).intValue();
        pixels[xoffi][yoffi] = "["+rgb[0].intValue()+","+rgb[1].intValue()+","+rgb[2].intValue() +"]";

        for(int i=0;i<=2;i++) {
          rgb[i]=
            c[0][i]*((w-xoff)*(h-yoff)/(w*h)) +
            c[1][i]*(xoff*(h-yoff)/(w*h)) +
            c[2][i]*((w-xoff)*yoff/(w*h)) +
            c[3][i]*(xoff*yoff/(w*h));
        }
      }
    }

    for( int i = 0; i < w; i++ ) {
      for( int j = 0; j < h; j++ ) {
        System.out.print( pixels[i][j] );
      }
      System.out.print("\n");
    } 
  }



}
