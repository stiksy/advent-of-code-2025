package com.stiksy.aoc2025.day01;

import com.stiksy.aoc2025.util.InputReader;

import java.util.List;

public class Day01 {

    public static void main(String[] args) {
        Day01 solution = new Day01();
        List<String> input = InputReader.readLines("day01/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

    public long solvePart1(List<String> input) {
        int position = 50;  // Starting position
        int zeroCount = 0;

        for (String rotation : input) {
            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));

            if (direction == 'L') {
                position = (position - distance) % 100;
                if (position < 0) {
                    position += 100;
                }
            } else { // direction == 'R'
                position = (position + distance) % 100;
            }

            if (position == 0) {
                zeroCount++;
            }
        }

        return zeroCount;
    }

    public long solvePart2(List<String> input) {
        int position = 50;  // Starting position
        int zeroCount = 0;

        for (String rotation : input) {
            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));

            // Count complete wraps around the dial (each contributes 1 zero crossing)
            zeroCount += distance / 100;

            // Check if we cross 0 in the partial rotation
            int newPosition;
            if (direction == 'L') {
                newPosition = position - (distance % 100);
                if (newPosition <= 0 && position > 0) {
                    // We crossed or landed on 0
                    zeroCount++;
                }
                // Normalize position
                newPosition = (position - distance) % 100;
                if (newPosition < 0) {
                    newPosition += 100;
                }
            } else { // direction == 'R'
                newPosition = position + (distance % 100);
                if (newPosition >= 100) {
                    // We crossed 0 by wrapping around
                    zeroCount++;
                }
                // Normalize position
                newPosition = (position + distance) % 100;
            }

            position = newPosition;
        }

        return zeroCount;
    }
}
