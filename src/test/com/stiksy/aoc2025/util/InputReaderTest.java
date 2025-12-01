package com.stiksy.aoc2025.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class InputReaderTest {

    @Test
    public void testReadLines() {
        List<String> lines = InputReader.readLines("util/test_lines.txt");

        assertNotNull(lines);
        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
        assertEquals("line3", lines.get(2));
    }

    @Test
    public void testReadLinesWithEmptyLines() {
        List<String> lines = InputReader.readLines("util/test_with_empty_lines.txt");

        assertNotNull(lines);
        assertEquals(5, lines.size());
        assertEquals("first", lines.get(0));
        assertEquals("", lines.get(1));
        assertEquals("second", lines.get(2));
        assertEquals("", lines.get(3));
        assertEquals("third", lines.get(4));
    }

    @Test
    public void testReadNonEmptyLines() {
        List<String> lines = InputReader.readNonEmptyLines("util/test_with_empty_lines.txt");

        assertNotNull(lines);
        assertEquals(3, lines.size());
        assertEquals("first", lines.get(0));
        assertEquals("second", lines.get(1));
        assertEquals("third", lines.get(2));
    }

    @Test
    public void testReadFullFile() {
        String content = InputReader.readFullFile("util/test_lines.txt");

        assertNotNull(content);
        assertEquals("line1\nline2\nline3", content);
    }

    @Test
    public void testReadIntegers() {
        List<Integer> numbers = InputReader.readIntegers("util/test_integers.txt");

        assertNotNull(numbers);
        assertEquals(5, numbers.size());
        assertEquals(Integer.valueOf(1), numbers.get(0));
        assertEquals(Integer.valueOf(2), numbers.get(1));
        assertEquals(Integer.valueOf(3), numbers.get(2));
        assertEquals(Integer.valueOf(42), numbers.get(3));
        assertEquals(Integer.valueOf(100), numbers.get(4));
    }

    @Test
    public void testReadLongs() {
        List<Long> numbers = InputReader.readLongs("util/test_longs.txt");

        assertNotNull(numbers);
        assertEquals(3, numbers.size());
        assertEquals(Long.valueOf(1234567890123L), numbers.get(0));
        assertEquals(Long.valueOf(9876543210987L), numbers.get(1));
        assertEquals(Long.valueOf(42L), numbers.get(2));
    }

    @Test
    public void testReadNonExistentFile() {
        List<String> lines = InputReader.readLines("util/nonexistent.txt");

        assertNotNull(lines);
        assertTrue(lines.isEmpty());
    }
}
