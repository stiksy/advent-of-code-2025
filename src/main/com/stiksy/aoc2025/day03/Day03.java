package com.stiksy.aoc2025.day03;

import com.stiksy.aoc2025.util.InputReader;

import java.util.List;

public class Day03 {

    public static void main(String[] args) {
        Day03 solution = new Day03();
        List<String> input = InputReader.readLines("day03/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

    public long solvePart1(List<String> input) {
        long totalJoltage = 0;

        for (String bank : input) {
            long maxJoltage = findMaxJoltage(bank);
            totalJoltage += maxJoltage;
        }

        return totalJoltage;
    }

    private long findMaxJoltage(String bank) {
        // We need to find the largest two-digit number we can form
        // by selecting exactly two batteries (digits) in their original positions

        int maxJoltage = 0;
        int length = bank.length();

        // Try all pairs of positions (i, j) where i < j
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                int digit1 = Character.getNumericValue(bank.charAt(i));
                int digit2 = Character.getNumericValue(bank.charAt(j));
                int joltage = digit1 * 10 + digit2;
                maxJoltage = Math.max(maxJoltage, joltage);
            }
        }

        return maxJoltage;
    }

    public long solvePart2(List<String> input) {
        long totalJoltage = 0;

        for (String bank : input) {
            long maxJoltage = findMaxJoltageWithTwelveBatteries(bank);
            totalJoltage += maxJoltage;
        }

        return totalJoltage;
    }

    private long findMaxJoltageWithTwelveBatteries(String bank) {
        // We need to select exactly 12 digits from the bank to form the largest number
        // Strategy: Greedy approach - at each position, pick the largest digit
        // that still allows us to select enough remaining digits

        int length = bank.length();
        int needed = 12;
        StringBuilder result = new StringBuilder();
        int startIdx = 0;

        for (int i = 0; i < needed; i++) {
            // We need to pick 'needed - i' more digits
            // We can look ahead up to position: length - (needed - i)
            int maxAllowedPos = length - (needed - i);

            // Find the maximum digit in the range [startIdx, maxAllowedPos]
            char maxDigit = '0';
            int maxPos = startIdx;

            for (int j = startIdx; j <= maxAllowedPos; j++) {
                char digit = bank.charAt(j);
                if (digit > maxDigit) {
                    maxDigit = digit;
                    maxPos = j;
                }
            }

            result.append(maxDigit);
            startIdx = maxPos + 1;
        }

        return Long.parseLong(result.toString());
    }
}
