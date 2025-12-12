package com.stiksy.aoc2025.day11;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day11Test {

    @Test
    public void testPart1Example() {
        List<String> input = Arrays.asList(
            "aaa: you hhh",
            "you: bbb ccc",
            "bbb: ddd eee",
            "ccc: ddd eee fff",
            "ddd: ggg",
            "eee: out",
            "fff: out",
            "ggg: out",
            "hhh: ccc fff iii",
            "iii: out"
        );
        Day11 solution = new Day11();
        long result = solution.solvePart1(input);
        assertEquals(5, result);
    }

    @Test
    public void testPart1Actual() {
        Day11 solution = new Day11();
        List<String> input = InputReader.readLines("day11/input.txt");
        long result = solution.solvePart1(input);
        assertEquals(674, result);
    }

    @Test
    public void testPart2Example() {
        List<String> input = Arrays.asList(
            "svr: aaa bbb",
            "aaa: fft",
            "fft: ccc",
            "bbb: tty",
            "tty: ccc",
            "ccc: ddd eee",
            "ddd: hub",
            "hub: fff",
            "eee: dac",
            "dac: fff",
            "fff: ggg hhh",
            "ggg: out",
            "hhh: out"
        );
        Day11 solution = new Day11();
        long result = solution.solvePart2(input);
        assertEquals(2, result);
    }

    @Test
    public void testPart2Actual() {
        Day11 solution = new Day11();
        List<String> input = InputReader.readLines("day11/input.txt");
        long result = solution.solvePart2(input);
        assertEquals(438314708837664L, result);
    }
}
