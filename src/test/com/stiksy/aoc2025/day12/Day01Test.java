package com.stiksy.aoc2025.day01;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day01Test {

    @Test
    public void testPart1Example() {
        Day01 solution = new Day01();
        List<String> exampleInput = Arrays.asList(
            "L68",
            "L30",
            "R48",
            "L5",
            "R60",
            "L55",
            "L1",
            "L99",
            "R14",
            "L82"
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(3, result);
    }

    @Test
    public void testPart2Example() {
        Day01 solution = new Day01();
        List<String> exampleInput = Arrays.asList(
            "L68",
            "L30",
            "R48",
            "L5",
            "R60",
            "L55",
            "L1",
            "L99",
            "R14",
            "L82"
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(6, result);
    }

    @Test
    public void testPart1Actual() {
        Day01 solution = new Day01();
        List<String> input = InputReader.readLines("day01/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(1145, result);
    }

    @Test
    public void testPart2Actual() {
        Day01 solution = new Day01();
        List<String> input = InputReader.readLines("day01/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(6561, result);
    }
}
