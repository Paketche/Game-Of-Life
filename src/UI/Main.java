package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Main extends Application {

//    static int[][] makeMatrix(HashMap<Point, Cell> positions) {
//        int minWidth = (int) positions.keySet().stream().mapToDouble(Point::getX).min().getAsDouble();
//        int maxWidth = (int) positions.keySet().stream().mapToDouble(Point::getX).max().getAsDouble();
//        int width = maxWidth - minWidth + 1;
//
//
//        int minHeight = (int) positions.keySet().stream().mapToDouble(Point::getY).min().getAsDouble();
//        int maxHeight = (int) positions.keySet().stream().mapToDouble(Point::getY).max().getAsDouble();
//        int height = maxHeight - minHeight + 1;
//
//        int[][] matrix = new int[height][width];
//
//        return matrix;
//    }
//
//    static void printMatrix(int[][] matrix, HashMap<Point, Cell> positions) {
//        int centreY = (int) Math.ceil(matrix.length / 2.0) - 1;
//        int centreX = (int) Math.ceil(matrix[0].length / 2.0) - 1;
//
//        positions.forEach((point, cell) -> {
//
//            try {
//                matrix[centreY - point.y][centreX + point.x] = cell.isAlive() ? 1 : 0;
//            }catch (IndexOutOfBoundsException e){
////                System.out.println(point);
//            }
//        });
//
//        for (int[] row : matrix) {
//            for (int block : row) {
//                System.out.print(block + " ");
//            }
//            System.out.println();
//        }
//    }

    public static void main(String[] args) {

//        Colony colony = new Colony();


//        new Cell(new Point(0, 0), colony);
//        new Cell(new Point(0, 1), colony);
//        new Cell(new Point(-1, 0), colony);
//        new Cell(new Point(-1, 1), colony);
//
//        new Cell(new Point(1, -1), colony);
//        new Cell(new Point(2, -1), colony);
//        new Cell(new Point(1, -2), colony);
//        new Cell(new Point(2, -2), colony);
//
//        new Cell(new Point(0, 0), colony);
//        new Cell(new Point(1, 0), colony);
//        new Cell(new Point(-1, 0), colony);

//        new Cell(new Point(0, 0), colony);
//        new Cell(new Point(1, 0), colony);
//        new Cell(new Point(2, 0), colony);
//
//
//        new Cell(new Point(-1, -1), colony);
//        new Cell(new Point(0, -1), colony);
//        new Cell(new Point(1, -1), colony);

//        new Cell(new Point(-1, 0), colony);
//        new Cell(new Point(1, 0), colony);
//        new Cell(new Point(1, 1), colony);
//        new Cell(new Point(0, -1), colony);
//        new Cell(new Point(1, -1), colony);

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        GridPane root = new GridPane();
//        root.setAlignment(Pos.CENTER);
//        root.setVgap(10);
//        root.setHgap(10);
//
//        Label label = new Label("Welcome to JavaFX!");
//        label.setTextFill(Color.GREEN);
//        label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 70));

//        root.getChildren().add(label);

        primaryStage.setTitle("Hello JavaFX!");
        primaryStage.setScene(new Scene(root, 700, 550));
        primaryStage.show();
    }
}
