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

import java.io.InputStream;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSException;

import edu.mit.jgss.swig.*;

public class OidImpl extends Oid {
    
    public OidImpl(String strOid) throws GSSException {
        super(strOid);
    }
    
    public OidImpl(InputStream derOid) throws GSSException {
        super(derOid);
    }
    
    public OidImpl(byte[] derOid) throws GSSException {
        super(derOid);
    }

    public gss_OID_desc getNativeOid() {
        return super.oid;
    }

    public void setNativeOid(gss_OID_desc newOid) {
        if (newOid != null) {
            super.oid = newOid;
            super.derOid = null;
        }
    }

    public void freeOid() {
        if (super.oid != null) {

            long maj_status = 0;
            long[] min_status = {0};

            maj_status = gsswrapper.gss_release_oid(min_status,
                    super.oid);
            }
    }
}


