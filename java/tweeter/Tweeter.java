import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tweeter {

  private static final int CHAR_LIMIT = 240;
  private static final Pattern ID_PATTERN = Pattern.compile("\"id_str\"\\s*?:\\s*?\"(.*?)\"");

  private String apiKey = null;
  private String apiKeySecret = null;
  private String token = null;
  private String tokenSecret = null;

  public Tweeter( String credFile ) {
    this.getCredentials( credFile );
  }

  public void readTweet() {
    this.sendTweet( StreamUtil.stringsFromStream( System.in, CHAR_LIMIT ) );
  }

  private void sendTweet(List<String> tweets) {
    try {
      final OAuth10aService service = new ServiceBuilder("Tweeter")
                           .apiKey(this.apiKey)
                           .apiSecret(this.apiKeySecret)
                           .build(TwitterApi.instance());

      final OAuth1AccessToken accessToken = new OAuth1AccessToken(this.token,this.tokenSecret);

      String tweet = null;
      String prevId = null;
      String responseBody = null;
      OAuthRequest request = null;
      Response response = null;

      for( int i = 0; i < tweets.size(); i++ ) {
        tweet = tweets.get(i);
        if( responseBody != null ) {
          prevId = this.getIdFromResponse( responseBody );
        } 

        request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/update.json?status=" + encodeURIComponent( tweet ) + (prevId == null ? "" : "&auto_populate_reply_metadata=true&in_reply_to_status_id="+prevId));
        service.signRequest(accessToken, request);
        response = service.execute(request);
        responseBody = response.getBody();
      }

    } catch( Exception e ) {
      System.out.println( "Exception!! " + e.toString() );
    }
  }

  private String getIdFromResponse( String response ) {
    String retVal = null;
    if( response != null ) {

      Matcher m = ID_PATTERN.matcher( response );
      if( m.find() ) {
        retVal = m.group(1);
      }
    }

    return retVal;
  }

  private static String encodeURIComponent(String s) {
    String result;

    try {
        result = URLEncoder.encode(s, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
    } catch (UnsupportedEncodingException e) {
        result = s;
    }

    return result;
  }

  private void getCredentials( String fname )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      this.apiKey = input_reader.readLine();
      this.apiKeySecret = input_reader.readLine();
      this.token = input_reader.readLine();
      this.tokenSecret = input_reader.readLine();
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
    finally {
      try {
       input_reader.close();
      } catch( IOException ioe ) {
        System.out.println( "Exception closing stream");
      }
    }
  }

  public static void main( String [] args ) {
    if( args.length != 1 ) {
      System.err.println( "Usage: Tweeter <credentialFile>" );
    } else {
      Tweeter T = new Tweeter( args[0] );
      T.readTweet();
    }
  }

}
