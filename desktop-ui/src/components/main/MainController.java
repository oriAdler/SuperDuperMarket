package components.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.binding.Binding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import engine.Engine;

import java.io.File;

public class MainController {


    @FXML private Button openFileButton;
    @FXML private Button displayStoresButton;
    @FXML private Button displayItemsButton;
    @FXML private Button makeOrderButton;
    @FXML private Button showOrdersHistoryButton;
    @FXML private Button updateStoreItemsButton;
    @FXML private Button showMapButton;

    private SimpleBooleanProperty isFileLoaded;

    private Stage primaryStage;
    private Engine engine;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize(){
        displayStoresButton.disableProperty().bind(isFileLoaded.not());
        displayItemsButton.disableProperty().bind(isFileLoaded.not());
        makeOrderButton.disableProperty().bind(isFileLoaded.not());
        showOrdersHistoryButton.disableProperty().bind(isFileLoaded.not());
        updateStoreItemsButton.disableProperty().bind(isFileLoaded.not());
        showMapButton.disableProperty().bind(isFileLoaded.not());
    }

    @FXML
    public void displayItemsButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void displayStoresButtonAction(ActionEvent event) {

    }

    @FXML
    public void makeOrderButtonAction(ActionEvent event) {

    }

    @FXML
    void openFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select SDM file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try{
            engine.loadDataFromFile(absolutePath);
        }
        catch (Exception exception){

        }

        isFileLoaded.set(true);
    }

    @FXML
    public void showMapButtonAction(ActionEvent event) {

    }

    @FXML
    public void showOrdersHistoryButtonAction(ActionEvent event) {

    }

    @FXML
    public void updateStoreItemsButtonAction(ActionEvent event) {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
