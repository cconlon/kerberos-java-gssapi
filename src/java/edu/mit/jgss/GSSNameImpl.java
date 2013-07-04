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

import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import edu.mit.jgss.swig.gss_name_t_desc;
import edu.mit.jgss.swig.gss_buffer_desc;
import edu.mit.jgss.swig.gss_OID_desc;
import edu.mit.jgss.swig.gsswrapper;

public class GSSNameImpl implements GSSName {

    /* Representing our underlying SWIG-wrapped gss_name_t_desc object */
    private gss_name_t_desc internGSSName;
   
    /* debugging */
    private boolean DEBUG_ERR = false;

    public boolean equals(GSSName another) throws GSSException {

        long maj_status = 0;
        long[] min_status = {0};
        int ret[] = {0};

        maj_status = gsswrapper.gss_compare_name(min_status, this.internGSSName,
               another.getInternGSSName(), ret);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            if (DEBUG_ERR == true)
                System.out.println("gss_compare_name failed... maj_status = " 
                        + maj_status);
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        } 

        if (ret[0] == 1)
            return true;
        else
            return false;

    }

    public boolean equals(Object another) {

        if (! (another instanceof GSSName))
            return false;

        GSSName tmpName = (GSSName) another;

        if (tmpName == null)
            throw new NullPointerException("Input Obj is null");

        try {
            return equals(tmpName);
        } catch (GSSException e) {
            return false;
        }

    }

    public GSSName canonicalize(Oid mech) throws GSSException {
        long maj_status = 0;
        long[] min_status = {0};
        int ret = 0;
        gss_OID_desc tmpOid;

        GSSNameImpl canonicalName = new GSSNameImpl();
        gss_name_t_desc tmpName = new gss_name_t_desc();
      
        if (mech == null) {
        
            tmpOid = new gss_OID_desc("1.2.840.113554.1.2.2");

        } else {

            /* note that underlying native MIT gssapi library only supports
               gss_mech_krb5_old and gss_mech_krb5 mechanism types, so
               we're explicity requiring gss_mech_krb5 to be used */
            tmpOid = mech.getNativeOid();

            if (!tmpOid.toDotString().equals("1.2.840.113554.1.2.2")) {
                throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
            }
        }

        maj_status = gsswrapper.gss_canonicalize_name(min_status,
               internGSSName, 
               tmpOid,
               tmpName);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            if (DEBUG_ERR == true)
                System.err.println("internal gss_canonicalize_name failed");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        ret = canonicalName.setInternGSSName(tmpName);
        if (ret != 0) {
            if (DEBUG_ERR == true)
                System.err.println("setInternGSSName failed after " + 
                        "canonicalize");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        return canonicalName;
    }

    public byte[] export() throws GSSException {
        long maj_status = 0;
        long[] min_status = {0};

        gss_buffer_desc exportName = new gss_buffer_desc();
        maj_status = gsswrapper.gss_export_name(min_status,
                this.getInternGSSName(),
                exportName);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        byte[] tmp_array = new byte[(int)exportName.getLength()];
        tmp_array = gsswrapper.getDescArray(exportName);

        gsswrapper.gss_release_buffer(min_status, exportName);
        
        return tmp_array;
    }

    public String toString() {
        long maj_status = 0;
        long[] min_status = {0};
        String outString;

        gss_buffer_desc output_name_buffer = new gss_buffer_desc();
        gss_OID_desc output_name_type = new gss_OID_desc();

        maj_status = gsswrapper.gss_display_name(min_status,
                this.internGSSName,
                output_name_buffer,
                output_name_type);

        if (maj_status != gsswrapper.GSS_S_COMPLETE)
            return null;

        outString = output_name_buffer.getValue();

        return outString;
    }

    public Oid getStringNameType() throws GSSException {
        long maj_status = 0;
        long[] min_status = {0};

        gss_buffer_desc output_name_buffer = new gss_buffer_desc();
        gss_OID_desc output_name_type = new gss_OID_desc();

        maj_status = gsswrapper.gss_display_name(min_status,
                this.getInternGSSName(),
                output_name_buffer,
                output_name_type);

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            return null;
        }

        Oid newOid = new Oid(output_name_type.toDotString());

        return newOid;
    }

    public boolean isAnonymous() {

        try {
            if (GSSName.NT_ANONYMOUS.equals(this.getStringNameType()))
                return true;
            return false;
        } catch (GSSException e) {
            return false;
        }
    }

    public boolean isMN() {
        
        long maj_status = 0;
        long[] min_status = {0};
        gss_buffer_desc exportName = new gss_buffer_desc();

        /* to test if our GSSName is a MN, we call the native
           export_name function and check for the
           GSS_S_NAME_NOT_MN error code */
        maj_status = gsswrapper.gss_export_name(min_status,
                this.getInternGSSName(),
                exportName);

        if (maj_status == gsswrapper.GSS_S_NAME_NOT_MN) {
            return false;
        }
        return true;

    }

    public gss_name_t_desc getInternGSSName() {
        return this.internGSSName;
    }
   
    public int setInternGSSName(gss_name_t_desc newName) {
        if (newName != null) {
            this.internGSSName = newName;
        } else {
            this.internGSSName = gsswrapper.GSS_C_NO_NAME;
        }

        return 0;
    }

    GSSName importName(String nameStr, Oid nameType) throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        gss_buffer_desc nameBuffer = new gss_buffer_desc();

        if (nameStr == null) {
            if (DEBUG_ERR == true)
                System.out.println("nameStr == null, during createName");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        nameBuffer.setLength(nameStr.length());
        nameBuffer.setValue(nameStr);

        /* Check supported nametypes. If it's not a supported one, or null,
           throw exception */
        if (nameType != null &&
            !nameType.equals(GSSName.NT_HOSTBASED_SERVICE) &&
            !nameType.equals(GSSName.NT_USER_NAME) &&
            !nameType.equals(GSSName.NT_MACHINE_UID_NAME) &&
            !nameType.equals(GSSName.NT_STRING_UID_NAME) &&
            !nameType.equals(GSSName.NT_ANONYMOUS)) {
                /* nametype is not supported */
                if (DEBUG_ERR == true)
                    System.out.println("nametype not supported");
                throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
            }

        /* initialize internal gss_name_t_desc */
        internGSSName = new gss_name_t_desc();
        
        if (nameType != null) {
            maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                    nameType.getNativeOid(), internGSSName);
        } else {
            /* use the default nametype */
            maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                    null, internGSSName);
        }

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            if (DEBUG_ERR == true)
                System.out.println("gss_import_name failed, during createName");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }
        
        return this;
    }
    
    GSSName importName(byte[] nameStr, Oid nameType) throws GSSException {
        
        long maj_status = 0;
        long[] min_status = {0};
        gss_buffer_desc nameBuffer = new gss_buffer_desc();

        if (nameStr == null) {
            if (DEBUG_ERR == true)
                System.out.println("nameStr == null, during createName");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }

        /* copy byte[] to native gss_buffer_desc */
        gsswrapper.setDescArray(nameBuffer, nameStr);
        nameBuffer.setLength(nameStr.length);

        /* Check supported nametypes. If it's not a supported one, or null,
           throw exception */
        if (nameType != null &&
            !nameType.equals(GSSName.NT_HOSTBASED_SERVICE) &&
            !nameType.equals(GSSName.NT_USER_NAME) &&
            !nameType.equals(GSSName.NT_MACHINE_UID_NAME) &&
            !nameType.equals(GSSName.NT_STRING_UID_NAME) &&
            !nameType.equals(GSSName.NT_ANONYMOUS) &&
            !nameType.equals(GSSName.NT_EXPORT_NAME)) {
                /* nametype is not supported */
                if (DEBUG_ERR == true)
                    System.out.println("nametype not supported");
                throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
            }

        /* initialize internal gss_name_t_desc */
        internGSSName = new gss_name_t_desc();
        
        if (nameType != null) {
            maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                    nameType.getNativeOid(), internGSSName);
        } else {
            /* use the default nametype */
            maj_status = gsswrapper.gss_import_name(min_status, nameBuffer,
                    null, internGSSName);
        }

        if (maj_status != gsswrapper.GSS_S_COMPLETE) {
            if (DEBUG_ERR == true)
                System.out.println("gss_import_name failed, during createName");
            throw new GSSExceptionImpl((int)maj_status, (int)min_status[0]);
        }
        
        return this;
    }

    public void freeGSSName() {
        if (internGSSName != null) {
            long maj_status = 0;
            long[] min_status = {0};

            maj_status = gsswrapper.gss_release_name(min_status,
                    internGSSName);
        }
    }

}
