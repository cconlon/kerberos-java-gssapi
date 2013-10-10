/* -*- mode: c; c-basic-offset: 4; indent-tabs-mode: nil -*- */
/* gsswrapper.i - GSS-API SWIG Java wrapper interface file */
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
 * This file is used by SWIG to generate the Java GSS-API SWIG wrapper,
 * used to create the edu.mit.jgss.swig package, subsequently used in the
 * MIT implementation of RFC 5653 (package edu.mit.jgss).
 *
 */

%module gsswrapper
%{
    #include <gssapi.h>
    #include "gsswrapper_wrap.h"

    /* 
     * Structs added from mglueP.h for SWIG wrapper generation.
     * typedef names on some structs modified to allow for
     * consistent Java naming scheme. For example, in C there
     * are several ways to access a structure - direct, pointer,
     * and pointer-to-pointer. In Java, it is ideal to have a
     * single object which can be used in all three of these
     * cases.
     * -- gss_ctx_id_struct
     * -- gss_cred_id_struct
     * -- gss_channel_bindings_struct
     * -- gss_name_struct
     */
    typedef struct gss_ctx_id_struct {
        struct gss_ctx_id_struct *loopback;
        gss_OID         mech_type;
        gss_ctx_id_t    internal_ctx_id;
    } gss_ctx_id_t_desc;

    typedef struct gss_cred_id_struct {
        struct gss_cred_id_struct *loopback;
        int count;
        gss_OID mechs_array;
        gss_cred_id_t   *cred_array;
    } gss_cred_id_t_desc;

    typedef struct gss_channel_bindings_struct gss_channel_bindings_struct;

    typedef struct gss_name_struct {
        struct gss_name_struct *loopback;
        gss_OID             name_type;
        gss_buffer_t        external_name;
        gss_OID         mech_type;
        gss_name_t      mech_name;
    } gss_name_t_desc;

    /* 
     * Hand-rolled JNI function for retrieval of gss_buffer_t 
     * value by Java. Requires gss_buffer_desc as input.
     */
    JNIEXPORT jbyteArray JNICALL 
    Java_edu_mit_jgss_swig_gsswrapperJNI_getDescArray(JNIEnv *jenv, 
            jclass jcls, jlong jarg1, jobject jarg1_) 
    {
        jbyteArray newArray = 0 ;
        gss_buffer_t arg1 = (gss_buffer_t) 0 ;
          
        (void)jenv;
        (void)jcls;
        (void)jarg1_;

        arg1 = *(gss_buffer_t *)&jarg1; 
        newArray = (*jenv)->NewByteArray(jenv, arg1->length); 
        (*jenv)->SetByteArrayRegion(jenv, newArray, 0, arg1->length, 
                (const jbyte*)arg1->value);
        
        return newArray;
    }

    /*
     * Hand-rolled JNI function to set the value (void *) of a 
     * gss_buffer_t object using a Java byte[] as input.
     */
    JNIEXPORT jint JNICALL 
    Java_edu_mit_jgss_swig_gsswrapperJNI_setDescArray(JNIEnv *jenv, jclass jcls,
            jlong jarg1, jobject jarg1_, jbyteArray jarg2) 
    {
        jint jresult = 0 ;
        gss_buffer_t arg1 = (gss_buffer_t) 0 ;
        jsize len;

        (void)jenv;
        (void)jcls;
        (void)jarg1_;
        arg1 = *(gss_buffer_t *)&jarg1; 

        /* Convert jbyteArray (jarg2) to void array (arg2) */
        len = (*jenv)->GetArrayLength(jenv, jarg2);
        arg1->value = malloc(len * sizeof(void *));
        (*jenv)->GetByteArrayRegion(jenv, jarg2, 0, len, arg1->value);

        return jresult;
    }

    /*
     * Wrapper for gss_display_status. Needed because Java passes in a
     * long for min_stat instead of a pointer to a long (or OM_uint32)
     */
    OM_uint32 gss_display_status_wrap(OM_uint32 min_stat, 
            OM_uint32 status_value, int status_type, gss_OID mech_type,
            OM_uint32 *message_context, gss_buffer_t status_string)
    {
        OM_uint32 *min_stat_ptr;
        OM_uint32 ret;
        min_stat_ptr = &min_stat;

        ret = gss_display_status(min_stat_ptr, status_value, status_type,
                mech_type, message_context, status_string);

        return ret;
    }

    /*
     * Structure helper functions.
     * Usually for either getting or setting members.
     * Used by SWIG %extend
     */
    gss_OID gssOIDset_getElement(gss_OID_set_desc *inputset, int offset)
    {
        gss_OID temp_oid;
        temp_oid = &inputset->elements[offset];
        return temp_oid;
    }

    int gssOIDset_addElement(gss_OID_set_desc *inputset, gss_OID newelement)
    {
        /* TODO */
        return 0;
    }
%}
/*
===========================================================================
custom Java code
===========================================================================
*/

/* 
 * ------------ AUTO-LOAD LIBRARY -----------------
 * Let JNI load our library so Java doesn't have to
 * ------------------------------------------------
 * Verify that the following "jniclasscode" block
 * contains the shared library name you want the SWIG 
 * wrapper/Java to auto-load.
 */

%pragma(java) jniclasscode=%{
    static {
        try {
            System.loadLibrary("gsswrapper");
        } catch(UnsatisfiedLinkError e) {
            System.err.println("Unable to load libgsswrapper. " + 
                    "Check LD_LIBRARY_PATH environment variable.\n" + e);
            System.exit(1);
        }
    }
%}


/* This code is inserted directly into Java "gsswrapper" module */
%pragma(java) modulecode=%{
    
    /*
     * The macros that test status codes for error conditions.  Note that the
     * GSS_ERROR() macro has changed slightly from the V1 GSSAPI so that it 
     * now evaluates its argument only once.
     */
    public static final long GSS_CALLING_ERROR(long x)
    {
        return (x & (gsswrapperConstants.GSS_C_CALLING_ERROR_MASK <<
                    gsswrapperConstants.GSS_C_CALLING_ERROR_OFFSET));
    }

    public static final long GSS_ROUTINE_ERROR(long x)
    {
        return (x & (gsswrapperConstants.GSS_C_ROUTINE_ERROR_MASK <<
                    gsswrapperConstants.GSS_C_ROUTINE_ERROR_OFFSET));
    }
    
    public static final long GSS_SUPPLEMENTARY_INFO(long x)
    {
        return (x & (gsswrapperConstants.GSS_C_SUPPLEMENTARY_MASK <<
                    gsswrapperConstants.GSS_C_SUPPLEMENTARY_OFFSET));
    }

    public static final long GSS_ERROR(long x)
    {
        return (x & ((gsswrapperConstants.GSS_C_CALLING_ERROR_MASK <<
                    gsswrapperConstants.GSS_C_CALLING_ERROR_OFFSET) |
                    (gsswrapperConstants.GSS_C_ROUTINE_ERROR_MASK) <<
                    gsswrapperConstants.GSS_C_ROUTINE_ERROR_OFFSET));
    }

    /* XXXX these are not part of the GSSAPI C bindings!  (but should be) */
    public static final long GSS_CALLING_ERROR_FIELD(long x)
    {
        return ( (x >> gsswrapperConstants.GSS_C_CALLING_ERROR_OFFSET)
                    & gsswrapperConstants.GSS_C_CALLING_ERROR_MASK);
    }
    public static final long GSS_ROUTINE_ERROR_FIELD(long x)
    {
        return ( (x >> gsswrapperConstants.GSS_C_ROUTINE_ERROR_OFFSET)
                    & gsswrapperConstants.GSS_C_ROUTINE_ERROR_MASK);
    }
    public static final long GSS_SUPPLEMENTARY_INFO_FIELD(long x)
    {
        return ( (x >> gsswrapperConstants.GSS_C_SUPPLEMENTARY_OFFSET)
                    & gsswrapperConstants.GSS_C_SUPPLEMENTARY_MASK);
    }
%}

