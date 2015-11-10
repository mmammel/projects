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
package com.sshtools.j2ssh.io;

import com.sshtools.j2ssh.util.State;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class IOStreamConnectorState extends State {
    /**  */
    public final static int BOF = 1;

    /**  */
    public final static int CONNECTED = 2;

    /**  */
    public final static int EOF = 3;

    /**  */
    public final static int CLOSED = 4;

    /**
     * Creates a new IOStreamConnectorState object.
     */
    public IOStreamConnectorState() {
        super(BOF);
    }

    /**
     *
     *
     * @param state
     *
     * @return
     */
    public boolean isValidState(int state) {
        return ((state == BOF) || (state == CONNECTED) || (state == EOF) ||
        (state == CLOSED));
    }
}
