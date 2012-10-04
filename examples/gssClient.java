/* gssClient.java - Example GSS-API Java client */
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
 * A simple client application which uses the MIT Kerberos Java GSS-API.
 * The following actions are taken by the client:
 *      a) Establish a GSSAPI context with the example server.
 *      b) Sign and encrypt, and send a message to the server.
 *      c) Verify the signature block returned by the server.
 *
 * Before running the client example, the client principal credentials must
 * be acquired using kinit.
 * 
 */

import java.io.*;
import java.net.*;
import org.ietf.jgss.*;

public class gssClient {

    /* set these to match your environment and principal names */
    private static int port = 11115;
    private static String server = "127.0.0.1";
    private static String serviceName = "service@host";
    private static String clientPrincipal = "clientname";

    private static GSSCredential clientCred = null;
    private static GSSContext context       = null;
    private static GSSManager mgr = GSSManager.getInstance();
   
    /* using null to request default mech, krb5 */ 
    private static Oid mech                 = null;
        
    private static Socket clientSocket      = null;
    private static OutputStream serverOut   = null;
    private static InputStream serverIn     = null;

    public static void main(String argv[]) throws Exception  {
       
        System.out.println("Starting GSS-API Client Example\n"); 
       
        connectToServer();
        initializeGSS();
        establishContext(serverIn, serverOut);
        doCommunication(serverIn, serverOut);

        /* shutdown */
        context.dispose();
        clientCred.dispose();
        serverIn.close();
        serverOut.close();
        clientSocket.close();
    }

    /**
     * Connect to example GSS-API server, using specified port and 
     * service name.
     **/
    public static void connectToServer() {
        
        try {
            clientSocket = new Socket(server, port);
            System.out.println("Connected to " + server + " at port " 
                    + port + "\n");

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

    }

    /**
     * Set up GSS-API in preparation for context establishment. Creates
     * GSSName and GSSCredential for client principal.
     **/
    public static void initializeGSS() {

        try {
            GSSName clientName = mgr.createName(clientPrincipal,
                    GSSName.NT_USER_NAME);

            /* create cred with max lifetime */
            clientCred = mgr.createCredential(clientName,
                    GSSCredential.INDEFINITE_LIFETIME, mech,
                    GSSCredential.INITIATE_ONLY);

            System.out.println("GSSCredential created for " 
                    + clientCred.getName().toString());
            System.out.println("Credential lifetime (sec) = " 
                    + clientCred.getRemainingLifetime() + "\n");

        } catch (GSSException e) {
            System.out.println("GSS-API error in credential acquisition: "
                    + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Establish a GSS-API context with example server, calling
     * initSecContext() until context.isEstablished() is true.
     *
     * This method also tests exporting and re-importing the security
     * context.
     **/
    public static void establishContext(InputStream serverIn,
            OutputStream serverOut) {

        byte[] inToken  = new byte[0];
        byte[] outToken = null;
        int err = 0;

        try {
            GSSName peer = mgr.createName(serviceName,
                    GSSName.NT_HOSTBASED_SERVICE);

            context = mgr.createContext(peer, mech, clientCred,
                    GSSContext.INDEFINITE_LIFETIME);

            context.requestConf(true);
            context.requestReplayDet(true);
            context.requestMutualAuth(true);

            while (!context.isEstablished()) {

                System.out.println("Calling initSecContext");
                outToken = context.initSecContext(inToken, 0, inToken.length);

                if (outToken != null && outToken.length > 0) {
                    err = Util.WriteToken(serverOut, outToken);
                    if (err == 0) {
                        System.out.println("Sent token to server...");
                    } else {
                        System.out.println("Error sending token to server...");
                    }
                }

                if (!context.isEstablished()) {
                    inToken = Util.ReadToken(serverIn); 
                    System.out.println("Read token from server... ");
                }
            }

            GSSName peerName = context.getSrcName();
            System.out.println("Security context established with " + peer);
            System.out.println("Context lifetime = " + context.getLifetime());

            /* Test exporting/importing established security context */
            byte[] exportedContext = context.export();
            context = mgr.createContext(exportedContext);
            GSSName serverInfo2 = context.getTargName();
            System.out.println("after context import, targetName = "
                + serverInfo2.toString());
     
        } catch (GSSException e) {
            System.out.println("GSS-API error during context establishment: "
                    + e.getMessage());
            System.exit(1);
        }

            }

    /**
     * Communicate with the server. First send a message that has been
     * wrapped with context.wrap(), then verify the signature block which
     * the server sends back.
     **/
    public static void doCommunication(InputStream serverIn,
            OutputStream serverOut) {

        MessageProp messagInfo = new MessageProp(false);
        byte[] inToken  = new byte[0];
        byte[] outToken = null;
        byte[] buffer;
        int err = 0;

        try {

            String msg = "Hello Server, this is the client!";
            buffer = msg.getBytes();

            /* Set privacy to "true" and use the default QOP */
            messagInfo.setPrivacy(true);

            outToken = context.wrap(buffer, 0, buffer.length, messagInfo);
            err = Util.WriteToken(serverOut, outToken);
            if (err == 0) {
                System.out.println("Sent message to server...");

                /* Read signature block from the server */ 
                inToken = Util.ReadToken(serverIn);
                System.out.println("Read sig block from server...");

                GSSName serverInfo = context.getTargName();
                System.out.println("Message from " + serverInfo.toString() +
                    " arrived.");
                System.out.println("Was it encrypted? " + 
                    messagInfo.getPrivacy());
                System.out.println("Duplicate Token? " +
                    messagInfo.isDuplicateToken());
                System.out.println("Old Token? " + 
                    messagInfo.isOldToken());
                System.out.println("Gap Token? " + 
                    messagInfo.isGapToken());

                /* Verify signature block */
                context.verifyMIC(inToken, 0, inToken.length, buffer, 0, 
                    buffer.length, messagInfo);
                System.out.println("verified MIC from server");

            } else {
                System.out.println("Error sending message to server...");
            }

        } catch (GSSException e) {
            System.out.println("GSS-API error in per-message calls: " + 
                    e.getMessage());
        }
    }

}
