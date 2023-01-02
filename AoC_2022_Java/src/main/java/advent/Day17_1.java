package advent;

import com.google.common.collect.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17_1 {

    static final class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void moveLeft() {
            x--;
        }

        public void moveRight() {
            x++;
        }

        public void moveDown() {
            y--;
        }
    }

    public interface Shape {
        int getHeight();

        ShapeInMove putToMove(int positionFromBottom);
    }

    public interface ShapeInMove {
        int getBottomLine();

        void moveLeft(PieceOfWell lines);

        void moveRight(PieceOfWell lines);

        boolean moveDown(PieceOfWell liens);

        void print(PieceOfWell lines);
    }

    public static class Plus implements Shape {

        @Override
        public int getHeight() {
            return 3;
        }

        @Override
        public ShapeInMove putToMove(int positionFromBottom) {
            return new ShapeInMove() {
                Point top = new Point(3, positionFromBottom + 2);
                Point left = new Point(2, positionFromBottom + 1);
                Point center = new Point(3, positionFromBottom + 1);
                Point right = new Point(4, positionFromBottom + 1);
                Point bottom = new Point(3, positionFromBottom);
                List<Point> leftPoints = List.of(top, left, bottom);
                List<Point> rightPoints = List.of(top, right, bottom);
                List<Point> bottomPoints = List.of(left, bottom, right);
                List<Point> allPoints = List.of(top, left, center, right, bottom);

                @Override
                public int getBottomLine() {
                    return bottom.y;
                }

                @Override
                public void moveLeft(PieceOfWell pieceOfWell) {
                    if (left.x == 0) {
                        return;
                    }
                    // TODO

                    allPoints.forEach(Point::moveLeft);
                }

                @Override
                public void moveRight(PieceOfWell pieceOfWell) {
                    if (right.x == 6) {
                        return;
                    }
                    // TODO

                    allPoints.forEach(Point::moveRight);
                }

                public boolean moveDown(PieceOfWell pieceOfWell) {
                    for (Point bottomPoint : bottomPoints) {
                        if (pieceOfWell.getLine(bottomPoint.y - 1)[bottomPoint.x] != '.') {
                            return false;
                        }
                    }
                    allPoints.forEach(Point::moveDown);
                    return true;
                }

                @Override
                public void print(PieceOfWell pieceOfWell) {
                    for (Point point : allPoints) {
                        pieceOfWell.getLine(point.y)[point.x] = '#';
                    }
                }
            };
        }

    }

    public static class Dash implements Shape {
        @Override
        public int getHeight() {
            return 1;
        }

        @Override
        public ShapeInMove putToMove(int positionFromBottom) {
            return new ShapeInMove() {
                private List<Point> points =
                        List.of(new Point(2, positionFromBottom),
                                new Point(3, positionFromBottom),
                                new Point(4, positionFromBottom),
                                new Point(5, positionFromBottom));

                @Override
                public int getBottomLine() {
                    return points.stream().map(p -> p.y).collect(Collectors.toSet()).iterator().next();
                }

                @Override
                public void moveLeft(PieceOfWell pieceOfWell) {
                    if (points.get(0).x > 0 && pieceOfWell.getLine(getBottomLine())[points.get(0).x - 1] == '.') {
                        points.forEach(Point::moveLeft);
                    }
                }

                @Override
                public void moveRight(PieceOfWell pieceOfWell) {
                    if (points.get(3).x < 3 && pieceOfWell.getLine(getBottomLine())[points.get(3).x + 4] == '.') {
                        points.forEach(Point::moveRight);
                    }
                }

                public boolean moveDown(PieceOfWell pieceOfWell) {
                    char[] line = pieceOfWell.getLine(getBottomLine() - 1);
                    for (Point point : points) {
                        if (line[point.x] != '.') {
                            return false;
                        }
                    }
                    points.forEach(Point::moveDown);
                    return true;
                }

                @Override
                public void print(PieceOfWell pieceOfWell) {
                    char[] chars = pieceOfWell.getLine(getBottomLine());
                    for (Point point : points) {
                        chars[point.x] = '#';
                    }
                }
            };
        }

    }


    public static class Controller {
        private final Well well;
        private Shape currentShape;
        private ShapeInMove shapeInMove;
        private boolean lastDown = true;

        public Controller(Well well) {
            this.well = well;
        }

        public boolean hasShape() {
            return currentShape != null;
        }

        public void addShape(Shape shape) {
            currentShape = shape;
            lastDown = true;
            int height = shape.getHeight();
            for (int i = 0; i < height + 3; i++) {
                well.addEmptyLine();
            }
            shapeInMove = shape.putToMove(height + 3);
        }

        public void moveShape(Direction direction) {
            switch (direction) {
                case LEFT -> shapeInMove.moveLeft(well.getLines(shapeInMove.getBottomLine(), currentShape.getHeight()));
                case RIGHT ->
                        shapeInMove.moveRight(well.getLines(shapeInMove.getBottomLine(), currentShape.getHeight()));
            }
            lastDown = shapeInMove.moveDown(well.getLines(shapeInMove.getBottomLine() - 1, currentShape.getHeight()));
        }

        public boolean canMove() {
            return lastDown;
        }

        public void placeShape() {
            shapeInMove.print(well.getLines(shapeInMove.getBottomLine(), currentShape.getHeight()));
        }
    }

    public static class Well {
        private final static int WIDTH = 7;
        private final List<char[]> elements = new ArrayList<>();

        public Well() {
            final char[] firstLine = new char[WIDTH];
            Arrays.fill(firstLine, '-');
            elements.add(firstLine);
        }

        public void addEmptyLine() {
            final char[] line = new char[WIDTH];
            Arrays.fill(line, '.');
            elements.add(line);
        }

        public PieceOfWell getLines(int fromBottom, int noOfLines) {
            final Range<Integer> range = Range.closed(fromBottom, fromBottom + noOfLines);
            return new PieceOfWell(range, elements.subList(range.lowerEndpoint(), range.upperEndpoint()));
        }


        public void printFromTop() {
            List<char[]> topLines = new ArrayList<>(elements);
            Collections.reverse(topLines);
            for (char[] line : topLines) {
                System.out.print('|');
                for (char c : line) {
                    System.out.print(c);
                }
                System.out.println('|');
            }
            System.out.println("===============");
        }
    }

    public static class PieceOfWell {
        private final Range<Integer> range;
        private final List<char[]> lines;


        public PieceOfWell(Range<Integer> range, List<char[]> lines) {
            this.range = range;
            this.lines = lines;
        }

        public Range<Integer> getRange() {
            return range;
        }

        public char[] getLine(int fromBottom) {
            return lines.get(fromBottom - range.lowerEndpoint());
        }
    }

    public static class CircularIterator<T> implements Iterator<T> {

        private final List<T> objects;
        private Iterator<T> currentIterator;

        public CircularIterator(List<T> objects) {
            this.objects = objects;
            currentIterator = objects.iterator();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            if (currentIterator.hasNext()) {
                return currentIterator.next();
            }
            currentIterator = objects.iterator();
            return currentIterator.next();
        }
    }

    public enum Direction {
        LEFT, RIGHT
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day17.txt");
        List<Direction> winds = Files.lines(path)
                .map(String::toCharArray)
                .flatMap(ar ->
                        IntStream.range(0, ar.length)
                                .mapToObj(i -> ar[i])
                )
                .map(c -> switch (c) {
                    case '<' -> Direction.LEFT;
                    case '>' -> Direction.RIGHT;
                    default -> throw new IllegalStateException();
                }).collect(Collectors.toList());
        CircularIterator<Direction> directionIterator = new CircularIterator<>(winds);
        CircularIterator<Shape> shapeIterator = new CircularIterator<>(List.of(new Dash(), new Plus()));
        final int FINISH = 2;
        Well well = new Well();
        for (int i = 0; i < FINISH; i++) {
            Shape shape = shapeIterator.next();
            Controller controller = new Controller(well);
            controller.addShape(shape);
            if (i == 1) {
                controller.placeShape();
                TimeUnit.SECONDS.sleep(1);
                System.exit(1);
            }
            do {
                Direction direction = directionIterator.next();
                controller.moveShape(direction);
            } while (controller.canMove());
            controller.placeShape();
            well.printFromTop();
        }
    }

}

