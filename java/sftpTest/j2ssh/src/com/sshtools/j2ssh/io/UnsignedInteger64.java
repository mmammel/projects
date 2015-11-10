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

import java.io.*;

import java.math.*;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class UnsignedInteger64 extends Number implements Serializable,
    Comparable {
    final static long serialVersionUID = 200;

    /**  */
    public final static BigInteger MAX_VALUE = new BigInteger(
            "18446744073709551615");

    /**  */
    public final static BigInteger MIN_VALUE = new BigInteger("0");
    private BigInteger bigInt;

    /**
     * Creates a new UnsignedInteger64 object.
     *
     * @param sval
     *
     * @throws NumberFormatException
     */
    public UnsignedInteger64(String sval) throws NumberFormatException {
        bigInt = new BigInteger(sval);

        if ((bigInt.compareTo(MIN_VALUE) < 0) ||
                (bigInt.compareTo(MAX_VALUE) > 0)) {
            throw new NumberFormatException();
        }
    }

    /**
     * Creates a new UnsignedInteger64 object.
     *
     * @param bval
     *
     * @throws NumberFormatException
     */
    public UnsignedInteger64(byte[] bval) throws NumberFormatException {
        bigInt = new BigInteger(bval);

        if ((bigInt.compareTo(MIN_VALUE) < 0) ||
                (bigInt.compareTo(MAX_VALUE) > 0)) {
            throw new NumberFormatException();
        }
    }

    /**
     * Creates a new UnsignedInteger64 object.
     *
     * @param input
     *
     * @throws NumberFormatException
     */
    public UnsignedInteger64(BigInteger input) {
        bigInt = new BigInteger(input.toString());

        if ((bigInt.compareTo(MIN_VALUE) < 0) ||
                (bigInt.compareTo(MAX_VALUE) > 0)) {
            throw new NumberFormatException();
        }
    }

    /**
     *
     *
     * @param o
     *
     * @return
     */
    public boolean equals(Object o) {
        try {
            UnsignedInteger64 u = (UnsignedInteger64) o;

            return u.bigInt.equals(this.bigInt);
        } catch (ClassCastException ce) {
            // This was not an UnsignedInt64, so equals should fail.
            return false;
        }
    }

    /**
     *
     *
     * @return
     */
    public BigInteger bigIntValue() {
        return bigInt;
    }

    /**
     *
     *
     * @return
     */
    public int intValue() {
        return bigInt.intValue();
    }

    /**
     *
     *
     * @return
     */
    public long longValue() {
        return bigInt.longValue();
    }

    /**
     *
     *
     * @return
     */
    public double doubleValue() {
        return bigInt.doubleValue();
    }

    /**
     *
     *
     * @return
     */
    public float floatValue() {
        return bigInt.floatValue();
    }

    /**
     *
     *
     * @param val
     *
     * @return
     */
    public int compareTo(Object val) {
        return bigInt.compareTo(((UnsignedInteger64) val).bigInt);
    }

    /**
     *
     *
     * @return
     */
    public String toString() {
        return bigInt.toString();
    }

    /**
     *
     *
     * @return
     */
    public int hashCode() {
        return bigInt.hashCode();
    }

    /**
     *
     *
     * @param x
     * @param y
     *
     * @return
     */
    public static UnsignedInteger64 add(UnsignedInteger64 x, UnsignedInteger64 y) {
        return new UnsignedInteger64(x.bigInt.add(y.bigInt));
    }

    /**
     *
     *
     * @param x
     * @param y
     *
     * @return
     */
    public static UnsignedInteger64 add(UnsignedInteger64 x, int y) {
        return new UnsignedInteger64(x.bigInt.add(BigInteger.valueOf(y)));
    }
}
