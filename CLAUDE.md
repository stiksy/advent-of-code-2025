# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Advent of Code 2025 solutions implemented in Java 21 using Bazel as the build system.

Advent of Code is a daily programming challenge event running December 1-25, with two-part puzzles released each day. This repository is structured to support adding solutions for each day independently.

## Build System

This project uses **Bazel** with the Bzlmod module system (MODULE.bazel). Key configurations:
- Java 21 language version and runtime (configured in .bazelrc)
- rules_java (v8.10.0) for Java compilation
- rules_jvm_external (v6.7) for Maven dependency management
- JUnit 4.13.2 for testing

## Common Commands

### Daily Solutions
```bash
# Run a specific day's solution (e.g., Day 1)
bazel run //src/main/com/stiksy/aoc2025/day01:Day01

# Test a specific day's solution
bazel test //src/test/com/stiksy/aoc2025/day01:Day01Test

# Test with verbose output to see print statements
bazel test //src/test/com/stiksy/aoc2025/day01:Day01Test --test_output=all

# Run all tests for all days
bazel test //src/test/com/stiksy/aoc2025/...
```

### Building and Cleaning
```bash
# Build a specific day
bazel build //src/main/com/stiksy/aoc2025/day01:Day01

# Clean build artifacts
bazel clean

# Deep clean (removes all cached data)
bazel clean --expunge
```

## Code Organization

```
src/
├── main/
│   ├── com/stiksy/aoc2025/
│   │   ├── util/            # Shared utility classes
│   │   │   ├── InputReader.java  # Input reading utilities
│   │   │   └── BUILD.bazel
│   │   └── dayXX/           # Solution for each day (day01, day02, etc.)
│   │       ├── DayXX.java   # Main solution class
│   │       └── BUILD.bazel  # Bazel build config
│   └── resources/
│       └── dayXX/
│           ├── input.txt    # Actual puzzle input from adventofcode.com
│           └── BUILD.bazel  # Resource filegroup
└── test/
    └── com/stiksy/aoc2025/
        └── dayXX/
            ├── DayXXTest.java  # Tests using example inputs
            └── BUILD.bazel      # Test build config
```

### Utility Classes

**InputReader** (`src/main/com/stiksy/aoc2025/util/InputReader.java`):
- `readLines(String filename)` - Read all lines from a resource file
- `readFullFile(String filename)` - Read entire file as a single string
- `readIntegers(String filename)` - Parse file as list of integers
- `readLongs(String filename)` - Parse file as list of longs
- `readNonEmptyLines(String filename)` - Read lines, filtering out empty ones

Always use `InputReader` instead of duplicating input reading logic in each day's solution.

**IMPORTANT**: All utility classes MUST have comprehensive unit tests. See `src/test/com/stiksy/aoc2025/util/InputReaderTest.java` for an example. When adding new utility methods, add corresponding tests immediately.

### Solution Structure

Each day follows this pattern:

**Main Solution Class** (`src/main/com/stiksy/aoc2025/dayXX/DayXX.java`):
- `solvePart1(List<String> input)` - Part 1 solution logic
- `solvePart2(List<String> input)` - Part 2 solution logic
- `main()` - Entry point that uses `InputReader.readLines()` to read input and prints both solutions

**Test Class** (`src/test/com/stiksy/aoc2025/dayXX/DayXXTest.java`):
- **Example tests**: Tests using the example inputs provided in the puzzle description
- **Regression tests**: Tests using actual puzzle input with confirmed correct answers
- Validates against the expected results from the examples
- Helps verify solution correctness before running on actual input
- Prevents future changes (e.g., to utility classes) from breaking previous solutions

**Input File** (`src/main/resources/dayXX/input.txt`):
- Contains the actual puzzle input from adventofcode.com
- Each user receives unique input, so this file is user-specific

### BUILD File Pattern

Each package requires BUILD.bazel files:
- **Day solution**: `java_binary` rule with main_class, resources dependency, and util dependency
  ```python
  deps = ["//src/main/com/stiksy/aoc2025/util:util"]
  resources = ["//src/main/resources/dayXX:input"]
  ```
- **Resources**: `filegroup` rule to make input.txt available
- **Tests**: `java_test` rule with deps on the day's solution, util, and JUnit, plus data for input file
  ```python
  deps = [
      "//src/main/com/stiksy/aoc2025/dayXX:DayXX",
      "//src/main/com/stiksy/aoc2025/util:util",
      "@maven//:junit_junit",
  ]
  data = ["//src/main/resources/dayXX:input"]
  ```

## Adding New Solutions

When starting a new day's puzzle:

1. **Copy the Day01 template structure**:
   ```bash
   # For day 02, for example:
   cp -r src/main/com/stiksy/aoc2025/day01 src/main/com/stiksy/aoc2025/day02
   cp -r src/main/resources/day01 src/main/resources/day02
   cp -r src/test/com/stiksy/aoc2025/day01 src/test/com/stiksy/aoc2025/day02
   ```

