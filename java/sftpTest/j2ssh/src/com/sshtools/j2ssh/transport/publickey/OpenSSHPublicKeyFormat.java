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
package com.sshtools.j2ssh.transport.publickey;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.util.Base64;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.19 $
 */
public class OpenSSHPublicKeyFormat implements SshPublicKeyFormat {
    String comment = null;

    /**
     * Creates a new OpenSSHPublicKeyFormat object.
     *
     * @param comment
     */
    public OpenSSHPublicKeyFormat(String comment) {
        setComment(comment);
    }

    /**
     * Creates a new OpenSSHPublicKeyFormat object.
     */
    public OpenSSHPublicKeyFormat() {
    }

    /**
     *
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     *
     * @return
     */
    public String getFormatType() {
        return "OpenSSH-PublicKey";
    }

    /**
     *
     *
     * @param formattedKey
     *
     * @return
     *
     * @throws InvalidSshKeyException
     */
    public byte[] getKeyBlob(byte[] formattedKey) throws InvalidSshKeyException {
        String temp = new String(formattedKey);

        // Get the algorithm name end index
        int i = temp.indexOf(" ");

        if (i > 0) {
            String algorithm = temp.substring(0, i);

            if (supportsAlgorithm(algorithm)) {
                // Get the keyblob end index
                int i2 = temp.indexOf(" ", i + 1);

                if (i2 > i) {
                    String encoded = temp.substring(i + 1, i2);

                    if (temp.length() > i2) {
                        comment = temp.substring(i2 + 1).trim();
                    }

                    return Base64.decode(encoded);
                }
            }
        }

        throw new InvalidSshKeyException("Failed to read OpenSSH key format");
    }

    /**
     *
     *
     * @param keyblob
     *
     * @return
     */
    public byte[] formatKey(byte[] keyblob) {
        String algorithm = ByteArrayReader.readString(keyblob, 0);
        String formatted = algorithm + " " + Base64.encodeBytes(keyblob, true);

        if (comment != null) {
            formatted += (" " + comment);
        }

        return formatted.getBytes();
    }

    /**
     *
     *
     * @param formattedKey
     *
     * @return
     */
    public boolean isFormatted(byte[] formattedKey) {
        String test = new String(formattedKey).trim();

        if (test.startsWith("ssh-dss") || test.startsWith("ssh-rsa")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     *
     * @param algorithm
     *
     * @return
     */
    public boolean supportsAlgorithm(String algorithm) {
        if (algorithm.equals("ssh-dss") || algorithm.equals("ssh-rsa")) {
            return true;
        } else {
            return false;
        }
    }
}
