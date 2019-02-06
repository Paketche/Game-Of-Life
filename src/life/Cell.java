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


    public Cell(Point location) {
        this.location = location;
        this.isAlive = false;
    }

    public Cell(Point location, Colony colony) {

        this.location = location;
        this.isAlive = true;
        colony.saveNewbornCell(this);
    }

    public Point getLocation() {
        return location;
    }

    public void evolve(Colony colony) {
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
                colony.saveNewbornCell(inverseCopy());
                break;
            case DESOLATION:
                //do not carry cell over to next generation
                break;
            default:
                colony.saveCell(this);
        }
    }

    private void evolveLiveCell(Colony colony) {
        Set<Cell> neighbours = colony.getCellsSurrounding(this.location);
        int livingNeighbours = (int) neighbours.stream()
                .filter(Cell::isAlive)
                .count();

        if (livingNeighbours > UNDERPOPULATION && livingNeighbours < OVERPOPULATION){
            colony.saveCell(this);
        }
        else{
            colony.saveCell(inverseCopy());
        }

    }

    public boolean isAlive() {
        return isAlive;
    }

    private Cell inverseCopy(){
        Cell copy =  new Cell(this.location);
        copy.isAlive = !this.isAlive;
        return  copy;
    }
}
