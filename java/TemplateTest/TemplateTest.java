import java.io.File;
import java.io.IOException;

public interface TemplateTest<T>
{
	public static final String TIMESTAMP_KEY = "__tImEsTaMp__";
    public T getData( String key ) throws IOException;
}