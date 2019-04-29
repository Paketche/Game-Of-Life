package valchanov.georgi.UI;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import valchanov.georgi.life.Colony;

import java.awt.*;
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
    private Alert alert;

    private Task<Colony> colonyTask;

    private Point mouseReference;

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
        alert.getDialogPane().paddingProperty().setValue(new Insets(10, 20, 10, 20));

        mouseReference = new Point();
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

    /**
     * Creates a new task to generate the new colony on a separate thread so that the application thread is not blocked.
     *
     * @return the constructed task
     */
    @Override
    protected Task<Colony> createTask() {
        return new Task<Colony>() {
            @Override
            protected Colony call() {
                colony.iterate();

                //run on main application thread
                Platform.runLater(() -> colonyView.render(colony));
                return colony;
            }
        };
    }

    /**
     * Initializes the event listeners of the  UI controls. This is used by the FXML loader
     */
    @FXML
    public void initialize() {
        speed.valueProperty().addListener(this::changeSpeed);
    }


    /**
     * Method that handle the event change
     *
     * @param event that caused the calling of this method
     */
    protected void onFrameChange(ActionEvent event) {

        if (isNull(colonyTask) || colonyTask.isDone()) {
            this.colonyTask = this.createTask();
            this.executeTask(colonyTask);
        }

        event.consume();
    }

    /**
     * Starts the colony animation. Each generation is displayed for the duration that is provided as an argument
     *
     * @param duration the duration that each colony generation will be displayed
     * @throws NullPointerException if either the colony, the colony view or the stage has not been set
     */
    public void startAnimation(Duration duration) throws NullPointerException {
        if (isNull(colony) || isNull(colonyView) || isNull(stage))
            throw new NullPointerException("Either the colony, the colony view or the stage has not been set");

        timeline = new Timeline(new KeyFrame(duration, this::onFrameChange));
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

        onFrameChange(event);
    }

    @FXML
    public void center(ActionEvent event) {
        colonyView.resetOffset();
        event.consume();
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

    @FXML
    public void openFile(ActionEvent event) {
        pause(event);
        try {
            File file = fileChooser.showOpenDialog(stage);
            if (nonNull(file)) {

                Path pathToFile = Paths.get(file.getAbsolutePath());

                this.colony = Colony.fromCSV(pathToFile);

                this.colony.iterate();
                this.colonyView.render(colony);

            }
        } catch (IOException | IllegalArgumentException e) {
            Text text = new Text(e.getMessage());
            alert.getDialogPane().setContent(text);
            alert.showAndWait();
        }
    }


    protected void onDragStarted(MouseEvent event) {
        //set a reference point from which the drag difference is going to be calculated
        this.colonyView.setCursor(Cursor.CLOSED_HAND);
        mouseReference.setLocation(
                event.getSceneX(),
                event.getSceneY()
        );

    }

    protected void onMouseDragged(MouseEvent event) {
        //shift offset based on drag difference
        colonyView.shiftOffsets(
                event.getSceneX() - mouseReference.getX(),
                event.getSceneY() - mouseReference.getY()
        );

        //reset the reference for the next drag event
        mouseReference.setLocation(
                event.getSceneX(),
                event.getSceneY()
        );
        colonyView.render(colony);
    }

    public void setColony(Colony colony) {
        this.colony = requireNonNull(colony);
    }

    /**
     * Sets the colony view that this controller is going be controlling
     *
     * @param colonyView view that will be controlled
     */
    public void setColonyView(ColonyView colonyView) {
        this.colonyView = requireNonNull(colonyView);
        this.colonyView.setCursor(Cursor.OPEN_HAND);

        this.colonyView.onMousePressedProperty().setValue(this::onDragStarted);
        this.colonyView.onMouseDraggedProperty().setValue(this::onMouseDragged);
        this.colonyView.onDragDoneProperty().setValue(e -> colonyView.setCursor(Cursor.OPEN_HAND));
    }

    /**
     * Sets the window object that this controller will controlling
     *
     * @param stage window that is to be controlled
     */
    public void setStage(Stage stage) {
        this.stage = requireNonNull(stage);
    }

    /**
     * Method used as an event handler for the slider bar.
     */
    private void changeSpeed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        if (nonNull(timeline)) {
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(
                    new KeyFrame(
                            Duration.millis(newValue.intValue()),
                            this::onFrameChange
                    )
            );
            timeline.play();
        }
    }
}
