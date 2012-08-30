package edu.mit.jgss;

import org.ietf.jgss.GSSException;

public class GSSExceptionImpl extends GSSException {

    /* set the minor and major codes, internal gss_display_name
       will handle setting the error strings */ 
    public GSSExceptionImpl(int majorCode, int minorCode) {
        super(majorCode);
        super.setMinor(minorCode, null);
    }

}

