package org.ietf.jgss;

import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.Arrays;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSException;

public class OidTest extends TestCase {

    public void testOidStringConstructor() throws GSSException {
        try {
            Oid testOid = new Oid("asdfasdf");
            fail("No exception was thrown");
        } catch (GSSException e) {
            // We expect to fail, proceed
        }

        try {
            Oid testOid = new Oid("1.2.3.4");
        } catch (GSSException e) {
            fail("new Oid(String) failed");
        }
    }

    public void testOidMethods() throws GSSException {
        String oidString = "1.2.840.113554.1.2.2"; 
        byte[] oidDER = {(byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, 
                         (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x12, 
                         (byte)0x01, (byte)0x02, (byte)0x02};

        /* incorrect DER-encoded Oid array, bad length value */
        byte[] badOidDER = {(byte)0x06, (byte)0x0A, (byte)0x2A, (byte)0x86, 
                         (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x12, 
                         (byte)0x01, (byte)0x02, (byte)0x02};
        try {

            /* testing Oid(DER) constructor and Oid.toString() method */
            Oid testoid = new Oid("1.2.840.113554.1.2.2");
            if (!oidString.equals(testoid.toString())) {
                fail("Oid.toString failed");
            }

            Oid testoid2 = new Oid(oidDER);
            if (!oidString.equals(testoid2.toString())) {
                fail("Oid.toString failed using DER-encoded OID, " + testoid2.toString());
            }
            System.out.format("%-40s %10s%n", "... testing toString()", "... passed");

            /* testing Oid.getDER() method */
            if (!Arrays.equals(testoid.getDER(), testoid2.getDER())) {
                fail("Oid.getDER failed during Oid comparison");
            }
            System.out.format("%-40s %10s%n", "... testing getDER()", "... passed");

            /* testing Oid.containedIn(Oid[]) method */
            Oid[] oidArray = new Oid[2];
            oidArray[0] = testoid;
            oidArray[1] = testoid2;

            if (!testoid.containedIn(oidArray)) {
                fail("Oid.containedIn failed");
            }

            Oid testoid3 = new Oid("1.2.3.4");
            if (testoid3.containedIn(oidArray)) {
                fail("Oid.containedIn failed");
            }
            System.out.format("%-40s %10s%n", "... testing containedIn()", "... passed");

        } catch (GSSException e) {
            throw e;
        }

        try {
            Oid testoid3 = new Oid(badOidDER);
            fail ("Oid(Oid DER array) validation failed.");

        } catch (GSSException e) {
            // We expect this to fail verification
            System.out.format("%-40s %10s%n", "... testing Oid DER validation", "... passed");
        }
    }

    public void testOidStreamConstructor() {
        String oidString = "1.2.840.113554.1.2.2"; 
        byte[] oidDER = {(byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, 
                         (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x12, 
                         (byte)0x01, (byte)0x02, (byte)0x02};
        try {
            ByteArrayInputStream oidStream = new ByteArrayInputStream(oidDER);
            Oid testOid = new Oid(oidStream);

            if (!oidString.equals(testOid.toString())) {
                fail("Oid.toString failed using Oid from InputStream");
            }
        } catch (GSSException e) {
            fail("testOidStreamConstructor() failed");
        }
    }

    public void testOidEquals() {
        String oidString = "1.2.840.113554.1.2.2"; 

        try {
            Oid test1 = new Oid(oidString);
            Oid test2 = new Oid(oidString);

            if(!test1.equals(test2)) {
                fail("Oid equals method failed!");
            }
        } catch (GSSException e) {
            fail("failed when testing Oid.equals() method");
        }
        System.out.format("%-40s %10s%n", "... testing equals()", "... passed");
    }

}
