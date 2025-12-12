#!/usr/bin/env python3

"""
Generate performance documentation from benchmark results.
Reads performance_results.csv and creates a markdown document with statistics and visualizations.
"""

import csv
import sys
from pathlib import Path
from datetime import datetime

def format_time(ms):
    """Format milliseconds nicely."""
    if ms < 1:
        return f"{ms:.2f}ms"
    elif ms < 1000:
        return f"{ms}ms"
    else:
        return f"{ms/1000:.2f}s"

def create_bar_chart(value, max_value, width=40):
    """Create a simple ASCII bar chart."""
    if max_value == 0:
        return ""
    filled = int((value / max_value) * width)
    return "█" * filled + "░" * (width - filled)

def main():
    csv_file = Path("performance_results.csv")
    output_file = Path("docs/performance.md")

    if not csv_file.exists():
        print(f"Error: {csv_file} not found. Run ./scripts/performance_test.sh first.")
        sys.exit(1)

    # Read CSV data
    days = []
    with open(csv_file, 'r') as f:
        reader = csv.DictReader(f)
        for row in reader:
            days.append({
                'day': int(row['Day']),
                'min': float(row['Min(ms)']),
                'max': float(row['Max(ms)']),
                'avg': float(row['Avg(ms)']),
                'median': float(row['Median(ms)']),
                'p95': float(row['P95(ms)']),
                'stddev': float(row['StdDev(ms)']),
                'part1': row['Part1_Answer'],
                'part2': row['Part2_Answer']
            })

    if not days:
        print("Error: No data found in CSV file.")
        sys.exit(1)

    # Calculate overall statistics
    total_avg = sum(d['avg'] for d in days)
    max_avg = max(d['avg'] for d in days)
    min_avg = min(d['avg'] for d in days)
    fastest_day = min(days, key=lambda d: d['avg'])
    slowest_day = max(days, key=lambda d: d['avg'])

    # Generate markdown
    md_content = f"""---
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
| **Total Solutions** | {len(days)} days |
| **Combined Average Time** | {format_time(total_avg)} |
| **Fastest Solution** | Day {fastest_day['day']} ({format_time(fastest_day['avg'])}) |
| **Slowest Solution** | Day {slowest_day['day']} ({format_time(slowest_day['avg'])}) |
| **Overall Average** | {format_time(total_avg / len(days))} per solution |

## Detailed Results

### Performance by Day

| Day | Challenge | Avg | Min | Max | Median | P95 | StdDev |
|-----|-----------|-----|-----|-----|--------|-----|--------|
"""

    # Add table rows
    for d in days:
        challenge_names = {
            1: "Secret Entrance",
            2: "Gift Shop",
            3: "Lobby",
            4: "Printing Department",
            5: "Cafeteria",
            6: "Trash Compactor",
            7: "Laboratories",
            8: "Playground",
            9: "Movie Theater",
            10: "Factory",
            11: "Reactor",
            12: "Christmas Tree Farm"
        }
        name = challenge_names.get(d['day'], f"Day {d['day']}")
        md_content += f"| {d['day']:2d} | {name:20s} | {d['avg']:6.0f}ms | {d['min']:6.0f}ms | {d['max']:6.0f}ms | {d['median']:6.0f}ms | {d['p95']:6.0f}ms | {d['stddev']:6.2f}ms |\n"

    md_content += """
### Visual Performance Comparison

Average execution time per solution (relative scale):

```
"""

    # Add visual bars
    for d in days:
        bar = create_bar_chart(d['avg'], max_avg, width=50)
        md_content += f"Day {d['day']:2d}: {bar} {format_time(d['avg'])}\n"

    md_content += "```"

    md_content += """

## Performance Categories

Solutions are categorized by average execution time:

"""

    # Categorize solutions
    fast = [d for d in days if d['avg'] < 10]
    medium = [d for d in days if 10 <= d['avg'] < 100]
    slow = [d for d in days if d['avg'] >= 100]

    md_content += f"""### 🚀 Fast (< 10ms)
{len(fast)} solution(s): """ + ", ".join(f"Day {d['day']}" for d in fast) + "\n\n"

    md_content += f"""### ⚡ Medium (10-100ms)
{len(medium)} solution(s): """ + ", ".join(f"Day {d['day']}" for d in medium) + "\n\n"

    md_content += f"""### 🐌 Slow (> 100ms)
{len(slow)} solution(s): """ + ", ".join(f"Day {d['day']}" for d in slow) + "\n\n"

    md_content += """
## Key Observations

"""

    # Add automatic observations
    md_content += f"1. **Fastest Solution**: Day {fastest_day['day']} completes in an average of {format_time(fastest_day['avg'])}, demonstrating optimal algorithmic efficiency.\n\n"
    md_content += f"2. **Slowest Solution**: Day {slowest_day['day']} takes {format_time(slowest_day['avg'])} on average, likely due to complex computations or large input size.\n\n"

    # Check for high variability
    high_var = [d for d in days if d['stddev'] / d['avg'] > 0.1]
    if high_var:
        md_content += f"3. **High Variability**: Days {', '.join(str(d['day']) for d in high_var)} show high standard deviation (>10% of average), indicating non-deterministic performance or JVM optimization effects.\n\n"

    md_content += f"""
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

*Last updated: {datetime.fromtimestamp(Path('performance_results.csv').stat().st_mtime).strftime('%Y-%m-%d %H:%M:%S')}*
"""

    # Write the markdown file
    with open(output_file, 'w') as f:
        f.write(md_content)

    print(f"✓ Performance documentation generated: {output_file}")
    print(f"  Analyzed {len(days)} solutions")
    print(f"  Fastest: Day {fastest_day['day']} ({format_time(fastest_day['avg'])})")
    print(f"  Slowest: Day {slowest_day['day']} ({format_time(slowest_day['avg'])})")
    print(f"  Combined average: {format_time(total_avg)}")

if __name__ == '__main__':
    main()
