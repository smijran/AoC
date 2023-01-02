package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10_1 {
    final static Pattern ADDX_PATTERN = Pattern.compile("addx ([\\-0-9]+)");


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day10.txt");
        List<String> lines = Files.lines(path).toList();
        Cpu cpu = new Cpu();

        for (String line : lines) {
            if (line.trim().equals("noop")) {
                cpu.loadOrder(new Noop());
            }
            Matcher matcher = ADDX_PATTERN.matcher(line);
            if (matcher.matches()) {
                int operand = Integer.parseInt(matcher.group(1));
                cpu.loadOrder(new Addx(operand));
            }
        }
        int result = 0;
        for (int cycle = 2; cycle < 240; cycle++) {
            cpu.cycle();
            System.out.println(cycle + " " + cpu.register);
            if ((cycle - 20) % 40 == 0) {
                result += cycle * cpu.register.value;
            }
        }
        System.out.println(result);
    }

    public record Register(int value) {
    }

    public record OrderResult(Register register, boolean completed) {
    }

    public interface Order {
        OrderResult cycle(Register register);

    }


    public static class Noop implements Order {

        @Override
        public OrderResult cycle(Register register) {
            return new OrderResult(register, true);
        }

        @Override
        public String toString() {
            return "Noop{}";
        }
    }

    public static class Addx implements Order {

        private final int operand;
        private boolean prepare = true;

        public Addx(int operand) {
            this.operand = operand;
        }

        @Override
        public OrderResult cycle(Register register) {
            if (prepare) {
                prepare = false;
                return new OrderResult(register, false);
            }
            return new OrderResult(new Register(register.value() + operand), true);
        }

        @Override
        public String toString() {
            return "Addx{" +
                    "operand=" + operand +
                    '}';
        }
    }

    public static class Cpu {
        private final Queue<Order> program = new ArrayDeque<>();
        private Register register = new Register(1);

        public void loadOrder(Order order) {
            program.offer(order);
        }

        public void cycle() {
            if (program.peek() == null) {
                throw new IllegalStateException("Program has ended.");
            }
            Order order = program.peek();
            System.out.println(order);
            OrderResult result = order.cycle(register);
            register = result.register;
            if (result.completed()) {
                program.poll();
            }
        }
    }

}

