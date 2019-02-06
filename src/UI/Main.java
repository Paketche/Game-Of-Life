package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import life.Colony;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Game Of Life");
        primaryStage.setResizable(false);

        Colony colony = new Colony();

        ColonyView view = new ColonyView(100, 50, 15, 1);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        BorderPane root = loader.load();

        ColonyController colonyController = loader.getController();
        colonyController.setColony(colony);
        colonyController.setColonyView(view);
        colonyController.setStage(primaryStage);

        root.setCenter(view);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        view.resetCanvas();
        colonyController.startAnimation(Duration.millis(500));
    }
}
