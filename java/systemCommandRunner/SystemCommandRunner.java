import java.io.IOException;

/**
 * User: mammelma
 * Date: Jul 4, 2009
 * Time: 12:11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SystemCommandRunner
{
    public SystemCommandResult executeCommand(String command) throws IOException;
    public void setHost( String host );
    public void setPort( Integer port );
}
