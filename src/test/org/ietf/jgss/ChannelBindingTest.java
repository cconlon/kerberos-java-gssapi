package org.ietf.jgss;

import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.util.Arrays;

import org.ietf.jgss.ChannelBinding;

public class ChannelBindingTest extends TestCase {
    
    public void testChannelBindingCreation() {

        byte[] appData = {55, 54, 33};
        
        try {

            InetAddress initAddr = InetAddress.getByName("127.0.0.1");
            InetAddress acceptAddr = InetAddress.getByName("127.0.0.1");

            ChannelBinding cb = new ChannelBinding(initAddr, acceptAddr,
                                                   appData);
            ChannelBinding cb2 = new ChannelBinding(initAddr, acceptAddr,
                                                   appData);
            ChannelBinding cb3 = new ChannelBinding(appData);

            if (!cb.getInitiatorAddress().getHostAddress().equals("127.0.0.1"))
                fail("Failed to set initiator InetAddress correctly");

            if (!cb.getAcceptorAddress().getHostAddress().equals("127.0.0.1"))
                fail("Failed to set acceptor InetAddress correctly");
            System.out.format("%-40s %10s%n", "... testing setting InetAddress", 
                    "... passed");

            if (!Arrays.equals(cb.getApplicationData(), appData))
                fail("Failed to set application data correctly");
            System.out.format("%-40s %10s%n", "... testing setting app data", 
                    "... passed");

            if (!cb.equals(cb2))
                fail("ChannelBinding equals method failed");
            System.out.format("%-40s %10s%n", "... testing equals()", 
                    "... passed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

