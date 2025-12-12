---
layout: default
title: Day 5 - Cafeteria
nav_order: 5
---

# Day 5: Cafeteria

[Puzzle Link](https://adventofcode.com/2025/day/5) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day05/Day05.java) | [🎨 Interactive Visualization](visualizations/day05-visualization.html) | [← Back to Home](../index.html)

## Puzzle Description

### Part 1

As the forklifts break through the wall, the Elves are delighted to discover that there was a cafeteria on the other side after all.

You can hear a commotion coming from the kitchen. "At this rate, we won't have any time left to put the wreaths up in the dining hall!" Resolute in your quest, you investigate.

"If only we hadn't switched to the new inventory management system right before Christmas!" another Elf exclaims. You ask what's going on.

The Elves in the kitchen explain the situation: because of their complicated new inventory management system, they can't figure out which of their ingredients are fresh and which are spoiled. When you ask how it works, they give you a copy of their database (your puzzle input).

**The Setup:**
- The database operates on ingredient IDs
- It consists of a list of fresh ingredient ID ranges, a blank line, and a list of available ingredient IDs
- Fresh ID ranges are inclusive: `3-5` means IDs 3, 4, and 5 are all fresh
- Ranges can overlap; an ingredient ID is fresh if it is in ANY range

**Example Input:**
```
3-5
10-14
16-20
12-18

1
5
8
11
17
32
```

**Processing the Example:**
- Ingredient ID `1` is **spoiled** (not in any range)
- Ingredient ID `5` is **fresh** (in range 3-5)
- Ingredient ID `8` is **spoiled** (not in any range)
- Ingredient ID `11` is **fresh** (in range 10-14)
- Ingredient ID `17` is **fresh** (in ranges 16-20 and 12-18)
- Ingredient ID `32` is **spoiled** (not in any range)

**Result:** 3 of the available ingredient IDs are fresh.

**Goal:** Process the database file from the new inventory management system. How many of the available ingredient IDs are fresh?

### Part 2

The Elves start bringing their spoiled inventory to the trash chute at the back of the kitchen.

So that they can stop bugging you when they get new inventory, the Elves would like to know all of the IDs that the fresh ingredient ID ranges consider to be fresh. An ingredient ID is still considered fresh if it is in any range.

Now, the second section of the database (the available ingredient IDs) is irrelevant. Here are the fresh ingredient ID ranges from the above example:

```
3-5
10-14
16-20
12-18
```

The ingredient IDs that these ranges consider to be fresh are: 3, 4, 5, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, and 20.

**Analysis:**
- Range `3-5` contains 3 IDs
- Ranges `10-14`, `16-20`, and `12-18` overlap significantly
- After merging overlaps: `10-20` contains 11 IDs
- Total: 3 + 11 = **14 ingredient IDs**

**Goal:** Process the database file again. How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges?

## Solution Approach

### Part 1: Checking Individual Ingredients

Part 1 is a straightforward membership checking problem: for each ingredient ID in the list, check if it falls within any of the given ranges.

**Algorithm:**
1. Parse input into two sections:
   - List of Range objects (start, end pairs)
   - List of ingredient IDs to check
2. For each ingredient ID:
   - Check if it falls within any range (start ≤ id ≤ end)
   - If yes, count it as fresh
3. Return the count

**Code Implementation:**

```java
public long solvePart1(List<String> input) {
    // Parse the input into ranges and ingredient IDs
    List<Range> ranges = new ArrayList<>();
    List<Long> ingredientIds = new ArrayList<>();
    boolean parsingRanges = true;

    for (String line : input) {
        if (line.trim().isEmpty()) {
            parsingRanges = false;
            continue;
        }

        if (parsingRanges) {
            // Parse range like "3-5"
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new Range(start, end));
        } else {
            // Parse individual ingredient ID
            ingredientIds.add(Long.parseLong(line.trim()));
        }
    }

    // Count how many ingredient IDs are fresh
    long freshCount = 0;
    for (long id : ingredientIds) {
        if (isFresh(id, ranges)) {
            freshCount++;
        }
    }

    return freshCount;
}

private boolean isFresh(long id, List<Range> ranges) {
    for (Range range : ranges) {
        if (id >= range.start && id <= range.end) {
            return true;
        }
    }
    return false;
}
```

**Key Insights:**
- Simple linear search through ranges for each ingredient ID
- Early termination when a matching range is found
- Time complexity: O(n × m) where n = number of ingredients, m = number of ranges
- Space complexity: O(n + m) for storing parsed data
- No optimization needed - the brute force approach is sufficient

### Part 2: Counting All Fresh IDs

Part 2 transforms the problem from checking specific IDs to counting ALL possible fresh IDs across the ranges. The key challenge is handling overlapping ranges.

**Challenge:**
```
Ranges: 10-14, 16-20, 12-18
```

If we naively count: (14-10+1) + (20-16+1) + (18-12+1) = 5 + 5 + 7 = 17

But this **double-counts** overlapping IDs:
- IDs 12, 13, 14 are in both ranges 10-14 and 12-18
- IDs 16, 17, 18 are in both ranges 16-20 and 12-18

**Solution: Merge Overlapping Ranges**

The correct approach is to merge all overlapping or adjacent ranges first, then count the IDs in the merged ranges.

**Algorithm:**
1. Parse only the ranges (ignore ingredient IDs after blank line)
2. Sort ranges by start position
3. Merge overlapping/adjacent ranges:
   - Two ranges overlap if: `nextStart ≤ currentEnd + 1`
   - When merging: `newEnd = max(currentEnd, nextEnd)`
4. Count total IDs in all merged ranges

**Code Implementation:**

```java
public long solvePart2(List<String> input) {
    // Parse only the ranges
    List<Range> ranges = new ArrayList<>();

    for (String line : input) {
        if (line.trim().isEmpty()) {
            break; // Stop at blank line
        }

        String[] parts = line.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);
        ranges.add(new Range(start, end));
    }

    return countFreshIds(ranges);
}

private long countFreshIds(List<Range> ranges) {
    if (ranges.isEmpty()) {
        return 0;
    }

    // Sort ranges by start position
    List<Range> sortedRanges = new ArrayList<>(ranges);
    sortedRanges.sort((a, b) -> Long.compare(a.start, b.start));

    // Merge overlapping ranges
    List<Range> mergedRanges = new ArrayList<>();
    Range current = sortedRanges.get(0);

    for (int i = 1; i < sortedRanges.size(); i++) {
        Range next = sortedRanges.get(i);

        // Check if ranges overlap or are adjacent
        if (next.start <= current.end + 1) {
            // Merge ranges
            current = new Range(current.start, Math.max(current.end, next.end));
        } else {
            // No overlap, save current and start new range
            mergedRanges.add(current);
            current = next;
        }
    }
    mergedRanges.add(current);

    // Count total IDs in merged ranges
    long totalCount = 0;
    for (Range range : mergedRanges) {
        totalCount += (range.end - range.start + 1);
    }

    return totalCount;
}
```

**Why Adjacent Ranges Matter:**

The condition `next.start <= current.end + 1` handles both overlapping AND adjacent ranges:

```
Overlapping:  [10-14] and [12-18]  →  12 ≤ 14+1 ✓  →  Merge to [10-18]
Adjacent:     [10-14] and [15-20]  →  15 ≤ 14+1 ✓  →  Merge to [10-20]
Separated:    [10-14] and [16-20]  →  16 ≤ 14+1 ✗  →  Keep separate
```

Adjacent ranges should be merged because IDs 10-14 and 15-20 represent a continuous range 10-20 with no gaps.

**Example Walkthrough:**

Starting ranges: `3-5`, `10-14`, `16-20`, `12-18`

1. **Sort by start:** `3-5`, `10-14`, `12-18`, `16-20`

2. **Merge process:**
   - Start with `current = [3-5]`
   - Check `[10-14]`: 10 ≤ 5+1? (10 ≤ 6? No) → Save [3-5], `current = [10-14]`
   - Check `[12-18]`: 12 ≤ 14+1? (12 ≤ 15? Yes) → Merge: `current = [10-18]`
   - Check `[16-20]`: 16 ≤ 18+1? (16 ≤ 19? Yes) → Merge: `current = [10-20]`
   - End of list → Save [10-20]

3. **Merged ranges:** `[3-5]`, `[10-20]`

4. **Count IDs:**
   - Range [3-5]: 5 - 3 + 1 = 3 IDs
   - Range [10-20]: 20 - 10 + 1 = 11 IDs
   - Total: 14 IDs ✓

**Key Insights:**
- Classic interval merging problem
- Sorting is crucial for efficient merging
- The `+1` in the overlap condition handles adjacent ranges
- Time complexity: O(n log n) for sorting + O(n) for merging = O(n log n)
- Space complexity: O(n) for storing sorted and merged ranges
- This is a common pattern in range/interval problems

### Range Data Structure

Both parts use a simple immutable `Range` class:

```java
private static class Range {
    final long start;
    final long end;

    Range(long start, long end) {
        this.start = start;
        this.end = end;
    }
}
```

Using `long` instead of `int` is important - the actual puzzle input has very large ingredient IDs (Part 2 answer is over 352 trillion!).

## Algorithm Complexity

### Part 1
- **Time:** O(n × m) where n = ingredients, m = ranges
- **Space:** O(n + m)
- For typical inputs with small m, this is effectively O(n)

### Part 2
- **Time:** O(n log n) dominated by sorting
- **Space:** O(n) for storing ranges
- Much more efficient than Part 1 for large inputs

## Results

- **Part 1:** `694` (fresh ingredients from the available list)
- **Part 2:** `352716206375547` (total fresh ingredient IDs across all ranges)

## Implementation Notes

The solution demonstrates two classic algorithmic patterns:

1. **Part 1** (`Day05.java:18`): Set membership testing - checking if elements belong to a collection of ranges
2. **Part 2** (`Day05.java:53`): Interval merging - consolidating overlapping ranges to avoid double-counting

The dramatic difference in answers (694 vs 352 trillion) highlights why Part 2 asks for the count rather than generating all IDs - the actual ID space is enormous! The range-merging approach efficiently handles this without materializing individual IDs.