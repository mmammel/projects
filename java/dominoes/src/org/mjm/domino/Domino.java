package org.mjm.domino;

import java.util.Map;
import java.util.HashMap;

public enum Domino {
  ZeroZero(0,0),
  OneZero(1,0),
  OneOne(1,1),
  TwoZero(2,0),
  TwoOne(2,1),
  TwoTwo(2,2),
  ThreeZero(3,0),
  ThreeOne(3,1),
  ThreeTwo(3,2),
  ThreeThree(3,3),
  FourZero(4,0),
  FourOne(4,1),
  FourTwo(4,2),
  FourThree(4,3),
  FourFour(4,4),
  FiveZero(5,0),
  FiveOne(5,1),
  FiveTwo(5,2),
  FiveThree(5,3),
  FiveFour(5,4),
  FiveFive(5,5),
  SixZero(6,0),
  SixOne(6,1),
  SixTwo(6,2),
  SixThree(6,3),
  SixFour(6,4),
  SixFive(6,5),
  SixSix(6,6);

  private int [] vals;

  public int [] vals() { return this.vals; }
  public int val( int i ) { return (i == 1 || i == 0 ? this.vals[i] : -1); }

  Domino( int v1, int v2 )
  {
    this.vals = new int [2];
    this.vals[0] = v1;
    this.vals[1] = v2;
  }
}
