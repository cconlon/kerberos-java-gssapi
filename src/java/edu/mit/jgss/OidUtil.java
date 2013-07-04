/* 
 * Copyright (C) 2012 by the Massachusetts Institute of Technology.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mit.jgss;

import java.io.*;
import java.util.regex.*;

/**
 * Contains utility functions for Oid object conversions.
 */
public class OidUtil {

    /**
     * Converts a DER-encoded Oid from a byte array to a dot-separated
     * String representation.
     *
     * @param oid DER-encoded Oid byte array
     * @return Oid in dot-separated String representation
     *
     */
    public static String OidDer2String(byte[] oid) {
        // TODO - not necessary, but might be useful in the future?
        return null;
    }

    /**
     * Converts a DER-encoded Oid from an InputStream to a dot-separated
     * String representation.
     *
     * @param oid DER-encoded Oid InputStream
     * @return Oid in dot-separated String representation
     *
     */
    public static byte[] OidStream2DER(InputStream oid) throws IOException {
        
        int tag;
        int length;
        ByteArrayOutputStream outOid = new ByteArrayOutputStream();

        try {
            tag = oid.read();
            length = oid.read();
            outOid.write(tag);
            outOid.write(length);

            for (int i = 0; i < length; i++) {
                outOid.write(oid.read());
            }
        } catch (IOException e) {
            throw new IOException("I/O Error occurred when reading InputStream");
        }

        return outOid.toByteArray();
    }

    /**
     * Converts an Oid in dot-separated string representation to a
     * DER-encoded byte array.
     *
     * @param oid Oid in dot-separated String representation
     * @return DER-encoded Oid byte array
     *
     */
    public static byte[] OidString2DER(String oid) {
       
        int octet = 0; 
        int base = 0;
        int times = 0;
        int tbMaj = 0;

        ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
        ByteArrayOutputStream tmpArray = new ByteArrayOutputStream();

        /* Convert String to int[] */
        String[] tmp = oid.split("\\.");
        int[] input = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++ ) {
            input[i] = Integer.parseInt(tmp[i]);
        }

        /* Add tag for OID (0x06) */
        bytearray.write(6);

        /* Calculate first byte */
        tmpArray.write((input[0]*40) + input[1]);

        /* Encode the rest of the OID nodes in DER format */
        for (int j = 2; j < input.length; j++) {

            if (input[j] <= 127) {

                /* Encode directly */
                tmpArray.write(input[j]);

            } else if (input[j] > 127) {

                /* Reset variables */
                octet = input[j];
                base = 128;
                times = 0;
                tbMaj = 8;

                /* If bigger than 16383 */
                if (octet > 16383) {

                    base = 262144;
                    times = (int) Math.floor(octet / base);
                    tbMaj = 8 + times;
                    octet = octet - (times * base);

                    base = 16384;
                    times = (int) Math.floor(octet / base);
                    tmpArray.write((16*tbMaj) + times);

                    /* Reset tbMaj in case we're skipping next if */
                    tbMaj = 8;
                }

                /* 2047 < octet <= 16383 */
                if (octet > 2047 && octet <= 16383) {

                    base = 2048;
                    times = (int) Math.floor(octet / base);
                    tbMaj = 8 + times;
                    octet = octet - (times * base);
                }

                /* 127 < octet < 2047 */
                base = 128;
                times = (int) Math.floor(octet / base);
                tmpArray.write((16 * tbMaj) + times);
                tmpArray.write(octet - (times * 128));
            }
        }

        byte[] convArray = tmpArray.toByteArray();
        bytearray.write(convArray.length);
        for (int k = 0; k < convArray.length; k++) {
            bytearray.write(convArray[k]);
        }

        return bytearray.toByteArray();

    } /* end OidString2DER */

    /**
     * Verifies that an OID is in dot-separated string format.
     * TODO: Make this more robust.
     *
     * @param oid OID to verify
     * @return true if OID is valid, false otherwise
     */
    public static boolean verifyOid(String oid) {
        
        // Pattern to match '.' separated Oid string format
        Pattern oidPattern = Pattern.compile("^([0-9]+.{1})+[0-9]+$");
        Matcher oidIN = oidPattern.matcher(oid);
        
        if (oidIN.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Verifies DER-encoded byte array has a valid tag and length
     *
     * @param oid OID to verify, DER-encoded byte array
     * @return true if OID is valid, false otherwise
     */
    public static boolean verifyOid(byte[] oid) {
       
        /* verify tag is set to an OID (0x06) */
        if (oid[0] != 6)
            return false;

        /* verify length is correct */
        if (oid.length-2 != oid[1]) {
            return false;
        }
        
        return true;
    }

}
