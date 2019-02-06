package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import life.Cell;
import life.Colony;

import java.awt.*;
import java.util.Set;

public class ColonyView extends Canvas {

    private final GraphicsContext context;

    private double offsetX;
    private double offsetY;

    private double cellDimension;
    private double cellMargin;

    private Color blockColor = Color.BLACK;
    private Color backGroundColor = Color.WHITE;


    public ColonyView(int hCellCount, int vCellCount, double cellDimension, double cellMargin) {
        super(
                hCellCount * (cellDimension + cellMargin),
                vCellCount * (cellDimension + cellMargin)
        );
        this.context = super.getGraphicsContext2D();
        this.cellDimension = cellDimension;
        this.cellMargin = cellMargin;

        this.offsetX = getWidth() / 2;
        this.offsetY = getHeight() / 2;
    }

    public void render(Colony colony) {
        resetCanvas();
        Set<Cell> cells = colony.getLiveCells();

        context.setFill(blockColor);
        cells.stream()
                .map(Cell::getLocation)
                .map(CellView::new)
                .forEach(cellView -> cellView.render(context));
    }

    public void resetCanvas() {
        context.setFill(this.backGroundColor);
        context.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void setDarkTheme() {
        blockColor = Color.WHITE;
        backGroundColor = Color.BLACK;
    }

    public void setLightTheme() {
        backGroundColor = Color.WHITE;
        blockColor = Color.BLACK;
    }

    private class CellView {

        private final double x;
        private final double y;
        private final double cellSize;

        public CellView(Point point) {
            this.x = offsetX + point.x * (cellMargin + cellDimension);
            this.y = offsetY - point.y * (cellMargin + cellDimension);
            cellSize = cellDimension;
        }

        public void render(GraphicsContext context) {
            context.fillRect(x, y, cellSize, cellSize);
        }
    }
}
