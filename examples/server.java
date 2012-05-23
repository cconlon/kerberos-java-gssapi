/* server.java - Example GSS-API Java server */
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

/*
 * Original source developed by yaSSL (http://www.yassl.com)
 *
 * Description: 
 * A simple server application which uses the MIT Kerberos Java GSS-API 
 * SWIG wrapper. The following actions are taken by the server:
 *      a) Establish a GSS-API context with a client.
 *      b) Unwraps a signed and encrypted message that the client sends,
 *         using gss_unwrap.
 *      c) Generates and sends a signature block for the received message
 *         using gss_get_mic.
 *      d) Repeats steps b) and c) using gss_unseal / gss_sign.
 *
 */
import java.io.*;
import java.net.*;
import edu.mit.jgss.swig.*;

class server implements gsswrapperConstants
{
    public static gss_ctx_id_t_desc context = new gss_ctx_id_t_desc();

    public static void main(String argv[]) throws Exception
    {
        int server_port = 11115;
        String serviceName = "service@ubuntu.local";
        int ret = 0;
        int authorizationError = 0;
        long maj_status = 0;
        long[] min_status = {0};
        String clientMsg;
        gss_ctx_id_t_desc gssContext =  GSS_C_NO_CONTEXT;
        gss_cred_id_t_desc serverCreds = new gss_cred_id_t_desc();

        /* create input and output streams */
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        OutputStream clientOut = null;
        InputStream clientIn = null;

        /* create server socket */
        try {
            serverSocket = new ServerSocket(server_port);
        } catch (IOException e) {
            System.out.println("Error on port: " + server_port + ", " + e);
            System.exit(1);
        }

        System.out.println("Started Java GSS-API server example, " +
                           "waiting for client connection...");

        while(true)
        {
            try {
               
                clientSocket = serverSocket.accept();
                
                /* get input and output streams */
                clientOut = clientSocket.getOutputStream();
                clientIn = clientSocket.getInputStream();
                System.out.println("Client connection received");

            } catch (IOException e) {
                System.out.println("Server did not accept connection: " + e);
            }

            /* Import service name and acquire creds for it,
             * returns NULL on failure */
            serverCreds = AcquireServerCreds(serviceName);

            if (serverCreds != null) {
                System.out.println("Finished acquiring server creds");
                /* Stores created context in global static "context" */
                ret = Authenticate(clientSocket, clientIn, clientOut);

                if (ret == 0) {
                    System.out.println("Finished Authentication");
                    /* Using gss_unwrap / gss_get_mic */    
                    ret = Communicate(clientSocket, clientIn, clientOut);

                    if (ret == 0) {
                        System.out.println("Finished first communication " +
                                           "with server");
                        /* Using gss_unseal / gss_sign */
                        ret = AltCommunicate(clientSocket, clientIn,
                                             clientOut);

                        if (ret == 0) {
                            System.out.println("Finished second " +
                                               "communication with server");
                        } else {
                            System.out.println("Failed during second " +
                                               "communication with server");
                        }
                    } else {
                        System.out.println("Failed during first " +
                                           "communication with server");
                    }
                } else {
                    System.out.println("Failed during Authentication");
                }
            } else {
                System.out.println("Failed when acquiring server creds");
            }   
            
            if (ret == 0) {
                System.out.println("SUCCESS!");
                gsswrapper.gss_release_cred(min_status, serverCreds);

                /* Delete established GSSAPI context */ 
                gss_buffer_desc output_token = GSS_C_NO_BUFFER;
                gss_OID_desc tmp = context.getMech_type();
                maj_status = gsswrapper.gss_delete_sec_context(min_status, 
                        context, output_token);
                if (maj_status != GSS_S_COMPLETE) {
                    Util.displayError("deleting security context", 
                                      min_status, maj_status);
                }
            } else {
                System.out.println("FAILURE!");
                continue;
            }

            clientOut.close();
            clientIn.close();
            clientSocket.close();
        }
    }

