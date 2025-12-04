---
layout: default
title: Day 4 - Printing Department
nav_order: 4
---

# Day 4: Printing Department

[Puzzle Link](https://adventofcode.com/2025/day/4) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day04/Day04.java) | [← Back to Home](../index.html)

## Puzzle Description

### Part 1

You ride the escalator down to the printing department, where they're preparing Christmas decorations. To help you continue deeper into the North Pole base while elevators are offline, the Elves need to optimize their forklift operations to have time to break through the back wall to reach the cafeteria.

**The Setup:**
- Paper rolls (`@`) are arranged on a large grid
- Forklifts can only access a roll if it has **fewer than 4** adjacent rolls
- Adjacent means the 8 surrounding positions (all directions including diagonals)
- Empty spaces are marked with `.`

**Goal:** Count how many rolls can be accessed by a forklift.

**Example Grid:**
```
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
```

**Accessible Rolls (marked with `x`):**
```
..xx.xx@x.
x@@.@.@.@@
@@@@@.x.@@
@.@@@@..@.
x@.@@@@.@x
.@@@@@@@.@
.@.@.@.@@@
x.@@@.@@@@
.@@@@@@@@.
x.x.@@@.x.
```

In this example, **13 rolls** can be accessed (they have fewer than 4 adjacent rolls).

### Part 2

Now the Elves need to access as much paper as possible through an iterative removal process:

1. Remove all accessible rolls (those with < 4 adjacent rolls)
2. After removal, check if more rolls become accessible
3. Repeat until no more rolls can be removed

**Process Example:**

Starting grid has **13 accessible rolls** → Remove them
- After removal, **12 more rolls** become accessible → Remove them
- After removal, **7 more rolls** become accessible → Remove them
- After removal, **5 more rolls** become accessible → Remove them
- After removal, **2 more rolls** become accessible → Remove them
- After removal, **1 more roll** becomes accessible → Remove it
- After removal, **1 more roll** becomes accessible → Remove it
- After removal, **1 more roll** becomes accessible → Remove it
- After removal, **1 more roll** becomes accessible → Remove it
- No more rolls are accessible - **Stop**

**Total removed:** 13 + 12 + 7 + 5 + 2 + 1 + 1 + 1 + 1 = **43 rolls**

**Goal:** Count the total number of rolls that can be removed through this iterative process.

## Solution Approach

### Part 1: Counting Accessible Rolls

The solution checks each roll in the grid and counts its adjacent neighbors in all 8 directions.

**Algorithm:**
1. Iterate through each position in the grid
2. For each roll (`@`):
   - Count adjacent rolls in all 8 directions
   - If count < 4, the roll is accessible
3. Return total count of accessible rolls

**Code Implementation:**
```java
public long solvePart1(List<String> input) {
    int rows = input.size();
    int cols = input.get(0).length();
    int accessibleCount = 0;

    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            if (input.get(row).charAt(col) == '@') {
                int adjacentRolls = countAdjacentRolls(input, row, col, rows, cols);
                if (adjacentRolls < 4) {
                    accessibleCount++;
                }
            }
        }
    }

    return accessibleCount;
}

private int countAdjacentRolls(List<String> grid, int row, int col, int rows, int cols) {
    int count = 0;
    int[][] directions = {
        {-1, -1}, {-1, 0}, {-1, 1},  // Top row
        {0, -1},           {0, 1},    // Left and right
        {1, -1},  {1, 0},  {1, 1}     // Bottom row
    };

    for (int[] dir : directions) {
        int newRow = row + dir[0];
        int newCol = col + dir[1];

        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
            if (grid.get(newRow).charAt(newCol) == '@') {
                count++;
            }
        }
    }

    return count;
}
```

**Key Insights:**
- Use 8-directional adjacency checking
- Simple grid traversal with boundary checks
- Time complexity: O(rows × cols × 8) = O(rows × cols)
- Space complexity: O(1)

### Part 2: Iterative Removal Process

Part 2 requires simulating the cascading removal of accessible rolls.

**Algorithm:**
1. Create a mutable copy of the grid
2. Loop until no more removals occur:
   - Find all accessible rolls (< 4 adjacent) in current state
   - Mark them for removal in a boolean array
   - Remove all marked rolls simultaneously
   - Count total removed
3. Return total count

**Code Implementation:**
```java
public long solvePart2(List<String> input) {
    // Create mutable grid
    int rows = input.size();
    int cols = input.get(0).length();
    char[][] grid = new char[rows][cols];

    for (int row = 0; row < rows; row++) {
        grid[row] = input.get(row).toCharArray();
    }

    int totalRemoved = 0;
    boolean removed = true;

    // Iterative removal
    while (removed) {
        removed = false;
        boolean[][] toRemove = new boolean[rows][cols];

        // Find all accessible rolls
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == '@') {
                    int adjacentRolls = countAdjacentRolls(grid, row, col, rows, cols);
                    if (adjacentRolls < 4) {
                        toRemove[row][col] = true;
                        removed = true;
                    }
                }
            }
        }

        // Remove all accessible rolls simultaneously
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (toRemove[row][col]) {
                    grid[row][col] = '.';
                    totalRemoved++;
                }
            }
        }
    }

    return totalRemoved;
}
```

**Why Simultaneous Removal is Important:**

We must find ALL accessible rolls in the current state, then remove them all at once. If we removed them one-by-one during iteration, we'd be checking accessibility against a partially-modified grid, which would give incorrect results.

**Example:**
```
Initial: @@   (both have 1 adjacent, both accessible)
```

If we removed left-to-right immediately:
- Left @ has 1 adjacent → Remove it → Grid becomes `.@`
- Right @ now has 0 adjacent (incorrect - it had 1 when we started!)

Correct approach:
- Left @ has 1 adjacent → Mark for removal
- Right @ has 1 adjacent → Mark for removal
- Remove both → Grid becomes `..`

**Key Insights:**
- Iterative simulation with fixed-point iteration
- Must identify all accessible rolls BEFORE removing any
- Grid state changes after each complete round of removals
- Continues until no accessible rolls remain
- Time complexity: O(iterations × rows × cols)
  - Worst case iterations: O(min(rows, cols))
  - Practical iterations: Usually much fewer
- Space complexity: O(rows × cols) for grid copy and removal tracking

**Algorithm Type:** This is a form of **cellular automaton** or **fixed-point iteration** where we repeatedly apply a rule until the system reaches a stable state.

## Results

- **Part 1:** `1553` (rolls that can be accessed initially)
- **Part 2:** `8442` (total rolls removed through iterative process)

## Implementation Notes

The solution elegantly handles both parts:
- Part 1 (`Day04.java:17`) is a simple accessibility check
- Part 2 (`Day04.java:64`) extends Part 1 with iterative removal simulation

The key difference is mutability: Part 1 treats the grid as immutable, while Part 2 creates a mutable copy to simulate the removal process. The use of a separate `toRemove` boolean array ensures all removals in an iteration happen simultaneously based on the state at the start of that iteration.
