public class Job implements Comparable<Job>
{
  public int startTime = -1;
  public int deadline;
  public int duration;
  public int value;

  public Job( int deadline, int duration, int value )
  {
    this.deadline = deadline;
    this.duration = duration;
    this.value = value;
  }

  public int compareTo( Job j )
  {
    Integer mydeadline = new Integer(this.deadline);
    Integer otherdeadline = new Integer(j.deadline);
    return mydeadline.compareTo(otherdeadline);
  }

  public String toString()
  {
    return "[StartTime: " + this.startTime + ", Deadline: " + this.deadline + ", Runtime: " + this.duration + ", $" + this.value + "]";
  }
}