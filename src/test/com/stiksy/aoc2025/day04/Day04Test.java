package com.stiksy.aoc2025.day04;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day04Test {

    @Test
    public void testPart1Example() {
        Day04 solution = new Day04();
        List<String> exampleInput = Arrays.asList(
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(13, result);
    }

    @Test
    public void testPart1Actual() {
        Day04 solution = new Day04();
        List<String> input = InputReader.readLines("day04/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(1553, result);
    }

    @Test
    public void testPart2Example() {
        Day04 solution = new Day04();
        List<String> exampleInput = Arrays.asList(
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(43, result);
    }

    @Test
    public void testPart2Actual() {
        Day04 solution = new Day04();
        List<String> input = InputReader.readLines("day04/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(8442, result);
    }
}
