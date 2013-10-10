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
 *
 * A simple server application which uses the MIT Kerberos Java GSS-API. 
 * The following actions are taken by the server:
 *      a) Establish a GSS-API context with the example client.
 *      b) Unwrap a signed and encrypted message that the client sends.
 *      c) Generate and send a signature block for the received message.
 *
 * Before starting the example server, there should be an entry in your
 * system keytab for the service principal.
 *
 * This class uses the utility class, GssUtil.java.
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

    public static void main(String args[]) {
        try {
            new gssServer().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(String args[]) throws Exception 
    {
        /* pull in command line options from user */
        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];

            if (arg.equals("-?")) {
                printUsage();
            } else if (arg.equals("-p")) {
                if (args.length < i+2)
                    printUsage();
                server_port = Integer.parseInt(args[++i]);
            } else if (arg.equals("-s")) {
                if (args.length < i+2)
                    printUsage();
                serviceName = args[++i];
            } else {
                printUsage();
            }
        }
        
        System.out.println("Starting GSS-API Server Example"); 

        /* set up a shutdown hook to release GSSCredential storage
           when the user terminates the server with Ctrl+C */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    cred.dispose();
                } catch (GSSException e) {
                    System.out.println("Couldn't free GSSCredential storage");
                }
                System.out.println("Freed GSSCredential storage");
            }
        });
        
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
                System.out.println("\nwaiting for client connection"
                        + "-----------------------------");
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

                    inToken = GssUtil.ReadToken(clientIn);
                    System.out.println("Received token from client...");

                    System.out.println("Calling acceptSecContext");
                    outToken = context.acceptSecContext(inToken,
                            0, inToken.length);

                    if (outToken != null && outToken.length > 0) {
                        err = GssUtil.WriteToken(clientOut, outToken);
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
                GssUtil.printSubString("Target Name", targetName.toString());
                GssUtil.printSubString("Mechanism", mechName.toString());
                GssUtil.printSubString("AnonymityState", context.getAnonymityState());
                GssUtil.printSubString("ConfState", context.getConfState());
                GssUtil.printSubString("CredDelegState", context.getCredDelegState());
                GssUtil.printSubString("IntegState", context.getIntegState());
                GssUtil.printSubString("Lifetime", context.getLifetime());
                GssUtil.printSubString("MutualAuthState", context.getMutualAuthState());
                GssUtil.printSubString("ReplayDetState", context.getReplayDetState());
                GssUtil.printSubString("SequenceDetState", context.getSequenceDetState());
                GssUtil.printSubString("Is initiator?", context.isInitiator());
                GssUtil.printSubString("Is Prot Ready?", context.isProtReady());

                /* read message sent by the client */
                inToken = Util.ReadToken(clientIn);
                System.out.println("Received token from client...");

                /* unwrap the message */
                buffer = context.unwrap(inToken, 0, inToken.length, supplInfo);
                System.out.println("Message received from client ('" + 
                        new String(buffer) + "')");

                /* print other supplementary per-message status info */
                System.out.println("Message from " +
                        peerName.toString() + " arrived.");
                GssUtil.printSubString("Was it encrypted?", supplInfo.getPrivacy());
                GssUtil.printSubString("Duplicate Token?", supplInfo.isDuplicateToken());
                GssUtil.printSubString("Old Token?", supplInfo.isOldToken());
                GssUtil.printSubString("Unsequenced Token?", supplInfo.isUnseqToken());
                GssUtil.printSubString("Gap Token?", supplInfo.isGapToken());

                supplInfo.setPrivacy(true);     // privacy requested
                supplInfo.setQOP(0);            // default QOP

                /* produce a signature block for the message */
                buffer = context.getMIC(buffer, 0, buffer.length, supplInfo);

                /* send signature block to client */
                err = GssUtil.WriteToken(clientOut, buffer);
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
    
    public void printUsage() {
        System.out.println("GSS-API example server usage:");
        System.out.println("-?\t\tHelp, print this usage");
        System.out.println("-p <port>\tPort to listen on, default 11115");
        System.out.println("-s <str>\tService name, default 'service@host'");
        System.exit(1);
    }
}
