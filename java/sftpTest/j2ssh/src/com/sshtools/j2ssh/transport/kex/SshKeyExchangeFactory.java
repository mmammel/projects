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
package com.sshtools.j2ssh.transport.kex;

import com.sshtools.j2ssh.configuration.ConfigurationException;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.configuration.ExtensionAlgorithm;
import com.sshtools.j2ssh.configuration.SshAPIConfiguration;
import com.sshtools.j2ssh.transport.AlgorithmNotSupportedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.28 $
 */
public class SshKeyExchangeFactory {
    private static Map kexs;
    private static String defaultAlgorithm;
    private static Log log = LogFactory.getLog(SshKeyExchangeFactory.class);

    static {
        kexs = new HashMap();
        log.info("Loading key exchange methods");
        kexs.put("diffie-hellman-group1-sha1", DhGroup1Sha1.class);

        try {
            // Load external compression from configuration file
            if (ConfigurationLoader.isConfigurationAvailable(
                        SshAPIConfiguration.class)) {
                SshAPIConfiguration config = (SshAPIConfiguration) ConfigurationLoader.getConfiguration(SshAPIConfiguration.class);

                if (config != null) {
                    List list = config.getKeyExchangeExtensions();

                    if (list != null) {
                        Iterator it = list.iterator();

                        while (it.hasNext()) {
                            ExtensionAlgorithm algorithm = (ExtensionAlgorithm) it.next();
                            String name = algorithm.getAlgorithmName();

                            if (kexs.containsKey(name)) {
                                log.debug("Standard key exchange " + name +
                                    " is being overidden by " +
                                    algorithm.getImplementationClass());
                            } else {
                                log.debug(algorithm.getAlgorithmName() +
                                    " key exchange is implemented by " +
                                    algorithm.getImplementationClass());
                            }

                            try {
                                kexs.put(algorithm.getAlgorithmName(),
                                    ConfigurationLoader.getExtensionClass(
                                        algorithm.getImplementationClass()));
                            } catch (ClassNotFoundException cnfe) {
                                log.error("Could not locate " +
                                    algorithm.getImplementationClass());
                            }
                        }
                    }

                    defaultAlgorithm = config.getDefaultKeyExchange();
                }
            }
        } catch (ConfigurationException ex) {
        }

        if ((defaultAlgorithm == null) || !kexs.containsKey(defaultAlgorithm)) {
            log.debug(
                "The default key exchange is not set! using first in list");

            Iterator it = kexs.keySet().iterator();
            defaultAlgorithm = (String) it.next();
        }
    }

    /**
     * Creates a new SshKeyExchangeFactory object.
     */
    protected SshKeyExchangeFactory() {
    }

    /**
     *
     */
    public static void initialize() {
    }

    /**
     *
     *
     * @return
     */
    public static String getDefaultKeyExchange() {
        return defaultAlgorithm;
    }

    /**
     *
     *
     * @return
     */
    public static List getSupportedKeyExchanges() {
        return new ArrayList(kexs.keySet());
    }

    /**
     *
     *
     * @param methodName
     *
     * @return
     *
     * @throws AlgorithmNotSupportedException
     */
    public static SshKeyExchange newInstance(String methodName)
        throws AlgorithmNotSupportedException {
        try {
            return (SshKeyExchange) ((Class) kexs.get(methodName)).newInstance();
        } catch (Exception e) {
            throw new AlgorithmNotSupportedException(methodName +
                " is not supported!");
        }
    }
}
