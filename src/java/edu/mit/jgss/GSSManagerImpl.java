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

import edu.mit.jgss.swig.*;

public class GSSManagerImpl extends GSSManager {

    /* MIT GSSAPI only supports the following two mechanisms:
       1) gss_mech_krb5         1.2.840.113554.1.2.2
       2) gss_mech_krb5_old     1.3.5.1.5.2
     */
    public Oid[] getMechs() {

        /* because this implementation doesn't currently support a 
           service provider framwork, this method just returns the
           mechanisms supported by MIT's GSSAPI, listed above */

        Oid[] mechs = new Oid[2];

        try {

            mechs[0] = new OidImpl("1.2.840.113554.1.2.2");
            mechs[1] = new OidImpl("1.3.5.1.5.2");

        } catch (GSSException e) {
            /* ignore and continue */
        }

        return mechs;
    }

    public Oid[] getNamesForMech(Oid mech) {
        
        long maj_status = 0;
        long[] min_status = {0};
        gss_OID_set_desc mech_names = new gss_OID_set_desc();
        gss_buffer_desc oid_name = new gss_buffer_desc();
        Oid[] mechNames;
        int numNames = 0;

        try {
            OidImpl tmpMech = new OidImpl(mech.toString());

            maj_status = gsswrapper.gss_inquire_names_for_mech(min_status,
                    tmpMech.getNativeOid(), mech_names);

            if (maj_status != gsswrapper.GSS_S_COMPLETE) {
                /* just return null since we can't throw an exception here */
                return null;
            }

            numNames = (int) mech_names.getCount();
            mechNames = new Oid[numNames];
            for (int i = 0; i < numNames; i++) {
                maj_status = gsswrapper.gss_oid_to_str(min_status,
                        mech_names.getElement(i), oid_name);
                mechNames[i] = new Oid(oid_name.getValue());
            }

            gsswrapper.gss_release_buffer(min_status, oid_name);
            gsswrapper.gss_release_oid_set(min_status, mech_names);
            tmpMech.freeOid();

        } catch (GSSException e) {
            return null;
        }

        return mechNames;
    }

    public Oid[] getMechsForName(Oid nameType) {

        long maj_status = 0;
        long[] min_status = {0};
        gss_OID_set_desc mech_types = new gss_OID_set_desc();
        gss_buffer_desc mech_name = new gss_buffer_desc();
        Oid[] mechs;
        int numMechs;

        try {

            /* create a gss_name_t_desc from the nametype we got */
            GSSNameImpl tmpName = (GSSNameImpl) createName("test", nameType);

            maj_status = gsswrapper.gss_inquire_mechs_for_name(min_status,
                    tmpName.getInternGSSName(), mech_types);

            if (maj_status != gsswrapper.GSS_S_COMPLETE) {
                /* just return null since we can't throw an exception here */
                return null;
            }

            numMechs = (int) mech_types.getCount();
            mechs = new Oid[numMechs];
            for (int i = 0; i < numMechs; i++) {
                maj_status = gsswrapper.gss_oid_to_str(min_status,
                        mech_types.getElement(i), mech_name);
                mechs[i] = new Oid(mech_name.getValue());
            }

            gsswrapper.gss_release_buffer(min_status, mech_name);
            gsswrapper.gss_release_oid_set(min_status, mech_types);
            tmpName.freeGSSName();
        
        } catch (GSSException e) {
            return null;
        }

        return mechs;
    }

    public GSSName createName(String nameStr, Oid nameType) 
        throws GSSException {

        GSSNameImpl newName = new GSSNameImpl();
        newName.importName(nameStr, nameType);
        return newName;

    }

    public GSSName createName(byte[] name, Oid nameType) 
        throws GSSException {

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

    public GSSName createName(byte[] name, Oid nameType, Oid mech) 
        throws GSSException {

        GSSName newName = createName(name, nameType);
        GSSName canonicalizedName = newName.canonicalize(mech);
        return canonicalizedName;
    
    }

    public GSSCredential createCredential(int usage) 
        throws GSSException {
       
        if (usage != GSSCredential.INITIATE_AND_ACCEPT ||
            usage != GSSCredential.INITIATE_ONLY ||
            usage != GSSCredential.ACCEPT_ONLY) {
            throw new GSSException(GSSException.FAILURE);
        } else { 
            GSSCredentialImpl newCred = new GSSCredentialImpl();
            newCred.acquireCred(usage);
            return newCred;
        }

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

        return new GSSContextImpl(peer, mech, myCred, lifetime);

    }

    public GSSContext createContext(GSSCredential myCred) 
        throws GSSException {

        return new GSSContextImpl(myCred);

    }

    public GSSContext createContext(byte[] interProcessToken)
        throws GSSException {

        return new GSSContextImpl(interProcessToken);

    }

    /* this gssapi implementation is wrapped around MIT Kerberos
       GSSAPI, which only supported Kerberos v5. Thus, at this time, 
       a service provider framework is not supported. RFC 5653 states that a 
       service provider framework is optional, and the implementation should 
       throw a GSSEXception with GSSException.UNAVAILABLE if not supported. */
    public void addProviderAtFront(Provider p, Oid mech)
        throws GSSException {
            throw new GSSException(GSSException.UNAVAILABLE);
    }

    public void addProviderAtEnd(Provider p, Oid mech)
        throws GSSException {
            throw new GSSException(GSSException.UNAVAILABLE);
    }

}
