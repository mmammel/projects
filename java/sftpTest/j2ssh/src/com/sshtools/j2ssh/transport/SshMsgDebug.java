/*
 *  SSHTools - Java SSH2 API
 *
 *  Copyright (C) 2002-2003 Lee David Painter and Contributors.
 *
 *  Contributions made by:
 *
 *  Brett Smith
 *  Richard Pernavas
 *  Erwin Bolwidt
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.sshtools.j2ssh.transport;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;

import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.20 $
 */
public class SshMsgDebug extends SshMessage {
    /**  */
    protected final static int SSH_MSG_DEBUG = 4;

    // Holds the language_tag value
    private String langTag;

    // Holds the message value
    private String message;

    // Holds the always_display value
    private boolean alwaysDisplay;

    /**
     * Creates a new SshMsgDebug object.
     *
     * @param alwaysDisplay
     * @param message
     * @param langTag
     */
    public SshMsgDebug(boolean alwaysDisplay, String message, String langTag) {
        super(SSH_MSG_DEBUG);

        // Save the debug details
        this.alwaysDisplay = alwaysDisplay;
        this.message = message;
        this.langTag = langTag;
    }

    /**
     * Creates a new SshMsgDebug object.
     */
    public SshMsgDebug() {
        super(SSH_MSG_DEBUG);
    }

    /**
     *
     *
     * @return
     */
    public boolean getDisplayAlways() {
        return alwaysDisplay;
    }

    /**
     *
     *
     * @return
     */
    public String getLanguageTag() {
        return langTag;
    }

    /**
     *
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_MSG_DEBUG";
    }

    /**
     *
     *
     * @param baw
     *
     * @throws InvalidMessageException
     */
    protected void constructByteArray(ByteArrayWriter baw)
        throws InvalidMessageException {
        try {
            // Write the data
            baw.write(alwaysDisplay ? 1 : 0);
            baw.writeString(message);
            baw.writeString(langTag);
        } catch (IOException ioe) {
            throw new InvalidMessageException("Error writing message data: " +
                ioe.getMessage());
        }
    }

    /**
     *
     *
     * @param bar
     *
     * @throws InvalidMessageException
     */
    protected void constructMessage(ByteArrayReader bar)
        throws InvalidMessageException {
        try {
            // Extract the message information
            alwaysDisplay = (bar.read() == 0) ? false : true;
            message = bar.readString();
            langTag = bar.readString();
        } catch (IOException ioe) {
            throw new InvalidMessageException("Error reading message data: " +
                ioe.getMessage());
        }
    }
}
