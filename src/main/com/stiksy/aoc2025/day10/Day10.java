package com.stiksy.aoc2025.day10;

import com.stiksy.aoc2025.util.InputReader;

import java.util.*;

public class Day10 {

    static class Machine {
        boolean[] target;
        List<Set<Integer>> buttons;
        int[] joltageRequirements;

        Machine(boolean[] target, List<Set<Integer>> buttons, int[] joltageRequirements) {
            this.target = target;
            this.buttons = buttons;
            this.joltageRequirements = joltageRequirements;
        }
    }

    public static Machine parseMachine(String line) {
        // Parse: [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}

        // Extract target state
        int startBracket = line.indexOf('[');
        int endBracket = line.indexOf(']');
        String targetStr = line.substring(startBracket + 1, endBracket);
        boolean[] target = new boolean[targetStr.length()];
        for (int i = 0; i < targetStr.length(); i++) {
            target[i] = targetStr.charAt(i) == '#';
        }

        // Extract buttons
        List<Set<Integer>> buttons = new ArrayList<>();
        int pos = endBracket + 1;
        while (pos < line.length()) {
            int startParen = line.indexOf('(', pos);
            if (startParen == -1) break;

            int endParen = line.indexOf(')', startParen);
            String buttonStr = line.substring(startParen + 1, endParen);

            Set<Integer> button = new HashSet<>();
            if (!buttonStr.isEmpty()) {
                String[] indices = buttonStr.split(",");
                for (String idx : indices) {
                    button.add(Integer.parseInt(idx.trim()));
                }
            }
            buttons.add(button);

            pos = endParen + 1;
        }

        // Extract joltage requirements
        int startBrace = line.indexOf('{');
        int endBrace = line.indexOf('}');
        String joltageStr = line.substring(startBrace + 1, endBrace);
        String[] joltageStrs = joltageStr.split(",");
        int[] joltageRequirements = new int[joltageStrs.length];
        for (int i = 0; i < joltageStrs.length; i++) {
            joltageRequirements[i] = Integer.parseInt(joltageStrs[i].trim());
        }

        return new Machine(target, buttons, joltageRequirements);
    }

    public static int solveMinButtonPresses(Machine machine) {
        int numLights = machine.target.length;
        int numButtons = machine.buttons.size();

        // Create augmented matrix for Gaussian elimination over GF(2)
        // Each row represents a light, each column represents a button
        // The last column is the target state
        boolean[][] matrix = new boolean[numLights][numButtons + 1];

        // Fill the matrix
        for (int j = 0; j < numButtons; j++) {
            for (int light : machine.buttons.get(j)) {
                if (light < numLights) {
                    matrix[light][j] = true;
                }
            }
        }

        // Set target column
        for (int i = 0; i < numLights; i++) {
            matrix[i][numButtons] = machine.target[i];
        }

        // Gaussian elimination over GF(2)
        int[] pivotCol = new int[numLights];
        Arrays.fill(pivotCol, -1);
        int currentRow = 0;

        for (int col = 0; col < numButtons && currentRow < numLights; col++) {
            // Find pivot
            int pivotRow = -1;
            for (int row = currentRow; row < numLights; row++) {
                if (matrix[row][col]) {
                    pivotRow = row;
                    break;
                }
            }

            if (pivotRow == -1) continue;

            // Swap rows
            if (pivotRow != currentRow) {
                boolean[] temp = matrix[currentRow];
                matrix[currentRow] = matrix[pivotRow];
                matrix[pivotRow] = temp;
            }

            pivotCol[currentRow] = col;

            // Eliminate
            for (int row = 0; row < numLights; row++) {
                if (row != currentRow && matrix[row][col]) {
                    for (int c = 0; c <= numButtons; c++) {
                        matrix[row][c] ^= matrix[currentRow][c];
                    }
                }
            }

            currentRow++;
        }

        // Check for inconsistency
        for (int row = currentRow; row < numLights; row++) {
            if (matrix[row][numButtons]) {
                return -1; // No solution
            }
        }

        // Find free variables
        Set<Integer> pivotCols = new HashSet<>();
        for (int i = 0; i < currentRow; i++) {
            if (pivotCol[i] != -1) {
                pivotCols.add(pivotCol[i]);
            }
        }

        List<Integer> freeVars = new ArrayList<>();
        for (int col = 0; col < numButtons; col++) {
            if (!pivotCols.contains(col)) {
                freeVars.add(col);
            }
        }

        // Try all combinations of free variables to find minimum
        int minPresses = Integer.MAX_VALUE;
        int numFreeVars = freeVars.size();

        for (int mask = 0; mask < (1 << numFreeVars); mask++) {
            boolean[] solution = new boolean[numButtons];

            // Set free variables
            for (int i = 0; i < numFreeVars; i++) {
                solution[freeVars.get(i)] = ((mask >> i) & 1) == 1;
            }

            // Back substitute to find pivot variables
            for (int row = currentRow - 1; row >= 0; row--) {
                if (pivotCol[row] == -1) continue;

                boolean val = matrix[row][numButtons];
                for (int col = pivotCol[row] + 1; col < numButtons; col++) {
                    if (matrix[row][col]) {
                        val ^= solution[col];
                    }
                }
                solution[pivotCol[row]] = val;
            }

            // Count button presses
            int count = 0;
            for (boolean pressed : solution) {
                if (pressed) count++;
            }
            minPresses = Math.min(minPresses, count);
        }

        return minPresses;
    }

