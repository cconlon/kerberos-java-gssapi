package org.ietf.jgss;

import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.Arrays;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.GSSException;

public class GSSNameTest extends TestCase {

    /**
     * Tests the creaton of new GSSName objects.  There are 4 different
     * createName methods inside of GSSManager:
     *
     * 1) createName(String, Oid)
     * 2) createName(byte[], Oid)
     * 3) createName(String, Oid, Oid)
     * 4) createName(byte[], Oid, Oid)
     *
     */
    public void testCreateGSSName() throws GSSException {
        
        GSSManager testManager = GSSManager.getInstance();
      
        byte[] exportName_actual = {(byte)0x04, (byte)0x01, (byte)0x00, 
                                    (byte)0x0B, (byte)0x06, (byte)0x09,
                                    (byte)0x2A, (byte)0x86, (byte)0x48, 
                                    (byte)0x86, (byte)0xF7, (byte)0x12,
                                    (byte)0x01, (byte)0x02, (byte)0x02, 
                                    (byte)0x00, (byte)0x00, (byte)0x00,
                                    (byte)0x0C, (byte)0x73, (byte)0x65, 
                                    (byte)0x72, (byte)0x76, (byte)0x69,
                                    (byte)0x63, (byte)0x65, (byte)0x40, 
                                    (byte)0x68, (byte)0x6F, (byte)0x73,
                                    (byte)0x74};

        /* create NT_USER_NAME */
        try {
            GSSName testName = testManager.createName("testuser", 
                    GSSName.NT_USER_NAME);
        } catch (GSSException e) {
            fail("Failed to create new GSSName (testuser, NT_USER_NAME)");
        }
       
        /* create NT_HOSTBASED_SERVICE */ 
        try {
            GSSName testName2 = testManager.createName("service@hostname", 
                    GSSName.NT_HOSTBASED_SERVICE);
        } catch (GSSException e) {
            fail("Failed to create new GSSName (service@hostname, " + 
                 "NT_HOSTBASED_SERVICE)");
        }
       
        /* create NT_MACHINE_UID_NAME */ 
        try {
            GSSName testName3 = testManager.createName("test@test", 
                    GSSName.NT_MACHINE_UID_NAME);
        } catch (GSSException e) {
            fail("Failed to create new GSSName (test@test, " +
                 "NT_MACHINE_UID_NAME)");
        }
       
        /* create NT_STRING_UID_NAME */ 
        try {
            GSSName testName4 = testManager.createName("test@test", 
                    GSSName.NT_STRING_UID_NAME);
        } catch (GSSException e) {
            fail("Failed to create new GSSName (test@test, " +
                 "NT_STRING_UID_NAME)");
        }
       
        /* create NT_ANONYMOUS */ 
        try {
            GSSName testName5 = testManager.createName("test@test", 
                    GSSName.NT_ANONYMOUS);
        } catch (GSSException e) {
            fail("Failed to create new GSSName (test@test, " + 
                 "NT_ANONYMOUS)");
        }
        
        /* createName(String, GSSName, Oid) */
        Oid krb5Mech = new Oid("1.2.840.113554.1.2.2");
        try {
            GSSName testName6 = testManager.createName("foo",
                    GSSName.NT_USER_NAME, krb5Mech);
        } catch (GSSException e) {
            fail("Failed in GSSName.createName(String, GSSName, Oid)");
        }

        /* createName(byte[], GSSName) */
        try {
            GSSName testName7 = testManager.createName(exportName_actual,
                    GSSName.NT_EXPORT_NAME);
        } catch (GSSException e) {
            fail("Failed in GSSName.createName(byte[], GSSName)");
        }
        
        /* createName(byte[], GSSName, Oid) */
        try {
            GSSName testName8 = testManager.createName(exportName_actual,
                    GSSName.NT_EXPORT_NAME, krb5Mech);
        } catch (GSSException e) {
            fail("Failed in GSSName.createName(byte[], GSSName)");
        }
       
    }

