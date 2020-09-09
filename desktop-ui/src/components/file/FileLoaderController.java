package components.file;

import components.main.MainController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FileLoaderController implements Initializable {

    // View:
    @FXML private Button loadFileButton;
    @FXML private Label filePathLabel;
    @FXML private Label taskMessageLabel;
    @FXML private Label progressPercentLabel;
    @FXML private ProgressBar taskProgressBar;
    @FXML private ImageView imageView;
    @FXML private Label finalMessageLabel;

    SimpleStringProperty filePath;
    SimpleStringProperty taskMessage;
    SimpleStringProperty finalMessage;

    private Task<Boolean> currentRunningTask;
    Engine engine;
    File selectedFile;

    MainController mainController;

    public FileLoaderController(){
        filePath = new SimpleStringProperty();
        taskMessage = new SimpleStringProperty();
        finalMessage = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filePathLabel.textProperty().bind(filePath);
        taskMessageLabel.textProperty().bind(taskMessage);
        finalMessageLabel.textProperty().bind(finalMessage);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public Button getLoadFileButton() {
        return loadFileButton;
    }

    @FXML
    void loadFileButtonOnAction(ActionEvent event) {
        String absolutePath = selectedFile.getAbsolutePath();

        currentRunningTask = new FileLoaderTask(absolutePath, finalMessage::set,
                mainController.isFileLoadedProperty()::set, engine);

        // Bind task to UI components:
        taskMessageLabel.textProperty().bind(currentRunningTask.messageProperty());
        taskProgressBar.progressProperty().bind(currentRunningTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format("%.0f",
                                Bindings.multiply(
                        currentRunningTask.progressProperty(), 100)), " %"));

        new Thread(currentRunningTask).start();
        loadFileButton.setDisable(true);
    }
}
