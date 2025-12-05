package com.stiksy.aoc2025.day05;

import com.stiksy.aoc2025.util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day05 {

    public static void main(String[] args) {
        Day05 solution = new Day05();
        List<String> input = InputReader.readLines("day05/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

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

        // Count how many ingredient IDs are fresh (fall within any range)
        long freshCount = 0;
        for (long id : ingredientIds) {
            if (isFresh(id, ranges)) {
                freshCount++;
            }
        }

        return freshCount;
    }

    public long solvePart2(List<String> input) {
        // Parse only the ranges (ignore ingredient IDs after blank line)
        List<Range> ranges = new ArrayList<>();

        for (String line : input) {
            if (line.trim().isEmpty()) {
                break; // Stop at blank line
            }

            // Parse range like "3-5"
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new Range(start, end));
        }

        // Merge overlapping ranges and count total IDs
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

    private boolean isFresh(long id, List<Range> ranges) {
        for (Range range : ranges) {
            if (id >= range.start && id <= range.end) {
                return true;
            }
        }
        return false;
    }

    private static class Range {
        final long start;
        final long end;

        Range(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
