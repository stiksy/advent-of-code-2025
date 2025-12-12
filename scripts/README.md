# Performance Testing Scripts

This directory contains scripts for benchmarking Advent of Code 2025 solutions.

## Scripts

### `performance_test.sh`

Runs comprehensive performance benchmarks for all solutions.

**What it does:**
- Builds all solutions using Bazel
- Runs each solution 1,000 times
- Performs JVM warm-up (10 iterations) before measurement
- Collects timing statistics (min, max, avg, median, P95, stddev)
- Saves results to CSV and text files

**Usage:**
```bash
./scripts/performance_test.sh
```

**Duration:** ~15-30 minutes depending on hardware

**Output files:**
- `performance_results.txt` - Detailed text report
- `performance_results.csv` - CSV data for analysis

### `generate_performance_doc.py`

Generates a formatted markdown document from benchmark results.

**What it does:**
- Reads `performance_results.csv`
- Creates performance comparison tables
- Generates ASCII bar charts
- Categorizes solutions by speed
- Produces automated observations

**Usage:**
```bash
python3 scripts/generate_performance_doc.py
```

**Output file:**
- `docs/performance.md` - Performance documentation page

## Complete Workflow

Run the complete performance analysis:

```bash
# Step 1: Run benchmarks
./scripts/performance_test.sh

# Step 2: Generate documentation
python3 scripts/generate_performance_doc.py

# Step 3: View results
cat docs/performance.md
```

## Performance Metrics

The benchmarks track these statistics:

- **Min**: Best execution time (fastest run)
- **Max**: Worst execution time (slowest run)
- **Average**: Mean across all iterations
- **Median**: Middle value (50th percentile)
- **P95**: 95th percentile (95% of runs faster than this)
- **StdDev**: Standard deviation (consistency indicator)

## Notes

- Results include JVM startup time per execution
- Each iteration is independent (new JVM process)
- 10 warm-up iterations allow JIT optimization
- Pure execution time is measured (no compilation overhead)
- Results are hardware-dependent

## Requirements

- Bazel build system
- Python 3.x
- Bash shell
- `bc` calculator utility
