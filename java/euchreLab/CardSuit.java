public enum CardSuit
{
  Diamonds(CardColor.Red),
  Hearts(CardColor.Red),
  Spades(CardColor.Black),
  Clubs(CardColor.Black);

  private final CardColor color;
  
  public CardColor color() { return this.color; }

  CardSuit( CardColor color )
  {
    this.color = color;
  }
}
