package life;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toSet;

public class Colony {
    private final HashMap<Point, Cell> currentColony;
    private final HashMap<Point, Cell> nextColony;


    public Colony() {
        currentColony = new HashMap<>();
        nextColony = new HashMap<>();
    }

    /**
     * Creates a new Colony filling it with cells as described from
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Colony fromCSV(Path path) throws IOException {
        List<String> rows = Files.readAllLines(path);

        if (rows.size() == 0) {
            throw new IllegalArgumentException("File does not contain values");
        }
        long cellsPerRow = rows.get(0).chars().filter(ch -> ch == ',').count() + 1;

        int xOffset = (int) Math.floor(cellsPerRow / 2.0);
        int yOffset = (int) Math.floor(rows.size() / 2.0);

        Colony colony = new Colony();

        for (int y = 0; y < rows.size(); y++) {

            String[] values = rows.get(y).split(",");

            if (values.length != cellsPerRow)
                throw new IllegalArgumentException("Invalid csv format. Each row must have the same number of cells");

            for (int x = 0; x < values.length; x++)
                if (values[x].equals("1"))
                    new Cell(
                            new Point(x - xOffset, yOffset - y),
                            colony
                    );
        }

        return colony;
    }

    public void saveCell(Cell cell) {
        nextColony.put(
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
                .map(currentColony::get)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public HashMap<Point, Cell> iterate() {

        this.currentColony.clear();
        this.currentColony.putAll(nextColony);
        this.nextColony.clear();

        currentColony.values().forEach(
                cell -> cell.evolve(this)
        );

        return currentColony;
    }


    public void saveNewbornCell(Cell newborn) {

        saveCell(newborn);

        ArrayList<Point> locations = neighbouringCellLocations(newborn.getLocation());

        locations.stream()
                .map(Cell::new)
                .forEach(cell -> nextColony.computeIfAbsent(cell.getLocation(), l -> cell));
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

    public Set<Cell> getLiveCells() {
        return currentColony.values()
                .stream()
                .filter(Cell::isAlive)
                .collect(toSet());
    }
}
