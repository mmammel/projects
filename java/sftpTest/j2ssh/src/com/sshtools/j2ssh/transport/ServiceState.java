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

import com.sshtools.j2ssh.util.State;


/**
 * <p>
 * This class represents the state of a transport protocol service.
 * </p>
 *
 * @author Lee David Painter
 * @version $Revision: 1.24 $
 *
 * @since 0.2.0
 */
public class ServiceState extends State {
    /** The service is unitialized */
    public final static int SERVICE_UNINITIALIZED = 1;

    /** The service has started and can send/recieve messages */
    public final static int SERVICE_STARTED = 2;

    /** The service has stopped and no messages can be sent or received */
    public final static int SERVICE_STOPPED = 3;

    /**
     * <p>
     * Constructs the state instance
     * </p>
     */
    public ServiceState() {
        super(SERVICE_UNINITIALIZED);
    }

    /**
     * <p>
     * Evaluates whether the state is valid.
     * </p>
     *
     * @param state
     *
     * @return
     *
     * @since 0.2.0
     */
    public boolean isValidState(int state) {
        return ((state == SERVICE_UNINITIALIZED) || (state == SERVICE_STARTED) ||
        (state == SERVICE_STOPPED));
    }
}
