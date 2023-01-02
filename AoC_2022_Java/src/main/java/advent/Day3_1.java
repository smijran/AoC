package advent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.BitSet;

public class Day3_1 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day3.txt");
        BufferedReader read = new BufferedReader(
                new FileReader(path.toFile()));
        String line;
        long sum = 0;
        while ((line = read.readLine()) != null) {
            var left = line.substring(0, line.length() / 2);
            var right = line.substring(line.length() / 2);
            BitSet leftChecks = new BitSet(52);
            BitSet rightChecks = new BitSet(52);
            for (char letter : left.toCharArray()) {
                leftChecks.set(priority(letter) - 1);
            }
            for (char letter : right.toCharArray()) {
                rightChecks.set(priority(letter) - 1);
            }
            leftChecks.and(rightChecks);
            for (int i = 0; i < leftChecks.length(); i++) {
                if (leftChecks.get(i)) {
                    sum += i + 1;
                    break;
                }
            }
        }
        System.out.println(sum);
        read.close();
    }

    private static int priority(char a) {
        if (Character.isLowerCase(a)) {
            return (int) a - 'a' + 1;
        } else if (Character.isUpperCase(a)) {
            return (int) a - 'A' + 27;
        }
        return 0;
    }
}
