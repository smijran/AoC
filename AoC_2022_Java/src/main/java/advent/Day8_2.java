package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.OptionalInt;

public class Day8_2 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day8.txt");
        List<String> lines = Files.lines(path).toList();
        int[][] treeLine = new int[lines.size()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                treeLine[i][j] = ((int) line[j] - (int) '0');
            }
        }
        OptionalInt currentMax = OptionalInt.empty();
        for (int i = 0; i < treeLine.length; i++) {
            for (int j = 0; j < treeLine[i].length; j++) {
                final int currentTree = treeLine[i][j];
                int top = 0;
                int bottom = 0;
                int left = 0;
                int right = 0;
                // Move top
                for (int row = i - 1; row >= 0; row--) {
                    top++;
                    if (treeLine[row][j] >= currentTree) {
                        break;
                    }
                }
                // Move bottom
                for (int row = i + 1; row < treeLine.length; row++) {
                    bottom++;
                    if (treeLine[row][j] >= currentTree) {
                        break;
                    }
                }
                // Move top
                for (int column = j - 1; column >= 0; column--) {
                    left++;
                    if (treeLine[i][column] >= currentTree) {
                        break;
                    }
                }
                // Move bottom
                for (int column = j + 1; column < treeLine[i].length; column++) {
                    right++;
                    if (treeLine[i][column] >= currentTree) {
                        break;
                    }
                }
                int factor = top * bottom * left * right;
                if (currentMax.isEmpty() || currentMax.getAsInt() < factor) {
                    System.out.println(top + " " + bottom + " " + left + " " + right);
                    System.out.println(i + " " + j + " " + currentTree + " fac " + factor);
                    currentMax = OptionalInt.of(factor);
                }
            }
        }
        System.out.println(currentMax);
    }

    enum Visible {
        TOP, BOTTOM, LEFT, RIGHT
    }
}

