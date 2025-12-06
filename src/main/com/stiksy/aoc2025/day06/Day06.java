package com.stiksy.aoc2025.day06;

import com.stiksy.aoc2025.util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day06 {

    public static void main(String[] args) {
        Day06 solution = new Day06();
        List<String> input = InputReader.readLines("day06/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }

    public long solvePart1(List<String> input) {
        List<Problem> problems = parseProblems(input);
        long grandTotal = 0;

        for (Problem problem : problems) {
            grandTotal += problem.solve();
        }

        return grandTotal;
    }

    public long solvePart2(List<String> input) {
        List<Problem> problems = parseProblemsPart2(input);
        long grandTotal = 0;

        for (Problem problem : problems) {
            long result = problem.solve();
            // System.out.println("Problem: " + problem.numbers + " " + problem.operator + " = " + result);
            grandTotal += result;
        }

        return grandTotal;
    }

    private List<Problem> parseProblemsPart2(List<String> input) {
        List<Problem> problems = new ArrayList<>();

        if (input.isEmpty()) {
            return problems;
        }

        // Use Part 1 parsing to identify problem column ranges
        // But parse numbers within each range using cephalopod math rules

        String operatorRow = input.get(input.size() - 1);

        int col = 0;
        while (col < operatorRow.length()) {
            // Skip space columns
            while (col < operatorRow.length() && operatorRow.charAt(col) == ' ') {
                col++;
            }

            if (col >= operatorRow.length()) {
                break;
            }

            // Found an operator - identify the problem's column range
            char operator = operatorRow.charAt(col);

            // Find the start of this number group (leftmost column with non-space in any row)
            int problemStart = col;
            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (col < line.length() && line.charAt(col) != ' ') {
                    // Find the left edge of this number
                    int numStart = col;
                    while (numStart > 0 && line.charAt(numStart - 1) != ' ') {
                        numStart--;
                    }
                    problemStart = Math.min(problemStart, numStart);
                }
            }

            // Find the end of this number group
            int problemEnd = col;
            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (col < line.length() && line.charAt(col) != ' ') {
                    // Find the right edge of this number
                    int numEnd = col;
                    while (numEnd < line.length() && line.charAt(numEnd) != ' ') {
                        numEnd++;
                    }
                    problemEnd = Math.max(problemEnd, numEnd);
                }
            }

            // Parse numbers using cephalopod math (right-to-left, column-based)
            List<Long> numbers = parseNumbersRightToLeft(input, problemStart, problemEnd);

            if (!numbers.isEmpty()) {
                problems.add(new Problem(numbers, operator));
            }

            // Move to next problem
            col = problemEnd;
        }

        return problems;
    }

    private List<Long> parseNumbersRightToLeft(List<String> input, int start, int end) {
        // In cephalopod math:
        // - We read columns RIGHT-TO-LEFT
        // - Each COLUMN represents ONE NUMBER
        // - Within a column, digits go TOP-TO-BOTTOM (MSD at top, LSD at bottom)

        List<Long> numbers = new ArrayList<>();

        // Process columns from right to left
        for (int col = end - 1; col >= start; col--) {
            // Build one number from this column by reading digits top-to-bottom
            long number = 0;
            boolean foundDigit = false;

            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (col < line.length()) {
                    char ch = line.charAt(col);
                    if (ch != ' ' && Character.isDigit(ch)) {
                        // Reading top-to-bottom: first digit is MSD
                        number = number * 10 + (ch - '0');
                        foundDigit = true;
                    }
                }
            }

            if (foundDigit) {
                numbers.add(number);
            }
        }

        return numbers;
    }

    private List<Problem> parseProblems(List<String> input) {
        List<Problem> problems = new ArrayList<>();

        if (input.isEmpty()) {
            return problems;
        }

        // Find the operator row (last row)
        String operatorRow = input.get(input.size() - 1);

        // Split the worksheet into problems based on column spacing
        // A problem is a vertical column of numbers ending with an operator
        // Problems are separated by at least one full column of spaces

        int col = 0;
        while (col < operatorRow.length()) {
            // Skip space columns
            while (col < operatorRow.length() && operatorRow.charAt(col) == ' ') {
                col++;
            }

            if (col >= operatorRow.length()) {
                break;
            }

            // Found an operator - identify the problem's column range
            char operator = operatorRow.charAt(col);

            // Find the start of this number group (leftmost column with non-space in any row)
            int problemStart = col;
            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (col < line.length() && line.charAt(col) != ' ') {
                    // Find the left edge of this number
                    int numStart = col;
                    while (numStart > 0 && line.charAt(numStart - 1) != ' ') {
                        numStart--;
                    }
                    problemStart = Math.min(problemStart, numStart);
                }
            }

            // Find the end of this number group
            int problemEnd = col;
            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (col < line.length() && line.charAt(col) != ' ') {
                    // Find the right edge of this number
                    int numEnd = col;
                    while (numEnd < line.length() && line.charAt(numEnd) != ' ') {
                        numEnd++;
                    }
                    problemEnd = Math.max(problemEnd, numEnd);
                }
            }

            // Extract all numbers in this problem's column range
            List<Long> numbers = new ArrayList<>();
            for (int row = 0; row < input.size() - 1; row++) {
                String line = input.get(row);
                if (problemStart < line.length()) {
                    String segment = line.substring(problemStart, Math.min(problemEnd, line.length())).trim();
                    if (!segment.isEmpty()) {
                        numbers.add(Long.parseLong(segment));
                    }
                }
            }

            if (!numbers.isEmpty()) {
                problems.add(new Problem(numbers, operator));
            }

            // Move to next problem
            col = problemEnd;
        }

        return problems;
    }

    private String extractNumberAtColumn(String line, int col) {
        if (col >= line.length()) {
            return "";
        }

        // If current position is a space, no number here
        if (line.charAt(col) == ' ') {
            return "";
        }

        // Find the start of the number (go left until space or start)
        int start = col;
        while (start > 0 && line.charAt(start - 1) != ' ') {
            start--;
        }

        // Find the end of the number (go right until space or end)
        int end = col;
        while (end < line.length() && line.charAt(end) != ' ') {
            end++;
        }

        return line.substring(start, end).trim();
    }

    static class Problem {
        private final List<Long> numbers;
        private final char operator;

        public Problem(List<Long> numbers, char operator) {
            this.numbers = numbers;
            this.operator = operator;
        }

        public long solve() {
            if (numbers.isEmpty()) {
                return 0;
            }

            long result = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                if (operator == '+') {
                    result += numbers.get(i);
                } else if (operator == '*') {
                    result *= numbers.get(i);
                }
            }

            return result;
        }
    }
}
