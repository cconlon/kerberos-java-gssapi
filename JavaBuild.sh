#!/bin/bash

#
# Description: This shell script generates the Java GSS-API interface for 
#              the MIT Kerberos libraries and compiles the example client 
#              and server applications.

# Notes: The paths to Java and Kerberos locations should be updated to match
#        the setup of the development machine which this script is being run
#        on.
#
#        If building on OS X:
#
#        1) The Java include directory is usually similar to:
#           /System/Library/Frameworks/JavaVM.framework/Versions/A/Headers
#        2) The shared library extension should be .dylib (libgsswrapper.dylib)
#           instead of .so.
#
# Original source developed by yaSSL (http://www.yassl.com)
# 

# Create our package directory structure
mkdir -p edu/mit/kerberos

# Generate the SWIG GSS-API interface
swig -java -package edu.mit.kerberos -outdir ./edu/mit/kerberos -o gsswrapper_wrap.c gsswrapper.i

# Compile and link libgsswrapper.so
gcc -c gsswrapper_wrap.c -I/usr/lib/jvm/java-6-openjdk/include -I/usr/local/include/gssapi
gcc -shared gsswrapper_wrap.o -L/usr/local/lib -lgssapi_krb5 -lkrb5 -lk5crypto -lcom_err -lkrb5support -o libgsswrapper.so

# Build our example client and server
javac Util.java client.java server.java

