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

import org.ietf.jgss.GSSException;
import edu.mit.jgss.swig.*;
import java.io.*;

/*
 *  Utility functions for DER encodings
 */
public class DerUtil {

    /*
     * Remove the length header from a GSS token. Doing this removes
     * the bytes from our InputStream, so we save them in the DerHeader
     * object in order to be able to pass them back to the underlying
     * native GSSAPI implementation.
     */ 
    public static DerHeader getHeader(InputStream in) 
        throws GSSException, IOException {

        int length = 0;
        int tag, b;
        ByteArrayOutputStream headBytes = new ByteArrayOutputStream();
        DerHeader header = new DerHeader();

        try {
            tag = in.read();
            headBytes.write(tag);
            if (tag != 0x60) {
                throw new GSSExceptionImpl(gsswrapper.GSS_S_DEFECTIVE_TOKEN, 0);
            }

            b = in.read();
            headBytes.write(b);
            if (b >= 0x80) {
                int bytes = b & 0x7F;
            
                while ((bytes--) > 0) {
                    b = in.read();
                    headBytes.write(b);
                    length = (length << 8) | b;
                }
            } else {
                length = b;
            }

        } catch (IOException e) {
            throw new IOException("I/O Error occurred when reading InputStream");
        }

        header.setLength(length);
        header.setBytes(headBytes.toByteArray());

        return header;

    }
    
    /*
     * Print a byte[], for debug purposes.
     */ 
    public static void printByteArray(byte[] input)
    {
        for (int i = 0; i < input.length; i++ ) {
            System.out.format("%02X ", input[i]);
        }
        System.out.println();
    }

}

