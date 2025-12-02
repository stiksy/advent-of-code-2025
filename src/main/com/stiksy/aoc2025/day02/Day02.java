package com.stiksy.aoc2025.day02;

import com.stiksy.aoc2025.util.InputReader;

import java.util.List;

public class Day02 {

    public static void main(String[] args) {
        Day02 solution = new Day02();
        List<String> input = InputReader.readLines("day02/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

    public long solvePart1(List<String> input) {
        String rangesLine = String.join("", input).trim();
        String[] ranges = rangesLine.split(",");

        long totalInvalidIds = 0;

        for (String range : ranges) {
            range = range.trim();
            if (range.isEmpty()) continue;

            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            for (long id = start; id <= end; id++) {
                if (isInvalidId(id)) {
                    totalInvalidIds += id;
                }
            }
        }

        return totalInvalidIds;
    }

    public long solvePart2(List<String> input) {
        String rangesLine = String.join("", input).trim();
        String[] ranges = rangesLine.split(",");

        long totalInvalidIds = 0;

        for (String range : ranges) {
            range = range.trim();
            if (range.isEmpty()) continue;

            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            for (long id = start; id <= end; id++) {
                if (isInvalidIdPart2(id)) {
                    totalInvalidIds += id;
                }
            }
        }

        return totalInvalidIds;
    }

    /**
     * Check if an ID is invalid (made of a repeated sequence).
     * An invalid ID is one where the digits form a pattern that repeats exactly twice.
     * Examples: 55 (5 twice), 6464 (64 twice), 123123 (123 twice)
     */
    boolean isInvalidId(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();

        // Must have even length to be splittable into two equal parts
        if (length % 2 != 0) {
            return false;
        }

        int halfLength = length / 2;
        String firstHalf = idStr.substring(0, halfLength);
        String secondHalf = idStr.substring(halfLength);

        return firstHalf.equals(secondHalf);
    }

    /**
     * Check if an ID is invalid for Part 2 (made of a repeated sequence at least twice).
     * An invalid ID is one where the digits form a pattern that repeats at least 2 times.
     * Examples: 55 (5 twice), 111 (1 three times), 12341234 (1234 twice), 123123123 (123 three times)
     */
    boolean isInvalidIdPart2(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();

        // Try all possible pattern lengths from 1 to length/2
        for (int patternLength = 1; patternLength <= length / 2; patternLength++) {
            // Check if the total length is divisible by the pattern length
            if (length % patternLength != 0) {
                continue;
            }

            String pattern = idStr.substring(0, patternLength);
            boolean isRepeated = true;

            // Check if the pattern repeats throughout the entire string
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
}
