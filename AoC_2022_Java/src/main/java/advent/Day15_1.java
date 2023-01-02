package advent;

import com.google.common.collect.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15_1 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day15.txt");
        Optional<Range<Integer>> oRange = Files.lines(path)
                .map(Day15_1::parse)
                .filter(p -> p.isCoveringRow(2000000))
                .map(p -> p.getXRangeAtRow(2000000))
                .reduce(Range::span);

        if (oRange.isPresent()) {
            Range<Integer> range = oRange.get();
            System.out.println(range.upperEndpoint() - range.lowerEndpoint());
        }
    }


    record ClosePair(Sensor sensor, Beacon beacon) {

        public int getManhattanDistance() {
            return Math.abs(beacon.position.x - sensor.position.x) +
                    Math.abs(beacon.position.y - sensor.position.y);
        }


        public Range<Integer> getXRangeAtRow(int y) {
            int remaining = Math.abs(sensor.position.y - y);
            int mdLeft = getManhattanDistance() - remaining;
            return Range.closed(sensor.position.x - mdLeft, sensor.position.x + mdLeft);
        }

        public boolean isCoveringRow(int y) {
            return Range.closed(sensor.position.y - getManhattanDistance(), sensor.position.y + getManhattanDistance()).contains(y);
        }

        public Range<Integer> getYRangeAtColumn(int x) {
            int remaining = Math.abs(sensor.position.x - x);
            int mdLeft = getManhattanDistance() - remaining;
            return Range.closed(sensor.position.y - mdLeft, sensor.position.y + mdLeft);
        }

        public boolean isCoveringColumn(int x) {
            return Range.closed(sensor.position.x - getManhattanDistance(), sensor.position.x + getManhattanDistance()).contains(x);
        }
    }

    record Beacon(Point position) {
    }

    record Sensor(Point position) {
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("Sensor at x=([-+]?\\d*), y=([-+]?\\d*): closest beacon is at x=([-+]?\\d*), y=([-+]?\\d*)");

    private static ClosePair parse(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (matcher.matches()) {
            Point sensor = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Point beacon = new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            return new ClosePair(new Sensor(sensor), new Beacon(beacon));
        }
        throw new IllegalArgumentException();
    }

    record Point(int x, int y) {
    }

}

