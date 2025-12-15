---
layout: default
title: Day 12 - Christmas Tree Farm
nav_order: 12
---

[Puzzle Link](https://adventofcode.com/2025/day/12) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day12/Day12.java) | [🎨 Interactive Visualization](visualizations/day12-visualization.html) | [← Back to Home](../index.html)

# Day 12: Christmas Tree Farm

[View Solution on GitHub](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day12/Day12.java) | [View Puzzle](https://adventofcode.com/2025/day/12)

## Problem Description

The Elves need to place presents under Christmas trees, but the presents come in weird polyomino shapes and must fit into rectangular regions on a 2D grid. The challenge is to determine which regions can accommodate all their required presents.

### Input Format

The input has two sections:

1. **Shape Definitions**: Each shape is labeled with an index and displayed visually using `#` for present cells and `.` for empty space
2. **Region Requirements**: Each line specifies region dimensions (width×height) and the count of each shape type needed

Example:
```
0:
###
##.
##.

1:
###
##.
.##

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
```

### Rules

- Presents can be **rotated and flipped** to fit
- Shapes cannot overlap (no two `#` cells in the same grid position)
- Empty cells (`.`) in shape diagrams don't block other presents
- All presents must fit perfectly on the grid

## Solution Approach

This is a **polyomino packing problem** solved using backtracking with constraint satisfaction.

### Algorithm

1. **Shape Parsing and Normalization**
   - Parse each shape into a list of (row, col) coordinates
   - Normalize to have minimum row and column at 0

2. **Generate All Orientations**
   - For each shape, generate all unique rotations (0°, 90°, 180°, 270°)
   - Generate flipped versions
   - Deduplicate using normalized representations
   - Typical shapes have 2-8 unique orientations

3. **Backtracking Search**
   - Build list of presents to place (expanding counts into individual pieces)
   - Try to place each present sequentially
   - For each present, try all orientations at all grid positions
   - If placement is valid, recurse to place next present
   - Backtrack if no valid placement exists

### Key Code: Shape Rotation and Reflection

```java
Shape rotateOnce() {
    List<int[]> newCells = new ArrayList<>();
    for (int[] cell : cells) {
        // Rotate 90 degrees clockwise: (r, c) -> (c, -r)
        newCells.add(new int[]{cell[1], -cell[0]});
    }
    return new Shape(newCells);
}

Shape flip() {
    List<int[]> newCells = new ArrayList<>();
    for (int[] cell : cells) {
        // Flip horizontally: (r, c) -> (r, -c)
        newCells.add(new int[]{cell[0], -cell[1]});
    }
    return new Shape(newCells);
}
```

### Key Code: Backtracking Placement

```java
private boolean backtrack(boolean[][] grid, List<Integer> presentsToPlace, int index,
                           List<List<Shape>> allOrientations) {
    if (index == presentsToPlace.size()) {
        return true; // All presents placed successfully
    }

    int shapeIdx = presentsToPlace.get(index);
    List<Shape> orientations = allOrientations.get(shapeIdx);

    // Try each orientation
    for (Shape orientation : orientations) {
        // Try each position in the grid
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (canPlace(grid, orientation, row, col)) {
                    place(grid, orientation, row, col, true);
                    if (backtrack(grid, presentsToPlace, index + 1, allOrientations)) {
                        return true;
                    }
                    place(grid, orientation, row, col, false); // Backtrack
                }
            }
        }
    }

    return false;
}
```

### Optimizations

1. **Early Pruning**: Before attempting backtracking, verify total cells needed ≤ available cells
2. **Orientation Deduplication**: Only try unique orientations (many polyominoes have symmetries)
3. **Grid Bounds Checking**: Quick rejection of invalid placements

### Why Backtracking Works

Despite the large search space, several factors make this tractable:

- **Early Failure Detection**: Invalid placements are rejected immediately
- **Constraint Propagation**: Each placed piece constrains future placements
- **Problem Structure**: The actual puzzle inputs are designed to be solvable
- **Limited Piece Count**: Even with 200+ presents, most regions either fit easily or fail quickly

## Complexity Analysis

**Time Complexity**: O(P × O × W × H) per region where:
- P = number of presents to place
- O = orientations per shape (typically 2-8)
- W, H = region dimensions

In worst case, this is exponential, but practical inputs solve in seconds.

**Space Complexity**: O(P + W×H) for:
- Recursion stack depth (P)
- Grid state (W×H)

## Results

**Part 1 Answer:** 577

Out of 20 regions tested, **577 regions** can fit all their required presents.

Example breakdown:
- Region sizes range from 35×48 to 50×50
- Each region requires 170-350+ total cells filled
- Shapes are 7-cell polyominoes
- Success depends on specific shape combinations and region dimensions

## Code Structure

- **[Day12.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day12/Day12.java)**: Complete solution with shape parsing, orientation generation, and backtracking
- **[Day12Test.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/test/com/stiksy/aoc2025/day12/Day12Test.java)**: Unit tests with example and regression tests

## Running the Solution

```bash
# Run the solution
bazel run //src/main/com/stiksy/aoc2025/day12:Day12

# Run tests
bazel test //src/test/com/stiksy/aoc2025/day12:Day12Test
```
