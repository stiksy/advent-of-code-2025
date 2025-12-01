package com.stiksy.aoc2025.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputReader {

    public static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = InputReader.class.getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading input file: " + filename);
            e.printStackTrace();
        }
        return lines;
    }

    public static String readFullFile(String filename) {
        return readLines(filename).stream()
                .collect(Collectors.joining("\n"));
    }

    public static List<Integer> readIntegers(String filename) {
        return readLines(filename).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static List<Long> readLongs(String filename) {
        return readLines(filename).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public static List<String> readNonEmptyLines(String filename) {
        return readLines(filename).stream()
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList());
    }
}
