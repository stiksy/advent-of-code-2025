package com.stiksy.aoc2025.day09;

import com.stiksy.aoc2025.util.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day09 {

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    public static void main(String[] args) {
        Day09 solution = new Day09();
        List<String> input = InputReader.readLines("day09/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

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

    static class Rectangle {
        Point p1, p2;
        long area;
        int minX, maxX, minY, maxY;

        Rectangle(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.minX = Math.min(p1.x, p2.x);
            this.maxX = Math.max(p1.x, p2.x);
            this.minY = Math.min(p1.y, p2.y);
            this.maxY = Math.max(p1.y, p2.y);
            this.area = (long)(maxX - minX + 1) * (maxY - minY + 1);
        }
    }

    public long solvePart2(List<String> input) {
        List<Point> redTiles = parseInput(input);

        // Generate all possible rectangles and sort by area (largest first)
        List<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < redTiles.size(); i++) {
            for (int j = i + 1; j < redTiles.size(); j++) {
                rectangles.add(new Rectangle(redTiles.get(i), redTiles.get(j)));
            }
        }
        rectangles.sort((a, b) -> Long.compare(b.area, a.area));

        // Check rectangles from largest to smallest
        for (Rectangle rect : rectangles) {
            if (isValidRectangleEfficient(rect, redTiles)) {
                return rect.area;
            }
        }

        return 0;
    }

    private Set<Point> buildEdgeTileSet(List<Point> redTiles) {
        Set<Point> edgeTiles = new HashSet<>();

        // Add all red tiles
        edgeTiles.addAll(redTiles);

        // Add green tiles connecting consecutive red tiles
        for (int i = 0; i < redTiles.size(); i++) {
            Point current = redTiles.get(i);
            Point next = redTiles.get((i + 1) % redTiles.size());

            // Add all tiles along the straight line between current and next
            if (current.x == next.x) {
                // Vertical line
                int minY = Math.min(current.y, next.y);
                int maxY = Math.max(current.y, next.y);
                for (int y = minY; y <= maxY; y++) {
                    edgeTiles.add(new Point(current.x, y));
                }
            } else if (current.y == next.y) {
                // Horizontal line
                int minX = Math.min(current.x, next.x);
                int maxX = Math.max(current.x, next.x);
                for (int x = minX; x <= maxX; x++) {
                    edgeTiles.add(new Point(x, current.y));
                }
            }
        }

        return edgeTiles;
    }

    private boolean isInsidePolygon(Point p, List<Point> polygon) {
        // Ray casting algorithm: count intersections with edges going right from point
        int intersections = 0;

        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            // Check if ray from p going right intersects edge p1-p2
            if (p1.y == p2.y) {
                // Horizontal edge - skip or handle specially
                continue;
            }

            // Ensure p1.y < p2.y
            if (p1.y > p2.y) {
                Point temp = p1;
                p1 = p2;
                p2 = temp;
            }

            // Check if point is in y-range of edge
            if (p.y < p1.y || p.y >= p2.y) {
                continue;
            }

            // Calculate x-coordinate of intersection
            double xIntersect = p1.x + (double)(p.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y);

            if (p.x < xIntersect) {
                intersections++;
            }
        }

        return intersections % 2 == 1;
    }

    private boolean isValidRectangleEfficient(Rectangle rect, List<Point> redTiles) {
        // Check if any red tile (except corners) is inside the rectangle
        // or if any edge of the polygon passes through the rectangle
        for (int i = 0; i < redTiles.size(); i++) {
            Point current = redTiles.get(i);
            Point next = redTiles.get((i + 1) % redTiles.size());

            // Check if red tile is strictly inside rectangle (not on the boundary)
            if (current.x > rect.minX && current.x < rect.maxX &&
                current.y > rect.minY && current.y < rect.maxY) {
                return false;
            }

            // Check if horizontal edge passes through rectangle
            if (current.y == next.y) {
                int minX = Math.min(current.x, next.x);
                int maxX = Math.max(current.x, next.x);
                // Edge passes through if it spans the rectangle horizontally
                // and is strictly between top and bottom
                if (minX <= rect.minX && maxX >= rect.maxX &&
                    current.y > rect.minY && current.y < rect.maxY) {
                    return false;
                }
            }

            // Check if vertical edge passes through rectangle
            if (current.x == next.x) {
                int minY = Math.min(current.y, next.y);
                int maxY = Math.max(current.y, next.y);
                // Edge passes through if it spans the rectangle vertically
                // and is strictly between left and right
                if (minY <= rect.minY && maxY >= rect.maxY &&
                    current.x > rect.minX && current.x < rect.maxX) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Point> parseInput(List<String> input) {
        List<Point> points = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            points.add(new Point(x, y));
        }
        return points;
    }
}
