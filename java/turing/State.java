public final class State
{
  private String name;
  private StateType type;

  public State( String name, StateType type )
  {
    if( name == null )
    {
      throw new IllegalArgumentException( "State name cannot be null" );
    }

    this.name = name;
  }

  public boolean equals( Object o )
  {
    State other = null;
    boolean retVal = false;

    if( o instanceof State )
    {
      other = (State)o;

      retVal = (other.name.equals(this.name) && other.type == this.type);
    }

    return retVal;
  }

  public int hashCode()
  {
    return (this.name + this.type).hashCode();
  }

  public String toString()
  {
    return this.name + "[" + this.type + "]";
  }
}