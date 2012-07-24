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

import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSName;

public class GSSCredentialImpl implements GSSCredential {
    
    public void dispose() throws GSSException {
        // TODO
    }
    
    public GSSName getName() throws GSSException {
        // TODO
        return null;
    }
    
    public GSSName getName(Oid mechOID) throws GSSException {
        // TODO
        return null;
    }
    
    public int getRemainingLifetime() throws GSSException {
        // TODO
        return 0;
    }
    
    public int getRemainingInitLifetime(Oid mech) throws GSSException {
        // TODO
        return 0;
    }
    
    public int getRemainingAcceptLifetime(Oid mech) throws GSSException {
        // TODO
        return 0;
    }
    
    public int getUsage() throws GSSException {
        // TODO
        return 0;
    }
    
    public int getUsage(Oid mechOID) throws GSSException {
        // TODO
        return 0;
    }
    
    public Oid[] getMechs() throws GSSException {
        // TODO
        return null;
    }
    
    public void add(GSSName aName, int initLifetime, int acceptLifetime,
            Oid mech, int usage) throws GSSException {
        // TODO
    }
    
    public boolean equals(Object another) {
        // TODO
        return false;
    }

}

