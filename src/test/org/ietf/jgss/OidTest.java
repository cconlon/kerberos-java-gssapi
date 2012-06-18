package org.ietf.jgss;

import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;

import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSException;

public class OidTest extends TestCase {

    public void testOidStringFail() throws GSSException {
        try {
            Oid testOid = new Oid("asdfasdf");
            fail("No exception was thrown");
        } catch (GSSException e) {
            // We expect to fail here
        }
    }

    public void testOidStringPass() throws GSSException {
        try {
            Oid testOid = new Oid("1.2.3.4");
        } catch (GSSException e) {
            fail("We expect this one to pass");
        }
    }

    public void testOidtoString() {
        String oidString = "1.2.840.113554.1.2.2"; 
        try {
            Oid testoid = new Oid("1.2.840.113554.1.2.2");
            if (oidString != testoid.toString()) {
                fail("Oid.toString failed");
            }
        } catch (GSSException e) {
            fail("Failed to create Oid object");
        }
    }
}
