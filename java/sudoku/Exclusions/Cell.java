import java.util.EnumSet;
import java.util.Iterator;

public class Cell
{
  private int mId = -1;
  private EnumSet<Value> mPossibleVals;

  public Cell(int id)
  {
    this.mId = id;
    this.mPossibleVals = EnumSet.range(Value.ONE,Value.NINE);
  }

  public boolean setValue( Value val )
  {
    boolean retVal = false;
    if( this.mPossibleVals.size() > 1 )
    {
      this.mPossibleVals = EnumSet.of(val);
      retVal = true;
    }

    return retVal;
  }

  public String getPrintableValue()
  {
    String retVal = " ";
    if( this.mPossibleVals.size() == 1 )
    {
      retVal = "" + (this.mPossibleVals.toArray(new Value[1])[0].ordinal() + 1);
    }
    return retVal;
  }

  public EnumSet<Value> getExclusions()
  {
    return EnumSet.complementOf( this.mPossibleVals );
  }

  public int getExclusionCount()
  {
    return (9 - this.mPossibleVals.size());
  }

  public boolean exclude( Value val )
  {
    boolean retVal = false;

    if( this.mPossibleVals.contains( val ) )
    {
      retVal = true;
      this.mPossibleVals.remove( val );
    }

    return retVal;
  }

  public boolean exclude( Value [] vals )
  {
    boolean retVal = false;

    for( int i = 0; i < vals.length; i++ )
    {
      retVal = retVal | this.exclude( vals[i] );
    }

    return retVal;
  }

  public int getId() {
    return this.mId;
  }

  public EnumSet<Value> getPossibleVals()
  {
    return this.mPossibleVals;
  }
  
  public int getPossibleValCount()
  {
    return this.mPossibleVals.size();
  }

  public boolean isPossible( Value v )
  {
    return this.mPossibleVals.contains(v);
  }

  public String getExcludeString()
  {
    return this.getValueString( this.getExclusions() );
  }

  public String getIncludeString()
  {
    return this.getValueString(this.mPossibleVals);
  }

  private String getValueString(EnumSet<Value> values)
  {
    Value tempVal;
    StringBuilder retVal = new StringBuilder();

    for( Iterator<Value> iter = values.iterator(); iter.hasNext(); )
    {
      tempVal = iter.next();
      retVal.append(tempVal);
    }

    return retVal.toString();
  }

  public boolean equals( Object o )
  {
    boolean retVal = false;

    if( o instanceof Cell )
    {
      Cell oc = (Cell)o;
      retVal = this.mId == oc.getId();
    }

    return retVal;
  }

  public int hashCode()
  {
    String tmp = ""+this.mId;
    return tmp.hashCode();
  }
}
