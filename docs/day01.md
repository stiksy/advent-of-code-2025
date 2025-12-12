---
layout: default
title: Day 1 - Secret Entrance
nav_order: 1
---

# Day 1: Secret Entrance

[Puzzle Link](https://adventofcode.com/2025/day/1) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day01/Day01.java) | [🎨 Interactive Visualization](visualizations/day01-visualization.html) | [← Back to Home](../index.html)

## Puzzle Description

### Part 1

The Elves have discovered project management and need help decorating the North Pole before December 12th. At the secret entrance, there's a safe with a circular dial numbered from 0 to 99. The dial starts at position **50**.

You receive a sequence of rotations in the format:
- `L` or `R` (left rotates toward lower numbers, right toward higher numbers)
- Followed by a number indicating how many clicks to rotate

**Key mechanics:**
- The dial wraps around: turning left from `0` goes to `99`, turning right from `99` goes to `0`
- The dial forms a circle with 100 positions (0-99)

**Goal:** Count how many times the dial lands exactly on position `0` after completing each rotation.

**Example:**
```
L68, L30, R48, L5, R60, L55, L1, L99, R14, L82
```

Following these rotations:
- After `R48`: dial points to `0` ✓
- After `L55`: dial points to `0` ✓
- After `L99`: dial points to `0` ✓

Password: **3** (the dial pointed at 0 three times)

### Part 2

You're sure that's the right password, but the door won't open. You knock, but nobody answers. You build a snowman while you think.

As you're rolling the snowballs for your snowman, you find another security document that must have fallen into the snow:

**"Due to newer security protocols, please use password method 0x434C49434B until further notice."**

You remember from the training seminar that "method 0x434C49434B" means you're actually supposed to count the number of times any click causes the dial to point at 0, **regardless of whether it happens during a rotation or at the end of one**.

**Example (same rotations as Part 1):**

The dial points at zero during these rotations:
- The dial starts by pointing at **50**
- The dial is rotated **L68** to point at **82**; during this rotation, it points at 0 once
- The dial is rotated **L30** to point at **52**
- The dial is rotated **R48** to point at **0** ✓
- The dial is rotated **L5** to point at **95**
- The dial is rotated **R60** to point at **55**; during this rotation, it points at 0 once
- The dial is rotated **L55** to point at **0** ✓
- The dial is rotated **L1** to point at **99**
- The dial is rotated **L99** to point at **0** ✓
- The dial is rotated **R14** to point at **14**
- The dial is rotated **L82** to point at **32**; during this rotation, it points at 0 once

In this example, the dial points at 0 three times at the end of a rotation, plus three more times during a rotation. So, in this example, the new password would be **6**.

**Be careful:** If the dial were pointing at 50, a single rotation like **R1000** would cause the dial to point at 0 ten times before returning back to 50!

## Interactive Visualization

Want to see the dial in action? Check out the [interactive visualization](visualizations/day01-visualization.html) that lets you:
- Watch the dial rotate step-by-step through each rotation
- Toggle between Part 1 and Part 2 modes to see the different counting methods
- Step forward/backward through the rotations
- Auto-play through the entire sequence
- Load the example data or actual puzzle input
- See a history of all rotations and zero crossings

The visualization clearly shows the difference between Part 1 (counting only when the dial lands on 0) and Part 2 (counting every time the dial passes through 0).

## Solution Approach

### Part 1: Counting Final Positions at Zero

The solution simulates the dial rotations and tracks the position after each rotation.

**Algorithm:**
1. Start at position 50
2. For each rotation:
   - Parse direction (`L` or `R`) and distance
   - Update position using modular arithmetic:
     - Left: `position = (position - distance) % 100`
     - Right: `position = (position + distance) % 100`
   - Handle negative values for left rotations
3. Check if position equals 0 after each rotation
4. Return the total count

**Code Implementation:**
```java
public long solvePart1(List<String> input) {
    int position = 50;  // Starting position
    int zeroCount = 0;

    for (String rotation : input) {
        char direction = rotation.charAt(0);
        int distance = Integer.parseInt(rotation.substring(1));

        // Apply rotation with wraparound
        if (direction == 'L') {
            position = (position - distance) % 100;
            if (position < 0) {
                position += 100;  // Handle negative wraparound
            }
        } else { // direction == 'R'
            position = (position + distance) % 100;
        }

        // Check if we landed on 0
        if (position == 0) {
            zeroCount++;
        }
    }

    return zeroCount;
}
```

**Key Insights:**
- Use modular arithmetic (`% 100`) to handle wraparound
- Java's modulo can return negative values, so we add 100 for left rotations
- Time complexity: O(n) where n is the number of rotations
- Space complexity: O(1)

### Part 2: Counting Zero Crossings

For Part 2, we need to count every time we pass through 0, not just when we land on it.

**Algorithm:**
1. For rotations ≥100, count complete wraps: `distance / 100`
2. For the partial rotation (distance % 100):
   - **Left rotation**: Check if `newPosition ≤ 0` and we started at `position > 0`
   - **Right rotation**: Check if `newPosition ≥ 100` (wrapping from 99 to 0)
3. Sum all crossings

**Code Implementation:**
```java
public long solvePart2(List<String> input) {
    int position = 50;
    int zeroCount = 0;

    for (String rotation : input) {
        char direction = rotation.charAt(0);
        int distance = Integer.parseInt(rotation.substring(1));

        // Count complete wraps (each contributes 1 zero crossing)
        zeroCount += distance / 100;

        // Check if we cross 0 in the partial rotation
        int newPosition;
        if (direction == 'L') {
            newPosition = position - (distance % 100);
            if (newPosition <= 0 && position > 0) {
                zeroCount++;  // We crossed or landed on 0
            }
            // Normalize position
            newPosition = (position - distance) % 100;
            if (newPosition < 0) {
                newPosition += 100;
            }
        } else { // direction == 'R'
            newPosition = position + (distance % 100);
            if (newPosition >= 100) {
                zeroCount++;  // We crossed 0 by wrapping
            }
            // Normalize position
            newPosition = (position + distance) % 100;
        }

        position = newPosition;
    }

    return zeroCount;
}
```

**Key Insights:**
- Complete wraps (distance ≥ 100) always pass through 0: `distance / 100` times
- For partial rotations, detect boundary crossing:
  - Left: crossing from positive to ≤0
  - Right: crossing from <100 to ≥100
- The condition `newPosition <= 0 && position > 0` handles both landing on 0 and crossing it
- Time complexity: O(n)
- Space complexity: O(1)

## Results

- **Part 1:** `1145` (dial landed on 0 exactly 1145 times)
- **Part 2:** `6561` (dial passed through 0 a total of 6561 times)

## Implementation Notes

The solution is implemented in `Day01.java:17` (Part 1) and `Day01.java:42` (Part 2), using modular arithmetic to elegantly handle the circular dial wraparound without complex branching logic.
