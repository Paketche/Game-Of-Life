package valchanov.georgi.life;

import java.awt.*;
import java.util.Set;

/**
 * This class represents a single cell - the smallest unit in the game of life.
 */
public class Cell {

    public static final int UNDERPOPULATION = 1;
    public static final int OVERPOPULATION = 4;
    public static final int REPRODUCTION = 3;
    public static final int DESOLATION = 0;

    private final Point location;

    private boolean isAlive;

    /**
     * Creates a dead cell with the given location
     *
     * @param location of the cell
     */
    public Cell(Point location) {
        this.location = location;
        this.isAlive = false;
    }

    /**
     * Creates a living cell and places it in the provided colony
     *
     * @param location of the cell
     * @param colony   that will contain {@code this} cell
     */
    public Cell(Point location, Colony colony) {

        this.location = location;
        this.isAlive = true;
        colony.saveNewbornCell(this);
    }

    /**
     * Returns the location of the cell
     *
     * @return the location of the cell
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Evolves the state of this cell base on the current cell state
     * and the neighbouring cells in the provided colony.
     *
     * @param colony in which this going to save its state
     */
    public void evolve(Colony colony) {
        Set<Cell> neighbours = colony.getCellsSurrounding(this.location);
        int livingNeighbours = (int) neighbours.stream()
                .filter(Cell::isAlive)
                .count();

        if (isAlive) evolveLiveCell(colony, livingNeighbours);
        else evolveDeadCell(colony, livingNeighbours);
    }

    /**
     * Evolves the state of this dead cell.
     * If the number of living neighbours 3 it a new live cell is created in the place of this.
     * However, if this does not have living neighbours it doesn't carry itself to the next generation.
     * In any other case it stays dead.
     *
     * @param colony           in which this going to save its state
     * @param livingNeighbours the number of living neighbours
     */
    private void evolveDeadCell(Colony colony, int livingNeighbours) {
        switch (livingNeighbours) {
            case REPRODUCTION:
                colony.saveNewbornCell(inverseCopy());
                break;
            case DESOLATION:
                //do not carry cell over to next generation
                break;
            default:
                colony.saveCell(this);
        }
    }

    /**
     * Evolves the state of this live cell.
     * If the number of living neighbours is either 2 or 3 it survives.
     * Otherwise it dies.
     *
     * @param colony           in which this going to save its state
     * @param livingNeighbours the number of living neighbours
     */
    private void evolveLiveCell(Colony colony, int livingNeighbours) {

        if (livingNeighbours > UNDERPOPULATION && livingNeighbours < OVERPOPULATION) {
            colony.saveCell(this);
        } else {
            colony.saveCell(inverseCopy());
        }

    }

    /**
     * Returns the status of the cell
     *
     * @return true if the cell is alive; false otherwise
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Creates a copy of this cell with the opposite life status.
     * Method is used to when this changes it state and it need to save the new state while maintaining an
     * old state for current iteration of a colony
     *
     * @return a copy of this cell
     */
    private Cell inverseCopy() {
        Cell copy = new Cell(this.location);
        copy.isAlive = !this.isAlive;
        return copy;
    }
}
