/* client.java - Example GSS-API Java client */
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
 * 
 * A simple client application which uses the MIT Kerberos Java GSS-API 
 * SWIG wrapper. The following actions are taken by the client:
 *      a) Establish a GSSAPI context with the example server
 *      b) Signs and encrypts, and sends a message to the server using
 *         gss_wrap.
 *      c) Verifies the signature block returned by the server with
 *         gss_verify_mic.
 *      d) Repeat steps b) and c) but using gss_seal / gss_verify
 *      e) Perform misc. GSSAPI function tests
 *
 */
import java.io.*;
import java.net.*;
import edu.mit.jgss.swig.*;

public class client implements gsswrapperConstants
{
    /* Global GSS-API context */
    public static gss_ctx_id_t_desc context = new gss_ctx_id_t_desc();

    public static void main(String argv[]) throws Exception
    {
        /* Return/OUTPUT variables */
        long maj_status = 0;
        long[] min_status = {0};
        long[] ret_flags = {0};
        long[] time_rec = {0};
   
        int ret = 0;
        int port = 11115;
        String server = "127.0.0.1";
        String clientName = "clientname";
        String serviceName = "service@host";
        
        /* Customize this if a specific mechanisms should be negotiated, 
           otherwise set neg_mech_set to GSS_C_NO_OID_SET */
        gss_OID_set_desc neg_mech_set = new gss_OID_set_desc();
        gss_OID_desc neg_mech = new gss_OID_desc("{ 1 2 840 113554 1 2 2 }");
        maj_status = gsswrapper.gss_add_oid_set_member(min_status, 
                neg_mech, neg_mech_set);

        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("adding oid to set", min_status, maj_status);
            System.exit(1);
        }
        
        Socket clientSocket = null;
        OutputStream serverOut = null;
        InputStream serverIn = null;
        String serverMsg;
       
        /* testing gss_oid_to_str */
        gss_buffer_desc buffer = new gss_buffer_desc();
        maj_status = gsswrapper.gss_oid_to_str(min_status, 
                gsswrapper.getGSS_C_NT_EXPORT_NAME(), buffer);

        if (maj_status != GSS_S_COMPLETE) {
            Util.errorExit("Error calling gss_oid_to_str", 
                           min_status, maj_status);
        }
        gsswrapper.gss_release_buffer(min_status, buffer);

