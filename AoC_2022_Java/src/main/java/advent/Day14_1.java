package advent;

import com.google.common.collect.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.ToIntFunction;

public class Day14_1 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day14.txt");
        List<Rock> rocks = Files.lines(path).map(Day14_1::parse).toList();
        Field field = new Field(rocks);
        field.printField();
        int rounds = 0;
        while (field.dropSand()) {
            rounds++;
        }
        field.printField();
        System.out.println(rounds);
    }

    public static class Field {
        private final char[][] cells;
        private final Range<Integer> horizontalSpan;
        private final Range<Integer> verticalSpan;

        private final Point sandStartPoint;

        public Field(List<Rock> rocks) {
            horizontalSpan = rocks.stream().map(Rock::getXRange).reduce(Range.closed(500, 500), Range::span);
            verticalSpan = rocks.stream().map(Rock::getYRange).reduce(Range.closed(0, 0), Range::span);
            int width = horizontalSpan.upperEndpoint() - horizontalSpan.lowerEndpoint() + 1;
            int height = verticalSpan.upperEndpoint() - verticalSpan.lowerEndpoint() + 1;
            cells = new char[height][width];
            for (char[] cell : cells) {
                Arrays.fill(cell, '.');
            }
            sandStartPoint = new Point(adjustX(500), 0);
            cells[sandStartPoint.y][sandStartPoint.x] = '+';
            rocks.forEach(this::loadRock);
        }

        public boolean dropSand() {
            Point sand = sandStartPoint;
            Result nextPosition = moveSand(sand);
            while (nextPosition.nextPoint != null && !nextPosition.hasFallenOut) {
                sand = nextPosition.nextPoint;
                nextPosition = moveSand(sand);
            }
            if (nextPosition.hasFallenOut) {
                return false;
            }
            cells[sand.y][sand.x] = 'o';
            return true;
        }

        record Result(Point nextPoint, boolean hasFallenOut) {
        }

        private Result moveSand(Point sand) {
            if (cells.length <= sand.y + 1) {
                return new Result(null, true);
            }
            if (cells[sand.y + 1][sand.x] == '.') {
                return new Result(new Point(sand.x, sand.y + 1), false);
            } else if (sand.x - 1 < 0) {
                return new Result(null, true);
            } else if (cells[sand.y + 1][sand.x - 1] == '.') {
                return new Result(new Point(sand.x - 1, sand.y + 1), false);
            } else if (sand.x + 1 >= cells[sand.y + 1].length) {
                return new Result(null, true);
            } else if (cells[sand.y + 1][sand.x + 1] == '.') {
                return new Result(new Point(sand.x + 1, sand.y + 1), false);
            }
            return new Result(null, false);
        }

        private void loadRock(Rock rock) {
            List<Point> remainingPoints = new ArrayList<>(rock.getLines());
            Point start = remainingPoints.remove(0);
            while (!remainingPoints.isEmpty()) {
                Point end = remainingPoints.remove(0);
                if (start.x == end.x) {
                    for (int i = Math.min(start.y, end.y); i <= Math.max(start.y, end.y); i++) {
                        cells[i][adjustX(start.x)] = '#';
                    }
                } else if (start.y == end.y) {
                    for (int i = Math.min(start.x, end.x); i <= Math.max(start.x, end.x); i++) {
                        cells[start.y][adjustX(i)] = '#';
                    }
                } else {
                    throw new IllegalStateException(start + " " + end);
                }
                start = end;
            }
        }

        public void printField() {
            printField(null);
        }

        public void printField(Point sandDrop) {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    if (sandDrop != null && i == sandDrop.y && j == sandDrop.x) {
                        System.out.print('O');
                    } else {
                        System.out.print(cells[i][j]);
                    }
                }
                System.out.println();
            }
        }

        private int adjustX(int x) {
            return x - horizontalSpan.lowerEndpoint();
        }
    }


    private static Rock parse(String line) {
        return new Rock(Arrays.stream(line.split("->"))
                .map(String::trim)
                .map(Day14_1::parsePoint)
                .toList());
    }

    private static Point parsePoint(String pointToParse) {
        String[] split = pointToParse.split(",");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    record Point(int x, int y) {
    }

    static class Rock {
        private final List<Point> points;

        Rock(List<Point> points) {
            this.points = points;
        }

        public List<Point> getLines() {
            return points;
        }

        public Range<Integer> getXRange() {
            return getRange(Point::x);
        }

        public Range<Integer> getYRange() {
            return getRange(Point::y);
        }

        private Range<Integer> getRange(ToIntFunction<Point> selector) {
            IntSummaryStatistics intSummaryStatistics =
                    points
                            .stream()
                            .mapToInt(selector)
                            .summaryStatistics();
            return Range.closed(intSummaryStatistics.getMin(), intSummaryStatistics.getMax());
        }

        @Override
        public String toString() {
            return points.toString();
        }
    }

}

