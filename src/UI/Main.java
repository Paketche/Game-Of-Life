package UI;

import life.Cell;
import life.Colony;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    static int[][] makeMatrix(HashMap<Point, Cell> positions) {
        int minWidth = (int) positions.keySet().stream().mapToDouble(Point::getX).min().getAsDouble();
        int maxWidth = (int) positions.keySet().stream().mapToDouble(Point::getX).max().getAsDouble();
        int width = maxWidth - minWidth + 1;


        int minHeight = (int) positions.keySet().stream().mapToDouble(Point::getY).min().getAsDouble();
        int maxHeight = (int) positions.keySet().stream().mapToDouble(Point::getY).max().getAsDouble();
        int height = maxHeight - minHeight + 1;

        int[][] matrix = new int[height][width];
        for (int[] row : matrix) {
            Arrays.fill(row, 0);
        }
        return matrix;
    }

    static void printMatrix(int[][] matrix, HashMap<Point, Cell> positions) {
        int centreY = (int) Math.ceil(matrix.length / 2.0) - 1;
        int centreX = (int) Math.ceil(matrix[0].length / 2.0) - 1;

        positions.forEach((point, cell) -> {

            matrix[point.y + centreY][point.x + centreX] = cell.isAlive() ? 1 : 0;
        });

        for (int[] row : matrix) {
            for (int block : row) {
                System.out.print(block + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Colony colony = new Colony();


        new Cell(new Point(0, 0), colony);
        new Cell(new Point(0, 1), colony);
        new Cell(new Point(-1, 0), colony);
        new Cell(new Point(-1, 1), colony);

        new Cell(new Point(1, -1), colony);
        new Cell(new Point(2, -1), colony);
        new Cell(new Point(1, -2), colony);
        new Cell(new Point(2, -2), colony);


        for (int i = 0; i < 5; i++) {
            HashMap<Point, Cell> newPopulation = colony.iterate();
            int[][] matrix = makeMatrix(newPopulation);
            printMatrix(matrix, newPopulation);
            System.out.println();
        }
    }
}
