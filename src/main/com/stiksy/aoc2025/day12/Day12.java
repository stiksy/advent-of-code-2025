package com.stiksy.aoc2025.day12;

import com.stiksy.aoc2025.util.InputReader;

import java.util.*;

public class Day12 {

    static class Shape {
        List<int[]> cells; // List of (row, col) coordinates

        Shape(List<int[]> cells) {
            this.cells = cells;
        }

        // Generate all unique rotations and reflections
        List<Shape> getAllOrientations() {
            Set<String> seen = new HashSet<>();
            List<Shape> orientations = new ArrayList<>();

            for (int rotation = 0; rotation < 4; rotation++) {
                Shape rotated = rotate(rotation);
                addIfUnique(orientations, seen, rotated);
                addIfUnique(orientations, seen, rotated.flip());
            }

            return orientations;
        }

        private void addIfUnique(List<Shape> list, Set<String> seen, Shape shape) {
            Shape normalized = shape.normalize();
            String key = normalized.toKey();
            if (!seen.contains(key)) {
                seen.add(key);
                list.add(normalized);
            }
        }

        Shape rotate(int times) {
            Shape result = this;
            for (int i = 0; i < times; i++) {
                result = result.rotateOnce();
            }
            return result;
        }

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

        // Normalize to have min row and col at 0
        Shape normalize() {
            if (cells.isEmpty()) return this;

            int minRow = cells.stream().mapToInt(c -> c[0]).min().orElse(0);
            int minCol = cells.stream().mapToInt(c -> c[1]).min().orElse(0);

            List<int[]> newCells = new ArrayList<>();
            for (int[] cell : cells) {
                newCells.add(new int[]{cell[0] - minRow, cell[1] - minCol});
            }
            return new Shape(newCells);
        }

        String toKey() {
            List<String> coords = new ArrayList<>();
            for (int[] cell : cells) {
                coords.add(cell[0] + "," + cell[1]);
            }
            Collections.sort(coords);
            return String.join(";", coords);
        }
    }

    static class Region {
        int width;
        int height;
        int[] presentCounts;

        Region(int width, int height, int[] presentCounts) {
            this.width = width;
            this.height = height;
            this.presentCounts = presentCounts;
        }
    }

    public static List<Shape> parseShapes(List<String> input) {
        List<Shape> shapes = new ArrayList<>();
        int i = 0;

        while (i < input.size()) {
            String line = input.get(i).trim();
            if (line.isEmpty()) {
                i++;
                continue;
            }

            // Check if this is a shape definition (starts with number:)
            if (line.matches("\\d+:")) {
                i++; // Skip the "N:" line
                List<int[]> cells = new ArrayList<>();
                int row = 0;

                // Read shape lines
                while (i < input.size() && !input.get(i).trim().isEmpty() && !input.get(i).contains("x")) {
                    String shapeLine = input.get(i);
                    for (int col = 0; col < shapeLine.length(); col++) {
                        if (shapeLine.charAt(col) == '#') {
                            cells.add(new int[]{row, col});
                        }
                    }
                    row++;
                    i++;
                }
                shapes.add(new Shape(cells).normalize());
            } else {
                break; // Reached region definitions
            }
        }

        return shapes;
    }

    public static List<Region> parseRegions(List<String> input, int numShapes) {
        List<Region> regions = new ArrayList<>();

        for (String line : input) {
            if (line.contains("x") && line.contains(":")) {
                String[] parts = line.split(":");
                String[] dims = parts[0].trim().split("x");
                int width = Integer.parseInt(dims[0]);
                int height = Integer.parseInt(dims[1]);

                String[] counts = parts[1].trim().split("\\s+");
                int[] presentCounts = new int[numShapes];
                for (int i = 0; i < Math.min(counts.length, numShapes); i++) {
                    presentCounts[i] = Integer.parseInt(counts[i]);
                }

                regions.add(new Region(width, height, presentCounts));
            }
        }

        return regions;
    }

    public boolean canFitPresents(Region region, List<Shape> shapes) {
        // Generate all orientations for each shape
        List<List<Shape>> allOrientations = new ArrayList<>();
        for (Shape shape : shapes) {
            allOrientations.add(shape.getAllOrientations());
        }

        // Build list of presents to place
        List<Integer> presentsToPlace = new ArrayList<>();
        for (int shapeIdx = 0; shapeIdx < shapes.size(); shapeIdx++) {
            for (int count = 0; count < region.presentCounts[shapeIdx]; count++) {
                presentsToPlace.add(shapeIdx);
            }
        }

        // Calculate required cells
        int totalCellsNeeded = 0;
        for (int shapeIdx = 0; shapeIdx < shapes.size(); shapeIdx++) {
            totalCellsNeeded += shapes.get(shapeIdx).cells.size() * region.presentCounts[shapeIdx];
        }

        int availableCells = region.width * region.height;
        if (totalCellsNeeded > availableCells) {
            return false; // Not enough space
        }

        // Try to place all presents using backtracking
        boolean[][] grid = new boolean[region.height][region.width];
        return backtrack(grid, presentsToPlace, 0, allOrientations);
    }

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

    private boolean canPlace(boolean[][] grid, Shape shape, int startRow, int startCol) {
        for (int[] cell : shape.cells) {
            int row = startRow + cell[0];
            int col = startCol + cell[1];
            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
                return false;
            }
            if (grid[row][col]) {
                return false;
            }
        }
        return true;
    }

    private void place(boolean[][] grid, Shape shape, int startRow, int startCol, boolean occupy) {
        for (int[] cell : shape.cells) {
            int row = startRow + cell[0];
            int col = startCol + cell[1];
            grid[row][col] = occupy;
        }
    }

    public long solvePart1(List<String> input) {
        List<Shape> shapes = parseShapes(input);
        List<Region> regions = parseRegions(input, shapes.size());

        int count = 0;
        for (Region region : regions) {
            if (canFitPresents(region, shapes)) {
                count++;
            }
        }

        return count;
    }

    public long solvePart2(List<String> input) {
        // Part 2 not yet revealed
        return 0;
    }

    public static void main(String[] args) {
        Day12 solution = new Day12();
        List<String> input = InputReader.readLines("day12/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }
}
