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

/**
 * This interface encapsulates the GSS-API credentials for an entity.
 * This interface is defined in section 7.3 of RFC 5653 
 * (http://tools.ietf.org/html/rfc5653).
 */
public interface GSSCredential extends Cloneable {

    /**
     * Flag requesting that credential be albe to be used for both
     * context initiation and acceptance.
     */
    public static final int INITIATE_AND_ACCEPT = 0;

    /**
     * Flag requesting that credential be able to be used for context
     * initialization only.
     */
    public static final int INITIATE_ONLY = 1;

    /**
     * Flag requesting that credential be able to be used for context
     * acceptance only.
     */
    public static final int ACCEPT_ONLY = 2;

    /**
     * Lifetime constant representing the default credential lifetime.
     */
    public static final int DEFAULT_LIFETIME = 0;

    /**
     * Lifetime constant representing indefinite credential lifetime.
     */
    public static final int INDEFINITE_LIFETIME = Integer.MAX_VALUE;

    /**
     * Releases any sensitive information that the GSSCredential object
     * may be containing. This should be called as soon as the credential
     * is no longer needed to minimize the time any sensitive information
     * is maintained.
     *
     * @throws GSSException
     */
    public void dispose() throws GSSException;
    
    /**
     * Retrieves the name of the entity that the credential asserts.
     *
     * @return name of entity that the credential asserts.
     * @throws GSSException
     */
    public GSSName getName() throws GSSException;
    
    /**
     * Retrieves a mechanism name of the entity that the credential asserts.
     * Equivalent to calling canonicalize() on the name returned from
     * GSSCredential.getName().
     *
     * @param mechOID mechanism for which information shoud be returned.
     * @return mechanism name of entity that the credential asserts.
     * @throws GSSException
     */
    public GSSName getName(Oid mechOID) throws GSSException;
    
    /**
     * Returns the remaining lifetime in seconds for the credential.
     *
     * @return remaining lifetime in seconds for the credential.
     * @throws GSSException
     */
    public int getRemainingLifetime() throws GSSException;
    
    /**
     * Returns the remaining lifetime in seconds for the credential to
     * remain capable of initiating security contexts under the specified
     * mechanism. A return value of GSSCredential.INDEFINITE_LIFETIME
     * indicates that the credential does not expire for context initiation.
     * A return value of 0 indicates that the credential is already expired.
     *
     * @param mech mechanism for which information should be returned.
     * @return remaining lifetime in seconds for credential to remain
     * capable of initiating security contexts, 
     * GSSCredential.INDEFINITE_LIFETIME if it never expires, or 0 if
     * it has already expired.
     * @throws GSSException
     */
    public int getRemainingInitLifetime(Oid mech) throws GSSException;
    
    /**
     * Returns the remaining lifetime in seconds for the credential to
     * remain capable of accepting security contexts under the specified
     * mechanism. A return value of GSSCredential.INDEFINITE_LIFETIME
     * indicates that the credential does not expire for context acceptance.
     * A return value of 0 indicates that the credential is already expired.
     *
     * @param mech mechanism for which information should be returned.
     * @return remaining lifetime in seconds for credential to remain
     * capable of accepting security contexts, 
     * GSSCredential.INDEFINITE_LIFETIME if it never expires, or 0 if
     * it has already expired.
     * @throws GSSException
     */
    public int getRemainingAcceptLifetime(Oid mech) throws GSSException;
    
    /**
     * Returns the credential usage flag as a union over all mechanisms.
     *
     * @return either GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or GSSCredential.ACCEPT_ONLY(2).
     * @throws GSSException
     */
    public int getUsage() throws GSSException;
    
    /**
     * Returns the credential usage flag for the specified mechanism only.
     *
     * @return either GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or GSSCredential.ACCEPT_ONLY(2).
     * @throws GSSException
     */
    public int getUsage(Oid mechOID) throws GSSException;
    
    /**
     * Returns an array of mechanisms supported by this credential.
     *
     * @return array of mechanisms supported by this credential.
     * @throws GSSException
     */
    public Oid[] getMechs() throws GSSException;
    
    /**
     * Adds a mechanism-specific credential-element to an existing
     * credential, thus allowing the construction of a credential one
     * mechanism at a time.
     *
     * For initLifetime and acceptLifetime, use 
     * GSSCredential.INDEFINITE_LIFETIME to request that credentials have
     * the maximum permitted lifetime. Use GSSCredential.DEFAULT_LIFETIME
     * to request default credential lifetime.
     *
     * The value of "usage" must be one of:
     * GSSCredential.INITIATE_AND_ACCEPT(0),
     * GSSCredential.INITIATE_ONLY(1), or
     * GSSCredential.ACCEPT_ONLY(2)
     *
     * @param aName name of principal for whom credential is to be acquired.
     * @param initLifetime number of seconds that credentials should
     * remain valid for initiating of security contexts.
     * @param acceptLifetime number of seconds that credentials should
     * remain valid for accepting fo security contexts.
     * @param mech mechanisms over which the credential is to be acquired.
     * @param usage intended usage for this credential object.
     * @throws GSSException
     */
    public void add(GSSName aName, int initLifetime, int acceptLifetime,
            Oid mech, int usage) throws GSSException;
    
    /**
     * Tests if this GSSCredential refers to the same entity as the supplied
     * object. To be equal, the two credentials must be acquired over the
     * same mechanisms and must refer to the same principal.
     *
     * @param another GSSCredential object for comparison.
     * @return "true" if the two credentials are equal, otherwise "false".
     */
    public boolean equals(Object another);
   
}
