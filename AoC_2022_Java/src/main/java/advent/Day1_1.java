package advent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;

public class Day1_1 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day1.txt");
        BufferedReader read = new BufferedReader(
                new FileReader(path.toFile()));
        String i;
        List<Integer> elvesValues = new ArrayList<>();
        OptionalInt current = OptionalInt.empty();
        while ((i = read.readLine()) != null) {
            if (i.isBlank()) {
                current.ifPresent(elvesValues::add);
                current = OptionalInt.empty();
            } else {
                int currentValue = Integer.parseInt(i);
                current = current.isPresent() ? OptionalInt.of(current.getAsInt() + currentValue) : OptionalInt.of(currentValue);
            }
        }
        System.out.println(elvesValues.stream().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).limit(3).sum());
        read.close();
    }
}
