#!/bin/bash
mvn clean install
numactl -m 0 -C 2 java \
--add-modules jdk.incubator.vector \
-jar target/benchmarks.jar BitPackingBench
