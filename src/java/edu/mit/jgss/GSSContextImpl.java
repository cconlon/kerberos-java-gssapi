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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GSSContextImpl implements GSSContext {
    
    /* representing our underlying SWIG-wrapped gss_ctx_id_t object */
    private gss_ctx_id_t_desc internGSSCtx = new gss_ctx_id_t_desc();
    
    /* has this context been destroyed? */ 
    private boolean invalid = false;

    /* context state */
    private enum State { UNINIT, INPROG, DONE, DISPOSED }

    /* variables set during context creation */
    private GSSCredentialImpl credential = null;
    private GSSCredentialImpl delegCredential = null;
    private GSSNameImpl targetName = null;
    private GSSNameImpl srcName = null;
    private OidImpl mech = null;

    /* create a ChannelBinding equal to GSS_C_NO_CHANNEL_BINDINGS */
    private ChannelBindingImpl channelBinding = new ChannelBindingImpl(null, 
            null, null);

    /* use default lifetime at first - note that we store the lifetime as a 
     * long because native gssapi uses unsigned ints, whereas Java has no such 
     * thing. Thus, we use the larger datatype to imitate an unsigned int. */
    private long lifetime = convertLifetime(GSSContext.DEFAULT_LIFETIME);

    /* state flags */
    private boolean requestCredDeleg   = false;
    private boolean requestMutualAuth  = false;
    private boolean requestReplayDet   = false;
    private boolean requestSequenceDet = false;
    private boolean requestConf        = false;
    private boolean requestInteg       = false;
    private boolean requestAnonymity   = false;
    private boolean isProtReady        = false;
    private boolean isTransferable     = false;

    /* designates if context is initiator or acceptor */
    private boolean initiator = false;

    /* keep track of context state */
    private State ctxState = State.UNINIT;

    /* create context on the initiator side */
    public GSSContextImpl(GSSName peer, Oid mech, GSSCredential myCred,
            int lifetime) throws GSSException {
        
        if (peer != null) {
            this.targetName = (GSSNameImpl)peer;
        } else {
            throw new GSSException(GSSException.BAD_NAME);
        }

        if (mech == null) {
            /* default native mech = gss_mech_krb5 */
            this.mech = new OidImpl("1.2.840.113554.1.2.2");
        } else {
            this.mech = new OidImpl(mech.toString());
        }

        if (myCred != null) {
            this.credential = (GSSCredentialImpl) myCred;
            this.srcName = (GSSNameImpl) myCred.getName();
        } else {
            /* create GSS_C_NO_CREDENTIAL */
            this.credential =  new GSSCredentialImpl();
            this.credential.setInternGSSCred(null);
        }

        if (lifetime >= 0) {
            this.lifetime = convertLifetime(lifetime);
        }

        this.invalid = false;
        this.initiator = true;
    }

    /* create context on the acceptor side
     *
     * If null is passed in for myCred, we should set up a default
     * acceptor principal using:
     * 
     * credential = GSS_C_NO_CREDENTIAL,
     * channelBinding = GSS_C_NO_CHANNEL_BINDINGS,
     * srcName = GSS_C_NO_NAME,
     * targetName = ?
     */
    public GSSContextImpl(GSSCredential myCred) throws GSSException {

        if (myCred != null) {

            credential = (GSSCredentialImpl) myCred;
            targetName = (GSSNameImpl) myCred.getName();
            
        } else {

            /* create GSS_C_NO_CREDENTIAL */
            credential =  new GSSCredentialImpl();
            credential.setInternGSSCred(null);

            /* create GSS_C_NO_NAME */
            srcName = new GSSNameImpl();
            srcName.setInternGSSName(null);

        }

        this.invalid = false;
        this.initiator = false;
    }

    /* create context from exported inter-process token */
    public GSSContextImpl(byte[] interProcessToken) throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};

        gss_name_t_desc src_name = new gss_name_t_desc();
        gss_name_t_desc targ_name = new gss_name_t_desc();
        gss_buffer_desc sname = new gss_buffer_desc();
        gss_buffer_desc tname = new gss_buffer_desc();
        gss_OID_desc mechanism = new gss_OID_desc();
        gss_OID_desc name_type = new gss_OID_desc();
        long[] lifetime = {0};
        long[] context_flags = {0};
        int[] is_local = {0};
        int[] is_open = {0};
        
        gss_buffer_desc processToken = new gss_buffer_desc();
        gsswrapper.setDescArray(processToken, interProcessToken);
        processToken.setLength(interProcessToken.length);

        /* import context from token */
        maj_status = gsswrapper.gss_import_sec_context(min_status,
                processToken, this.internGSSCtx);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }

        /* get information about context */
        maj_status = gsswrapper.gss_inquire_context(min_status,
                this.internGSSCtx, src_name, targ_name, lifetime,
                mechanism, context_flags, is_local, is_open);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }

        /* store src name */
        maj_status = gsswrapper.gss_display_name(min_status, src_name,
                sname, name_type);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        } else {
            srcName = new GSSNameImpl();
            srcName.setInternGSSName(src_name);
        }

        /* store target name */
        maj_status = gsswrapper.gss_display_name(min_status, targ_name,
                tname, name_type);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        } else {
            targetName = new GSSNameImpl();
            targetName.setInternGSSName(targ_name);
        }

        /* store additional context information in GSSContext object */
        mech = new OidImpl(mechanism.toDotString());
        this.lifetime = lifetime[0];
        setReturnedFlags(context_flags[0]);

        if (is_open[0] == 1) {
            this.ctxState = State.DONE;
        }
        this.invalid = false;

        /* free native structures */
        gsswrapper.gss_release_buffer(min_status, sname);
        gsswrapper.gss_release_buffer(min_status, tname);
        gsswrapper.gss_release_buffer(min_status, processToken);

    }

    public byte[] initSecContext(byte[] inputBuf, int offset, int len) 
        throws GSSException {

        int ret = 0;
        
        ByteArrayInputStream bis = new ByteArrayInputStream(inputBuf,
                offset, len);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ret = initSecContext(bis, bos); 
        return bos.toByteArray();
    }

    /* 
     * returns the number of bytes written to the OutpuStream, or 0 if
     * none need to be written 
     */
    public int initSecContext(InputStream inStream, OutputStream outStream) 
        throws GSSException {

        int ret              = 0;
        long maj_status      = 0;
        long[] min_status    = {0};
        long[] actual_flags  = {0};
        long[] time_rec      = {0};
        byte[] inputTokArray = null;;
        byte[] temp_token;
        gss_ctx_id_t_desc context_tmp;
        gss_OID_desc actual_mech_type = new gss_OID_desc();
        gss_buffer_desc outputToken = new gss_buffer_desc();
        gss_buffer_desc inputToken = new gss_buffer_desc();
        ByteArrayOutputStream outTok = new ByteArrayOutputStream();

        /* setup temporary context */
        if (ctxState == State.UNINIT) {
            context_tmp = gsswrapper.GSS_C_NO_CONTEXT;
        } else {
            context_tmp = internGSSCtx;
        }

        /* setup output token */
        outputToken.setLength(0);
        outputToken.setValue(null);

        try {
            if (inStream.available() > 0) {

                /* retrieve token header to get token length */
                DerHeader tokHeader = DerUtil.getHeader(inStream);

                /* put our header bytes back */
                outTok.write(tokHeader.getBytes());

                /* copy over the rest of our input token */
                for (int i = 0; i < tokHeader.getLength(); i++) {
                    outTok.write(inStream.read());
                }
            }

        } catch (IOException e) {
            /* I/O Error occurred when reading InputStream */
            throw new GSSException(GSSException.FAILURE);
        }
       
        /* read token -> byte array -> gss_buffer_desc for native gssapi */
        inputTokArray = outTok.toByteArray();
        if (inputTokArray != null && inputTokArray.length > 0) {
            gsswrapper.setDescArray(inputToken, inputTokArray);
            inputToken.setLength(inputTokArray.length);
        }

        /* get requested flags */
        long requestedFlags = getRequestedFlags();

        maj_status = gsswrapper.gss_init_sec_context(min_status,
                credential.getInternGSSCred(),
                context_tmp,
                targetName.getInternGSSName(),
                mech.getNativeOid(),
                requestedFlags,
                lifetime,
                channelBinding.getNativeChannelBindings(),
                inputToken,
                actual_mech_type,
                outputToken,
                actual_flags,
                time_rec);

        if (maj_status == gsswrapper.GSS_S_CONTINUE_NEEDED) {
            /* gss_init_sec_context needs to be called again with output
               token from gss_accept_sec_context. */
            ctxState = State.INPROG;
        } else if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        } else {
            ctxState = State.DONE;
        }

        /* save native context */
        internGSSCtx = context_tmp;

        /* set some context variables */
        this.lifetime = (int) time_rec[0];
        mech = new OidImpl(actual_mech_type.toDotString());
        setReturnedFlags(actual_flags[0]);

        /* set our context as an initiator */
        this.initiator = true;

        /* place output token into OutputStream */ 
        if (outputToken.getLength() > 0) {
            temp_token = new byte[(int) outputToken.getLength()];
            temp_token = gsswrapper.getDescArray(outputToken);
            try {
                outStream.write(temp_token);
            } catch (IOException e) {
                throw new GSSException(GSSException.FAILURE);
            }
            ret = temp_token.length;
        } else {
            ret = 0;
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputToken);
        gsswrapper.gss_release_buffer(min_status, inputToken);

        return ret;

    } /* end initSecContext() */

    public byte[] acceptSecContext(byte[] inTok, int offset, int len)
        throws GSSException{

        ByteArrayInputStream bis = new ByteArrayInputStream(inTok,
                offset, len);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        acceptSecContext(bis, bos);

        if (bos.size() > 0) {
            return bos.toByteArray();
        } else {
            return null;
        }

    } /* end acceptSecContext() */

    public void acceptSecContext(InputStream inStream, OutputStream outStream)
        throws GSSException {

        long maj_status     = 0;
        long requestedFlags = 0;
        long[] min_status   = {0};
        long[] actual_flags = {0};
        long[] time_rec     = {0};
        byte[] inputTokArray;
        gss_ctx_id_t_desc context_tmp;
        gss_OID_desc actual_mech_type = new gss_OID_desc();
        gss_buffer_desc outputToken = new gss_buffer_desc();
        gss_buffer_desc inputToken = new gss_buffer_desc();
        gss_cred_id_t_desc delegatedCred = new gss_cred_id_t_desc();
        ByteArrayOutputStream outTok = new ByteArrayOutputStream();

        /* setup temporary context */
        if (ctxState == State.UNINIT) {
            context_tmp = gsswrapper.GSS_C_NO_CONTEXT;
        } else {
            context_tmp = internGSSCtx;
        }

        /* if srcName is null, set it up */
        if (srcName == null) {
            srcName = new GSSNameImpl();
            srcName.setInternGSSName(null);
        }

        /* setup output token */
        outputToken.setLength(0);
        outputToken.setValue(null);

        try {
            if (inStream.available() > 0) {

                /* retrieve token header to get token length */
                DerHeader tokHeader = DerUtil.getHeader(inStream);

                /* put our header bytes back */
                outTok.write(tokHeader.getBytes());

                /* copy over the rest of the input token */
                for (int i = 0; i < tokHeader.getLength(); i++) {
                    outTok.write(inStream.read());
                }
            }

        } catch (IOException e) {
            /* I/O Error occurred when reading InputStream */
            throw new GSSException(GSSException.FAILURE);
        }

        /* read token into byte array, gss_buffer_desc for native gssapi */
        inputTokArray = outTok.toByteArray();
        if (inputTokArray != null && inputTokArray.length > 0) {
            gsswrapper.setDescArray(inputToken, inputTokArray);
            inputToken.setLength(inputTokArray.length);
        }

        requestedFlags = getRequestedFlags();

        maj_status = gsswrapper.gss_accept_sec_context(min_status,
                context_tmp,
                credential.getInternGSSCred(),
                inputToken,
                channelBinding.getNativeChannelBindings(),
                srcName.getInternGSSName(),
                actual_mech_type,
                outputToken,
                actual_flags,
                time_rec,
                delegatedCred);

        if (maj_status == gsswrapper.GSS_S_CONTINUE_NEEDED) {
            ctxState = State.INPROG;
        } else if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        } else {
            ctxState = State.DONE;
        }

        /* set context */
        internGSSCtx = context_tmp;

        /* save some context variables */
        this.lifetime = time_rec[0];
        mech = new OidImpl(actual_mech_type.toDotString());
        setReturnedFlags(actual_flags[0]);

        if (delegCredential == null)
            delegCredential = new GSSCredentialImpl();
        delegCredential.setInternGSSCred(delegatedCred);
        
        /* set our context as an acceptor */
        this.initiator = false;

        /* place output token into OutputStream */
        if (outputToken.getLength() > 0) {
            byte[] temp_token = new byte[(int) outputToken.getLength()];
            temp_token = gsswrapper.getDescArray(outputToken);
            try {
                outStream.write(temp_token);
            } catch (IOException e) {
                throw new GSSException(GSSException.FAILURE);
            }
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputToken);
        gsswrapper.gss_release_buffer(min_status, inputToken);
        gsswrapper.gss_release_cred(min_status, delegatedCred);

    } /* end acceptSecContext() */

    public boolean isEstablished() {

        if (ctxState == State.DONE)
            return true;
        return false;

    }

    /**
     * Deletes one end of the security context, also deleting local data
     * structures associated with the security context. This needs to be 
     * called on both initiator and acceptor sides to close both ends of 
     * the security context.
     */
    public void dispose() throws GSSException {
        
        if (!invalid) { 

            long maj_status   = 0;
            long[] min_status = {0};
            gss_buffer_desc output_token = gsswrapper.GSS_C_NO_BUFFER;

            maj_status = gsswrapper.gss_delete_sec_context(min_status, 
                    this.internGSSCtx, 
                    output_token);

            if (maj_status != gsswrapper.GSS_S_COMPLETE) {
                throw new GSSExceptionImpl((int) maj_status, 
                        (int) min_status[0]);
            }
            gsswrapper.gss_release_buffer(min_status, output_token);

            if (mech != null)
                mech.freeOid();

            if (srcName != null)
                srcName.freeGSSName();

            if (targetName != null)
                targetName.freeGSSName();

            if (delegCredential != null)
                delegCredential.freeGSSCredential();

            this.invalid = true;
            ctxState = State.DISPOSED;
            srcName = null;
            targetName = null;
            credential = null;
            delegCredential = null;
        }

    } /* end dispose() */

    public int getWrapSizeLimit(int qop, boolean confReq, int maxTokenSize)
        throws GSSException {

        long maj_status   = 0;
        long[] min_status = {0};
        long[] max_size   = {0};
        int conf_req      = 0;

        if(confReq)
            conf_req = 1;

        maj_status = gsswrapper.gss_wrap_size_limit(min_status,
                this.internGSSCtx, conf_req, (long) qop, 
                (long) maxTokenSize, max_size);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }

        return (int)max_size[0];

    } /* end getWrapSizeLimit() */

    public byte[] wrap(byte[] inBuf, int offset, int len, MessageProp msgProp)
        throws GSSException {

       ByteArrayInputStream bis = new ByteArrayInputStream(inBuf, offset, len);
       ByteArrayOutputStream bos = new ByteArrayOutputStream();

       wrap(bis, bos, msgProp);

       return bos.toByteArray();

    } /* end wrap() */

    public void wrap(InputStream inStream, OutputStream outStream, 
            MessageProp msgProp) throws GSSException {

        int conf_req      = 0;
        int numBytes      = 0;
        int[] state       = {0};
        long maj_status   = 0;
        long[] min_status = {0};
        byte[] inMsg, temp_msg;
        gss_buffer_desc outputMsg = new gss_buffer_desc();
        gss_buffer_desc inputMsg = new gss_buffer_desc();
        ByteArrayOutputStream inMsgTmp = new ByteArrayOutputStream();

        try {

            /* grab the msg byte[] from inputStream */
            numBytes = inStream.available();
            inMsg = new byte[numBytes];
            for (int i = 0; i < numBytes; i++) {
                inMsgTmp.write(inStream.read());
            }

        } catch (IOException e) {
            throw new GSSException(GSSException.FAILURE);
        }
        inMsg = inMsgTmp.toByteArray();

        /* copy input message to gss_buffer_desc for native wrap() */
        if (inMsg != null) {
            gsswrapper.setDescArray(inputMsg, inMsg);
            inputMsg.setLength(inMsg.length);
        }

        /* determine requested level of confidentiality and integrity */
        if (msgProp.getPrivacy())
            conf_req = 1;

        maj_status = gsswrapper.gss_wrap(min_status, this.internGSSCtx,
                conf_req, msgProp.getQOP(), inputMsg, state, outputMsg);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }
        
        /* set the actual privacy state applied for caller to use */
        if (state[0] == 0) {
            msgProp.setPrivacy(false);
        } else {
            msgProp.setPrivacy(true);
        }

        /* get the byte[] from our outputMsg, copy back to OutputStream */
        if (outputMsg.getLength() > 0) {
            temp_msg = new byte[(int) outputMsg.getLength()];
            temp_msg = gsswrapper.getDescArray(outputMsg);
            try {
                outStream.write(temp_msg);
            } catch (IOException e) {
                throw new GSSException(GSSException.FAILURE);
            }
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputMsg);
        gsswrapper.gss_release_buffer(min_status, inputMsg);
        
    } /* end wrap() */

    public byte[] unwrap(byte[] inBuf, int offset, int len,
            MessageProp msgProp) throws GSSException {

        ByteArrayInputStream bis = new ByteArrayInputStream(inBuf, offset, len);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        unwrap(bis, bos, msgProp);

        return bos.toByteArray();

    } /* end unwrap() */

    public void unwrap(InputStream inStream, OutputStream outStream,
            MessageProp msgProp) throws GSSException {

        int numBytes      = 0;
        int[] conf_state  = {0};
        long maj_status   = 0;
        long[] min_status = {0};
        long[] qop_state  = {0};
        byte[] inMsg;
        boolean tokDup    = false;
        boolean tokOld    = false;
        boolean tokUnseq  = false;
        boolean tokGap    = false;
        gss_buffer_desc outputMsg = new gss_buffer_desc();
        gss_buffer_desc inputMsg = new gss_buffer_desc();
        ByteArrayOutputStream inMsgTmp = new ByteArrayOutputStream();

        try {

            /* grab the msg byte[] from inputStream */
            numBytes = inStream.available();
            inMsg = new byte[numBytes];
            for (int i = 0; i < numBytes; i++) {
                inMsgTmp.write(inStream.read());
            }

        } catch (IOException e) {
            throw new GSSException(GSSException.FAILURE);
        }
        inMsg = inMsgTmp.toByteArray();

        /* copy input message to gss_buffer_desc for native unwrap() */
        if (inMsg != null) {
            gsswrapper.setDescArray(inputMsg, inMsg);
            inputMsg.setLength(inMsg.length);
        }

        maj_status = gsswrapper.gss_unwrap(min_status, this.internGSSCtx,
                inputMsg, outputMsg, conf_state, qop_state);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        } else {

            /* set supplementary info */
            if ((maj_status & gsswrapper.GSS_S_DUPLICATE_TOKEN) != 0)
                tokDup =  true;
            if ((maj_status & gsswrapper.GSS_S_OLD_TOKEN) != 0)
                tokOld =  true;
            if ((maj_status & gsswrapper.GSS_S_UNSEQ_TOKEN) != 0)
                tokUnseq =  true;
            if ((maj_status & gsswrapper.GSS_S_GAP_TOKEN) != 0)
                tokGap =  true;

            msgProp.setSupplementaryStates(tokDup, tokOld, tokUnseq, tokGap,
                    (int)min_status[0], ""); 
        }

        /* set the actual privacy and conf state applied for caller to use */
        if (conf_state[0] == 0) {
            msgProp.setPrivacy(false);
        } else {
            msgProp.setPrivacy(true);
        }

        msgProp.setQOP((int) qop_state[0]);

        /* get the byte[] from our outputMsg, copy back to OutputStream */
        if (outputMsg.getLength() > 0) {
            byte[] temp_msg = new byte[(int) outputMsg.getLength()];
            temp_msg = gsswrapper.getDescArray(outputMsg);
            gsswrapper.gss_release_buffer(min_status, outputMsg);
            try {
                outStream.write(temp_msg);
            } catch (IOException e) {
                throw new GSSException(GSSException.FAILURE);
            }
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputMsg);
        gsswrapper.gss_release_buffer(min_status, inputMsg);

    } /* end unwrap() */

    public byte[] getMIC(byte[] inMsg, int offset, int len,
            MessageProp msgProp) throws GSSException {

        ByteArrayInputStream bis = new ByteArrayInputStream(inMsg, offset, len);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        getMIC(bis, bos, msgProp);

        return bos.toByteArray();

    } /* end getMIC() */

    public void getMIC(InputStream inStream, OutputStream outStream,
            MessageProp msgProp) throws GSSException {
       
        int numBytes      = 0; 
        int desiredQOP    = 0;
        long maj_status   = 0;
        long[] min_status = {0};
        byte[] inMsg;
        gss_buffer_desc outputMsg = new gss_buffer_desc();
        gss_buffer_desc inputMsg = new gss_buffer_desc();
        ByteArrayOutputStream inMsgTmp = new ByteArrayOutputStream();

        try {

            /* grab the msg byte[] from InputStream */
            numBytes = inStream.available();
            inMsg = new byte[numBytes];
            for (int i = 0; i < numBytes; i++) {
                inMsgTmp.write(inStream.read());
            }
        } catch (IOException e) {
            throw new GSSException(GSSException.FAILURE);
        }
        inMsg = inMsgTmp.toByteArray();

        /* copy input message to gss_buffer_desc for native function */
        if (inMsg != null) {
            gsswrapper.setDescArray(inputMsg, inMsg);
            inputMsg.setLength(inMsg.length);
        }

        /* set default QOP if msgProp is null */
        if (msgProp != null)
            desiredQOP = msgProp.getQOP();

        maj_status = gsswrapper.gss_get_mic(min_status, this.internGSSCtx,
                msgProp.getQOP(), inputMsg, outputMsg);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }

        /* get the byte[] from our outputMsg, copy back to OutputStream */
        if (outputMsg.getLength() > 0) {
            byte[] temp_msg = new byte[(int) outputMsg.getLength()];
            temp_msg = gsswrapper.getDescArray(outputMsg);
            try {
                outStream.write(temp_msg);
            } catch (IOException e) {
                throw new GSSException(GSSException.FAILURE);
            }
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputMsg);
        gsswrapper.gss_release_buffer(min_status, inputMsg);

    } /* end getMIC() */

    public void verifyMIC(byte[] inTok, int tokOffset, int tokLen,
            byte[] inMsg, int msgOffset, int msgLen,
            MessageProp msgProp) throws GSSException {

        ByteArrayInputStream tis = new ByteArrayInputStream(inTok,
                tokOffset, tokLen);
        ByteArrayInputStream mis = new ByteArrayInputStream(inMsg,
                msgOffset, msgLen);

        verifyMIC(tis, mis, msgProp);

    } /* end verifyMIC() */

    public void verifyMIC(InputStream tokStream, InputStream msgStream,
            MessageProp msgProp) throws GSSException {

        int numBytes;
        long maj_status   = 0;
        long[] min_status = {0};
        long[] qop_state  = {0};
        byte[] inTok, inMsg;
        boolean tokDup    = false;
        boolean tokOld    = false;
        boolean tokUnseq  = false;
        boolean tokGap    = false;
        gss_buffer_desc inputTok = new gss_buffer_desc();
        gss_buffer_desc inputMsg = new gss_buffer_desc();
        ByteArrayOutputStream inTokTmp = new ByteArrayOutputStream();
        ByteArrayOutputStream inMsgTmp = new ByteArrayOutputStream();

        try {

            /* grab the token byte[] from tokStream */
            numBytes = tokStream.available();
            inTok = new byte[numBytes];
            for (int i = 0; i < numBytes; i++) {
                inTokTmp.write(tokStream.read());
            }

            /* grab the msg byte[] from msgStream */
            numBytes = msgStream.available();
            inMsg = new byte[numBytes];
            for (int i = 0; i < numBytes; i++) {
                inMsgTmp.write(msgStream.read());
            }

        } catch (IOException e) {
            throw new GSSException(GSSException.FAILURE);
        }

        inTok = inTokTmp.toByteArray();
        inMsg = inMsgTmp.toByteArray();

        /* copy input token to gss_buffer_desc */
        if (inTok != null) {
            gsswrapper.setDescArray(inputTok, inTok);
            inputTok.setLength(inTok.length);
        }

        /* copy input msg to gss_buffer_desc */
        if (inMsg != null) {
            gsswrapper.setDescArray(inputMsg, inMsg);
            inputMsg.setLength(inMsg.length);
        }

        maj_status = gsswrapper.gss_verify_mic(min_status,
                this.internGSSCtx, inputMsg, inputTok, qop_state);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        } else {

            /* save supplementary info */
            if ((maj_status & gsswrapper.GSS_S_DUPLICATE_TOKEN) != 0)
                tokDup = true;
            if ((maj_status & gsswrapper.GSS_S_OLD_TOKEN) != 0)
                tokOld = true;
            if ((maj_status & gsswrapper.GSS_S_UNSEQ_TOKEN) != 0)
                tokUnseq = true;
            if ((maj_status & gsswrapper.GSS_S_GAP_TOKEN) != 0)
                tokGap = true;

            msgProp.setSupplementaryStates(tokDup, tokOld, tokUnseq, tokGap,
                    (int) min_status[0], "");
        }

        /* save qop */
        msgProp.setQOP((int) qop_state[0]);

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, inputTok);
        gsswrapper.gss_release_buffer(min_status, inputMsg);

    } /* end verifyMIC() */

    public byte[] export() throws GSSException {

        long maj_status   = 0;
        long[] min_status = {0};
        byte[] outTok     = null;
        gss_buffer_desc outputToken = new gss_buffer_desc();

        maj_status = gsswrapper.gss_export_sec_context(min_status,
                this.internGSSCtx,
                outputToken);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int) maj_status, (int) min_status[0]);
        }

        if (outputToken.getLength() > 0) {
            outTok = new byte[(int) outputToken.getLength()];
            outTok = gsswrapper.getDescArray(outputToken);
        }

        /* release native structures */
        gsswrapper.gss_release_buffer(min_status, outputToken);

        return outTok;

    } /* end export() */

    public void requestMutualAuth(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestMutualAuth = true;
    }

    public void requestReplayDet(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestReplayDet = true;
    }

    public void requestSequenceDet(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestSequenceDet = true;
    }

    public void requestCredDeleg(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestCredDeleg = true;
    }

    public void requestAnonymity(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestAnonymity = true;
    }

    public void requestConf(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestConf = true;
    }

    public void requestInteg(boolean state) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            requestInteg = true;
    }

    public void requestLifetime(int lifetime) throws GSSException {
        if (ctxState == State.UNINIT && initiator)
            this.lifetime = lifetime;
    }

    public void setChannelBinding(ChannelBinding cb) throws GSSException {
        if (ctxState == State.UNINIT)
            channelBinding = (ChannelBindingImpl) cb;
    }

    public boolean getCredDelegState() {
        return requestCredDeleg;
    }

    public boolean getMutualAuthState() {
        return requestMutualAuth;
    }

    public boolean getReplayDetState() {
        return requestReplayDet;
    }

    public boolean getSequenceDetState() {
        return requestSequenceDet;
    }

    public boolean getAnonymityState() {
        return requestAnonymity;
    }

    public boolean isTransferable() throws GSSException {
        if (ctxState == State.DONE)
            return isTransferable;
        return false;
    }

    public boolean isProtReady() {
        return isProtReady;
    }

    public boolean getConfState() {
        return requestConf;
    }

    public boolean getIntegState() {
        return requestInteg;
    }

    public int getLifetime() {
        if (lifetime == gsswrapper.GSS_C_INDEFINITE) {
            return GSSContext.INDEFINITE_LIFETIME;
        }
        return (int) lifetime;
    }

    public GSSName getSrcName() {
        if (ctxState == State.DONE || isProtReady())
            return srcName;
        return null;
    }

    public GSSName getTargName() {
        if (ctxState == State.DONE || isProtReady())
            return targetName;
        return null;
    }

    public Oid getMech() throws GSSException {
        return mech;
    }

    public GSSCredential getDelegCred() throws GSSException {
        if (ctxState == State.DONE) {
            return delegCredential;
        }
        return null;
    }

    public boolean isInitiator() throws GSSException {
        if (ctxState == State.INPROG || ctxState == State.DONE)
            return initiator;
        return false;
    }

    private long getRequestedFlags() {
    
        long requestedFlags = 0;

        if (requestCredDeleg)
            requestedFlags ^= gsswrapper.GSS_C_DELEG_FLAG;
        if (requestMutualAuth)
            requestedFlags ^= gsswrapper.GSS_C_MUTUAL_FLAG;
        if (requestReplayDet)
            requestedFlags ^= gsswrapper.GSS_C_REPLAY_FLAG;
        if (requestSequenceDet)
            requestedFlags ^= gsswrapper.GSS_C_SEQUENCE_FLAG;
        if (requestConf)
            requestedFlags ^= gsswrapper.GSS_C_CONF_FLAG;
        if (requestInteg)
            requestedFlags ^= gsswrapper.GSS_C_INTEG_FLAG;
        if (requestAnonymity)
            requestedFlags ^= gsswrapper.GSS_C_ANON_FLAG;

        return requestedFlags;

    } /* end getRequestedFlags() */

    private void setReturnedFlags(long retFlags) {

        /* since request*() methods are only valid before context
           creation, we'll just re-use those variables to indicate the
           current flags of the established context as well */

        // clear current flag values
        requestAnonymity = false;
        requestConf = false;
        requestCredDeleg = false;
        requestInteg = false;
        requestMutualAuth = false;
        requestReplayDet = false;
        requestSequenceDet = false;
        isProtReady = false;
        isTransferable = false;

        // set the new flags
        if ((retFlags & gsswrapper.GSS_C_ANON_FLAG) != 0)
            requestAnonymity = true;
        if ((retFlags & gsswrapper.GSS_C_CONF_FLAG) != 0)
            requestConf = true;
        if ((retFlags & gsswrapper.GSS_C_DELEG_FLAG) != 0)
            requestCredDeleg = true;
        if ((retFlags & gsswrapper.GSS_C_INTEG_FLAG) != 0)
            requestInteg = true;
        if ((retFlags & gsswrapper.GSS_C_MUTUAL_FLAG) != 0)
            requestMutualAuth = true;
        if ((retFlags & gsswrapper.GSS_C_REPLAY_FLAG) != 0)
            requestReplayDet = true;
        if ((retFlags & gsswrapper.GSS_C_SEQUENCE_FLAG) != 0)
            requestSequenceDet = true;
        if ((retFlags & gsswrapper.GSS_C_PROT_READY_FLAG) != 0)
            isProtReady = true;
        if ((retFlags & gsswrapper.GSS_C_TRANS_FLAG) != 0)
            isTransferable = true;

    } /* end setReturnedFlags() */

    private long convertLifetime(int smLife) {

        if (smLife == GSSContext.INDEFINITE_LIFETIME) {
            return gsswrapper.GSS_C_INDEFINITE;
        }

        return (long) smLife;

    } /* end convertLifetime() */

}
