package com.stiksy.aoc2025.day07;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day07Test {

    @Test
    public void testPart1Example() {
        Day07 solution = new Day07();
        List<String> exampleInput = Arrays.asList(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(21, result);
    }

    @Test
    public void testPart1Actual() {
        Day07 solution = new Day07();
        List<String> input = InputReader.readLines("day07/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(1570, result);
    }

    @Test
    public void testPart2Example() {
        Day07 solution = new Day07();
        List<String> exampleInput = Arrays.asList(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(40, result);
    }

    @Test
    public void testPart2Actual() {
        Day07 solution = new Day07();
        List<String> input = InputReader.readLines("day07/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(15118009521693L, result);
    }
}
