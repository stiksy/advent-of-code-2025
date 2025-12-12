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
   - **If the input needs extra processing that might be useful in the future, create a helper class in the util package with comprehensive tests**

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

7. **Create solution documentation** (MANDATORY):
   - After completing both parts, create `docs/dayXX.md` with comprehensive explanation
   - Include the original puzzle text from https://adventofcode.com/2025/day/XX
   - Provide detailed explanation of the solution approach for both parts
   - Include code snippets with explanations where helpful
   - Reference specific parts of the puzzle to aid comprehension
   - Documentation template structure:
     - Title with day number and puzzle name
     - Original puzzle text (Part 1 and Part 2)
     - Solution approach explanation for Part 1
     - Code walkthrough with snippets for Part 1
     - Solution approach explanation for Part 2
     - Code walkthrough with snippets for Part 2
     - Key insights and algorithm complexity notes
     - Final answers

8. **Update GitHub Pages configuration** (MANDATORY):
   - Add Jekyll front matter to `docs/dayXX.md`:
     ```yaml
     ---
     layout: default
     title: Day X - Puzzle Name
     nav_order: X
     ---
     ```
   - Update header links in `docs/dayXX.md`:
     - Change relative source code links to absolute GitHub URLs
     - Add "← Back to Home" link: `[← Back to Home](../index.html)`
   - Update `index.md` to add the new day to the solutions table:
     ```markdown
     | X | [Puzzle Name](https://adventofcode.com/2025/day/X) | ⭐ ANSWER1 | ⭐ ANSWER2 | [📖 Read Solution](docs/dayXX.html) |
     ```
   - Update progress counter in `index.md`: `Current progress: X/24 ⭐`
   - Update `README.md` with the same table row and progress counter
   - The GitHub Pages site will automatically rebuild when changes are pushed to main

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

- **Parallel Execution**: Tests and builds run in parallel for faster feedback
- **Automated Testing**: All tests run on every commit
- **Build Verification**: Ensures all solutions compile
- **Dynamic Day Discovery**: Automatically generates steps for all completed days
- **Result Annotations**: Each day gets a beautiful annotation showing:
  ```
  🎄 Day X: Advent of Code Challenge
  Results:
  - Part 1: `answer` ⭐
  - Part 2: `answer` ⭐
  ```
- **Artifacts**: Test logs and outputs are preserved
- **Grouped Results**: All daily results appear in a single group

### Adding New Days to Pipeline

**No manual pipeline updates needed!** The pipeline automatically discovers and runs all completed days.

When you complete a new day:
1. Create the solution in `src/main/com/stiksy/aoc2025/dayXX/`
2. Commit and push your code
3. The pipeline will automatically detect and run the new day

The `scripts/generate_day_steps.sh` script scans for all `dayXX` directories and generates pipeline steps dynamically. All day results are grouped together for easy viewing.

### Scripts

- **`scripts/generate_day_steps.sh`**: Automatically discovers completed days and generates pipeline steps
- **`scripts/create_day_annotation.sh`**: Generates Buildkite annotations with challenge links and results
- **`scripts/test_summary.sh`**: Creates test summary annotations

## Maven Dependencies

To add new Maven dependencies, update the `maven.install()` call in MODULE.bazel with the artifact coordinates, then reference them in BUILD files as `@maven//:group_artifact` (replacing dots and colons with underscores).

## GitHub Pages Documentation Site

This project uses GitHub Pages to host comprehensive solution documentation.

### Live Site

Documentation is available at: **https://stiksy.github.io/advent-of-code-2025/**

### Configuration Files

- **`_config.yml`**: Jekyll configuration with Cayman theme, SEO settings, and navigation
- **`index.md`**: Landing page with solutions table and navigation
- **`docs/dayXX.md`**: Individual solution documentation pages with Jekyll front matter

### Automatic Updates

The GitHub Pages site automatically rebuilds when changes are pushed to the `main` branch. Changes typically appear within 1-2 minutes.

### Setup Instructions

