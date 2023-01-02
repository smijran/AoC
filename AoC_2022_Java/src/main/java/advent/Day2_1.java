package advent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class Day2_1 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day2.txt");
        BufferedReader read = new BufferedReader(
                new FileReader(path.toFile()));
        String i;
        long sum = 0;
        while ((i = read.readLine()) != null) {
            switch (i) {
                case "A X":
                    sum += 3;
                    break;
                case "A Y":
                    sum += 4;
                    break;
                case "A Z":
                    sum += 8;
                    break;
                case "B X":
                    sum += 1;
                    break;
                case "B Y":
                    sum += 5;
                    break;
                case "B Z":
                    sum += 9;
                    break;
                case "C X":
                    sum += 2;
                    break;
                case "C Y":
                    sum += 6;
                    break;
                case "C Z":
                    sum += 7;
                    break;
                default:
                    throw new IllegalArgumentException(i);
            }
        }
        System.out.println(sum);
        read.close();
    }
}