2. **Rename classes and update BUILD files**:
   - Rename `Day01.java` to `DayXX.java` and update class name
   - Update package declarations to `com.stiksy.aoc2025.dayXX`
   - Update BUILD.bazel files to reference correct day and class names
   - Update resource path in InputReader call (e.g., `InputReader.readLines("day02/input.txt")`)

3. **Add puzzle input**:
   - Download your puzzle input from adventofcode.com
   - Save it to `src/main/resources/dayXX/input.txt`

4. **Write tests with example data**:
   - Copy example input from the puzzle description
   - Add it to test methods in `DayXXTest.java`
   - Set expected results from the example
   - Run tests to verify solution against examples

5. **Implement solution**:
   - Start with Part 1, implement `solvePart1()` method
   - Verify with example tests
   - Run on actual input to get Part 1 answer
   - Implement Part 2, add example tests, and solve

6. **Add regression tests** (IMPORTANT):
   - Once you have confirmed correct answers for both parts, add regression tests:
   ```java
   @Test
   public void testPart1Actual() {
       DayXX solution = new DayXX();
       List<String> input = InputReader.readLines("dayXX/input.txt");
       long result = solution.solvePart1(input);
       assertEquals(CONFIRMED_ANSWER, result);
   }

   @Test
   public void testPart2Actual() {
       DayXX solution = new DayXX();
       List<String> input = InputReader.readLines("dayXX/input.txt");
       long result = solution.solvePart2(input);
       assertEquals(CONFIRMED_ANSWER, result);
   }
   ```
   - These tests prevent future refactoring or utility changes from breaking working solutions
   - Run `bazel test //src/test/com/stiksy/aoc2025/dayXX:DayXXTest` to verify all tests pass

## Development Workflow

**Every solution must be properly documented with unit tests using the examples provided in the challenge description.** This ensures correctness before running on the actual puzzle input.

1. Read the puzzle description carefully
2. Extract example inputs and expected outputs
3. Write failing tests using the examples
4. Implement the solution to pass the tests
5. Run on actual input to get the answer
6. **Add regression tests with the confirmed correct answers** (this protects against future breaking changes)

## Testing Requirements

### Mandatory Testing
All code must have comprehensive unit tests:

1. **Daily Solutions**:
   - Example tests (from puzzle description)
   - Regression tests (with confirmed answers)

2. **Utility Classes**:
   - Unit tests for all public methods
   - Edge case testing (empty files, missing files, etc.)
   - Test resources in `src/test/resources/`

3. **Running Tests**:
   ```bash
   # Run all tests
   bazel test //src/test/...

   # Run specific test suite
   bazel test //src/test/com/stiksy/aoc2025/util:InputReaderTest

   # Run with verbose output
   bazel test //src/test/... --test_output=all
   ```

**Before committing any changes to utility classes, always run the full test suite** to ensure no existing solutions are broken.

## CI/CD Pipeline

### Buildkite Pipeline

The repository includes a comprehensive Buildkite pipeline (`.buildkite/pipeline.yml`) that:

1. **Setup & Cache**: Verifies Bazel installation and fetches dependencies
2. **Run All Tests**: Executes unit tests, regression tests, and utility tests
3. **Build All Solutions**: Compiles all day solutions
4. **Day Results**: Runs each day's solution and creates annotations with:
   - Link to the challenge on adventofcode.com
   - Part 1 and Part 2 results
   - Link to source code

### Pipeline Features

- **Automated Testing**: All tests run on every commit
- **Build Verification**: Ensures all solutions compile
- **Result Annotations**: Each day gets a beautiful annotation showing:
  ```
  🎄 Day X: Advent of Code Challenge
  Results:
  - Part 1: `answer` ⭐
  - Part 2: `answer` ⭐
  ```
- **Artifacts**: Test logs and outputs are preserved

### Adding New Days to Pipeline

When you complete a new day, add it to the pipeline by uncommenting and updating the template in `.buildkite/pipeline.yml`:

```yaml
- label: ":christmas_tree: Day X Results"
  key: "dayXX-results"
  command: |
    echo "--- :runner: Running Day X solution"
    bazel run //src/main/com/stiksy/aoc2025/dayXX:DayXX 2>&1 | grep "Part" > dayXX_output.txt || true
    cat dayXX_output.txt

    echo "--- :memo: Creating annotation"
    bash scripts/create_day_annotation.sh X dayXX_output.txt
  artifact_paths:
    - "dayXX_output.txt"
  agents:
    queue: "selfhosted"
    os: "linux"
```

### Scripts

- **`scripts/create_day_annotation.sh`**: Generates Buildkite annotations with challenge links and results
- **`scripts/test_summary.sh`**: Creates test summary annotations

## Maven Dependencies

To add new Maven dependencies, update the `maven.install()` call in MODULE.bazel with the artifact coordinates, then reference them in BUILD files as `@maven//:group_artifact` (replacing dots and colons with underscores).