        /* create socket to connect to the server */
        try {
            clientSocket = new Socket(server, port);
            System.out.println("Connected to " + server + " at port " + port);

            /* get input and output streams */
            serverOut = clientSocket.getOutputStream();
            serverIn = clientSocket.getInputStream();
        
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + server);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error for the connection to " + server);
            e.printStackTrace();
        }

        /* Stores created context in global static "context" variable */
        ret = Authenticate(clientSocket, serverIn, serverOut, clientName, 
                           serviceName, neg_mech_set);

        if (ret == 0) {
            System.out.println("Finished Authentication");
            ret = PrintContextInfo();

            if (ret == 0) {
                ret = Communicate(clientSocket, serverIn, serverOut);

                if (ret == 0) {
                    System.out.println("Finished first communication with " + 
                                       "server");
                    ret = AltCommunicate(clientSocket, serverIn, serverOut);

                    if (ret == 0) {
                        System.out.println("Finished second communication " + 
                                           "with server");
                        ret = MiscFunctionTests();
                    } else {
                        System.out.println("Failed during second " +
                                           "communication with server");
                    }
                } else {
                    System.out.println("Failed during first communication " +
                                       "with server");
                }
            } else {
                System.out.println("Failed during PrintContextInfo()");
            }
        } else {
            System.out.println("Failed during Authentication");
        }

        if (ret == 0) {
            System.out.println("SUCCESS!");
            gss_buffer_desc output_token = GSS_C_NO_BUFFER;
            gss_OID_desc tmp = context.getMech_type();
            maj_status = gsswrapper.gss_delete_sec_context(min_status, 
                    context, output_token);
            System.out.println("Deleted security context.");

            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("deleting security context", 
                                  min_status, maj_status);
            }

            gsswrapper.gss_release_buffer(min_status, output_token);

        } else {
            System.out.println("FAILURE!");
        }

        gsswrapper.gss_release_oid(min_status, neg_mech);
        gsswrapper.gss_release_oid_set(min_status, neg_mech_set);
        
        serverIn.close();
        serverOut.close();
        clientSocket.close();
    }

    public static int Authenticate(Socket clientSocket,
            InputStream serverIn, OutputStream serverOut,
            String inClientName, String inServiceName,
            gss_OID_set_desc neg_mech_set) 
    {
        long maj_status = 0;
        long[] min_status = {0};
        gss_name_t_desc clientName = new gss_name_t_desc();
        gss_name_t_desc serverName = new gss_name_t_desc();
        gss_cred_id_t_desc clientCredentials = GSS_C_NO_CREDENTIAL;
        gss_ctx_id_t_desc context_tmp = GSS_C_NO_CONTEXT;
        long[] actualFlags = {0};
        int err = 0;
        int ret[] = {0};
        long[] time_rec = {0};
        /* kerberos v5 */
        gss_OID_desc gss_mech_krb5 = new gss_OID_desc("1.2.840.113554.1.2.2");

        byte[] inputTokenBuffer = null;
        gss_buffer_desc inputToken = new gss_buffer_desc();

        System.out.println("Authenticating [client]");

        /* Testing gss_indicate_mechs */
        gss_OID_set_desc mech_set = GSS_C_NO_OID_SET;
        maj_status = gsswrapper.gss_indicate_mechs(min_status, mech_set);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_indicate_mechs(mech_set)", min_status,
                              maj_status);
            return -1;
        }

        maj_status = gsswrapper.gss_release_oid_set(min_status, mech_set);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_release_mechs(mech_set)", min_status,
                              maj_status);
            return -1;
        }

        /* Client picks client principal it wants to use.  Only done if we
         * know what client principal will get the service principal we need. 
         */
        if (inClientName != null) {
            gss_buffer_desc nameBuffer = new gss_buffer_desc();
            nameBuffer.setLength(inClientName.length());
            nameBuffer.setValue(inClientName);

            maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                    gsswrapper.getGSS_C_NT_USER_NAME(), clientName);
            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("gss_import_name(inClientName)", min_status,
                                  maj_status);
                return -1;
            }

            maj_status = gsswrapper.gss_acquire_cred(min_status, clientName,
                    GSS_C_INDEFINITE,
                    GSS_C_NO_OID_SET,
                    GSS_C_INITIATE, clientCredentials,
                    null, time_rec);
            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("gss_acquire_cred", min_status,
                                  maj_status);
                gsswrapper.gss_release_cred(min_status, clientCredentials);
                return -1;
            }

            /* Did we want a specific mechanism to be used? */
            if (neg_mech_set != GSS_C_NO_OID_SET) {
                maj_status = gsswrapper.gss_set_neg_mechs(min_status, 
                        clientCredentials,
                        neg_mech_set);

                if (maj_status != GSS_S_COMPLETE) {
                    Util.displayError("setting negotiation mechanism", 
                                      min_status, maj_status);
                    return -1;
                } else {
                    System.out.println("Successfully set neg. mechanism");
                }

            }
        }

        /* checking for valid import. Remember to run "kinit <clientName>" */
        long[] lifetime = {0};
        int[] cred_usage = {0};
        gss_name_t_desc name = new gss_name_t_desc();
        gss_OID_set_desc temp_mech_set = new gss_OID_set_desc();
        maj_status = gsswrapper.gss_inquire_cred(min_status,
                clientCredentials, name, lifetime, cred_usage, temp_mech_set);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_inquire_cred(temp_mech_set)", min_status,
                              maj_status);
            return -1;
        }
        System.out.println("Credential Valid for " 
                               + lifetime[0] + " seconds");
        maj_status = gsswrapper.gss_release_oid_set(min_status,
                temp_mech_set);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_release_oid_set(temp_mech_set)", min_status,
                              maj_status);
            return -1;
        }

        /* Test gss_duplicate_name function */
        gss_name_t_desc clientName_dup = new gss_name_t_desc();
        maj_status = gsswrapper.gss_duplicate_name(min_status,
                clientName, clientName_dup);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("duplicating client name", 
                              min_status, maj_status);
            return -1;
        }
        gsswrapper.gss_release_name(min_status, clientName_dup);

        /* Test gss_canonicalize_name function */
        gss_name_t_desc clientCanonicalized = new gss_name_t_desc();
        maj_status = gsswrapper.gss_canonicalize_name(min_status,
                clientName, gss_mech_krb5, clientCanonicalized);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("canonicalizing client name", 
                              min_status, maj_status);
            return -1;
        }

        /* Test gss_export_name function */
        gss_buffer_desc clientName_export = new gss_buffer_desc();
        maj_status = gsswrapper.gss_export_name(min_status, 
                clientCanonicalized,
                clientName_export);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("exporting client name", min_status, maj_status);
            return -1;
        }         
        gsswrapper.gss_release_name(min_status, clientCanonicalized);
        gsswrapper.gss_release_buffer(min_status, clientName_export);
       

        /* Client picks the service principal it will try to use to connect
         * to the server.  The server principal is given at the top of this
         * file.*/
        gss_buffer_desc nameBuffer = new gss_buffer_desc();
        nameBuffer.setLength(inServiceName.length());
        nameBuffer.setValue(inServiceName);

        maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                   gsswrapper.getGSS_C_NT_HOSTBASED_SERVICE(), serverName);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_import_name(inServiceName)", min_status,
                              maj_status);
            return -1;
        }

        /* The main authentication loop. Because GSS is a multimechanism
         * API, we need to loop calling gss_init_sec_context - passing
         * in the "input tokens" received from the server and send the
         * resulting "output tokens" back until we get GSS_S_COMPLETE
         * or an error. */

        maj_status = GSS_S_CONTINUE_NEEDED;
        while (maj_status != GSS_S_COMPLETE) {

            gss_buffer_desc outputToken = new gss_buffer_desc();
            outputToken.setLength(0);
            outputToken.setValue(null);

            long requestedFlags = (
                    GSS_C_MUTUAL_FLAG ^
                    GSS_C_REPLAY_FLAG ^
                    GSS_C_SEQUENCE_FLAG ^
                    GSS_C_CONF_FLAG ^
                    GSS_C_INTEG_FLAG );

            System.out.println("Calling gss_init_sec_context...");
            gss_OID_desc actual_mech_type = new gss_OID_desc();

            maj_status = gsswrapper.gss_init_sec_context(min_status,
                    clientCredentials, context_tmp, serverName,
                    GSS_C_NO_OID, 
                    requestedFlags,
                    GSS_C_INDEFINITE,
                    GSS_C_NO_CHANNEL_BINDINGS,
                    inputToken,
                    actual_mech_type, outputToken, actualFlags, time_rec);

            if (outputToken.getLength() > 0) {
                /* 
                 * Send the output token to the server (even on error), 
                 * using a Java byte[]
                 */
                byte[] temp_token = new byte[(int)outputToken.getLength()];
                temp_token = gsswrapper.getDescArray(outputToken);
                System.out.println("Generated Token Length = " + 
                                   temp_token.length);
                err = Util.WriteToken(serverOut, temp_token);

                /* free the output token */
                gsswrapper.gss_release_buffer(min_status, outputToken);
            }

            if (err == 0) {
                if (maj_status == GSS_S_CONTINUE_NEEDED) {
                    
                    /* Protocol requires another packet exchange */
                    System.out.println("Protocol requires another " + 
                                       "packet exchange");

                    /* Clean up old input buffer */
                    if (inputTokenBuffer != null)
                        inputTokenBuffer = null; 

                    /* Read another input token from the server */
                    inputTokenBuffer = Util.ReadToken(serverIn); 

                    if (inputTokenBuffer != null) {
                        gsswrapper.setDescArray(inputToken, inputTokenBuffer);
                        inputToken.setLength(inputTokenBuffer.length);
                        System.out.println("Received Token Length = " + 
                                           inputToken.getLength());
                    }
                } else if (maj_status != GSS_S_COMPLETE) {
                    Util.displayError("gss_init_sec_context", 
                                      min_status, maj_status);
                    return -1;
                }
            }

        } /* end while loop */

        /* Test gss_compare_name - client and server names should differ */
        maj_status = gsswrapper.gss_compare_name(min_status, 
                clientName, serverName, ret);
        if (ret[0] == 1) {
            System.out.println("TEST:  clientName == serverName");
        } else {
            System.out.println("TEST:  clientName != serverName");
        }

        /* Save our context */
        context = context_tmp;
        
        return 0;

    } /* end Authenticate() */

    public static int Communicate(Socket clientSocket,
            InputStream serverIn, OutputStream serverOut)
    {
        long maj_status = 0;
        long[] min_status = {0};
        long[] qop_state = {0};
        int[] state = {0};
        int err = 0;
        gss_buffer_desc in_buf = new gss_buffer_desc("Hello Server!");
        gss_buffer_desc out_buf = new gss_buffer_desc();
        byte[] sigBlockBuffer = null;

        System.out.println("Beginning communication with server");

        /* Sign and encrypt plain message */
        maj_status = gsswrapper.gss_wrap(min_status, context, 1, 
                GSS_C_QOP_DEFAULT, in_buf, 
                state, out_buf);

        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("wrapping message, gss_wrap", 
                              min_status, maj_status);
            return -1;
        } else if (state[0] == 0) {
            System.out.println("Warning!  Message not encrypted.");
        }

        /* Send wrapped message to server */
        byte[] temp_token = new byte[(int)out_buf.getLength()];
        temp_token = gsswrapper.getDescArray(out_buf);
        err = Util.WriteToken(serverOut, temp_token);
        if (err != 0) {
            System.out.println("Error sending wrapped message to " +
                               "server, WriteToken");
            return -1;
        }
       
        /* Read signature block from the server */ 
        sigBlockBuffer = Util.ReadToken(serverIn); 

        if (sigBlockBuffer != null) {
            gsswrapper.setDescArray(out_buf, sigBlockBuffer);
            out_buf.setLength(sigBlockBuffer.length);
        }

        /* Verify signature block */
        maj_status = gsswrapper.gss_verify_mic(min_status, context,
                in_buf, out_buf, qop_state);

        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("verifying signature, gss_verify_mic", 
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Signature Verified");
        }
       
        gsswrapper.gss_release_buffer(min_status, in_buf);
        gsswrapper.gss_release_buffer(min_status, out_buf); 
        
        return 0;

    } /* end Communicate() */
    
    public static int AltCommunicate(Socket clientSocket, 
            InputStream serverIn, OutputStream serverOut)
    {
        long maj_status = 0;
        long[] min_status = {0};
        int[] qop_state = {0};
        int[] state = {0};
        int err = 0;
        gss_buffer_desc in_buf = new gss_buffer_desc("Hello Server!");
        gss_buffer_desc out_buf = new gss_buffer_desc();
        byte[] sigBlockBuffer = null;

        gss_buffer_desc context_token = new gss_buffer_desc();

        /* Test context export/import functions */
        maj_status = gsswrapper.gss_export_sec_context(min_status,
                context, context_token);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("exporting security context", 
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Successfully exported security context");
        }

        maj_status = gsswrapper.gss_import_sec_context(min_status,
                context_token, context);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("importing security context", 
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Successfully imported security context");
        }
        gsswrapper.gss_release_buffer(min_status, context_token);

        /* Sign and encrypt plain message */
        maj_status = gsswrapper.gss_seal(min_status, context, 1, 
                GSS_C_QOP_DEFAULT, in_buf, 
                state, out_buf);

        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("wrapping message, gss_seal", 
                              min_status, maj_status);
            return -1;
        } else if (state[0] == 0) {
            System.out.println("Warning!  Message not encrypted.");
        }

        /* Send wrapped message to server */
        byte[] temp_token = new byte[(int)out_buf.getLength()];
        temp_token = gsswrapper.getDescArray(out_buf);
        err = Util.WriteToken(serverOut, temp_token);
        if (err != 0) {
            System.out.println("Error sending wrapped message to " +
                               "server, WriteToken");
            return -1;
        }
       
        /* Read signature block from the server */ 
        sigBlockBuffer = Util.ReadToken(serverIn); 

        if (sigBlockBuffer != null) {
            gsswrapper.setDescArray(out_buf, sigBlockBuffer);
            out_buf.setLength(sigBlockBuffer.length);
        }

        /* Verify signature block */
        maj_status = gsswrapper.gss_verify(min_status, context,
                in_buf, out_buf, qop_state);

        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("verifying signature, gss_verify", 
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Signature Verified");
        }
       
        gsswrapper.gss_release_buffer(min_status, in_buf);
        gsswrapper.gss_release_buffer(min_status, out_buf); 
        
        return 0;

    } /* end AltCommunicate() */

    public static int PrintContextInfo() 
    {
        long maj_status = 0;
        long[] min_status = {0};
        gss_name_t_desc src_name = new gss_name_t_desc();
        gss_name_t_desc targ_name = new gss_name_t_desc();
        gss_buffer_desc sname = new gss_buffer_desc();
        gss_buffer_desc tname = new gss_buffer_desc();
        gss_buffer_desc oid_name = new gss_buffer_desc();
        gss_buffer_desc sasl_mech_name = new gss_buffer_desc();
        gss_buffer_desc mech_name = new gss_buffer_desc();
        gss_buffer_desc mech_description = new gss_buffer_desc();
        long lifetime[] = {0};
        long[] time_rec = {0};
        gss_OID_desc mechanism = new gss_OID_desc();
        gss_OID_desc name_type = new gss_OID_desc();
        gss_OID_desc oid = new gss_OID_desc();
        long context_flags[] = {0};
        int is_local[] = {0};
        int is_open[] = {0};
        gss_OID_set_desc mech_names = new gss_OID_set_desc();
        gss_OID_set_desc mech_attrs = new gss_OID_set_desc();
        gss_OID_set_desc known_attrs = new gss_OID_set_desc();

        /* Get context information */
        maj_status = gsswrapper.gss_inquire_context(min_status, context,
                src_name, targ_name, lifetime, mechanism, context_flags,
                is_local, is_open);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Inquiring context:  gss_inquire_context", 
                              min_status, maj_status);
            return -1;
        }

        /* Check if our context is still valid */
        maj_status = gsswrapper.gss_context_time(min_status, context, time_rec);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("checking for valid context", 
                              min_status, maj_status);
            return -1;
        }

        /* Get context source name */
        maj_status = gsswrapper.gss_display_name(min_status, 
                src_name, sname, name_type);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Displaying source name:  gss_display_name", 
                              min_status, maj_status);
            return -1;
        }

        /* Get context target name */
        maj_status = gsswrapper.gss_display_name(min_status, 
                targ_name, tname, name_type);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Displaying target name:  gss_display_name", 
                              min_status, maj_status);
            return -1;
        }

        System.out.println("------------- Context Information ---------------");
        System.out.println("Context is Valid for another " 
                           + time_rec[0] + " seconds");
        System.out.println(sname.getValue() + " to " + tname.getValue());
        System.out.println("Lifetime: " + lifetime[0] + " seconds");
        System.out.println("Flags: " + context_flags[0]);
        System.out.println("Initiated: " + ((is_local[0] == 1) 
                           ? "locally" : "remotely"));
        System.out.println("Status: " + ((is_open[0] == 1) 
                           ? "open" : "closed"));

        gsswrapper.gss_release_name(min_status, src_name);
        gsswrapper.gss_release_name(min_status, targ_name);
        gsswrapper.gss_release_buffer(min_status, sname);
        gsswrapper.gss_release_buffer(min_status, tname);

        maj_status = gsswrapper.gss_oid_to_str(min_status, 
                name_type, oid_name);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Converting oid->string:  gss_oid_to_str", 
                              min_status, maj_status);
            return -1;
        }

        System.out.println("Name type of source name: " + oid_name.getValue());
        gsswrapper.gss_release_buffer(min_status, oid_name);

        /* Get mechanism attributes */
        maj_status = gsswrapper.gss_inquire_attrs_for_mech(min_status, 
                mechanism, mech_attrs, known_attrs);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Inquiring mechanism attributes", 
                              min_status, maj_status);
            return -1;
        }
        System.out.println("  Mechanism Attributes:");
        for (int j = 0; j < mech_attrs.getCount(); j++)
        {
            gss_buffer_desc name = new gss_buffer_desc();
            gss_buffer_desc short_desc = new gss_buffer_desc();
            gss_buffer_desc long_desc = new gss_buffer_desc();

            maj_status = gsswrapper.gss_display_mech_attr(min_status, 
                    mech_attrs.getElement(j), name, short_desc, long_desc);
            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("Displaying mechanism attributes", 
                                  min_status, maj_status);
                return -1;
            }
            System.out.println("    " + name.getValue());
            gsswrapper.gss_release_buffer(min_status, name);
            gsswrapper.gss_release_buffer(min_status, short_desc);
            gsswrapper.gss_release_buffer(min_status, long_desc);
        }

        /* Get known attributes */
        System.out.println("  Known Attributes:");
        for (int k = 0; k < known_attrs.getCount(); k++)
        {
            gss_buffer_desc name = new gss_buffer_desc();
            gss_buffer_desc short_desc = new gss_buffer_desc();
            gss_buffer_desc long_desc = new gss_buffer_desc();

            maj_status = gsswrapper.gss_display_mech_attr(min_status,
                    known_attrs.getElement(k), name, short_desc, long_desc);
            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("Displaying known attributes", 
                                  min_status, maj_status);
                return -1;
            }
            System.out.println("    " + name.getValue());
            gsswrapper.gss_release_buffer(min_status, name);
            gsswrapper.gss_release_buffer(min_status, short_desc);
            gsswrapper.gss_release_buffer(min_status, long_desc);

        }
        gsswrapper.gss_release_oid_set(min_status, mech_attrs);
        gsswrapper.gss_release_oid_set(min_status, known_attrs);

        /* Get names supported by the mechanism */
        maj_status = gsswrapper.gss_inquire_names_for_mech(min_status, 
                mechanism, mech_names);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Inquiring mech names", 
                              min_status, maj_status);
            return -1;
        }

        maj_status = gsswrapper.gss_oid_to_str(min_status, 
                mechanism, oid_name);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Converting oid->string", 
                              min_status, maj_status);
            return -1;
        }

        System.out.println("Mechanism " + oid_name.getValue() 
                           + " supports " + mech_names.getCount() + " names");
        for (int i = 0; i < mech_names.getCount(); i++)
        {
            maj_status = gsswrapper.gss_oid_to_str(min_status, 
                    mech_names.getElement(i), oid_name);
            if (maj_status != GSS_S_COMPLETE) {
                Util.displayError("Converting oid->string", 
                                  min_status, maj_status);
                return -1;
            }
            System.out.println("  " + i + ": " + oid_name.getValue());
            gsswrapper.gss_release_buffer(min_status, oid_name);
        }
        gsswrapper.gss_release_oid_set(min_status, mech_names);

        /* Get SASL mech */
        maj_status = gsswrapper.gss_inquire_saslname_for_mech(min_status, 
                mechanism, sasl_mech_name, mech_name, mech_description);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Inquiring SASL name", 
                              min_status, maj_status);
            return -1;
        }
        System.out.println("SASL mech: " + sasl_mech_name.getValue());
        System.out.println("Mech name: " + mech_name.getValue());
        System.out.println("Mech desc: " + mech_description.getValue());

        /* Inquire Mech for SASL name - to test */
        maj_status = gsswrapper.gss_inquire_mech_for_saslname(min_status,
                sasl_mech_name, oid);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("Inquiring mechs for SASL name", 
                              min_status, maj_status);
            return -1;
        }

        if (oid == GSS_C_NO_OID) {
            System.out.println("Got different OID for mechanism");
        }

        /* Determine largest message that can be wrapped,
           assuming max output token size of 100 (just for testing) */
        long[] max_size = {0};        
        maj_status = gsswrapper.gss_wrap_size_limit(min_status, context, 1,
                GSS_C_QOP_DEFAULT,
                100, max_size);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("determining largest wrapped message size",
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Largest message size able to be wrapped: " 
                               + max_size[0]);
        }

        gsswrapper.gss_release_buffer(min_status, sasl_mech_name);
        gsswrapper.gss_release_buffer(min_status, mech_name);
        gsswrapper.gss_release_buffer(min_status, mech_description);
        gsswrapper.gss_release_oid(min_status, oid);

        System.out.println("-------------------------------------------------");

        return 0;
    }
    
    public static int MiscFunctionTests() 
    {
        long maj_status = 0;
        long[] min_status = {0};
        gss_cred_id_t_desc cred_handle = GSS_C_NO_CREDENTIAL;
        /* kerberos v5 */
        gss_OID_desc mech_type = new gss_OID_desc("1.2.840.113554.1.2.2");
        gss_name_t_desc name = new gss_name_t_desc();
        long[] init_lifetime = {0};
        long[] accept_lifetime = {0};
        int[] cred_usage = {0};

        System.out.println("------------------- MISC TESTS ------------------");
        
        /* 
         * FUNCTION: gss_inquire_cred_by_mech
         * Get info about default cred for default security mech.
         */
        maj_status = gsswrapper.gss_inquire_cred_by_mech(min_status,
                cred_handle, mech_type, name, init_lifetime,
                accept_lifetime, cred_usage);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("inquiring credential info from mech", 
                              min_status, maj_status);
            return -1;
        } else {
            System.out.println("Credential Principal Name: " 
                               + name.getExternal_name().getValue());
            System.out.println("Credential Valid for Initiating Contexts for " 
                               + init_lifetime[0] + " seconds");
            System.out.println("Credential Valid for Accepting Contexts for " 
                               + accept_lifetime[0] + " seconds");
            System.out.println("Credential Usage: " + cred_usage[0]);
        }

       
        /* FUNCTION: gss_pseudo_random */ 
        gss_buffer_desc prf_out = new gss_buffer_desc();
        gss_buffer_desc prf_in = new gss_buffer_desc("gss prf test");
        maj_status = gsswrapper.gss_pseudo_random(min_status,
                context, 0, prf_in, 19, prf_out);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("testing gss_pseudo_random function", 
                              min_status, maj_status);
            return -1;
        }

        gsswrapper.gss_release_buffer(min_status, prf_out);
        gsswrapper.gss_release_buffer(min_status, prf_in);

        /* FUNCTION: gss_indicate_mechs_by_attrs */
        gss_OID_set_desc desired_mech_attrs = GSS_C_NO_OID_SET;
        gss_OID_set_desc except_mech_attrs =  GSS_C_NO_OID_SET;
        gss_OID_set_desc critical_mech_attrs =  GSS_C_NO_OID_SET;
        gss_OID_set_desc mechs = new gss_OID_set_desc();
        maj_status = gsswrapper.gss_indicate_mechs_by_attrs(min_status,
                desired_mech_attrs, except_mech_attrs, critical_mech_attrs,
                mechs);
        if (maj_status != GSS_S_COMPLETE) {
            Util.displayError("gss_indicate_mechs_by_attrs", 
                              min_status, maj_status);
            return -1;
        }

        System.out.println("-------------------------------------------------");

        return 0;
    }
}

