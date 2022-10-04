// Copyright (C) 2022 Intel Corporation

// SPDX-License-Identifier: Apache-2.0

package me.lemire.integercompression.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import me.lemire.integercompression.*;
import me.lemire.integercompression.vector.*;
import org.openjdk.jmh.annotations.*;

public class IntCompressionBench {

  @State(Scope.Thread)
  public static class FastPFORBenchCompress {
    private final int N = 1310720;
    private int[] data = new int[N];
    private int[] compressed = new int[N];
    private IntegerCODEC codec;
    private int compressedSize;

    @Setup(Level.Trial)
    public void doSetup() {
      for (int k = 0; k < N; k += 1)
        data[k] = 3;

      // throwing in some large values

      for (int k = 0; k < N; k += 5)
        data[k] = 100;
      for (int k = 0; k < N; k += 533)
        data[k] = 10000;

      codec = new Composition(new FastPFOR(), new VariableByte());
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      storeArray("intcompressionbench.default.original.data", data, N);
      storeArray("intcompressionbench.default.compressed.data", compressed,
                 compressedSize);
    }

    public void compress() {
      IntWrapper outoffset = new IntWrapper(0);
      codec.compress(data, new IntWrapper(0), data.length, compressed,
                     outoffset);
      compressedSize = outoffset.intValue();
    }
  }

  @State(Scope.Thread)
  public static class FastPFORBenchDecompress {
    private final int N = 1310720;
    private int[] data = new int[N];
    private int[] compressed;
    private IntegerCODEC codec;

    @Setup(Level.Trial)
    public void doSetup() throws IOException {
      compressed = loadArray("intcompressionbench.default.compressed.data");
      codec = new Composition(new FastPFOR(), new VariableByte());
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      int[] a = loadArray("intcompressionbench.default.original.data");
      if (!Arrays.equals(a, data))
        System.out.println("decompression failed.");
    }

    public void decompress() {
      codec.uncompress(compressed, new IntWrapper(0), compressed.length - 1,
                       data, new IntWrapper(0));
    }
  }

  @State(Scope.Thread)
  public static class VectorFastPFORBenchCompress {
    private final int N = 1310720;
    private int[] data = new int[N];
    private int[] compressed = new int[N];
    private IntegerCODEC codec;
    private int compressedSize;

    @Setup(Level.Trial)
    public void doSetup() {
      for (int k = 0; k < N; k += 1)
        data[k] = 3;

      // throwing in some large values

      for (int k = 0; k < N; k += 5)
        data[k] = 100;
      for (int k = 0; k < N; k += 533)
        data[k] = 10000;

      codec = new Composition(new VectorFastPFOR(), new VariableByte());
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      storeArray("intcompressionbench.vector.original.data", data, N);
      storeArray("intcompressionbench.vector.compressed.data", compressed,
                 compressedSize);
    }

    public void compress() {
      IntWrapper outoffset = new IntWrapper(0);
      codec.compress(data, new IntWrapper(0), data.length, compressed,
                     outoffset);
      compressedSize = outoffset.intValue();
    }
  }

  @State(Scope.Thread)
  public static class VectorFastPFORBenchDecompress {
    private final int N = 1310720;
    private int[] data = new int[N];
    private int[] compressed;
    private IntegerCODEC codec;

    @Setup(Level.Trial)
    public void doSetup() throws IOException {
      compressed = loadArray("intcompressionbench.vector.compressed.data");
      codec = new Composition(new VectorFastPFOR(), new VariableByte());
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      int[] a = loadArray("intcompressionbench.vector.original.data");
      if (!Arrays.equals(a, data))
        System.out.println("decompression failed.");
    }

    public void decompress() {
      codec.uncompress(compressed, new IntWrapper(0), compressed.length, data,
                       new IntWrapper(0));
    }
  }

  private static void storeArray(String fname, int[] arr, int len)
      throws IOException {
    FileWriter writer = new FileWriter(fname);
    for (int i = 0; i < len; i++)
      writer.write(arr[i] + ",");
    writer.close();
  }

  private static int[] loadArray(String fname) throws IOException {
    String str = "";
    byte[] bytes = Files.readAllBytes(Paths.get(fname));
    str = new String(bytes);
    String[] sarr = str.split(",");
    int[] arr = new int[sarr.length];
    for (int i = 0; i < arr.length; i++)
      arr[i] = Integer.parseInt(sarr[i]);
    return arr;
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void fastPFORCompression(FastPFORBenchCompress state) {
    state.compress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void fastPFORDecompression(FastPFORBenchDecompress state) {
    state.decompress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void vectorFastPFORCompression(VectorFastPFORBenchCompress state) {
    state.compress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void vectorFastPFORDecompression(VectorFastPFORBenchDecompress state) {
    state.decompress();
  }
}
