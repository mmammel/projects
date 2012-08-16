
public class DataAccessException extends Exception
{

    private Throwable cause = null;

    public DataAccessException() {
        super();
    }

    public  DataAccessException(Exception e) {
        super(e);
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

}
