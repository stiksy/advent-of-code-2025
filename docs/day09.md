---
layout: default
title: Day 9 - Movie Theater
nav_order: 9
---

[Puzzle Link](https://adventofcode.com/2025/day/9) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day09/Day09.java) | [🎨 Interactive Visualization](visualizations/day09-visualization.html) | [← Back to Home](../index.html)

# Day 9: Movie Theater

[View Source Code](https://github.com/Stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day09/Day09.java)

## Problem Description

### Part 1

You slide down the firepole in the corner of the playground and land in the North Pole base movie theater!

The movie theater has a big tile floor with an interesting pattern. Elves here are redecorating the theater by switching out some of the square tiles in the big grid they form. Some of the tiles are red; the Elves would like to find the largest rectangle that uses red tiles for two of its opposite corners.

You can choose any two red tiles as the opposite corners of your rectangle; your goal is to find the largest rectangle possible.

Using two red tiles as opposite corners, what is the largest area of any rectangle you can make?

### Part 2

The Elves just remembered: they can only switch out tiles that are red or green. So, your rectangle can only include red or green tiles.

In your list, every red tile is connected to the red tile before and after it by a straight line of green tiles. The list wraps, so the first red tile is also connected to the last red tile. In addition, all of the tiles inside this loop of red and green tiles are also green.

The rectangle you choose still must have red tiles in opposite corners, but any other tiles it includes must now be red or green.

Using two red tiles as opposite corners, what is the largest area of any rectangle you can make using only red and green tiles?

## Solution Approach

### Part 1: Brute Force Rectangle Area Calculation

The problem asks us to find the largest rectangle that can be formed using any two red tiles as opposite corners.

#### Key Implementation Details

**Important**: The area calculation must include both boundary tiles:
- Width = |x2 - x1| + 1
- Height = |y2 - y1| + 1
- Area = Width × Height

#### Algorithm

```java
public long solvePart1(List<String> input) {
    List<Point> redTiles = parseInput(input);
    long maxArea = 0;

    // Try all pairs of red tiles as opposite corners
    for (int i = 0; i < redTiles.size(); i++) {
        for (int j = i + 1; j < redTiles.size(); j++) {
            Point p1 = redTiles.get(i);
            Point p2 = redTiles.get(j);

            // Calculate rectangle area (including both boundary points)
            long width = Math.abs(p2.x - p1.x) + 1;
            long height = Math.abs(p2.y - p1.y) + 1;
            long area = width * height;

            maxArea = Math.max(maxArea, area);
        }
    }

    return maxArea;
}
```

**Time Complexity**: O(n²) where n is the number of red tiles (496 in the actual input).

### Part 2: Efficient Edge-Crossing Algorithm

Part 2 adds a significant constraint: rectangles can only contain red or green tiles. The key insight is to use an efficient validation algorithm instead of checking every point.

#### The Challenge

With 496 red tiles and coordinates up to ~98,000, a naive approach of checking every point inside every rectangle would be extremely slow.

#### The Solution

Instead of checking every point, we:
1. **Sort rectangles by area (largest first)** - allows early exit once we find a valid one
2. **Check only polygon edges** - rather than checking millions of points

A rectangle is **invalid** if:
- Any red tile (except corners) is strictly inside the rectangle
- Any edge of the polygon passes completely through the rectangle

#### Algorithm

```java
private boolean isValidRectangleEfficient(Rectangle rect, List<Point> redTiles) {
    for (int i = 0; i < redTiles.size(); i++) {
        Point current = redTiles.get(i);
        Point next = redTiles.get((i + 1) % redTiles.size());

        // Check if red tile is strictly inside rectangle
        if (current.x > rect.minX && current.x < rect.maxX &&
            current.y > rect.minY && current.y < rect.maxY) {
            return false;
        }

        // Check if horizontal edge passes through rectangle
        if (current.y == next.y) {
            int minX = Math.min(current.x, next.x);
            int maxX = Math.max(current.x, next.x);
            if (minX <= rect.minX && maxX >= rect.maxX &&
                current.y > rect.minY && current.y < rect.maxY) {
                return false;
            }
        }

        // Check if vertical edge passes through rectangle
        if (current.x == next.x) {
            int minY = Math.min(current.y, next.y);
            int maxY = Math.max(current.y, next.y);
            if (minY <= rect.minY && maxY >= rect.maxY &&
                current.x > rect.minX && current.x < rect.maxX) {
                return false;
            }
        }
    }

    return true;
}
```

#### Performance

This approach reduces the complexity from checking millions of points to just checking 496 edges:
- **Sorting rectangles**: O(n² log n) ≈ 122,760 * log(122,760) ≈ 2M operations
- **Edge checks**: O(n) per rectangle = 496 edge checks
- **Early termination**: Usually finds answer after checking only a few rectangles

The optimized solution runs in **under 1 second**, compared to several minutes for the naive approach.

## Results

- **Part 1**: 4781546175
- **Part 2**: 1573359081

## Testing

The solution includes comprehensive tests:

1. **Example Tests**: Verifies correct answers for the provided examples (Part 1: 50, Part 2: 24)
2. **Regression Tests**: Ensures answers remain correct for actual puzzle input

Run tests with:
```bash
bazel test //src/test/com/stiksy/aoc2025/day09:Day09Test --test_output=all
```

Run the solution:
```bash
bazel run //src/main/com/stiksy/aoc2025/day09:Day09
```
