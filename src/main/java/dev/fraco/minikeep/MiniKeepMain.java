package dev.fraco.minikeep;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class MiniKeepMain extends javafx.application.Application {
    private static Scene scene;

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


    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            scene.setRoot(root);
        } catch (IOException ioe) {
            handleException(ioe);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MiniKeepMain.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void handleException(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Irrecuperable");
        alert.setHeaderText("Se ha producido la siguiente excepción");

        String mensaje = exception.getMessage();
        String causa = exception.getCause() != null ? exception.getCause().toString() : "";
        String stackTrace = Arrays.toString(exception.getStackTrace());

        String detalles = "Mensaje: " + mensaje + "\n\n"
                + "Causa: " + causa + "\n\n"
                + "Traza de la pila:\n" + stackTrace;

        TextArea textArea = new TextArea(detalles);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}