/*
===========================================================================
exclusions
===========================================================================
*/

/* MIT doesn't implement these */
%ignore gss_import_name_object;
%ignore gss_export_name_object;


/*
===========================================================================
typemaps and definitions

NOTE: GCC will throw some warning messages about "cast to pointer from 
integer of different size." This is a known message caused by SWIG casting
casting conventions. The approach taken by SWIG is claimed to be "mostly 
portable."
===========================================================================
*/

%include typemaps.i

typedef long ssize_t;           /* From gss <sys/types.h> */
typedef unsigned int uint32_t;  /* for SWIG convienence */

/* 
 * TYPEMAP:  void * <--> jbyteArray
 * -------------------------------
 * void * output typemaps for custom JNI buffer_toArray function 
 *
 */
%typemap(jni) void * "jbyteArray"
%typemap(jtype) void * "byte[]"
%typemap(jstype) void * "byte[]"
%typemap(in) void * { $1 = $input; }
%typemap(out) void * { $result = $1; }
%typemap(javain) void * "$javainput"
%typemap(javaout) void * { 
    return $jnicall; 
}

/* 
 * TYPEMAP:  void *value (native) <--> String (Java)
 * -------------------------------------------------
 * C "void *value" and "void *elements" types equal 
 * Java String... handle conversion here 
 *
 */
%typemap(jni) void *value, void *elements "jobject"
%typemap(jtype) void *value, void *elements "String"
%typemap(jstype) void *value, void *elements "String"
%typemap(in) void *value, void *elements {
    $1 = NULL;
    if ($input != NULL) {
        /* Get our Java String as a jstring */
        jclass javaStringClass = (*jenv)->GetObjectClass(jenv, $input);
        jmethodID javaToString = (*jenv)->GetMethodID(jenv, javaStringClass, 
                "toString", "()Ljava/lang/String;");
        jstring javaString = (jstring) (*jenv)->CallObjectMethod(jenv, 
                $input, javaToString);

        /* Convert Java String to a C string */
        const char *nativeString = (*jenv)->GetStringUTFChars(jenv, 
                javaString, 0);

        /* Copy const char* to void* and give it back to GSS-API */
        int len = (*jenv)->GetStringLength(jenv, javaString);
        $1 = (void *) malloc(len+1);
        strcpy($1, nativeString);

       /* Release the UTF string we obtained with GetStringUTFChars */
       (*jenv)->ReleaseStringUTFChars(jenv, javaString, nativeString);
    }
}
%typemap(javain) void *value, void *elements "$javainput"
%typemap(out) void *value, void *elements {
    if ($1 != NULL) {

        /* Create new Java String from C void* */
        jstring newString = (*jenv)->NewStringUTF(jenv, $1);
        $result = newString;
    }
}
%typemap(javaout) void *value, void *elements {
    return $jnicall;
}

/*
 * TYPEMAP: (char * BYTE, int LENGTH) (native) <--> byte[] (Java)
 * --------------------------------------------------------------
 *
 */
/*%typemap(in) (char * BYTE, int LENGTH) {
    $1 = (char *) JCALL2(GetByteArrayElements, jenv, $input, 0);
    $2 = (int)    JCALL1(GetArrayLength, jenv, $input);
}
%typemap(jni) (char * BYTE, int LENGTH) "jbyteArray"
%typemap(jtype) (char * BYTE, int LENGTH) "byte[]"
%typemap(jstype) (char * BYTE, int LENGTH) "byte[]"
%typemap(javain) (char * BYTE, int LENGTH) "$javainput"
%apply (char * BYTE, int LENGTH) { (char * byteArray, long len) };*/
%typemap(in) (char * BYTE, int LENGTH) {
    $1 = NULL;
    $2 = NULL;
    if ($input != NULL) {
        /* Get our Java byte array as a char * */
        const char* nativeArray = 
            (char*) (*jenv)->GetByteArrayElements(jenv, $input, NULL);

        /* Get the length of our byte array */
        $2 = (*jenv)->GetArrayLength(jenv, $input);

        /* Copy char* and give it back to Java GSS-API */
        $1 = (char *) malloc($2);
        strcpy($1, nativeArray);

        /* Release the Java byte[] */
        (*jenv)->ReleaseByteArrayElements(jenv, $input, nativeArray, JNI_ABORT);
    }
}
%typemap(jni) (char * BYTE, int LENGTH) "jbyteArray"
%typemap(jtype) (char * BYTE, int LENGTH) "byte[]"
%typemap(jstype) (char * BYTE, int LENGTH) "byte[]"
%typemap(javain) (char * BYTE, int LENGTH) "$javainput"
%apply (char * BYTE, int LENGTH) { (char * byteArray, long len) };

/* 
 * TYPEMAP:  gss_OID_set * (native) <-->  gss_OID_set_desc (Java)
 * --------------------------------------------------------------
 * Typemap for marshalling gss_OID_set_desc ** 
 *
 */