For initial setup or troubleshooting, see `GITHUB_PAGES.md` for complete instructions on:
- Enabling GitHub Pages in repository settings
- Changing themes
- Troubleshooting deployment issues
- Adding new documentation pages

## Interactive Visualizations

Each day's solution should include an interactive HTML visualization to demonstrate the algorithm in action.

### Visualization Standards

**IMPORTANT**: All visualizations must follow these standards for consistency:

1. **Styling**:
   - Use the shared CSS file: `<link rel="stylesheet" href="visualization-styles.css">`
   - The shared CSS provides: fonts, colors, button styles, layouts, animations
   - Minimal custom CSS allowed for puzzle-specific elements only

2. **Speed Control**:
   - Always include a speed slider with standardized labels
   - Range: 1 (slow) to 10 (fast), default: 5
   - Label: "Speed:" with no units (ms/s) shown
   - Use inverse mapping: lower number = slower animation

3. **Input Data**:
   - **Always use actual puzzle input** from `src/main/resources/dayXX/input.txt`
   - **No sample/example input fields** - visualizations should work with real data only
   - Embed the actual input directly in the JavaScript
   - No textarea or input fields for users to enter data

4. **Structure**:
   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <meta name="viewport" content="width=device-width, initial-scale=1.0">
       <title>Day X - Puzzle Name Visualization</title>
       <link rel="stylesheet" href="visualization-styles.css">
       <style>
           /* Only puzzle-specific custom styles here */
       </style>
   </head>
   <body>
       <div class="container">
           <h1>Day X: Puzzle Name</h1>
           <p class="subtitle">Algorithm Visualization</p>

           <!-- Part toggle if needed -->
           <div class="mode-toggle">
               <button class="active" onclick="setMode('part1')">Part 1</button>
               <button onclick="setMode('part2')">Part 2</button>
           </div>

           <!-- Main visualization area -->
           <div class="visualization-area">
               <!-- Puzzle-specific visualization -->
           </div>

           <!-- Statistics -->
           <div class="stats">
               <!-- Stat cards -->
           </div>

           <!-- Speed control (required) -->
           <div class="speed-control">
               <label>Speed:</label>
               <input type="range" id="speedSlider" min="1" max="10" value="5">
               <span id="speedLabel">●●●●●</span>
           </div>

           <!-- Controls -->
           <div class="controls">
               <button class="btn-primary" onclick="start()">▶ Start</button>
               <button class="btn-secondary" onclick="reset()">⟲ Reset</button>
           </div>
       </div>
       <script>
           // Actual puzzle input embedded here
           const PUZZLE_INPUT = "...actual input...";

           // Speed mapping (inverse: 1=slowest, 10=fastest)
           function getDelay() {
               const speed = parseInt(document.getElementById('speedSlider').value);
               return 1100 - (speed * 100); // 1000ms to 100ms
           }
       </script>
   </body>
   </html>
   ```

5. **File Organization**:
   - Visualizations: `docs/visualizations/dayXX-visualization.html`
   - Shared CSS: `docs/visualizations/visualization-styles.css`
   - Link from docs: Add to the documentation header navigation

6. **Required Elements**:
   - Title with day number and puzzle name
   - Subtitle describing what's being visualized
   - Mode toggle (if puzzle has Part 1 and Part 2)
   - Main visualization area (puzzle-specific)
   - Statistics display (optional but recommended)
   - Speed control (required, standardized format)
   - Control buttons (start/pause, reset minimum)

7. **Documentation Integration**:
   - Add visualization link to `docs/dayXX.md` header:
     ```markdown
     [🎨 Interactive Visualization](visualizations/dayXX-visualization.html)
     ```
   - Add description section after puzzle description explaining what the visualization shows

### Creating New Visualizations

When adding a visualization for a new day:

1. Read the actual puzzle input from `src/main/resources/dayXX/input.txt`
2. Create `docs/visualizations/dayXX-visualization.html` using the standard structure
3. Link the shared CSS file
4. Embed the actual puzzle input in the JavaScript
5. Implement puzzle-specific visualization logic
6. Add standardized speed control
7. Update `docs/dayXX.md` to link to the visualization
