import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class Http401Test {
  private static final String REQUEST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope version=\"01.00\"><Sender><Id>FADVASSESSMENT</Id> <Credential/></Sender><Recipient> <Id>Taleo</Id></Recipient><TransactInfo transactType=\"data\"> <TransactId>1141840</TransactId> <TimeStamp>2013-11-14  21:40:0</TimeStamp></TransactInfo><Packet> <PacketInfo packetType=\"data\">  <PacketId>16874781</PacketId>  <Action>AssessmentResult</Action>  <Manifest>AssessmentResult.xsd</Manifest> </PacketInfo> <Payload><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><AssessmentResult xmlns =\"http://ns.hr-xml.org/2004-08-02\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ns.hr-xml.org/2004-08-02 file:/D:/Hr-xml/HR-XML-2004-08/HR-XML-2_3/Assessment/AssessmentResult.xsd\" validTo=\"2014-03-19\"><ReceiptId idOwner=\"FADVASSESSMENT\"> <IdValue Name=\"ReceiptID\">8020909731375627407</IdValue></ReceiptId><Results> <OverallResult>  <Score>100</Score>  <Recommendation></Recommendation>  <ResultText>The overall score is a percentage from 0-100.  Please refer to the detailed report for additional information.</ResultText> </OverallResult></Results><AssessmentStatus> <Status>Completed</Status> <StatusDate>03/19/13 13:34</StatusDate></AssessmentStatus><UserArea> <AdditionalItems>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"version\">   <Text>700</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"dtdVersion\">   <Text>6_10</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"transactId\">   <Text>1141840</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"transactTimestamp\">   <Text>2013-03-12T19:29:50</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"recipientZone\">   <Text>DH-TALEO</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"reportRecipient\">   <Text>https://dhlth.taleo.net/enterprise/passport/xml?auth=1</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"serviceIdentifier\">   <Text>FADVAssessment</Text>  </AdditionalItem>  <AdditionalItem type=\"OriginatorInfo\" qualifier=\"instanceId\">   <Text>67edd1dd-f0b9-4d18-94b1-4638dbc06ede</Text>  </AdditionalItem> </AdditionalItems></UserArea></AssessmentResult>]]></Payload></Packet></Envelope>";
  private static final int BUFFER_SIZE = 2048;

  public static void main( String [] args ) {
    try {
      Http401Test HT = new Http401Test();
      //System.out.println( HT.makeRequest( "https://stgbcbst.taleo.net/enterprise/passport/xml?auth=1",REQUEST_XML,"POST"));
      System.out.println( HT.makeRequest( "https://dhlth.taleo.net/enterprise/passport/xml?auth=1",REQUEST_XML,"POST"));
    } catch( Exception e ) {
      System.out.println( "Exception in main: " + e.toString() );
      e.printStackTrace();
    }
  }
  public String makeRequest( String url, String content, String method ) {
    HttpURLConnection connection = null;
    String response = null;
    String encoding = "UTF-8";

    char[] buffer = new char[BUFFER_SIZE];
    int readSize = 0;
    StringBuilder retVal = new StringBuilder();
    int c = 0;
    int rc = 0;
    String rm = null;
    BufferedWriter writer = null;
    OutputStream os = null;
    InputStreamReader responseReader = null;

    try {

      URL endpoint = new URL( url );
      connection = (HttpURLConnection)endpoint.openConnection();

      // Can we already see a response code?
      //rc = connection.getResponseCode();
      //System.out.println( "Response code after open: " + rc );
      //if( rc == 401 ) { 
      // connection = (HttpURLConnection)endpoint.openConnection();
      //}
       
      connection.setRequestMethod( method );
      connection.setRequestProperty("Authorization","Basic RkFEVkFTU0VTU01FTlQ6UHYhM3VfNDc0eg==");
      connection.setRequestProperty("Content-Type","text/xml; charset=utf-8" );
      connection.setRequestProperty("Content-Length",""+content.length());
      connection.setDoInput(true);
      connection.setDoOutput(true);
      
      os = connection.getOutputStream();
      writer = new BufferedWriter(new OutputStreamWriter(os));
      writer.write(content);

      writer.flush();
      writer.close();

      rc = connection.getResponseCode();
      rm = connection.getResponseMessage();

      System.out.println( "rc/rval = " + rc + "/" + rm );

      if( rc / 2 != 100 ) {
        //error
        System.out.println( "Error response: " + rc );
      } else {
        //success
        System.out.println( "Success response: " + rc );
      }
      InputStream responseInputStream = null;
      responseInputStream = connection.getInputStream();
      responseReader = new InputStreamReader(responseInputStream,encoding);
      while((c = responseReader.read()) != -1 ) {
        char charin = (char)c;
        retVal.append(charin);
      }
    }
    catch( IOException ioe ) {
      System.out.println( "Exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
    finally {
      try {
        if( writer != null) writer.close();
        if( responseReader != null) responseReader.close();
        if( connection != null ) connection.disconnect();
      }
      catch (IOException ioe2) {
        System.out.println("Could not close streams");
      }
    }

    return retVal.toString();
  }
}
