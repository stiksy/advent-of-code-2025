package com.stiksy.aoc2025.day08;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day08Test {

    @Test
    public void testPart1Example() {
        Day08 solution = new Day08();
        List<String> exampleInput = Arrays.asList(
            "162,817,812",
            "57,618,57",
            "906,360,560",
            "592,479,940",
            "352,342,300",
            "466,668,158",
            "542,29,236",
            "431,825,988",
            "739,650,466",
            "52,470,668",
            "216,146,977",
            "819,987,18",
            "117,168,530",
            "805,96,715",
            "346,949,466",
            "970,615,88",
            "941,993,340",
            "862,61,35",
            "984,92,344",
            "425,690,689"
        );

        // After 10 shortest connections, multiply 3 largest circuit sizes
        long result = solution.solvePart1(exampleInput, 10);
        assertEquals(40, result);
    }

    @Test
    public void testPart1Actual() {
        Day08 solution = new Day08();
        List<String> input = InputReader.readLines("day08/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(96672, result);
    }

    @Test
    public void testPart2Example() {
        Day08 solution = new Day08();
        List<String> exampleInput = Arrays.asList(
            "162,817,812",
            "57,618,57",
            "906,360,560",
            "592,479,940",
            "352,342,300",
            "466,668,158",
            "542,29,236",
            "431,825,988",
            "739,650,466",
            "52,470,668",
            "216,146,977",
            "819,987,18",
            "117,168,530",
            "805,96,715",
            "346,949,466",
            "970,615,88",
            "941,993,340",
            "862,61,35",
            "984,92,344",
            "425,690,689"
        );

        // Last connection is between boxes at 216,146,977 and 117,168,530
        // Multiply X coordinates: 216 * 117 = 25272
        long result = solution.solvePart2(exampleInput);
        assertEquals(25272, result);
    }

    @Test
    public void testPart2Actual() {
        Day08 solution = new Day08();
        List<String> input = InputReader.readLines("day08/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(22517595, result);
    }
}
