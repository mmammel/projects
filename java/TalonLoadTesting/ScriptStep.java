public class ScriptStep
{
  public ScriptStep( String command, String args, RowType type )
  {
    this.type = type;
    this.command = command;
    this.args = args;
  }

  private RowType type;
  private String command;
  private String args;

  public RowType getType() {
    return type;
  }

  public String getCommand() {
    return command;
  }

  public String getArgs() {
    return args;
  }
}