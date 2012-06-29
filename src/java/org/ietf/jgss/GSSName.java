package org.ietf.jgss;

/**
 * Encapsulates a single GSS-API princiapl entity. This interface is defined 
 * in section 7.2 of RFC 5653 (http://tools.ietf.org/html/rfc5653).
 */
public interface GSSName {

    /**
     * Oid indicating a host-based service name form. It is used to represent
     * services associated with host computers. This name form is constructed
     * using two elements, "service" and "hostname", i.e. "service@hostname".
     *
     * It represents the following value: { iso{1) member-body(2) United
     * States(840) mit(113554) infosys(1) gssapi(2) generic(1)
     * service_name(4) }
     */
    public static final Oid NT_HOSTBASED_SERVICE = Oid.getNewOid("1.2.840.113554.1.2.1.4");

    /**
     * Name type to indicate a named user on a local system. 
     * 
     * It represents the following value: { iso(1) member-body(2) 
     * United States(840) mit(113554) infosys(1) gssapi(2) generic(1) 
     * user_name(1) }
     */
    public static final Oid NT_USER_NAME = Oid.getNewOid("1.2.840.113554.1.2.1.1");

    /**
     * Name type to indicate a numeric user identifier corresponding to a
     * user on a local system (e.g., Uid).
     *
     * It represents the following value: { iso(1) member-body(2)
     * United States(840) mit(113554) infosys(1) gssapi(2) generic(1)
     * machine_uid_name(2) }
     */
    public static final Oid NT_MACHINE_UID_NAME = Oid.getNewOid("1.2.840.113554.1.2.1.2");

    /**
     * Name type to indicate a string of digits representing the numeric
     * user identifier of a user on a local system.
     *
     * It represents the following value: { iso(1) member-body(2) United
     * States(840) mit(113554) infosys(1) gssapi(2) generic(1)
     * string_uid_name(3) }
     */
    public static final Oid NT_STRING_UID_NAME = Oid.getNewOid("1.2.840.113554.1.2.1.3");

    /**
     * Name type for representing an anonymous entity.
     *
     * It represents the following value: { iso(1), org(3), dod(6),
     * internet(1), security(5), nametypes(6), gss-anonymous-name(3) }
     */
    public static final Oid NT_ANONYMOUS = Oid.getNewOid("1.3.6.1.5.6.3");

    /**
     * Name type used to indicate an exported name produced by the export
     * method.
     *
     * It represents the following value: { iso(1), org(3), dod(6),
     * internet(1), security(5), nametypes(6), gss-api-exported-name(4) }
     */
    public static final Oid NT_EXPORT_NAME = Oid.getNewOid("1.3.6.1.5.6.4");

    /**
     * Compares two GSSName objects to determine if they refer to the same
     * entity. If the names cannot be compared, a GSSException may
     * be thrown.
     *
     * @param another GSSName object with which to compare
     * @return "true" if the two objects are equal, "false" if the are
     * not equal or if either of the names represents an anonymous
     * entity.
     */
    public boolean equals(GSSName another) throws GSSException;

    /**
     * Compares the two GSSName objects to determine if they refer to the
     * same entity. If an error occurs, "false" will be returned. Note that
     * two GSSName objects are equal if the result of calling hashCode()
     * on each is equal.
     *
     * @param another GSSName object with which to compare
     * @return "true" if the two objects are equal, "false" if they are
     * not, if an error occurs, or if either of the names represents an
     * anonymous entity.
     */
    public boolean equals(Object another);

    /**
     * Creates a mechanism name (MN) from an arbitrary internal name.
     *
     * @param mech Oid for the mechanism for which the canonical form of
     * the name is requested.
     * @return the canonical form of the given Oid
     */
    public GSSName canonicalize(Oid mech) throws GSSException;

    /**
     * Returns a canonical contiguous byte representation of a mechanism
     * name (MN), suitable for direct, byte-by-byte comparison by
     * authorization functions. If the name is not a MN, a GSSException
     * may be thrown.
     *
     * @return canonical contiguous byte representation of the mechanism
     * name.
     */
    public byte[] export() throws GSSException;

    /**
     * Returns a textual representation of the GSSName object.
     *
     * @return textual representation of the GSSName object.
     */
    public String toString();

    /**
     * Returns the Oid representing the type of name returned through the
     * toString method.
     *
     * @return Oid representing the type of name returned through the toString
     * method.
     */
    public Oid getStringNameType() throws GSSException;

    /**
     * Tests if the GSSName object represents an anonymous entity.
     *
     * @return "true" if the GSSName object is an anonymous name, "false"
     * otherwise.
     */
    public boolean isAnonymous();

    /**
     * Tests if the GSSName object contains only one mechanism element and
     * is thus a mechanism name.
     *
     * @return "true" if the GSSName object is a mechanism name, "false"
     * otherwise.
     */
    public boolean isMN();

}
