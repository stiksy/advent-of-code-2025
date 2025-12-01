#!/bin/bash

set -euo pipefail

# This script generates Buildkite pipeline steps for all completed Advent of Code days
# It scans the src/main/com/stiksy/aoc2025/ directory for dayXX folders

echo "steps:"

# Find all day directories and generate steps for each
for day_dir in src/main/com/stiksy/aoc2025/day*/; do
    if [ -d "$day_dir" ]; then
        # Extract day number from directory name (e.g., day01 -> 1, day02 -> 2)
        day_name=$(basename "$day_dir")
        day_num=$(echo "$day_name" | sed 's/day0*//')

        # Check if the Day java file exists
        if [ -f "${day_dir}Day${day_num}.java" ] || [ -f "${day_dir}Day$(printf "%02d" ${day_num}).java" ]; then
            day_padded=$(printf "%02d" ${day_num})

            cat <<EOF
  - label: ":christmas_tree: Day ${day_num} Results"
    key: "day${day_padded}-results"
    command: |
      echo "--- :runner: Running Day ${day_num} solution"
      bazel run //src/main/com/stiksy/aoc2025/day${day_padded}:Day${day_padded} 2>&1 | grep "Part" > day${day_padded}_output.txt || true
      cat day${day_padded}_output.txt

      echo "--- :memo: Creating annotation"
      bash scripts/create_day_annotation.sh ${day_num} day${day_padded}_output.txt
    artifact_paths:
      - "day${day_padded}_output.txt"
    agents:
      queue: "selfhosted"
      os: "linux"

EOF
        fi
    fi
done
