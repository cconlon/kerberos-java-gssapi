#!/bin/bash

cd ./examples/build
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:../../native/:/usr/local/lib
java -classpath ../../lib/kerberos-java-gssapi.jar:./ client

