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

import com.sshtools.j2ssh.io.UnsignedInteger32;
import com.sshtools.j2ssh.subsystem.SubsystemMessage;
import com.sshtools.j2ssh.subsystem.SubsystemMessageStore;
import com.sshtools.j2ssh.util.OpenClosedState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;


class SftpMessageStore extends SubsystemMessageStore {
    /**  */
    public static Log log = LogFactory.getLog(SftpMessageStore.class);

    /**
     * Creates a new SftpMessageStore object.
     */
    public SftpMessageStore() {
    }

    /**
     *
     *
     * @param requestId
     *
     * @return
     *
     * @throws InterruptedException
     */
    public synchronized SubsystemMessage getMessage(UnsignedInteger32 requestId)
        throws InterruptedException {
        Iterator it;
        SubsystemMessage msg;

        // If there ae no messages available then wait untill there are.
        while (getState().getValue() == OpenClosedState.OPEN) {
            if (messages.size() > 0) {
                it = messages.iterator();

                while (it.hasNext()) {
                    msg = (SubsystemMessage) it.next();

                    if (msg instanceof MessageRequestId) {
                        if (((MessageRequestId) msg).getId().equals(requestId)) {
                            messages.remove(msg);

                            return msg;
                        }
                    }
                }
            }

            log.debug("Waiting for new messages");
            wait(5000);
        }

        return null;
    }
}
