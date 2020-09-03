package components.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import engine.Engine;
import engine.EngineImpl;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();

        // Load main fxml
        URL mainFXML = getClass().getResource("/components/main/sdm.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        // Wire up controller
        MainController mainController = loader.getController();
        Engine engine = new EngineImpl();
        mainController.setPrimaryStage(primaryStage);
        mainController.setEngine(engine);

        // Set stage:
        primaryStage.setTitle("Super Duper Market");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(Main.class);
    }
}
