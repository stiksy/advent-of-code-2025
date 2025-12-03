package com.stiksy.aoc2025.day03;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Day03Test {

    @Test
    public void testPart1Example() {
        Day03 solution = new Day03();
        List<String> exampleInput = Arrays.asList(
            "987654321111111",
            "811111111111119",
            "234234234234278",
            "818181911112111"
        );

        long result = solution.solvePart1(exampleInput);
        assertEquals(357, result);
    }

    @Test
    public void testIndividualBanks() {
        Day03 solution = new Day03();

        // Test individual bank max joltages
        List<String> bank1 = Arrays.asList("987654321111111");
        assertEquals(98, solution.solvePart1(bank1));

        List<String> bank2 = Arrays.asList("811111111111119");
        assertEquals(89, solution.solvePart1(bank2));

        List<String> bank3 = Arrays.asList("234234234234278");
        assertEquals(78, solution.solvePart1(bank3));

        List<String> bank4 = Arrays.asList("818181911112111");
        assertEquals(92, solution.solvePart1(bank4));
    }

    @Test
    public void testPart1Actual() {
        Day03 solution = new Day03();
        List<String> input = InputReader.readLines("day03/input.txt");

        long result = solution.solvePart1(input);
        assertEquals(17330, result);
    }

    @Test
    public void testPart2Example() {
        Day03 solution = new Day03();
        List<String> exampleInput = Arrays.asList(
            "987654321111111",
            "811111111111119",
            "234234234234278",
            "818181911112111"
        );

        long result = solution.solvePart2(exampleInput);
        assertEquals(3121910778619L, result);
    }

    @Test
    public void testPart2IndividualBanks() {
        Day03 solution = new Day03();

        // Test individual bank max joltages with 12 batteries
        List<String> bank1 = Arrays.asList("987654321111111");
        assertEquals(987654321111L, solution.solvePart2(bank1));

        List<String> bank2 = Arrays.asList("811111111111119");
        assertEquals(811111111119L, solution.solvePart2(bank2));

        List<String> bank3 = Arrays.asList("234234234234278");
        assertEquals(434234234278L, solution.solvePart2(bank3));

        List<String> bank4 = Arrays.asList("818181911112111");
        assertEquals(888911112111L, solution.solvePart2(bank4));
    }

    @Test
    public void testPart2Actual() {
        Day03 solution = new Day03();
        List<String> input = InputReader.readLines("day03/input.txt");

        long result = solution.solvePart2(input);
        assertEquals(171518260283767L, result);
    }
}
