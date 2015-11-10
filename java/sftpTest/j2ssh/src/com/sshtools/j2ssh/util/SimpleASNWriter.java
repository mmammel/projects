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
package com.sshtools.j2ssh.util;

import java.io.*;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.12 $
 */
public class SimpleASNWriter {
    private ByteArrayOutputStream data;

    /**
     * Creates a new SimpleASNWriter object.
     */
    public SimpleASNWriter() {
        this.data = new ByteArrayOutputStream();
    }

    /**
     *
     *
     * @param b
     */
    public void writeByte(int b) {
        data.write(b);
    }

    /**
     *
     *
     * @param b
     */
    public void writeData(byte[] b) {
        writeLength(b.length);
        this.data.write(b, 0, b.length);
    }

    /**
     *
     *
     * @param length
     */
    public void writeLength(int length) {
        if (length < 0x80) {
            data.write(length);
        } else {
            if (length < 0x100) {
                data.write(0x81);
                data.write(length);
            } else if (length < 0x10000) {
                data.write(0x82);
                data.write(length >>> 8);
                data.write(length);
            } else if (length < 0x1000000) {
                data.write(0x83);
                data.write(length >>> 16);
                data.write(length >>> 8);
                data.write(length);
            } else {
                data.write(0x84);
                data.write(length >>> 24);
                data.write(length >>> 16);
                data.write(length >>> 8);
                data.write(length);
            }
        }
    }

    /**
     *
     *
     * @return
     */
    public byte[] toByteArray() {
        return data.toByteArray();
    }
}
