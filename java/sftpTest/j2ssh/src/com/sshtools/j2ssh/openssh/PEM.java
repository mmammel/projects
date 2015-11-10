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
package com.sshtools.j2ssh.openssh;

import java.io.*;

import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class PEM {
    /**  */
    public final static String DSA_PRIVATE_KEY = "DSA PRIVATE KEY";

    /**  */
    public final static String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";

    /**  */
    protected final static String PEM_BOUNDARY = "-----";

    /**  */
    protected final static String PEM_BEGIN = PEM_BOUNDARY + "BEGIN ";

    /**  */
    protected final static String PEM_END = PEM_BOUNDARY + "END ";

    /**  */
    protected final static int MAX_LINE_LENGTH = 75;

    /**  */
    protected final static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private final static int MD5_HASH_BYTES = 0x10;

    /**
     *
     *
     * @param passphrase
     * @param iv
     * @param keySize
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Error
     */
    protected static SecretKey getKeyFromPassphrase(String passphrase,
        byte[] iv, int keySize)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] passphraseBytes;

        try {
            passphraseBytes = passphrase.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new Error(
                "Mandatory US-ASCII character encoding is not supported by the VM");
        }

        /*
           hash is MD5
           h(0) <- hash(passphrase, iv);
           h(n) <- hash(h(n-1), passphrase, iv);
           key <- (h(0),...,h(n))[0,..,key.length];
         */
        MessageDigest hash = MessageDigest.getInstance("MD5");
        byte[] key = new byte[keySize];
        int hashesSize = keySize & 0xfffffff0;

        if ((keySize & 0xf) != 0) {
            hashesSize += MD5_HASH_BYTES;
        }

        byte[] hashes = new byte[hashesSize];
        byte[] previous;

        for (int index = 0; (index + MD5_HASH_BYTES) <= hashes.length;
                hash.update(previous, 0, previous.length)) {
            hash.update(passphraseBytes, 0, passphraseBytes.length);
            hash.update(iv, 0, iv.length);
            previous = hash.digest();
            System.arraycopy(previous, 0, hashes, index, previous.length);
            index += previous.length;
        }

        System.arraycopy(hashes, 0, key, 0, key.length);

        return new SecretKeySpec(key, "DESede");
    }
}
