package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.Priority;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateNote implements Initializable {
    public TextField titleInput;
    public TextField tagsInput;
    public TextArea bodyInput;
    public ColorPicker colorInput;
    public TextField collaboratorsInput;
    public ChoiceBox<Priority> priorityCombo;

    public void backHandler(ActionEvent actionEvent) {
        try {
            Application.setRoot("workspace");
        } catch (IOException e) {
            Application.handleException(e);
        }
    }

    public void addHandler(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));
    }
}
