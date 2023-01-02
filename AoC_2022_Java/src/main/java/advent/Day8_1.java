package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;

public class Day8_1 {
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
        int visible = 0;
        for (int i = 0; i < treeLine.length; i++) {
            for (int j = 0; j < treeLine[i].length; j++) {
                int currentTree = treeLine[i][j];
                EnumSet<Visible> visibility = EnumSet.allOf(Visible.class);
                for (int row = 0; row < treeLine.length; row++) {
                    int analyzedTree = treeLine[row][j];
                    if (row == i) {
                        continue;
                    }
                    if (analyzedTree >= currentTree) {
                        if (row < i) {
                            visibility.remove(Visible.LEFT);
                        } else {
                            visibility.remove(Visible.RIGHT);
                        }
                    }
                }
                for (int column = 0; column < treeLine.length; column++) {
                    int analyzedTree = treeLine[i][column];
                    if (column == j) {
                        continue;
                    }
                    if (analyzedTree >= currentTree) {
                        if (column < j) {
                            visibility.remove(Visible.TOP);
                        } else {
                            visibility.remove(Visible.BOTTOM);
                        }
                    }
                }
                if (!visibility.isEmpty()) {
                    visible++;
                }
            }
        }
        System.out.println(visible);
    }

    enum Visible {
        TOP, BOTTOM, LEFT, RIGHT
    }
}

