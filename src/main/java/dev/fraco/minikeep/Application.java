package dev.fraco.minikeep;

import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.User;
import dev.fraco.minikeep.logic.UserAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(Application.class.getResourceAsStream("logo.png")));
        scene = new Scene(loadFXML("login"), 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("css/styles.css")).toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("MiniKeep");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException ioe) {
            handleException(ioe);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void handleException(Exception exception) {
        System.out.println(exception.toString());
    }

    public static void main(String[] args) {
        launch();
    }
}