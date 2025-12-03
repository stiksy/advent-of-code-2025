---
layout: default
title: Day 2 - Gift Shop
nav_order: 2
---

# Day 2: Gift Shop

[Puzzle Link](https://adventofcode.com/2025/day/2) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day02/Day02.java) | [← Back to Home](../index.html)

## Puzzle Description

### Part 1

You've reached the gift shop at the North Pole, where a clerk needs help identifying invalid product IDs that a young Elf accidentally added to the database.

**Invalid ID Definition:** Any sequence of digits repeated exactly twice.

Examples of invalid IDs:
- `55` - the digit "5" repeated twice
- `6464` - the sequence "64" repeated twice
- `123123` - the sequence "123" repeated twice

**Important:** Numbers have no leading zeroes, so `0101` would not be considered a valid number.

**Input Format:** Comma-separated ranges in the format `start-end`:
```
11-22,95-115,998-1012,1188511880-1188511890,222220-222224,...
```

**Example Analysis:**
- Range `11-22`: Contains invalid IDs `11` and `22`
- Range `95-115`: Contains invalid ID `99`
- Range `998-1012`: Contains invalid ID `1010`
- Range `1188511880-1188511890`: Contains invalid ID `1188511885`
- Range `222220-222224`: Contains invalid ID `222222`

**Goal:** Sum all invalid IDs found in the given ranges.

Example answer: `1227775554`

### Part 2

The clerk quickly discovers that there are still invalid IDs in the ranges in your list. Maybe the young Elf was doing other silly patterns as well?

**New Definition:** Now, an ID is invalid if it is made only of some sequence of digits repeated **at least twice**.

Examples of invalid IDs:
- `12341234` - 1234 two times
- `123123123` - 123 three times
- `1212121212` - 12 five times
- `1111111` - 1 seven times

**Example Analysis (from same input as Part 1):**
- `11-22` still has two invalid IDs: **11** and **22**
- `95-115` now has **two** invalid IDs: **99** and **111** (new!)
- `998-1012` now has **two** invalid IDs: **999** (new!) and **1010**
- `1188511880-1188511890` still has one invalid ID: **1188511885**
- `222220-222224` still has one invalid ID: **222222**
- `1698522-1698528` still contains no invalid IDs
- `446443-446449` still has one invalid ID: **446446**
- `38593856-38593862` still has one invalid ID: **38593859**
- `565653-565659` now has one invalid ID: **565656** (new!)
- `824824821-824824827` now has one invalid ID: **824824824** (new!)
- `2121212118-2121212124` now has one invalid ID: **2121212121** (new!)

Adding up all the invalid IDs in this example produces **4174379265**.

## Solution Approach

### Part 1: Exact Two Repetitions

The key insight is that an ID with exactly two repetitions must have:
1. **Even length** (to split into two equal halves)
2. **First half equals second half**

**Algorithm:**
1. Parse the input to extract ranges
2. For each range, iterate through all numbers
3. Check if the number is invalid:
   - Convert to string
   - Check if length is even
   - Compare first half with second half
4. Sum all invalid IDs

**Code Implementation:**
```java
boolean isInvalidId(long id) {
    String idStr = String.valueOf(id);
    int length = idStr.length();

    // Must have even length to split into two equal parts
    if (length % 2 != 0) {
        return false;
    }

    int halfLength = length / 2;
    String firstHalf = idStr.substring(0, halfLength);
    String secondHalf = idStr.substring(halfLength);

    return firstHalf.equals(secondHalf);
}
```

**Example:**
- `123123`: length = 6 (even ✓), first half = "123", second half = "123" → invalid
- `12345`: length = 5 (odd ✗) → valid
- `123456`: length = 6 (even ✓), first half = "123", second half = "456" → valid

**Time Complexity:**
- For each ID: O(d) where d is the number of digits
- Overall: O(n × d) where n is the total count of IDs across all ranges

### Part 2: At Least Two Repetitions

For Part 2, we need to detect any repeating pattern, not just two repetitions.

**Algorithm:**
1. Try all possible pattern lengths from 1 to length/2
2. For each pattern length:
   - Check if total length is divisible by pattern length
   - Extract the pattern (first n characters)
   - Verify the pattern repeats throughout the entire string
3. Return true if any valid repeating pattern is found

**Code Implementation:**
```java
boolean isInvalidIdPart2(long id) {
    String idStr = String.valueOf(id);
    int length = idStr.length();

    // Try all possible pattern lengths from 1 to length/2
    for (int patternLength = 1; patternLength <= length / 2; patternLength++) {
        // Check if the total length is divisible by pattern length
        if (length % patternLength != 0) {
            continue;
        }

        String pattern = idStr.substring(0, patternLength);
        boolean isRepeated = true;

        // Verify pattern repeats throughout entire string
        for (int i = patternLength; i < length; i += patternLength) {
            String chunk = idStr.substring(i, Math.min(i + patternLength, length));
            if (!chunk.equals(pattern)) {
                isRepeated = false;
                break;
            }
        }

        if (isRepeated) {
            return true;
        }
    }

    return false;
}
```

**Example Analysis:**

For `123123123` (9 digits):
- Try pattern length 1: "1" → doesn't repeat throughout → continue
- Try pattern length 2: length not divisible by 2 → skip
- Try pattern length 3: "123" → repeats 3 times → invalid ✓

For `111111` (6 digits):
- Try pattern length 1: "1" → repeats 6 times → invalid ✓

For `12121212` (8 digits):
- Try pattern length 1: "1" → doesn't repeat → continue
- Try pattern length 2: "12" → repeats 4 times → invalid ✓

**Key Insights:**
- We only need to check pattern lengths up to `length/2` (smaller patterns mean at least 2 repetitions)
- Length must be divisible by pattern length for valid repetition
- Finding the first valid pattern is sufficient (early return)

**Time Complexity:**
- Worst case: O(d²) where d is the number of digits (trying all pattern lengths and verifying each)
- In practice: Often finds pattern quickly due to early return

## Results

- **Part 1:** `18467608014` (sum of all IDs with exactly two repetitions)
- **Part 2:** `20043697201` (sum of all IDs with at least two repetitions)

## Implementation Notes

The solution elegantly handles both parts with simple string manipulation:
- Part 1 (`Day02.java:70`) uses a simple half-split comparison
- Part 2 (`Day02.java:91`) uses a pattern-matching approach that generalizes to any number of repetitions

The input parsing handles comma-separated ranges, and the solution iterates through each range independently, making it easy to parallelize if needed for very large inputs.