    public long solvePart1(List<String> input) {
        int totalPresses = 0;
        for (String line : input) {
            Machine machine = parseMachine(line);
            int presses = solveMinButtonPresses(machine);
            totalPresses += presses;
        }
        return totalPresses;
    }

    public static int solveMinButtonPressesJoltage(Machine machine) {
        int numCounters = machine.joltageRequirements.length;
        int numButtons = machine.buttons.size();

        // Create augmented matrix for Gaussian elimination over integers
        // Each row represents a counter, each column represents a button
        // The last column is the target joltage value
        double[][] matrix = new double[numCounters][numButtons + 1];

        // Fill the matrix
        for (int j = 0; j < numButtons; j++) {
            for (int counter : machine.buttons.get(j)) {
                if (counter < numCounters) {
                    matrix[counter][j] = 1.0;
                }
            }
        }

        // Set target column
        for (int i = 0; i < numCounters; i++) {
            matrix[i][numButtons] = machine.joltageRequirements[i];
        }

        // Gaussian elimination
        int[] pivotCol = new int[numCounters];
        Arrays.fill(pivotCol, -1);
        int currentRow = 0;

        for (int col = 0; col < numButtons && currentRow < numCounters; col++) {
            // Find pivot (largest absolute value for numerical stability)
            int pivotRow = -1;
            double maxVal = 1e-10;
            for (int row = currentRow; row < numCounters; row++) {
                if (Math.abs(matrix[row][col]) > maxVal) {
                    maxVal = Math.abs(matrix[row][col]);
                    pivotRow = row;
                }
            }

            if (pivotRow == -1) continue;

            // Swap rows
            if (pivotRow != currentRow) {
                double[] temp = matrix[currentRow];
                matrix[currentRow] = matrix[pivotRow];
                matrix[pivotRow] = temp;
            }

            pivotCol[currentRow] = col;

            // Scale pivot row
            double pivot = matrix[currentRow][col];
            for (int c = 0; c <= numButtons; c++) {
                matrix[currentRow][c] /= pivot;
            }

            // Eliminate
            for (int row = 0; row < numCounters; row++) {
                if (row != currentRow && Math.abs(matrix[row][col]) > 1e-10) {
                    double factor = matrix[row][col];
                    for (int c = 0; c <= numButtons; c++) {
                        matrix[row][c] -= factor * matrix[currentRow][c];
                    }
                }
            }

            currentRow++;
        }

        // Find free variables
        Set<Integer> pivotCols = new HashSet<>();
        for (int i = 0; i < currentRow; i++) {
            if (pivotCol[i] != -1) {
                pivotCols.add(pivotCol[i]);
            }
        }

        List<Integer> freeVars = new ArrayList<>();
        for (int col = 0; col < numButtons; col++) {
            if (!pivotCols.contains(col)) {
                freeVars.add(col);
            }
        }

        // Try different combinations of free variables to find minimum
        // Start with all free variables at 0, then increase
        int maxFreeVarValue = 1000; // Upper bound for search
        int minPresses = Integer.MAX_VALUE;

        // Use a simple approach: try free variables from 0 up to a reasonable limit
        int numFreeVars = freeVars.size();

        if (numFreeVars == 0) {
            // No free variables, directly back substitute
            int[] solution = new int[numButtons];
            for (int row = currentRow - 1; row >= 0; row--) {
                if (pivotCol[row] == -1) continue;

                double val = matrix[row][numButtons];
                for (int col = pivotCol[row] + 1; col < numButtons; col++) {
                    val -= matrix[row][col] * solution[col];
                }
                solution[pivotCol[row]] = (int) Math.round(val);
            }

            // Check if valid (all non-negative)
            boolean valid = true;
            int sum = 0;
            for (int v : solution) {
                if (v < 0) {
                    valid = false;
                    break;
                }
                sum += v;
            }
            return valid ? sum : -1;
        }

        // With free variables, use priority queue approach to explore smallest sums first
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> {
            int sumA = 0, sumB = 0;
            for (int v : a) sumA += v;
            for (int v : b) sumB += v;
            return Integer.compare(sumA, sumB);
        });
        Set<String> visited = new HashSet<>();
        queue.offer(new int[numFreeVars]);

