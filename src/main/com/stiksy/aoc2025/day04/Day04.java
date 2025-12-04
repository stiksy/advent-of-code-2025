package com.stiksy.aoc2025.day04;

import com.stiksy.aoc2025.util.InputReader;

import java.util.List;

public class Day04 {

    public static void main(String[] args) {
        Day04 solution = new Day04();
        List<String> input = InputReader.readLines("day04/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

    public long solvePart1(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();
        int accessibleCount = 0;

        // Check each position in the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Only check positions that have a roll (@)
                if (input.get(row).charAt(col) == '@') {
                    int adjacentRolls = countAdjacentRolls(input, row, col, rows, cols);
                    // Accessible if fewer than 4 adjacent rolls
                    if (adjacentRolls < 4) {
                        accessibleCount++;
                    }
                }
            }
        }

        return accessibleCount;
    }

    private int countAdjacentRolls(List<String> grid, int row, int col, int rows, int cols) {
        int count = 0;

        // Check all 8 adjacent positions
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},  // Top row
            {0, -1},           {0, 1},    // Left and right
            {1, -1},  {1, 0},  {1, 1}     // Bottom row
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check bounds and if position has a roll
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                if (grid.get(newRow).charAt(newCol) == '@') {
                    count++;
                }
            }
        }

        return count;
    }

    public long solvePart2(List<String> input) {
        // Create a mutable copy of the grid
        int rows = input.size();
        int cols = input.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            grid[row] = input.get(row).toCharArray();
        }

        int totalRemoved = 0;
        boolean removed = true;

        // Keep removing accessible rolls until no more can be removed
        while (removed) {
            removed = false;

            // Find all accessible rolls in this iteration
            boolean[][] toRemove = new boolean[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (grid[row][col] == '@') {
                        int adjacentRolls = countAdjacentRolls(grid, row, col, rows, cols);
                        if (adjacentRolls < 4) {
                            toRemove[row][col] = true;
                            removed = true;
                        }
                    }
                }
            }

            // Remove all accessible rolls found in this iteration
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (toRemove[row][col]) {
                        grid[row][col] = '.';
                        totalRemoved++;
                    }
                }
            }
        }

        return totalRemoved;
    }

    private int countAdjacentRolls(char[][] grid, int row, int col, int rows, int cols) {
        int count = 0;

        // Check all 8 adjacent positions
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},  // Top row
            {0, -1},           {0, 1},    // Left and right
            {1, -1},  {1, 0},  {1, 1}     // Bottom row
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check bounds and if position has a roll
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                if (grid[newRow][newCol] == '@') {
                    count++;
                }
            }
        }

        return count;
    }
}
