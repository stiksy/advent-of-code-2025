# 🎄 Advent of Code 2025

My solutions for [Advent of Code 2025](https://adventofcode.com/2025) implemented in Java 21 with Bazel.

[![Build status](https://badge.buildkite.com/2f9c44cb4ac88cebbae4916853e755ee088a81e0c1c9adce7d.svg)](https://buildkite.com/bobcat/advent-of-code-2025)

## 🌟 Progress

| Day | Challenge | Part 1 | Part 2 | Source | Solution |
|-----|-----------|--------|--------|--------|----------|
| 1   | [Secret Entrance](https://adventofcode.com/2025/day/1) | 1145 | 6561 | [Day01.java](src/main/com/stiksy/aoc2025/day01/Day01.java) | [Explanation](docs/day01.md) |
| 2   | [Gift Shop](https://adventofcode.com/2025/day/2) | 54234399924 | 70187097315 | [Day02.java](src/main/com/stiksy/aoc2025/day02/Day02.java) | [Explanation](docs/day02.md) |
| 3   | [Lobby](https://adventofcode.com/2025/day/3) | 17330 | 171518260283767 | [Day03.java](src/main/com/stiksy/aoc2025/day03/Day03.java) | [Explanation](docs/day03.md) |
| 4   | [Printing Department](https://adventofcode.com/2025/day/4) | 1553 | 8442 | [Day04.java](src/main/com/stiksy/aoc2025/day04/Day04.java) | [Explanation](docs/day04.md) |
| 5   | [Cafeteria](https://adventofcode.com/2025/day/5) | 694 | 352716206375547 | [Day05.java](src/main/com/stiksy/aoc2025/day05/Day05.java) | [Explanation](docs/day05.md) |
| 6   | [Trash Compactor](https://adventofcode.com/2025/day/6) | 5552221122013 | 11371597126232 | [Day06.java](src/main/com/stiksy/aoc2025/day06/Day06.java) | [Explanation](docs/day06.md) |
| 7   | [Laboratories](https://adventofcode.com/2025/day/7) | 1570 | 15118009521693 | [Day07.java](src/main/com/stiksy/aoc2025/day07/Day07.java) | [Explanation](docs/day07.md) |
| 8   | [Playground](https://adventofcode.com/2025/day/8) | 96672 | 22517595 | [Day08.java](src/main/com/stiksy/aoc2025/day08/Day08.java) | [Explanation](docs/day08.md) |
| 9   | [Movie Theater](https://adventofcode.com/2025/day/9) | 4781546175 | 1573359081 | [Day09.java](src/main/com/stiksy/aoc2025/day09/Day09.java) | [Explanation](docs/day09.md) |
| 10  | [Factory](https://adventofcode.com/2025/day/10) | 375 | 15377 | [Day10.java](src/main/com/stiksy/aoc2025/day10/Day10.java) | [Explanation](docs/day10.md) |
| 11  | [Reactor](https://adventofcode.com/2025/day/11) | 674 | 438314708837664 | [Day11.java](src/main/com/stiksy/aoc2025/day11/Day11.java) | [Explanation](docs/day11.md) |
| 12  | [Christmas Tree Farm](https://adventofcode.com/2025/day/12) | 577 | N/A | [Day12.java](src/main/com/stiksy/aoc2025/day12/Day12.java) | [Explanation](docs/day12.md) |

⭐ **23 puzzles solved!** Current progress: **23/23 ⭐** (Complete!)

## 🚀 Quick Start

### Prerequisites

- Java 21
- [Bazel](https://bazel.build/) (version specified in `.bazelversion`)

### Running Solutions

```bash
# Run a specific day
bazel run //src/main/com/stiksy/aoc2025/day01:Day01

# Run all tests
bazel test //src/test/...

# Run tests with verbose output
bazel test //src/test/... --test_output=all
```

## 🏗️ Project Structure

```
advent-of-code-2025/
├── .buildkite/
│   └── pipeline.yml          # CI/CD pipeline configuration
├── scripts/
│   ├── create_day_annotation.sh
│   └── test_summary.sh
├── src/
│   ├── main/
│   │   ├── com/stiksy/aoc2025/
│   │   │   ├── util/         # Shared utilities (InputReader, etc.)
│   │   │   └── dayXX/        # Daily solutions
│   │   └── resources/
│   │       └── dayXX/        # Puzzle inputs
│   └── test/
│       ├── com/stiksy/aoc2025/
│       │   ├── util/         # Utility tests
│       │   └── dayXX/        # Daily solution tests
│       └── resources/
│           └── util/         # Test resources
├── CLAUDE.md                  # Development guide for Claude Code
└── README.md
```

## 🧪 Testing

This project follows test-driven development:

- **Example Tests**: Validate solutions against examples from puzzle descriptions
- **Regression Tests**: Ensure solutions continue to work with confirmed answers
- **Utility Tests**: Comprehensive coverage of shared utilities

All code must have tests before being committed.

## 📦 Utilities

### InputReader

A utility class for reading puzzle inputs with multiple helper methods:

- `readLines(filename)` - Read all lines
- `readFullFile(filename)` - Read entire file as string
- `readIntegers(filename)` - Parse integers
- `readLongs(filename)` - Parse long numbers
- `readNonEmptyLines(filename)` - Filter empty lines

## 🔧 CI/CD

The Buildkite pipeline automatically:

1. ✅ Runs all tests (unit, regression, utility)
2. 🏗️ Builds all solutions
3. 📊 Creates annotations with challenge links and results
4. 💾 Preserves test artifacts

Each day's solution gets a beautiful annotation showing:
- Link to the challenge
- Part 1 and Part 2 results
- Link to source code

## ⚡ Performance

Comprehensive performance benchmarks are available for all solutions. Each solution has been tested with 100 iterations to provide accurate statistics.

```bash
# Run performance benchmarks (~15-30 minutes)
./scripts/performance_test.sh

# Generate performance documentation
python3 scripts/generate_performance_doc.py
```

See [Performance Analysis](docs/performance.md) for detailed results and comparisons.

## 📝 Development

See [CLAUDE.md](CLAUDE.md) for detailed development guidelines, including:

- How to add new daily solutions
- Testing requirements
- Build system configuration
- CI/CD pipeline usage

## 🙏 Acknowledgments

- [Advent of Code](https://adventofcode.com/) by Eric Wastl
- Solutions developed with assistance from [Claude Code](https://claude.com/code)
