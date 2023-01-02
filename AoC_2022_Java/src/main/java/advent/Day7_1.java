package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7_1 {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day7.txt");
        List<String> lines = Files.lines(path).toList();
        DirEntry root = new DirEntry(null, "/");
        DirEntry current = root;
        for (String line : lines) {
            if (line.startsWith("$")) {
                String command = line.substring(1).trim();
                if (command.startsWith("cd")) {
                    String folder = command.substring(2).trim();
                    switch (folder) {
                        case "/" -> current = root;
                        case ".." -> current = current.getParent().orElseThrow(IllegalStateException::new);
                        default -> current = current.getDirChild(folder);
                    }
                    continue;
                } else if (command.equals("ls")) {
                    continue;
                }
                throw new IllegalArgumentException(line);
            } else {
                Entry entry = readEntry(current, line);
                current.addChild(entry);
            }
        }

        AtomicInteger integer = new AtomicInteger(0);
        root.visit(entry -> {
            if (entry instanceof DirEntry) {
                if (entry.getSize() < 100000) {
                    integer.addAndGet(entry.getSize());
                }
            }
        });

        System.out.println(integer.get());
    }

    private final static Pattern DIR_PATTERN = Pattern.compile("dir (.+)");
    private final static Pattern FILE_PATTERN = Pattern.compile("(\\d+) (.+)");

    private static Entry readEntry(DirEntry current, String line) {
        Matcher dirMatcher = DIR_PATTERN.matcher(line);
        Matcher fileMatcher = FILE_PATTERN.matcher(line);
        if (dirMatcher.matches()) {
            return new DirEntry(current, dirMatcher.group(1));
        } else if (fileMatcher.matches()) {
            return new FileEntry(current, fileMatcher.group(2), Integer.parseInt(fileMatcher.group(1)));
        }
        throw new IllegalArgumentException(line);
    }

    static abstract class Entry {
        private final DirEntry parent;
        private final String name;

        protected Entry(DirEntry parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Optional<DirEntry> getParent() {
            return Optional.ofNullable(parent);
        }

        abstract int getSize();

        abstract void visit(Consumer<Entry> consumer);
    }

    static class DirEntry extends Entry {
        private final List<Entry> children = new ArrayList<>();

        DirEntry(DirEntry parent, String name) {
            super(parent, name);
        }

        @Override
        int getSize() {
            return children.stream().mapToInt(Entry::getSize).sum();
        }

        @Override
        void visit(Consumer<Entry> consumer) {
            consumer.accept(this);
            children.forEach(child -> child.visit(consumer));
        }


        void addChild(Entry entry) {
            children.add(entry);
        }

        public DirEntry getDirChild(String folder) {
            return children.stream().filter(DirEntry.class::isInstance).map(DirEntry.class::cast).filter(dirEntry -> dirEntry.getName().equals(folder)).findFirst().orElseThrow();
        }

        @Override
        public String toString() {
            return "dir " + getName();
        }

    }

    static class FileEntry extends Entry {
        private final int size;

        FileEntry(DirEntry parent, String name, int size) {
            super(parent, name);
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        @Override
        void visit(Consumer<Entry> consumer) {
            consumer.accept(this);
        }

        @Override
        public String toString() {
            return "file " + getName();
        }
    }
}

