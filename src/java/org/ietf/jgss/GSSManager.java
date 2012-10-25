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

import java.security.Provider;

/**
 * Abstract class that serves as a factory for three GSS interfaces:
 * GSSName, GSSCredential, and GSSContext. It also provides methods for
 * applications to determine what mechanisms are available from the GSS
 * implementation and what name types these mechanisms support.
 * 
 * This abstract class is defined in section 7.1 of RFC 5653 
 * (http://tools.ietf.org/html/rfc5653).
 */
public abstract class GSSManager {

    /**
     * Returns the default GSSManager implementation.
     *
     * @return default GSSManager implementation.
     */
    public static GSSManager getInstance() {
        return new edu.mit.jgss.GSSManagerImpl();
    }

    /**
     * Returns an array of Oid objects indicating the mechanisms available
     * to GSS-API callers.
     *
     * @return array of Oid objects indicating the mechanisms available to
     * GSS-API callers.
     */
    public abstract Oid[] getMechs();

    /**
     * Returns name type Oids supported by the specified mechanism.
     *
     * @param mech Oid object for the mechanism to query.
     * @return name type Oids supported by mech.
     * @throws GSSException
     */
    public abstract Oid[] getNamesForMech(Oid mech)
            throws GSSException;

    /**
     * Returns array of Oid objects corresponding to the mechanisms that
     * support the specific name type. If no mechanisms are found, "null"
     * is returned.
     *
     * @param nameType Oid object for the name type.
     * @return array of Oid objects with the mechanisms supported by nameType.
     */
    public abstract Oid[] getMechsForName(Oid nameType);

    /**
     * Converts a contiguous string name from the specified namespace to a
     * GSSName object. In general, the GSSName object created will not be
     * a MN.
     *
     * @param nameStr string representing a printable form of the name to
     * create.
     * @param nameType Oid specifying the namespace of the printable name.
     * "null" can be used to specify that a mechanism-specific default
     * printable syntax should be assumed by each mechanism that examines
     * nameStr.
     * @return GSSName representation of the string name nameStr.
     * @throws GSSException
     */
    public abstract GSSName createName(String nameStr, Oid nameType)
            throws GSSException;

    /**
     * Converts a contiguous byte array containing a name from the specified
     * namespace to a GSSName object. In general, the GSSName object created
     * will not be a MN.
     *
     * @param name byte array containing the name to create.
     * @param nameType Oid specifying the namespace of the printable name.
     * "null" can be used to specify that a mechanism-specific default
     * printable syntax should be assumed by each mechanism that examines
     * nameStr.
     * @return GSSName representation of the string name represented in name.
     * @throws GSSException
     */
    public abstract GSSName createName(byte[] name, Oid nameType)
            throws GSSException;

    /**
     * Converts a contiguous string name from the specified namespace to a
     * GSSName object that is a mechanism name (MN). This method is equivalent
     * to calling createName() and GSSName.canonicalize().
     *
     * @param nameStr string representing a printable form of the name to 
     * create.
     * @param nameType Oid specifying the namespace of the printable name
     * supplied. "null" may be used to specify that a mechanism-specific
     * default printable syntax should be assumed when the mechanism
     * examines nameStr.
     * @param mech Oid specifying the mechanism for which this name
     * should be created.
     * @return GSSName representation of the string name, nameStr.
     * @throws GSSException
     */
    public abstract GSSName createName(String nameStr, Oid nameType,
            Oid mech) throws GSSException;

    /**
     * Converts a contiguous byte array containing a name from the
     * specified namespace to a GSSName object that is a MN. This method
     * is the equivalent to calling createName() and GSSName.canonicalize().
     *
     * @param name byte array representing the name to create
     * @param nameType Oid specifying the namespace of the name supplied
     * in the byte array. "null" may be used to specify that a mechanism-
     * specific default syntax should be assumed by each mechanism that
     * examines the byte array.
     * @param mech Oid specifying the mechanism for which this name should be
     * created.
     * @return GSSName representation of the string name held in name.
     * @throws GSSException
     */
    public abstract GSSName createName(byte[] name, Oid nameType, Oid mech)
            throws GSSException;

    /**
     * Acquires default credentials. This causes the GSS-API to use
     * system-specific defaults for the set of mechanisms, name, and
     * a DEFAULT lifetime.
     *
     * @param usage the intented usage of this credential object. This value
     * must be either GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or GSSCredential.ACCEPT_ONLY(2).
     * @return the created GSSCredential object.
     * @throws GSSException
     */
    public abstract GSSCredential createCredential(int usage)
            throws GSSException;