    public static gss_cred_id_t_desc AcquireServerCreds(String serviceName)
    {
        gss_cred_id_t_desc server_creds = new gss_cred_id_t_desc();
        gss_buffer_desc name_buf = new gss_buffer_desc(serviceName);
        gss_name_t_desc server_name = new gss_name_t_desc();
        gss_OID_set_desc actual_mechs =  GSS_C_NO_OID_SET;
        long maj_status = 0;
        long[] min_status = {0};
        long[] time_rec = {0};

        maj_status = gsswrapper.gss_import_name(min_status, name_buf,
                gsswrapper.getGSS_C_NT_HOSTBASED_SERVICE(), server_name);

        if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("gss_import_name, serviceName", 
                                  min_status, maj_status);
                return null;
        }

        maj_status = gsswrapper.gss_acquire_cred(min_status, server_name,
                0, actual_mechs,
                GSS_C_ACCEPT,
                server_creds, null, time_rec);
        if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("gss_acquire_cred(server_name)", 
                                  min_status, maj_status);
                return null;
        }

        gsswrapper.gss_release_name(min_status, server_name);

        return server_creds;            
    }

    public static int Authenticate(Socket inSocket,
            InputStream clientIn, 
            OutputStream clientOut)
    {
        int err = 0;
        long maj_status = 0;
        long[] min_status = {0};
        gss_ctx_id_t_desc context_tmp = GSS_C_NO_CONTEXT;

        byte[] inputTokenBuffer = null;
        gss_buffer_desc inputToken = new gss_buffer_desc();

        System.out.println("Authenticating...");

        /* The main authentication loop.  We need to loop reading "input
         * tokens" from the client, calling gss_accept_sec_context on the
         * "input tokens" and send the resulting "output tokens" back to
         * the client until we get GSS_S_COMPLETE or an error */

        maj_status = GSS_S_CONTINUE_NEEDED;
        while ( (err == 0) && 
                (maj_status != GSS_S_COMPLETE)) {

            /* Clean up old input buffer */
            if (inputTokenBuffer != null)
                inputTokenBuffer = null;

            inputTokenBuffer = Util.ReadToken(clientIn);

            if (inputTokenBuffer != null) {
                /* Set up input buffers for the next run through the loop */
                gsswrapper.setDescArray(inputToken, inputTokenBuffer);
                inputToken.setLength(inputTokenBuffer.length);

                System.out.println("inputToken.value = " 
                                   + inputToken.getValue());
                System.out.println("inputToken.length = " 
                                   + inputToken.getLength());
            } else {
                System.out.println("Got bad token from client, " +
                                   "check client for error description.");
                return -1;
            }

            if (inputTokenBuffer != null) {
                gss_buffer_desc outputToken = new gss_buffer_desc();
                outputToken.setValue(null);
                outputToken.setLength(0);

                /* gss_accept_sec_context takes the client request and 
                 * generates an appropriate reply. Passing
                 * GSS_C_NO_CREDENTIAL for the service principal causes
                 * the server to accept any service princiapl in the
                 * server's keytab. */

                System.out.println("Calling gss_accept_sec_context...");
                long[] req_flags = {0};
                long[] time_rec = {0};
                gss_cred_id_t_desc acceptor_cred_handle =  GSS_C_NO_CREDENTIAL;
                gss_channel_bindings_struct input_chan_bindings = 
                    GSS_C_NO_CHANNEL_BINDINGS;
                maj_status = gsswrapper.gss_accept_sec_context(
                        min_status, context_tmp,
                        acceptor_cred_handle,
                        inputToken,
                        input_chan_bindings,
                        null, null, outputToken, req_flags, time_rec, null);

                if ( (outputToken.getLength() > 0) &&
                        (outputToken.getValue() != null) ) {
                    /* Send the output token to the client */
                    byte[] temp_token = new byte[(int)outputToken.getLength()];
                    temp_token = gsswrapper.getDescArray(outputToken);
                    System.out.println("temp_token.length = " 
                                       + temp_token.length);

                    err = Util.WriteToken(clientOut, temp_token);
                    if (err != 0) {
                        System.out.println("Error sending token to client");
                    }

                    System.out.println("outputToken.value = " 
                                       + outputToken.getValue());
                    System.out.println("outputToken.length = " 
                                       + outputToken.getLength());

                    /* free the output token */
                    gsswrapper.gss_release_buffer(min_status, outputToken);
                }
            }

            if ( (maj_status != GSS_S_COMPLETE) &&
                    (maj_status != GSS_S_CONTINUE_NEEDED)) 
            {
                Util.displayError("gss_accept_sec_context", 
                        min_status, maj_status);
                return -1;
            }
        }

        if (err == 0) {
            context = context_tmp;
        } else {
            System.out.println("Authenticate failed!");
            return -1;
        }

        return err;

    } /* end Authorize() */

    public static int Communicate(Socket inSocket,
            InputStream clientIn, 
            OutputStream clientOut)
    {
        long maj_status = 0;
        long[] min_status = {0};
        int[] conf_state = {0};
        long[] qop_state = {0};
        int err = 0;
        gss_buffer_desc in_buf = new gss_buffer_desc();
        gss_buffer_desc out_buf = new gss_buffer_desc();
        gss_buffer_desc mic_buf = new gss_buffer_desc();
        byte[] messageBuffer = null;


            /* Receive the message token */
            messageBuffer = Util.ReadToken(clientIn);

            if (messageBuffer != null) {
                gsswrapper.setDescArray(in_buf, messageBuffer);
                in_buf.setLength(messageBuffer.length);
            }
            
            /* Unwrap token */
            maj_status = gsswrapper.gss_unwrap(min_status, context,
                    in_buf, out_buf, conf_state, qop_state);

            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("unwrapping token, gss_unwrap", 
                                  min_status, maj_status);
                return -1;
            } else if (conf_state[0] == 0) {
                System.out.println("Warning!  Message not encrypted.");
            }

            System.out.println("Received message: " + out_buf.getValue());

            /* Produce a signature block for the message */
            maj_status = gsswrapper.gss_get_mic(min_status, context,
                    GSS_C_QOP_DEFAULT,
                    out_buf, mic_buf);

            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("producing signature block, gss_get_mic", 
                                  min_status, maj_status);
                return -1;
            }

            /* Send signature block to client */
            byte[] temp_token = new byte[(int)mic_buf.getLength()];
            temp_token = gsswrapper.getDescArray(mic_buf);
            err = Util.WriteToken(clientOut, temp_token);

            if (err != 0) {
                System.out.println("Error sending signature block to client");
                return -1;
            }
        
        gsswrapper.gss_release_buffer(min_status, in_buf);
        gsswrapper.gss_release_buffer(min_status, out_buf);
        gsswrapper.gss_release_buffer(min_status, mic_buf);

        return 0;

    } /* end Communicate() */
    
    public static int AltCommunicate(Socket inSocket,
            InputStream clientIn, 
            OutputStream clientOut)
    {
        long maj_status = 0;
        long[] min_status = {0};
        int[] conf_state = {0};
        int[] qop_state = {0};
        int err = 0;
        gss_buffer_desc in_buf = new gss_buffer_desc();
        gss_buffer_desc out_buf = new gss_buffer_desc();
        gss_buffer_desc mic_buf = new gss_buffer_desc();
        byte[] messageBuffer = null;

            /* Receive the message token */
            messageBuffer = Util.ReadToken(clientIn);

            if (messageBuffer != null) {
                gsswrapper.setDescArray(in_buf, messageBuffer);
                in_buf.setLength(messageBuffer.length);
            }
            
            /* Unwrap token */
            maj_status = gsswrapper.gss_unseal(min_status, context,
                    in_buf, out_buf, conf_state, qop_state);

            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("unwrapping token, gss_unseal", 
                                  min_status, maj_status);
                return -1;
            } else if (conf_state[0] == 0) {
                System.out.println("Warning!  Message not encrypted.");
            }

            System.out.println("Received message: " + out_buf.getValue());

            /* Produce a signature block for the message */
            maj_status = gsswrapper.gss_sign(min_status, context,
                    GSS_C_QOP_DEFAULT,
                    out_buf, mic_buf);

            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("producing signature block, gss_sign", 
                                  min_status, maj_status);
                return -1;
            }

            /* Send signature block to client */
            byte[] temp_token = new byte[(int)mic_buf.getLength()];
            temp_token = gsswrapper.getDescArray(mic_buf);
            err = Util.WriteToken(clientOut, temp_token);

            if (err != 0) {
                System.out.println("Error sending signature block to client");
                return -1;
            }
        
        gsswrapper.gss_release_buffer(min_status, in_buf);
        gsswrapper.gss_release_buffer(min_status, out_buf);
        gsswrapper.gss_release_buffer(min_status, mic_buf);

        return 0;

    } /* end AltCommunicate() */
}

