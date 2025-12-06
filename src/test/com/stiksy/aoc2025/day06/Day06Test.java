package com.stiksy.aoc2025.day06;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day06Test {

    @Test
    public void testPart1Example() {
        Day06 solution = new Day06();
        List<String> exampleInput = Arrays.asList(
            "123 328  51 64 ",
            " 45 64  387 23 ",
            "  6 98  215 314",
            "*   +   *   +  "
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(4277556, result);
    }

    @Test
    public void testPart2Example() {
        Day06 solution = new Day06();
        List<String> exampleInput = Arrays.asList(
            "123 328  51 64 ",
            " 45 64  387 23 ",
            "  6 98  215 314",
            "*   +   *   +  "
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(3263827, result);
    }

    @Test
    public void testPart1Actual() {
        Day06 solution = new Day06();
        List<String> input = InputReader.readLines("day06/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(5552221122013L, result);
    }

    @Test
    public void testPart2Actual() {
        Day06 solution = new Day06();
        List<String> input = InputReader.readLines("day06/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(11371597126232L, result);
    }
}
