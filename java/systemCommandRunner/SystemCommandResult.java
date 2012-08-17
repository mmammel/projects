import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 * User: mammelma
 * Date: Jul 4, 2009
 * Time: 4:12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemCommandResult
{
    Integer returnCode;
    String result;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String toString()
    {
        String tempStr;
        StringBuffer retVal = new StringBuffer();
        BufferedReader resultReader = null;
        retVal.append("{ReturnCode:").append(returnCode).append(",Output:[");

        try
        {
            resultReader = new BufferedReader(new StringReader(this.result));
            while( (tempStr = resultReader.readLine()) != null )
            {
                retVal.append("[").append(tempStr).append("]");
            }
        }
        catch(IOException ioe)
        {
            retVal.append( ioe.toString() );
        }
        finally
        {
            try
            {
                if( resultReader != null ) resultReader.close();
            }
            catch( IOException ioe )
            {
                // Do nothing, assume the stream is closed.
            }
        }

        retVal.append("]}");

        return retVal.toString();
    }
}
