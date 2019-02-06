package UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import life.Colony;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.*;


public class ColonyController {

    private Colony colony;
    private ColonyView colonyView;
    private Timeline timeline;
    private Stage stage;
    private FileChooser fileChooser;

    @FXML
    private Slider speed = null;

    public ColonyController() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select colony file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }


    public ColonyController(Colony colony, ColonyView colonyView) {
        this();
        this.colonyView = colonyView;
        this.colony = colony;
    }

    @FXML
    public void initialize() {
        speed.valueProperty().addListener(this::changeSpeed);
    }

    public EventHandler<ActionEvent> frameListener() {
        return this::iterateColony;
    }

    private void iterateColony(ActionEvent event) {

        colony.iterate();
        colonyView.render(colony);
        //todo put on a separate thread
        event.consume();
    }


    public void startAnimation(Duration time) {
        if (isNull(colony) || isNull(colonyView) || isNull(stage))
            throw new NullPointerException("Either the colony or the colony view has not been set");

        timeline = new Timeline(new KeyFrame(time, frameListener()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void play(Event event) {
        if (nonNull(timeline))
            timeline.play();

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
    }

    public void setStage(Stage stage) {
        this.stage = requireNonNull(stage);
    }

    @FXML
    public void openFile(ActionEvent event) {
        pause(event);
        try {
            File file = fileChooser.showOpenDialog(stage);
            Path pathToFile = Paths.get(file.getAbsolutePath());
            this.colony = Colony.fromCSV(pathToFile);
            this.colony.iterate();
            this.colonyView.render(colony);
        } catch (IOException | IllegalArgumentException e) {
            //todo handle bad files
            e.printStackTrace();
        }
    }

    private void changeSpeed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        if (nonNull(timeline)) {
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(newValue.intValue()), frameListener()));
            timeline.play();
        }
    }
}
