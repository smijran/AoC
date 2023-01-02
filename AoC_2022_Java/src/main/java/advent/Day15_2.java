package advent;

import com.google.common.collect.Range;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15_2 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day15.txt");
        List<ClosePair> pairs = Files.lines(path)
                .map(Day15_2::parse).toList();
        Range<Integer> validRange = Range.closed(0, 4000000);
        Set<Point> candidates = pairs
                .stream()
                .parallel()
                .map(p -> p.getPointsOnTheEdge(1))
                .flatMap(Set::stream)
                .filter(p -> validRange.contains(p.x) && validRange.contains(p.y))
                .collect(Collectors.toSet());
        System.out.println("===");
        System.out.println(candidates.size());
        Optional<Point> oPoint = candidates.stream()
                .parallel()
                .filter(p -> pairs.stream().noneMatch(pair -> pair.isCoveringPoint(p)))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple candidates");
                });
        if (oPoint.isPresent()) {
            BigInteger x = BigInteger.valueOf(oPoint.get().x);
            BigInteger y = BigInteger.valueOf(oPoint.get().y);
            BigInteger mu = BigInteger.valueOf(validRange.upperEndpoint());
            System.out.println(x.multiply(mu).add(y));
        }

    }


    record ClosePair(Sensor sensor, Beacon beacon) {
        public int getManhattanDistance() {
            return Day15_2.getManhattanDistance(sensor.position, beacon.position);
        }

        public boolean isCoveringPoint(Point point) {
            return getManhattanDistance() >= Day15_2.getManhattanDistance(sensor.position, point);
        }

        public Set<Point> getPointsOnTheEdge(int distanceModifier) {
            Set<Point> result = new HashSet<>();
            for (int md = 0; md <= getManhattanDistance() + distanceModifier; md++) {
                var rMd = getManhattanDistance() + distanceModifier - md;
                Point topRight = new Point(sensor.position.x + md, sensor.position.y + rMd);
                Point bottomRight = new Point(sensor.position.x + md, sensor.position.y - rMd);
                Point topLeft = new Point(sensor.position.x - md, sensor.position.y + rMd);
                Point bottomLeft = new Point(sensor.position.x - md, sensor.position.y - rMd);
                result.add(topRight);
                result.add(topLeft);
                result.add(bottomRight);
                result.add(bottomLeft);
            }
            System.out.println(result.size());
            return result;
        }
    }

    public static int getManhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
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

