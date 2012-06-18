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

        if (msgp2.getPrivacy() != false)
            fail("Failed to get Privacy from MessageProp");

        if (msgp2.getMinorStatus() != 0)  
            fail("Failed to get minor status from MessageProp");


        if (!msgp2.getMinorString().equals("no status"))
            fail("Failed to get minor status string from MessageProp");

        if (msgp2.isDuplicateToken())
            fail("Incorrect duplicate flag set");

        if (msgp2.isOldToken())
            fail("Incorrect old token flag set");

        if (msgp2.isUnseqToken())
            fail("Incorrect unseq token set");

        if (msgp2.isGapToken())
            fail("Incorrect gap token set");
    }

}

