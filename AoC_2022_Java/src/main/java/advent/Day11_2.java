package advent;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day11_2 {


    public static final BigInteger THREE = BigInteger.valueOf(3);

    public static final BigInteger SEVEN = BigInteger.valueOf(7);

    public static final BigInteger NINETEEN = BigInteger.valueOf(19);

    public static final BigInteger FIVE = BigInteger.valueOf(5);

    public static final BigInteger EIGHT = BigInteger.valueOf(8);

    public static final BigInteger SEVENTEEN = BigInteger.valueOf(17);

    public static final BigInteger ELEVEN = BigInteger.valueOf(11);

    public static final BigInteger THIRTEEN = BigInteger.valueOf(13);

    public static final BigInteger FOUR = BigInteger.valueOf(4);

    public static Monkey[] TEST_MONKEYS = new Monkey[4];

    public static final BigInteger TWENTY_THREE = BigInteger.valueOf(23);

    public static final BigInteger SIX = BigInteger.valueOf(6);

    static {
        TEST_MONKEYS[0] = new Monkey(
                val -> val.multiply(NINETEEN),
                val -> val.mod(TWENTY_THREE).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[2].receiveItem(item),
                item -> TEST_MONKEYS[3].receiveItem(item),
                79, 98);
        TEST_MONKEYS[1] = new Monkey(
                val -> val.add(SIX),
                val -> val.mod(NINETEEN).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[2].receiveItem(item),
                item -> TEST_MONKEYS[0].receiveItem(item),
                54, 65, 75, 74);
        TEST_MONKEYS[2] = new Monkey(
                val -> val.pow(2),
                val -> val.mod(THIRTEEN).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[1].receiveItem(item),
                item -> TEST_MONKEYS[3].receiveItem(item),
                79, 60, 97);
        TEST_MONKEYS[3] = new Monkey(
                val -> val.add(THREE),
                val -> val.mod(SEVENTEEN).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[0].receiveItem(item),
                item -> TEST_MONKEYS[1].receiveItem(item),
                74);
    }

    public static Monkey[] PUZZLE_MONKEYS = new Monkey[8];

    static {
        PUZZLE_MONKEYS[0] = new Monkey(
                val -> val.multiply(BigInteger.valueOf(17)),
                val -> val.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[4].receiveItem(item),
                item -> PUZZLE_MONKEYS[2].receiveItem(item),
                99, 67, 92, 61, 83, 64, 98);
        PUZZLE_MONKEYS[1] = new Monkey(
                val -> val.multiply(ELEVEN),
                val -> val.mod(FIVE).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[3].receiveItem(item),
                item -> PUZZLE_MONKEYS[5].receiveItem(item),
                78, 74, 88, 89, 50);
        PUZZLE_MONKEYS[2] = new Monkey(
                val -> val.add(FOUR),
                val -> val.mod(BigInteger.TWO).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[6].receiveItem(item),
                item -> PUZZLE_MONKEYS[4].receiveItem(item),
                98, 91);
        PUZZLE_MONKEYS[3] = new Monkey(
                val -> val.pow(2),
                val -> val.mod(THIRTEEN).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[0].receiveItem(item),
                item -> PUZZLE_MONKEYS[5].receiveItem(item),
                59, 72, 94, 91, 79, 88, 94, 51);
        PUZZLE_MONKEYS[4] = new Monkey(
                val -> val.add(SEVEN),
                val -> val.mod(ELEVEN).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[7].receiveItem(item),
                item -> PUZZLE_MONKEYS[6].receiveItem(item),
                95, 72, 78);
        PUZZLE_MONKEYS[5] = new Monkey(
                val -> val.add(EIGHT),
                val -> val.mod(SEVENTEEN).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[0].receiveItem(item),
                item -> PUZZLE_MONKEYS[2].receiveItem(item),
                76);
        PUZZLE_MONKEYS[6] = new Monkey(
                val -> val.add(FIVE),
                val -> val.mod(NINETEEN).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[7].receiveItem(item),
                item -> PUZZLE_MONKEYS[1].receiveItem(item),
                69, 60, 53, 89, 71, 88);
        PUZZLE_MONKEYS[7] = new Monkey(
                val -> val.add(THREE),
                val -> val.mod(SEVEN).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[1].receiveItem(item),
                item -> PUZZLE_MONKEYS[3].receiveItem(item),
                72, 54, 63, 80);
    }

    public static void main(String[] args) throws IOException {
        Monkey[] monkeys = PUZZLE_MONKEYS;

        for (int round = 0; round < 10000; round++) {
            for (int i = 0; i < monkeys.length; i++) {
                monkeys[i].inspect();
            }
            if (round % 100 == 0) {
                System.out.println(round);
                List<Integer> results = Arrays.stream(monkeys).map(Monkey::getTotalNumberOfInspections).sorted().collect(Collectors.toList());
                System.out.println(results);
            }
        }
        List<Integer> results = Arrays.stream(monkeys).map(Monkey::getTotalNumberOfInspections).sorted().collect(Collectors.toList());
        System.out.println(results);
    }


    public static class Monkey {
        private final List<BigInteger> items = new ArrayList<>();
        private final Function<BigInteger, BigInteger> operation;
        private final Predicate<BigInteger> test;
        private final Consumer<BigInteger> ifTrue;
        private final Consumer<BigInteger> ifFalse;
        private int totalNumberOfInspections = 0;

        public Monkey(Function<BigInteger, BigInteger> operation, Predicate<BigInteger> test, Consumer<BigInteger> ifTrue, Consumer<BigInteger> ifFalse, int... items) {
            this(operation, test, ifTrue, ifFalse, Arrays.stream(items).mapToObj(BigInteger::valueOf).toArray(BigInteger[]::new));
        }

        public Monkey(Function<BigInteger, BigInteger> operation, Predicate<BigInteger> test, Consumer<BigInteger> ifTrue, Consumer<BigInteger> ifFalse, BigInteger... items) {
            this.operation = operation;
            this.test = test;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
            this.items.addAll(Arrays.stream(items).toList());
        }

        public void inspect() {
            List<BigInteger> itemsToInspect = List.copyOf(items);
            items.clear();
            for (BigInteger item : itemsToInspect) {
                BigInteger worryLevel = operation.apply(item);
                worryLevel = worryLevel.mod(BigInteger.valueOf(9699690));
                if (test.test(worryLevel)) {
                    ifTrue.accept(worryLevel);
                } else {
                    ifFalse.accept(worryLevel);
                }
            }
            totalNumberOfInspections += itemsToInspect.size();
        }

        public int getTotalNumberOfInspections() {
            return totalNumberOfInspections;
        }

        public void receiveItem(BigInteger item) {
            items.add(item);
        }

        @Override
        public String toString() {
            return "Monkey:" + items;
        }


    }

//    static BigInteger moduloMultiplication(BigInteger a, BigInteger b, BigInteger mod) {
//
//        // Initialize result
//        BigInteger res = BigInteger.ZERO;
//
//        // Update a if it is more than
//        // or equal to mod
//        a = a.mod(mod);
//
//        while (b.signum() > 0) {
//
//            // If b is odd, add a with result
//            if ((b & 1) > 0) {
//                res = (res + a) % mod;
//            }
//
//            // Here we assume that doing 2*a
//            // doesn't cause overflow
//            a = (2 * a) % mod;
//
//            b = b.shiftRight(1) >>= 1; // b = b / 2
//        }
//        return res;
//    }
}