    /**
     * Acquires a single mechanism credential.
     *
     * @param aName name of the principal for whom this credential is to be
     * acquired. Use "null" to specify the default principal.
     * @param lifetime number of seconds that credentials should remain
     * valid. Use GSSCredential.INDEFINITE_LIFETIME to request that the
     * credentials have the maximum permitted lifetime. Use
     * GSSCredential.DEFAULT_LIFETIME to request default credential lifetime.
     * @param mech Oid of the desired mechanism. Use "(Oid) null" to request
     * the default mechanism(s).
     * @param usage the intented usage of this credential object. This value
     * must be either GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or GSSCredential.ACCEPT_ONLY(2).
     * @return the created GSSCredential object.
     * @throws GSSException
     */
    public abstract GSSCredential createCredential(GSSName aName,
            int lifetime, Oid mech, int usage) throws GSSException;

    /**
     * Acquires credentials for each of the mechanisms specified in the
     * array called mechs. To determine the list of mechanisms for which
     * the acquisition of credentials succeeded, the caller should use
     * the GSSCredential.getMechs() method.
     *
     * @param aName Name of the principal for whom this credential is to be
     * acquired. Use "null" to specify the default principal.
     * @param lifetime number of seconds that credentials should remain
     * valid. Use GSSCredential.INDEFINITE_LIFETIME to request that the
     * credentials have the maximum permitted lifetime. Use
     * GSSCredential.DEFAULT_LIFETIME to request default credential lifetime.
     * @param mechs array of mechanisms over which the credential is to be
     * acquired. Use "(Oid[]) null" for requesting a system-specific
     * default set of mechanisms.
     * @param usage the intented usage of this credential object. This value
     * must be either GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or GSSCredential.ACCEPT_ONLY(2).
     * @return the created GSSCredential object.
     * @throws GSSException
     */
    public abstract GSSCredential createCredential(GSSName aName,
            int lifetime, Oid[] mechs, int usage) throws GSSException;

    /**
     * Creates a context on the initiator's side. Context flags may be
     * modified through the mutator methods prior to calling
     * GSSContext.initSecContext().
     *
     * @param peer name of the target peer.
     * @param mech Oid of the desired mechanism. Use "(Oid) null" to request
     * the default mechanism.
     * @param myCred credentials of the initiator. Use "null" to act as a
     * default initiator principal.
     * @param lifetime The request lifetime, in seconds, for the context.
     * Use GSSContext.INDEFINITE_LIFETIME and GSSContext.DEFAULT_LIFETIME to
     * request indefinite or default context lifetime.
     * @return the created GSSContext object.
     * @throws GSSException
     */
    public abstract GSSContext createContext(GSSName peer, Oid mech,
            GSSCredential myCred, int lifetime) throws GSSException;

    /**
     * Creates a context on the acceptor's side. The context properties will
     * be determined from the input token supplied to the accept method.
     *
     * @param myCred credentials for the acceptor. Use "null" to act as a
     * default acceptor principal.
     * @return the created GSSContext object.
     * @throws GSSException
     */
    public abstract GSSContext createContext(GSSCredential myCred)
            throws GSSException;

    /**
     * Creates a previously-exported context. The context properties will
     * be determined from the input token and can't be modified
     * through the set methods.
     *
     * @param interProcessToken token previously emitted from the export
     * method.
     * @return the created GSSContext object.
     * @throws GSSException
     */
    public abstract GSSContext createContext(byte[] interProcessToken)
            throws GSSException;

    /**
     * Indicates that a specified provider should be used ahead of all
     * other providers when support for the given mechanism is desired. When
     * "null" is used in place of the Oid for the mechanism, the GSSManager
     * must use the indicated provider ahead of all others no matter what the
     * mechanism is. Only when the indicated provider does not support the 
     * needed mechanism should the GSSManager move on to a different provider.
     *
     * @param p provider instance that should be used.
     * @param mech mechanism for which the provider is being set.
     * @throws GSSException
     */
    public abstract void addProviderAtFront(Provider p, Oid mech)
            throws GSSException;

    /**
     * Indicates that the application would like a particular provider to
     * be used if no other provider can be found that supports the given
     * mechanism. When "null" is used instead of an Oid for the mechanism,
     * the GSSManager must use the indicated provider for any mechanism.
     *
     * @param p provider instance that should be used whenever support is
     * needed for mech.
     * @param mech mechanism for which the provider is being set.
     * @throws GSSException
     */
    public abstract void addProviderAtEnd(Provider p, Oid mech)
            throws GSSException;

}
