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
package org.ietf.jgss;

/**
 * This is a utility class used within the per-message GSSContext
 * methods to convey per-message properties.
 */
public class MessageProp {

    private int qop;
    private boolean privState;
    private int minorStatus;
    private String minorString;
    private boolean duplicate;
    private boolean old;
    private boolean unseq;
    private boolean gap;

    /**
     * Creates a MessageProp object with the desired privacy state. QOP value
     * is set to 0, indicating that the default QOP is requested.
     *
     * @param privState the desired privacy state. "true" for privacy and
     * "false for integrity only. 
     */ 
    public MessageProp(boolean privState) {

        this.qop = 0;
        this.privState = privState;
    }

    /**
     * Creates a MessageProp object with the desired QOP and privacy state.
     * 
     * @param qop the desired QOP. Use 0 to request a default QOP.
     * @param privState the desired privacy state. "true" for privacy and
     * "false" for integrity only.
     */
    public MessageProp(int qop, boolean privState) {

        this.qop = qop;
        this.privState = privState;
    }

    /**
     * Returns the QOP value.
     *
     * @return current QOP value. 
     */
    public int getQOP() {

        return this.qop;
    }

    /**
     * Returns the privacy state.
     *
     * @return current privacy state. "true" indicates privacy, "false"
     * indicates integrity only.
     */
    public boolean getPrivacy() {

        return this.privState;
    }

    /**
     * Returns the minor status that the underlying mechanism might have set.
     *
     * @return minor status set by underlying mechanism.
     */
    public int getMinorStatus() {

        return this.minorStatus;
    }

    /**
     * Returns the string explanation of the minor code set by the
     * underlying mechanism.
     *
     * @return string explanation of minor code. "null" is returned when
     * no mechanism error code has been set.
     */
    public String getMinorString() {

        return this.minorString;
    }

    /**
     * Sets the QOP value.
     *
     * @param qopValue QOP value to be set. Use 0 to request a default QOP
     * value.
     */
    public void setQOP(int qopValue) {

        this.qop = qopValue;
    }

    /**
     * Sets the privacy state.
     *
     * @param privState the privacy state. Use "true" for privacy, "false" for
     * integrity only.
     */
    public void setPrivacy(boolean privState) {

        this.privState = privState;
    }

    /**
     * Returns "true" if this is a duplicate of an earlier token.
     *
     * @return "true" if token is a duplicate, "false" otherwise.
     */
    public boolean isDuplicateToken() {

        return this.duplicate;
    }

    /**
     * Returns "true" if the token's validity period has expired.
     *
     * @return "true" if the token has expired, "false" otherwise.
     */ 
    public boolean isOldToken() {

        return this.old;
    }

    /**
     * Returns "true" if a later token has already been processed.
     *
     * @return "true" if a later token has been processed.
     */
    public boolean isUnseqToken() {

        return this.unseq;
    }

    /**
     * Returns "true" if an expected per-message token was not received.
     *
     * @return "true" if expected per-message token was not received.
     */
    public boolean isGapToken() {

        return this.gap;
    }

    /**
     * Sets the supplementary information flags and the minor status.
     *
     * @param duplicate "true" if token was a duplicate of an earlier token;
     * otherwise, "false"
     * @param old "true" if the token's validity period has expired;
     * otherwise, "false"
     * @param unseq "true" if a later token has already been processed;
     * otherwise, "false"
     * @param gap "true" if one or more predecessor tokens have not yet been
     * successfully processed; otherwise, "false".
     * @param minorStatus the integer minor status code set by the underlying
     * mechanism.
     * @param minorString textual representation of the minor status value.
     */
    public void setSupplementaryStates(boolean duplicate, boolean old,
                                       boolean unseq, boolean gap,
                                       int minorStatus, String minorString) {
        this.duplicate = duplicate;
        this.old = old;
        this.unseq = unseq;
        this.gap = gap;
        this.minorStatus = minorStatus;
        this.minorString = minorString;
    }
}
