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

import java.security.Provider;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.GSSManager;

import edu.mit.jgss.swig.gsswrapper;

public class GSSManagerImpl extends GSSManager {
    
    public Oid[] getMechs() {
        // TODO
        return null;
    }

    public Oid[] getNamesForMech(Oid mech) {
        // TODO
        return null;
    }

    public Oid[] getMechsForName(Oid nameType) {
        // TODO
        return null;
    }

    public GSSName createName(String nameStr, Oid nameType) throws GSSException {

        GSSNameImpl newName = new GSSNameImpl();
        newName.importName(nameStr, nameType);
        return newName;

    }

    public GSSName createName(byte[] name, Oid nameType) throws GSSException {

        GSSNameImpl newName = new GSSNameImpl();
        newName.importName(name, nameType);
        return newName;

    }

    public GSSName createName(String nameStr, Oid nameType, Oid mech)
        throws GSSException {

        GSSName newName = createName(nameStr, nameType);
        GSSName canonicalizedName = newName.canonicalize(mech);
        return canonicalizedName;

    }

    public GSSName createName(byte[] name, Oid nameType, Oid mech) throws GSSException {

        GSSName newName = createName(name, nameType);
        GSSName canonicalizedName = newName.canonicalize(mech);
        return canonicalizedName;
    
    }

    public GSSCredential createCredential(int usage) throws GSSException {
        
        GSSCredentialImpl newCred = new GSSCredentialImpl();
        newCred.acquireCred(usage);
        return newCred;

    }

    public GSSCredential createCredential(GSSName aName, int lifetime,
        Oid mech, int usage) throws GSSException {

        GSSCredentialImpl newCred = new GSSCredentialImpl();
        
        Oid[] mechs = null;

        if (mech != null) {
            mechs = new Oid[1];
            mechs[0] = mech;
        }

        newCred.acquireCred(aName, lifetime, mechs, usage);
        return newCred;

    }

    public GSSCredential createCredential(GSSName aName, int lifetime,
        Oid[] mechs, int usage) throws GSSException {

        GSSCredentialImpl newCred = new GSSCredentialImpl();
        newCred.acquireCred(aName, lifetime, mechs, usage);
        return newCred;
    }

    public GSSContext createContext(GSSName peer, Oid mech, 
        GSSCredential myCred, int lifetime) throws GSSException {

        // TODO
        return null;
    }

    public GSSContext createContext(GSSCredential myCred) 
        throws GSSException {

        // TODO
        return null;
    }

    public GSSContext createContext(byte[] interProcessToken)
        throws GSSException {

        // TODO
        return null;
    }

    public void addProviderAtFront(Provider p, Oid mech)
        throws GSSException {
        // TODO
    }

    public void addProviderAtEnd(Provider p, Oid mech)
        throws GSSException {
        // TODO
    }

}
