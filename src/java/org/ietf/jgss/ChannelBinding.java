package org.ietf.jgss;

import java.net.InetAddress;
import java.util.Arrays;
import edu.mit.jgss.swig.gss_channel_bindings_struct;

/**
 * Class to represent GSS-API caller-provided channel binding information.
 * Channel bindings are used to allow callers to bind the establishment
 * of the security context to relevant characteristics like addresses
 * or to application-specific data.
 *
 * The use of channel bindings is optional in GSS-API. Because channel binding
 * information may be transmitted in context establishment tokens,
 * applications should therefore not use confidential data as channel-binding
 * components.
 */
public class ChannelBinding {

    private gss_channel_bindings_struct channelBindStruct;

    private InetAddress initAddr;   /* Address of the context initiator */
    private InetAddress acceptAddr; /* Address of the context acceptor */
    private byte[] appData;         /* Application-supplied data to be used
                                       as part of the channel bindings */

    /**
     * Creates ChannelBinding object with the given address and data
     * information. "null" values may be used for any fields that the
     * application does not want to specify.
     *
     * @param initAddr the address of the context initiator.
     * @param acceptAddr the address fo the context acceptor.
     * @param appData application-supplied data to be used as part of the
     * channel bindings.
     */
    public ChannelBinding(InetAddress initAddr, InetAddress acceptAddr,
                          byte[] appData) {
        this.initAddr = initAddr;
        this.acceptAddr = acceptAddr;
        this.appData = appData;
    }

    /**
     * Creates ChannelBinding object with the supplied application data
     * (without any addressing information).
     *
     * @param appData application-supplied data to be used as part of the
     * channel bindings.
     */
    public ChannelBinding(byte[] appData) {
        this.appData = appData;
    }

    /**
     * Returns the address of the context initiator.
     *
     * @return address of the context initiator.
     */ 
    public InetAddress getInitiatorAddress() {
        return initAddr;
    }

    /**
     * Returns the address of the context acceptor.
     *
     * @return address of the context acceptor.
     */
    public InetAddress getAcceptorAddress() {
        return acceptAddr;
    }

    /**
     * Returns the application data being used as part of the
     * ChannelBinding. "null" is returned if no application data has been
     * specified for the channel bindings.
     *
     * @return application-supplied data used as part of the ChannelBinding,
     * "null" if not set.
     */
    public byte[] getApplicationData() {
        if (appData == null)
            return null;

        return appData;
    }

    /**
     * Returns "true" if two ChannelBinding objects match.
     *
     * @return "true" if the two objects are equal, "false" otherwise.
     */
    public boolean equals(Object obj) {

        if (!(obj instanceof ChannelBinding)) {
            return false;
        }

        ChannelBinding tmp = (ChannelBinding) obj;

        if (!initAddr.equals(tmp.initAddr) || 
            !(acceptAddr.equals(tmp.acceptAddr))) {
            return false;
        }

        if (!Arrays.equals(appData, tmp.appData)) {
            return false;
        }

        return true;
    }
}
