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
package com.sshtools.j2ssh.authentication;

import com.sshtools.j2ssh.SshException;
import com.sshtools.j2ssh.transport.MessageNotAvailableException;
import com.sshtools.j2ssh.transport.MessageStoreEOFException;
import com.sshtools.j2ssh.transport.Service;
import com.sshtools.j2ssh.transport.SshMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.27 $
 */
public class AuthenticationProtocolClient extends Service {
    private static Log log = LogFactory.getLog(AuthenticationProtocolClient.class);
    private int[] resultFilter = new int[2];
    private int[] singleIdFilter = new int[3];
    private Vector listeners = new Vector();

    /**
     * Creates a new AuthenticationProtocolClient object.
     */
    public AuthenticationProtocolClient() {
        super("ssh-userauth");
        resultFilter[0] = SshMsgUserAuthSuccess.SSH_MSG_USERAUTH_SUCCESS;
        resultFilter[1] = SshMsgUserAuthFailure.SSH_MSG_USERAUTH_FAILURE;
        singleIdFilter[0] = SshMsgUserAuthSuccess.SSH_MSG_USERAUTH_SUCCESS;
        singleIdFilter[1] = SshMsgUserAuthFailure.SSH_MSG_USERAUTH_FAILURE;
    }

    /**
     *
     *
     * @throws java.io.IOException
     */
    protected void onServiceAccept() throws java.io.IOException {
    }

    /**
     *
     */
    protected void onStart() {
    }

    /**
     *
     *
     * @param startMode
     *
     * @throws java.io.IOException
     * @throws IOException
     */
    protected void onServiceInit(int startMode) throws java.io.IOException {
        if (startMode == Service.ACCEPTING_SERVICE) {
            throw new IOException(
                "The Authentication Protocol client cannot be accepted");
        }

        messageStore.registerMessage(SshMsgUserAuthFailure.SSH_MSG_USERAUTH_FAILURE,
            SshMsgUserAuthFailure.class);
        messageStore.registerMessage(SshMsgUserAuthSuccess.SSH_MSG_USERAUTH_SUCCESS,
            SshMsgUserAuthSuccess.class);
        messageStore.registerMessage(SshMsgUserAuthBanner.SSH_MSG_USERAUTH_BANNER,
            SshMsgUserAuthBanner.class);

        //messageStore.registerMessage(SshMsgUserAuthPwdChangeReq.SSH_MSG_USERAUTH_PWD_CHANGEREQ,
        //    SshMsgUserAuthPwdChangeReq.class);
    }

    /**
     *
     *
     * @throws java.io.IOException
     * @throws IOException
     */
    protected void onServiceRequest() throws java.io.IOException {
        throw new IOException("This class implements the client protocol only!");
    }

