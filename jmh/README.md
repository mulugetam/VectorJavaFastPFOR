Benchmarking bit packing and integer array compression
------------------------------------------------------
The jmh offers two benchmarks: bitpacking and integer array compression.

Build Instructions
------------------
Before you can build the jmh project, you need to install ``vector-javafastpfor-1.0.0.jar`` in your local maven repository.

```
./install-vector-javafastpfor.sh
```

To build:
```
cd int-codec-benchmark
mvn clean install
```

Running the benchmark
---------------------
To run the BitPacking benchmark:

```
numactl -m 0 -C 2 java \
--add-modules jdk.incubator.vector \
-jar target/benchmarks.jar BitPackingBench
```

To run the integer array compression benchmark:

```
numactl -m 0 -C 2 java \
-agentpath:/home/muler/libperfmap.so \
--add-modules jdk.incubator.vector \
-jar target/benchmarks.jar IntCompressionBench

```
