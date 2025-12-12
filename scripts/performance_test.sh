#!/bin/bash

# Performance test script for Advent of Code 2025 solutions
# Runs each day 100 times and collects timing statistics

set -e

ITERATIONS=100
RESULTS_FILE="performance_results.txt"
CSV_FILE="performance_results.csv"

echo "=== Advent of Code 2025 Performance Test ===" | tee "$RESULTS_FILE"
echo "Running each solution $ITERATIONS times..." | tee -a "$RESULTS_FILE"
echo "Started at: $(date)" | tee -a "$RESULTS_FILE"
echo "" | tee -a "$RESULTS_FILE"

# Build all targets first
echo "Building all solutions..." | tee -a "$RESULTS_FILE"
bazel build //src/main/com/stiksy/aoc2025/... 2>&1 | grep -E "(INFO|Target)" | tail -5 | tee -a "$RESULTS_FILE"
echo "" | tee -a "$RESULTS_FILE"

# CSV header
echo "Day,Min(ms),Max(ms),Avg(ms),Median(ms),P95(ms),StdDev(ms),Part1_Answer,Part2_Answer" > "$CSV_FILE"

# Function to calculate percentile
calculate_percentile() {
    local -n arr=$1
    local percentile=$2
    local sorted=($(printf '%s\n' "${arr[@]}" | sort -n))
    local index=$(echo "scale=0; (${#sorted[@]} * $percentile / 100)" | bc)
    echo "${sorted[$index]}"
}

# Function to run performance test for a day
test_day() {
    local day=$1
    local day_padded=$(printf "%02d" $day)

    echo "Testing Day $day..." | tee -a "$RESULTS_FILE"

    # Check if the JAR exists
    local jar_path="bazel-bin/src/main/com/stiksy/aoc2025/day$day_padded/Day$day_padded.jar"
    if [ ! -f "$jar_path" ]; then
        echo "  Day $day JAR not found, skipping." | tee -a "$RESULTS_FILE"
        return
    fi

    # Get the deploy JAR path for standalone execution
    local deploy_jar="bazel-bin/src/main/com/stiksy/aoc2025/day$day_padded/Day$day_padded"

    # Array to store execution times
    times=()
    output=""

    # Warm-up runs (JVM optimization)
    echo "  Warming up JVM..." | tee -a "$RESULTS_FILE"
    for i in {1..10}; do
        $deploy_jar > /dev/null 2>&1 || java -jar "$jar_path" > /dev/null 2>&1
    done

    echo "  Running $ITERATIONS iterations..." | tee -a "$RESULTS_FILE"

    # Run iterations
    for i in $(seq 1 $ITERATIONS); do
        # Show progress every 100 iterations
        if [ $((i % 10)) -eq 0 ]; then
            echo -ne "  Progress: $i/$ITERATIONS\r"
        fi

        # Time the execution (in milliseconds)
        # Use /usr/bin/time for accurate measurements
        start=$(date +%s%N)
        if [ -x "$deploy_jar" ]; then
            output=$($deploy_jar 2>/dev/null)
        else
            output=$(java -jar "$jar_path" 2>/dev/null)
        fi
        end=$(date +%s%N)

        # Calculate elapsed time in milliseconds
        elapsed=$(( (end - start) / 1000000 ))
        times+=($elapsed)
    done

    echo -ne "\n"

    # Extract answers from last output
    part1=$(echo "$output" | grep "Part 1:" | awk '{print $3}')
    part2=$(echo "$output" | grep "Part 2:" | awk '{print $3}')

    # Sort times for statistics
    IFS=$'\n' sorted_times=($(sort -n <<<"${times[*]}"))
    unset IFS

    # Calculate statistics
    min=${sorted_times[0]}
    max=${sorted_times[${#sorted_times[@]}-1]}
    sum=0

    for time in "${times[@]}"; do
        sum=$((sum + time))
    done

    avg=$((sum / ITERATIONS))

    # Calculate median
    mid=$((ITERATIONS / 2))
    if [ $((ITERATIONS % 2)) -eq 0 ]; then
        median=$(( (sorted_times[mid-1] + sorted_times[mid]) / 2 ))
    else
        median=${sorted_times[mid]}
    fi

    # Calculate 95th percentile
    p95_idx=$(( ITERATIONS * 95 / 100 ))
    p95=${sorted_times[$p95_idx]}

    # Calculate standard deviation
    variance_sum=0
    for time in "${times[@]}"; do
        diff=$((time - avg))
        variance_sum=$((variance_sum + diff * diff))
    done
    variance=$((variance_sum / ITERATIONS))
    stddev=$(echo "scale=2; sqrt($variance)" | bc)

    # Output results
    echo "  Results for Day $day:" | tee -a "$RESULTS_FILE"
    echo "    Min:     ${min}ms" | tee -a "$RESULTS_FILE"
    echo "    Max:     ${max}ms" | tee -a "$RESULTS_FILE"
    echo "    Average: ${avg}ms" | tee -a "$RESULTS_FILE"
    echo "    Median:  ${median}ms" | tee -a "$RESULTS_FILE"
    echo "    P95:     ${p95}ms" | tee -a "$RESULTS_FILE"
    echo "    StdDev:  ${stddev}ms" | tee -a "$RESULTS_FILE"
    echo "    Part 1:  $part1" | tee -a "$RESULTS_FILE"
    echo "    Part 2:  $part2" | tee -a "$RESULTS_FILE"
    echo "" | tee -a "$RESULTS_FILE"

    # Save to CSV
    echo "$day,$min,$max,$avg,$median,$p95,$stddev,$part1,$part2" >> "$CSV_FILE"
}

# Test all days
for day in {1..12}; do
    test_day $day
done

echo "=== Performance Test Complete ===" | tee -a "$RESULTS_FILE"
echo "Finished at: $(date)" | tee -a "$RESULTS_FILE"
echo "" | tee -a "$RESULTS_FILE"
echo "Results saved to:" | tee -a "$RESULTS_FILE"
echo "  - $RESULTS_FILE (detailed text)" | tee -a "$RESULTS_FILE"
echo "  - $CSV_FILE (CSV format)" | tee -a "$RESULTS_FILE"
echo "" | tee -a "$RESULTS_FILE"
echo "To generate performance documentation:" | tee -a "$RESULTS_FILE"
echo "  python3 scripts/generate_performance_doc.py" | tee -a "$RESULTS_FILE"