%typemap(jni) gss_OID_set * "jobject"
%typemap(jtype) gss_OID_set * "gss_OID_set_desc"
%typemap(jstype) gss_OID_set * "gss_OID_set_desc"
%typemap(in) gss_OID_set * (gss_OID_set_desc *sOID){
    /* Convert Java gss_OID_set_desc *to C gss_OID_set * */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_OID_set_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_OID_set_desc *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_OID_set * {
    /* Give Java gss_OID_set_desc class a gss_OID_set_desc pointer 
       Conversion: gss_OID_set_desc ** -> gss_OID_set_desc * */
    if ($input != NULL) {
        /* Copy back temporary struct to gss_OID_set_desc Java class */
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_OID_set_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    } 
}
%typemap(javain) gss_OID_set * "$javainput"

/* 
 * TYPEMAP:  gss_OID * (native) <--> gss_OID_desc (Java)
 * -----------------------------------------------------
 * Typemap for marshalling gss_OID_desc 
 *
 */
%typemap(jni) gss_OID * "jobject"
%typemap(jtype) gss_OID * "gss_OID_desc"
%typemap(jstype) gss_OID * "gss_OID_desc"
%typemap(in) gss_OID * (gss_OID_desc *sOID) {
    /* Convert incoming Java gss_OID_desc to a native gss_OID pointer */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_OID_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        
        /* Get pointer to gss_OID_desc */
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_OID_desc *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_OID * {
    /* Convert outgoing native gss_OID * to Java gss_OID_desc (gss_OID **) */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_OID_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    }
}
%typemap(javain) gss_OID * "$javainput"
%typemap(javacode) gss_OID_desc %{
   public int hashCode() {
       return (int)swigCPtr;
   }
%}


/* 
 * TYPEMAP:  gss_ctx_id_t * (native) <--> gss_ctx_id_t_desc (Java)
 * ---------------------------------------------------------------
 * Typemap for marshalling gss_ctx_id_t_desc 
 *
 */
%typemap(jni) gss_ctx_id_t * "jobject"
%typemap(jtype) gss_ctx_id_t * "gss_ctx_id_t_desc"
%typemap(jstype) gss_ctx_id_t * "gss_ctx_id_t_desc"
%typemap(in) gss_ctx_id_t * (gss_ctx_id_t_desc *sOID) {
    /* Convert incoming Java gss_ctx_id_t_desc to a native 
       gss_ctx_id_t pointer */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_ctx_id_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");

        /* Get pointer to gss_ctx_id_t_desc */
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_ctx_id_t_desc *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_ctx_id_t * {
    /* Convert outgoing native gss_ctx_id_t * to Java gss_ctx_id_t_desc */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_ctx_id_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    }
}
%typemap(javain) gss_ctx_id_t * "$javainput"

/*
 * TYPEMAP:  gss_cred_id_t * (native) <--> gss_cred_id_t_desc (Java)
 * ----------------------------------------------------------------- 
 * Typemap for marshalling gss_cred_id_t_desc 
 *
 */
%typemap(jni) gss_cred_id_t * "jobject"
%typemap(jtype) gss_cred_id_t * "gss_cred_id_t_desc"
%typemap(jstype) gss_cred_id_t * "gss_cred_id_t_desc"
%typemap(in) gss_cred_id_t * (gss_cred_id_t_desc *sOID) {
    /* Convert incoming Java gss_cred_id_t_desc to a native 
       gss_cred_id_t pointer */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_cred_id_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        
        /* Get pointer to gss_cred_id_t_desc */
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_cred_id_t_desc *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_cred_id_t * {
    /* Convert outgoing native gss_cred_id_t * to Java gss_cred_id_t_desc */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_cred_id_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    }
}
%typemap(javain) gss_cred_id_t * "$javainput"
%typemap(javaout) gss_cred_id_t * {
    return $jnicall;
}

/*
 * TYPEMAP:  gss_channel_bindings_t * (native) <--> 
 *           gss_channel_bindings_struct (Java)
 * ----------------------------------------------------------------- 
 * Typemap for marshalling gss_channel_bindings_struct
 *
 */
%typemap(jni) gss_channel_bindings_t * "jobject"
%typemap(jtype) gss_channel_bindings_t * "gss_channel_bindings_struct"
%typemap(jstype) gss_channel_bindings_t * "gss_channel_bindings_struct"
%typemap(in) gss_channel_bindings_t * (gss_channel_bindings_struct *sOID) {
    /* Convert incoming Java gss_channel_bindings_struct to a 
       native gss_channel_bindings_t pointer */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_channel_bindings_struct");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");

        /* Get pointer to gss_channel_bindings_struct */
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_channel_bindings_struct *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_channel_bindings_t * {
    /* Convert outgoing native gss_channel_bindings_t * to 
       Java gss_channel_bindings_struct */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_channel_bindings_struct");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    }
}
%typemap(javain) gss_channel_bindings_t * "$javainput"
%typemap(javaout) gss_channel_bindings_t * {
    return $jnicall;
}

/*
 * TYPEMAP:  gss_name_t * (native) <--> gss_name_t_desc (Java)
 * ----------------------------------------------------------------- 
 * Typemap for marshalling gss_name_t_desc 
 *
 */
%typemap(jni) gss_name_t * "jobject"
%typemap(jtype) gss_name_t * "gss_name_t_desc"
%typemap(jstype) gss_name_t * "gss_name_t_desc"
%typemap(in) gss_name_t * (gss_name_t_desc *sOID) {
    /* Convert incoming Java gss_name_t_desc to a native gss_name_t pointer */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_name_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        
        /* Get pointer to gss_name_t_desc */
        jlong cPtr = (*jenv)->GetLongField(jenv, $input, fid);

        sOID = (gss_name_t_desc *)*(&cPtr);
        $1 = &sOID;
    }
}
%typemap(argout) gss_name_t * {
    /* Convert outgoing native gss_name_t * to Java gss_name_t_desc */
    if ($input != NULL) {
        jclass clazz = (*jenv)->FindClass(jenv, 
                "edu/mit/jgss/swig/gss_name_t_desc");
        jfieldID fid = (*jenv)->GetFieldID(jenv, clazz, "swigCPtr", "J");
        jlong cPtr = 0;
        cPtr = (long) *$1;
        (*jenv)->SetLongField(jenv, $input, fid, cPtr);
    }
}
%typemap(javain) gss_name_t * "$javainput"
%typemap(javaout) gss_name_t * {
    return $jnicall;
}

/*
===========================================================================
Pullled in from mglueP.h to help complete Java struct wrappers
===========================================================================
*/

typedef struct gss_ctx_id_struct {
    struct gss_ctx_id_struct *loopback;
    gss_OID         mech_type;
    gss_ctx_id_t    internal_ctx_id;
} gss_ctx_id_t_desc;

typedef struct gss_cred_id_struct {
    struct gss_cred_id_struct *loopback;
    int count;
    gss_OID mechs_array;
    gss_cred_id_t   *cred_array;
} gss_cred_id_t_desc;

typedef struct gss_name_struct {
    struct gss_name_struct *loopback;
    gss_OID             name_type;
    gss_buffer_t        external_name;
    gss_OID         mech_type;
    gss_name_t      mech_name;
} gss_name_t_desc;

/*
===========================================================================
Pulled in from gssapi.h, with minor modifications for SWIG.
===========================================================================
*/

#ifndef KRB5_CALLCONV
#define KRB5_CALLCONV
#define KRB5_CALLCONV_C
#endif

/*
 * First, define the three platform-dependent pointer types.
 */

/* struct gss_name_struct; */
typedef struct gss_name_struct * gss_name_t;

