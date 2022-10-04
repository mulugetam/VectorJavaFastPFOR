#!/bin/bash
mvn install:install-file  -Dfile=../build/libs/vector-javafastpfor-1.0.0.jar \
-DgroupId=me.lemire.integercompression.vector \
-DartifactId=vector-javafastpfor \
-Dversion=1.0.0 \
-Dpackaging=jar \
-DgeneratePom=true
