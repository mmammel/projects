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

import com.sshtools.j2ssh.io.ByteArrayWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InterruptedIOException;

import java.net.Socket;
import java.net.SocketException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public abstract class SocketChannel extends Channel {
    private static Log log = LogFactory.getLog(SocketChannel.class);

    /**  */
    protected Socket socket = null;
    Thread thread;

    /**
     *
     *
     * @param socket
     *
     * @throws IOException
     */
    public void bindSocket(Socket socket) throws IOException {
        if (state.getValue() == ChannelState.CHANNEL_UNINITIALIZED) {
            this.socket = socket;
        } else {
            throw new IOException(
                "The socket can only be bound to an unitialized channel");
        }
    }

    /**
     *
     *
     * @param msg
     *
     * @throws IOException
     */
    protected void onChannelData(SshMsgChannelData msg)
        throws IOException {
        try {
            socket.getOutputStream().write(msg.getChannelData());
        } catch (IOException ex) {
        }
    }

    /**
     *
     *
     * @throws IOException
     */
    protected void onChannelEOF() throws IOException {
        try {
            //synchronized(state) {
            //if (isOpen())
            //  close();
            socket.shutdownOutput();

            // }
        } catch (IOException ex) {
            log.info(
                "Failed to shutdown Socket OutputStream in response to EOF event: " +
                ex.getMessage());
        }
    }

    /**
     *
     *
     * @throws IOException
     */
    protected void onChannelClose() throws IOException {
        try {
            socket.close();
        } catch (IOException ex) {
            log.info("Failed to close socket on channel close event: " +
                ex.getMessage());
        }
    }

    /**
     *
     *
     * @throws IOException
     */
    protected void onChannelOpen() throws IOException {
        if (socket == null) {
            throw new IOException(
                "The socket must be bound to the channel before opening");
        }

        thread = new Thread(new SocketReader());
        thread.start();
    }

    /**
     *
     *
     * @param msg
     *
     * @throws IOException
     */
    protected void onChannelExtData(SshMsgChannelExtendedData msg)
        throws IOException {
        // We do not have an extended data channel for the socket so ignore
    }

    class SocketReader implements Runnable {
        public void run() {
            byte[] buffer = new byte[getMaximumPacketSize()];
            ByteArrayWriter baw = new ByteArrayWriter();

            try {
                socket.setSoTimeout(2000);
            } catch (SocketException ex2) {
            }

            try {
                int read = 0;

                while ((read >= 0) && !isClosed()) {
                    try {
                        read = socket.getInputStream().read(buffer);
                    } catch (InterruptedIOException ex1) {
                        read = ex1.bytesTransferred;
                    }

                    synchronized (state) {
                        if (isClosed() || isLocalEOF()) {
                            break;
                        }

                        if (read > 0) {
                            baw.write(buffer, 0, read);
                            sendChannelData(baw.toByteArray());
                            baw.reset();
                        }
                    }
                }
            } catch (IOException ex) {
                // Break out of the while loop
            }

            try {
                synchronized (state) {
                    if (!isLocalEOF()) {
                        setLocalEOF();
                    }

                    if (isOpen()) {
                        close();
                    }
                }
            } catch (Exception ex) {
                log.info("Failed to send channel EOF message: " +
                    ex.getMessage());
            }

            thread = null;
        }
    }
}
