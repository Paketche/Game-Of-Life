package UI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import life.Cell;
import life.Colony;

import java.awt.*;
import java.util.Set;

/**
 * This class takes care of the displaying of a colony on a canvas.
 */
public class ColonyView extends Canvas {

    private final GraphicsContext context;

    private double offsetX;
    private double offsetY;

    private final double initialOffsetX;
    private final double initialOffsetY;

    private double cellDimension;
    private double cellMargin;

    private Color blockColor = Color.BLACK;
    private Color backGroundColor = Color.WHITE;


    /**
     * Creates a new object with the specified dimensions. The underlining canvas dimensions will
     * be set to accommodate requested number and size of cells.
     *
     * @param hCellCount    number of cells to be displayed horizontally
     * @param vCellCount    number of cells to be displayed vertically
     * @param cellDimension the height and width of the cell in pixels
     * @param cellMargin    the margin between neighbouring cells
     */
    public ColonyView(int hCellCount, int vCellCount, double cellDimension, double cellMargin) {
        super(
                hCellCount * (cellDimension + cellMargin),
                vCellCount * (cellDimension + cellMargin)
        );
        this.context = super.getGraphicsContext2D();
        this.cellDimension = cellDimension;
        this.cellMargin = cellMargin;

        //used for transforming cartesian to polar coordinates
        this.offsetX = this.initialOffsetX = getWidth() / 2;
        this.offsetY = this.initialOffsetY = getHeight() / 2;
    }

    /**
     * Renders a colony
     *
     * @param colony to be rendered
     */
    public void render(Colony colony) {
        resetCanvas();

        context.setFill(blockColor);

        colony.getLiveCells()
                .stream()
                .map(Cell::getLocation)
                .map(CellView::new)
                .forEach(cellView -> cellView.render(context));
    }

    /**
     * Clears the canvas.
     */
    public void resetCanvas() {
        context.setFill(this.backGroundColor);
        context.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Changes the block color to white and the background to black
     */
    public void setDarkTheme() {
        blockColor = Color.WHITE;
        backGroundColor = Color.BLACK;
    }

    /**
     * Changes the block color to black and the background to white
     */
    public void setLightTheme() {
        backGroundColor = Color.WHITE;
        blockColor = Color.BLACK;
    }

    /**
     * Shifts the horizontal and vertical offsets that translate cartesian to polar coordinates
     *
     * @param offsetX the amount that the horizontal axis is going to be shifted
     *                (negative numbers shift to the right, positive to the left)
     * @param offsetY the amount that the vertical axis is going to be shifted
     *                (negative numbers shift upwards, positive downwards)
     */
    public void shiftOffsets(double offsetX, double offsetY) {
        System.out.println("Shifting x by " + offsetX + "y by " + offsetY);
        this.offsetX += offsetX;
        this.offsetY += offsetY;
    }

    /**
     * Resets the center offset to its initial value
     */
    public void resetOffset() {
        this.offsetX = this.initialOffsetX;
        this.offsetY = this.initialOffsetY;
    }

    /**
     * This represents a the view of a single sell
     */
    private class CellView {

        private final double x;
        private final double y;
        private final double cellSize;

        /**
         * Creates a new cell view from the provided cartesian coordinates
         *
         * @param point cartesian coordinates of a cell model
         */
        public CellView(Point point) {
            //transform cartesian to polar coordinates
            this.x = offsetX + point.x * (cellMargin + cellDimension);
            this.y = offsetY - point.y * (cellMargin + cellDimension);
            cellSize = cellDimension;
        }

        /**
         * Render the cell
         *
         * @param context on which the cell will be rendered
         */
        public void render(GraphicsContext context) {
            context.fillRect(x, y, cellSize, cellSize);
        }
    }
}
