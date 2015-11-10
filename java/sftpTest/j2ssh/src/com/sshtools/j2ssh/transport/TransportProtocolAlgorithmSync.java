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

import com.sshtools.j2ssh.transport.cipher.SshCipher;
import com.sshtools.j2ssh.transport.compression.SshCompression;
import com.sshtools.j2ssh.transport.hmac.SshHmac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.18 $
 */
public class TransportProtocolAlgorithmSync {
    private static Log log = LogFactory.getLog(TransportProtocolAlgorithmSync.class);
    private SshCipher cipher = null;
    private SshCompression compression = null;
    private SshHmac hmac = null;
    private boolean isLocked = false;

    /**
     * Creates a new TransportProtocolAlgorithmSync object.
     */
    public TransportProtocolAlgorithmSync() {
    }

    /**
     *
     *
     * @param cipher
     */
    public synchronized void setCipher(SshCipher cipher) {
        this.cipher = cipher;
    }

    /**
     *
     *
     * @return
     */
    public synchronized SshCipher getCipher() {
        return cipher;
    }

    /**
     *
     *
     * @param compression
     */
    public synchronized void setCompression(SshCompression compression) {
        this.compression = compression;
    }

    /**
     *
     *
     * @return
     */
    public synchronized SshCompression getCompression() {
        return compression;
    }

    /**
     *
     *
     * @param hmac
     */
    public synchronized void setHmac(SshHmac hmac) {
        this.hmac = hmac;
    }

    /**
     *
     *
     * @return
     */
    public synchronized SshHmac getHmac() {
        return hmac;
    }

    /**
     *
     */
    public synchronized void lock() {
        while (isLocked) {
            try {
                wait(50);
            } catch (InterruptedException e) {
            }
        }

        isLocked = true;
    }

    /**
     *
     */
    public synchronized void release() {
        isLocked = false;
        notifyAll();
    }
}
