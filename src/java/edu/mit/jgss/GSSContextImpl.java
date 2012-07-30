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
package edu.mit.jgss;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.GSSContext;

import edu.mit.jgss.swig.*;

import java.io.InputStream;
import java.io.OutputStream;

public class GSSContextImpl implements GSSContext {
    
    /* representing our underlying SWIG-wrapped gss_ctx_id_t object */
    private gss_ctx_id_t_desc internGSSCtx;

    public byte[] initSecContext(byte[] inputBuf, int offset, int len) 
        throws GSSException {
        // TODO
        return null;
    }

    public int initSecContext(InputStream inStream, OutputStream outStream) 
        throws GSSException {
        // TODO
        return 0;
    }

    public byte[] acceptSecContext(byte[] inTok, int offset, int len)
        throws GSSException{
        // TODO
        return null;
    }

    public void acceptSecContext(InputStream inStream, OutputStream outStream)
        throws GSSException {
        // TODO
    }

    public boolean isEstablished() {
        // TODO
        return false;
    }

    public void dispose() throws GSSException {
        // TODO
    }

    public int getWrapSizeLimit(int qop, boolean confReq, int maxTokenSize)
        throws GSSException {
        // TODO
        return 0;
    }

    public byte[] wrap(byte[] inBuf, int offset, int len, MessageProp msgProp)
        throws GSSException {
        // TODO
        return null;
    }

    public void wrap(InputStream inStream, OutputStream outStream, 
            MessageProp msgProp) throws GSSException {
        // TODO
    }

    public byte[] unwrap(byte[] inBuf, int offset, int len,
            MessageProp msgProp) throws GSSException {
        // TODO
        return null;
    }

    public void unwrap(InputStream inStream, OutputStream outStream,
            MessageProp msgProp) throws GSSException {
        // TODO
    }

    public byte[] getMIC(byte[] inMsg, int offset, int len,
            MessageProp msgProp) throws GSSException {
        // TODO
        return null;
    }

    public void getMIC(InputStream inStream, OutputStream outStream,
            MessageProp msgProp) throws GSSException {
        // TODO
    }

    public void verifyMIC(byte[] inTok, int tokOffset, int tokLen,
            byte[] inMsg, int msgOffset, int msgLen,
            MessageProp msgProp) throws GSSException {
        // TODO
    }

    public void verifyMIC(InputStream tokStream, InputStream msgStream,
            MessageProp msgProp) throws GSSException {
        // TODO
    }

    public byte[] export() throws GSSException {
        // TODO
        return null;
    }

    public void requestMutualAuth(boolean state) throws GSSException {
        // TODO
    }

    public void requestReplayDet(boolean state) throws GSSException {
        // TODO
    }

    public void requestSequenceDet(boolean state) throws GSSException {
        // TODO
    }

    public void requestCredDeleg(boolean state) throws GSSException {
        // TODO
    }

    public void requestAnonymity(boolean state) throws GSSException {
        // TODO
    }

    public void requestConf(boolean state) throws GSSException {
        // TODO
    }

    public void requestInteg(boolean state) throws GSSException {
        // TODO
    }

    public void requestLifetime(int lifetime) throws GSSException {
        // TODO
    }

    public void setChannelBinding(ChannelBinding cb) throws GSSException {
        // TODO
    }

    public boolean getCredDelegState() {
        // TODO
        return false;
    }

    public boolean getMutualAuthState() {
        // TODO
        return false;
    }

    public boolean getReplayDetState() {
        // TODO
        return false;
    }

    public boolean getSequenceDetState() {
        // TODO
        return false;
    }

    public boolean getAnonymityState() {
        // TODO
        return false;
    }

    public boolean isTransferable() throws GSSException {
        // TODO
        return false;
    }

    public boolean isProtReady() {
        // TODO
        return false;
    }

    public boolean getConfState() {
        // TODO
        return false;
    }

    public boolean getIntegState() {
        // TODO
        return false;
    }

    public int getLifetime() {
        // TODO
        return 0;
    }

    public GSSName getSrcName() {
        // TODO
        return null;
    }

    public GSSName getTargName() {
        // TODO
        return null;
    }

    public Oid getMech() throws GSSException {
        // TODO
        return null;
    }

    public GSSCredential getDelegCred() throws GSSException {
        // TODO
        return null;
    }

    public boolean isInitiator() throws GSSException {
        // TODO
        return false;
    }
}
