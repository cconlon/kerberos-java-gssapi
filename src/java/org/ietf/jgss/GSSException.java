package org.ietf.jgss;

public class GSSException extends Exception {

    public static final int BAD_BINDINGS = 1;
    public static final int BAD_MECH = 2;
    public static final int BAD_NAME = 3;
    public static final int BAD_NAMETYPE = 4;
    public static final int BAD_STATUS = 5;
    public static final int BAD_MIC = 6;
    public static final int CONTEXT_EXPIRED = 7;
    public static final int CREDENTIALS_EXPIRED = 8;
    public static final int DEFECTIVE_CREDENTIAL = 9;
    public static final int DEFECTIVE_TOKEN = 10;
    public static final int FAILURE = 11;
    public static final int NO_CONTEXT = 12;
    public static final int NO_CRED = 13;
    public static final int BAD_QOP = 14;
    public static final int UNAUTHORIZED = 15;
    public static final int UNAVAILABLE = 16;
    public static final int DUPLICATE_ELEMENT = 17;
    public static final int NAME_NOT_MN = 18;
    public static final int DUPLICATE_TOKEN = 19;
    public static final int OLD_TOKEN = 20;
    public static final int UNSEQ_TOKEN = 21;
    public static final int GAP_TOKEN = 22;

    private int majCode;
    private int minCode;
    private String minString;
    private String majString;

    public GSSException(int majorCode) {
        super();
        this.majCode = majorCode;
    }

    public GSSException(int majorCode, int minorCode, String minorString) {
        super();
        this.majCode = majorCode;
        this.minCode = minorCode;
        this.minString = minorString;
    }

    public int getMajor() {
        return this.majCode;
    }

    public int getMinor() {
        return this.minCode;
    }

    public String getMajorString() {
        return this.majString;
    }

    public String getMinorString() {
        return this.minString;
    }

    public void setMinor(int minorCode, String message) {
        this.minCode = minorCode;
        this.minString = message;
    }

    @Override
    public String toString() {
        return "GSSException: " + getMessage();
    }

    @Override
    public String getMessage() {
        return "Major Status: (" + getMajor() + ", " + getMajorString() + "), " + "Minor Status: (" + getMinor() + ", " + getMinorString() + ")";
    }

}
