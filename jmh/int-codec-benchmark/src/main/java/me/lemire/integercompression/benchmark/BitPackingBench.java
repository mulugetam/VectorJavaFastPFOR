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

public class BitPackingBench {

  @State(Scope.Thread)
  public static class DefaultCompression {
    private static final int BLOCK_SIZE = 256;

    @Param({"1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9",  "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23", "24", "25", "26", "27", "28", "29", "30", "31"})
    private static int bitWidth;
    private int[] in = new int[BLOCK_SIZE];
    private int[] ou = new int[BLOCK_SIZE];

    @Setup(Level.Trial)
    public void doSetup() {
      in = new int[BLOCK_SIZE];
      ou = new int[(BLOCK_SIZE / 32) * bitWidth];

      Random r = new Random(123456789L);
      final int max = bitWidth < 30 ? (1 << bitWidth) : (1 << (bitWidth - 3));
      for (int i = 0; i < BLOCK_SIZE; i++)
        in[i] = r.nextInt(max);
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      storeArray("bitpackingbench.default.original.data", in, BLOCK_SIZE);
      storeArray("bitpackingbench.default.compressed.data", ou, ou.length);
    }

    public void compress() {
      int outpos = 0;
      for (int i = 0; i < 8; i++) {
        BitPacking.fastpack(in, i * 32, ou, outpos, bitWidth);
        outpos += bitWidth;
      }
    }
  }

  @State(Scope.Thread)
  public static class DefaultDecompression {
    private static final int BLOCK_SIZE = 256;

    @Param({"1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9",  "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23", "24", "25", "26", "27", "28", "29", "30", "31"})
    private static int bitWidth;

    private int[] in;
    private int[] ou;

    private Random r;

    @Setup(Level.Trial)
    public void doSetup() throws IOException {
      in = loadArray("bitpackingbench.default.compressed.data");
      ou = new int[BLOCK_SIZE];
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      int[] a = loadArray("bitpackingbench.default.original.data");
      if (!Arrays.equals(a, ou))
        System.out.println("decompression failed.");
    }

    public void decompress() {
      int outpos = 0;
      for (int i = 0; i < 8; i++) {
        BitPacking.fastunpack(in, i * bitWidth, ou, outpos, bitWidth);
        outpos += 32;
      }
    }
  }

  @State(Scope.Thread)
  public static class VectorCompression {
    private static final int BLOCK_SIZE = 256;

    @Param({"1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9",  "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23", "24", "25", "26", "27", "28", "29", "30", "31"})
    private static int bitWidth;

    private int[] in = new int[BLOCK_SIZE];
    private int[] ou = new int[BLOCK_SIZE];

    @Setup(Level.Trial)
    public void doSetup() {
      in = new int[BLOCK_SIZE];
      ou = new int[(BLOCK_SIZE / 32) * bitWidth];

      Random r = new Random(123456789L);
      final int max = bitWidth < 30 ? (1 << bitWidth) : (1 << (bitWidth - 3));
      for (int i = 0; i < BLOCK_SIZE; i++)
        in[i] = r.nextInt(max);
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      storeArray("bitpackingbench.vector.original.data", in, BLOCK_SIZE);
      storeArray("bitpackingbench.vector.compressed.data", ou, ou.length);
    }

    public void compress() { VectorBitPacker.fastpack(in, 0, ou, 0, bitWidth); }
  }

  @State(Scope.Thread)
  public static class VectorDecompression {
    private static final int BLOCK_SIZE = 256;

    @Param({"1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9",  "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23", "24", "25", "26", "27", "28", "29", "30", "31"})
    private static int bitWidth;

    private int[] in;
    private int[] ou;

    private Random r;

    @Setup(Level.Trial)
    public void doSetup() throws IOException {
      in = loadArray("bitpackingbench.vector.compressed.data");
      ou = new int[BLOCK_SIZE];
    }

    @TearDown(Level.Trial)
    public void doTeardown() throws IOException {
      int[] a = loadArray("bitpackingbench.vector.original.data");
      if (!Arrays.equals(a, ou))
        System.out.println("decompression failed.");
    }

    public void decompress() {
      VectorBitPacker.fastunpack(in, 0, ou, 0, bitWidth);
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
  public void defaultCompression(DefaultCompression state) {
    state.compress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void vectorCompression(VectorCompression state) {
    state.compress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void defaultDecompression(DefaultDecompression state) {
    state.decompress();
  }

  @Benchmark
  @Fork(warmups = 2, value = 3)
  @Warmup(iterations = 2)
  @Measurement(iterations = 4)
  @BenchmarkMode(Mode.Throughput)
  public void vectorDecompression(VectorDecompression state) {
    state.decompress();
  }
}
