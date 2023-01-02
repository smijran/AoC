package advent;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Day4_2 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day4.txt");
        Stream<String> lines = Files.lines(path);
        long sum = lines
                .map(Day4_2::parse)
                .filter(Day4_2::enclosing)
                .count();
        System.out.println(sum);
    }

    private static boolean enclosing(Pair<Range<Integer>, Range<Integer>> pair) {
        return pair.getLeft().isConnected(pair.getRight());
    }

    private static Pair<Range<Integer>, Range<Integer>> parse(String line) {
        String[] split = line.split(",");
        Range<Integer> left = parseRange(split[0]);
        Range<Integer> right = parseRange(split[1]);
        System.out.println(left + " " + right + " " + enclosing(Pair.of(left, right)));
        return Pair.of(left, right);
    }

    private static Range<Integer> parseRange(String range) {
        String[] split = range.split("-");
        int bottom = Integer.parseInt(split[0]);
        int top = Integer.parseInt(split[1]);
        return Range.range(bottom, BoundType.CLOSED, top, BoundType.CLOSED);
    }

}
