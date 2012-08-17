import java.io.*;
import java.net.Socket;

/**
 * User: mammelma
 * Date: Jul 4, 2009
 * Time: 12:12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class SocketListenerCommandRunner implements SystemCommandRunner
{
    private Integer port;
    private String host;

    public SystemCommandResult executeCommand(String command ) throws IOException
    {
        String tempInput;
        SystemCommandResult retVal = new SystemCommandResult();
        StringBuffer inputBuffer = new StringBuffer();
        Socket socket = new Socket( this.host, this.port );

        PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        writer.println(command);

        boolean first = true;
        while( (tempInput = reader.readLine()) != null )
        {
            if( first )
            {
                retVal.setReturnCode(Integer.parseInt(tempInput));
                first = false;
                continue;
            }

            inputBuffer.append( tempInput ).append('\n');
        }

        retVal.setResult(inputBuffer.toString());

        writer.close();
        reader.close();
        socket.close();

        return retVal;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
