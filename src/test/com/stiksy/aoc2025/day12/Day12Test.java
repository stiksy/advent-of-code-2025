package com.stiksy.aoc2025.day12;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day12Test {

    @Test
    public void testPart1Example() {
        List<String> input = Arrays.asList(
            "0:",
            "###",
            "##.",
            "##.",
            "",
            "1:",
            "###",
            "##.",
            ".##",
            "",
            "2:",
            ".##",
            "###",
            "##.",
            "",
            "3:",
            "##.",
            "###",
            "##.",
            "",
            "4:",
            "###",
            "#..",
            "###",
            "",
            "5:",
            "###",
            ".#.",
            "###",
            "",
            "4x4: 0 0 0 0 2 0",
            "12x5: 1 0 1 0 2 2",
            "12x5: 1 0 1 0 3 2"
        );
        Day12 solution = new Day12();
        long result = solution.solvePart1(input);
        assertEquals(2, result);
    }

    @Test
    public void testPart1Actual() {
        Day12 solution = new Day12();
        List<String> input = InputReader.readLines("day12/input.txt");
        long result = solution.solvePart1(input);
        assertEquals(577, result);
    }
}
