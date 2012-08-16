import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: mmammel
 * Date: May 19, 2008
 * Time: 2:20:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class XMLTagIterator {
    BufferedReader mReader = null;
    String mTagName = null;
    State mState = State.SEEK;
    ParseState mParseState = ParseState.SCAN;
    StringBuffer mCurrentOutput = null;
    Pattern mStartTagPattern = null;
    Pattern mEndTagPattern = null;

    public enum ParseState {
        SCAN,
        TAG_READ,
        CDATA,
        COMMENT
    }

    public enum State {
        SEEK,
        WRITE,
        DONE
    }

    public static final char TAG_START = '<';
    public static final char TAG_END = '>';

    /**
     * Assumes Valid XML!!!
     * @param tagName
     * @param urlString
     * @throws IOException
     */
    public XMLTagIterator(String tagName, String urlString ) throws IOException {
        this(tagName, new URL(urlString));
    }

    /**
     * Assumes Valid XML!!!
     * @param tagName
     * @param input
     * @throws IOException
     */
    public XMLTagIterator(String tagName, URL input) throws IOException {
        mTagName = tagName;
        this.mStartTagPattern = Pattern.compile("^<" + this.mTagName + "(?=\\s+|>).*");
        this.mEndTagPattern = Pattern.compile("^</" + this.mTagName + ">");
        mReader = new BufferedReader(new InputStreamReader(input.openStream()));
    }

    /**
     * Called to get the next segment defined by the XML tag name passed to the
     * constructor.  Will return a string representation of the XML segment or
     * null if there are no more in the document.
     *
     * !!!!!Assumes valid XML!!!!!!
     *
     * @return
     * @throws IOException
     */
    public String getNextSegment() throws IOException {
        String retVal = null;
        String tag;
        boolean foundSegment = false;

        if( this.mState != State.DONE )
        {
            this.mCurrentOutput = new StringBuffer();

            while ((tag = this.getNextTag()) != null) {
                if( this.mState == State.SEEK )
                {
                   if (tag.startsWith( "<" + this.mTagName ) && this.mStartTagPattern.matcher(tag).matches()) {
                        this.mCurrentOutput.append(tag);

                        if (tag.endsWith("/>")) {
                            this.mState = State.SEEK;
                            foundSegment = true;
                            break;
                        } else {
                            this.mState = State.WRITE;
                        }
                    }
                }
                else if( this.mState == State.WRITE)
                {
                    this.mCurrentOutput.append(tag);

                    if (this.mEndTagPattern.matcher(tag).matches()) {
                        this.mState = State.SEEK;
                        foundSegment = true;
                        break;
                    }
                }
            }

            if( foundSegment )
            {
                retVal = this.mCurrentOutput.toString();
            }
            else
            {
                this.mState = State.DONE;
                this.mReader.close();
                retVal = null;
            }
        }

        return retVal;
    }

    /**
     * Called internally to get the next tag, which can have any of the forms:
     * (1) <someName>
     * (2) <someName attr0="foo" ... attrN="bar">
     * (3) <someName/>
     * (4) <someName attr0="foo" ... attrN="bar" />
     * (5) </someName>
     * (6) <![CDATA[...]]>
     * (7) <!-- ... -->
     * (8) <? ... ?>
     *
     * @return
     * @throws IOException
     */
    protected String getNextTag() throws IOException {
        int readChar = -1;
        StringBuffer buff = new StringBuffer();
        String retVal = null;
        boolean foundTag = false;

        while ((readChar = this.mReader.read()) != -1) {
            if (readChar == TAG_START) {
                if (this.mParseState == ParseState.TAG_READ) {
                    if( buff.indexOf("<![CDATA[") == 0 )
                        this.mParseState = ParseState.CDATA;
                    else if( buff.indexOf("<!--") == 0 )
                        this.mParseState = ParseState.COMMENT;
                } else if (this.mParseState == ParseState.SCAN) {
                    this.mParseState = ParseState.TAG_READ;
                }

                buff.append((char) readChar);
            } else if (readChar == TAG_END) {
                buff.append((char) readChar);

                if (this.mParseState == ParseState.CDATA) {
                    if (buff.lastIndexOf("]]>") != -1) {
                        foundTag = true;
                        this.mParseState = ParseState.SCAN;
                        break;
                    }
                } else if( this.mParseState == ParseState.COMMENT) {
                    if( buff.lastIndexOf("-->") != -1) {
                        foundTag = true;
                        this.mParseState = ParseState.SCAN;
                        break;
                    }
                } else {
                    foundTag = true;
                    this.mParseState = ParseState.SCAN;
                    break;
                }
            } else if (this.mParseState != ParseState.SCAN) {
                buff.append((char) readChar);
            } else if (this.mState == State.WRITE) {
                this.mCurrentOutput.append((char) readChar);
            }
        }

        if (foundTag) {
            retVal = buff.toString();
        }

        return retVal;
    }

    public static void main(String[] args) {
        try {
            XMLTagIterator XI = new XMLTagIterator(args[0], args[1]);

            String segment = null;

            while ((segment = XI.getNextSegment()) != null) {
                System.out.println("Found Segment!!!");
                System.out.println("------START-----");
                System.out.println(segment);
                System.out.println("-------END------");
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
