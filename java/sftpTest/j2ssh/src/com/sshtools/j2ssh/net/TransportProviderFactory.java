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
package com.sshtools.j2ssh.net;

import com.sshtools.j2ssh.configuration.SshConnectionProperties;

import java.io.IOException;

import java.net.UnknownHostException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.15 $
 */
public class TransportProviderFactory {
    /**
     *
     *
     * @param properties
     * @param socketTimeout
     *
     * @return
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public static TransportProvider connectTransportProvider(
        SshConnectionProperties properties /*, int connectTimeout*/,
        int socketTimeout) throws UnknownHostException, IOException {
        if (properties.getTransportProvider() == SshConnectionProperties.USE_HTTP_PROXY) {
            return HttpProxySocketProvider.connectViaProxy(properties.getHost(),
                properties.getPort(), properties.getProxyHost(),
                properties.getProxyPort(), properties.getProxyUsername(),
                properties.getProxyPassword(), "J2SSH");
        } else if (properties.getTransportProvider() == SshConnectionProperties.USE_SOCKS4_PROXY) {
            return SocksProxySocket.connectViaSocks4Proxy(properties.getHost(),
                properties.getPort(), properties.getProxyHost(),
                properties.getProxyPort(), properties.getProxyUsername());
        } else if (properties.getTransportProvider() == SshConnectionProperties.USE_SOCKS5_PROXY) {
            return SocksProxySocket.connectViaSocks5Proxy(properties.getHost(),
                properties.getPort(), properties.getProxyHost(),
                properties.getProxyPort(), properties.getProxyUsername(),
                properties.getProxyPassword());
        } else {
            // No proxy just attempt a standard socket connection

            /*SocketTransportProvider socket = new SocketTransportProvider();
             socket.setSoTimeout(socketTimeout);
             socket.connect(new InetSocketAddress(properties.getHost(),
                                     properties.getPort()),
               connectTimeout);*/
            SocketTransportProvider socket = new SocketTransportProvider(properties.getHost(),
                    properties.getPort());
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(socketTimeout);

            return socket;
        }
    }
}