/* struct gss_cred_id_struct; */
typedef struct gss_cred_id_struct * gss_cred_id_t;

/*struct gss_ctx_id_struct;*/
typedef struct gss_ctx_id_struct * gss_ctx_id_t;

/*
 * The following type must be defined as the smallest natural unsigned integer
 * supported by the platform that has at least 32 bits of precision.
 */
typedef uint32_t gss_uint32;
typedef int32_t gss_int32;

#ifdef  OM_STRING
/*
 * We have included the xom.h header file.  Use the definition for
 * OM_object identifier.
 */
typedef OM_object_identifier    gss_OID_desc, *gss_OID;
#else   /* OM_STRING */
/*
 * We can't use X/Open definitions, so roll our own.
 */
typedef gss_uint32      OM_uint32;

typedef struct gss_OID_desc_struct {
    OM_uint32 length;
    void *elements;
} gss_OID_desc, *gss_OID;
#endif  /* OM_STRING */

typedef struct gss_OID_set_desc_struct  {
    size_t  count;
    gss_OID elements;
} gss_OID_set_desc, *gss_OID_set;

typedef struct gss_buffer_desc_struct {
    size_t length;
    void *value;
} gss_buffer_desc, *gss_buffer_t;

typedef struct gss_channel_bindings_struct {
    OM_uint32 initiator_addrtype;
    gss_buffer_desc initiator_address;
    OM_uint32 acceptor_addrtype;
    gss_buffer_desc acceptor_address;
    gss_buffer_desc application_data;
} *gss_channel_bindings_t;

/*
 * For now, define a QOP-type as an OM_uint32 (pending resolution of ongoing
 * discussions).
 */
typedef OM_uint32       gss_qop_t;
typedef int             gss_cred_usage_t;

/*
 * Flag bits for context-level services.
 */
#define GSS_C_DELEG_FLAG        1
#define GSS_C_MUTUAL_FLAG       2
#define GSS_C_REPLAY_FLAG       4
#define GSS_C_SEQUENCE_FLAG     8
#define GSS_C_CONF_FLAG         16
#define GSS_C_INTEG_FLAG        32
#define GSS_C_ANON_FLAG         64
#define GSS_C_PROT_READY_FLAG   128
#define GSS_C_TRANS_FLAG        256
#define GSS_C_DELEG_POLICY_FLAG 32768

/*
 * Credential usage options
 */
#define GSS_C_BOTH      0
#define GSS_C_INITIATE  1
#define GSS_C_ACCEPT    2

/*
 * Status code types for gss_display_status
 */
#define GSS_C_GSS_CODE  1
#define GSS_C_MECH_CODE 2

/*
 * The constant definitions for channel-bindings address families
 */
#define GSS_C_AF_UNSPEC     0
#define GSS_C_AF_LOCAL      1
#define GSS_C_AF_INET       2
#define GSS_C_AF_IMPLINK    3
#define GSS_C_AF_PUP        4
#define GSS_C_AF_CHAOS      5
#define GSS_C_AF_NS         6
#define GSS_C_AF_NBS        7
#define GSS_C_AF_ECMA       8
#define GSS_C_AF_DATAKIT    9
#define GSS_C_AF_CCITT      10
#define GSS_C_AF_SNA        11
#define GSS_C_AF_DECnet     12
#define GSS_C_AF_DLI        13
#define GSS_C_AF_LAT        14
#define GSS_C_AF_HYLINK     15
#define GSS_C_AF_APPLETALK  16
#define GSS_C_AF_BSC        17
#define GSS_C_AF_DSS        18
#define GSS_C_AF_OSI        19
#define GSS_C_AF_NETBIOS    20
#define GSS_C_AF_X25        21

#define GSS_C_AF_NULLADDR   255

/*
 * Various Null values.
 */
%constant gss_name_t GSS_C_NO_NAME = 0;
%constant gss_buffer_t GSS_C_NO_BUFFER = 0;
%constant gss_OID GSS_C_NO_OID = 0;
%constant gss_OID_set GSS_C_NO_OID_SET = 0;
%constant gss_ctx_id_t GSS_C_NO_CONTEXT = 0;
%constant gss_cred_id_t GSS_C_NO_CREDENTIAL = 0;
%constant gss_channel_bindings_t GSS_C_NO_CHANNEL_BINDINGS = 0;
%constant gss_buffer_t GSS_C_EMPTY_BUFFER = 0;

/*
 * Some alternate names for a couple of the above values.  These are defined
 * for V1 compatibility.
 */
#define GSS_C_NULL_OID          GSS_C_NO_OID
#define GSS_C_NULL_OID_SET      GSS_C_NO_OID_SET

/*
 * Define the default Quality of Protection for per-message services.  Note
 * that an implementation that offers multiple levels of QOP may either reserve
 * a value (for example zero, as assumed here) to mean "default protection", or
 * alternatively may simply equate GSS_C_QOP_DEFAULT to a specific explicit
 * QOP value.  However a value of 0 should always be interpreted by a GSSAPI
 * implementation as a request for the default protection level.
 */
#define GSS_C_QOP_DEFAULT 0

/*
 * Expiration time of 2^32-1 seconds means infinite lifetime for a
 * credential or security context
 */
%constant long GSS_C_INDEFINITE = (OM_uint32) 0xfffffffful;

/* Major status codes */

#define GSS_S_COMPLETE 0

/*
 * Some "helper" definitions to make the status code macros obvious.
 */
#define GSS_C_CALLING_ERROR_OFFSET 24
#define GSS_C_ROUTINE_ERROR_OFFSET 16
#define GSS_C_SUPPLEMENTARY_OFFSET 0
#define GSS_C_CALLING_ERROR_MASK 0377ul
#define GSS_C_ROUTINE_ERROR_MASK 0377ul
#define GSS_C_SUPPLEMENTARY_MASK 0177777ul

/*
 * Now the actual status code definitions
 */

/*
 * Calling errors:
 */
%constant long GSS_S_CALL_INACCESSIBLE_READ = 
                             (((OM_uint32) 1ul) << GSS_C_CALLING_ERROR_OFFSET);
%constant long GSS_S_CALL_INACCESSIBLE_WRITE = 
                             (((OM_uint32) 2ul) << GSS_C_CALLING_ERROR_OFFSET);
%constant long GSS_S_CALL_BAD_STRUCTURE =
                             (((OM_uint32) 3ul) << GSS_C_CALLING_ERROR_OFFSET);

/*
 * Routine errors:
 */
