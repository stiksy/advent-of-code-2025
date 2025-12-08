package com.stiksy.aoc2025.day08;

import com.stiksy.aoc2025.util.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Day08 {

    public static void main(String[] args) {
        Day08 solution = new Day08();
        List<String> input = InputReader.readLines("day08/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

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

        Map<Integer, Integer> getComponentSizes() {
            Map<Integer, Integer> componentSizes = new HashMap<>();
            for (int i = 0; i < parent.length; i++) {
                int root = find(i);
                componentSizes.put(root, size[root]);
            }
            return componentSizes;
        }
    }

    public long solvePart1(List<String> input) {
        return solvePart1(input, 1000);
    }

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
            uf.union(edge.from, edge.to); // Try to connect (may or may not succeed)
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
}
