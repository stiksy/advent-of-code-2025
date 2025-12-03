# 🎄 Advent of Code 2025

My solutions for [Advent of Code 2025](https://adventofcode.com/2025) implemented in Java 21 with Bazel.

[![Build status](https://badge.buildkite.com/2f9c44cb4ac88cebbae4916853e755ee088a81e0c1c9adce7d.svg)](https://buildkite.com/bobcat/advent-of-code-2025)

## 🌟 Progress

| Day | Challenge | Part 1 | Part 2 | Source | Solution |
|-----|-----------|--------|--------|--------|----------|
| 1   | [Secret Entrance](https://adventofcode.com/2025/day/1) | ⭐ 1145 | ⭐ 6561 | [Day01.java](src/main/com/stiksy/aoc2025/day01/Day01.java) | [Explanation](docs/day01.md) |
| 2   | [Gift Shop](https://adventofcode.com/2025/day/2) | ⭐ 18467608014 | ⭐ 20043697201 | [Day02.java](src/main/com/stiksy/aoc2025/day02/Day02.java) | [Explanation](docs/day02.md) |
| 3   | [Lobby](https://adventofcode.com/2025/day/3) | ⭐ 17330 | ⭐ 171518260283767 | [Day03.java](src/main/com/stiksy/aoc2025/day03/Day03.java) | [Explanation](docs/day03.md) |

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

## 📝 Development

See [CLAUDE.md](CLAUDE.md) for detailed development guidelines, including:

- How to add new daily solutions
- Testing requirements
- Build system configuration
- CI/CD pipeline usage

## 🙏 Acknowledgments

- [Advent of Code](https://adventofcode.com/) by Eric Wastl
- Solutions developed with assistance from [Claude Code](https://claude.com/code)

---

⭐ **12 puzzles to solve!** Current progress: 6/24 ⭐