%constant long GSS_S_BAD_MECH = 
     (((OM_uint32) 1ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_NAME = 
     (((OM_uint32) 2ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_NAMETYPE = 
     (((OM_uint32) 3ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_BINDINGS = 
     (((OM_uint32) 4ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_STATUS = 
     (((OM_uint32) 5ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_SIG = 
     (((OM_uint32) 6ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_NO_CRED = 
     (((OM_uint32) 7ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_NO_CONTEXT = 
     (((OM_uint32) 8ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_DEFECTIVE_TOKEN = 
     (((OM_uint32) 9ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_DEFECTIVE_CREDENTIAL =
     (((OM_uint32) 10ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_CREDENTIALS_EXPIRED =
     (((OM_uint32) 11ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_CONTEXT_EXPIRED =
     (((OM_uint32) 12ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_FAILURE = 
     (((OM_uint32) 13ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_QOP = 
     (((OM_uint32) 14ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_UNAUTHORIZED = 
     (((OM_uint32) 15ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_UNAVAILABLE = 
     (((OM_uint32) 16ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_DUPLICATE_ELEMENT =
     (((OM_uint32) 17ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_NAME_NOT_MN =
     (((OM_uint32) 18ul) << GSS_C_ROUTINE_ERROR_OFFSET);
%constant long GSS_S_BAD_MECH_ATTR =
     (((OM_uint32) 19ul) << GSS_C_ROUTINE_ERROR_OFFSET);

/*
 * Supplementary info bits:
 */
#define GSS_S_CONTINUE_NEEDED (1 << (GSS_C_SUPPLEMENTARY_OFFSET + 0))
#define GSS_S_DUPLICATE_TOKEN (1 << (GSS_C_SUPPLEMENTARY_OFFSET + 1))
#define GSS_S_OLD_TOKEN (1 << (GSS_C_SUPPLEMENTARY_OFFSET + 2))
#define GSS_S_UNSEQ_TOKEN (1 << (GSS_C_SUPPLEMENTARY_OFFSET + 3))
#define GSS_S_GAP_TOKEN (1 << (GSS_C_SUPPLEMENTARY_OFFSET + 4))


/*
 * Finally, function prototypes for the GSSAPI routines.
 */

#if defined (_WIN32) && defined (_MSC_VER)
# ifdef GSS_DLL_FILE
#  define GSS_DLLIMP __declspec(dllexport)
# else
#  define GSS_DLLIMP __declspec(dllimport)
# endif
#else
# define GSS_DLLIMP
#endif

/* Reserved static storage for GSS_oids.  Comments are quotes from RFC 2744.
 *
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {10, (void *)"\x2a\x86\x48\x86\xf7\x12\x01\x02\x01\x01"},
 * corresponding to an object-identifier value of
 * {iso(1) member-body(2) United States(840) mit(113554)
 * infosys(1) gssapi(2) generic(1) user_name(1)}.  The constant
 * GSS_C_NT_USER_NAME should be initialized to point
 * to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_USER_NAME;

/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {10, (void *)"\x2a\x86\x48\x86\xf7\x12\x01\x02\x01\x02"},
 * corresponding to an object-identifier value of
 * {iso(1) member-body(2) United States(840) mit(113554)
 * infosys(1) gssapi(2) generic(1) machine_uid_name(2)}.
 * The constant GSS_C_NT_MACHINE_UID_NAME should be
 * initialized to point to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_MACHINE_UID_NAME;

/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {10, (void *)"\x2a\x86\x48\x86\xf7\x12\x01\x02\x01\x03"},
 * corresponding to an object-identifier value of
 * {iso(1) member-body(2) United States(840) mit(113554)
 * infosys(1) gssapi(2) generic(1) string_uid_name(3)}.
 * The constant GSS_C_NT_STRING_UID_NAME should be
 * initialized to point to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_STRING_UID_NAME;

/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {6, (void *)"\x2b\x06\x01\x05\x06\x02"},
 * corresponding to an object-identifier value of
 * {iso(1) org(3) dod(6) internet(1) security(5)
 * nametypes(6) gss-host-based-services(2)).  The constant
 * GSS_C_NT_HOSTBASED_SERVICE_X should be initialized to point
 * to that gss_OID_desc.  This is a deprecated OID value, and
 * implementations wishing to support hostbased-service names
 * should instead use the GSS_C_NT_HOSTBASED_SERVICE OID,
 * defined below, to identify such names;
 * GSS_C_NT_HOSTBASED_SERVICE_X should be accepted a synonym
 * for GSS_C_NT_HOSTBASED_SERVICE when presented as an input
 * parameter, but should not be emitted by GSS-API
 * implementations
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_HOSTBASED_SERVICE_X;

/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {10, (void *)"\x2a\x86\x48\x86\xf7\x12"
 *              "\x01\x02\x01\x04"}, corresponding to an
 * object-identifier value of {iso(1) member-body(2)
 * Unites States(840) mit(113554) infosys(1) gssapi(2)
 * generic(1) service_name(4)}.  The constant
 * GSS_C_NT_HOSTBASED_SERVICE should be initialized
 * to point to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_HOSTBASED_SERVICE;

/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {6, (void *)"\x2b\x06\01\x05\x06\x03"},
 * corresponding to an object identifier value of
 * {1(iso), 3(org), 6(dod), 1(internet), 5(security),
 * 6(nametypes), 3(gss-anonymous-name)}.  The constant
 * and GSS_C_NT_ANONYMOUS should be initialized to point
 * to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_ANONYMOUS;


/*
 * The implementation must reserve static storage for a
 * gss_OID_desc object containing the value
 * {6, (void *)"\x2b\x06\x01\x05\x06\x04"},
 * corresponding to an object-identifier value of
 * {1(iso), 3(org), 6(dod), 1(internet), 5(security),
 * 6(nametypes), 4(gss-api-exported-name)}.  The constant
 * GSS_C_NT_EXPORT_NAME should be initialized to point
 * to that gss_OID_desc.
 */
GSS_DLLIMP extern gss_OID GSS_C_NT_EXPORT_NAME;

/* Function Prototypes */

OM_uint32 KRB5_CALLCONV
gss_acquire_cred(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_name_t,             /* desired_name */
    OM_uint32,              /* time_req */
    gss_OID_set,            /* desired_mechs */
    gss_cred_usage_t,       /* cred_usage */
    gss_cred_id_t *,        /* output_cred_handle */
    gss_OID_set *,          /* actual_mechs */
    OM_uint32 *OUTPUT);     /* time_rec */

OM_uint32 KRB5_CALLCONV
gss_release_cred(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_cred_id_t *);       /* cred_handle */

OM_uint32 KRB5_CALLCONV
gss_init_sec_context(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_cred_id_t,          /* claimant_cred_handle */
    gss_ctx_id_t *,         /* context_handle */
    gss_name_t,             /* target_name */
    gss_OID,                /* mech_type (used to be const) */
    OM_uint32,              /* req_flags */
    OM_uint32,              /* time_req */
    gss_channel_bindings_t,     /* input_chan_bindings */
    gss_buffer_t,           /* input_token */
    gss_OID *,              /* actual_mech_type */
    gss_buffer_t,           /* output_token */
    OM_uint32 *OUTPUT,      /* ret_flags */
    OM_uint32 *OUTPUT);     /* time_rec */

OM_uint32 KRB5_CALLCONV
gss_accept_sec_context(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t *,         /* context_handle */
    gss_cred_id_t,          /* acceptor_cred_handle */
    gss_buffer_t,           /* input_token_buffer */
    gss_channel_bindings_t, /* input_chan_bindings */
    gss_name_t *,           /* src_name */
    gss_OID *,              /* mech_type */
    gss_buffer_t,           /* output_token */
    OM_uint32 *OUTPUT,      /* ret_flags */
    OM_uint32 *OUTPUT,      /* time_rec */
    gss_cred_id_t *);       /* delegated_cred_handle */

OM_uint32 KRB5_CALLCONV
gss_process_context_token(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    gss_buffer_t);          /* token_buffer */


OM_uint32 KRB5_CALLCONV
gss_delete_sec_context(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t *,         /* context_handle */
    gss_buffer_t);          /* output_token */


OM_uint32 KRB5_CALLCONV
gss_context_time(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    OM_uint32 *OUTPUT);     /* time_rec */


/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_get_mic(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    gss_qop_t,              /* qop_req */
    gss_buffer_t,           /* message_buffer */
    gss_buffer_t);          /* message_token */


/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_verify_mic(OM_uint32 *OUTPUT,   /* minor_status */
               gss_ctx_id_t,        /* context_handle */
               gss_buffer_t,        /* message_buffer */
               gss_buffer_t,        /* message_token */
               gss_qop_t *OUTPUT    /* qop_state */
);

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_wrap(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    int,                    /* conf_req_flag */
    gss_qop_t,              /* qop_req */
    gss_buffer_t,           /* input_message_buffer */
    int *OUTPUT,            /* conf_state */
    gss_buffer_t);          /* output_message_buffer */


/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_unwrap(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    gss_buffer_t,           /* input_message_buffer */
    gss_buffer_t,           /* output_message_buffer */
    int *OUTPUT,            /* conf_state */
    gss_qop_t *OUTPUT);     /* qop_state */


OM_uint32 KRB5_CALLCONV
gss_display_status(
    OM_uint32 *OUTPUT,      /* minor_status */
    OM_uint32,              /* status_value */
    int,                    /* status_type */
    gss_OID,                /* mech_type (used to be const) */
    OM_uint32 *INOUT,       /* message_context */
    gss_buffer_t);          /* status_string */


OM_uint32 KRB5_CALLCONV
gss_indicate_mechs(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_OID_set *);         /* mech_set */


OM_uint32 KRB5_CALLCONV
gss_compare_name(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_name_t,             /* name1 */
    gss_name_t,             /* name2 */
    int *OUTPUT);           /* name_equal */


OM_uint32 KRB5_CALLCONV
gss_display_name(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_name_t,             /* input_name */
    gss_buffer_t,           /* output_name_buffer */
    gss_OID *);             /* output_name_type */


OM_uint32 KRB5_CALLCONV
gss_import_name(
    OM_uint32 *OUTPUT,                  /* minor_status */
    gss_buffer_t input_name_buffer,     /* input_name_buffer */
    gss_OID input_name_type,            /* input_name_type(used to be const) */
    gss_name_t *);                      /* output_name */

OM_uint32 KRB5_CALLCONV
gss_release_name(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_name_t *INOUT);     /* input_name */

OM_uint32 KRB5_CALLCONV
gss_release_buffer(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_buffer_t);          /* buffer */

OM_uint32 KRB5_CALLCONV
gss_release_oid_set(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_OID_set *);         /* set */

OM_uint32 KRB5_CALLCONV
gss_inquire_cred(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_cred_id_t,          /* cred_handle */
    gss_name_t *,           /* name */
    OM_uint32 *OUTPUT,      /* lifetime */
    gss_cred_usage_t *OUTPUT, /* cred_usage */
    gss_OID_set *);         /* mechanisms */

/* Last argument new for V2 */
OM_uint32 KRB5_CALLCONV
gss_inquire_context(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    gss_name_t *OUTPUT,     /* src_name */
    gss_name_t *OUTPUT,     /* targ_name */
    OM_uint32 *OUTPUT,      /* lifetime_rec */
    gss_OID *OUTPUT,        /* mech_type */
    OM_uint32 *OUTPUT,      /* ctx_flags */
    int *OUTPUT,            /* locally_initiated */
    int *OUTPUT);           /* open */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_wrap_size_limit(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_ctx_id_t,           /* context_handle */
    int,                    /* conf_req_flag */
    gss_qop_t,              /* qop_req */
    OM_uint32,              /* req_output_size */
    OM_uint32 *OUTPUT);     /* max_input_size */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_import_name_object(
    OM_uint32 *,        /* minor_status */
    void *,             /* input_name */
    gss_OID,            /* input_name_type */
    gss_name_t *);      /* output_name */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_export_name_object(
    OM_uint32 *,        /* minor_status */
    gss_name_t,         /* input_name */
    gss_OID,            /* desired_name_type */
    void **);           /* output_name */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_add_cred(
    OM_uint32 *OUTPUT,      /* minor_status */
    gss_cred_id_t,          /* input_cred_handle */
    gss_name_t,             /* desired_name */
    gss_OID,                /* desired_mech */
    gss_cred_usage_t,       /* cred_usage */
    OM_uint32,              /* initiator_time_req */
    OM_uint32,              /* acceptor_time_req */
    gss_cred_id_t *,        /* output_cred_handle */
    gss_OID_set *,          /* actual_mechs */
    OM_uint32 *OUTPUT,      /* initiator_time_rec */
    OM_uint32 *OUTPUT);     /* acceptor_time_rec */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_inquire_cred_by_mech(
    OM_uint32 *OUTPUT,          /* minor_status */
    gss_cred_id_t,              /* cred_handle */
    gss_OID,                    /* mech_type */
    gss_name_t *,               /* name */
    OM_uint32 *OUTPUT,          /* initiator_lifetime */
    OM_uint32 *OUTPUT,          /* acceptor_lifetime */
    gss_cred_usage_t *OUTPUT);  /* cred_usage */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_export_sec_context(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t *,     /* context_handle */
    gss_buffer_t);      /* interprocess_token */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_import_sec_context(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_buffer_t,       /* interprocess_token */
    gss_ctx_id_t *);    /* context_handle */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_release_oid(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID *);         /* oid */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_create_empty_oid_set(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID_set *);     /* oid_set */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_add_oid_set_member(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID,            /* member_oid */
    gss_OID_set *);     /* oid_set */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_test_oid_set_member(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID,            /* member */
    gss_OID_set,        /* set */
    int *OUTPUT);       /* present */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_str_to_oid(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_buffer_t,       /* oid_str */
    gss_OID *);         /* oid */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_oid_to_str(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID,            /* oid */
    gss_buffer_t);      /* oid_str */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_inquire_names_for_mech(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_OID,            /* mechanism */
    gss_OID_set *);     /* name_types */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_inquire_mechs_for_name(
    OM_uint32 *OUTPUT,  /* minor_status */
    const gss_name_t,   /* input_name */
    gss_OID_set *);     /* mech_types */

/*
 * The following routines are obsolete variants of gss_get_mic, gss_wrap,
 * gss_verify_mic and gss_unwrap.  They should be provided by GSSAPI V2
 * implementations for backwards compatibility with V1 applications.  Distinct
 * entrypoints (as opposed to #defines) should be provided, to allow GSSAPI
 * V1 applications to link against GSSAPI V2 implementations.
 */
OM_uint32 KRB5_CALLCONV
gss_sign(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t,       /* context_handle */
    int,                /* qop_req */
    gss_buffer_t,       /* message_buffer */
    gss_buffer_t);      /* message_token */

OM_uint32 KRB5_CALLCONV
gss_verify(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t,       /* context_handle */
    gss_buffer_t,       /* message_buffer */
    gss_buffer_t,       /* token_buffer */
    int *OUTPUT);       /* qop_state */

OM_uint32 KRB5_CALLCONV
gss_seal(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t,       /* context_handle */
    int,                /* conf_req_flag */
    int,                /* qop_req */
    gss_buffer_t,       /* input_message_buffer */
    int *OUTPUT,        /* conf_state */
    gss_buffer_t);      /* output_message_buffer */

OM_uint32 KRB5_CALLCONV
gss_unseal(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t,       /* context_handle */
    gss_buffer_t,       /* input_message_buffer */
    gss_buffer_t,       /* output_message_buffer */
    int *OUTPUT,        /* conf_state */
    int *OUTPUT);       /* qop_state */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_export_name(
    OM_uint32  *OUTPUT, /* minor_status */
    const gss_name_t,   /* input_name */
    gss_buffer_t);      /* exported_name */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_duplicate_name(
    OM_uint32  *OUTPUT, /* minor_status */
    const gss_name_t,   /* input_name */
    gss_name_t *);      /* dest_name */

/* New for V2 */
OM_uint32 KRB5_CALLCONV
gss_canonicalize_name(
    OM_uint32  *OUTPUT, /* minor_status */
    const gss_name_t,   /* input_name */
    const gss_OID,      /* mech_type */
    gss_name_t *);      /* output_name */

/* RFC 4401 */

#define GSS_C_PRF_KEY_FULL      0
#define GSS_C_PRF_KEY_PARTIAL   1

OM_uint32 KRB5_CALLCONV
gss_pseudo_random(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_ctx_id_t,       /* context */
    int,                /* prf_key */
    const gss_buffer_t, /* prf_in */
    ssize_t,            /* desired_output_len */
    gss_buffer_t);      /* prf_out */

OM_uint32 KRB5_CALLCONV
gss_store_cred(
    OM_uint32 *OUTPUT,          /* minor_status */
    const gss_cred_id_t,        /* input_cred_handle */
    gss_cred_usage_t,           /* input_usage */
    const gss_OID,              /* desired_mech */
    OM_uint32,                  /* overwrite_cred */
    OM_uint32,                  /* default_cred */
    gss_OID_set *,              /* elements_stored */
    gss_cred_usage_t *OUTPUT);  /* cred_usage_stored */

OM_uint32 KRB5_CALLCONV
gss_set_neg_mechs(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_cred_id_t,      /* cred_handle */
    const gss_OID_set); /* mech_set */

#if TARGET_OS_MAC
#    pragma pack(pop)
#endif

/* XXXX This is a necessary evil until the spec is fixed */
/* #define GSS_S_CRED_UNAVAIL GSS_S_FAILURE */
%constant long GSS_S_CRED_UNAVAIL = 
     (((OM_uint32) 13ul) << GSS_C_ROUTINE_ERROR_OFFSET);

/*
 * RFC 5587
 */
typedef const gss_buffer_desc *gss_const_buffer_t;
typedef const struct gss_channel_bindings_struct *gss_const_channel_bindings_t;
typedef const struct gss_ctx_id_struct gss_const_ctx_id_t;
typedef const struct gss_cred_id_struct gss_const_cred_id_t;
typedef const struct gss_name_struct gss_const_name_t;
typedef const gss_OID_desc *gss_const_OID;
typedef const gss_OID_set_desc *gss_const_OID_set;

OM_uint32 KRB5_CALLCONV
gss_indicate_mechs_by_attrs(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_const_OID_set,  /* desired_mech_attrs */
    gss_const_OID_set,  /* except_mech_attrs */
    gss_const_OID_set,  /* critical_mech_attrs */
    gss_OID_set *);     /* mechs */

OM_uint32 KRB5_CALLCONV
gss_inquire_attrs_for_mech(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_const_OID,      /* mech */
    gss_OID_set *,      /* mech_attrs */
    gss_OID_set *);     /* known_mech_attrs */

OM_uint32 KRB5_CALLCONV
gss_display_mech_attr(
    OM_uint32 *OUTPUT,  /* minor_status */
    gss_const_OID,      /* mech_attr */
    gss_buffer_t,       /* name */
    gss_buffer_t,       /* short_desc */
    gss_buffer_t);      /* long_desc */

/*
 * To access these from Java, use the following:
 * ... = gsswrapper.getXXXXXXX();
 */
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MECH_CONCRETE;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MECH_PSEUDO;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MECH_COMPOSITE;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MECH_NEGO;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MECH_GLUE;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_NOT_MECH;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_DEPRECATED;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_NOT_DFLT_MECH;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_ITOK_FRAMED;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_INIT;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_TARG;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_INIT_INIT;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_TARG_INIT;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_INIT_ANON;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_AUTH_TARG_ANON;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_DELEG_CRED;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_INTEG_PROT;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_CONF_PROT;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_MIC;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_WRAP;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_PROT_READY;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_REPLAY_DET;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_OOS_DET;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_CBINDINGS;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_PFS;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_COMPRESS;
GSS_DLLIMP extern gss_const_OID GSS_C_MA_CTX_TRANS;

/*
 * RFC 5801
 */
OM_uint32 KRB5_CALLCONV
gss_inquire_saslname_for_mech(
    OM_uint32 *OUTPUT,  /* minor_status */
    const gss_OID,      /* desired_mech */
    gss_buffer_t,       /* sasl_mech_name */
    gss_buffer_t,       /* mech_name */
    gss_buffer_t        /* mech_description */
);

OM_uint32 KRB5_CALLCONV
gss_inquire_mech_for_saslname(
    OM_uint32 *OUTPUT,  /* minor_status */
    const gss_buffer_t, /* sasl_mech_name */
    gss_OID *           /* mech_type */
);


/*
===========================================================================
struct extensions
===========================================================================
*/

/* Release the char * after constructing a Java String */
%newobject gss_buffer_desc::toString();

%extend gss_buffer_desc_struct {
    gss_buffer_desc_struct() {
        return (gss_buffer_desc *) calloc(1,sizeof(gss_buffer_desc));
    }

    gss_buffer_desc_struct(void *value) {
        gss_buffer_desc *gs;
        gs = (gss_buffer_desc *) malloc (sizeof(gss_buffer_desc));
        gs->value = value;
        gs->length = strlen(gs->value);
        return gs;
    }
    
    /* 
     */
    char * toString() {
        OM_uint32 maj_status, min_status, msg_ctx;
        char* outputStr;
        maj_status = 0;
        min_status = 0;
        msg_ctx = 0;
        

        if ($self) {
            outputStr = malloc($self->length);
            strcpy(outputStr, $self->value);
            return outputStr;
        } else {
            return NULL;
        }
    }
}

/* Release the char * after constructing a Java String */
%newobject gss_OID_desc::toString();
%newobject gss_OID_desc::toDotString();

%extend gss_OID_desc_struct {
    gss_OID_desc_struct() {
       gss_OID_desc *ret = (gss_OID_desc *) calloc(1, sizeof(gss_OID_desc));
       return ret;
    }

    gss_OID_desc_struct(void *value) {
        gss_OID_desc *newoid;
        gss_buffer_desc input_string;
        OM_uint32 maj_status, min_status;

        newoid = (gss_OID_desc *) calloc (1, sizeof(gss_OID_desc));
        input_string.value = value;
        input_string.length = strlen(input_string.value);
        maj_status = gss_str_to_oid(&min_status, &input_string, &newoid);
        if (maj_status != GSS_S_COMPLETE) {
            newoid = GSS_C_NO_OID;
        }
        return newoid;
    }

    gss_OID_desc_struct(char * byteArray, long len) {
        gss_OID_desc *newoid;

        newoid = (gss_OID_desc *) calloc (1, sizeof(gss_OID_desc));
        newoid->length = len;
        newoid->elements = byteArray;
        /*fprintf(stderr, "newoid->length = %lu\n", (long) newoid->length);
        fprintf(stderr, "newoid->elements = ");
        for (i=0; i<len; i++) {
            fprintf(stderr, "%X (%d) ", (char) byteArray[i], (int) byteArray[i]);
        }
        fprintf(stderr, "\n");*/
        return newoid;
    }

    /* 
     * Comparison function for gss_OID_desc and input mech string.
     * Return: 1 on true, 0 on false or error
     */
    int equals(char *mechString_in) {
        OM_uint32 maj_status, min_status;
        gss_buffer_t oidString = NULL;

        maj_status = gss_oid_to_str(&min_status, $self, oidString);
        if (maj_status == GSS_S_COMPLETE &&
               strcmp(oidString->value, mechString_in) == 0 ) {
           return 1;
       } else {
           return 0;
       } 
    }

    /* 
     * Returns a space delimited string with brackets, ie:
     * "{ 1 2 ... 233 }"
     */
    char * toString() {
        OM_uint32 maj_status, min_status;
        gss_buffer_desc oidBuf;
        char* ret;

        maj_status = 0;
        min_status = 0;

        if ($self) {
            /* Convert OID into string representation */
            maj_status = gss_oid_to_str(&min_status, $self, &oidBuf);
            if (maj_status != GSS_S_COMPLETE) {
                return NULL;
            }

            /*
             * Allocate a buffer and copy the oid to it. This buffer
             * will be freed automatically by the intermediate JNI layers.
             */
            ret = (char *)malloc(oidBuf.length);
            memcpy(ret, oidBuf.value, oidBuf.length);

            /* Release the gss_buffer_desc */
            gss_release_buffer(&min_status, &oidBuf);

            return ret;
        } else {
            return NULL;
        }
    }

    /*
     * Returns a dot delimited string representation of the OID, ie:
     * "1.2. ... .233"
     */
    char * toDotString() {
        OM_uint32 maj_status, min_status;
        gss_buffer_desc oidBuf;
        char *newString;
        int elements = 0;
        int i = 0;
        int j = 0;

        maj_status = 0;
        min_status = 0;

        if ($self) {

            /* convert OID into native string representation "{1 2 ... 2}" */
            maj_status = gss_oid_to_str(&min_status, $self, &oidBuf);
            if (maj_status != GSS_S_COMPLETE) {
                return NULL;
            }

            /* determine correct length of new string */
            for (i = 0; ((char*)oidBuf.value)[i] != '\0'; i++) {
                if (((char*)oidBuf.value)[i] != '{' &&
                    ((char*)oidBuf.value)[i] != '}') {
                    elements++;
                }
            }

            newString = malloc(elements + 1);

            /* create new dot-separated string */
            for (i = 0; ((char*)oidBuf.value)[i] != '\0'; i++) {
                if (((char*)oidBuf.value)[i] != ' ' &&
                    ((char*)oidBuf.value)[i] != '{' &&
                    ((char*)oidBuf.value)[i] != '}') {
                    newString[j] = ((char*)oidBuf.value)[i];
                    j++;
                } else if (((char*)oidBuf.value)[i] == ' ' &&
                           ((char*)oidBuf.value)[i-1] != '{' &&
                           ((char*)oidBuf.value)[i+1] != '}') {
                    newString[j] = '.';
                    j++;
                }
            }
            newString[j] = '\0';

            /* Release the buffer */
            gss_release_buffer(&min_status, &oidBuf);
            return newString;

        } else {
            return NULL;
        }
    }

    /* keep the java virtual machine from freeing gss_OID_desc memory.
       If this isn't here, we get often get a segfault when running
       client applications when Java tries to free memory it shouldn't. */
    ~gss_OID_desc_struct() {
        gss_OID_desc *oid = $self;
    }
}

%extend gss_OID_set_desc_struct {
    gss_OID_set_desc_struct() {
        return (gss_OID_set_desc *) calloc(1, sizeof(gss_OID_set_desc));
    }

    gss_OID getElement(int offset) {
        gss_OID temp;

        if ((unsigned int)offset > $self->count)
            return NULL;
        temp = gssOIDset_getElement($self, offset);
        return temp;
    }
}

/*
===========================================================================
helper function prototypes
===========================================================================
*/

OM_uint32 gss_display_status_wrap(OM_uint32 min_stat, OM_uint32 status_value,
        int status_type, gss_OID mech_type, OM_uint32 *INOUT, 
        gss_buffer_t status_string);

/*
===========================================================================
hand-rolled JNI function prototypes
- SWIG will only generate Java code to acces JNI C code
===========================================================================
*/
%native(getDescArray) void * getDescArray(gss_buffer_t buffer);
%native(setDescArray) int setDescArray(gss_buffer_t inputBuffer, 
        void *javaArray);

