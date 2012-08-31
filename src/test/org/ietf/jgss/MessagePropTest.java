package org.ietf.jgss;

import org.junit.Test;
import junit.framework.TestCase;

import org.ietf.jgss.MessageProp;

public class MessagePropTest extends TestCase {

    public void testMessagePropCreation() {

        MessageProp msgp = new MessageProp(true);
        MessageProp msgp2 = new MessageProp(0, true);

        msgp2.setQOP(1);
        msgp2.setPrivacy(false);
        msgp2.setSupplementaryStates(false, false, false, false,
                                     0, "no status");

        if (msgp2.getQOP() != 1)
            fail("Failed to get QOP from MessageProp");
        System.out.format("%-40s %10s%n", "... testing getQOP()", 
                "... passed");

        if (msgp2.getPrivacy() != false)
            fail("Failed to get Privacy from MessageProp");
        System.out.format("%-40s %10s%n", "... testing getPrivacy()", 
                "... passed");

        if (msgp2.getMinorStatus() != 0)  
            fail("Failed to get minor status from MessageProp");
        System.out.format("%-40s %10s%n", "... testing getMinorStatus()", 
                "... passed");

        if (!msgp2.getMinorString().equals("no status"))
            fail("Failed to get minor status string from MessageProp");
        System.out.format("%-40s %10s%n", "... testing getMinorString()", 
                "... passed");

        if (msgp2.isDuplicateToken())
            fail("Incorrect duplicate flag set");
        System.out.format("%-40s %10s%n", "... testing isDuplicateToken()", 
                "... passed");

        if (msgp2.isOldToken())
            fail("Incorrect old token flag set");
        System.out.format("%-40s %10s%n", "... testing isOldToken()", 
                "... passed");

        if (msgp2.isUnseqToken())
            fail("Incorrect unseq token set");
        System.out.format("%-40s %10s%n", "... testing isUnseqToken()", 
                "... passed");

        if (msgp2.isGapToken())
            fail("Incorrect gap token set");
        System.out.format("%-40s %10s%n", "... testing isGapToken()", 
                "... passed");
    }

}

