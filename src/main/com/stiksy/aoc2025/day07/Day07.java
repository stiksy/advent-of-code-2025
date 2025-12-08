package com.stiksy.aoc2025.day07;

import com.stiksy.aoc2025.util.InputReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day07 {

    public static void main(String[] args) {
        Day07 solution = new Day07();
        List<String> input = InputReader.readLines("day07/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

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
        // State: (row, col, direction)
        // Direction: 0=down (only direction beams move)
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
            if (row < 0 || row >= input.size() || col < 0 || col >= input.get(0).length()) {
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
                // Create two new beams: one to the left, one to the right
                queue.add(new int[]{row + 1, col - 1});
                queue.add(new int[]{row + 1, col + 1});
            } else {
                // Empty space, continue downward
                queue.add(new int[]{row + 1, col});
            }
        }

        return splitCount;
    }

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

    private long countPathsMemo(List<String> input, int row, int col, Map<String, Long> memo) {
        // Check bounds - if we exit the manifold, this is one timeline
        if (row < 0 || row >= input.size() || col < 0 || col >= input.get(0).length()) {
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
            // Splitter: particle takes both paths (many-worlds interpretation)
            pathCount += countPathsMemo(input, row + 1, col - 1, memo);
            pathCount += countPathsMemo(input, row + 1, col + 1, memo);
        } else {
            // Empty space, continue downward
            pathCount += countPathsMemo(input, row + 1, col, memo);
        }

        memo.put(state, pathCount);
        return pathCount;
    }
}
