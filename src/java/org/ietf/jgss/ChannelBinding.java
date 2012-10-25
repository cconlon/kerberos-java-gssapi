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

import java.net.InetAddress;
import java.util.Arrays;
import edu.mit.jgss.swig.*;

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

    protected gss_channel_bindings_struct channelBindStruct;

    /* Address of the context initiator */
    private InetAddress initAddr = null;

    /* Address of the context acceptor */
    private InetAddress acceptAddr = null;

    /* Application-supplied data to be used as part of the channel bindings */
    private byte[] appData = null;

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

        this(appData);

        /* If the user passes in null values for all arguments, we'll
           just set our internal gss_channel_bindings_struct equal to
           GSS_C_NO_CHANNEL_BINDINGS */
        if (initAddr == null && acceptAddr == null && appData == null) {
            channelBindStruct = gsswrapper.GSS_C_NO_CHANNEL_BINDINGS;
            return;
        } 

        /* otherwise, continue with setup as normal */
        channelBindStruct = new gss_channel_bindings_struct();

        if (initAddr != null) {
            this.initAddr = initAddr;
            channelBindStruct.setInitiator_addrtype(gsswrapper.GSS_C_AF_INET);
            gss_buffer_desc initAddrBuff = new gss_buffer_desc();
            gsswrapper.setDescArray(initAddrBuff, initAddr.getAddress());
            initAddrBuff.setLength(initAddr.getAddress().length);
            channelBindStruct.setInitiator_address(initAddrBuff);
        }

        if (acceptAddr != null) {
            this.acceptAddr = acceptAddr;
            channelBindStruct.setAcceptor_addrtype(gsswrapper.GSS_C_AF_INET);
            gss_buffer_desc acceptAddrBuff = new gss_buffer_desc();
            gsswrapper.setDescArray(acceptAddrBuff, acceptAddr.getAddress());
            acceptAddrBuff.setLength(acceptAddr.getAddress().length);
            channelBindStruct.setAcceptor_address(acceptAddrBuff);
        }

        if (appData != null) {
            setAppData(appData);

            gss_buffer_desc appBuffer = new gss_buffer_desc();
            gsswrapper.setDescArray(appBuffer, appData);
            appBuffer.setLength(appData.length);
            channelBindStruct.setApplication_data(appBuffer);
        }

    }

    /**
     * Creates ChannelBinding object with the supplied application data
     * (without any addressing information).
     *
     * @param appData application-supplied data to be used as part of the
     * channel bindings.
     */
    public ChannelBinding(byte[] appData) {

        if (appData != null) {

            channelBindStruct = new gss_channel_bindings_struct();
            setAppData(appData);

            gss_buffer_desc appBuffer = new gss_buffer_desc();
            gsswrapper.setDescArray(appBuffer, appData);
            appBuffer.setLength(appData.length);
            channelBindStruct.setApplication_data(appBuffer);
        } else {
            channelBindStruct = gsswrapper.GSS_C_NO_CHANNEL_BINDINGS;
        }
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

        if (appData == null) {
            return null;
        } else {
            byte[] tmp = new byte[this.appData.length];
            System.arraycopy(this.appData, 0, tmp, 0, this.appData.length);
            return tmp;
        }
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

    private void setAppData(byte[] appData) {
        if (appData != null) {
            this.appData = new byte[appData.length];
            System.arraycopy(appData, 0, this.appData, 0, appData.length);
        }
    }
    
}
