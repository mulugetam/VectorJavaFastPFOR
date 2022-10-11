A vector implementation for the JavaFastPFOR Algorithm
------------------------------------------------------
This project offers a vector implementation for Lemire's [JavaFastPFOR](https://github.com/lemire/JavaFastPFOR) integer compression algorithm based on ``jdk.incubator.vector``. 

The implementation offers three classes:
1. ``VectorBitPacker`` -- a vectorized implementation of bit packing mirroring[BitPacking.java](https://github.com/lemire/JavaFastPFOR/blob/master/src/main/java/me/lemire/integercompression/BitPacking.java). Auto-generated from ``VectorBitPackerTerse``. Uses less branch instructions and is recommended for use.
2. ``VectorBitPackerTerse`` -- a shorter version of VectorBitPacker but with more branch instructions (and less efficient).
3. ``VectorFastPFOR`` -- is the vector equivalent for [FastPFOR](https://github.com/lemire/JavaFastPFOR/blob/master/src/main/java/me/lemire/integercompression/FastPFOR.java). Uses a 256 BLOCK_SIZE.

Requirements
-----------------
1. JavaFastPFOR library --  https://github.com/lemire/JavaFastPFOR.
2. JDK 16. We recommend using JDK 19 and above.
3. Gradle and Maven tools.

Building the Library
--------------------
To build the library run:
```
./gradlew assemble
```

The resulting jar is located at ``./build/libs/vector-javafastpfor-1.0.0.jar``.

Running the example
-------------------
Compile:
```
javac -cp ./build/libs/*:./libs/* examples/Example.java
```

Run:
```
java --add-modules jdk.incubator.vector -cp ./build/libs/*:./libs/*:./examples Example 0
```

Performance (using JMH)
----------------------
The VectorFastPFOR algorithm compresses integers in blocks of 256 integers. The below jmh data compares the throughput (in units of blocks of integers compressed/uncompressed per second) of the vector implementation against the default (non-vectorized implementation). Overall, we see gains ranging from 4x to 10x for the vector implementation.

```
Benchmark              bitWidth  Cnt   Score              Error                Err %  Units   Vector Speedup
------------------------------------------------------------------------------------------------------------
FastPFOR.Compress             1   12    9,100,817.37      ± 26,668.17          0.29%  ops/s
FastPFOR.Compress             2   12    8,501,160.99      ± 88,614.69          1.04%  ops/s
FastPFOR.Compress             3   12    8,742,561.99      ± 160,436.11         1.84%  ops/s
FastPFOR.Compress             4   12   10,002,598.95      ± 75,065.07          0.75%  ops/s
FastPFOR.Compress             5   12    9,268,713.67      ± 63,633.93          0.69%  ops/s
FastPFOR.Compress             6   12    9,479,228.03      ± 53,350.26          0.56%  ops/s
FastPFOR.Compress             7   12    9,196,508.85      ± 77,579.52          0.84%  ops/s
FastPFOR.Compress             8   12   12,715,791.49      ± 640,077.56         5.03%  ops/s
FastPFOR.Compress             9   12    9,111,927.51      ± 90,320.09          0.99%  ops/s
FastPFOR.Compress            10   12    9,188,702.07      ± 80,907.75          0.88%  ops/s
FastPFOR.Compress            11   12    8,874,635.33      ± 129,146.12         1.46%  ops/s
FastPFOR.Compress            12   12    9,382,801.76      ± 121,116.04         1.29%  ops/s
FastPFOR.Compress            13   12    8,557,471.60      ± 228,244.55         2.67%  ops/s
FastPFOR.Compress            14   12    8,315,389.90      ± 438,725.66         5.28%  ops/s
FastPFOR.Compress            15   12    8,372,547.94      ± 216,439.97         2.59%  ops/s
FastPFOR.Compress            16   12   12,918,102.63      ± 48,636.86          0.38%  ops/s
FastPFOR.Compress            17   12    7,856,991.20      ± 580,454.64         7.39%  ops/s
FastPFOR.Compress            18   12    8,444,965.44      ± 117,665.15         1.39%  ops/s
FastPFOR.Compress            19   12    7,572,875.11      ± 665,464.51         8.79%  ops/s
FastPFOR.Compress            20   12    8,441,226.32      ± 408,254.12         4.84%  ops/s
FastPFOR.Compress            21   12    7,268,702.44      ± 565,073.60         7.77%  ops/s
FastPFOR.Compress            22   12    7,647,599.05      ± 402,620.60         5.26%  ops/s
FastPFOR.Compress            23   12    7,537,231.89      ± 298,683.58         3.96%  ops/s
FastPFOR.Compress            24   12    8,618,756.83      ± 422,172.96         4.90%  ops/s
FastPFOR.Compress            25   12    6,705,433.21      ± 405,934.46         6.05%  ops/s
FastPFOR.Compress            26   12    7,113,905.30      ± 506,608.16         7.12%  ops/s
FastPFOR.Compress            27   12    6,813,503.84      ± 431,623.69         6.33%  ops/s
FastPFOR.Compress            28   12    7,485,231.46      ± 617,033.48         8.24%  ops/s
FastPFOR.Compress            29   12    7,040,351.79      ± 253,773.72         3.60%  ops/s
FastPFOR.Compress            30   12    7,151,432.90      ± 339,563.74         4.75%  ops/s
FastPFOR.Compress            31   12    6,802,542.71      ± 176,698.46         2.60%  ops/s
FastPFOR.Decompress           1   12   10,505,733.55      ± 10,943.45          0.10%  ops/s
FastPFOR.Decompress           2   12   10,217,377.95      ± 440,495.84         4.31%  ops/s
FastPFOR.Decompress           3   12   10,028,869.96      ± 26,298.61          0.26%  ops/s
FastPFOR.Decompress           4   12   10,641,847.30      ± 253,996.17         2.39%  ops/s
FastPFOR.Decompress           5   12    9,692,370.96      ± 219,485.61         2.26%  ops/s
FastPFOR.Decompress           6   12    9,951,863.25      ± 3,868.91           0.04%  ops/s
FastPFOR.Decompress           7   12    9,467,420.12      ± 188,725.29         1.99%  ops/s
FastPFOR.Decompress           8   12   10,619,954.48      ± 322,351.01         3.04%  ops/s
FastPFOR.Decompress           9   12    8,652,903.03      ± 789,470.01         9.12%  ops/s
FastPFOR.Decompress          10   12    9,237,245.10      ± 267,619.74         2.90%  ops/s
FastPFOR.Decompress          11   12    8,760,492.35      ± 772,256.13         8.82%  ops/s
FastPFOR.Decompress          12   12    9,794,230.89      ± 15,114.72          0.15%  ops/s
FastPFOR.Decompress          13   12    8,538,911.81      ± 660,918.98         7.74%  ops/s
FastPFOR.Decompress          14   12    8,836,756.06      ± 305,626.92         3.46%  ops/s
FastPFOR.Decompress          15   12    7,704,694.55      ± 620,404.27         8.05%  ops/s
FastPFOR.Decompress          16   12   10,624,200.43      ± 369,752.86         3.48%  ops/s
FastPFOR.Decompress          17   12    8,030,756.34      ± 680,015.11         8.47%  ops/s
FastPFOR.Decompress          18   12    8,128,976.38      ± 635,685.62         7.82%  ops/s
FastPFOR.Decompress          19   12    7,868,600.46      ± 603,698.49         7.67%  ops/s
FastPFOR.Decompress          20   12    8,564,625.53      ± 619,280.23         7.23%  ops/s
FastPFOR.Decompress          21   12    7,817,627.13      ± 347,241.29         4.44%  ops/s
FastPFOR.Decompress          22   12    7,866,461.18      ± 531,011.75         6.75%  ops/s
FastPFOR.Decompress          23   12    7,406,330.04      ± 364,666.01         4.92%  ops/s
FastPFOR.Decompress          24   12    9,159,020.56      ± 191,285.63         2.09%  ops/s
FastPFOR.Decompress          25   12    7,422,259.37      ± 442,464.90         5.96%  ops/s
FastPFOR.Decompress          26   12    7,278,067.34      ± 293,093.00         4.03%  ops/s
FastPFOR.Decompress          27   12    6,932,447.38      ± 440,733.45         6.36%  ops/s
FastPFOR.Decompress          28   12    7,738,750.14      ± 526,297.67         6.80%  ops/s
FastPFOR.Decompress          29   12    6,795,704.08      ± 456,742.54         6.72%  ops/s
FastPFOR.Decompress          30   12    6,825,112.91      ± 317,707.26         4.65%  ops/s
FastPFOR.Decompress          31   12    7,064,644.28      ± 182,479.64         2.58%  ops/s
VectorFastPFOR.Compress       1   12   56,351,822.92      ± 5,609,478.28       9.95%  ops/s    6.19
VectorFastPFOR.Compress       2   12   89,442,210.73      ± 7,961,518.70       8.90%  ops/s   10.52
VectorFastPFOR.Compress       3   12   61,688,005.69      ± 4,408,094.44       7.15%  ops/s    7.06
VectorFastPFOR.Compress       4   12   87,624,287.59      ± 1,304,608.65       1.49%  ops/s    8.76
VectorFastPFOR.Compress       5   12   57,931,536.68      ± 1,541,292.52       2.66%  ops/s    6.25
VectorFastPFOR.Compress       6   12   85,444,284.07      ± 1,652,021.97       1.93%  ops/s    9.01
VectorFastPFOR.Compress       7   12   58,405,436.97      ± 2,499,852.10       4.28%  ops/s    6.35
VectorFastPFOR.Compress       8   12   86,847,249.32      ± 1,936,822.49       2.23%  ops/s    6.83
VectorFastPFOR.Compress       9   12   58,962,699.24      ± 5,457,494.14       9.26%  ops/s    6.47
VectorFastPFOR.Compress      10   12   83,339,915.19      ± 2,201,233.20       2.64%  ops/s    9.07
VectorFastPFOR.Compress      11   12   58,248,872.60      ± 2,993,173.99       5.14%  ops/s    6.56
VectorFastPFOR.Compress      12   12   84,071,802.37      ± 2,289,233.50       2.72%  ops/s    8.96
VectorFastPFOR.Compress      13   12   56,373,636.09      ± 1,284,394.64       2.28%  ops/s    6.59
VectorFastPFOR.Compress      14   12   83,034,579.45      ± 1,693,260.33       2.04%  ops/s    9.99
VectorFastPFOR.Compress      15   12   55,309,761.32      ± 1,509,621.24       2.73%  ops/s    6.61
VectorFastPFOR.Compress      16   12   90,027,994.83      ± 2,276,483.70       2.53%  ops/s    6.97
VectorFastPFOR.Compress      17   12   57,333,518.08      ± 3,304,953.89       5.76%  ops/s    7.30
VectorFastPFOR.Compress      18   12   81,301,874.86      ± 1,521,194.67       1.87%  ops/s    9.63
VectorFastPFOR.Compress      19   12   56,567,667.44      ± 3,864,523.96       6.83%  ops/s    7.47
VectorFastPFOR.Compress      20   12   85,359,254.09      ± 8,108,174.00       9.50%  ops/s   10.11
VectorFastPFOR.Compress      21   12   53,150,486.43      ± 1,425,773.95       2.68%  ops/s    7.31
VectorFastPFOR.Compress      22   12   78,230,951.41      ± 633,125.61         0.81%  ops/s   10.23
VectorFastPFOR.Compress      23   12   56,025,372.46      ± 2,769,556.20       4.94%  ops/s    7.43
VectorFastPFOR.Compress      24   12   81,319,781.82      ± 693,784.45         0.85%  ops/s    9.44
VectorFastPFOR.Compress      25   12   52,478,620.60      ± 1,082,782.42       2.06%  ops/s    7.83
VectorFastPFOR.Compress      26   12   72,131,499.75      ± 1,671,529.06       2.32%  ops/s   10.14
VectorFastPFOR.Compress      27   12   53,423,495.85      ± 1,939,029.72       3.63%  ops/s    7.84
VectorFastPFOR.Compress      28   12   76,568,909.17      ± 6,149,119.84       8.03%  ops/s   10.23
VectorFastPFOR.Compress      29   12   51,640,449.10      ± 1,321,516.30       2.56%  ops/s    7.33
VectorFastPFOR.Compress      30   12   69,905,500.65      ± 1,659,740.87       2.37%  ops/s    9.78
VectorFastPFOR.Compress      31   12   51,113,448.94      ± 681,556.37         1.33%  ops/s    7.51
VectorFastPFOR.Decompress     1   12   51,914,544.22      ± 400,047.50         0.77%  ops/s    4.94
VectorFastPFOR.Decompress     2   12   91,188,776.59      ± 8,986,755.16       9.86%  ops/s    8.92
VectorFastPFOR.Decompress     3   12   72,774,174.41      ± 787,655.43         1.08%  ops/s    7.26
VectorFastPFOR.Decompress     4   12   93,863,054.69      ± 7,358,484.62       7.84%  ops/s    8.82
VectorFastPFOR.Decompress     5   12   69,652,612.83      ± 5,905,462.49       8.48%  ops/s    7.19
VectorFastPFOR.Decompress     6   12   97,267,484.48      ± 1,084,914.06       1.12%  ops/s    9.77
VectorFastPFOR.Decompress     7   12   60,837,329.63      ± 969,573.26         1.59%  ops/s    6.43
VectorFastPFOR.Decompress     8   12   97,628,897.76      ± 1,523,490.18       1.56%  ops/s    9.19
VectorFastPFOR.Decompress     9   12   56,245,783.42      ± 2,722,426.71       4.84%  ops/s    6.50
VectorFastPFOR.Decompress    10   12   97,653,811.75      ± 1,419,340.88       1.45%  ops/s   10.57
VectorFastPFOR.Decompress    11   12   51,979,105.17      ± 2,994,505.25       5.76%  ops/s    5.93
VectorFastPFOR.Decompress    12   12   85,487,675.64      ± 11,752,495.40     13.75%  ops/s    8.73
VectorFastPFOR.Decompress    13   12   48,443,433.55      ± 2,209,230.70       4.56%  ops/s    5.67
VectorFastPFOR.Decompress    14   12   92,557,162.41      ± 2,973,172.46       3.21%  ops/s   10.47
VectorFastPFOR.Decompress    15   12   46,497,960.99      ± 2,483,435.73       5.34%  ops/s    6.04
VectorFastPFOR.Decompress    16   12   49,144,684.91      ± 19,187.41          0.04%  ops/s    4.63
VectorFastPFOR.Decompress    17   12   38,769,664.00      ± 223,743.44         0.58%  ops/s    4.83
VectorFastPFOR.Decompress    18   12   46,160,712.25      ± 15,219.89          0.03%  ops/s    5.68
VectorFastPFOR.Decompress    19   12   39,294,861.13      ± 315,283.80         0.80%  ops/s    4.99
VectorFastPFOR.Decompress    20   12   60,364,681.97      ± 21,221,785.04     35.16%  ops/s    7.05
VectorFastPFOR.Decompress    21   12   39,393,996.81      ± 64,932.70          0.16%  ops/s    5.04
VectorFastPFOR.Decompress    22   12   48,522,174.64      ± 14,553.71          0.03%  ops/s    6.17
VectorFastPFOR.Decompress    23   12   39,723,263.71      ± 182,057.99         0.46%  ops/s    5.36
VectorFastPFOR.Decompress    24   12   52,564,640.51      ± 18,039.45          0.03%  ops/s    5.74
VectorFastPFOR.Decompress    25   12   38,739,861.19      ± 94,066.23          0.24%  ops/s    5.22
VectorFastPFOR.Decompress    26   12   48,854,105.54      ± 366,254.98         0.75%  ops/s    6.71
VectorFastPFOR.Decompress    27   12   37,759,181.97      ± 137,782.71         0.36%  ops/s    5.45
VectorFastPFOR.Decompress    28   12   54,257,730.43      ± 458,133.31         0.84%  ops/s    7.01
VectorFastPFOR.Decompress    29   12   37,688,435.15      ± 1,444,064.58       3.83%  ops/s    5.55
VectorFastPFOR.Decompress    30   12   48,164,251.61      ± 1,710,441.65       3.55%  ops/s    7.06
VectorFastPFOR.Decompress    31   12   35,818,355.90      ± 114,531.94         0.32%  ops/s    5.07
```
