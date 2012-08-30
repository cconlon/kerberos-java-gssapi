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

/*
 *  Simple class to hold information about DER headers
 */
public class DerHeader {

    private int length = 0;
    private byte[] headerBytes = null;

    public int getLength() {
        return length;
    }

    public byte[] getBytes() {
        byte[] tmp = headerBytes.clone();
        return tmp;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setBytes(byte[] hBytes) {
        headerBytes = new byte[hBytes.length];
        System.arraycopy(hBytes, 0, headerBytes, 0, hBytes.length);
    }

    public void appendBytes(byte[] append) {

        if (headerBytes == null) {
            headerBytes = new byte[append.length];
            System.arraycopy(append, 0, headerBytes, 0, append.length);
        } else {
            /* merge into tmp array */
            byte[] tmp = new byte[headerBytes.length + append.length];
            System.arraycopy(headerBytes, 0, tmp, 0, headerBytes.length);
            System.arraycopy(append, 0, tmp, headerBytes.length, append.length);

            /* copy back into object array */
            headerBytes = new byte[headerBytes.length + append.length];
            System.arraycopy(tmp, 0, headerBytes, 0, tmp.length);
        }

    }
}


