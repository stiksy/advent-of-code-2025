---
layout: default
title: Day 10 - Factory
nav_order: 10
---

[← Back to Home](../index.html)

# Day 10: Factory

[View Solution on GitHub](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day10/Day10.java) | [View Puzzle](https://adventofcode.com/2025/day/10)

## Problem Description

### Part 1

Just across the hall, you find a large factory. Fortunately, the Elves here have plenty of time to decorate. Unfortunately, it's because the factory machines are all offline, and none of the Elves can figure out the initialization procedure.

The Elves do have the manual for the machines, but the section detailing the initialization procedure was eaten by a Shiba Inu. All that remains of the manual are some indicator light diagrams, button wiring schematics, and joltage requirements for each machine.

For example:

```
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
```

The manual describes one machine per line. Each line contains:
- A single **indicator light diagram** in `[square brackets]`, where `.` means off and `#` means on
- One or more **button wiring schematics** in `(parentheses)`, listing which lights each button toggles
- **Joltage requirements** in `{curly braces}` (can be ignored)

To start a machine:
1. Its indicator lights are all initially off
2. You need to configure them to match the target state in the diagram
3. You can push any button any number of times (including zero)
4. Each button press toggles the state of its listed lights (on→off or off→on)

**Goal:** Determine the fewest total button presses required to correctly configure all machines in your list.

#### Example Walkthrough

For the first machine `[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}`:
- Target: lights at positions 1 and 2 should be on (positions 0 and 3 should be off)
- The fewest button presses required is **2**
- One way: press buttons `(0,2)` and `(0,1)` once each

For the second machine: **3 button presses** minimum
For the third machine: **2 button presses** minimum

**Total:** 2 + 3 + 2 = **7 button presses**

## Implementation Details (Part 1)

#### Machine Parsing

```java
public static Machine parseMachine(String line) {
    // Extract target state from [square brackets]
    String targetStr = line.substring(startBracket + 1, endBracket);
    boolean[] target = new boolean[targetStr.length()];
    for (int i = 0; i < targetStr.length(); i++) {
        target[i] = targetStr.charAt(i) == '#';
    }

    // Extract button definitions from (parentheses)
    List<Set<Integer>> buttons = new ArrayList<>();
    // Parse each (button) group...
}
```

#### Gaussian Elimination

The core algorithm uses row operations over GF(2):

```java
// Find pivot and swap rows
for (int col = 0; col < numButtons && currentRow < numLights; col++) {
    // Find pivot row
    int pivotRow = -1;
    for (int row = currentRow; row < numLights; row++) {
        if (matrix[row][col]) {
            pivotRow = row;
            break;
        }
    }

    if (pivotRow == -1) continue; // This column is free

    // Swap rows if needed
    // Eliminate: XOR pivot row with all other rows where column is 1
    for (int row = 0; row < numLights; row++) {
        if (row != currentRow && matrix[row][col]) {
            for (int c = 0; c <= numButtons; c++) {
                matrix[row][c] ^= matrix[currentRow][c]; // XOR operation
            }
        }
    }
}
```

#### Finding Minimum Solution

After Gaussian elimination, we try all combinations of free variables:

```java
int minPresses = Integer.MAX_VALUE;
for (int mask = 0; mask < (1 << numFreeVars); mask++) {
    boolean[] solution = new boolean[numButtons];

    // Set free variables according to mask
    for (int i = 0; i < numFreeVars; i++) {
        solution[freeVars.get(i)] = ((mask >> i) & 1) == 1;
    }

    // Back substitute to find pivot variables
    for (int row = currentRow - 1; row >= 0; row--) {
        boolean val = matrix[row][numButtons];
        for (int col = pivotCol[row] + 1; col < numButtons; col++) {
            if (matrix[row][col]) {
                val ^= solution[col];
            }
        }
        solution[pivotCol[row]] = val;
    }

    // Count button presses
    int count = 0;
    for (boolean pressed : solution) {
        if (pressed) count++;
    }
    minPresses = Math.min(minPresses, count);
}
```

### Complexity Analysis

For a machine with `n` lights and `m` buttons:

- **Matrix construction**: O(n × m)
- **Gaussian elimination**: O(n × m²)
- **Free variable enumeration**: O(2^k × m) where k is the number of free variables
- **Overall per machine**: O(n × m² + 2^k × m)

In practice, k (free variables) is typically small, making this very efficient.

### Why This Works

The problem has these mathematical properties:

1. **Linearity**: Button presses combine linearly (with XOR)
2. **Commutativity**: Order of button presses doesn't matter
3. **Involution**: Each button press is its own inverse (pressing twice = no effect)
4. **Binary**: We only care about odd/even number of presses

These properties make it a perfect fit for linear algebra over GF(2).

### Part 2: Joltage Configuration

All machines are now starting to come online! But they need to be configured to exact joltage levels to function properly.

Each machine has numeric counters (one per joltage requirement) that start at 0. The buttons now operate in **joltage configuration mode**: each button press increases the listed counters by 1 (instead of toggling lights).

#### Example:
- Joltage requirements: `{3,5,4,7}` means 4 counters need to reach values 3, 5, 4, and 7
- Button `(1,3)` increases counters 1 and 3 by 1 each time pressed
- Goal: Find minimum total button presses to reach all target values

Using the same three example machines:
- Machine 1: **10 button presses** minimum
- Machine 2: **12 button presses** minimum
- Machine 3: **11 button presses** minimum
- **Total: 33 button presses**

## Solution Approach

### Part 1: Binary States (GF(2) Linear Algebra)

### Key Insight: Linear Algebra over GF(2)

This problem is a system of linear equations over **GF(2)** (the Galois field with two elements: 0 and 1). Here's why:

1. Each button press toggles specific lights
2. Pressing a button twice is the same as not pressing it (toggle twice = no change)
3. Therefore, we only care about pressing each button an **odd** (1) or **even** (0) number of times
4. This is equivalent to XOR operations (binary addition modulo 2)

### Algorithm: Gaussian Elimination over GF(2)

For each machine, we:

1. **Build a matrix** where:
   - Each row represents an indicator light
   - Each column represents a button
   - `matrix[i][j] = 1` if button j toggles light i
   - An augmented column contains the target state

2. **Perform Gaussian elimination** with:
   - Addition is XOR (binary addition mod 2)
   - Multiplication is AND (binary multiplication)
   - No division needed (we're in GF(2))

3. **Handle free variables**:
   - After row reduction, some variables may be "free" (not determined by pivot columns)
   - Try all 2^k combinations of free variables to find the solution with minimum button presses

4. **Back substitute** to find which buttons to press

### Part 2: Integer Counters (Integer Linear Programming)

For Part 2, the problem changes fundamentally:

1. **Integer arithmetic**: Counters are integers, not binary states
2. **Additive operations**: Buttons add 1, not toggle
3. **Multiple presses**: Each button can be pressed many times

#### Algorithm: Gaussian Elimination + Priority Queue Search

1. **Build integer matrix** similar to Part 1, but with regular arithmetic
2. **Perform Gaussian elimination** over real numbers to find the solution space
3. **Find free variables** - buttons whose press counts aren't uniquely determined
4. **Priority queue search**:
   - Explore combinations of free variable values
   - Order by sum (explore smaller sums first)
   - For each combination, back-substitute to find pivot variable values
   - Track the minimum valid (all non-negative integer) solution

```java
PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> {
    int sumA = 0, sumB = 0;
    for (int v : a) sumA += v;
    for (int v : b) sumB += v;
    return Integer.compare(sumA, sumB);
});
```

The priority queue ensures we find the optimal solution efficiently by exploring states with fewer button presses first.

## Results

**Part 1 Answer:** 375
**Part 2 Answer:** 15377

For 154 machines:
- Part 1 (binary lights): **375 total button presses**
- Part 2 (integer joltage): **15377 total button presses**

### Key Implementation Detail

An important aspect of the Part 2 solution is **solution verification**. Since we use floating-point arithmetic for Gaussian elimination but need integer solutions, rounding errors can produce incorrect results. The solution includes verification code that checks each proposed solution against the original equations:

```java
// Verify the solution actually satisfies the original equations
for (int counter = 0; counter < numCounters; counter++) {
    int actual = 0;
    for (int button = 0; button < numButtons; button++) {
        if (machine.buttons.get(button).contains(counter)) {
            actual += solution[button];
        }
    }
    if (actual != machine.joltageRequirements[counter]) {
        verified = false;
        break;
    }
}
```

This verification step is crucial for correctness, as it filters out solutions that appear valid after rounding but don't actually satisfy the constraints.

## Code Structure

- **[Day10.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day10/Day10.java)**: Main solution with machine parsing, Gaussian elimination over GF(2), and minimum solution finding
- **[Day10Test.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/test/com/stiksy/aoc2025/day10/Day10Test.java)**: Unit tests with example cases and regression tests

## Running the Solution

```bash
# Run the solution
bazel run //src/main/com/stiksy/aoc2025/day10:Day10

# Run tests
bazel test //src/test/com/stiksy/aoc2025/day10:Day10Test
```
