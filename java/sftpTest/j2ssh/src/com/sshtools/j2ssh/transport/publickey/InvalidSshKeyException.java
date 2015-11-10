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

import com.sshtools.j2ssh.transport.*;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.15 $
 */
public class InvalidSshKeyException extends TransportProtocolException {
    /**
     * Creates a new InvalidSshKeyException object.
     */
    public InvalidSshKeyException() {
        super("The SSH key supplied is invalid");
    }

    /**
     * Creates a new InvalidSshKeyException object.
     *
     * @param msg
     */
    public InvalidSshKeyException(String msg) {
        super(msg);
    }
}
