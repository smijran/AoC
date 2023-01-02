package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6_2 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day6.txt");
        List<String> lines = Files.lines(path).toList();

        System.out.println(startint("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
        System.out.println(startint("bvwbjplbgvbhsrlpgdmjqwftvncz"));
        System.out.println(startint("nppdvjthqldpwncqszvftbrmjlhg"));
        System.out.println(startint("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
        System.out.println(startint("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));
        System.out.println(startint(lines.get(0)));
    }

    private final static int WINDOWN_LENGTH = 14;

    public static OptionalInt startint(String lane) {
        char[] charArray = lane.toCharArray();
        for (int windowStart = 0; windowStart < charArray.length - WINDOWN_LENGTH; windowStart++) {
            char[] window = Arrays.copyOfRange(charArray, windowStart, windowStart + WINDOWN_LENGTH);
            Map<Character, Long> histogram = hist(window);
            if (histogram.size() == WINDOWN_LENGTH) {
                System.out.println(histogram);
                System.out.println(window);
                return OptionalInt.of(windowStart + WINDOWN_LENGTH);
            }
        }
        return OptionalInt.empty();
    }

    private static Map<Character, Long> hist(char[] window) {
        return IntStream.range(0, window.length)
                .mapToObj(i -> window[i])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
