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

import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.23 $
 */
public class SftpFile implements Comparable {
    private String filename;
    private byte[] handle;
    private FileAttributes attrs;
    private SftpSubsystemClient sftp;
    private String absolutePath;

    /**
     * Creates a new SftpFile object.
     *
     * @param absolutePath
     * @param attrs
     */
    public SftpFile(String absolutePath, FileAttributes attrs) {
        this.absolutePath = absolutePath;

        int i = absolutePath.lastIndexOf("/");

        if (i > -1) {
            this.filename = absolutePath.substring(i + 1);
        } else {
            this.filename = absolutePath;
        }

        this.attrs = attrs;
    }

    /**
     * Creates a new SftpFile object.
     *
     * @param absolutePath
     */
    public SftpFile(String absolutePath) {
        this(absolutePath, new FileAttributes());
    }

    /**
     *
     *
     * @throws IOException
     */
    public void delete() throws IOException {
        if (sftp == null) {
            throw new IOException("Instance not connected to SFTP subsystem");
        }

        if (isDirectory()) {
            sftp.removeDirectory(getAbsolutePath());
        } else {
            sftp.removeFile(getAbsolutePath());
        }
    }

    /**
     *
     *
     * @param newFilename
     *
     * @throws IOException
     */
    public void rename(String newFilename) throws IOException {
        if (sftp == null) {
            throw new IOException("Instance not connected to SFTP subsystem");
        }

        sftp.renameFile(getAbsolutePath() + filename, newFilename);
    }

    /**
     *
     *
     * @return
     */
    public boolean canWrite() {
        return (getAttributes().getPermissions().longValue() &
        FileAttributes.S_IWUSR) == FileAttributes.S_IWUSR;
    }

    /**
     *
     *
     * @return
     */
    public boolean canRead() {
        return (getAttributes().getPermissions().longValue() &
        FileAttributes.S_IRUSR) == FileAttributes.S_IRUSR;
    }

    /**
     *
     *
     * @return
     */
    public boolean isOpen() {
        if (sftp == null) {
            return false;
        }

        return sftp.isValidHandle(handle);
    }

    /**
     *
     *
     * @param handle
     */
    protected void setHandle(byte[] handle) {
        this.handle = handle;
    }

    /**
     *
     *
     * @return
     */
    protected byte[] getHandle() {
        return handle;
    }

    /**
     *
     *
     * @param sftp
     */
    protected void setSFTPSubsystem(SftpSubsystemClient sftp) {
        this.sftp = sftp;
    }

    /**
     *
     *
     * @return
     */
    protected SftpSubsystemClient getSFTPSubsystem() {
        return sftp;
    }

    /**
     *
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    private String pad(int num) {
        String str = "";

        if (num > 0) {
            for (int i = 0; i < num; i++) {
                str += " ";
            }
        }

        return str;
    }

    /**
     *
     *
     * @return
     */
    public String getLongname() {
        StringBuffer str = new StringBuffer();
        str.append(pad(10 - getAttributes().getPermissionsString().length()) +
            getAttributes().getPermissionsString());
        str.append("   1 ");
        str.append(getAttributes().getUID().toString() +
            pad(8 - getAttributes().getUID().toString().length())); //uid
        str.append(" ");
        str.append(getAttributes().getGID().toString() +
            pad(8 - getAttributes().getGID().toString().length())); //gid
        str.append(" ");
        str.append(pad(8 - getAttributes().getSize().toString().length()) +
            getAttributes().getSize().toString());
        str.append(" ");
        str.append(pad(12 - getAttributes().getModTimeString().length()) +
            getAttributes().getModTimeString());
        str.append(" ");
        str.append(filename);

        return str.toString();
    }

    /**
     *
     *
     * @return
     */
    public FileAttributes getAttributes() {
        try {
            if (attrs == null) {
                attrs = sftp.getAttributes(this);
            }
        } catch (IOException ioe) {
            attrs = new FileAttributes();
        }

        return attrs;
    }

    /**
     *
     *
     * @return
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     *
     *
     * @throws IOException
     */
    public void close() throws IOException {
        sftp.closeFile(this);
    }

    /**
     *
     *
     * @return
     */
    public boolean isDirectory() {
      return getAttributes().isDirectory();
    }

    /**
     *
     *
     * @return
     */
    public boolean isFile() {
      return getAttributes().isFile();
    }

    /**
     *
     *
     * @return
     */
    public boolean isLink() {
      return getAttributes().isLink();
    }

    /**
     *
     *
     * @return
     */
    public boolean isFifo() {
      return getAttributes().isFifo();
    }

    /**
     *
     *
     * @return
     */
    public boolean isBlock() {
      return getAttributes().isBlock();
    }

    /**
     *
     *
     * @return
     */
    public boolean isCharacter() {
      return getAttributes().isCharacter();
    }

    /**
     *
     *
     * @return
     */
    public boolean isSocket() {
      return getAttributes().isSocket();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
      return getFilename().compareTo(((SftpFile) o).getFilename());
    }
}