    /**
     *
     *
     * @param listener
     */
    public void addEventListener(AuthenticationProtocolListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     *
     *
     * @param username
     * @param serviceName
     *
     * @return
     *
     * @throws IOException
     * @throws SshException
     */
    public List getAvailableAuths(String username, String serviceName)
        throws IOException {
        log.info("Requesting authentication methods");

        SshMessage msg = new SshMsgUserAuthRequest(username, serviceName,
                "none", null);
        transport.sendMessage(msg, this);

        try {
            msg = messageStore.getMessage(resultFilter);
        } catch (InterruptedException ex) {
            throw new SshException(
                "The thread was interrupted whilst waiting for an authentication message");
        }

        if (msg instanceof SshMsgUserAuthFailure) {
            return ((SshMsgUserAuthFailure) msg).getAvailableAuthentications();
        } else {
            throw new IOException(
                "None request returned success! Insecure feature not supported");
        }
    }

    /**
     *
     *
     * @param auth
     * @param serviceToStart
     *
     * @return
     *
     * @throws IOException
     * @throws SshException
     */
    public int authenticate(SshAuthenticationClient auth, Service serviceToStart)
        throws IOException {
        try {
            if (!auth.canAuthenticate() && auth.canPrompt()) {
                SshAuthenticationPrompt prompt = auth.getAuthenticationPrompt();

                if (!prompt.showPrompt(auth)) {
                    return AuthenticationProtocolState.CANCELLED;
                }
            }

            auth.authenticate(this, serviceToStart.getServiceName());

            SshMessage msg = parseMessage(messageStore.getMessage(resultFilter));

            // We should not get this far
            throw new AuthenticationProtocolException(
                "Unexpected authentication message " + msg.getMessageName());
        } catch (TerminatedStateException tse) {
            if (tse.getState() == AuthenticationProtocolState.COMPLETE) {
                serviceToStart.init(Service.ACCEPTING_SERVICE, transport); //, nativeSettings);
                serviceToStart.start();

                for (Iterator it = listeners.iterator(); it.hasNext();) {
                    AuthenticationProtocolListener listener = (AuthenticationProtocolListener) it.next();

                    if (listener != null) {
                        listener.onAuthenticationComplete();
                    }
                }
            }

            return tse.getState();
        } catch (InterruptedException ex) {
            throw new SshException(
                "The thread was interrupted whilst waiting for an authentication message");
        }
    }

    /**
     *
     *
     * @param msg
     *
     * @throws IOException
     */
    public void sendMessage(SshMessage msg) throws IOException {
        transport.sendMessage(msg, this);
    }

    /**
     *
     *
     * @return
     */
    public byte[] getSessionIdentifier() {
        return transport.getSessionIdentifier();
    }

    /**
     *
     *
     * @param cls
     * @param messageId
     */
    public void registerMessage(Class cls, int messageId) {
        messageStore.registerMessage(messageId, cls);
    }

    /**
     *
     *
     * @param messageId
     *
     * @return
     *
     * @throws TerminatedStateException
     * @throws IOException
     */
    public SshMessage readMessage(int messageId)
        throws TerminatedStateException, IOException {
        singleIdFilter[2] = messageId;

        return internalReadMessage(singleIdFilter);
    }

    private SshMessage internalReadMessage(int[] messageIdFilter)
        throws TerminatedStateException, IOException {
        try {
            SshMessage msg = messageStore.getMessage(messageIdFilter);

            return parseMessage(msg);
        } catch (MessageStoreEOFException meof) {
            throw new AuthenticationProtocolException("Failed to read messages");
        } catch (InterruptedException ex) {
            throw new SshException(
                "The thread was interrupted whilst waiting for an authentication message");
        }
    }

    /**
     *
     *
     * @param messageId
     *
     * @return
     *
     * @throws TerminatedStateException
     * @throws IOException
     */
    public SshMessage readMessage(int[] messageId)
        throws TerminatedStateException, IOException {
        int[] messageIdFilter = new int[messageId.length + resultFilter.length];
        System.arraycopy(resultFilter, 0, messageIdFilter, 0,
            resultFilter.length);
        System.arraycopy(messageId, 0, messageIdFilter, resultFilter.length,
            messageId.length);

        return internalReadMessage(messageIdFilter);
    }

    /**
     *
     *
     * @throws IOException
     * @throws TerminatedStateException
     */
    public void readAuthenticationState()
        throws IOException, TerminatedStateException {
        internalReadMessage(resultFilter);
    }

    private SshMessage parseMessage(SshMessage msg)
        throws TerminatedStateException {
        if (msg instanceof SshMsgUserAuthFailure) {
            if (((SshMsgUserAuthFailure) msg).getPartialSuccess()) {
                throw new TerminatedStateException(AuthenticationProtocolState.PARTIAL);
            } else {
                throw new TerminatedStateException(AuthenticationProtocolState.FAILED);
            }
        } else if (msg instanceof SshMsgUserAuthSuccess) {
            throw new TerminatedStateException(AuthenticationProtocolState.COMPLETE);
        } else {
            return msg;
        }
    }

    /**
     *
     *
     * @param timeout
     *
     * @return
     *
     * @throws IOException
     * @throws SshException
     */
    public String getBannerMessage(int timeout) throws IOException {
        try {
            log.debug(
                "getBannerMessage is attempting to read the authentication banner");

            SshMessage msg = messageStore.peekMessage(SshMsgUserAuthBanner.SSH_MSG_USERAUTH_BANNER,
                    timeout);

            return ((SshMsgUserAuthBanner) msg).getBanner();
        } catch (MessageNotAvailableException e) {
            return "";
        } catch (MessageStoreEOFException eof) {
            log.error(
                "Failed to retreive banner becasue the message store is EOF");

            return "";
        } catch (InterruptedException ex) {
            throw new SshException(
                "The thread was interrupted whilst waiting for an authentication message");
        }
    }
}
