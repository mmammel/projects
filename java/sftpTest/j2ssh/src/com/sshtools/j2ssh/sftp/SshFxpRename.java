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
package com.sshtools.j2ssh.sftp;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.io.UnsignedInteger32;
import com.sshtools.j2ssh.subsystem.SubsystemMessage;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.16 $
 */
public class SshFxpRename extends SubsystemMessage implements MessageRequestId {
    /**  */
    public static final int SSH_FXP_RENAME = 18;
    private UnsignedInteger32 id;
    String oldpath;
    String newpath;

    /**
     * Creates a new SshFxpRename object.
     */
    public SshFxpRename() {
        super(SSH_FXP_RENAME);
    }

    /**
     * Creates a new SshFxpRename object.
     *
     * @param id
     * @param oldpath
     * @param newpath
     */
    public SshFxpRename(UnsignedInteger32 id, String oldpath, String newpath) {
        super(SSH_FXP_RENAME);
        this.id = id;
        this.oldpath = oldpath;
        this.newpath = newpath;
    }

    /**
     *
     *
     * @return
     */
    public UnsignedInteger32 getId() {
        return id;
    }

    /**
     *
     *
     * @return
     */
    public String getOldPath() {
        return oldpath;
    }

    /**
     *
     *
     * @return
     */
    public String getNewPath() {
        return newpath;
    }

    /**
     *
     *
     * @param bar
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     */
    public void constructMessage(ByteArrayReader bar)
        throws java.io.IOException, 
            com.sshtools.j2ssh.transport.InvalidMessageException {
        id = bar.readUINT32();
        oldpath = bar.readString();
        newpath = bar.readString();
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_FXP_RENAME";
    }

    /**
     *
     *
     * @param baw
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     */
    public void constructByteArray(ByteArrayWriter baw)
        throws java.io.IOException, 
            com.sshtools.j2ssh.transport.InvalidMessageException {
        baw.writeUINT32(id);
        baw.writeString(oldpath);
        baw.writeString(newpath);
    }
}
