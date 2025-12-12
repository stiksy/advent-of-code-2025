---
layout: default
title: Day 11 - Reactor
nav_order: 11
---

[← Back to Home](../index.html)

# Day 11: Reactor

[View Solution on GitHub](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day11/Day11.java) | [View Puzzle](https://adventofcode.com/2025/day/11)

## Problem Description

### Part 1

You discover a reactor connected to various devices through a network of cables. Each device has outputs that connect to other devices, forming a directed graph. The challenge is to find all possible paths that data can take from a starting device ("you") to the reactor output ("out").

Given input like:
```
aaa: you hhh
you: bbb ccc
bbb: ddd eee
...
```

Each line shows a device and its connected outputs. The task is to count all distinct paths from "you" to "out".

**Example**: The sample input has 5 different paths from "you" to "out".

### Part 2

After analysis, the Elves discover the problematic data path must visit both "dac" (digital-to-analog converter) and "fft" (fast Fourier transform device). Now the task is to find all paths from "svr" (server rack) to "out" that visit BOTH "dac" and "fft" in any order.

**Example**: From the Part 2 sample input with 8 total paths from "svr" to "out", only 2 paths visit both required devices.

## Solution Approach

### Part 1: Basic Path Counting with DFS

The solution uses **depth-first search (DFS) with backtracking**:

1. **Parse input** into an adjacency list representation
2. **DFS traversal** starting from "you", exploring all possible paths
3. **Cycle prevention** using a visited set for the current path
4. **Backtracking** by removing nodes from visited after exploring
5. **Count paths** incrementing counter when reaching "out"

```java
private static int dfs(Map<String, List<String>> graph, String current,
                       String end, Set<String> visited) {
    if (current.equals(end)) return 1;

    if (!graph.containsKey(current)) return 0;

    visited.add(current);
    int pathCount = 0;

    for (String neighbor : graph.get(current)) {
        if (!visited.contains(neighbor)) {
            pathCount += dfs(graph, neighbor, end, visited);
        }
    }

    visited.remove(current);  // Backtrack
    return pathCount;
}
```

### Part 2: Constrained Path Counting with DAG Optimization

Part 2 adds the constraint that paths must visit specific nodes. The key insight is to track which required nodes have been visited along each path.

#### Challenge: Exponential Path Growth and Memoization

The graph from "svr" to "out" contains **hundreds of trillions of paths**, making naive DFS impossibly slow. The solution needs **memoization** to cache results.

However, naive memoization based on `(current_node, required_nodes_visited)` has a fundamental problem: the number of paths from a given state depends on which nodes are already in the current path (the `visited` set used for cycle prevention). Different paths reaching the same state have different visited sets, making simple memoization incorrect.

#### The Key Insight: Directed Acyclic Graph (DAG)

The breakthrough is recognizing that **the reactor graph is a DAG** - it has no cycles! This changes everything:

- In a DAG, we don't need a `visited` set for cycle prevention
- Without the path-dependent visited set, memoization based on `(node, requiredVisited)` becomes valid
- The cached result for a state is truly independent of how we reached that state

#### Implementation

First, detect if the graph is a DAG:

```java
private static boolean hasCycle(Map<String, List<String>> graph) {
    Set<String> visiting = new HashSet<>();
    Set<String> visited = new HashSet<>();

    for (String node : graph.keySet()) {
        if (hasCycleDFS(graph, node, visiting, visited)) {
            return true;
        }
    }
    return false;
}
```

Then use conditional memoization based on whether it's a DAG:

```java
private static long dfsWithRequired(Map<String, List<String>> graph, String current, String end,
                                    Set<String> visited, Set<String> required,
                                    Set<String> requiredVisited, Map<State, Long> memo,
                                    boolean isDAG) {
    if (current.equals(end)) {
        return requiredVisited.containsAll(required) ? 1 : 0;
    }

    // If it's a DAG, we can use memoization safely
    if (isDAG) {
        State state = new State(current, new HashSet<>(requiredVisited));
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
    }

    visited.add(current);
    if (required.contains(current)) {
        requiredVisited.add(current);
    }

    long pathCount = 0;
    for (String neighbor : graph.get(current)) {
        if (isDAG || !visited.contains(neighbor)) {
            pathCount += dfsWithRequired(graph, neighbor, end, visited,
                                        required, requiredVisited, memo, isDAG);
        }
    }

    visited.remove(current);
    if (required.contains(current)) {
        requiredVisited.remove(current);
    }

    if (isDAG) {
        memo.put(state, pathCount);
    }

    return pathCount;
}
```

### Why DAG Detection Enables Memoization

In a DAG:
- There are no cycles by definition
- We don't need to track visited nodes for cycle prevention
- The number of paths from `(node X, {dac visited})` to "out" is constant
- We can safely cache and reuse results across different paths

This transforms an intractable problem into one that solves in milliseconds.

### Complexity Analysis

**For DAG with memoization:**
- Time: O(N × 2^R × E) where N is nodes, R is required nodes, E is avg edges per node
- Space: O(N × 2^R) for the memo cache
- With R=2: O(N × 4), which is very efficient

**For non-DAG:**
- Would require O(P) time where P is the number of paths (exponential)
- Our input being a DAG makes Part 2 solvable

## Results

**Part 1 Answer:** 674
**Part 2 Answer:** 438,314,708,837,664

The astronomical difference between Part 1 (674 paths) and Part 2 (438+ trillion paths) shows:
- The "svr" starting node has access to vastly more paths than "you"
- The requirement to visit both "dac" and "fft" still leaves an enormous number of valid paths
- The DAG property combined with memoization is absolutely essential for solving Part 2
- Without recognizing the DAG structure, this problem would be computationally intractable

## Code Structure

- **[Day11.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day11/Day11.java)**: Main solution with graph parsing, DFS with backtracking, and memoized path counting
- **[Day11Test.java](https://github.com/stiksy/advent-of-code-2025/blob/main/src/test/com/stiksy/aoc2025/day11/Day11Test.java)**: Unit tests with example cases and regression tests

## Running the Solution

```bash
# Run the solution
bazel run //src/main/com/stiksy/aoc2025/day11:Day11

# Run tests
bazel test //src/test/com/stiksy/aoc2025/day11:Day11Test
```
