package dev.fraco.minikeep;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MiniKeepMain extends javafx.application.Application {
    private static Scene scene;

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the JavaFX application by setting up the initial stage.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(MiniKeepMain.class.getResourceAsStream("images/logo.png")));

        scene = new Scene(loadFXML("login"), 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(MiniKeepMain.class.getResource("css/styles.css")).toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("MiniKeep");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the root FXML file for the scene.
     *
     * @param fxml The name of the FXML file.
     */
    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            scene.setRoot(root);
        } catch (IOException ioe) {
            handleException(ioe);
        }
    }

    /**
     * Loads an FXML file and returns the root object.
     *
     * @param fxml The name of the FXML file.
     * @return The root object of the loaded FXML file.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MiniKeepMain.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Handles exceptions by printing the error message to the standard error stream.
     *
     * @param exception The exception to handle.
     */
    public static void handleException(Exception exception) {
        System.err.println(exception.getMessage());
    }
}
