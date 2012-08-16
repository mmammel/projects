import java.util.Set;

public class StateMachine<T>
{
  private Set<State> states;

  private Set<Transition<T>> transitions;
}
