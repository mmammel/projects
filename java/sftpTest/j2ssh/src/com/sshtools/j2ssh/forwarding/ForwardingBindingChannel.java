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
package com.sshtools.j2ssh.forwarding;

import com.sshtools.j2ssh.connection.BindingChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import java.net.InetSocketAddress;
import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.12 $
 */
public class ForwardingBindingChannel extends BindingChannel
    implements ForwardingChannel {
    private static Log log = LogFactory.getLog(ForwardingSocketChannel.class);
    private ForwardingChannelImpl channel;

    /**
     * Creates a new ForwardingBindingChannel object.
     *
     * @param forwardType
     * @param hostToConnectOrBind
     * @param portToConnectOrBind
     * @param originatingHost
     * @param originatingPort
     *
     * @throws ForwardingConfigurationException
     */
    public ForwardingBindingChannel(String forwardType, String name,
        
    /*ForwardingConfiguration config,*/
    String hostToConnectOrBind, int portToConnectOrBind,
        String originatingHost, int originatingPort)
        throws ForwardingConfigurationException {
        if (!forwardType.equals(LOCAL_FORWARDING_CHANNEL) &&
                !forwardType.equals(REMOTE_FORWARDING_CHANNEL) &&
                !forwardType.equals(X11_FORWARDING_CHANNEL)) {
            throw new ForwardingConfigurationException(
                "The forwarding type is invalid");
        }

        channel = new ForwardingChannelImpl(forwardType, name,
                hostToConnectOrBind, portToConnectOrBind, originatingHost,
                originatingPort);
    }

    public String getName() {
        return channel.getName();
    }

    /**
     *
     *
     * @return
     */
    public byte[] getChannelOpenData() {
        return channel.getChannelOpenData();
    }

    /**
     *
     *
     * @return
     */
    public byte[] getChannelConfirmationData() {
        return channel.getChannelConfirmationData();
    }

    /**
     *
     *
     * @return
     */
    public String getChannelType() {
        return channel.getChannelType();
    }

    /**
     *
     *
     * @return
     */
    protected int getMinimumWindowSpace() {
        return 32768;
    }

    /**
     *
     *
     * @return
     */
    protected int getMaximumWindowSpace() {
        return 131072;
    }

    /**
     *
     *
     * @return
     */
    protected int getMaximumPacketSize() {
        return 32768;
    }

    /**
     *
     *
     * @return
     */
    public String getOriginatingHost() {
        return channel.getOriginatingHost();
    }

    /**
     *
     *
     * @return
     */
    public int getOriginatingPort() {
        return channel.getOriginatingPort();
    }

    /**
     *
     *
     * @return
     */
    public String getHostToConnectOrBind() {
        return channel.getHostToConnectOrBind();
    }

    /**
     *
     *
     * @return
     */
    public int getPortToConnectOrBind() {
        return channel.getPortToConnectOrBind();
    }

    /**
     *
     *
     * @param request
     * @param wantReply
     * @param requestData
     *
     * @throws IOException
     */
    protected void onChannelRequest(String request, boolean wantReply,
        byte[] requestData) throws IOException {
        connection.sendChannelRequestFailure(this);
    }
}
