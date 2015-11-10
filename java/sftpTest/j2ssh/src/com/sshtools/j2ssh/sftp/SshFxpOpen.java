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
import com.sshtools.j2ssh.transport.InvalidMessageException;

import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.16 $
 */
public class SshFxpOpen extends SubsystemMessage implements MessageRequestId {
    /**  */
    public static final int SSH_FXP_OPEN = 3;

    /**  */
    public static final int FXF_READ = 0x00000001;

    /**  */
    public static final int FXF_WRITE = 0x00000002;

    /**  */
    public static final int FXF_APPEND = 0x00000004;

    /**  */
    public static final int FXF_CREAT = 0x00000008;

    /**  */
    public static final int FXF_TRUNC = 0x00000010;

    /**  */
    public static final int FXF_EXCL = 0x00000020;
    private UnsignedInteger32 id;
    private String filename;
    private UnsignedInteger32 pflags;
    private FileAttributes attrs;

    //public static final int FXF_TEXT = 0x00000040;
    public SshFxpOpen(UnsignedInteger32 id, String filename,
        UnsignedInteger32 pflags, FileAttributes attrs) {
        super(SSH_FXP_OPEN);
        this.id = id;
        this.filename = filename;
        this.pflags = pflags;
        this.attrs = attrs;
    }

    /**
     * Creates a new SshFxpOpen object.
     */
    public SshFxpOpen() {
        super(SSH_FXP_OPEN);
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
    public String getFilename() {
        return filename;
    }

    /**
     *
     *
     * @return
     */
    public UnsignedInteger32 getPflags() {
        return pflags;
    }

    /**
     *
     *
     * @return
     */
    public FileAttributes getAttributes() {
        return attrs;
    }

    /**
     *
     *
     * @param bar
     *
     * @throws IOException
     * @throws InvalidMessageException
     */
    public void constructMessage(ByteArrayReader bar)
        throws IOException, InvalidMessageException {
        id = bar.readUINT32();
        filename = bar.readString();
        pflags = bar.readUINT32();
        attrs = new FileAttributes(bar);
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_FXP_OPEN";
    }

    /**
     *
     *
     * @param baw
     *
     * @throws IOException
     * @throws InvalidMessageException
     */
    public void constructByteArray(ByteArrayWriter baw)
        throws IOException, InvalidMessageException {
        baw.writeUINT32(id);
        baw.writeString(filename);
        baw.writeUINT32(pflags);
        baw.writeBinaryString(attrs.toByteArray());
    }
}
