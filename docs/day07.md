---
layout: default
title: Day 7 - Laboratories
nav_order: 7
---

# Day 7: Laboratories

[Puzzle Link](https://adventofcode.com/2025/day/7) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day07/Day07.java) | [🎨 Interactive Visualization](visualizations/day07-visualization.html) | [← Back to Home](../index.html)

## Problem Description

### Part 1

After helping the cephalopods with the trash compactor, you find yourself in a teleporter lab. The teleporter is malfunctioning with error code 0H-N0, indicating an issue with one of the tachyon manifolds.

You receive a diagram of the tachyon manifold (your puzzle input). Here's how it works:

- A tachyon beam enters the manifold at position `S` and always moves **downward**
- Tachyon beams pass freely through empty space (`.`)
- When a beam encounters a splitter (`^`), the beam **stops**, and two new beams are emitted:
  - One beam continues from the **immediate left** of the splitter
  - One beam continues from the **immediate right** of the splitter

**Goal**: Count how many times the beam is split as it travels through the manifold.

#### Example

```
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
```

In this example, the beam is split **21 times**.

### Part 2

The manifold is actually a **quantum tachyon manifold**! A single tachyon particle takes **both** the left and right path at each splitter, following the many-worlds interpretation.

Each time a particle reaches a splitter, time itself splits into two timelines:
- In one timeline, the particle went left
- In the other timeline, the particle went right

**Goal**: Count the total number of different timelines (distinct paths through the manifold) that a single tachyon particle ends up on.

In the example above, there are **40 different timelines**.

## Solution Approach

### Part 1: Counting Splits with BFS

For Part 1, we need to simulate the beam's journey and count each split:

1. **Find the starting position** marked with `S`
2. **Use BFS (Breadth-First Search)** to track beam positions:
   - Start with a beam at `(startRow + 1, startCol)` (one row below S)
   - For each position:
     - If out of bounds, the beam exits (stop tracking it)
     - If we've visited this position before, skip it (avoid infinite loops)
     - If the cell is `.` (empty), continue downward
     - If the cell is `^` (splitter), increment split counter and create two new beams (left and right)

3. **Track visited positions** to ensure we count each splitter only once

The algorithm in `src/main/com/stiksy/aoc2025/day07/Day07.java:21`:

```java
public long solvePart1(List<String> input) {
    // Find starting position (S)
    int startRow = -1;
    int startCol = -1;
    for (int row = 0; row < input.size(); row++) {
        int col = input.get(row).indexOf('S');
        if (col != -1) {
            startRow = row;
            startCol = col;
            break;
        }
    }

    // Simulate the beam using BFS
    Queue<int[]> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    int splitCount = 0;

    // Start with initial beam going down from S
    queue.add(new int[]{startRow + 1, startCol});

    while (!queue.isEmpty()) {
        int[] current = queue.poll();
        int row = current[0];
        int col = current[1];

        // Check bounds
        if (row < 0 || row >= input.size() ||
            col < 0 || col >= input.get(0).length()) {
            continue;
        }

        // Check if already visited
        String state = row + "," + col;
        if (visited.contains(state)) {
            continue;
        }
        visited.add(state);

        char cell = input.get(row).charAt(col);

        if (cell == '^') {
            // Splitter! Count this split
            splitCount++;
            // Create two new beams: left and right
            queue.add(new int[]{row + 1, col - 1});
            queue.add(new int[]{row + 1, col + 1});
        } else {
            // Empty space, continue downward
            queue.add(new int[]{row + 1, col});
        }
    }

    return splitCount;
}
```

### Part 2: Counting Timelines with Memoized DFS

For Part 2, we need to count **all possible paths** (timelines) through the manifold. The key insight is that from any given position, the number of exit paths is deterministic and doesn't depend on how we got there.

**Algorithm:**

1. **Use DFS with Memoization**:
   - From each position, recursively explore all possible paths
   - At a splitter, the path count is the sum of paths from left and right branches
   - At empty space, continue downward
   - When exiting the grid, that's one complete timeline

2. **Memoization is crucial**:
   - Without memoization, we'd recalculate the same positions exponentially many times
   - With memoization, each position is calculated only once
   - This reduces time complexity from exponential to polynomial

The optimized algorithm in `src/main/com/stiksy/aoc2025/day07/Day07.java:80`:

```java
public long solvePart2(List<String> input) {
    // Find starting position (S)
    int startRow = -1;
    int startCol = -1;
    for (int row = 0; row < input.size(); row++) {
        int col = input.get(row).indexOf('S');
        if (col != -1) {
            startRow = row;
            startCol = col;
            break;
        }
    }

    // Count paths using DFS with memoization
    Map<String, Long> memo = new HashMap<>();
    return countPathsMemo(input, startRow + 1, startCol, memo);
}

private long countPathsMemo(List<String> input, int row, int col,
                           Map<String, Long> memo) {
    // Check bounds - if we exit the manifold, this is one timeline
    if (row < 0 || row >= input.size() ||
        col < 0 || col >= input.get(0).length()) {
        return 1;
    }

    // Check memoization
    String state = row + "," + col;
    if (memo.containsKey(state)) {
        return memo.get(state);
    }

    char cell = input.get(row).charAt(col);
    long pathCount = 0;

    if (cell == '^') {
        // Splitter: particle takes both paths
        pathCount += countPathsMemo(input, row + 1, col - 1, memo);
        pathCount += countPathsMemo(input, row + 1, col + 1, memo);
    } else {
        // Empty space, continue downward
        pathCount += countPathsMemo(input, row + 1, col, memo);
    }

    memo.put(state, pathCount);
    return pathCount;
}
```

## Key Insights

1. **Part 1 - BFS for exploration**: We use BFS to explore all reachable positions, counting splits along the way. The `visited` set prevents infinite loops and ensures each splitter is counted exactly once.

2. **Part 2 - Memoization is essential**: The initial backtracking approach was too slow because it recalculated the same positions many times. With memoization, we cache the result for each position, dramatically improving performance.

3. **Grid boundary as exit condition**: When a beam/particle exits the grid (goes out of bounds), that represents one complete timeline/path.

4. **State representation**: Using `"row,col"` as a string key works well for both the visited set (Part 1) and the memoization map (Part 2).

## Complexity Analysis

### Part 1
- **Time Complexity**: O(R × C) where R is rows and C is columns
  - Each cell is visited at most once due to the `visited` set
- **Space Complexity**: O(R × C)
  - Queue and visited set can contain at most all grid positions

### Part 2
- **Time Complexity**: O(R × C) with memoization
  - Each position is calculated exactly once and then cached
  - Without memoization: O(2^splits) - exponential in the number of splitters
- **Space Complexity**: O(R × C)
  - Memoization map stores results for each position
  - Recursion depth is at most R (height of grid)

## Final Answers

- **Part 1**: `1570` splits
- **Part 2**: `15118009521693` timelines

The large number in Part 2 shows the exponential growth of possible paths through the manifold when particles can take both branches at each splitter!
