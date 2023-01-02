package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16_1 {


    public static void main(String[] args) throws IOException {
        Path path = Path.of("/Users/koszalko/adventOfCode2022/input_day16.txt");
        Set<Valve> valveSet =
                Files
                        .lines(path)
                        .map(Day16_1::parse)
                        .collect(Collectors.toSet());
        World world = new World(valveSet);
        GameState topGameState = new GameState(world, "AA", Collections.emptySet(), 0, 0, List.of());
        GameState best = searchBest(topGameState);
        System.out.println(best);
    }

    public static GameState searchBest(GameState start) {
        GameState best = start;
        final Queue<GameState> gameStates = new PriorityQueue<>(Comparator.comparing(GameState::getAlreadyReleased).reversed());
        gameStates.add(start);
        while (!gameStates.isEmpty()) {
            GameState gameState = gameStates.poll();
            if (gameState.history.size() == TIME_BUDGET && best.getAlreadyReleased() < gameState.getAlreadyReleased()) {
                System.out.println(gameState);
                best = gameState;
            }
            Set<GameState> possibleGameStates = gameState.possibleGameStates();
            gameStates.addAll(possibleGameStates);
        }
        return best;
    }

    final static int TIME_BUDGET = 30;

    interface Action {

    }

    record Open(String name) implements Action {
    }

    record Move(String name) implements Action {
    }

    record No() implements Action {
    }

    record Valve(String name, int pressure, Set<String> accessibleValves) {
    }

    public static class World {
        private final Map<String, Valve> valves;
        private final Set<String> meaningfulValves;

        public World(Set<Valve> valves) {
            this.valves = valves.stream().collect(Collectors.toMap(Valve::name, Function.identity()));
            this.meaningfulValves = valves.stream().filter(valve -> valve.pressure > 0).map(Valve::name).collect(Collectors.toSet());
        }

        public Valve getValve(String name) {
            return valves.get(name);
        }

        public Set<String> getMeaningfulValves() {
            return meaningfulValves;
        }
    }

    public static final class GameState {
        private final World world;
        private final String currentValve;
        private final Set<String> valvesOpen;
        private final int openValvesFlow;
        private final int alreadyReleased;
        private final List<Action> history;

        public GameState(World world,
                         String currentValve,
                         Set<String> valvesOpen,
                         int openValvesFlow,
                         int alreadyReleased,
                         List<Action> history) {
            this.world = world;
            this.currentValve = currentValve;
            this.valvesOpen = valvesOpen;
            this.openValvesFlow = openValvesFlow;
            this.alreadyReleased = alreadyReleased;
            this.history = history;
        }

        public int getAlreadyReleased() {
            return alreadyReleased;
        }

        public Set<GameState> possibleGameStates() {
            if (history.size() == TIME_BUDGET) {
                return Set.of();
            }
            if (valvesOpen.containsAll(world.getMeaningfulValves())) {
                return Set.of(noMove());
            }
            Set<GameState> result = new HashSet<>();
            boolean currentValveNotOpen = !valvesOpen.contains(currentValve);
            Valve currentValveObject = world.getValve(currentValve);
            if (currentValveNotOpen && currentValveObject.pressure > 0) {
                result.add(openCurrentValve(currentValveObject));
            }
            Action action = history.isEmpty() ? null : history.get(history.size() - 1);
            String forbidden = "";
            if (action instanceof Move) {
                forbidden = ((Move) action).name;
            }
            for (String accessibleValve : currentValveObject.accessibleValves) {
                if (accessibleValve.equals(forbidden)) {
                    continue;
                }
                Valve valve = world.getValve(accessibleValve);
                result.add(runToValve(valve));
            }
            return result;
        }

        private GameState openCurrentValve(Valve valve) {
            final Set<String> valvesOpen = new HashSet<>();
            valvesOpen.add(currentValve);
            valvesOpen.addAll(this.valvesOpen);
            final int currentFlow = openValvesFlow + valve.pressure;
            final List<Action> history = new ArrayList<>(this.history);
            history.add(new Open(currentValve));
            return new GameState(world, currentValve, valvesOpen, currentFlow, alreadyReleased + openValvesFlow, history);
        }

        private GameState runToValve(Valve valve) {
            List<Action> history = new ArrayList<>(this.history);
            history.add(new Move(valve.name));
            return new GameState(world, valve.name, valvesOpen, openValvesFlow, alreadyReleased + openValvesFlow, history);
        }

        private GameState noMove() {
            List<Action> history = new ArrayList<>(this.history);
            int remainingMinutes = TIME_BUDGET - history.size();
            while (history.size() < 30) {
                history.add(new No());
            }
            return new GameState(world, currentValve, valvesOpen, openValvesFlow, alreadyReleased + openValvesFlow * remainingMinutes, history);
        }

        @Override
        public String toString() {
            return "GameState{" +
                    "current=" + currentValve +
                    ", valvesOpen=" + valvesOpen +
                    ", alreadyReleased=" + alreadyReleased +
                    ", history=" + history +
                    '}';
        }
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("Valve ([A-Z]{2}) has flow rate=([-+]?\\d*); tunnel(s)? lead(s)? to valve(s)? ([A-Z\\,\\ ]+)");

    private static Valve parse(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new Valve(matcher.group(1),
                    Integer.parseInt(matcher.group(2)),
                    Arrays.stream(matcher.group(6).split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet()));
        }
        throw new IllegalArgumentException(line);
    }


}

