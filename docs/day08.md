---
layout: default
title: Day 8 - Playground
nav_order: 8
---

[Puzzle Link](https://adventofcode.com/2025/day/8) | [Solution Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day08/Day08.java) | [🎨 Interactive Visualization](visualizations/day08-visualization.html) | [← Back to Home](../index.html)

# Day 8: Playground

[View Source Code](https://github.com/stiksy/advent-of-code-2025/blob/main/src/main/com/stiksy/aoc2025/day08/Day08.java)

## Problem Description

### Part 1

After repairing the teleporter, you arrive at a vast underground playground where Elves are setting up Christmas decorations using suspended electrical junction boxes connected by strings of lights.

The Elves have a list of junction box positions in 3D space (X,Y,Z coordinates). They want to connect pairs of boxes that are closest together to save on string lights. When two boxes are connected, electricity can flow between them, forming a **circuit**.

**Connection Rules:**
- Connect the **1000 pairs** of junction boxes that are closest together
- Some connection attempts may fail if the boxes are already in the same circuit
- Calculate circuit sizes after all connection attempts

**Goal:** After making 1000 connection attempts, multiply together the sizes of the three largest circuits.

#### Example

With 20 junction boxes, after attempting the 10 shortest connections:
- 1 circuit contains 5 junction boxes
- 1 circuit contains 4 junction boxes
- 2 circuits contain 2 junction boxes each
- 7 circuits contain 1 junction box each

Product of three largest: `5 × 4 × 2 = 40`

### Part 2

The Elves need all junction boxes in one large circuit. Continue connecting the closest unconnected pairs until all boxes are in the same circuit.

**Goal:** Find the X coordinates of the last two junction boxes that needed to be connected, and multiply them together.

In the example, the last connection is between boxes at (216,146,977) and (117,168,530). The result is `216 × 117 = 25272`.

## Solution Approach

This is a classic **Minimum Spanning Tree (MST)** problem solved using **Kruskal's Algorithm** with a **Union-Find (Disjoint Set Union)** data structure.

### Key Data Structures

#### 1. Point Class
Represents a junction box in 3D space:

```java
static class Point {
    int x, y, z;

    Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double distanceTo(Point other) {
        long dx = (long) this.x - other.x;
        long dy = (long) this.y - other.y;
        long dz = (long) this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
```

#### 2. Edge Class
Represents a potential connection between two boxes:

```java
static class Edge implements Comparable<Edge> {
    int from, to;
    double distance;

    Edge(int from, int to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.distance, other.distance);
    }
}
```

#### 3. Union-Find Data Structure
Efficiently tracks and merges connected components (circuits):

```java
static class UnionFind {
    private int[] parent;
    private int[] size;

    UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false; // Already in the same set
        }

        // Union by size
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
        return true;
    }

    int getSize(int x) {
        return size[find(x)];
    }
}
```

### Part 1: Kruskal's Algorithm with Limited Connections

The algorithm in `src/main/com/stiksy/aoc2025/day08/Day08.java:111`:

```java
public long solvePart1(List<String> input, int connectionsToMake) {
    // Parse junction box positions
    List<Point> points = new ArrayList<>();
    for (String line : input) {
        String[] parts = line.split(",");
        points.add(new Point(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        ));
    }

    int n = points.size();

    // Create all edges and sort by distance (Kruskal's algorithm)
    PriorityQueue<Edge> edges = new PriorityQueue<>();
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist = points.get(i).distanceTo(points.get(j));
            edges.add(new Edge(i, j, dist));
        }
    }

    // Use Union-Find to connect the closest pairs
    UnionFind uf = new UnionFind(n);
    int connectionsAttempted = 0;

    while (!edges.isEmpty() && connectionsAttempted < connectionsToMake) {
        Edge edge = edges.poll();
        uf.union(edge.from, edge.to); // Try to connect
        connectionsAttempted++;
    }

    // Get the sizes of all circuits (connected components)
    Map<Integer, Integer> componentSizes = uf.getComponentSizes();
    List<Integer> sizes = new ArrayList<>(componentSizes.values());
    sizes.sort((a, b) -> b - a); // Sort in descending order

    // Multiply the three largest circuit sizes
    long result = 1;
    for (int i = 0; i < Math.min(3, sizes.size()); i++) {
        result *= sizes.get(i);
    }

    return result;
}
```

**Key Steps:**
1. Parse all junction box coordinates
2. Generate all possible edges (O(n²) edges for n boxes)
3. Sort edges by distance using a PriorityQueue
4. Make connection attempts in order of increasing distance
5. Count connection *attempts*, not successes (some attempts fail if boxes already connected)
6. After 1000 attempts, find component sizes and multiply the three largest

### Part 2: Complete MST

The algorithm in `src/main/com/stiksy/aoc2025/day08/Day08.java:158`:

```java
public long solvePart2(List<String> input) {
    // Parse junction box positions
    List<Point> points = new ArrayList<>();
    for (String line : input) {
        String[] parts = line.split(",");
        points.add(new Point(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        ));
    }

    int n = points.size();

    // Create all edges and sort by distance
    PriorityQueue<Edge> edges = new PriorityQueue<>();
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist = points.get(i).distanceTo(points.get(j));
            edges.add(new Edge(i, j, dist));
        }
    }

    // Use Union-Find to connect until all are in one circuit
    UnionFind uf = new UnionFind(n);
    int lastFrom = -1, lastTo = -1;

    while (!edges.isEmpty()) {
        Edge edge = edges.poll();
        if (uf.union(edge.from, edge.to)) {
            lastFrom = edge.from;
            lastTo = edge.to;

            // Check if all boxes are now in one circuit
            if (uf.getSize(edge.from) == n) {
                break;
            }
        }
    }

    // Multiply the X coordinates of the last two boxes connected
    return (long) points.get(lastFrom).x * points.get(lastTo).x;
}
```

**Key Steps:**
1. Parse all junction box coordinates
2. Generate and sort all edges by distance
3. Make connections until only 1 component remains
4. Track only *successful* connections (unlike Part 1)
5. When component size equals n, all boxes are connected
6. Return the product of X coordinates of the last connection

## Key Insights

### 1. Connection Attempts vs. Successful Connections

**Critical distinction between Part 1 and Part 2:**

- **Part 1**: Count connection *attempts* (including failed attempts if boxes already connected)
  - When we try to connect two boxes in the same circuit, nothing happens
  - This attempt still counts toward the 1000 limit

- **Part 2**: Count only *successful* connections
  - Only track connections that actually merge two components
  - Continue until all boxes are in one component

### 2. Union-Find Optimizations

**Path Compression:**
```java
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]); // Flatten tree during traversal
    }
    return parent[x];
}
```
- Flattens the tree structure during find operations
- Amortizes future find operations to nearly O(1)

**Union by Size:**
```java
if (size[rootX] < size[rootY]) {
    parent[rootX] = rootY;
    size[rootY] += size[rootX];
} else {
    parent[rootY] = rootX;
    size[rootX] += size[rootY];
}
```
- Always attach smaller tree to larger tree
- Keeps tree height logarithmic

### 3. Euclidean Distance in 3D

```java
double distanceTo(Point other) {
    long dx = (long) this.x - other.x;
    long dy = (long) this.y - other.y;
    long dz = (long) this.z - other.z;
    return Math.sqrt(dx * dx + dy * dy + dz * dz);
}
```
- Use `long` for intermediate calculations to prevent integer overflow
- Standard 3D distance formula: √(Δx² + Δy² + Δz²)

### 4. Kruskal's Algorithm

This is a greedy algorithm for finding a Minimum Spanning Tree:
1. Sort all edges by weight (distance)
2. Add edges one by one, skipping those that would create a cycle
3. Stop when all vertices are connected (or after k edges for Part 1)

Perfect for this problem because we want to minimize total cable length while connecting boxes.

## Complexity Analysis

### Part 1 & Part 2

**Time Complexity:**
- **Parsing**: O(n) where n is number of junction boxes
- **Edge generation**: O(n²) - creating all possible pairs
- **Edge sorting**: O(n² log n²) = O(n² log n) - PriorityQueue operations
- **Union-Find operations**: O(n² × α(n)) ≈ O(n²) where α is the inverse Ackermann function (effectively constant)
- **Overall**: **O(n² log n)**

**Space Complexity:**
- **Points storage**: O(n)
- **Edges storage**: O(n²) - storing all possible edges
- **Union-Find**: O(n)
- **Overall**: **O(n²)**

### Why Not Use Distance Matrix?

We could pre-compute a distance matrix, but:
- Matrix would be O(n²) space anyway
- PriorityQueue gives us sorted edges efficiently
- We only process edges we need (early termination in Part 2)

## Final Answers

- **Part 1**: `96672` (product of three largest circuit sizes after 1000 connection attempts)
- **Part 2**: `22517595` (product of X coordinates of last connection to unite all boxes)

## Algorithm Visualization

### Part 1 Process (Example with 20 boxes, 10 attempts):

```
Initial state: 20 separate circuits
Attempt 1: Connect boxes 0 ↔ 19 → Success → 19 circuits (one of size 2)
Attempt 2: Connect boxes 0 ↔ 7  → Success → 18 circuits (one of size 3)
Attempt 3: Connect boxes 2 ↔ 13 → Success → 17 circuits (two of size 2)
Attempt 4: Connect boxes 17 ↔ 18 → Success → 16 circuits
...
Attempt N: Connect boxes already connected → Fail (still counts)
...
After 10 attempts: 11 circuits with sizes [5, 4, 2, 2, 1, 1, 1, 1, 1, 1, 1]
Result: 5 × 4 × 2 = 40
```

### Part 2 Process:

```
Continue connecting until size reaches n (all boxes in one circuit)
Last successful connection determines the answer
Product of X coordinates of those two boxes
```

The Union-Find data structure makes this incredibly efficient, allowing us to quickly determine if two boxes are already connected and merge components in nearly constant time!
