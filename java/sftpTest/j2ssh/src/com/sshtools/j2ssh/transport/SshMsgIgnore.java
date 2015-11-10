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
public class SshMsgIgnore extends SshMessage {
    /**  */
    protected final static int SSH_MSG_IGNORE = 2;
    private String data;

    /**
     * Creates a new SshMsgIgnore object.
     *
     * @param data
     */
    public SshMsgIgnore(String data) {
        super(SSH_MSG_IGNORE);
        this.data = data;
    }

    /**
     * Creates a new SshMsgIgnore object.
     */
    public SshMsgIgnore() {
        super(SSH_MSG_IGNORE);
    }

    /**
     *
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_MSG_IGNORE";
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
            baw.writeString(data);
        } catch (IOException ioe) {
            throw new InvalidMessageException(
                "Error occurred writing message data: " + ioe.getMessage());
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
            data = bar.readString();
        } catch (IOException ioe) {
            throw new InvalidMessageException(
                "Error occurred reading message data: " + ioe.getMessage());
        }
    }
}
