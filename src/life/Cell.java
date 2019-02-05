package life;

import java.awt.*;
import java.util.Set;

public class Cell {

    public static final int UNDERPOPULATION = 1;
    public static final int OVERPOPULATION = 4;
    public static final int REPRODUCTION = 3;
    public static final int DESOLATION = 0;

    private final Point location;

    private boolean isAlive;
    private boolean willSurvive;
    private boolean toBeDeleted;

    public Cell(Point location) {
        this.location = location;
        this.isAlive = false;
        this.willSurvive = false;
        this.toBeDeleted = false;
    }

    public Cell(Point location, Colony colony) {

        this.location = location;
        this.isAlive = true;
        this.willSurvive = true;
        this.toBeDeleted = false;

        colony.saveCell(this);
        colony.createDeadCellsAround(this.location);
    }

    public Point getLocation() {
        return location;
    }

    public void evolve(Colony colony) {
        if (location.x == 1 && location.y == -1) {
            System.out.print("");
        }

        if (isAlive) evolveLiveCell(colony);
        else evolveDeadCell(colony);
    }

    private void evolveDeadCell(Colony colony) {
        Set<Cell> neighbours = colony.getCellsSurrounding(this.location);
        int livingNeighbours = (int) neighbours.stream()
                .filter(Cell::isAlive)
                .count();

        switch (livingNeighbours) {
            case REPRODUCTION:
                this.willSurvive = true;
                colony.createDeadCellsAround(this.location);
                break;
            case DESOLATION:
                toBeDeleted = true;
            default:
                this.willSurvive = false;
        }
    }

    private void evolveLiveCell(Colony colony) {
        Set<Cell> neighbours = colony.getCellsSurrounding(this.location);
        int livingNeighbours = (int) neighbours.stream()
                .filter(Cell::isAlive)
                .count();

        willSurvive = livingNeighbours > UNDERPOPULATION && livingNeighbours < OVERPOPULATION;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void survive(Colony colony) {
        this.isAlive = willSurvive;
        this.willSurvive = false;
        if (toBeDeleted)
            colony.deleteCellAt(this.location);
    }
}
