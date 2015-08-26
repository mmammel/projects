package org.mjm.euchre.card;

public enum CardVal {
  Nine("9"),
  Ten("10"),
  Jack("J"),
  Queen("Q"),
  King("K"),
  Ace("A");

  private final String str;

  CardVal( String str ) {
    this.str = str;
  }

  public String toString() { return this.str; }
}
