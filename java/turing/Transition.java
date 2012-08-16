public class Transition<T>
{
  private State state;
  private T input;
  private StateMachineAction action;

  public Transition( State state, T t, StateMachineAction<T> action )
  {
    this.state = state;
    this.input = t;
    this.action = action;
  }


}