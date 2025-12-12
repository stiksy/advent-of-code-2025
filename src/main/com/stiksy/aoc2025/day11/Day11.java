package com.stiksy.aoc2025.day11;

import com.stiksy.aoc2025.util.InputReader;

import java.util.*;

public class Day11 {

    public static Map<String, List<String>> parseGraph(List<String> input) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String line : input) {
            String[] parts = line.split(": ");
            String device = parts[0];
            String[] outputs = parts[1].split(" ");

            graph.put(device, Arrays.asList(outputs));
        }

        return graph;
    }

    public static int countPaths(Map<String, List<String>> graph, String start, String end) {
        Set<String> visited = new HashSet<>();
        return dfs(graph, start, end, visited);
    }

    private static int dfs(Map<String, List<String>> graph, String current, String end, Set<String> visited) {
        if (current.equals(end)) {
            return 1;
        }

        if (!graph.containsKey(current)) {
            return 0;
        }

        visited.add(current);
        int pathCount = 0;

        for (String neighbor : graph.get(current)) {
            if (!visited.contains(neighbor)) {
                pathCount += dfs(graph, neighbor, end, visited);
            }
        }

        visited.remove(current);
        return pathCount;
    }

    public long solvePart1(List<String> input) {
        Map<String, List<String>> graph = parseGraph(input);
        return countPaths(graph, "you", "out");
    }

    static class State {
        String node;
        Set<String> requiredVisited;

        State(String node, Set<String> requiredVisited) {
            this.node = node;
            this.requiredVisited = requiredVisited;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State state = (State) o;
            return node.equals(state.node) && requiredVisited.equals(state.requiredVisited);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, requiredVisited);
        }
    }

    // First, check if graph is a DAG. If so, we can use memoization without visited set
    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visiting = new HashSet<>();
        Set<String> visited = new HashSet<>();

        for (String node : graph.keySet()) {
            if (hasCycleDFS(graph, node, visiting, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCycleDFS(Map<String, List<String>> graph, String node,
                                       Set<String> visiting, Set<String> visited) {
        if (visiting.contains(node)) {
            return true;  // Found a cycle
        }
        if (visited.contains(node)) {
            return false;  // Already checked this node
        }

        visiting.add(node);
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (hasCycleDFS(graph, neighbor, visiting, visited)) {
                    return true;
                }
            }
        }
        visiting.remove(node);
        visited.add(node);
        return false;
    }

    public static long countPathsWithRequired(Map<String, List<String>> graph, String start, String end, Set<String> required) {
        boolean isDAG = !hasCycle(graph);

        Set<String> visited = new HashSet<>();
        Set<String> requiredVisited = new HashSet<>();
        Map<State, Long> memo = new HashMap<>();
        return dfsWithRequired(graph, start, end, visited, required, requiredVisited, memo, isDAG);
    }

    private static long dfsWithRequired(Map<String, List<String>> graph, String current, String end,
                                        Set<String> visited, Set<String> required, Set<String> requiredVisited,
                                        Map<State, Long> memo, boolean isDAG) {
        if (current.equals(end)) {
            return requiredVisited.containsAll(required) ? 1 : 0;
        }

        if (!graph.containsKey(current)) {
            return 0;
        }

        // If it's a DAG, we can use memoization without worrying about visited set
        if (isDAG) {
            State state = new State(current, new HashSet<>(requiredVisited));
            if (memo.containsKey(state)) {
                return memo.get(state);
            }
        }

        // For non-DAG or before caching, check visited
        if (!isDAG && visited.contains(current)) {
            return 0;
        }

        visited.add(current);
        if (required.contains(current)) {
            requiredVisited.add(current);
        }

        long pathCount = 0;

        for (String neighbor : graph.get(current)) {
            if (isDAG || !visited.contains(neighbor)) {
                pathCount += dfsWithRequired(graph, neighbor, end, visited, required, requiredVisited, memo, isDAG);
            }
        }

        visited.remove(current);
        if (required.contains(current)) {
            requiredVisited.remove(current);
        }

        // Cache the result if it's a DAG
        if (isDAG) {
            State state = new State(current, new HashSet<>(requiredVisited));
            memo.put(state, pathCount);
        }

        return pathCount;
    }

    public long solvePart2(List<String> input) {
        Map<String, List<String>> graph = parseGraph(input);
        Set<String> required = new HashSet<>(Arrays.asList("dac", "fft"));
        return countPathsWithRequired(graph, "svr", "out", required);
    }

    public static void main(String[] args) {
        Day11 solution = new Day11();
        List<String> input = InputReader.readLines("day11/input.txt");

        System.out.println("Part 1: " + solution.solvePart1(input));
        System.out.println("Part 2: " + solution.solvePart2(input));
    }
}