    /**
     * Tests methods inside GSSName class. Currently tests:
     * 1) equals(GSSName)
     * 2) equals(Object)
     * 3) canonicalize(Oid)
     * 4) export()
     * 5) toString()
     * 6) getStringNameType()
     * 7) isAnonymous()
     * 8) isMN()
     *
     */
    public void testGSSNameMethods() throws GSSException {

        GSSManager testManager = GSSManager.getInstance();

        try {
            
            /* (1) ----- testing GSSName.equals(GSSName) ----- */
            GSSName testName1 = testManager.createName("testUser",
                    GSSName.NT_USER_NAME);
            GSSName testName2 = testManager.createName("testUser",
                    GSSName.NT_USER_NAME);
            GSSName testName3 = testManager.createName("testUser3",
                    GSSName.NT_USER_NAME);

            if(!testName1.equals(testName2))
                fail("GSSName.equals(GSSName) failed");

            if(testName1.equals(testName3))
                fail("GSSName.equals(GSSName) failed");

            System.out.format("%-40s %10s%n", "... testing equals(GSSName)", 
                              "... passed");


            /* (2) ----- testing GSSName.equals(Object) ----- */
            
            if(!testName1.equals((Object)testName2))
                fail("GSSName.equals(GSSName) failed");

            if(testName1.equals((Object)testName3))
                fail("GSSName.equals(GSSName) failed");
           
            System.out.format("%-40s %10s%n", "... testing equals(Object)", 
                              "... passed");

            
            /* (3) ----- testing GSSName.canonicalize ----- */
            GSSName name = testManager.createName("service@host",
                            GSSName.NT_USER_NAME);

            Oid krb5 = new Oid("1.2.840.113554.1.2.2");

            GSSName mechName = name.canonicalize(krb5);

            /* above 2 steps are equal to the following */
            /* GSSName mechName = testManager.createName("service@host",
                            GSSName.NT_HOSTBASED_SERVICE, krb5); */

            if (!name.equals(mechName))
                fail("GSSName.equals on canonicalized name failed");
            
            System.out.format("%-40s %10s%n", "... testing canonicalize()", 
                              "... passed");
            
            
            /* (4) ----- testing GSSName.export ----- */
            byte[] exportName_actual = {(byte)0x04, (byte)0x01, (byte)0x00, 
                                        (byte)0x0B, (byte)0x06, (byte)0x09,
                                        (byte)0x2A, (byte)0x86, (byte)0x48, 
                                        (byte)0x86, (byte)0xF7, (byte)0x12,
                                        (byte)0x01, (byte)0x02, (byte)0x02, 
                                        (byte)0x00, (byte)0x00, (byte)0x00,
                                        (byte)0x0C, (byte)0x73, (byte)0x65, 
                                        (byte)0x72, (byte)0x76, (byte)0x69,
                                        (byte)0x63, (byte)0x65, (byte)0x40, 
                                        (byte)0x68, (byte)0x6F, (byte)0x73,
                                        (byte)0x74};

            byte[] exportName = mechName.export();

            if (!Arrays.equals(exportName, exportName_actual))
                fail("GSSName.export failed");

            System.out.format("%-40s %10s%n", "... testing export()", 
                              "... passed");
            
           
            /* (5) ----- testing GSSName.toString ----- */
            if(!name.toString().equals("service@host"))
                fail("GSSName.toString failed");

            System.out.format("%-40s %10s%n", "... testing toString()", 
                              "... passed");
            
            
            /* (6) ----- testing GSSName.getStringNameType ----- */
            Oid nameType = name.getStringNameType();
            if (!nameType.equals(GSSName.NT_USER_NAME))
                fail("GSSName.getStringNameType failed");

            System.out.format("%-40s %10s%n", 
                              "... testing getStringNameType()", 
                              "... passed");
           
            
            /* (7) ----- testing GSSName.isAnonymous ----- */
            GSSName nameAnon = testManager.createName("service@host",
                               GSSName.NT_ANONYMOUS);
            if(!nameAnon.isAnonymous())
                fail("GSSName.isAnonymous failed");
            if(name.isAnonymous())
                fail("GSSName.isAnonymous failed");

            System.out.format("%-40s %10s%n", "... testing isAnonymous()", 
                              "... passed");
          
            
            /* (8) ----- testing GSSName.isMN ----- */
            if(!mechName.isMN()) {
                fail("GSSName.isMN failed");
            }
            if(name.isMN()) {
                fail("GSSName.isMN failed");
            }
            System.out.format("%-40s %10s%n", "... testing isMN()", 
                    "... passed");

        } catch (GSSException e) {
            System.out.println(e.toString());
            fail("Failed during testGSSNameMethods");
        }
    }
}

