public enum CellVal {
  BLANK(" "),
  WHITE("\u25cb"),
  BLACK("\u25cf");

  private final String str;

  CellVal( String str ) {
    this.str = str;
  }

  public String toString() {
    return this.str;
  }
}
