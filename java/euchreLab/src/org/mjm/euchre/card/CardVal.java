package org.mjm.euchre.card;

public enum CardVal {
  Ace("A"),
  King("K"),
  Queen("Q"),
  Jack("J"),
  Ten("10"),
  Nine("9");

  private final String str;

  CardVal( String str ) {
    this.str = str;
  }

  public String toString() { return this.str; }
}
