#!/bin/bash

set -euo pipefail

DAY=$1
OUTPUT_FILE=$2

# Extract part results from output
PART1=$(grep "Part 1:" "$OUTPUT_FILE" | awk '{print $3}')
PART2=$(grep "Part 2:" "$OUTPUT_FILE" | awk '{print $3}')

# Create annotation with challenge link and results
buildkite-agent annotate --style "success" --context "day-${DAY}" <<EOF
### :christmas_tree: [Day ${DAY}: Advent of Code Challenge](https://adventofcode.com/2025/day/${DAY})

**Results:**
- **Part 1:** \`${PART1}\` ⭐
- **Part 2:** \`${PART2}\` ⭐

---
[View Challenge](https://adventofcode.com/2025/day/${DAY}) | [View Source](https://github.com/stiksy/advent-of-code-2025/tree/main/src/main/com/stiksy/aoc2025/day$(printf "%02d" ${DAY}))
EOF

echo "✅ Created annotation for Day ${DAY}"
echo "   Part 1: ${PART1}"
echo "   Part 2: ${PART2}"
