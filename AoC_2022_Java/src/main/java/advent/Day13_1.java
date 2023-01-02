package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Day13_1 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day13.txt");
        List<String> lines = Files.lines(path).toList();
        parse(new ArrayList<>(lines));
    }

    public static void parse(List<String> lines) {
        int index = 1;
        long sum = 0;
        while (!lines.isEmpty()) {
            if (lines.get(0).isBlank()) {
                lines.remove(0);
            }
            String left = lines.remove(0);
            String right = lines.remove(0);
            ChainList leftChain = (ChainList) parseList(left.trim()).chain;
            ChainList rightChain = (ChainList) parseList(right.trim()).chain;
            if (leftChain.compareTo(rightChain) == 1) {
                sum += index;
                System.out.println(index);
                System.out.println(sum);
                System.out.println(leftChain + " " + rightChain);
            }
            index++;
        }
    }


    public enum States {STARTING, TOKEN}

    public static ParsingResult parseList(String line) {
        List<Chain> chains = new ArrayList<>();
        States state = States.STARTING;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            char tempChar = line.charAt(i);
            switch (tempChar) {
                case '[': {
                    if (state == States.STARTING) {
                        state = States.TOKEN;
                    } else if (state == States.TOKEN) {
                        ParsingResult parsingResult = parseList(line.substring(i));
                        chains.add(parsingResult.chain);
                        i += parsingResult.positionEnded;
                    }
                    break;
                }
                case ']': {
                    if (state == States.STARTING) {
                        throw new IllegalStateException();
                    } else if (state == States.TOKEN) {
                        String materializedBuffer = buffer.toString().trim();
                        if (!materializedBuffer.isBlank()) {
                            Value value = parseValue(materializedBuffer);
                            chains.add(value);
                        }
                        return new ParsingResult(new ChainList(chains), i);
                    }
                }
                case ',': {
                    if (state == States.STARTING) {
                        throw new IllegalStateException();
                    } else if (state == States.TOKEN) {
                        String materializedBuffer = buffer.toString().trim();
                        if (!materializedBuffer.isBlank()) {
                            Value value = parseValue(materializedBuffer);
                            chains.add(value);
                        }
                        buffer = new StringBuffer();
                    }
                    break;
                }
                default: {
                    buffer.append(tempChar);
                }
            }
        }
        throw new IllegalStateException();
    }

    public static Value parseValue(String value) {
        return new Value(Integer.parseInt(value));
    }

    public static abstract class Chain implements Comparable<Chain> {

    }

    record ParsingResult(Chain chain, int positionEnded) {

    }

    public static class ChainList extends Chain {
        private final List<Chain> chainList;

        public ChainList(List<Chain> chainList) {
            this.chainList = List.copyOf(chainList);
        }

        @Override
        public int compareTo(Chain chain) {
            if (chain instanceof ChainList) {
                Iterator<Chain> left = chainList.iterator();
                Iterator<Chain> right = ((ChainList) chain).chainList.iterator();
                while (left.hasNext() && right.hasNext()) {
                    Chain leftChain = left.next();
                    Chain rightChain = right.next();
                    if (leftChain.compareTo(rightChain) >= 1) {
                        return 1;
                    } else if (leftChain.compareTo(rightChain) <= -1) {
                        return -1;
                    }
                }
                if (left.hasNext()) {
                    return -1;
                }
                if (right.hasNext()) {
                    return 1;
                }
                return 0;
            }
            if (chain instanceof Value) {
                return this.compareTo(new ChainList(List.of(chain)));
            }
            throw new IllegalStateException();
        }

        @Override
        public String toString() {
            return chainList.stream().map(Chain::toString).collect(Collectors.joining(",", "[", "]"));
        }
    }

    public static class Value extends Chain {
        private final int value;

        public Value(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Chain chain) {
            if (chain instanceof Value) {
                return -Integer.compare(value, ((Value) chain).value);
            } else if (chain instanceof ChainList) {
                return new ChainList(List.of(this)).compareTo(chain);
            }
            throw new IllegalStateException();
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }


}

