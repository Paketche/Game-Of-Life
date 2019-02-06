package life;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.stream.Collectors.toSet;

/**
 * This class represent a collection of cells that evolve together and interact with each other
 */
public class Colony {
    private HashMap<Point, Cell> currentColony;
    private HashMap<Point, Cell> nextColony;
    private ReadWriteLock lock;


    /**
     * Creates an empty colony
     */
    public Colony() {
        currentColony = new HashMap<>();
        nextColony = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    /**
     * Creates a new Colony filling it with cells as specified in a CSV file.
     * The file pointed by the path has to have the same number of values on each row.
     * Only the value of 1 is consider to be a live cell. Everything else is will read as a dead cell.
     *
     * @param path to the CSV file that contains a Game-OF-Life pattern
     * @return a colony populated with cells as specified by the file
     * @throws IOException              if an I/O error occurs reading from the file
     * @throws IllegalArgumentException if not all of the row contain the same number of values
     */
    public static Colony fromCSV(Path path) throws IOException, IllegalArgumentException {
        List<String> rows = Files.readAllLines(path);

        if (rows.size() == 0) {
            throw new IllegalArgumentException("File does not contain values");
        }
        long cellsPerRow = rows.get(0).chars().filter(ch -> ch == ',').count() + 1;

        //for converting polar coordinates to cartesian
        int xOffset = (int) Math.floor(cellsPerRow / 2.0);
        int yOffset = (int) Math.floor(rows.size() / 2.0);

        Colony colony = new Colony();

        //y and x are polar coordinate based on the position of a value in the file
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

    /**
     * Creates saves a new cell in the colony
     *
     * @param cell to be saved
     */
    public void saveCell(Cell cell) {
        nextColony.put(
                cell.getLocation(),
                cell
        );
    }

    /**
     * Retrieves the existing cells neighbouring the specified position
     *
     * @param location of which existing cells will be treived
     * @return the neighbouring cells
     */
    public Set<Cell> getCellsSurrounding(final Point location) {
        List<Point> neighbours = neighbouringCellLocations(location);

        try {
            this.lock.readLock().lock();
            return neighbours.stream()
                    .map(currentColony::get)
                    .filter(Objects::nonNull)//not all location will have a corresponding cell
                    .collect(toSet());

        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Iterates over the current generation of cell, evolving each one of them.
     */
    public void iterate() {

        currentColony.values().forEach(
                cell -> cell.evolve(this)
        );

        this.lock.writeLock().lock();
        this.currentColony = nextColony;
        this.nextColony = new HashMap<>();
        this.lock.writeLock().unlock();
    }


    /**
     * Saves a newborn Cell to the colony. A new born cell would be one that has changed its state from dead to alive
     *
     * @param newborn that is to be saved
     */
    public void saveNewbornCell(Cell newborn) {

        saveCell(newborn);

        ArrayList<Point> locations = neighbouringCellLocations(newborn.getLocation());

        //create dead cells surrounding this live cell.
        // This way they can be iterated over and can become alive
        locations.stream()
                .map(Cell::new)
                .forEach(
                        //this will not override live cells
                        cell -> nextColony.computeIfAbsent(
                                cell.getLocation(),
                                l -> cell
                        )
                );

    }

    /**
     * Retrieves a list of positions surrounding the provided location
     *
     * @param location around which location objects are going to be created
     * @return list of surrounding locations
     */
    private ArrayList<Point> neighbouringCellLocations(Point location) {
        ArrayList<Point> locations = new ArrayList<>();

        // loop through neighbours
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;

                locations.add(new Point(
                        x + location.x,
                        y + location.y
                ));
            }


        return locations;
    }

    /**
     * Retrieves a set of all the live cells
     *
     * @return a set of all live cells
     */
    public Set<Cell> getLiveCells() {
        try {
            this.lock.readLock().lock();
            return currentColony.values()
                    .stream()
                    .filter(Cell::isAlive)
                    .collect(toSet());

        } finally {
            this.lock.readLock().unlock();
        }
    }
}