        while (!queue.isEmpty()) {
            int[] freeVarValues = queue.poll();

            String key = Arrays.toString(freeVarValues);
            if (visited.contains(key)) continue;
            visited.add(key);

            // If sum of free vars already exceeds minPresses, skip
            int freeSum = 0;
            for (int v : freeVarValues) freeSum += v;
            if (freeSum >= minPresses) continue;

            int[] solution = new int[numButtons];

            // Set free variables
            for (int i = 0; i < numFreeVars; i++) {
                solution[freeVars.get(i)] = freeVarValues[i];
            }

            // Back substitute
            boolean valid = true;
            for (int row = currentRow - 1; row >= 0; row--) {
                if (pivotCol[row] == -1) continue;

                double val = matrix[row][numButtons];
                for (int col = pivotCol[row] + 1; col < numButtons; col++) {
                    val -= matrix[row][col] * solution[col];
                }
                int intVal = (int) Math.round(val);
                if (intVal < 0) {
                    valid = false;
                    break;
                }
                solution[pivotCol[row]] = intVal;
            }

            if (valid) {
                // Verify the solution actually satisfies the original equations
                boolean verified = true;
                for (int counter = 0; counter < numCounters; counter++) {
                    int actual = 0;
                    for (int button = 0; button < numButtons; button++) {
                        if (machine.buttons.get(button).contains(counter)) {
                            actual += solution[button];
                        }
                    }
                    if (actual != machine.joltageRequirements[counter]) {
                        verified = false;
                        break;
                    }
                }

                if (verified) {
                    int sum = 0;
                    for (int v : solution) {
                        sum += v;
                    }
                    minPresses = Math.min(minPresses, sum);
                }
            }

            // Always explore neighboring states (increment each free variable)
            for (int i = 0; i < numFreeVars; i++) {
                if (freeVarValues[i] < maxFreeVarValue) {
                    int[] newVals = freeVarValues.clone();
                    newVals[i]++;
                    queue.offer(newVals);
                }
            }
        }

        return minPresses == Integer.MAX_VALUE ? -1 : minPresses;
    }

    public long solvePart2(List<String> input) {
        long totalPresses = 0;
        int machineNum = 0;
        for (String line : input) {
            machineNum++;
            Machine machine = parseMachine(line);
            int presses = solveMinButtonPressesJoltage(machine);
            if (presses == -1) {
                System.err.println("Warning: No solution found for machine " + machineNum);
            }
            totalPresses += presses;
        }
        return totalPresses;
    }

    public static void main(String[] args) {
        Day10 solution = new Day10();
        List<String> input = InputReader.readLines("day10/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }
}