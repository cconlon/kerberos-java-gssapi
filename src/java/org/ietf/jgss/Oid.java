package org.ietf.jgss;

import java.io.InputStream;
import edu.mit.jgss.swig.gss_OID_desc;

public class Oid {

    /**
     * Creates a new Oid object from a string representation of the Oid's
     * integer components (e.g., "1.2.840.113554.1.2.2").
     *
     * @param strOid the string representation of the Oid, separated by dots.
     */
    public Oid(String strOid) throws GSSException {
        // TODO
    }

    /**
     * Creates a new Oid object from its DER encoding. The DER encoding
     * refers to the full encoding, including the tag and length.
     * Structure and encoding of Oids is defined in ISOIEC-8824 and
     * ISOIEC-8825.
     *
     * @param derOid the stream containing the DER-encoded Oid.
     */
    public Oid(InputStream derOid) throws GSSException {
        // TODO
    }

    /**
     * Creates a new Oid object from its DER encoding. The DER encoding
     * refers to the full encoding, including the tag and length.
     * Structure and encoding of Oids is defined in ISOIEC-8824 and
     * ISOIEC_8825.
     *
     * @param derOid the byte array containing the DER-encoded Oid.
     */
    public Oid(byte[] derOid) throws GSSException {
        // TODO
    }

    /**
     * Returns a string representation of the Oid's integer components in
     * the dot separated notation.
     *
     * @return string representation of the Oid's integer components in dot
     * notation.
     */
    public String toString() {
        // TODO
        return null;
    }

    /**
     * Compares two Oid objects, returning "true" if the two represent
     * the same Oid value. Two Oid objects are equal when the
     * integer result from hashCode() method called on them is the same.
     *
     * @param Obj the Oid object with which to compare.
     * @return "true" if the two objects are equal, "false" otherwise.
     */
    public boolean equals(Object Obj) {
        // TODO
        return true;
    }

    /**
     * Returns the full ASN.1 DER encoding for the Oid object. This encoding
     * includes the tag and length.
     * 
     * @return full ASN.1 DER encoding for the Oid object.
     */
    public byte[] getDER() {
        // TODO
        return null;
    }

    /**
     * Tests if an Oid object is contained in the given Oid object array.
     *
     * @param oids the array of Oid objects to test against.
     * @return "true" if the Oid object is contained in the array,
     * "false" otherwise.
     */
    public boolean containedIn(Oid[] oids) {
        // TODO
        return true;
    }

    public static Oid getOid(String strOid) {
        Oid retOid = null;
        try {
            retOid = new Oid(strOid);
        } catch (GSSException e) {
            e.printStackTrace();
        }
        return retOid;
    }

}
