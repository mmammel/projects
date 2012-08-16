public interface StateMachineAction<T>
{
  public abstract State performAction( T t );
}