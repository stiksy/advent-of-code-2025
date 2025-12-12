---
layout: default
title: Performance Analysis
nav_order: 13
---

[← Back to Home](../index.html)

# Performance Analysis

Benchmark results for all Advent of Code 2025 solutions. Each solution was executed **100 times** to gather accurate performance statistics.

## Test Environment

- **Iterations per solution**: 100
- **JVM Warm-up**: 10 iterations before measurement
- **Language**: Java 21
- **Build System**: Bazel
- **Measurement**: Pure execution time (excluding compilation)

## Summary Statistics

| Metric | Value |
|--------|-------|
| **Total Solutions** | 12 days |
| **Combined Average Time** | 1.97s |
| **Fastest Solution** | Day 6 (69.0ms) |
| **Slowest Solution** | Day 10 (696.0ms) |
| **Overall Average** | 164.33333333333334ms per solution |

## Detailed Results

### Performance by Day

| Day | Challenge | Avg | Min | Max | Median | P95 | StdDev |
|-----|-----------|-----|-----|-----|--------|-----|--------|
|  1 | Secret Entrance      |     70ms |     60ms |     80ms |     71ms |     76ms |   3.16ms |
|  2 | Gift Shop            |    211ms |    202ms |    227ms |    211ms |    217ms |   3.87ms |
|  3 | Lobby                |     74ms |     61ms |     90ms |     76ms |     83ms |   5.83ms |
|  4 | Printing Department  |     77ms |     65ms |     94ms |     76ms |     90ms |   5.47ms |
|  5 | Cafeteria            |     70ms |     57ms |     85ms |     73ms |     76ms |   5.83ms |
|  6 | Trash Compactor      |     69ms |     55ms |     78ms |     71ms |     75ms |   5.00ms |
|  7 | Laboratories         |     78ms |     72ms |     88ms |     78ms |     84ms |   2.64ms |
|  8 | Playground           |    108ms |     92ms |    116ms |    108ms |    113ms |   3.60ms |
|  9 | Movie Theater        |    115ms |    104ms |    136ms |    113ms |    129ms |   6.63ms |
| 10 | Factory              |    696ms |    605ms |    865ms |    718ms |    749ms |  53.35ms |
| 11 | Reactor              |     76ms |     61ms |    123ms |     77ms |     84ms |   6.63ms |
| 12 | Christmas Tree Farm  |    328ms |    320ms |    370ms |    327ms |    346ms |   7.28ms |

### Visual Performance Comparison

Average execution time per solution (relative scale):

```
Day  1: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 70.0ms
Day  2: ███████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 211.0ms
Day  3: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 74.0ms
Day  4: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 77.0ms
Day  5: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 70.0ms
Day  6: ████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 69.0ms
Day  7: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 78.0ms
Day  8: ███████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 108.0ms
Day  9: ████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 115.0ms
Day 10: ██████████████████████████████████████████████████ 696.0ms
Day 11: █████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 76.0ms
Day 12: ███████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░ 328.0ms
```


## Performance Categories

Solutions are categorized by average execution time:

### 🚀 Fast (< 10ms)
0 solution(s): 

### ⚡ Medium (10-100ms)
7 solution(s): Day 1, Day 3, Day 4, Day 5, Day 6, Day 7, Day 11

### 🐌 Slow (> 100ms)
5 solution(s): Day 2, Day 8, Day 9, Day 10, Day 12


## Key Observations

1. **Fastest Solution**: Day 6 completes in an average of 69.0ms, demonstrating optimal algorithmic efficiency.

2. **Slowest Solution**: Day 10 takes 696.0ms on average, likely due to complex computations or large input size.


## Methodology

The performance benchmark follows these steps:

1. **Build Phase**: All solutions are compiled using Bazel before testing begins
2. **JVM Warm-up**: Each solution runs 10 times to allow JIT compilation and optimization
3. **Measurement**: Each solution executes 100 times with high-precision timing (nanosecond accuracy)
4. **Statistics**: Min, max, average, median, 95th percentile, and standard deviation are calculated
5. **Verification**: Final run output is captured to verify correct answers

### Metrics Explained

- **Min**: Fastest execution time observed (best case)
- **Max**: Slowest execution time observed (worst case)
- **Average**: Mean execution time across all runs
- **Median**: Middle value when all times are sorted (less affected by outliers)
- **P95**: 95th percentile - 95% of runs completed within this time
- **StdDev**: Standard deviation indicating consistency of performance

## Hardware Note

Performance results are machine-dependent. These benchmarks were run on the development machine and serve as relative comparisons between solutions rather than absolute performance guarantees.

## Running the Benchmark

To reproduce these results:

```bash
# Run the performance test (takes 15-30 minutes)
./scripts/performance_test.sh

# Generate this documentation
python3 scripts/generate_performance_doc.py
```

---

*Last updated: 1765546056.8441277*
