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
package com.sshtools.j2ssh.connection;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.transport.InvalidMessageException;
import com.sshtools.j2ssh.transport.SshMessage;

import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.20 $
 */
public class SshMsgChannelOpenFailure extends SshMessage {
    /**  */
    protected final static int SSH_MSG_CHANNEL_OPEN_FAILURE = 92;

    /**  */
    protected final static long SSH_OPEN_ADMINISTRATIVELY_PROHIBITED = 1;

    /**  */
    protected final static long SSH_OPEN_CONNECT_FAILED = 2;

    /**  */
    protected final static long SSH_OPEN_UNKNOWN_CHANNEL_TYPE = 3;

    /**  */
    protected final static long SSH_OPEN_RESOURCE_SHORTAGE = 4;
    private String additional;
    private String languageTag;
    private long reasonCode;
    private long recipientChannel;

    /**
     * Creates a new SshMsgChannelOpenFailure object.
     *
     * @param recipientChannel
     * @param reasonCode
     * @param additional
     * @param languageTag
     */
    public SshMsgChannelOpenFailure(long recipientChannel, long reasonCode,
        String additional, String languageTag) {
        super(SSH_MSG_CHANNEL_OPEN_FAILURE);
        this.recipientChannel = recipientChannel;
        this.reasonCode = reasonCode;
        this.additional = additional;
        this.languageTag = languageTag;
    }

    /**
     * Creates a new SshMsgChannelOpenFailure object.
     */
    public SshMsgChannelOpenFailure() {
        super(SSH_MSG_CHANNEL_OPEN_FAILURE);
    }

    /**
     *
     *
     * @return
     */
    public String getAdditionalText() {
        return additional;
    }

    /**
     *
     *
     * @return
     */
    public String getLanguageTag() {
        return languageTag;
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_MSG_CHANNEL_OPEN_FAILURE";
    }

    /**
     *
     *
     * @return
     */
    public long getReasonCode() {
        return reasonCode;
    }

    /**
     *
     *
     * @return
     */
    public long getRecipientChannel() {
        return recipientChannel;
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
            baw.writeInt(recipientChannel);
            baw.writeInt(reasonCode);
            baw.writeString(additional);
            baw.writeString(languageTag);
        } catch (IOException ioe) {
            throw new InvalidMessageException("Invalid message data");
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
            recipientChannel = bar.readInt();
            reasonCode = bar.readInt();
            additional = bar.readString();
            languageTag = bar.readString();
        } catch (IOException ioe) {
            throw new InvalidMessageException("Invalid message data");
        }
    }
}
