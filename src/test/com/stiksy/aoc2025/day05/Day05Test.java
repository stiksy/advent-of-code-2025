package com.stiksy.aoc2025.day05;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day05Test {

    @Test
    public void testPart1Example() {
        Day05 solution = new Day05();
        List<String> exampleInput = Arrays.asList(
            "3-5",
            "10-14",
            "16-20",
            "12-18",
            "",
            "1",
            "5",
            "8",
            "11",
            "17",
            "32"
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(3, result);
    }

    @Test
    public void testPart2Example() {
        Day05 solution = new Day05();
        List<String> exampleInput = Arrays.asList(
            "3-5",
            "10-14",
            "16-20",
            "12-18",
            "",
            "1",
            "5",
            "8",
            "11",
            "17",
            "32"
        );

        long result = solution.solvePart2(exampleInput);
        // Ranges: 3-5, 10-14, 16-20, 12-18
        // After merging: 3-5 (3 IDs), 10-20 (11 IDs) = 14 total
        assertEquals(14, result);
    }

    @Test
    public void testPart1Actual() {
        Day05 solution = new Day05();
        List<String> input = InputReader.readLines("day05/input.txt");
        long result = solution.solvePart1(input);
        assertEquals(694, result);
    }

    @Test
    public void testPart2Actual() {
        Day05 solution = new Day05();
        List<String> input = InputReader.readLines("day05/input.txt");
        long result = solution.solvePart2(input);
        assertEquals(352716206375547L, result);
    }
}
