import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class SSLTest {
    public static void main(String[] args) {
        try {
            //URL url = new URL("https://api.bullhornstaffing.com/webservices-1.1/?wsdl");
            URL url = new URL("https://webtest.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet?ID=Enlivant&MODE=reportretrieval,6778324460361213571,xml");
            URLConnection connection = null;
            try {
                connection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, List<String>> fields = connection.getHeaderFields();
            Iterator<Entry<String, List<String>>> headerIterator = fields.entrySet().iterator();
            System.out.println("HEADERS");
            System.out.println("-------------------------------");
            while (headerIterator.hasNext()){
                Entry<String, List<String>> header = headerIterator.next();
                System.out.println(header.getKey()+" :");
                Iterator<String> valueIterator = header.getValue().iterator();
                while (valueIterator.hasNext()){
                    System.out.println("\t"+valueIterator.next());
                }

            }

            String inputLine;
            DataInputStream input = new DataInputStream(connection.getInputStream());
            System.out.println("BODY CONTENT");
            System.out.println("-------------------------------");
            while ((inputLine = input.readLine()) != null) {
                System.out.println(inputLine);
            }


        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
