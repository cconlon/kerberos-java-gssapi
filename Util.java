/*
 * Copyright (C) 2012 by the Massachusetts Institute of Technology.
 * All rights reserved.
 *
 * Export of this software from the United States of America may
 *   require a specific license from the United States Government.
 *   It is the responsibility of any person or organization contemplating
 *   export to obtain such a license before exporting.
 *
 * WITHIN THAT CONSTRAINT, permission to use, copy, modify, and
 * distribute this software and its documentation for any purpose and
 * without fee is hereby granted, provided that the above copyright
 * notice appear in all copies and that both that copyright notice and
 * this permission notice appear in supporting documentation, and that
 * the name of M.I.T. not be used in advertising or publicity pertaining
 * to distribution of the software without specific, written prior
 * permission.  Furthermore if you modify this software you must label
 * your software as modified software and not distribute it in such a
 * fashion that it might be confused with the original M.I.T. software.
 * M.I.T. makes no representations about the suitability of
 * this software for any purpose.  It is provided "as is" without express
 * or implied warranty.
 *
 * Original source developed by yaSSL (http://www.yassl.com)
 *
 * Description:
 * Java MIT Kerberos GSS-API interface utility functions.
 *
 */
import java.io.*;
import java.net.*;
import edu.mit.kerberos.*;

public class Util implements gsswrapperConstants{
    
    /*
     * Display error and exit program.
     */
    public static void errorExit(String msg, long[] min_stat, long maj_stat) 
    {
        System.out.println("Error: " + msg);
        System.out.println("maj_stat = " + maj_stat + ", min_stat = " 
                + (int)min_stat[0]);
        display_status(min_stat, maj_stat);
        System.exit(1);
    }

    /* 
     * Display error and continue.
     */
    public static void displayError(String msg, long[] min_stat, long maj_stat) 
    {
        System.out.println("Error: " + msg);
        System.out.println("maj_stat = " + maj_stat + ", min_stat = " 
                + (int)min_stat[0]);
        display_status(min_stat, maj_stat);
    }
    
    /*
     * Display status using minor and major status codes
     */
    public static void display_status(long[] min_stat, long maj_stat) 
    {
        long[] msg_ctx = {0};
        long ret = 0;
        gss_buffer_desc storage_buffer = new gss_buffer_desc();

        // Print GSS major status code error
        ret = gsswrapper.gss_display_status_wrap(min_stat[0], maj_stat, 
                GSS_C_GSS_CODE, 
                GSS_C_NO_OID, 
                msg_ctx, storage_buffer);
        if (ret != GSS_S_COMPLETE) {
            System.out.println("gss_display_status failed");
            System.exit(1);
        }
        System.out.println("Error message (major): " 
                + storage_buffer.getValue());
        
        // Print mechanism minor status code error
        ret = gsswrapper.gss_display_status_wrap(maj_stat, min_stat[0], 
                GSS_C_MECH_CODE, 
                GSS_C_NO_OID, msg_ctx, storage_buffer);
        if (ret != GSS_S_COMPLETE) {
            System.out.println("gss_display_status failed");
            System.exit(1);
        }
        System.out.println("Error message (minor): " 
                + storage_buffer.getValue());
    }

    /*
     * Write a token byte[] to OutputStream.
     * Return: 0 on success, -1 on failure
     */
    public static int WriteToken(OutputStream outStream, byte[] outputToken)
    {
        System.out.println("Entered WriteToken...");
       
        try { 

            /* First send the size of our byte array */
            byte[] size = Util.intToByteArray(outputToken.length);
            System.out.println("... sending byte array size: " +
                    Util.byteArrayToInt(size));
            outStream.write(size);

            /* Now send our actual byte array */
            System.out.println("... sending byte array: ");
            printByteArray(outputToken);
            System.out.println("... outputToken.length = " + 
                    outputToken.length);
            outStream.write(outputToken);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
   
    /*
     * Read a token byte[] from InputStream.
     * Return byte[] on success, null on failure
     */ 
    public static byte[] ReadToken(InputStream inStream) 
    {
        System.out.println("Entered ReadToken...");
        byte[] inputTokenBuffer = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int data;

            /* First read the incomming array size (first 4 bytes) */
            int array_size = 0;
            byte[] temp = null;
            for (int i = 0; i < 4; i++) {
                data = inStream.read();
                out.write(data);
            }
            temp = out.toByteArray();
            array_size = Util.byteArrayToInt(temp);
            out.reset();
            System.out.println("... got byte array size = " + array_size);
            if (array_size < 0)
                return null;

            /* Now read our full array */
            for (int j = 0; j < array_size; j++) {
                data = inStream.read();
                out.write(data);
            }
            System.out.println("... got data: ");
            Util.printByteArray(out.toByteArray());
            System.out.println("... returning from ReadToken, success");
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
   
    /*
     * Print a byte[], for debug purposes.
     */ 
    public static void printByteArray(byte[] input)
    {
        for (int i = 0; i < input.length; i++ ) {
            System.out.format("%02X ", input[i]);
        }
        System.out.println();
    }

    /* Based on http://snippets.dzone.com/posts/show/93 */
    public static byte[] intToByteArray(int input)
    {
        byte[] out = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (out.length - 1 - i) * 8;
            out[i] = (byte) ((input >>> offset) & 0xFF);
        }
        return out;
    }

    /* Based on http://snippets.dzone.com/posts/show/93 */
    public static int byteArrayToInt(byte[] data) 
    {
        if (data == null || data.length != 4) return 0x0;
        return (int)(
                (0xff & data[0]) << 24 |
                (0xff & data[1]) << 16 |
                (0xff & data[2]) << 8  |
                (0xff & data[3])  << 0
                );
    }
}
