package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day3_2 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day3.txt");
        List<String> lines = Files.lines(path).collect(Collectors.toList());
        List<String> currentGroup = new ArrayList<>();
        long sum = 0;
        for (String line : lines) {
            currentGroup.add(line);
            if (currentGroup.size() == 3) {
                List<BitSet> bitSets = new ArrayList<>();
                for (String rucksack : currentGroup) {
                    BitSet currentBitset = new BitSet(52);
                    for (char letter : rucksack.toCharArray()) {
                        currentBitset.set(priority(letter) - 1);
                    }
                    bitSets.add(currentBitset);
                }
                bitSets.get(1).and(bitSets.get(2));
                bitSets.get(0).and(bitSets.get(1));
                for (int i = 0; i < bitSets.get(0).length(); i++) {
                    if (bitSets.get(0).get(i)) {
                        sum += i + 1;
                        break;
                    }
                }
                currentGroup = new ArrayList<>();
            }
        }
//        for (List<String> group)
//
//            var left = line.substring(0, line.length() / 2);
//        var right = line.substring(line.length() / 2);
//        BitSet leftChecks = new BitSet(52);
//        BitSet rightChecks = new BitSet(52);
//        for (char letter : left.toCharArray()) {
//            leftChecks.set(priority(letter) - 1);
//        }
//        for (char letter : right.toCharArray()) {
//            rightChecks.set(priority(letter) - 1);
//        }
//        leftChecks.and(rightChecks);
//        for (int i = 0; i < leftChecks.length(); i++) {
//            if (leftChecks.get(i)) {
//                sum += i + 1;
//                break;
//            }
//        }
//
        System.out.println(sum);
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
