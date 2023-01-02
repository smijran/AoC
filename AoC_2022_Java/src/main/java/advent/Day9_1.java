package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9_1 {
    enum Direction {
        Up(0, +1), Down(0, -1), Left(-1, 0), Right(+1, 0);
        private final int dirX;
        private final int dirY;

        Direction(int x, int y) {
            this.dirX = x;
            this.dirY = y;
        }

        public int getDirX() {
            return dirX;
        }

        public int getDirY() {
            return dirY;
        }
    }

    record Move(Direction direction, int length) {
    }

    record Position(int x, int y) {
        Position move(Direction direction) {
            return new Position(x + direction.getDirX(), y + direction.getDirY());
        }
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day9.txt");
        List<Move> moves = Files.lines(path).map(Day9_1::toMove).toList();
        Position currentHeadPosition = new Position(0, 0);
        Position currentTailPosition = new Position(0, 0);
        Set<Position> visitedPositions = new HashSet<>();
        visitedPositions.add(currentTailPosition);
        for (Move move : moves) {
            List<Position> headPositions = generateHeadPositions(currentHeadPosition, move);
            System.out.println("MOVE" + move);
            System.out.println(headPositions.size());
            for (Position headPosition : headPositions) {
                currentHeadPosition = headPosition;
                System.out.println("H " + currentHeadPosition);
                System.out.println("T " + currentTailPosition);
                currentTailPosition = generateNextTailPosition(currentTailPosition, currentHeadPosition);
                System.out.println("H+ " + currentHeadPosition);
                System.out.println("T+ " + currentTailPosition);
                visitedPositions.add(currentTailPosition);
            }
        }
        System.out.println(visitedPositions.size());
    }

    private static Position generateNextTailPosition(Position tailPosition, Position headPosition) {
        int diffInX = headPosition.x - tailPosition.x;
        int diffInY = headPosition.y - tailPosition.y;
        if (Math.abs(diffInX) > 1 || Math.abs(diffInY) > 1) {
            if (diffInX == 0) {
                return new Position(tailPosition.x, diffInY > 0 ? tailPosition.y + 1 : tailPosition.y - 1);
            } else if (diffInY == 0) {
                return new Position(diffInX > 0 ? tailPosition.x + 1 : tailPosition.x - 1, tailPosition.y);
            } else if ((diffInX == 2 && diffInY == 1) || (diffInX == 1 && diffInY == 2)) {
                return new Position(tailPosition.x + 1, tailPosition.y + 1);
            } else if ((diffInX == -2 && diffInY == 1) || (diffInX == -1 && diffInY == 2)) {
                return new Position(tailPosition.x - 1, tailPosition.y + 1);
            } else if ((diffInX == 2 && diffInY == -1) || (diffInX == 1 && diffInY == -2)) {
                return new Position(tailPosition.x + 1, tailPosition.y - 1);
            } else if ((diffInX == -2 && diffInY == -1) || (diffInX == -1 && diffInY == -2)) {
                return new Position(tailPosition.x - 1, tailPosition.y - 1);
            }

        }
        return tailPosition;
    }

    private static List<Position> generateHeadPositions(Position headPosition, Move move) {
        Position currentPosition = headPosition;
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < move.length; i++) {
            currentPosition = currentPosition.move(move.direction());
            positions.add(currentPosition);
        }
        return positions;
    }

    private static Move toMove(String entry) {
        String[] split = entry.split("\\ ");
        var direction = parseDirection(split[0]);
        var move = parseLength(split[1]);
        return new Move(direction, move);
    }

    private static int parseLength(String length) {
        return Integer.parseInt(length);
    }

    private static Direction parseDirection(String direction) {
        return switch (direction) {
            case "U" -> Direction.Up;
            case "D" -> Direction.Down;
            case "L" -> Direction.Left;
            case "R" -> Direction.Right;
            default -> throw new IllegalArgumentException(direction);
        };
    }

}

