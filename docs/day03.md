---
layout: default
title: Day 3 - Lobby
nav_order: 3
---

# Day 3: Lobby

[Puzzle Link](https://adventofcode.com/2025/day/3) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day03/Day03.java) | [🎨 Interactive Visualization](visualizations/day03-visualization.html) | [← Back to Home](../index.html)

## Puzzle Description

### Part 1

You descend into the lobby and find that all elevators and the escalator are offline. An Elf explains that there was an electrical surge, and the escalator needs emergency power from batteries.

**The Setup:**
- Batteries are labeled with joltage ratings (digits 1-9)
- Batteries are organized into **banks** (each line of input is one bank)
- Each bank contains a sequence of batteries (digits)
- You must turn on **exactly two batteries** per bank
- The joltage produced = the two-digit number formed by the selected batteries
- **Batteries cannot be rearranged** - you must use them in their original positions

**Example Bank: `12345`**
- Turn on batteries at positions 1 and 3 → produces `24` jolts
- Turn on batteries at positions 0 and 4 → produces `15` jolts

**Goal:** Find the **maximum possible joltage** from each bank, then sum them all.

**Example Input:**
```
987654321111111
811111111111119
234234234234278
818181911112111
```

**Example Analysis:**
- `987654321111111`: Max joltage = `98` (first two batteries: 9 and 8)
- `811111111111119`: Max joltage = `89` (batteries at positions 0 and 14: 8 and 9)
- `234234234234278`: Max joltage = `78` (last two batteries: 7 and 8)
- `818181911112111`: Max joltage = `92` (batteries at positions 6 and 13: 9 and 2)

**Total:** 98 + 89 + 78 + 92 = `357`

### Part 2

The escalator doesn't move. The Elf explains that it probably needs more joltage to overcome the static friction of the system and hits the big red **"joltage limit safety override"** button. You lose count of the number of times she needs to confirm "yes, I'm sure" and decorate the lobby a bit while you wait.

Now, you need to make the largest joltage by turning on **exactly twelve batteries** within each bank.

**New Rules:**
- Turn on **exactly twelve batteries** per bank (instead of 2)
- The joltage output for the bank is still the number formed by the digits of the batteries you've turned on
- The only difference is that now there will be **12 digits** in each bank's joltage output instead of two
- Batteries still cannot be rearranged

**Example Analysis (same input as Part 1):**

Consider again the example from before:
```
987654321111111
811111111111119
234234234234278
818181911112111
```

Now, the joltages are much larger:

- In `987654321111111`, the largest joltage can be found by turning on everything except some 1s at the end to produce **987654321111**
- In the digit sequence `811111111111119`, the largest joltage can be found by turning on everything except some 1s, producing **811111111119**
- In `234234234234278`, the largest joltage can be found by turning on everything except a 2 battery, a 3 battery, and another 2 battery near the start to produce **434234234278**
- In `818181911112111`, the joltage **888911112111** is produced by turning on everything except some 1s near the front

The total output joltage is now much larger: 987654321111 + 811111111119 + 434234234278 + 888911112111 = **3121910778619**

## Interactive Visualization

Want to see the battery selection algorithm in action? Check out the [interactive visualization](visualizations/day03-visualization.html) that lets you:
- Watch the greedy algorithm select batteries step-by-step
- Toggle between Part 1 (2 batteries) and Part 2 (12 batteries) modes
- See batteries highlighted as they're considered and selected
- Visualize the search window and selection process
- Enter custom battery banks to test
- Adjust animation speed
- Track selection progress in real-time

The visualization clearly demonstrates how Part 1 exhaustively tries all pairs while Part 2 uses an efficient greedy approach to maximize the result.

## Solution Approach

### Part 1: Maximum Two-Digit Number

The problem is to select 2 positions from the battery bank to maximize the resulting two-digit number.

**Key Insight:** Try all possible pairs of positions (i, j) where i < j, and track the maximum.

**Algorithm:**
1. For each bank (line of input)
2. Try all pairs (i, j) where i < j
3. Form the number: `digit[i] × 10 + digit[j]`
4. Track the maximum value
5. Sum all maximum values

**Code Implementation:**
```java
private long findMaxJoltage(String bank) {
    int maxJoltage = 0;
    int length = bank.length();

    // Try all pairs of positions (i, j) where i < j
    for (int i = 0; i < length - 1; i++) {
        for (int j = i + 1; j < length; j++) {
            int digit1 = Character.getNumericValue(bank.charAt(i));
            int digit2 = Character.getNumericValue(bank.charAt(j));
            int joltage = digit1 * 10 + digit2;
            maxJoltage = Math.max(maxJoltage, joltage);
        }
    }

    return maxJoltage;
}
```

**Example Walkthrough (`987654321111111`):**
- Position 0,1: `98` ← Maximum!
- Position 0,2: `97`
- Position 0,3: `96`
- ... (other combinations)
- Best: `98`

**Complexity:**
- Time: O(n²) per bank, where n is the number of batteries
- Space: O(1)
- For typical inputs (15-20 batteries per bank), this is very fast

### Part 2: Maximum 12-Digit Number

Now we need to select 12 batteries from ~15 available to form the largest possible 12-digit number.

**Key Insight:** Use a **greedy algorithm**. At each position in our result:
1. Choose the largest available digit
2. That still leaves enough digits to complete the 12-digit number

**Algorithm:**
1. We need to pick 12 digits total
2. For position i in our result (0 to 11):
   - We need `12 - i` more digits
   - We can look ahead up to position: `length - (12 - i)`
   - Find the maximum digit in this lookahead window
   - Add it to result and continue from the next position
3. Return the formed 12-digit number

**Why Greedy Works:**
- We want the leftmost (most significant) digit to be as large as possible
- Once we pick a digit at position i, all remaining digits must come from positions after i
- The lookahead limit ensures we always have enough digits remaining

**Code Implementation:**
```java
private long findMaxJoltageWithTwelveBatteries(String bank) {
    int length = bank.length();
    int needed = 12;
    StringBuilder result = new StringBuilder();
    int startIdx = 0;

    for (int i = 0; i < needed; i++) {
        // Calculate how many more digits we need after this one
        int remaining = needed - i;

        // Maximum position we can look at while leaving enough digits
        int maxAllowedPos = length - remaining;

        // Find the maximum digit in the valid range
        char maxDigit = '0';
        int maxPos = startIdx;

        for (int j = startIdx; j <= maxAllowedPos; j++) {
            char digit = bank.charAt(j);
            if (digit > maxDigit) {
                maxDigit = digit;
                maxPos = j;
            }
        }

        // Add the maximum digit to result
        result.append(maxDigit);

        // Next search starts after the position we just selected
        startIdx = maxPos + 1;
    }

    return Long.parseLong(result.toString());
}
```

**Example Walkthrough (`987654321111111`, 15 batteries):**

Position 0 (need 12 more digits):
- Can look at indices 0-3 (need 12, have 15 total, so 15-12=3)
- Find max: '9' at index 0
- Result: `9`, next start: 1

Position 1 (need 11 more digits):
- Can look at indices 1-4 (15-11=4)
- Find max: '8' at index 1
- Result: `98`, next start: 2

Position 2 (need 10 more digits):
- Can look at indices 2-5 (15-10=5)
- Find max: '7' at index 2
- Result: `987`, next start: 3

... continue pattern ...

Final result: `987654321111` (skipped last three 1s)

**Complexity:**
- Time: O(n × k) where n is bank length, k is number of batteries to select (12)
- In practice: O(n) since k is constant
- Space: O(k) = O(12) = O(1)

**Why This Beats Trying All Combinations:**
- Trying all C(15,12) combinations = 455 possibilities per bank
- Greedy approach: only ~15×12 = 180 comparisons per bank
- More importantly: greedy runs in linear time vs exponential for brute force on larger inputs

## Results

- **Part 1:** `17330` (sum of maximum 2-digit joltages from all banks)
- **Part 2:** `171518260283767` (sum of maximum 12-digit joltages from all banks)

## Implementation Notes

The solution demonstrates two important algorithmic techniques:

1. **Part 1** (`Day03.java:28`): Brute force is acceptable when the search space is small (O(n²) is fine for n ≤ 20)

2. **Part 2** (`Day03.java:59`): Greedy algorithms excel when making locally optimal choices leads to globally optimal solutions. The key insight is that maximizing the leftmost digit is always optimal for numeric maximization.

Both solutions use simple string manipulation and avoid complex data structures, keeping the code clean and efficient.
