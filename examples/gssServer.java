/* gssServer.java - Example GSS-API Java server */
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
 * A simple server application which uses the MIT Kerberos Java GSS-API. 
 * The following actions are taken by the server:
 *      a) Establish a GSS-API context with the example client.
 *      b) Unwraps a signed and encrypted message that the client sends.
 *      c) Generates and sends a signature block for the received message.
 *
 * Before starting the example server, there should be an entry in your
 * system keytab for the service principal.
 *
 */

import java.io.*;
import java.net.*;
import org.ietf.jgss.*;

public class gssServer {

    private static int server_port = 11115;
    private static String serviceName = "service@host";

    private static GSSName name         = null;
    private static GSSCredential cred   = null;
    private static GSSManager mgr = GSSManager.getInstance();

    public static void main(String argv[]) throws Exception 
    {
       System.out.println("Starting GSS-API Server Example"); 
        
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

        /* set up GSS-API name, credential */
        name = mgr.createName(serviceName, GSSName.NT_HOSTBASED_SERVICE);
        cred = mgr.createCredential(name, GSSCredential.INDEFINITE_LIFETIME,
                (Oid) null, GSSCredential.ACCEPT_ONLY);

        while(true)
        {
            byte[] inToken = null;
            byte[] outToken = null;
            byte[] buffer;
            int err = 0;

            GSSName peer;
            MessageProp supplInfo = new MessageProp(true);

            try {
                System.out.println("\nwaiting for client connection..."
                        + "--------------------------");
                clientSocket = serverSocket.accept();
                
                /* get input and output streams */
                clientOut = clientSocket.getOutputStream();
                clientIn = clientSocket.getInputStream();
                System.out.println("client connection received from " +
                        clientSocket.getInetAddress().getHostAddress() + 
                        " at port " + clientSocket.getLocalPort() + "\n");

                /* establish context with client */
                GSSContext context = mgr.createContext(cred);

                while (!context.isEstablished()) {

                    inToken = Util.ReadToken(clientIn);
                    System.out.println("Got token from client...");

                    System.out.println("Calling acceptSecContext");
                    outToken = context.acceptSecContext(inToken,
                            0, inToken.length);

                    if (outToken != null && outToken.length > 0) {
                        err = Util.WriteToken(clientOut, outToken);
                        if (err == 0) {
                            System.out.println("Sent token to client..."); 
                        } else {
                            System.out.println("Error sending token to client");
                        }
                    }
                }

                GSSName peerName = context.getSrcName();
                GSSName targetName = context.getTargName();
                Oid mechName = context.getMech();
                System.out.println("Security context established with " +
                        peerName.toString());
                System.out.println(" | Target Name = " + targetName.toString());
                System.out.println(" | Mechanism = " + mechName.toString());
                System.out.println(" | AnonymityState = " + context.getAnonymityState());
                System.out.println(" | ConfState = " + context.getConfState());
                System.out.println(" | CredDelegState = " + context.getCredDelegState());
                System.out.println(" | IntegState = " + context.getIntegState());
                System.out.println(" | Lifetime = " + context.getLifetime() + " sec");
                System.out.println(" | MutualAuthState = " + context.getMutualAuthState());
                System.out.println(" | ReplayDetState = " + context.getReplayDetState());
                System.out.println(" | SequenceDetState = " + context.getSequenceDetState());
                System.out.println(" | Is initiator? " + context.isInitiator());
                System.out.println(" | Is Prot Ready? " + context.isProtReady());

                /* read message sent by the client */
                inToken = Util.ReadToken(clientIn);
                System.out.println("Got token from client...");

                /* unwrap the message */
                buffer = context.unwrap(inToken, 0, inToken.length, supplInfo);
                System.out.println("Message = " + new String(buffer));

                /* print other supplementary per-message status info */
                System.out.println("Message from " +
                        peerName.toString() + " arrived.");
                System.out.println(" | Was it encrypted?\t" + 
                        supplInfo.getPrivacy());
                System.out.println(" | Duplicate Token?\t" +
                        supplInfo.isDuplicateToken());
                System.out.println(" | Old Token?\t\t" +
                        supplInfo.isOldToken());
                System.out.println(" | Unsequenced Token?\t" +
                        supplInfo.isUnseqToken());
                System.out.println(" | Gap Token?\t\t" +
                        supplInfo.isGapToken() + "\n");

                supplInfo.setPrivacy(true);     // privacy requested
                supplInfo.setQOP(0);            // default QOP

                /* produce a signature block for the message */
                buffer = context.getMIC(buffer, 0, buffer.length, supplInfo);

                /* send signature block to client */
                err = Util.WriteToken(clientOut, buffer);
                if (err == 0) {
                    System.out.println("Sent sig block to client...");
                } else {
                    System.out.println("Error sending sig block to client...");
                }

                context.dispose();

            } catch (IOException e) {
                System.out.println("Server did not accept connection: " + e);
            } catch (GSSException e) {
                System.out.println("GSS-API Error: " + e.getMessage());
            }

            clientOut.close();
            clientIn.close();
            clientSocket.close();
        }
    }
}
