public class Piece
{
  public EnumSet<Slot> [] sides;

  public Piece( String sideString )
  {
    String [] sideStrings = sideString.split(" ");
    if( sideStrings.length != 4 ) throw new IllegalArgumentException( "Array must have 4 side representations." );
    this.sides = new EnumSet<Slot> [4];

    String tempInput = null;
    for( int j = 0; j < sideStrings.length; i++ )
    {
      this.sides[j] = new EnumSet<Slot>();
      tempInput = sideStrings[j];
      if( tempInput.length() != 6 ) throw new IllegalArgumentException( "Each side must represent 6 slots: \"" + tempInput + "\" does not." );

      for( int i = 0; i < tempInput.length(); i++ )
      {
        char c = tempInput.charAt(i);
        if( c == '1' )
        {
          switch(i)
          {
            case 0:
             this.sides[j].add( Slot.ONE );
             break;
            case 1:
             this.sides[j].add( Slot.TWO );
             break;
            case 2:
             this.sides[j].add( Slot.THREE );
             break;
            case 3:
             this.sides[j].add( Slot.FOUR );
             break;
            case 4:
             this.sides[j].add( Slot.FIVE );
             break;
            case 5:
             this.sides[j].add( Slot.SIX );
             break;
          }
        }
      }
    }
  } 
}
