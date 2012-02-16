#!/bin/bash

#
# Description: This shell script generates the Java GSS-API interface for 
#              the MIT Kerberos libraries and compiles the example client 
#              and server applications.

# Notes: The paths to Java and Kerberos locations should be updated to match
#        the setup of the development machine which this script is being run
#        on.
#
# Original source developed by yaSSL (http://www.yassl.com)
# 

# Generate the SWIG GSS-API interface
swig -java -package edu.mit.kerberos -outdir ./edu/mit/kerberos -o gsswrapper_wrap.c gsswrapper.i

# Compile and link libgsswrapper.so
gcc -c gsswrapper_wrap.c -I/usr/lib/jvm/java-6-openjdk/include -I/usr/local/include/gssapi
gcc -shared gsswrapper_wrap.o -L/home/myuser/kerberos/src/lib -lgssapi_krb5 -lkrb5 -lk5crypto -lcom_err -lkrb5support -o libgsswrapper.so

# Build our example client and server
javac Util.java client.java server.java

