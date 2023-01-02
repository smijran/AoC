package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12_1 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day12.txt");
        List<String> lines = Files.lines(path).toList();
        Field[][] fields = new Field[lines.size()][];
        Field start = null;
        Field end = null;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            char[] chars = line.toCharArray();
            fields[i] = new Field[chars.length];
            for (int j = 0; j < chars.length; j++) {
                fields[i][j] = new Field(i, j, chars[j]);
                if (chars[j] == 'S') {
                    start = fields[i][j];
                } else if (chars[j] == 'E') {
                    end = fields[i][j];
                }
            }
        }
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                var current = fields[i][j];
                // Up
                var top = i > 0 ? fields[i - 1][j] : null;
                if (top != null && isValidNeighbour(current, top)) {
                    current.validNeighbours.add(new Edge(top, computeWeight(current, top)));
                }
                // Down
                var down = i < fields.length - 1 ? fields[i + 1][j] : null;
                if (down != null && isValidNeighbour(current, down)) {
                    current.validNeighbours.add(new Edge(down, computeWeight(current, down)));
                }
                // Left
                var left = j > 0 ? fields[i][j - 1] : null;
                if (left != null && isValidNeighbour(current, left)) {
                    current.validNeighbours.add(new Edge(left, computeWeight(current, left)));
                }
                // Right
                var right = j < fields[i].length - 1 ? fields[i][j + 1] : null;
                if (right != null && isValidNeighbour(current, right)) {
                    current.validNeighbours.add(new Edge(right, computeWeight(current, right)));
                }
            }
        }
        System.out.println("=====");
        calculateShortestPathFromSource(start, end);
    }

    public static void calculateShortestPathFromSource(Field start, Field end) {
        Map<Field, Integer> distance = new HashMap<>();
        Map<Field, List<Field>> shortestPath = new HashMap<>();
        shortestPath.put(start, Collections.emptyList());
        distance.put(start, 0);

        Set<Field> settledNodes = new HashSet<>();
        Set<Field> unsettledNodes = new HashSet<>();

        unsettledNodes.add(start);

        while (unsettledNodes.size() != 0) {
            Field currentNode = getLowestDistanceNode(unsettledNodes, distance);
            unsettledNodes.remove(currentNode);
            for (Edge edge : currentNode.validNeighbours) {
                if (!settledNodes.contains(edge.field)) {
                    calculateMinimumDistance(edge, currentNode, distance, shortestPath);
                    unsettledNodes.add(edge.field);
                }
            }
            settledNodes.add(currentNode);
        }
        System.out.println(shortestPath.get(end));
        System.out.println(shortestPath.get(end).size() - 2);
    }

    private static Field getLowestDistanceNode(Set<Field> unsettledNodes, Map<Field, Integer> distance) {
        Field lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Field node : unsettledNodes) {
            int nodeDistance = distance.getOrDefault(node, Integer.MAX_VALUE);
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Edge neighbour, Field sourceNode, Map<Field, Integer> distance, Map<Field, List<Field>> shortestPaths) {
        Integer sourceDistance = distance.getOrDefault(sourceNode, Integer.MAX_VALUE);
        if (sourceDistance + neighbour.weight() < distance.getOrDefault(neighbour.field(), Integer.MAX_VALUE)) {
            distance.put(neighbour.field(), neighbour.weight() + sourceDistance);
            List<Field> shortestPath = new ArrayList<>(shortestPaths.computeIfAbsent(sourceNode, key -> Collections.emptyList()));
            shortestPath.add(sourceNode);
            shortestPaths.put(neighbour.field(), shortestPath);
        }
    }

    private static int computeWeight(Field current, Field neighbour) {
        if (current.getElevation() + 1 == neighbour.getElevation()) {
            return 0;
        } else if (neighbour.getElevation() <= current.getElevation()) {
            return 1 + Math.abs(current.getElevation() - neighbour.getElevation());
        }
        return Integer.MAX_VALUE;
    }

    private static boolean isValidNeighbour(Field current, Field neighbour) {
        return current.getElevation() + 1 == neighbour.getElevation() || neighbour.getElevation() <= current.getElevation();
    }

    record Edge(Field field, int weight) {
    }


    public static class Field {
        private final int x;
        private final int y;

        private final char character;
        private final List<Edge> validNeighbours = new ArrayList<>();

        public Field(int x, int y, char character) {
            this.x = x;
            this.y = y;
            this.character = character;
        }

        public int getElevation() {
            if (character == 'S') {
                return 0;
            } else if (character == 'E') {
                return 'z' - 'a' + 2;
            }
            return character - 'a' + 1;
        }

        @Override
        public String toString() {
            return "F{" + x + "," + y + "}=" + character;
        }
    }


}

