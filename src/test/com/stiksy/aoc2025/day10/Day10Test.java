package com.stiksy.aoc2025.day10;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Day10Test {

    @Test
    public void testExampleMachine1() {
        String line = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPresses(machine);
        assertEquals(2, result);
    }

    @Test
    public void testExampleMachine2() {
        String line = "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPresses(machine);
        assertEquals(3, result);
    }

    @Test
    public void testExampleMachine3() {
        String line = "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPresses(machine);
        assertEquals(2, result);
    }

    @Test
    public void testPart1Example() {
        List<String> input = Arrays.asList(
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
            "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
            "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        );
        Day10 solution = new Day10();
        long result = solution.solvePart1(input);
        assertEquals(7, result);
    }

    @Test
    public void testPart1Actual() {
        Day10 solution = new Day10();
        List<String> input = InputReader.readLines("day10/input.txt");
        long result = solution.solvePart1(input);
        assertEquals(375, result);
    }

    @Test
    public void testExampleMachine1Joltage() {
        String line = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPressesJoltage(machine);
        assertEquals(10, result);
    }

    @Test
    public void testExampleMachine2Joltage() {
        String line = "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPressesJoltage(machine);
        assertEquals(12, result);
    }

    @Test
    public void testExampleMachine3Joltage() {
        String line = "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}";
        Day10.Machine machine = Day10.parseMachine(line);
        int result = Day10.solveMinButtonPressesJoltage(machine);
        assertEquals(11, result);
    }

    @Test
    public void testPart2Example() {
        List<String> input = Arrays.asList(
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
            "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
            "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        );
        Day10 solution = new Day10();
        long result = solution.solvePart2(input);
        assertEquals(33, result);
    }

    @Test
    public void testPart2Actual() {
        Day10 solution = new Day10();
        List<String> input = InputReader.readLines("day10/input.txt");
        long result = solution.solvePart2(input);
        assertEquals(15377, result);
    }
}
