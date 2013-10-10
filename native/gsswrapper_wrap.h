/* -*- mode: c; c-basic-offset: 4; indent-tabs-mode: nil -*- */
/* gsswrapper_wrap.h - GSS-API SWIG function declarations*/
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
 * Original source developed by wolfSSL (http://www.wolfssl.com)
 *
 * Function prototypes for SWIG-generated functions.
 *
 */

#ifndef __SWIG_GSSWRAPPER_WRAP_H__
#define __SWIG_GSSWRAPPER_WRAP_H__
#ifdef __cplusplus
extern "C" {
#endif

#include <gssapi.h>

JNIEXPORT jbyteArray JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_getDescArray(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
JNIEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_setDescArray(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jbyteArray jarg2);
OM_uint32 gss_display_status_wrap(OM_uint32 min_stat, OM_uint32 status_value, int status_type, gss_OID mech_type, OM_uint32 *message_context, gss_buffer_t status_string);
gss_OID gssOIDset_getElement(gss_OID_set_desc *inputset, int offset);
int gssOIDset_addElement(gss_OID_set_desc *inputset, gss_OID newelement);

SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1loopback_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1loopback_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1mech_1type_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1mech_1type_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1internal_1ctx_1id_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1ctx_1id_1t_1desc_1internal_1ctx_1id_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1ctx_1id_1t_1desc(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1ctx_1id_1t_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1loopback_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1loopback_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1count_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1count_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1mechs_1array_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1mechs_1array_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1cred_1array_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jobject jarg2);
SWIGEXPORT jobject JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1cred_1id_1t_1desc_1cred_1array_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1cred_1id_1t_1desc(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1cred_1id_1t_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1loopback_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1loopback_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1name_1type_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1name_1type_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1external_1name_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1external_1name_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1mech_1type_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1mech_1type_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1mech_1name_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1name_1t_1desc_1mech_1name_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1name_1t_1desc(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1name_1t_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1length_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1length_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1elements_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jobject jarg2);
SWIGEXPORT jobject JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1elements_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1OID_1desc_1_1SWIG_10(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1OID_1desc_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jobject jarg1);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1OID_1desc_1_1SWIG_12(JNIEnv *jenv, jclass jcls, jbyteArray jarg1);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1equals(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jstring jarg2);
SWIGEXPORT jstring JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1toString(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jstring JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1desc_1toDotString(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1OID_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1set_1desc_1count_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1set_1desc_1count_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1set_1desc_1elements_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1set_1desc_1elements_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1OID_1set_1desc(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1OID_1set_1desc_1getElement(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jint jarg2);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1OID_1set_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1buffer_1desc_1length_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1buffer_1desc_1length_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1buffer_1desc_1value_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jobject jarg2);
SWIGEXPORT jobject JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1buffer_1desc_1value_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1buffer_1desc_1_1SWIG_10(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1buffer_1desc_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jobject jarg1);
SWIGEXPORT jstring JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1buffer_1desc_1toString(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1buffer_1desc(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1initiator_1addrtype_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1initiator_1addrtype_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1initiator_1address_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1initiator_1address_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1acceptor_1addrtype_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1acceptor_1addrtype_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1acceptor_1address_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1acceptor_1address_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1application_1data_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1channel_1bindings_1struct_1application_1data_1get(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_new_1gss_1channel_1bindings_1struct(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_delete_1gss_1channel_1bindings_1struct(JNIEnv *jenv, jclass jcls, jlong jarg1);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1DELEG_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MUTUAL_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1REPLAY_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1SEQUENCE_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1CONF_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1INTEG_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1ANON_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1PROT_1READY_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1TRANS_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1DELEG_1POLICY_1FLAG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1BOTH_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1INITIATE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1ACCEPT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1GSS_1CODE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MECH_1CODE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1UNSPEC_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1LOCAL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1INET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1IMPLINK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1PUP_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1CHAOS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1NS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1NBS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1ECMA_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1DATAKIT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1CCITT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1SNA_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1DECnet_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1DLI_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1LAT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1HYLINK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1APPLETALK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1BSC_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1DSS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1OSI_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1NETBIOS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1X25_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1AF_1NULLADDR_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1BUFFER_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1OID_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1OID_1SET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1CONTEXT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1CREDENTIAL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NO_1CHANNEL_1BINDINGS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1EMPTY_1BUFFER_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1QOP_1DEFAULT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1INDEFINITE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1COMPLETE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1CALLING_1ERROR_1OFFSET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1ROUTINE_1ERROR_1OFFSET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1SUPPLEMENTARY_1OFFSET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1CALLING_1ERROR_1MASK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1ROUTINE_1ERROR_1MASK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1SUPPLEMENTARY_1MASK_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CALL_1INACCESSIBLE_1READ_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CALL_1INACCESSIBLE_1WRITE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CALL_1BAD_1STRUCTURE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1MECH_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1NAMETYPE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1BINDINGS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1STATUS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1SIG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1NO_1CRED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1NO_1CONTEXT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1DEFECTIVE_1TOKEN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1DEFECTIVE_1CREDENTIAL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CREDENTIALS_1EXPIRED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CONTEXT_1EXPIRED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1FAILURE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1QOP_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1UNAUTHORIZED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1UNAVAILABLE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1DUPLICATE_1ELEMENT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1NAME_1NOT_1MN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1BAD_1MECH_1ATTR_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CONTINUE_1NEEDED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1DUPLICATE_1TOKEN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1OLD_1TOKEN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1UNSEQ_1TOKEN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1GAP_1TOKEN_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1USER_1NAME_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1USER_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1MACHINE_1UID_1NAME_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1MACHINE_1UID_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1STRING_1UID_1NAME_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1STRING_1UID_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1HOSTBASED_1SERVICE_1X_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1HOSTBASED_1SERVICE_1X_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1HOSTBASED_1SERVICE_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1HOSTBASED_1SERVICE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1ANONYMOUS_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1ANONYMOUS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1EXPORT_1NAME_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1NT_1EXPORT_1NAME_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1acquire_1cred(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jlong jarg4, jobject jarg4_, jint jarg5, jobject jarg6, jobject jarg7, jlongArray jarg8);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1release_1cred(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1init_1sec_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_, jlong jarg6, jlong jarg7, jlong jarg8, jobject jarg8_, jlong jarg9, jobject jarg9_, jobject jarg10, jlong jarg11, jobject jarg11_, jlongArray jarg12, jlongArray jarg13);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1accept_1sec_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_, jobject jarg6, jobject jarg7, jlong jarg8, jobject jarg8_, jlongArray jarg9, jlongArray jarg10, jobject jarg11);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1process_1context_1token(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1delete_1sec_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2, jlong jarg3, jobject jarg3_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1context_1time(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlongArray jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1get_1mic(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1verify_1mic(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jlongArray jarg5);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1wrap(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jlong jarg4, jlong jarg5, jobject jarg5_, jintArray jarg6, jlong jarg7, jobject jarg7_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1unwrap(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jintArray jarg5, jlongArray jarg6);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1display_1status(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jint jarg3, jlong jarg4, jobject jarg4_, jlongArray jarg5, jlong jarg6, jobject jarg6_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1indicate_1mechs(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1compare_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jintArray jarg4);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1display_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jobject jarg4);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1import_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jobject jarg4);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1release_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1release_1buffer(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1release_1oid_1set(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1cred(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3, jlongArray jarg4, jintArray jarg5, jobject jarg6);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3, jobject jarg4, jlongArray jarg5, jobject jarg6, jlongArray jarg7, jintArray jarg8, jintArray jarg9);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1wrap_1size_1limit(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jlong jarg4, jlong jarg5, jlongArray jarg6);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1add_1cred(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jint jarg5, jlong jarg6, jlong jarg7, jobject jarg8, jobject jarg9, jlongArray jarg10, jlongArray jarg11);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1cred_1by_1mech(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jobject jarg4, jlongArray jarg5, jlongArray jarg6, jintArray jarg7);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1export_1sec_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2, jlong jarg3, jobject jarg3_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1import_1sec_1context(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1release_1oid(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1create_1empty_1oid_1set(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jobject jarg2);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1add_1oid_1set_1member(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1test_1oid_1set_1member(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jintArray jarg4);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1str_1to_1oid(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1oid_1to_1str(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1names_1for_1mech(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1mechs_1for_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1sign(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1verify(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jintArray jarg5);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1seal(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jint jarg4, jlong jarg5, jobject jarg5_, jintArray jarg6, jlong jarg7, jobject jarg7_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1unseal(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jintArray jarg5, jintArray jarg6);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1export_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1duplicate_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1canonicalize_1name(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jobject jarg4);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1PRF_1KEY_1FULL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1PRF_1KEY_1PARTIAL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1pseudo_1random(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jlong jarg4, jobject jarg4_, jint jarg5, jlong jarg6, jobject jarg6_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1store_1cred(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jint jarg3, jlong jarg4, jobject jarg4_, jlong jarg5, jlong jarg6, jobject jarg7, jintArray jarg8);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1set_1neg_1mechs(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_);
SWIGEXPORT jint JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1S_1CRED_1UNAVAIL_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1indicate_1mechs_1by_1attrs(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jobject jarg5);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1attrs_1for_1mech(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3, jobject jarg4);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1display_1mech_1attr(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1CONCRETE_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1CONCRETE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1PSEUDO_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1PSEUDO_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1COMPOSITE_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1COMPOSITE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1NEGO_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1NEGO_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1GLUE_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MECH_1GLUE_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1NOT_1MECH_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1NOT_1MECH_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1DEPRECATED_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1DEPRECATED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1NOT_1DFLT_1MECH_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1NOT_1DFLT_1MECH_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1ITOK_1FRAMED_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1ITOK_1FRAMED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1INIT_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1INIT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1INIT_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1INIT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1ANON_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1INIT_1ANON_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1ANON_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1AUTH_1TARG_1ANON_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1DELEG_1CRED_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1DELEG_1CRED_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1INTEG_1PROT_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1INTEG_1PROT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CONF_1PROT_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CONF_1PROT_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MIC_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1MIC_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1WRAP_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1WRAP_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1PROT_1READY_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1PROT_1READY_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1REPLAY_1DET_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1REPLAY_1DET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1OOS_1DET_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1OOS_1DET_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CBINDINGS_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CBINDINGS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1PFS_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1PFS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1COMPRESS_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1COMPRESS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT void JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CTX_1TRANS_1set(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_GSS_1C_1MA_1CTX_1TRANS_1get(JNIEnv *jenv, jclass jcls);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1saslname_1for_1mech(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jlong jarg3, jobject jarg3_, jlong jarg4, jobject jarg4_, jlong jarg5, jobject jarg5_);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1inquire_1mech_1for_1saslname(JNIEnv *jenv, jclass jcls, jlongArray jarg1, jlong jarg2, jobject jarg2_, jobject jarg3);
SWIGEXPORT jlong JNICALL Java_edu_mit_jgss_swig_gsswrapperJNI_gss_1display_1status_1wrap(JNIEnv *jenv, jclass jcls, jlong jarg1, jlong jarg2, jint jarg3, jlong jarg4, jobject jarg4_, jlongArray jarg5, jlong jarg6, jobject jarg6_);


#ifdef __cplusplus
}
#endif
#endif

