package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5_2 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day5.txt");
        List<String> lines = Files.lines(path).toList();
        boolean readStacks = true;
        Ship ship = new Ship();
        List<Character[]> load = new ArrayList<>();
        for (String line : lines) {
            readStacks = readStacks && !line.isEmpty();
            if (readStacks) {
                Character[] characters = readLine(line);
                load.add(characters);
            } else if (line.isEmpty()) {
                Collections.reverse(load);
                for (Character[] crates : load) {
                    ship.load(crates);
                }
            } else {
                Move move = readMove(line);
                ship.move(move);
            }
        }
        System.out.println(ship.topList());

    }

    private static class Ship {
        private Stack<Character>[] stacks = new Stack[9];

        private Ship() {
            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = new Stack<>();
            }
        }

        private void load(Character[] characters) {
            for (int i = 0; i < characters.length; i++) {
                if (characters[i] == null || Character.isWhitespace(characters[i]) || Character.isDigit(characters[i])) {
                    continue;
                }
                stacks[i].push(characters[i]);
            }
        }

        private void move(Move move) {
            int howMany = move.howMany();
            List<Character> elements = new ArrayList<>();
            while (howMany-- > 0) {
                elements.add(stacks[move.from() - 1].pop());
            }
            Collections.reverse(elements);
            for (Character element : elements){
                stacks[move.to() - 1].push(element);
            }
        }

        @Override
        public String toString() {
            return Arrays.deepToString(stacks);
        }

        public String topList() {
            final StringBuffer stringBuffer = new StringBuffer();
            for (Stack<Character> stack : stacks) {
                stringBuffer.append(stack.peek());
            }
            return stringBuffer.toString();

        }
    }

    private record Move(int howMany, int from, int to) {
    }

    private static Character[] readLine(String line) {
        Character[] characters = new Character[9];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = 1 + i * 4 < line.length() ? line.charAt(1 + i * 4) : ' ';
        }
        return characters;
    }

    private static Pattern MOVE_PATTERN = Pattern.compile("move ([0-9]+) from ([0-9]+) to ([0-9]+)");

    private static Move readMove(String line) {
        Matcher matcher = MOVE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        var what = Integer.parseInt(matcher.group(1));
        var from = Integer.parseInt(matcher.group(2));
        var to = Integer.parseInt(matcher.group(3));
        return new Move(what, from, to);
    }
}
