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
package org.ietf.jgss;

import edu.mit.jgss.swig.*;

public class GSSException extends Exception {

    private static final long serialVersionUID = -5142354087553823574L;

    public static final int BAD_BINDINGS = 1;
    public static final int BAD_MECH = 2;
    public static final int BAD_NAME = 3;
    public static final int BAD_NAMETYPE = 4;
    public static final int BAD_STATUS = 5;
    public static final int BAD_MIC = 6;
    public static final int CONTEXT_EXPIRED = 7;
    public static final int CREDENTIALS_EXPIRED = 8;
    public static final int DEFECTIVE_CREDENTIAL = 9;
    public static final int DEFECTIVE_TOKEN = 10;
    public static final int FAILURE = 11;
    public static final int NO_CONTEXT = 12;
    public static final int NO_CRED = 13;
    public static final int BAD_QOP = 14;
    public static final int UNAUTHORIZED = 15;
    public static final int UNAVAILABLE = 16;
    public static final int DUPLICATE_ELEMENT = 17;
    public static final int NAME_NOT_MN = 18;
    public static final int DUPLICATE_TOKEN = 19;
    public static final int OLD_TOKEN = 20;
    public static final int UNSEQ_TOKEN = 21;
    public static final int GAP_TOKEN = 22;

    private int majCode;
    private int minCode;
    private String minString;
    private String majString;

    public GSSException(int majorCode) {
        super();
        this.majCode = majorCode;
    }

    public GSSException(int majorCode, int minorCode, String minorString) {
        super();
        this.majCode = majorCode;
        this.minCode = minorCode;
        this.minString = minorString;
    }

    public int getMajor() {
        return this.majCode;
    }

    public int getMinor() {
        return this.minCode;
    }

    public String getMajorString() {

        long[] msg_ctx = {0};
        long ret = 0;
        gss_buffer_desc storeBuff = new gss_buffer_desc();

        ret = gsswrapper.gss_display_status_wrap(minCode, majCode,
                gsswrapper.GSS_C_GSS_CODE,
                gsswrapper.GSS_C_NO_OID,
                msg_ctx, storeBuff);
        if (ret != gsswrapper.GSS_S_COMPLETE) {
            this.majString = null;
        } else {
            this.majString = storeBuff.getValue();
        }

        return this.majString;
    }

    public String getMinorString() {

        long[] msg_ctx = {0};
        long ret = 0;
        gss_buffer_desc storeBuff = new gss_buffer_desc();

        ret = gsswrapper.gss_display_status_wrap(majCode, minCode,
                gsswrapper.GSS_C_MECH_CODE,
                gsswrapper.GSS_C_NO_OID,
                msg_ctx, storeBuff);

        if (ret != gsswrapper.GSS_S_COMPLETE) {
            this.minString = null;
        } else {
            this.minString = storeBuff.getValue();
        }
        return this.minString;
    }

    public void setMinor(int minorCode, String message) {
        this.minCode = minorCode;
        this.minString = message;
    }

    @Override
    public String toString() {
        return "GSSException: " + getMessage();
    }

    @Override
    public String getMessage() {
        return "Major Status: (" + getMajor() + ", " + getMajorString() + "), " + "Minor Status: (" + getMinor() + ", " + getMinorString() + ")";
    }

}
