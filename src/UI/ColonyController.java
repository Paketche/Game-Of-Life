package UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import life.Colony;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.*;

/**
 * Represents an MVC controller that controls the a Colony model and visualizes it with a Colony view object.
 */
public class ColonyController extends Service<Colony> {

    private Colony colony;
    private ColonyView colonyView;
    private Timeline timeline;
    private Stage stage;
    private FileChooser fileChooser;
    private Task<Colony> colonyTask;
    private Alert alert;

    @FXML
    private Slider speed = null;

    /**
     * Creates a new controller object. This constructors is to be used only when the controllers is parsed to a FXML file.
     */
    public ColonyController() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select colony file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error reading file");
        alert.getDialogPane().paddingProperty().setValue(new Insets(10, 20,10, 20));
    }

    /**
     * Creates a new Controller object with the provided view and model.
     *
     * @param colonyModel the colony that is to be controlled
     * @param colonyView  the view object that is going to be used to render the colony
     */
    public ColonyController(Stage stage, Colony colonyModel, ColonyView colonyView) {
        this();
       setColony(colonyModel);
       setStage(stage);
       setColonyView(colonyView);
    }

    @Override
    protected Task<Colony> createTask() {
        return new Task<Colony>() {
            @Override
            protected Colony call() throws Exception {
                colony.iterate();

                //run on main application thread
                Platform.runLater(()-> colonyView.render(colony));
                return colony;
            }
        };
    }

    @FXML
    public void initialize() {
        speed.valueProperty().addListener(this::changeSpeed);
    }


    private void iterateColony(ActionEvent event) {

        if (isNull(colonyTask) || colonyTask.isDone()) {
            this.colonyTask = this.createTask();
            this.executeTask(colonyTask);
        }

        event.consume();
    }

    /**
     * Starts the animation of the i
     *
     * @param time the duration that each colony generation will be displayed
     */
    public void startAnimation(Duration time) {
        if (isNull(colony) || isNull(colonyView) || isNull(stage))
            throw new NullPointerException("Either the colony, the colony view or the stage has not been set");

        timeline = new Timeline(new KeyFrame(time, this::iterateColony));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void play(Event event) {
        if (nonNull(timeline)) {
            timeline.play();
        }

        event.consume();
    }

    @FXML
    public void pause(ActionEvent event) {
        if (nonNull(timeline))
            timeline.pause();

        event.consume();
    }

    @FXML
    public void stepThrough(ActionEvent event) {
        if (nonNull(timeline))
            timeline.pause();

        iterateColony(event);
    }

    @FXML
    public void setDarkTheme(ActionEvent event) {
        colonyView.setDarkTheme();
        colonyView.render(colony);
        event.consume();
    }

    @FXML
    public void setLightTheme(ActionEvent event) {
        colonyView.setLightTheme();
        colonyView.render(colony);
        event.consume();
    }

    public void setColony(Colony colony) {
        this.colony = requireNonNull(colony);
    }

    public void setColonyView(ColonyView colonyView) {
        this.colonyView = requireNonNull(colonyView);
        this.colonyView.setCursor(Cursor.OPEN_HAND);
    }

    public void setStage(Stage stage) {
        this.stage = requireNonNull(stage);
    }

    @FXML
    public void openFile(ActionEvent event) {
        pause(event);
        try {
            File file = fileChooser.showOpenDialog(stage);
            if (nonNull(file)){
                Path pathToFile = Paths.get(file.getAbsolutePath());
                this.colony = Colony.fromCSV(pathToFile);

                this.colony.iterate();
                this.colonyView.render(colony);

            }
        } catch (IOException | IllegalArgumentException e) {
            Text text =  new Text(e.getMessage());
            alert.getDialogPane().setContent(text);
            alert.showAndWait();
        }
    }

    private void changeSpeed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        if (nonNull(timeline)) {
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            Duration.millis(newValue.intValue()),
                            this::iterateColony
                    )
            );
            timeline.play();
        }
    }
}
