package com.stiksy.aoc2025.day09;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day09Test {

    @Test
    public void testPart1Example() {
        Day09 solution = new Day09();
        List<String> exampleInput = Arrays.asList(
            "7,1",
            "11,1",
            "11,7",
            "9,7",
            "9,5",
            "2,5",
            "2,3",
            "7,3"
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(50, result);
    }

    @Test
    public void testPart2Example() {
        Day09 solution = new Day09();
        List<String> exampleInput = Arrays.asList(
            "7,1",
            "11,1",
            "11,7",
            "9,7",
            "9,5",
            "2,5",
            "2,3",
            "7,3"
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(24, result);
    }

    @Test
    public void testPart1Actual() {
        Day09 solution = new Day09();
        List<String> input = InputReader.readLines("day09/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(4781546175L, result);
    }

    @Test
    public void testPart2Actual() {
        Day09 solution = new Day09();
        List<String> input = InputReader.readLines("day09/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(1573359081L, result);
    }
}
