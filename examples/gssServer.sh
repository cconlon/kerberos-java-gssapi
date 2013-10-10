#!/bin/bash

cd ./examples/build
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:../../native/:/usr/local/lib
java -Xbootclasspath/p:../../lib/kerberos-java-gssapi.jar -classpath ../../lib/kerberos-java-gssapi.jar:./ -Dsun.boot.library.path=../../native/ gssServer $@

