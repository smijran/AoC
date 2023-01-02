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

public class Day11_1 {

    public static Monkey[] TEST_MONKEYS = new Monkey[4];

    static {
        TEST_MONKEYS[0] = new Monkey(
                val -> val.multiply(BigInteger.valueOf(19)),
                val -> val.mod(BigInteger.valueOf(23)).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[2].receiveItem(item),
                item -> TEST_MONKEYS[3].receiveItem(item),
                79, 98);
        TEST_MONKEYS[1] = new Monkey(
                val -> val.add(BigInteger.valueOf(6)),
                val -> val.mod(BigInteger.valueOf(19)).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[2].receiveItem(item),
                item -> TEST_MONKEYS[0].receiveItem(item),
                54, 65, 75, 74);
        TEST_MONKEYS[2] = new Monkey(
                val -> val.multiply(val),
                val -> val.mod(BigInteger.valueOf(13)).equals(BigInteger.ZERO),
                item -> TEST_MONKEYS[1].receiveItem(item),
                item -> TEST_MONKEYS[3].receiveItem(item),
                79, 60, 97);
        TEST_MONKEYS[3] = new Monkey(
                val -> val.add(BigInteger.valueOf(3)),
                val -> val.mod(BigInteger.valueOf(17)).equals(BigInteger.ZERO),
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
                val -> val.multiply(BigInteger.valueOf(11)),
                val -> val.mod(BigInteger.valueOf(5)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[3].receiveItem(item),
                item -> PUZZLE_MONKEYS[5].receiveItem(item),
                78, 74, 88, 89, 50);
        PUZZLE_MONKEYS[2] = new Monkey(
                val -> val.add(BigInteger.valueOf(4)),
                val -> val.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[6].receiveItem(item),
                item -> PUZZLE_MONKEYS[4].receiveItem(item),
                98, 91);
        PUZZLE_MONKEYS[3] = new Monkey(
                val -> val.multiply(val),
                val -> val.mod(BigInteger.valueOf(13)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[0].receiveItem(item),
                item -> PUZZLE_MONKEYS[5].receiveItem(item),
                59, 72, 94, 91, 79, 88, 94, 51);
        PUZZLE_MONKEYS[4] = new Monkey(
                val -> val.add(BigInteger.valueOf(7)),
                val -> val.mod(BigInteger.valueOf(11)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[7].receiveItem(item),
                item -> PUZZLE_MONKEYS[6].receiveItem(item),
                95, 72, 78);
        PUZZLE_MONKEYS[5] = new Monkey(
                val -> val.add(BigInteger.valueOf(8)),
                val -> val.mod(BigInteger.valueOf(17)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[0].receiveItem(item),
                item -> PUZZLE_MONKEYS[2].receiveItem(item),
                76);
        PUZZLE_MONKEYS[6] = new Monkey(
                val -> val.add(BigInteger.valueOf(5)),
                val -> val.mod(BigInteger.valueOf(19)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[7].receiveItem(item),
                item -> PUZZLE_MONKEYS[1].receiveItem(item),
                69, 60, 53, 89, 71, 88);
        PUZZLE_MONKEYS[7] = new Monkey(
                val -> val.add(BigInteger.valueOf(3)),
                val -> val.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO),
                item -> PUZZLE_MONKEYS[1].receiveItem(item),
                item -> PUZZLE_MONKEYS[3].receiveItem(item),
                72, 54, 63, 80);
    }

    public static void main(String[] args) throws IOException {
        Monkey[] monkeys = TEST_MONKEYS;

        for (int round = 0; round < 20; round++) {
            for (int i = 0; i < monkeys.length; i++) {
                monkeys[i].inspect();
            }
        }
        List<Integer> results = Arrays.stream(monkeys).map(Monkey::getTotalNumberOfInspections).sorted().collect(Collectors.toList());
        System.out.println(results);
    }


    record Item(BigInteger worryLevel) {

    }

    public static class Monkey {
        private final List<Item> items = new ArrayList<>();
        private final Function<BigInteger, BigInteger> operation;
        private final Predicate<BigInteger> test;
        private final Consumer<Item> ifTrue;
        private final Consumer<Item> ifFalse;
        private int totalNumberOfInspections = 0;

        public Monkey(Function<BigInteger, BigInteger> operation, Predicate<BigInteger> test, Consumer<Item> ifTrue, Consumer<Item> ifFalse, int... items) {
            this(operation, test, ifTrue, ifFalse, Arrays.stream(items).mapToObj(BigInteger::valueOf).map(Item::new).toArray(Item[]::new));
        }

        public Monkey(Function<BigInteger, BigInteger> operation, Predicate<BigInteger> test, Consumer<Item> ifTrue, Consumer<Item> ifFalse, Item... items) {
            this.operation = operation;
            this.test = test;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
            this.items.addAll(Arrays.stream(items).toList());
        }

        public void inspect() {
            List<Item> itemsToInspect = List.copyOf(items);
            items.clear();
            for (Item item : itemsToInspect) {
                BigInteger worryLevel = operation.apply(item.worryLevel);
                worryLevel = worryLevel.divide(BigInteger.valueOf(3));
                Item newItem = new Item(worryLevel);
                if (test.test(worryLevel)) {
                    ifTrue.accept(newItem);
                } else {
                    ifFalse.accept(newItem);
                }
            }
            totalNumberOfInspections += itemsToInspect.size();
        }

        public int getTotalNumberOfInspections() {
            return totalNumberOfInspections;
        }

        public void receiveItem(Item item) {
            items.add(item);
        }

        @Override
        public String toString() {
            return "Monkey:" + items;
        }
    }
}

