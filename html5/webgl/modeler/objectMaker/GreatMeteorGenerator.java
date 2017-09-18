public class GreatMeteorGenerator {
  private static final int [][] POSITIONS = {
    { 0, 0 },
    { 1, 0 },
    { 2, 0 },
    { 3, 0 },
    { 4, 0 },
    { 5, 0 },
    { -1, 0 },
    { -2, 0 },
    { -3, 0 },
    { -4, 0 },
    { -5, 0 },
    { 0, 1 },
    { 0, 2 },
    { 0, 3 },
    { 0, 4 },
    { 0, 5 },
    { 0, -1 },
    { 0, -2 },
    { 0, -3 },
    { 0, -4 },
    { 0, -5 },
    { 1, 1 },
    { 1, 2 },
    { 1, 3 },
    { 1, 4 },
    { 1, -1 },
    { 1, -2 },
    { 1, -3 },
    { 1, -4 },
    { -1, 1 },
    { -1, 2 },
    { -1, 3 },
    { -1, 4 },
    { -1, -1 },
    { -1, -2 },
    { -1, -3 },
    { -1, -4 },
    { 2, 1 },
    { 2, 2 },
    { 2, 3 },
    { 2, -1 },
    { 2, -2 },
    { 2, -3 },
    { -2, 1 },
    { -2, 2 },
    { -2, 3 },
    { -2, -1 },
    { -2, -2 },
    { -2, -3 },
    { 3, 1 },
    { 3, 2 },
    { 3, -1 },
    { 3, -2 },
    { -3, 1 },
    { -3, 2 },
    { -3, -1 },
    { -3, -2 },
    { 4, 1 },
    { 4, -1 },
    { -4, 1 },
    { -4, -1 }
  };

  private static final double [][] CORNERS = {
    {1.0d, 1.0d},
    {1.0d, -1.0d},
    {-1.0d, 1.0d},
    {-1.0d, -1.0d}
  };

  private static final int NO_ROT = -1;
  private static final int ROTX = 0;
  private static final int ROTY = 1;
  private static final int ROTZ = 2;

  private static final float UNIT = .1f;

  public static void main( String [] args ) {
    int [] tempPos = null;
    double [] point = { 0.0d, 0.0d, 0.0d };
    double X = 0.0d, Y = 0.0d, Z = 0.0d;
    double originX, originY, originZ;
    StringBuilder s = new StringBuilder();
    boolean first = true;
    s.append( "faces = [\n" );
    for( int i = 0; i < POSITIONS.length; i++ ) {
      tempPos = POSITIONS[i];
      X = new Integer( tempPos[0] ).doubleValue();
      Y = new Integer( tempPos[1] ).doubleValue();
      Z = 5.0d - (Math.abs( X ) + Math.abs( Y ));
      originX = 2.0d * UNIT * X;
      originY = 2.0d * UNIT * Y;
      originZ = Z * 2.0d * UNIT + UNIT;
      if( !first ) {
        s.append( ",\n" );
      } else {
        first = false;
      }

      point[0] = originX;
      point[1] = originY;
      point[2] = originZ;

      s.append( getFace( point, NO_ROT, 0.0d ) );
      s.append( ",\n" );
      s.append( getFace( point, ROTX, Math.PI * 0.5d ) );
      s.append( ",\n" );
      s.append( getFace( point, ROTX, Math.PI ) );
      s.append( ",\n" );
      s.append( getFace( point, ROTX, 3.0d * Math.PI * 0.5d ) );
      s.append( ",\n" );
      s.append( getFace( point, ROTY, Math.PI * 0.5d ) );
      s.append( ",\n" );
      s.append( getFace( point, ROTY, 3.0d * Math.PI * 0.5d ) );
    }

    s.append( "\n];" );
    System.out.println( s.toString() );
  }

  private static String getFace( double [] point, int axis, double angle ) {
    StringBuilder s = new StringBuilder();
    double [] tempPoint = { 0.0d, 0.0d, 0.0d };
    double [] rot = null;
    s.append( "{ vertices: [ " );
    for( int j = 0; j < CORNERS.length; j++ ) {
      tempPoint[0] = (point[0]+(CORNERS[j][0]*UNIT));
      tempPoint[1] = (point[1]+(CORNERS[j][1]*UNIT));
      tempPoint[2] = point[2];

      rot = rotate( axis, angle, tempPoint );

      s.append( rot[0] ).append( ", " ).append( rot[1] ).append( ", " ).append( rot[2] ).append( j < (CORNERS.length - 1) ? ", " : "" );
    }
    s.append( "] }" );

    return s.toString();
  }

  private static double [] rotate( int axis, double angle, double [] point ) {
    double [] retVal = null;

    switch( axis ) {
      case ROTX:
        retVal = rotateX( angle, point );
        break;
      case ROTY:
        retVal = rotateY( angle, point );
        break;
      case ROTZ:
        retVal = rotateZ( angle, point );
        break;
      default:
        retVal = point;
        break;
    }

    return retVal;
  }

  /**
   * 1    0    0
   * 0  cos  -sin
   * 0  sin   cos
   */
  private static double [] rotateX( double angle, double [] point ) {
    double [] retVal = { 0.0d, 0.0d, 0.0d };
    retVal[0] = point[0];
    retVal[1] = Math.cos( angle ) * point[1] - Math.sin( angle ) * point[2];
    retVal[2] = Math.sin( angle ) * point[1] + Math.cos( angle ) * point[2];
    return retVal;
  }

  /**
   * cos  0   sin
   * 0    1    0
   *-sin  0   cos
   */
  private static double [] rotateY( double angle, double [] point ) {
    double [] retVal = { 0.0d, 0.0d, 0.0d };
    retVal[0] = Math.cos( angle ) * point[0] + Math.sin( angle ) * point[2];
    retVal[1] = point[1];
    retVal[2] = -1.0d * Math.sin( angle ) * point[0] + Math.cos( angle ) * point[2];
    return retVal;
  }

  /**
   * cos  -sin  0
   * sin   cos  0
   *  0     0   1
   */
  private static double [] rotateZ( double angle, double [] point ) {
    double [] retVal = { 0.0d, 0.0d, 0.0d };
    retVal[0] = Math.cos( angle ) * point[0] - Math.sin( angle ) * point[1];
    retVal[1] = Math.sin( angle ) * point[0] + Math.cos( angle ) * point[1];
    retVal[2] = point[2];
    return retVal;
  }
}
