package com.stiksy.aoc2025.day02;

import com.stiksy.aoc2025.util.InputReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Day02Test {

    @Test
    public void testIsInvalidId() {
        Day02 solution = new Day02();

        // Valid invalid IDs (repeated sequences)
        assertTrue(solution.isInvalidId(11));      // 1 twice
        assertTrue(solution.isInvalidId(22));      // 2 twice
        assertTrue(solution.isInvalidId(55));      // 5 twice
        assertTrue(solution.isInvalidId(99));      // 9 twice
        assertTrue(solution.isInvalidId(6464));    // 64 twice
        assertTrue(solution.isInvalidId(123123));  // 123 twice
        assertTrue(solution.isInvalidId(1010));    // 10 twice
        assertTrue(solution.isInvalidId(222222));  // 222 twice
        assertTrue(solution.isInvalidId(446446));  // 446 twice
        assertTrue(solution.isInvalidId(1188511885)); // 11885 twice

        // Not invalid IDs
        assertFalse(solution.isInvalidId(101));    // Odd length
        assertFalse(solution.isInvalidId(12));     // Different digits
        assertFalse(solution.isInvalidId(1234));   // Different halves
        assertFalse(solution.isInvalidId(1698525)); // Not a repeated pattern
    }

    @Test
    public void testIsInvalidIdEdgeCases() {
        Day02 solution = new Day02();

        // Test the specific case from the problem
        assertTrue(solution.isInvalidId(38593859)); // 3859 twice
    }

    @Test
    public void testExamplePart1() {
        Day02 solution = new Day02();

        List<String> input = Arrays.asList(
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,",
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659,",
            "824824821-824824827,2121212118-2121212124"
        );

        long result = solution.solvePart1(input);
        assertEquals(1227775554L, result);
    }

    @Test
    public void testPart1Actual() {
        Day02 solution = new Day02();
        List<String> input = InputReader.readLines("day02/input.txt");
        long result = solution.solvePart1(input);
        assertEquals(54234399924L, result);
    }

    @Test
    public void testIsInvalidIdPart2() {
        Day02 solution = new Day02();

        // Repeated patterns - all should be invalid
        assertTrue(solution.isInvalidIdPart2(11));      // 1 twice
        assertTrue(solution.isInvalidIdPart2(111));     // 1 three times
        assertTrue(solution.isInvalidIdPart2(1111111)); // 1 seven times
        assertTrue(solution.isInvalidIdPart2(99));      // 9 twice
        assertTrue(solution.isInvalidIdPart2(6464));    // 64 twice
        assertTrue(solution.isInvalidIdPart2(123123));  // 123 twice
        assertTrue(solution.isInvalidIdPart2(12341234));   // 1234 twice
        assertTrue(solution.isInvalidIdPart2(123123123));  // 123 three times
        assertTrue(solution.isInvalidIdPart2(1212121212L)); // 12 five times
        assertTrue(solution.isInvalidIdPart2(565656));     // 56 three times
        assertTrue(solution.isInvalidIdPart2(824824824));  // 824 three times
        assertTrue(solution.isInvalidIdPart2(2121212121L)); // 21 five times

        // Not repeated patterns
        assertFalse(solution.isInvalidIdPart2(101));    // Not a repeated pattern
        assertFalse(solution.isInvalidIdPart2(1234));   // Not a repeated pattern
        assertFalse(solution.isInvalidIdPart2(1698525)); // Not a repeated pattern
    }

    @Test
    public void testExamplePart2() {
        Day02 solution = new Day02();

        List<String> input = Arrays.asList(
            "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,",
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659,",
            "824824821-824824827,2121212118-2121212124"
        );

        long result = solution.solvePart2(input);
        assertEquals(4174379265L, result);
    }

    @Test
    public void testPart2Actual() {
        Day02 solution = new Day02();
        List<String> input = InputReader.readLines("day02/input.txt");
        long result = solution.solvePart2(input);
        assertEquals(70187097315L, result);
    }
}
