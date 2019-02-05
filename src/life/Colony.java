package life;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toSet;

public class Colony {
    private final HashMap<Point, Cell> colonyMap;
    private final HashMap<Point, Cell> newCells;
    private final HashSet<Point> oldCell;

    public Colony() {
        colonyMap = new HashMap<>();
        newCells = new HashMap<>();
        oldCell = new HashSet<>();
    }


    public void saveCell(Cell cell) {
        colonyMap.put(
                cell.getLocation(),
                cell
        );
    }

    public Set<Cell> getCellsSurrounding(final Point location) {
        List<Point> neighbours = new ArrayList<>();
        lookAround((x, y) ->
                neighbours.add(new Point(
                        x + location.x,
                        y + location.y
                ))
        );

        return neighbours.stream()
                .map(colonyMap::get)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public HashMap<Point, Cell> iterate() {
        newCells.forEach((key, value) -> colonyMap.computeIfAbsent(key, (k) -> value));
        newCells.clear();

        oldCell.forEach(colonyMap::remove);
        oldCell.clear();

        colonyMap.values().forEach(cell -> cell.survive(this));

        colonyMap.values().stream().sorted((p1, p2) -> {
            if (p1.getLocation().y == p2.getLocation().y) {
                return Integer.compare(p1.getLocation().x, p2.getLocation().x);
            } else {
                return Integer.compare(p1.getLocation().y, p2.getLocation().y);
            }
        }).forEach(
                cell -> cell.evolve(this)
        );

        return colonyMap;
    }

    public void deleteCellAt(Point location) {
        oldCell.add(location);
    }

    public void createDeadCellsAround(Point cellLocation) {
        ArrayList<Point> locations = neighbouringCellLocations(cellLocation);

        for (Point location : locations) {
            newCells.put(location, new Cell(location));
        }
    }

    private ArrayList<Point> neighbouringCellLocations(Point location) {
        ArrayList<Point> locations = new ArrayList<>();

        lookAround((x, y) -> locations.add(new Point(
                x + location.x,
                y + location.y
        )));

        return locations;
    }

    private void lookAround(BiConsumer<Integer, Integer> consumer) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;
                consumer.accept(x, y);
            }
        }
    }
}
